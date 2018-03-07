/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tsz.afinal;

import java.lang.reflect.Field;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import net.tsz.afinal.annotation.view.EventListener;
import net.tsz.afinal.annotation.view.Select;
import net.tsz.afinal.annotation.view.ViewInject;

public abstract class FinalActivity extends Activity {

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initInjectedView(this);
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        initInjectedView(this);
    }

    public void setContentView(View view) {
        super.setContentView(view);
        initInjectedView(this);
    }

    public static void initInjectedView(Activity activity) {
        initInjectedView(activity, activity.getWindow().getDecorView());
    }

    public static void initInjectedView(Object injectedSource, View sourceView) {
        initInjectedWithBaseView(injectedSource, sourceView, injectedSource.getClass());
    }

    public static void initInjectedWithBaseView(Object injectedSource, View sourceView, Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (shouldInject(superClass))
            initInjectedWithBaseView(injectedSource, sourceView, superClass);
        inject(injectedSource, sourceView, clazz);
    }

    private static boolean shouldInject(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        return !(packageName.startsWith("java")
                || packageName.startsWith("android") || packageName.startsWith("com.android") || packageName.startsWith("com.google"));
    }

    private static void inject(Object injectedSource, View sourceView, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    ViewInject viewInject = field.getAnnotation(ViewInject.class);
                    View valueView = null;
                    Object valueObject = null;
                    if (viewInject != null) {
                        int viewId = viewInject.id();
                        valueView = sourceView.findViewById(viewId);
                        valueObject = field.get(injectedSource);
                        if (valueObject != null && valueObject == valueView)
                            continue;
                        field.set(injectedSource, valueView);

                        setListener(injectedSource, field, viewInject.click(), Method.Click);
                        setListener(injectedSource, field, viewInject.longClick(), Method.LongClick);
                        setListener(injectedSource, field, viewInject.itemClick(), Method.ItemClick);
                        setListener(injectedSource, field, viewInject.itemLongClick(), Method.itemLongClick);
                        setListener(injectedSource, field, viewInject.touch(), Method.Touch);

                        Select select = viewInject.select();
                        if (!TextUtils.isEmpty(select.selected())) {
                            setViewSelectListener(injectedSource, field, select.selected(), select.noSelected());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setViewSelectListener(Object injectedSource, Field field, String select, String noSelect) throws Exception {
        Object obj = field.get(injectedSource);
        if (obj instanceof View) {
            ((AbsListView) obj).setOnItemSelectedListener(new EventListener(injectedSource).select(select).noSelect(noSelect));
        }
    }

    private static void setListener(Object injectedSource, Field field, String methodName, Method method) throws Exception {
        if (methodName == null || methodName.trim().length() == 0)
            return;

        Object obj = field.get(injectedSource);

        switch (method) {
        case Click:
            if (obj instanceof View) {
                OnClickListener l = new EventListener(injectedSource).click(methodName);
                ((View) obj).setOnClickListener(l);
            }
            break;
        case ItemClick:
            if (obj instanceof AbsListView) {
                ((AbsListView) obj).setOnItemClickListener(new EventListener(injectedSource).itemClick(methodName));
            }
            break;
        case LongClick:
            if (obj instanceof View) {
                ((View) obj).setOnLongClickListener(new EventListener(injectedSource).longClick(methodName));
            }
            break;
        case itemLongClick:
            if (obj instanceof AbsListView) {
                ((AbsListView) obj).setOnItemLongClickListener(new EventListener(injectedSource).itemLongClick(methodName));
            }
            break;
        case Touch:
            if (obj instanceof View) {
                ((View) obj).setOnTouchListener(new EventListener(injectedSource).onTouch(methodName));
            }
            break;
        default:
            break;
        }
    }

    public enum Method {
        Click, LongClick, ItemClick, itemLongClick, Touch
    }

}

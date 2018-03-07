package cn.edu.tsu.se.mylife.net;

import android.os.Handler;
import android.os.Message;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cn.edu.tsu.se.mylife.model.CloudBookList;
import cn.edu.tsu.se.mylife.model.DialogList;
import cn.edu.tsu.se.mylife.model.GoalList;
import cn.edu.tsu.se.mylife.model.HomePageProfile;
import cn.edu.tsu.se.mylife.model.Token;
import cn.edu.tsu.se.mylife.model.TripList;
import cn.edu.tsu.se.mylife.util.LogUtil;
import cn.edu.tsu.se.mylife.util.SessionUtil;

public class Request {

	public static final String WEB_URL = Config.WEB_PROTOCOL + ":" + "//" + Config.IP + ":" + Config.WEB_PORT + "/mylife/";
	private static FinalHttp finalHttp;
	public static String sessionId = null;
	final static Token responseToken = new Token();

	static {
		finalHttp = FinalHttp.getInstance();
		finalHttp.configUserAgent("mobile_client");
		if (sessionId != null)
			finalHttp.addHeader("Cookie", "JSESSIONID=" + sessionId);
		finalHttp.configTimeout(20 * 1000);
	}

	public static Token loginOrRegister(boolean isLogin, final Handler handler, String uuid, String username,
			String password) {
		final Message msg = Message.obtain();
		AjaxParams params = new AjaxParams();
		params.put("uuid", uuid);
		params.put("username", username);
		params.put("password", password);
		final String url;
		if (isLogin)
			url = WEB_URL + ServerPath.PATH_LOGIN;
		else
			url = WEB_URL + ServerPath.PATH_REGISTER;
		finalHttp.post(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
					if (sessionId == null)
						sessionId = SessionUtil.getSessionId(finalHttp);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + " from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return responseToken;
	}

	public static void logout() {
		final String url = WEB_URL + ServerPath.PATH_LOGOUT;
		finalHttp.get(url, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				if (sessionId == null)
					sessionId = SessionUtil.getSessionId(finalHttp);
				LogUtil.d("onSuccess : " + t + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static HomePageProfile getHomePageProfile(final Handler handler) {
		final Message msg = Message.obtain();
		final HomePageProfile homePageProfile = new HomePageProfile();
		final String url = WEB_URL;
		finalHttp.get(url, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
				try {
					homePageProfile.parse(new JSONObject(json));
					msg.what = homePageProfile.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return homePageProfile;
	}

	public static Token autoLogin(final Handler handler, String uuid, String token) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_AUTO_LOGIN;
		AjaxParams params = new AjaxParams();
		params.put("uuid", uuid);
		params.put("token", token);
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				if (sessionId == null)
					sessionId = SessionUtil.getSessionId(finalHttp);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return responseToken;
	}

	public static void editProfile(final Handler handler, String nickname, String signature) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_EDIT_PROFILE;
		AjaxParams params = new AjaxParams();
		params.put("nickname", nickname);
		params.put("signature", signature);
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static DialogList getDialog(final Handler handler, int pageNumber) {
		final Message msg = Message.obtain();
		final DialogList dialogList = new DialogList();
		final String url = WEB_URL + ServerPath.PATH_DIALOG;
		AjaxParams params = new AjaxParams();
		params.put("page_num", String.valueOf(pageNumber));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
				try {
					dialogList.parse(new JSONObject(json));
					msg.what = dialogList.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return dialogList;
	}

	public static void addDialog(final Handler handler, String title, String content, int mood) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_DIALOG_ADD;
		AjaxParams params = new AjaxParams();
		params.put("title", title);
		params.put("content", content);
		params.put("mood", String.valueOf(mood));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void editDialog(final Handler handler, int id, String title, String content, int mood) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_DIALOG_EDIT;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		params.put("title", title);
		params.put("content", content);
		params.put("mood", String.valueOf(mood));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void deleteDialog(int id) {
		final String url = WEB_URL + ServerPath.PATH_DIALOG_DELETE;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		finalHttp.get(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				LogUtil.d("onSuccess : " + t + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static GoalList getGoal(final Handler handler, int pageNumber) {
		final Message msg = Message.obtain();
		final GoalList goalList = new GoalList();
		final String url = WEB_URL + ServerPath.PATH_GOAL;
		AjaxParams params = new AjaxParams();
		params.put("page_num", String.valueOf(pageNumber));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
				try {
					goalList.parse(new JSONObject(json));
					msg.what = goalList.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return goalList;
	}

	public static void addGoal(final Handler handler, String title, String content, String time) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_GOAL_ADD;
		AjaxParams params = new AjaxParams();
		params.put("title", title);
		params.put("content", content);
		params.put("time", time);
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void editGoal(final Handler handler, int id, String title, String content, String time) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_GOAL_EDIT;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		params.put("title", title);
		params.put("content", content);
		params.put("time", time);
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void deleteGoal(int id) {
		final String url = WEB_URL + ServerPath.PATH_GOAL_DELETE;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		finalHttp.get(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				LogUtil.d("onSuccess : " + t + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static TripList getTrip(final Handler handler, int pageNumber) {
		final Message msg = Message.obtain();
		final TripList tripList = new TripList();
		final String url = WEB_URL + ServerPath.PATH_TRIP;
		AjaxParams params = new AjaxParams();
		params.put("page_num", String.valueOf(pageNumber));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
				try {
					tripList.parse(new JSONObject(json));
					msg.what = tripList.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return tripList;
	}

	public static void addTrip(final Handler handler, String content, String time, boolean isNotice) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_TRIP_ADD;
		AjaxParams params = new AjaxParams();
		params.put("content", content);
		params.put("time", time);
		params.put("notice", String.valueOf(isNotice));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void deleteTrip(final Handler handler, int id) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_TRIP_DELETE;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void checkMD5(final Handler handler, String md5) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_CHECK_MD5;
		AjaxParams params = new AjaxParams();
		params.put("md5", md5);
		finalHttp.post(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static CloudBookList getCloudPDF(final Handler handler) {
		final CloudBookList cloudBooks = new CloudBookList();
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_PDF;
		finalHttp.post(url, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					cloudBooks.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
		return cloudBooks;
	}

	public static void uploadPDF(final Handler handler, File file) throws FileNotFoundException {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_PDF_UPLOAD;
		AjaxParams params = new AjaxParams();
		params.put("pdf", file);
		finalHttp.post(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void deletePDF(int id) {
		final String url = WEB_URL + ServerPath.PATH_PDF_DELETE;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			public void onSuccess(String json) {
				super.onSuccess(json);
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void readPDF(final Handler handler, int id, int currentPage) {
		final Message msg = Message.obtain();
		AjaxParams params = new AjaxParams();
		String urlTemp = WEB_URL + ServerPath.PATH_PDF_MORE;
		if (currentPage == 1) {
			urlTemp = WEB_URL + ServerPath.PATH_PDF_PREVIEW;
			params.put("id", String.valueOf(id));
		}
		final String url = urlTemp;
		params.put("start", String.valueOf(currentPage));
		finalHttp.get(url, params, new AjaxCallBack<Object>() {
			public void onSuccess(Object t) {
				super.onSuccess(t);
				msg.obj = t;
				msg.what = 1;
				handler.sendMessage(msg);
				LogUtil.d("onSuccess : " + t + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}

	public static void previewPDF(final Handler handler, int id) {
		final Message msg = Message.obtain();
		final String url = WEB_URL + ServerPath.PATH_PDF_PREVIEW;
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		params.put("start", String.valueOf(1));
		finalHttp.get(url, params, new AjaxCallBack<String>() {
			public void onSuccess(String json) {
				super.onSuccess(json);
				try {
					responseToken.parse(new JSONObject(json));
					msg.what = responseToken.getCode();
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.d("onSuccess : " + json + "  from url : " + url);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				LogUtil.d("onFailure status : " + errorNo + " message : " + strMsg + " from url : " + url);
			}
		});
	}
}

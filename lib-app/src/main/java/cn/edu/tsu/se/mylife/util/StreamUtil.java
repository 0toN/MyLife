package cn.edu.tsu.se.mylife.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

public class StreamUtil {

	public static String stream2String(InputStream inputStream) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte buffer[] = new byte[1024];
		int temp = -1;
		Log.d("streamBOS", bos.toString());
		try {
			while ((temp = inputStream.read(buffer)) == -1) {
				bos.write(buffer, 0, temp);
				return bos.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

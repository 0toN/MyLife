package cn.edu.tsu.se.mylife.helper;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfoHelper {
	private TelephonyManager telephonemanager;
	private String IMSI;
	private Context context;

	/**
	 * 获取手机国际识别码IMEI
	 */
	public PhoneInfoHelper(Context context) {
		this.context = context;
		telephonemanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public String getDeviceId() {
		return telephonemanager.getDeviceId();
	}

	/**
	 * 获取手机号码
	 */
	public String getNativePhoneNumber() {

		String nativephonenumber = null;
		nativephonenumber = telephonemanager.getLine1Number();

		return nativephonenumber;
	}

	/**
	 * 获取手机服务商信息
	 * 
	 */
	public String getProvidersName() {
		String providerName = null;
		try {
			IMSI = telephonemanager.getSubscriberId();
			// IMSI前面三位460是国家号码，其次的两位是运营商代号，00、02是中国移动，01是联通，03是电信。
			System.out.print("IMSI是：" + IMSI);
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				providerName = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				providerName = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				providerName = "中国电信";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return providerName;

	}

	/**
	 * 获取手机信息
	 */
	public String getPhoneInfo() {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();

		sb.append("\nDeviceID(IMEI)" + tm.getDeviceId());
		sb.append("\nDeviceSoftwareVersion:" + tm.getDeviceSoftwareVersion());
		sb.append("\ngetLine1Number:" + tm.getLine1Number());
		sb.append("\nNetworkCountryIso:" + tm.getNetworkCountryIso());
		sb.append("\nNetworkOperator:" + tm.getNetworkOperator());
		sb.append("\nNetworkOperatorName:" + tm.getNetworkOperatorName());
		sb.append("\nNetworkType:" + tm.getNetworkType());
		sb.append("\nPhoneType:" + tm.getPhoneType());
		sb.append("\nSimCountryIso:" + tm.getSimCountryIso());
		sb.append("\nSimOperator:" + tm.getSimOperator());
		sb.append("\nSimOperatorName:" + tm.getSimOperatorName());
		sb.append("\nSimSerialNumber:" + tm.getSimSerialNumber());
		sb.append("\ngetSimState:" + tm.getSimState());
		sb.append("\nSubscriberId:" + tm.getSubscriberId());
		sb.append("\nVoiceMailNumber:" + tm.getVoiceMailNumber());

		return sb.toString();
	}
}

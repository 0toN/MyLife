package cn.edu.tsu.se.mylife.util;

public class PhoneParameterUtil {
  public static  int ScreenX=0;
  public static int ScreenY=0;
  public static int image_width=0;
  public static int image_height=0;
  public static String sessionId=null;
  public static int currentPdfPage=0;
  public static void proportionScale(int width,int height){
	    ScreenY=(ScreenX-width)*height/width+height;
  }
}

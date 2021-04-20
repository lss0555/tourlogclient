package com.example.tourlog.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 缓存工具类
 */
public class CaCheUtils {

	/**
	 * 保存用户id
	 * @param context
	 * @param UserId
	 */
	public  static void saveId(Context context, String UserId) {
		SharedPreferences sp = context.getSharedPreferences("Id", Context.MODE_PRIVATE);
		sp.edit().putString("Id", UserId).commit();
	}
	public static String GetId(Context context) {
		SharedPreferences sp = context.getSharedPreferences("Id", Context.MODE_PRIVATE);
		return sp.getString("Id",null);
	}
	public  static void savePassword(Context context, String Password) {
		SharedPreferences sp = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
		sp.edit().putString("Password", Password).commit();
	}
	public static String GetPassword(Context context) {
		SharedPreferences sp = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
		return sp.getString("Password","");
	}

	public  static void saveUser(Context context, String User) {
		SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
		sp.edit().putString("User", User).commit();
	}
	public static String GetUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
		return sp.getString("User","");
	}

	/**
	 * @param context 是否登录
	 * @param IsLogin
	 */
	public  static void saveIsLogin(Context context, boolean IsLogin) {
		SharedPreferences sp = context.getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
		sp.edit().putBoolean("IsLogin", IsLogin).commit();
	}
	public static boolean IsLogin(Context context) {
		SharedPreferences sp = context.getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
		boolean IsLogin = sp.getBoolean("IsLogin", false);
		return IsLogin;
	}

}

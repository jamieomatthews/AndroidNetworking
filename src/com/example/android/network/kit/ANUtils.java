package com.example.android.network.kit;

import android.content.Context;
import android.content.SharedPreferences;

public class ANUtils {
	Context context;
	public ANUtils(Context context){
		this.context = context;
	}
	
	public Boolean hasUsername(){
		SharedPreferences settings = context.getSharedPreferences("ANUtils",0);
		String device_id = settings.getString("username","-1");
		return !device_id.equals("-1");
	}
	public String getUsername(){
		SharedPreferences settings = context.getSharedPreferences("ANUtils",0);
		String id = settings.getString("username","-1");
		return id;
	}
	public String getPassword(){
		SharedPreferences settings = context.getSharedPreferences("ANUtils",0);
		String token = settings.getString("password","-1");
		return token;
	}
	public void setLogin(String username, String password){
		SharedPreferences settings = context.getSharedPreferences("ANUtils",0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username);
		editor.putString("password", username);
		editor.commit();
	}
}

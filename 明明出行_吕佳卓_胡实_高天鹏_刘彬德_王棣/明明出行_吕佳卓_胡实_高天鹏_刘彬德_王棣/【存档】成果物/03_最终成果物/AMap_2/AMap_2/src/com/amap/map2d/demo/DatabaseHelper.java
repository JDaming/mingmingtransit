package com.amap.map2d.demo;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version){
		super(context,name,factory,version);
		this.getWritableDatabase();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql1 = "create table user(phonenumber varchar(11) primary key,username varchar(100) not null,password varchar(20),gender varchar(4));";
		String sql2 = "create table address(id integer primary key autoincrement,addressname varchar(200), long REAL(6), lat REAL(6),phone varchar(11));";
		
		
		db.execSQL(sql1);
		db.execSQL(sql2);
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		// TODO Auto-generated method stub
		
	}
	public boolean addUser(String phone, String password, String username, String gender){
		try{
			ContentValues new_user = new ContentValues();
			new_user.put("phonenumber",phone);
			new_user.put("password", password);
			new_user.put("username", username);
			new_user.put("gender", gender);
			this.getWritableDatabase().insert("user", null, new_user);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	public void close(){
		this.getWritableDatabase().close();
	}
	
}

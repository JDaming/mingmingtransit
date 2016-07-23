package com.amap.map2d.demo;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText login_password;
	private EditText login_phone;
	private boolean Login_status;
	private String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setActionBarLayout(R.layout.other_actionbar);
		try {
			Intent _int = getIntent();
			city = _int.getStringExtra("main_city");
		} catch (Exception e) {
			// TODO: handle exception
			city = "哈尔滨";
		}
		login_phone = (EditText)findViewById(R.id.login_phone);
		login_password = (EditText)findViewById(R.id.login_password);
		Button login_btn = (Button)findViewById(R.id.login_btn);
		
		login_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkUser();
			}
		});
		//跳转至注册界面
		TextView signup_text = (TextView)findViewById(R.id.singup_text);
		signup_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent login_to_register = new Intent();
				login_to_register.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(login_to_register);
				
			}
		});
	}
	
	private void setActionBarLayout(int otherActionbar) {
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
    	if (actionBar!=null){
    		actionBar.setDisplayShowHomeEnabled(false);
    		actionBar.setDisplayHomeAsUpEnabled(false);
    		actionBar.setDisplayShowCustomEnabled(true);
    		actionBar.setDisplayShowTitleEnabled(false);
    		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View v =inflator.inflate(otherActionbar,null);
    		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
    		actionBar.setCustomView(v,layout);
    	}
    	ImageView back = (ImageView)findViewById(R.id.back);
    	TextView page_title = (TextView)findViewById(R.id.page_title);
    	page_title.setText("登录");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void checkUser() {
		// TODO Auto-generated method stub
		DatabaseHelper database = new DatabaseHelper(LoginActivity.this, "userDB", null, 1);
		String sql = "select * from user where phonenumber = ?";
		Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{login_phone.getText().toString()});
		if (cursor.moveToFirst()){
			if (login_password.getText().toString().equals(cursor.getString(cursor.getColumnIndex("password")))){
				Toast.makeText(this, "登录成功", 5000).show();
				Login_status = true;
				String phone = login_phone.getText().toString();
				Intent _int = new Intent();
				_int.putExtra("main_city", city);
				_int.putExtra("Login_status", Login_status);
				_int.putExtra("Login_phone", phone);
				setResult(101, _int);
				finish();
				
			}else
			{
				Toast.makeText(this, "用户名或密码错误", 5000).show();
			}
		}else{
			Toast.makeText(this, "用户名不存在", 5000).show();
		}
		database.close();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}


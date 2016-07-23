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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText register_phone ;
	private EditText register_password_1 ;
	private EditText register_password_2;
	private EditText register_nickname;
	private RadioGroup register_gender;
	private RadioButton checked_register_gender;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setActionBarLayout(R.layout.other_actionbar);
		Button signup_btn = (Button)findViewById(R.id.signup_btn);
		register_phone = (EditText)findViewById(R.id.register_phone);
		register_password_1 = (EditText)findViewById(R.id.register_password_1);
		register_password_2 = (EditText)findViewById(R.id.register_password_2);
		register_nickname = (EditText)findViewById(R.id.register_nickname);
		register_gender = (RadioGroup)findViewById(R.id.register_gender);
		checked_register_gender = (RadioButton)findViewById(register_gender.getCheckedRadioButtonId());
		register_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedID) {
				// TODO Auto-generated method stub
				checked_register_gender = (RadioButton)register_gender.findViewById(checkedID);
			}
		});
		
		signup_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setUser();
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
    	page_title.setText("注册");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private void setUser() {
		// TODO Auto-generated method stub
		DatabaseHelper database = new DatabaseHelper(RegisterActivity.this, "userDB", null, 1);
		if (register_phone.getText().toString().length()<=0 || register_password_1.getText().toString().length()<=0 ||register_password_2.getText().toString().length()<=0){
			Toast.makeText(this, "手机号码或密码不能为空", 5000).show();
			return;
		}
		if (register_nickname.getText().toString().length()<=0){
			Toast.makeText(this, "请输入您的昵称", 5000).show();
			return;
		}
		if (register_phone.getText().toString().length()>11){
			Toast.makeText(this, "请检查你的手机号码是否有误", 5000).show();
			return;
		}
		if (!register_password_1.getText().toString().equals(register_password_2.getText().toString())){
			Toast.makeText(this,"两次输入的密码不相同",5000).show();
			return;
		}
		if (register_phone.getText().toString().length()>0){
			String sql = "select *from user where phonenumber = ?";
			Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{register_phone.getText().toString()});
			if (cursor.moveToFirst()){
				Toast.makeText(this, "该手机号已被注册", 5000).show();
				return;
			}
		}

		if(database.addUser(register_phone.getText().toString(), register_password_1.getText().toString(), register_nickname.getText().toString(), checked_register_gender.getText().toString())){
			Toast.makeText(this, "注册成功", 5000).show();
			Intent _int = new Intent();
			_int.setClass(this, MainActivity.class);
			startActivity(_int);
			
			
		}else
		{
			Toast.makeText(this, "未知错误，注册失败", 5000).show();
			
		}
		database.close();
	
			
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}


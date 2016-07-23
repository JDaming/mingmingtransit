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

public class UserInfoActivity extends Activity {
	private String login_phone;
	private String name;
	private String gender;
	private EditText nick_name;
	private RadioGroup user_center_gender;
	private RadioButton checked_user_center_gender;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setActionBarLayout(R.layout.other_actionbar);
		Intent _int = getIntent();
		login_phone = _int.getStringExtra("login_phone");
		DatabaseHelper database = new DatabaseHelper(UserInfoActivity.this, "userDB", null, 1);
		String sql = "select * from user where phonenumber = ?";
		Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{login_phone});
		if (cursor.moveToFirst()){
			 name = cursor.getString(cursor.getColumnIndex("username"));
			 gender = cursor.getString(cursor.getColumnIndex("gender"));
			 
		}
		
		
		nick_name = (EditText)findViewById(R.id.user_center_nickname);
		user_center_gender = (RadioGroup)findViewById(R.id.user_center_gender);
		Button modify = (Button)findViewById(R.id.modify);
		nick_name.setText(name);
		
		if(gender.equals("帅哥")){
			((RadioButton)user_center_gender.findViewById(R.id.user_center_male)).setChecked(true);
			
		}else{
			((RadioButton)user_center_gender.findViewById(R.id.user_center_female)).setChecked(true);
		}
		user_center_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkID) {
				// TODO Auto-generated method stub
				checked_user_center_gender = (RadioButton)user_center_gender.findViewById(checkID);
			}
		});
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				DatabaseHelper database = new DatabaseHelper(UserInfoActivity.this, "userDB", null, 1);
				String sql = "update user set username=?,gender=? where phonenumber=?";
				database.getWritableDatabase().execSQL(sql, new String[]{nick_name.getText().toString(),checked_user_center_gender.getText().toString(),login_phone});
				database.close();
				Intent _int = new Intent();
				_int.putExtra("login_phone", login_phone);
				_int.setClass(UserInfoActivity.this, UserCenterActivity.class);
				startActivity(_int);
				finish();
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
    	page_title.setText("修改个人信息");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}

}


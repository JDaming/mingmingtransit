package com.amap.map2d.demo;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.map2d.demo.basic.EventsActivity;



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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserCenterActivity extends Activity {
	List <Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private String login_phone;
	private String name;
	private String city;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		setActionBarLayout(R.layout.other_actionbar);
		Intent _int = getIntent();
		login_phone = _int.getStringExtra("login_phone");
		city = _int.getStringExtra("main_city");
		DatabaseHelper database = new DatabaseHelper(UserCenterActivity.this, "userDB", null, 1);
		String sql = "select * from user where phonenumber = ?";
		Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{login_phone});
		if (cursor.moveToFirst()){
			 name = cursor.getString(cursor.getColumnIndex("username"));
		}
		ListView userCenter = (ListView)findViewById(R.id.user_center);
		Button logout = (Button)findViewById(R.id.logout);
		Map <String,Object> map = new HashMap<String,Object>();
		
		map.put("icon",R.drawable.user_center_personal_data);
		map.put("option", name);
		map.put("forward_arrow", R.drawable.arrow_right);
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("icon", R.drawable.user_center_address);
		map.put("option", "常用地址");
		map.put("forward_arrow",R.drawable.arrow_right);
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("icon", R.drawable.user_center_orders);
		map.put("option", "我的订单");
		map.put("forward_arrow", R.drawable.arrow_right);
		list.add(map);
		
		myAdapter adapter = new myAdapter();
		userCenter.setAdapter(adapter);
		userCenter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id){
				// TODO Auto-generated method stub
				if(list.get(position).get("option").equals(name)){
					Intent _int = new Intent();
					_int.setClass(UserCenterActivity.this, UserInfoActivity.class);
					_int.putExtra("login_phone", login_phone);
					
					startActivity(_int);
				}
				if (list.get(position).get("option").equals("常用地址")){
					Intent _int = new Intent();
					_int.putExtra("main_city", city);
					_int.putExtra("login_phone", login_phone);
					_int.setClass(UserCenterActivity.this, InsertAddressActivity.class);
					startActivity(_int);
				}
			}
		});
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.putExtra("Login_status", false);
				_int.setClass(UserCenterActivity.this, MainActivity.class);
				setResult(501, _int);
				finish();
			}
		});
		database.close();
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
    	page_title.setText("个人中心");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	public class userCenter_viewHolder{
		ImageView icon;
		TextView option;
		ImageView forward_arrow;
	}
	private class myAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			userCenter_viewHolder holder = null;
			if (convertView == null){
				holder = new userCenter_viewHolder();
				convertView = LayoutInflater.from(UserCenterActivity.this).inflate(R.layout.listviewmodel, null);
				
				holder.icon = (ImageView)convertView.findViewById(R.id.icon);
				holder.option = (TextView)convertView.findViewById(R.id.option);
				holder.forward_arrow = (ImageView)convertView.findViewById(R.id.forward_arrow);
				
				convertView.setTag(holder);
			}else{
				holder = (userCenter_viewHolder)convertView.getTag();
			}
			
			holder.icon.setBackgroundResource((Integer)list.get(position).get("icon"));
			holder.option.setText((String)list.get(position).get("option"));
			holder.forward_arrow.setBackgroundResource((Integer)list.get(position).get("forward_arrow"));
			
			return convertView;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_center, menu);
		return true;
	}

}


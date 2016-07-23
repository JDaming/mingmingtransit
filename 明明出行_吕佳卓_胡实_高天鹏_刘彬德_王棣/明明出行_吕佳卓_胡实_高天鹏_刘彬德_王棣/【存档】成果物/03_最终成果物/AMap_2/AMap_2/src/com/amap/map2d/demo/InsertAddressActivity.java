package com.amap.map2d.demo;


import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.map2d.demo.basic.EventsActivity;



import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class InsertAddressActivity extends Activity {
	private String city;
	List <Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private String login_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_address);
        // Show the Up button in the action bar.
        setActionBarLayout(R.layout.other_actionbar);
        
        Intent _int  = getIntent();
        city =  _int.getStringExtra("main_city");
        login_phone = _int.getStringExtra("login_phone");
        
        DatabaseHelper database = new DatabaseHelper(InsertAddressActivity.this, "userDB", null, 1);
		String sql = "select * from address where phone = ?";
		Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{login_phone});
		
		ListView user_address_ListView = (ListView)findViewById(R.id.user_common_address);
		Button insert_addressButton  = (Button)findViewById(R.id.insert_address);
		
		Map <String,Object> map = new HashMap<String,Object>();
		
		while(cursor.moveToNext()){
		   map = new HashMap<String,Object>();
			map.put("icon_star",R.drawable.user_center_my_line);
			map.put("address_id",cursor.getString(cursor.getColumnIndex("id")) );
			map.put("address_info", cursor.getString(cursor.getColumnIndex("addressname")));
			map.put("long", cursor.getDouble(cursor.getColumnIndex("long")));
			map.put("lat", cursor.getDouble(cursor.getColumnIndex("lat")));
			map.put("address_forward_arrow", R.drawable.arrow_right);
			list.add(map);
		}
		myAdapter adapter = new myAdapter();
		user_address_ListView.setAdapter(adapter);
		
		user_address_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.putExtra("flag", 1);
				_int.putExtra("main_city", city);
				
				_int.putExtra("addressID", list.get(position).get("address_id").toString());
				_int.putExtra("address",list.get(position).get("address_info").toString());
				_int.putExtra("long", list.get(position).get("long").toString());
				_int.putExtra("lat", list.get(position).get("lat").toString());
			
				_int.setClass(InsertAddressActivity.this, EventsActivity.class);
				
				startActivityForResult(_int, 15);
				
			}
		});
		 insert_addressButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.putExtra("flag", 0);
				_int.putExtra("main_city", city);
				_int.setClass(InsertAddressActivity.this, EventsActivity.class);
				startActivityForResult(_int, 10);
			}
		});
		 database.close();
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
			address_viewHolder holder = null;
			if (convertView == null){
				holder = new address_viewHolder();
				convertView = LayoutInflater.from(InsertAddressActivity.this).inflate(R.layout.item_insert_address_activity, null);
				
				holder.icon_star = (ImageView)convertView.findViewById(R.id.icon_star);
				holder.address_info = (TextView)convertView.findViewById(R.id.address_info);
				holder.address_forward_arrow = (ImageView)convertView.findViewById(R.id.address_forward_arrow);
				
				convertView.setTag(holder);
			}else{
				holder = (address_viewHolder)convertView.getTag();
			}
			
			holder.icon_star.setBackgroundResource((Integer)list.get(position).get("icon_star"));
			holder.address_info.setText((String)list.get(position).get("address_info"));
			holder.address_forward_arrow.setBackgroundResource((Integer)list.get(position).get("address_forward_arrow"));
			
			return convertView;
		}
		
	}
    public class address_viewHolder{
		ImageView icon_star;
		TextView address_info;
		ImageView address_forward_arrow;
	}
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
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
    	page_title.setText("添加常用地址");
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
        getMenuInflater().inflate(R.menu.insert_address, menu);
        return true;
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data){
    	if (requestCode==10 && resultCode==11){
    		String address = data.getStringExtra("address");
    		Double latitude= data.getDoubleExtra("lat", 0);
    		Double longitude = data.getDoubleExtra("long", 0);
    		
    		  DatabaseHelper database = new DatabaseHelper(InsertAddressActivity.this, "userDB", null, 1);
    		  String sql = "insert into address(addressname,long,lat,phone) value (?,?,?,?)";
    		  ContentValues values = new ContentValues();
    		  values.put("addressname",address);
    		  values.put("long", longitude);
    		  values.put("lat", latitude);
    		  values.put("phone", login_phone);
    		  database.getWritableDatabase().insert("address", null, values);
    		  database.close();
    		  Intent _int = new Intent();
    		  _int.setClass(InsertAddressActivity.this, InsertAddressActivity.class);
    		  _int.putExtra("main_city",city );
    		  _int.putExtra("login_phone", login_phone);
    		  startActivity(_int);
    		  finish();
    	}
    	if(requestCode==15 && resultCode==11){
    		String address = data.getStringExtra("address");
    		Double latitude= data.getDoubleExtra("lat", 0);
    		Double longitude = data.getDoubleExtra("long", 0);
    		int id = data.getIntExtra("id", 0);
    		  DatabaseHelper database = new DatabaseHelper(InsertAddressActivity.this, "userDB", null, 1);
    		 
    		  ContentValues values = new ContentValues();
    		  values.put("addressname",address);
    		  values.put("long", longitude);
    		  values.put("lat", latitude);
    		  values.put("phone", login_phone);

    		  String[] args = {String.valueOf(id)};
    		  database.getWritableDatabase().update("address", values, "id=?",args);
    		  database.close();
    		  Intent _int = new Intent();
    		  _int.setClass(InsertAddressActivity.this, InsertAddressActivity.class);
    		  _int.putExtra("main_city",city );
    		  _int.putExtra("login_phone", login_phone);
    		  startActivity(_int);
    		  finish();
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.amap.map2d.demo;



import com.amap.map2d.demo.poisearch.PoiAroundSearchActivity;
import com.amap.map2d.demo.poisearch.PoiKeywordSearchActivity;
import com.amap.map2d.demo.route.RouteActivity;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private String city = "哈尔滨";
    private boolean Login_status =false;
    private TextView main_city;
    private ImageView user_center;
    private String login_phone;
	@Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
     
        setActionBarLayout(R.layout.main_actionbar);
        
        ImageView route_query = (ImageView)findViewById(R.id.route_query);
        ImageView best_way_query = (ImageView)findViewById(R.id.main_navigation);
        ImageView around_bus = (ImageView)findViewById(R.id.around_bus);
        ImageView station_query = (ImageView)findViewById(R.id.station_query);
        
        route_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.setClass(MainActivity.this, RouteQueryActivity.class);
				_int.putExtra("main_city", city);
				
				startActivity(_int);
				
			}
		});
        around_bus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.setClass(MainActivity.this,PoiAroundSearchActivity.class);
				_int.putExtra("main_city", city);
				
				startActivity(_int);
			}
		});
        station_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.setClass(MainActivity.this,PoiKeywordSearchActivity.class );
				_int.putExtra("main_city", city);
				
				startActivity(_int);
			}
		});
        best_way_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.setClass(MainActivity.this, RouteActivity.class);
				_int.putExtra("main_city", city);
				_int.putExtra("login_status", Login_status);
				if(Login_status){
					_int.putExtra("login_phone", login_phone);
				}
				startActivity(_int);
			}
		});
        
    }

    
	@SuppressWarnings("deprecation")
	public void setActionBarLayout(int LayoutID){
    	ActionBar actionBar = getActionBar();
    	if (actionBar!=null){
    		actionBar.setDisplayShowHomeEnabled(false);
    		actionBar.setDisplayHomeAsUpEnabled(false);
    		actionBar.setDisplayShowCustomEnabled(true);
    		actionBar.setDisplayShowTitleEnabled(false);
    		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View v =inflator.inflate(LayoutID,null);
    		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
    		actionBar.setCustomView(v,layout);
    	}
    	TextView main_title= (TextView)findViewById(R.id.main_title);
    	main_city = (TextView)findViewById(R.id.main_city);
    	user_center = (ImageView)findViewById(R.id.user_center);
    	
    	main_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.setClass(MainActivity.this, CityActivity.class);
				startActivityForResult(_int, 1000);
			}
		});
    	main_city.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _int = new Intent();
				_int.setClass(MainActivity.this, CityActivity.class);
				startActivityForResult(_int, 1000);
			}
		});
    	user_center.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Login_status){
					Intent _int = new Intent();
					_int.setClass(MainActivity.this, UserCenterActivity.class);
					_int.putExtra("login_phone", login_phone);
					_int.putExtra("main_city", city);
					startActivityForResult(_int,500);
				}else{
					Intent _int = new Intent();
					_int.setClass(MainActivity.this, LoginActivity.class);
					_int.putExtra("main_city", city);
					startActivityForResult(_int,100);
				}
			}
		});
    	main_city.setText(city);
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data){
		//super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode==1001){
			
			city = data.getStringExtra("main_city");
			main_city = (TextView)findViewById(R.id.main_city);
			main_city.setText(city);
		}
		if (requestCode == 100 && resultCode == 101){
			city = data.getStringExtra("main_city");
			main_city = (TextView)findViewById(R.id.main_city);
			main_city.setText(city);
			Login_status = data.getBooleanExtra("Login_status",false);
			login_phone = data.getStringExtra("Login_phone");
			
			if(Login_status){
				user_center.setImageResource(R.drawable.bus_ptr_rotate);
			}
			
		}
		if (requestCode == 500 && resultCode == 501){
			Login_status = data.getBooleanExtra("Login_status",false);
			if(!Login_status){
				user_center.setImageResource(R.drawable.bus_ptr_rotate_loading);
			}
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_map, menu);
        
        return true;
    }
    
}

package com.amap.map2d.demo.route;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.map2d.demo.DatabaseHelper;
import com.amap.map2d.demo.InsertAddressActivity;
import com.amap.map2d.demo.R;
import com.amap.map2d.demo.geocoder.GeocoderActivity;
import com.amap.map2d.demo.poisearch.PoiKeywordSearchActivity;
import com.amap.map2d.demo.util.AMapUtil;
import com.amap.map2d.demo.util.ToastUtil;

public class RouteActivity extends Activity implements OnMapClickListener, 
OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener, TextWatcher, OnGeocodeSearchListener,InputtipsListener {
	private Context mContext;
	private RouteSearch mRouteSearch;
	private BusRouteResult mBusRouteResult;
	private LatLonPoint mStartPoint =new LatLonPoint(39.923271, 80.396034);//起点，
	private LatLonPoint mEndPoint = new LatLonPoint(39.984947, 116.494689);//终点，
	private String mCurrentCityName = "哈尔滨";
	private final int ROUTE_TYPE_BUS = 1;
	private  AutoCompleteTextView start_text ;
	private  AutoCompleteTextView end_text;
	private String start_keyword;
	private String end_keyword;
	private GeocodeSearch geocoderSearch;
	private int flag = 0;
	private String addressName;
	//private GeocodeAddress address;
	
	private LinearLayout mBusResultLayout;
	private RelativeLayout mBottomLayout;
	private TextView mRotueTimeDes, mRouteDetailDes;
	private Button mBus;
	private ListView mBusResultList;
	private ImageView start_down;
	private ImageView end_down;
	private Boolean login_status;
	private String login_phone;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private List<String> list ;	
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.route_activity);
		setActionBarLayout(R.layout.other_actionbar);
		Intent _int = getIntent();
		login_status = _int.getBooleanExtra("login_status", false);
		if(login_status){
			login_phone = _int.getStringExtra("login_phone");
		}
		mCurrentCityName = _int.getStringExtra("main_city");
		Toast.makeText(this, mCurrentCityName, 3000).show();
		mContext = this.getApplicationContext();
		init();
		
		
		start_down.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub					
					if(login_status){		
						DatabaseHelper database = new DatabaseHelper(RouteActivity.this, "userDB", null, 1);
						String sql = "select * from address where phone= ?";
						Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{login_phone});
						list = new ArrayList<String>();
						while(cursor.moveToNext()){
							list.add(cursor.getString(cursor.getColumnIndex("addressname")));							
						}			
						ShowDialog(start_text);
					}else{
						Toast.makeText(RouteActivity.this, "用户未登录", 1000).show();
					}
				}
			});
		end_down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub					
				if(login_status){		
					DatabaseHelper database = new DatabaseHelper(RouteActivity.this, "userDB", null, 1);
					String sql = "select * from address where phone= ?";
					Cursor cursor = database.getWritableDatabase().rawQuery(sql, new String[]{login_phone});
					list = new ArrayList<String>();
					while(cursor.moveToNext()){
						list.add(cursor.getString(cursor.getColumnIndex("addressname")));							
					}			
					ShowDialog(end_text);
				}else{
					Toast.makeText(RouteActivity.this, "用户未登录", 1000).show();
				}
			}
		});
	}

	public void ShowDialog(final AutoCompleteTextView textView) {
	     Context context = RouteActivity.this;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.formcommonlist, null);
            ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);
	     MyAdapter adapter = new MyAdapter(context, list);
            myListView.setAdapter(adapter);
            myListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
		    //在这里面就是执行点击后要进行的操作,这里只是做一个显示
			textView.setText(list.get(positon).toString());
		   if (alertDialog != null) {
			alertDialog.dismiss();
		   }
		}
            });
            builder = new AlertDialog.Builder(context);
            builder.setView(layout);
            alertDialog = builder.create();
            alertDialog.show();
	}
	private  class MyAdapter extends BaseAdapter {
		private List<String> mlist;
		private Context mContext;

		public MyAdapter(Context context, List<String> list) {
			this.mContext = context;
			mlist = new ArrayList<String>();
			this.mlist = list;
		}

		@Override
		public int getCount() {
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Person person = null;
			if (convertView == null) {
			    LayoutInflater inflater = LayoutInflater.from(mContext);
			    convertView = inflater.inflate(R.layout.rut_item,null);
		            person = new Person();
		            person.name = (TextView)convertView.findViewById(R.id.tv_name);
		            convertView.setTag(person);
				}else{
					person = (Person)convertView.getTag();
				}
				person.name.setText(list.get(position).toString());
				return convertView;
			}
		class Person{
		    TextView name;
		}
		
	   }

	/*
	 * 初始化AMap对象
	 */
	
	private void init() {

		mRouteSearch = new RouteSearch(this);
		mRouteSearch.setRouteSearchListener(this);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
		mRotueTimeDes = (TextView) findViewById(R.id.firstline);
		mRouteDetailDes = (TextView) findViewById(R.id.secondline);
		mBus = (Button)findViewById(R.id.button1);
		start_down = (ImageView)findViewById(R.id.start_down);
		end_down = (ImageView)findViewById(R.id.end_down);
		mBusResultList = (ListView) findViewById(R.id.bus_result_list);
		start_text = (AutoCompleteTextView)findViewById(R.id.start_point);
		end_text = (AutoCompleteTextView)findViewById(R.id.end_point);
		start_text.addTextChangedListener(this);// 添加文本输入框监听事件
		end_text.addTextChangedListener(this);
	}


	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void onBusClick(View view) {
		start_keyword = AMapUtil.checkEditText(start_text);
		getLatlon(start_keyword);
		end_keyword = AMapUtil.checkEditText(end_text);
		getLatlon(end_keyword);
		
		//searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
		
		mBusResultLayout.setVisibility(View.VISIBLE);
	}
	
	
	public void onDriveClick(View view) {

	}

	public void onWalkClick(View view) {

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
    	page_title.setText("换乘查询");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(int routeType, int mode) {
		if (mStartPoint == null) {
			ToastUtil.show(mContext, "起点未设置");
			return;
		}
		if (mEndPoint == null) {
			ToastUtil.show(mContext, "终点未设置");
		}
		showProgressDialog();
		LatLonPoint mStartPoint_1 = new LatLonPoint(mStartPoint.getLatitude(), mStartPoint.getLongitude());
		LatLonPoint mEndPoint_1 =  new LatLonPoint(mEndPoint.getLatitude(), mEndPoint.getLongitude());
		
		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				mStartPoint_1, mEndPoint_1);
		BusRouteQuery query = new BusRouteQuery(fromAndTo, mode,
		mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
		mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int errorCode) {
		dissmissProgressDialog();
		mBottomLayout.setVisibility(View.GONE);
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mBusRouteResult = result;
					BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
					mBusResultList.setAdapter(mBusResultListAdapter);		
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result);
				}
			} else {
				ToastUtil.show(mContext, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
		
	}
	

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    progDialog.setIndeterminate(false);
		    progDialog.setCancelable(true);
		    progDialog.setMessage("正在搜索");
		    progDialog.show();
	    }

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String newText = s.toString().trim();
    	if (!AMapUtil.IsEmptyOrNullString(newText)) {
		InputtipsQuery inputquery = new InputtipsQuery(newText,mCurrentCityName);
		Inputtips inputTips = new Inputtips(mContext, inputquery);
		inputTips.setInputtipsListener(this);
		inputTips.requestInputtipsAsyn();
    	}
	}


	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	public void getLatlon(String name) {
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		GeocodeQuery query = new GeocodeQuery(name, mCurrentCityName);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}
	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if(rCode== 1000){
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				if(flag == 0){
					mStartPoint =  address.getLatLonPoint();
					flag = 1;
				}else{
					mEndPoint = address.getLatLonPoint();
					flag = 2;
					searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
				}
				/*addressName = "经纬度值:" + mStartPoint + "\n位置描述:"
						+ address.getFormatAddress();
				ToastUtil.show(RouteActivity.this, addressName);*/
				}else {
					ToastUtil.show(RouteActivity.this, R.string.no_result);
				}
		}else {
			ToastUtil.showerror(RouteActivity.this, rCode);
		}
	}
	@Override
	public void onGetInputtips(List<Tip> tipList, int rCode) {
		if (rCode == 1000) {// 正确返回
			List<String> listString = new ArrayList<String>();
			for (int i = 0; i < tipList.size(); i++) {
				listString.add(tipList.get(i).getName());
			}
			ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.route_inputs, listString);
			start_text.setAdapter(aAdapter);
			start_text.setThreshold(1);
			end_text.setAdapter(aAdapter);
			end_text.setThreshold(1);
			aAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.showerror(RouteActivity.this, rCode);
		}
	}
}

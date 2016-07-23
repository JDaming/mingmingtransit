package com.amap.map2d.demo.basic;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.VisibleRegion;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.map2d.demo.InsertAddressActivity;
import com.amap.map2d.demo.R;
import com.amap.map2d.demo.geocoder.GeocoderActivity;
import com.amap.map2d.demo.util.AMapUtil;
import com.amap.map2d.demo.util.Constants;
import com.amap.map2d.demo.util.ToastUtil;

/**
 * AMapV1地图中简单介绍OnMapClickListener, OnMapLongClickListener,
 * OnCameraChangeListener三种监听器用法
 */

public class EventsActivity extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnCameraChangeListener, OnMarkerClickListener,InfoWindowAdapter,OnGeocodeSearchListener {
	private AMap aMap;
	private MapView mapView;
	private TextView mTapTextView;
	private TextView mCameraTextView;
	private GeocodeSearch geocoderSearch;
	private Marker marker_use;
	private Marker regeoMarker;
	private String addressName;
	private LatLonPoint latLonPoint;
	private String city ;
	private int id = -1;
	//private BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_activity);
		setActionBarLayout(R.layout.other_actionbar);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		
		Intent _int =  getIntent();
		int flag = _int.getIntExtra("flag", 0);
		city = _int.getStringExtra("main_city");
		
		//latLonPoint = new LatLonPoint(Double.parseDouble(_int.getStringExtra("lat")),Double.parseDouble(_int.getStringExtra("long")));
		//longitude = _int.getStringExtra("long");
		//latitude = _int.getStringExtra("lat");
		init();
		if(flag ==1){
			latLonPoint = new LatLonPoint(Double.parseDouble(_int.getStringExtra("lat")),Double.parseDouble(_int.getStringExtra("long")));
			
			addressName = _int.getStringExtra("address");
			
			id = Integer.parseInt(_int.getStringExtra("addressID"));
			aMap.clear();
			marker_use = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()))
					.title(addressName).icon(
					BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed))));
			//marker_use.getSnippet();
			marker_use.showInfoWindow();
		}
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * amap添加一些事件监听器
	 */
	private void setUpMap() {
		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
		aMap.setOnMarkerClickListener(this);
		aMap.setInfoWindowAdapter(this);
		
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 对单击地图事件回调
	 */
	@Override
	public void onMapClick(LatLng point) {
		//mTapTextView.setText("tapped, point=" + point +marker_use.getSnippet());
		aMap.clear();
		latLonPoint = new LatLonPoint(point.latitude, point.longitude);
		getAddress(latLonPoint);
		
		
	}

	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng point) {
		//mTapTextView.setText("long pressed, point=" + point);
	}

	/**
	 * 对正在移动地图事件回调
	 */
	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		//mCameraTextView.setText("onCameraChange:" + cameraPosition.toString());
	}

	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		marker.showInfoWindow();
		return false;
	}
	
	@Override
	public View getInfoWindow(Marker marker) {
		View view = getLayoutInflater().inflate(R.layout.events_activity_uri, null);
		TextView title = (TextView) view.findViewById(R.id.title_1);
		title.setText(marker.getTitle());
		//TextView snippet = (TextView) view.findViewById(R.id.snippet_1);//站点站台信息
		//snippet.setText(marker.getSnippet());
		ImageButton button = (ImageButton) view
				.findViewById(R.id.start_amap_app_1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//进入站台详情，可选
				Intent _int = new Intent();
				_int.putExtra("lat", latLonPoint.getLatitude());
				_int.putExtra("long", latLonPoint.getLongitude());
				_int.putExtra("address", addressName);
				_int.putExtra("id", id);
				setResult(11, _int);
				finish();
			}
		});
		return view;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}
	/**
	 * 响应逆地理编码
	 */
	public void getAddress(LatLonPoint latLonPoint) {
		//showDialog();
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}
	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		//dismissDialog();
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress();
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				marker_use = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
						.position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()))
						.title(addressName).icon(
						BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed))));
				//marker_use.getSnippet();
				marker_use.showInfoWindow();
				mTapTextView.setText(addressName);
				ToastUtil.show(EventsActivity.this, addressName);
			} else {
				ToastUtil.show(EventsActivity.this, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(EventsActivity.this, rCode);
		}
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
    	page_title.setText("地址选择");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
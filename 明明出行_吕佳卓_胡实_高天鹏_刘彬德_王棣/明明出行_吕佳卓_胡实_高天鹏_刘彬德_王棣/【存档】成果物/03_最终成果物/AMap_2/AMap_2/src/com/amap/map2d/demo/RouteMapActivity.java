package com.amap.map2d.demo;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.overlay.BusLineOverlay;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RouteMapActivity extends Activity implements AMap.OnMapLoadedListener {
    private MapView mapView;
    private AMap aMap;
    private BusLineOverlay busLineOverlay;
    private busInfo bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        setActionBarLayout(R.layout.other_actionbar);
        mapView = (MapView) findViewById(R.id.route_detail_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);

        Intent intent = getIntent();
        bus = (busInfo) intent.getSerializableExtra("bus");
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器


        List<BusStationItem> busStationItemList = new ArrayList<BusStationItem>();
        List<LatLonPoint> pointList = new ArrayList<LatLonPoint>();
        for(int i = 0;i < bus.lineInfo.size(); i ++) {
            BusStationItem item = new BusStationItem();
            item.setBusStationId("");
            item.setBusStationName(bus.lineInfo.get(i).name);
            if(bus.lineInfo.get(i).lat+bus.lineInfo.get(i).lng-0<0.0001)
                continue;
            LatLonPoint pos = new LatLonPoint(bus.lineInfo.get(i).lat, bus.lineInfo.get(i).lng);
            item.setLatLonPoint(pos);
            item.setCityCode("");
            item.setAdCode("");
            item.setCityCode("");
            busStationItemList.add(item);
            pointList.add(pos);
        }

        BusLineItem busLineItem = new BusLineItem();
        busLineItem.setDirectionsCoordinates(pointList);
        busLineItem.setBusStations(busStationItemList);

        busLineOverlay = new BusLineOverlay(this, aMap, busLineItem);
        busLineOverlay.addToMap();
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
    	page_title.setText("线路详情");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中

//        busLineOverlay.zoomToSpan();

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for(int i = 0;i < bus.lineInfo.size(); i ++) {
            if(bus.lineInfo.get(i).lat+bus.lineInfo.get(i).lng-0<0.0001)
                continue;
            boundsBuilder.include(new LatLng(bus.lineInfo.get(i).lat, bus.lineInfo.get(i).lng));
        }
        LatLngBounds bounds = boundsBuilder.build();
        //第二个参数，与地图边缘边距
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
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

}


package com.amap.map2d.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_detail);
        setActionBarLayout(R.layout.other_actionbar);
        final busInfo bus;
        Map<String, Object> map;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Intent intent = getIntent();
        bus = (busInfo) intent.getSerializableExtra("bus");

        TextView info = (TextView)findViewById(R.id.route_detail_number);
        TextView ticket = (TextView)findViewById(R.id.route_detail_price);
        TextView time = (TextView)findViewById(R.id.route_detail_time);
        info.setText(bus.name);
        ticket.setText(bus.price);
        time.setText(bus.time);
        for(int i = 0;i < bus.lineInfo.size(); i ++) {
            stopInfo stop = (stopInfo)bus.lineInfo.get(i);
            map = new HashMap<String, Object>();
            if(i==0)
                map.put("pic", R.drawable.start);
            else if(i == bus.lineInfo.size()-1)
                map.put("pic", R.drawable.end);
            else
                map.put("pic", R.drawable.normal);
            map.put("name", stop.name);
            list.add(map);
        }

        ListView listView = (ListView)findViewById(R.id.route_detail_listview);
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.route_detail_listview_item,
                new String[]{"pic","name"},
                new int[]{R.id.ic_route_detail_list,R.id.route_detail_station});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RouteMapActivity.class);
                intent.putExtra("bus", bus);
                startActivity(intent);
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
    	page_title.setText("线路详情");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}


package com.amap.map2d.demo;





import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.content.Context;
import android.content.Intent;

public class RouteQueryActivity extends Activity {

    private static final String TAG = "Http Connection";
    private String main_city;
    public ArrayList busLineInfo = new ArrayList<busInfo>();
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private SimpleAdapter adapter;


    private void updateData() {
        list.clear();
        Map<String, Object> map;
        for(int i = 0;i < busLineInfo.size(); i ++) {
            busInfo bus = (busInfo)busLineInfo.get(i);
            map = new HashMap<String, Object>();
            map.put("name", bus.name);
            map.put("start", bus.start);
            map.put("terminal", bus.terminal);
            list.add(map);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_query);
        setActionBarLayout(R.layout.other_actionbar);
        try {
        	Intent _int = getIntent();
            main_city = _int.getStringExtra("main_city");
		} catch (Exception e) {
			// TODO: handle exception
			main_city = "哈尔滨";
		}
        
        
        ListView listView = (ListView)findViewById(R.id.route_result);
        adapter = new SimpleAdapter(this,list,R.layout.route_result_listview_item,
                new String[]{"name","start","terminal"},
                new int[]{R.id.route_result_number,R.id.route_result_start_point,R.id.route_result_ending});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),RouteDetailActivity.class);
                intent.putExtra("bus", (busInfo)busLineInfo.get(position));
                startActivity(intent);
            }
        });

        ImageView search = (ImageView) findViewById(R.id.route_query);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = (EditText)findViewById(R.id.route_number);
                String city="";
                try {
                    city = URLEncoder.encode(main_city, "UTF-8");
                }catch(UnsupportedEncodingException e){

                }
                final String url = "http://api.juheapi.com/bus/line?key=5bb1d8bc607a346693a970583247f45e&city="+city+"&q="+input.getText();
                Log.e(TAG, url);
                new AsyncHttpTask().execute(url);
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
    	page_title.setText("线路查询");
    	back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "*");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode ==  200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    //httpResult = response;
                    parseResult(response);
                    result = 1; // Successful
                }else{
                    Log.d(TAG,""+statusCode);
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }
        @Override
        protected void onPostExecute(Integer result) {
            /* Download complete. Lets update UI */
            if(result == 1){
                adapter.notifyDataSetChanged();
            }else{
                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }
        /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }
    private void parseResult(String result) {
        try{
            busLineInfo.clear();
            JSONObject response = new JSONObject(result);
            JSONArray results = response.optJSONArray("result");
            for(int i=0; i< results.length();i++ ){
                JSONObject busLine = results.optJSONObject(i);
                busLineInfo.add(new busInfo(busLine));
            }
            updateData();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}


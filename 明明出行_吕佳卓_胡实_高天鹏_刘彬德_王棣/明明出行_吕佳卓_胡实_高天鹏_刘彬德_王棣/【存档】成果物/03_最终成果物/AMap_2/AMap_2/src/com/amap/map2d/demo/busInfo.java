package com.amap.map2d.demo;



import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by ivan on 7/20/16.
 */
public class busInfo implements Serializable {
    public String name;
    public String start;
    public String terminal;
    public String price;
    public String time;
    public ArrayList<stopInfo> lineInfo;
    public busInfo(JSONObject info){
        String baseInfo = info.optString("_id");
        int startIndex = baseInfo.indexOf("(");
        int midIndex = baseInfo.indexOf("-");
        int endIndex = baseInfo.indexOf(")");
        name = baseInfo.substring(0,startIndex);
        start = baseInfo.substring(startIndex+1,midIndex);
        terminal = baseInfo.substring(midIndex+1,endIndex);
        String extraInfo = info.optString("info");
        midIndex = extraInfo.indexOf(";");
        time = extraInfo.substring(0,midIndex);
        price = extraInfo.substring(midIndex+1,extraInfo.length()-1);
        JSONArray stops = info.optJSONArray("stats");
        lineInfo = new ArrayList<stopInfo>();
        for(int i=0; i< stops.length();i++ ){
            JSONObject stop = stops.optJSONObject(i);
            lineInfo.add(new stopInfo(stop.optString("_id"),Double.valueOf(stop.optString("lat")),Double.valueOf(stop.optString("lng"))));
        }
    }
}

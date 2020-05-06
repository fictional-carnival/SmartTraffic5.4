package com.lenovo.smarttraffic.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class LxActivity extends BaseActivity {
    private ImageView im_1;
    private ImageView im_2;
    private MapView mapView;
    private AMap map;
    private LatLng latLng;
    private ArrayList<Marker> markers;
    private ArrayList<LatLng> latLngs;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_1;
    private ImageView im_4;
    private ImageView im_3;
    private TextView tv_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_chewei;
    }

    private void InitView() {

        initToolBar(findViewById(R.id.toolbar), true, "车位查询");
        tv_content = findViewById(R.id.tv_content);
        im_1 = findViewById(R.id.im_1);
        im_2 = findViewById(R.id.im_2);
        im_3 = findViewById(R.id.im_3);
        im_4 = findViewById(R.id.im_4);
        tv_content.setText("小车地图信息");

        map = mapView.getMap();
        im_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker_self)));
                map.addMarker(markerOptions);
            }
        });
        im_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Marker m : markers) {
                    m.setVisible(true);
                }
                tv_content.setText("1，2，3，4号小车地图标记完成！");
            }
        });
        im_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ceju();
            }
        });

    }
    private void ceju() {
        LatLng latLng = latLngs.get(1);
        map.addCircle(new CircleOptions().radius(20000).center(latLng).strokeWidth(5).strokeColor(Color.RED));
        float line1 = AMapUtils.calculateLineDistance(latLng, latLngs.get(0));
        float line2 = AMapUtils.calculateLineDistance(latLng, latLngs.get(2));
        float line3 = AMapUtils.calculateLineDistance(latLng, latLngs.get(3));
        String line = "";
        if (line1 < 20000) {
            line += "1、";
        }
        if (line2 < 20000) {
            line += "3、";
        }
        if (line3 < 20000) {
            line += "4、";
        }
        if (line.length() > 0) {
            line = line.substring(0, line.length() - 2);
        }
        tv_content.setText("当前在2号小车周围20KM范围内的小车车号有：" + line);
    }

    private void InitData() {
        markers = new ArrayList<>();
        latLngs = new ArrayList<LatLng>(){{
            add(new LatLng(39.941853, 116.385307));
            add(new LatLng(40.042737, 116.309884));
            add(new LatLng(39.860512, 116.347593));
            add(new LatLng(39.802303, 116.209749));
        }};
        int imgs[] = new int[]{
                R.mipmap.marker_one,
                R.mipmap.marker_second,
                R.mipmap.marker_thrid,
                R.mipmap.marker_forth
        };
        for (int i = 0; i < latLngs.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), imgs[i])));
            markerOptions.position(latLngs.get(i));
            markerOptions.title(i+1+"号小车");
            Marker marker = map.addMarker(markerOptions);
            marker.setVisible(false);
            markers.add(marker);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }


}

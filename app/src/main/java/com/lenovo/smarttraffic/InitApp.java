package com.lenovo.smarttraffic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class InitApp extends MultiDexApplication {

    private static Handler mainHandler;
    //    private static Context AppContext;
    private static InitApp instance;
    private Set<Activity> allActivities;
    private static Toast tos;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor edit;
    public static RequestQueue queue;
    public static ArrayList<String> label;
    public static HashMap m;
    public static final String Url = "http://www.lylala8.com:8081/transportservice/action/";


    public static synchronized InitApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        AppContext = this;
        instance = this;
        mainHandler = new Handler();


        sp = getSharedPreferences("settings", MODE_PRIVATE);
        edit = sp.edit();
        queue = Volley.newRequestQueue(instance);
        label = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.mobile_news_name)));
        m = new HashMap();
        m.put("UserName", "user1");
        InitApp.edit.remove("isLogin").commit();
        initData();
    }

    public static void toast(String s) {
        if (null != tos) {
            tos.cancel();
        }
        tos = Toast.makeText(instance, s, Toast.LENGTH_SHORT);
        tos.show();
    }

    public static void doPost(String path, Map map, Response.Listener<JSONObject> listener) {
        JSONObject object;
        if (null == map) {
            object = new JSONObject(m);
        }else {
            object = new JSONObject(map);
        }
        String url = Url + path;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast("网络连接故障");
            }
        });
        queue.add(request);
    }

    public static int random(int a, int b) {
        return (int) Math.round(Math.random() * (a - b) + b);
    }

    public static String timeFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    private void initData() {
        if (sp.getString("userinfo", null) == null) {
            getUserInfo();
        }
    }

    private void getUserInfo() {
        doPost("GetSUserInfo", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                edit.putString("userinfo", String.valueOf(jsonObject)).commit();
            }
        });
    }

    public static User.ROWSDETAILBean getUser(String type, String name) {
        try {
            JSONArray array = new JSONObject(sp.getString("userinfo", null)).getJSONArray("ROWS_DETAIL");
            for (int i = 0, l = array.length(); i < l; i++) {
                JSONObject object = array.getJSONObject(i);
                if (name.equals(object.getString(type))) {
                    return new Gson().fromJson(String.valueOf(object), User.ROWSDETAILBean.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //    public static Context getContext(){
//        return AppContext;
//    }
    public static Handler getHandler(){
        return mainHandler;
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}

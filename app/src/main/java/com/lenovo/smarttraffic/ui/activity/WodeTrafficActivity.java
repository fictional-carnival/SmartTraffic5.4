package com.lenovo.smarttraffic.ui.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Red;
import com.lenovo.smarttraffic.bean.ZuoJia;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;
import com.lenovo.smarttraffic.util.Sputil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WodeTrafficActivity extends BaseActivity {

    private ListView lv_list;
    private List<Red.RedBean> redArrays;
    private String[] names;
    private String[][] names2;
    private int[] ints;
    private Timer timer;
    private Map<String, String> map;
    private RedAdapter redAdapter;
    private PopupWindow popupWindow;
    private LinearLayout ll_content2;
    private LinearLayout ll_content1;
    private LinearLayout ll_two;
    private TextView tv_two_4;
    private TextView tv_two_3;
    private TextView tv_two_2;
    private TextView tv_two_1;
    private ImageView im_video;
    private TextView tv_two_5;
    private Timer timer2;
    private String[] load_status;
    private JSONArray xiaoxiArray;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Notification notification;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_wode_trffiic;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.item1));
        lv_list = findViewById(R.id.lv_list);
        TextView tv_tab_1 = findViewById(R.id.tv_tab_1);
        TextView tv_tab_2 = findViewById(R.id.tv_tab_2);
        ll_content1 = findViewById(R.id.ll_content1);
        ll_content2 = findViewById(R.id.ll_content2);
        tv_two_1 = findViewById(R.id.tv_two_1);
        tv_two_2 = findViewById(R.id.tv_two_2);
        tv_two_3 = findViewById(R.id.tv_two_3);
        tv_two_4 = findViewById(R.id.tv_two_4);
        tv_two_5 = findViewById(R.id.tv_two_5);
        im_video = findViewById(R.id.im_video);
        ll_two = findViewById(R.id.ll_two);
        tv_tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tab_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv_tab_2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                ll_content1.setVisibility(View.VISIBLE);
                ll_content2.setVisibility(View.GONE);
            }
        });
        tv_tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tab_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tv_tab_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                ll_content1.setVisibility(View.GONE);
                ll_content2.setVisibility(View.VISIBLE);

            }
        });

    }

    private void InitData() {

        String string = InitApp.sp.getString(Sputil.REDLAMP, "");
        try {
            xiaoxiArray = new JSONArray(InitApp.sp.getString(Sputil.XIAOXI, "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (string.equals("")) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < 5; i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("red", 10);
                    jsonObject.put("green", 10);
                    jsonObject.put("yellow", 10);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("red", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            InitApp.edit.putString(Sputil.REDLAMP, jsonObject.toString()).commit();
            redArrays = new Gson().fromJson(jsonObject.toString(), Red.class).getRed();
        } else {
            redArrays = new Gson().fromJson(string, Red.class).getRed();
        }
        names = new String[]{"红", "绿", "黄"};
        ints = new int[]{R.mipmap.red_bg_01, R.mipmap.red_bg_02, R.mipmap.red_bg_03};
        names2 = new String[5][4];
        for (int i = 0; i < 5; i++) {
            int random = InitApp.random(0, 2);
            if (random == 0) {
                names2[i][0] = names[0];
                names2[i][1] = names[1];
                names2[i][2] = redArrays.get(i).getRed()+"";
                names2[i][3] = redArrays.get(i).getGreen()+"";
            } else if (random == 1) {
                names2[i][0] = names[1];
                names2[i][1] = names[0];
                names2[i][2] = redArrays.get(i).getGreen() + "";
                names2[i][3] = redArrays.get(i).getRed() + "";
            } else {
                names2[i][0] = names[1];
                names2[i][1] = names[2];
                names2[i][2] = redArrays.get(i).getGreen() + "";
                names2[i][3] = redArrays.get(i).getYellow() + "";
            }
        }
        load_status = new String[]{"畅通", "较畅通", "拥挤", "堵塞", "报表", "异常"};

        map = new HashMap<>();
        map.put("红", "绿");
        map.put("黄", "红");
        map.put("绿", "黄");
        redAdapter = new RedAdapter();
        lv_list.setAdapter(redAdapter);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    if (!names2[i][2].equals("0")) {
                        names2[i][2] = Integer.parseInt(names2[i][2]) - 1 + "";
                    } else {
                        names2[i][0] = map.get(names2[i][0]);
                        if (names2[i][0].equals("红")) {
                            names2[i][2] = redArrays.get(i).getRed()+"";
                        } else if (names2[i][0].equals("黄")) {
                            names2[i][2] = redArrays.get(i).getYellow()+"";
                        } else {
                            names2[i][2] = redArrays.get(i).getGreen()+"";
                        }
                    }
                    if (!names2[i][3].equals("0")) {
                        names2[i][3] = Integer.parseInt(names2[i][3]) - 1 + "";
                    } else {
                        names2[i][1] = map.get(names2[i][1]);
                        if (names2[i][1].equals("红")) {
                            names2[i][3] = redArrays.get(i).getRed()+"";
                        } else if (names2[i][1].equals("黄")) {
                            names2[i][3] = redArrays.get(i).getYellow()+"";
                        } else {
                            names2[i][3] = redArrays.get(i).getGreen()+"";
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        redAdapter.notifyDataSetChanged();
                    }
                });
            }
        },1000,1000);
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int random = InitApp.random(20, 200);
                        if (random > 100) {
                            tv_two_2.setText("PM2.5 大于" +
                                    "100，不适合出行。");
                            tv_two_2.setVisibility(View.VISIBLE);

                            im_video.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("status", "PM2.5报警");
                                jsonObject.put("content", "PM2.5大于100，不适合出行");
                                jsonObject.put("time", InitApp.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                xiaoxiArray.put(jsonObject);
                                InitApp.edit.putString(Sputil.XIAOXI, xiaoxiArray.toString()).commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            tv_two_2.setVisibility(View.GONE);
                            im_video.setVisibility(View.GONE);
                        }
                        tv_two_5.setVisibility(View.VISIBLE);

                        tv_two_1.setText("PM当前值：" + random);
                        int random1 = InitApp.random(1000, 4800);
                        Log.i("safasfas", "run: "+random1/5000);
                        ll_two.setLayoutParams(new LinearLayout.LayoutParams((int) (random1/5000.0*400)-15, 35));
                        tv_two_3.setText("光照阈值当前值："+random1+"");
                        tv_two_4.setText(random1+"");
                        if (random1 < 3000) {
                            tv_two_5.setText("光照较弱，出行请开" +
                                    "灯");
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("status", "光照阈值报警");
                                jsonObject.put("content", "光照较弱，出行请开" +
                                        "灯");
                                jsonObject.put("time", InitApp.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                xiaoxiArray.put(jsonObject);
                                InitApp.edit.putString(Sputil.XIAOXI, xiaoxiArray.toString()).commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (random1 > 4000) {
                            tv_two_5.setText("光照较强，出行请戴墨镜");
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("status", "光照阈值报警");
                                jsonObject.put("content", "光照较强，出行请戴墨镜");
                                jsonObject.put("time", InitApp.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                xiaoxiArray.put(jsonObject);
                                InitApp.edit.putString(Sputil.XIAOXI, xiaoxiArray.toString()).commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (random1 >= 3000 && random1 <= 4000) {
                            tv_two_5.setVisibility(View.GONE);
                        }
                    }
                });
            }
        },3000,3000);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }

    private class RedAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return redArrays.size();
        }

        @Override
        public Red.RedBean getItem(int i) {
            return redArrays.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View cv;
            if (view == null) {
                cv = View.inflate(getApplicationContext(), R.layout.trffic_list, null);
            } else {
                cv = view;
            }

            TextView tv_1 = cv.findViewById(R.id.tv_1);
            TextView tv_2 = cv.findViewById(R.id.tv_2);
            TextView tv_3 = cv.findViewById(R.id.tv_3);
            TextView tv_4 = cv.findViewById(R.id.tv_4);
            TextView tv_5 = cv.findViewById(R.id.tv_5);
            TextView tv_6= cv.findViewById(R.id.tv_6);
            TextView tv_8 = cv.findViewById(R.id.tv_8);
            TextView tv_9 = cv.findViewById(R.id.tv_9);
            TextView tv_10 = cv.findViewById(R.id.tv_10);
            TextView tv_11 = cv.findViewById(R.id.tv_11);
            TextView tv_12 = cv.findViewById(R.id.tv_12);
            ImageView im_1 = cv.findViewById(R.id.im_1);
            ImageView im_2 = cv.findViewById(R.id.im_2);
            TextView tv_13 = cv.findViewById(R.id.tv_13);
            tv_12.setText("路口"+(i+1));
            tv_1.setText("绿灯"+getItem(i).getGreen()+"秒");
            tv_2.setText("黄灯"+getItem(i).getYellow()+"秒");
            tv_3.setText("红灯"+getItem(i).getRed()+"秒");
            tv_4.setText(names2[i][0]+"灯"+names2[i][2]+"秒");
            tv_5.setText(names2[i][1]+"灯"+names2[i][3]+"秒");
            if (names2[i][0].equals("红")) {
                im_1.setImageResource(ints[0]);
            } else if (names2[i][0].equals("黄")) {
                im_1.setImageResource(ints[1]);
            } else {
                im_1.setImageResource(ints[2]);

            }
            if (names2[i][1].equals("红")) {
                im_2.setImageResource(ints[0]);
            } else if (names2[i][1].equals("黄")) {
                im_2.setImageResource(ints[1]);
            } else {
                im_2.setImageResource(ints[2]);
            }
            tv_6.setText(names2[i][2]+"");
            tv_8.setText(names2[i][3]+"");

            tv_9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (names2[i][0].equals("绿")) {
                        InitApp.toast("该路口已经是绿灯了");
                    } else {

                        names2[i][0] = "绿";
                        names2[i][2] = 30 + "";
                        redAdapter.notifyDataSetChanged();
                    }
                }
            });
            tv_10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (names2[i][1].equals("绿")) {
                        InitApp.toast("该路口已经是绿灯了");
                    } else {
                        names2[i][1] = "绿";
                        names2[i][3] = 30 + "";
                        redAdapter.notifyDataSetChanged();
                    }

                }
            });
            tv_13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popsite(i);
                }
            });
            int random = InitApp.random(0, 5);
            tv_11.setText(load_status[random]);
            if (random > 2) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("status", "路况报警");
                    jsonObject.put("content", i + 1 + "号路口处于" + load_status[random] + "，请选择合适路线");
                    jsonObject.put("time", InitApp.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    xiaoxiArray.put(jsonObject);
                    InitApp.edit.putString(Sputil.XIAOXI, xiaoxiArray.toString()).commit();
                    setNotify(i,"道路报警",i+1+"号路口处于拥挤堵塞状态，请选择合适的路线");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return cv;
        }
    }
    private void setNotify(int id,String str1,String str2) {
        try {
            notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //android 8.0 兼容9.0
                NotificationChannel channel = new NotificationChannel(getApplication().getPackageName(), "asfsafas", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.setShowBadge(true);
                channel.setDescription(getApplication().getString(R.string.app_name));
                // 设置显示模式
                channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(getApplication(), getApplication().getPackageName());
                //设置小图标
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                //设置优先级，低优先级可能被隐藏
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                builder.setWhen(System.currentTimeMillis());
                //设置通知栏能否被清楚，true不能被清除，false可以被清除
                builder.setOngoing(false);
                builder.setContentTitle(str1);
                builder.setContentText(str2);
                builder.setAutoCancel(true);//用户点击就自动消失
                builder.setChannelId(getApplication().getPackageName());
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                builder.setCategory(Notification.CATEGORY_REMINDER);
                builder.setOnlyAlertOnce(true);

//点击后跳转Activity
                Intent intent = new Intent(getApplicationContext(),WdZjActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                notification = builder.build();
            } else {
                //其余版本
                builder = new NotificationCompat.Builder(getApplication());
                //设置小图标
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setContentTitle(str1);
                builder.setContentText(str2);
                // 设置优先级，低优先级可能被隐藏
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                builder.setWhen(System.currentTimeMillis());
                //设置通知栏能否被清楚，true不能被清除，false可以被清除
                builder.setOngoing(false);
                builder.setAutoCancel(true);//用户点击就自动消失
                Intent intent = new Intent(getApplicationContext(),WdZjActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                notification = builder.build();
            }
            //发布通知
            notificationManager.notify(id, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void popsite(int i) {
        View pop_view = this.getLayoutInflater().inflate(R.layout.pop_window2, null);
        popupWindow = new PopupWindow(pop_view, -2, -2, true);
        View parentView = LayoutInflater.from(WodeTrafficActivity.this).inflate(R.layout.activity_wdzj, null);
        popupWindow.showAtLocation(parentView, Gravity.CENTER, Gravity.CENTER,Gravity.CENTER);
        TextView pop_tv1 = pop_view.findViewById(R.id.pop_1);
        TextView pop_tv2 = pop_view.findViewById(R.id.pop_2);
        EditText et_red = pop_view.findViewById(R.id.et_red);
        EditText et_yellow = pop_view.findViewById(R.id.et_yellow);
        EditText et_green = pop_view.findViewById(R.id.et_green);
        TextView tv_close = pop_view.findViewById(R.id.im_back);
        pop_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String red = et_red.getText().toString().trim();
                String yellow = et_yellow.getText().toString().trim();
                String green = et_green.getText().toString().trim();
                if (!red.equals("") && !yellow.equals("") && !green.equals("")) {
                    int reds = Integer.parseInt(red);
                    int yellows = Integer.parseInt(yellow);
                    int greens = Integer.parseInt(green);
                    if (
                            reds >= 1 && reds <= 30 && yellows >= 1 && yellows <= 30 && greens >= 1 && greens <= 30) {
                        redArrays.get(i).setGreen(greens);
                        redArrays.get(i).setRed(reds);
                        redArrays.get(i).setYellow(yellows);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("red", new JSONArray(new Gson().toJson(redArrays)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InitApp.edit.putString(Sputil.REDLAMP, jsonObject.toString()).commit();
                        if (names2[i][0].equals("红")) {
                            names2[i][2] = redArrays.get(i).getRed()+"";
                        } else if (names2[i][0].equals("黄")) {
                            names2[i][2] = redArrays.get(i).getYellow()+"";
                        } else {
                            names2[i][2] = redArrays.get(i).getGreen()+"";
                        }
                        if (names2[i][1].equals("红")) {
                            names2[i][3] = redArrays.get(i).getRed()+"";
                        } else if (names2[i][1].equals("黄")) {
                            names2[i][3] = redArrays.get(i).getYellow()+"";
                        } else {
                            names2[i][3] = redArrays.get(i).getGreen()+"";
                        }
                        redAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    } else {
                        InitApp.toast("输入不符合规范");
                    }
                } else {
                    InitApp.toast("输入不可以为空");

                }
            }
        });
        pop_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

}

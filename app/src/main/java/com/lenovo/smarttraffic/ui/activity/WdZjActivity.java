package com.lenovo.smarttraffic.ui.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.ZuoJia;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;
import com.lenovo.smarttraffic.util.Sputil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class WdZjActivity extends BaseActivity {

    private Toolbar toolbar;
    private ArrayList<TextView> textViews;
    private TextView tv_tab_1;
    private TextView tv_tab_2;
    private TextView tv_tab_3;
    private ZuoJia zuoJia;
    private ArrayList<ZuoJia.CarInfoBean> carInfos;
    private ArrayList<ZuoJia.HistoryBean> historys;
    private ArrayList<LinearLayout> linearLayouts;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Notification notification;
    private PopupWindow popupWindow;
    private ArrayList<TextView> textViews2;
    private ListView lv_list;
    private LinearLayout ll_content3;
    private LinearLayout ll_content2;
    private LinearLayout ll_content1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_wdzj;
    }

    private void InitView() {
        toolbar = findViewById(R.id.toolbar);
        initToolBar(toolbar, true, "我的座驾");
        ll_content1 = findViewById(R.id.ll_content1);
        ll_content2 = findViewById(R.id.ll_content2);
        ll_content3 = findViewById(R.id.ll_content3);
        lv_list = findViewById(R.id.lv_list);
        textViews = new ArrayList<TextView>() {{
            add(findViewById(R.id.tv_1));
            add(findViewById(R.id.tv_1));
            add(findViewById(R.id.tv_2));
            add(findViewById(R.id.tv_3));
            add(findViewById(R.id.tv_4));
            add(findViewById(R.id.tv_5));
            add(findViewById(R.id.tv_6));
            add(findViewById(R.id.tv_7));
            add(findViewById(R.id.tv_8));
            add(findViewById(R.id.tv_9));
            add(findViewById(R.id.tv_10));
            add(findViewById(R.id.tv_11));
            add(findViewById(R.id.tv_12));
        }};
        textViews2 = new ArrayList<TextView>() {{
            add(findViewById(R.id.tv_wd_1));
            add(findViewById(R.id.tv_wd_1));
            add(findViewById(R.id.tv_wd_2));
            add(findViewById(R.id.tv_wd_3));
            add(findViewById(R.id.tv_wd_4));
            add(findViewById(R.id.tv_wd_5));
            add(findViewById(R.id.tv_wd_6));
            add(findViewById(R.id.tv_wd_7));
            add(findViewById(R.id.tv_wd_8));
        }};
        linearLayouts = new ArrayList<LinearLayout>(){{
            add(findViewById(R.id.ll_car_1));
            add(findViewById(R.id.ll_car_2));
            add(findViewById(R.id.ll_car_3));
            add(findViewById(R.id.ll_car_4));

        }};
        tv_tab_1 = findViewById(R.id.tv_tab_1);
        tv_tab_2 = findViewById(R.id.tv_tab_2);
        tv_tab_3 = findViewById(R.id.tv_tab_3);
        tv_tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tab_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv_tab_2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tv_tab_3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                ll_content1.setVisibility(View.VISIBLE);
                ll_content2.setVisibility(View.GONE);
                ll_content3.setVisibility(View.GONE);
            }
        });
        tv_tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tab_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tv_tab_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv_tab_3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ll_content1.setVisibility(View.GONE);
                ll_content2.setVisibility(View.VISIBLE);
                ll_content3.setVisibility(View.VISIBLE);

            }
        });
        tv_tab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tab_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tv_tab_2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tv_tab_3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                ll_content1.setVisibility(View.GONE);
                ll_content2.setVisibility(View.GONE);
                ll_content3.setVisibility(View.VISIBLE);

            }
        });

    }

    private void InitData() {
        try {

            String string = InitApp.sp.getString(Sputil.ZJINFO, "");
            if (string.equals("")) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < 4; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", i + 1);
                    jsonObject.put("money", 50 );
                    jsonObject.put("status", 0 );
                    jsonArray.put(jsonObject);
                }
                JSONArray jsonArray1 = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", 1);
                jsonObject.put("money", 50);
                jsonObject.put("datetime", InitApp.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                jsonArray1.put(jsonObject);
                jsonObject = new JSONObject();
                jsonObject.put("carInfo", jsonArray);
                jsonObject.put("history", jsonArray1);
                Log.i("afasfas", "InitData: "+jsonObject.toString());

                InitApp.edit.putString(Sputil.ZJINFO, jsonObject.toString()).commit();
                zuoJia = new Gson().fromJson(jsonObject.toString(), ZuoJia.class);

            } else {
                zuoJia = new Gson().fromJson(string, ZuoJia.class);
            }

            carInfos = (ArrayList<ZuoJia.CarInfoBean>) zuoJia.getCarInfo();
            historys = (ArrayList<ZuoJia.HistoryBean>) zuoJia.getHistory();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= 4;i++) {
            if (carInfos.get(i-1).getMoney() < 100) {
                textViews.get(i * 2 - 1).setText("警告");
                linearLayouts.get(i - 1).setBackgroundColor(Color.RED);
                setNotify(i,"警告",i+"号小车余额不足");
            } else {
                textViews.get(i * 2 - 1).setText("正常");
                linearLayouts.get(i - 1).setBackgroundColor(Color.GREEN);
            }
            textViews.get(i*3-1).setText(carInfos.get(i-1).getMoney()+"");
        }
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            linearLayouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popsite(finalI +1);
                }
            });
        }
        for (int i = 1; i <= 4; i++) {
            if (carInfos.get(i - 1).getStatus() == 0) {
                textViews2.get(i * 2).setBackgroundColor(Color.GREEN);
                textViews2.get(i * 2-1).setBackgroundColor(Color.WHITE);

            } else {
                textViews2.get(i * 2).setBackgroundColor(Color.WHITE);
                textViews2.get(i * 2-1).setBackgroundColor(Color.GREEN);
            }
        }
        for (int i = 1; i <= 4; i++) {
            int finalI = i;
            int finalI1 = i;
            textViews2.get(i*2-1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textViews2.get(finalI * 2 - 1).setBackgroundColor(Color.GREEN);
                    textViews2.get(finalI * 2 ).setBackgroundColor(Color.WHITE);
                    carInfos.get(finalI1 - 1).setStatus(1);
                    zuoJia.setHistory(historys);
                    zuoJia.setCarInfo(carInfos);
                    InitApp.edit.putString(Sputil.ZJINFO, new Gson().toJson(zuoJia)).commit();
                }
            });
            textViews2.get(i*2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textViews2.get(finalI * 2).setBackgroundColor(Color.GREEN);
                    textViews2.get(finalI * 2-1).setBackgroundColor(Color.WHITE);
                    carInfos.get(finalI1 - 1).setStatus(0);
                    zuoJia.setHistory(historys);
                    zuoJia.setCarInfo(carInfos);
                    InitApp.edit.putString(Sputil.ZJINFO, new Gson().toJson(zuoJia)).commit();
                }
            });
        }
    }

    private void popsite(int i) {
        View pop_view = this.getLayoutInflater().inflate(R.layout.pop_window, null);
        popupWindow = new PopupWindow(pop_view, -2, -2, true);
        View parentView = LayoutInflater.from(WdZjActivity.this).inflate(R.layout.activity_wdzj, null);
        popupWindow.showAtLocation(parentView, Gravity.CENTER, Gravity.CENTER,Gravity.CENTER);
        TextView pop_tv1 = pop_view.findViewById(R.id.pop_1);
        TextView pop_tv2 = pop_view.findViewById(R.id.pop_2);
        EditText et_money = pop_view.findViewById(R.id.et_money);
        TextView tv_close = pop_view.findViewById(R.id.im_back);
        pop_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = et_money.getText().toString().trim();
                if (!money.equals("")) {
                    int moneys = Integer.parseInt(money);
                    if (moneys > 50 || moneys < 1) {
                        InitApp.toast("数值不规范！");
                    } else {
                        InitApp.toast(i+"号小车，充值"+moneys+"元成功！");
                        ZuoJia.HistoryBean history = new ZuoJia.HistoryBean(i, moneys, InitApp.timeFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        carInfos.get(i - 1).setMoney(carInfos.get(i-1).getMoney() + moneys);
                        historys.add(history);
                        zuoJia.setHistory(historys);
                        zuoJia.setCarInfo(carInfos);
                        InitApp.edit.putString(Sputil.ZJINFO, new Gson().toJson(zuoJia)).commit();
                        popupWindow.dismiss();
                        InitData();
                    }
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

    @Override
    protected void onResume() {
        super.onResume();
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

}

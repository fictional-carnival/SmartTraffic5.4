package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;
import com.lenovo.smarttraffic.util.Sputil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class LoadLampActivity extends BaseActivity {

    private String status;
    private ImageView im_lamp;
    private TextView tv_4;
    private TextView tv_3;
    private TextView tv_2;
    private TextView tv_1;
    private String down;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_loadlamp;
    }


    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "我的路灯");
        tv_1 = findViewById(R.id.tv_lm_1);
        tv_2 = findViewById(R.id.tv_lm_2);
        tv_3 = findViewById(R.id.tv_lm_3);
        tv_4 = findViewById(R.id.tv_lm_4);
        im_lamp = findViewById(R.id.im_lamp);
    }


    private void InitData() {
        status = InitApp.sp.getString(Sputil.LAMP, "1");
        down = InitApp.sp.getString(Sputil.DOWN, "2");

        if (down.equals("1")) {
            im_lamp.setImageResource(R.drawable.lamp2);
            tv_3.setBackgroundColor(Color.GREEN);
            tv_4.setBackgroundColor(Color.WHITE);
        } else {
            im_lamp.setImageResource(R.drawable.lamp1);
            tv_4.setBackgroundColor(Color.GREEN);
            tv_3.setBackgroundColor(Color.WHITE);
        }
        if (status.equals("1")) {
            tv_1.setBackgroundColor(Color.GREEN);
            tv_2.setBackgroundColor(Color.WHITE);
        } else {
            tv_1.setBackgroundColor(Color.WHITE);
            tv_2.setBackgroundColor(Color.GREEN);
        }


        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_1.setBackgroundColor(Color.GREEN);
                tv_2.setBackgroundColor(Color.WHITE);
                status = "1";
                InitApp.toast("成功！");
                InitApp.edit.putString(Sputil.LAMP, "1").commit();
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_2.setBackgroundColor(Color.GREEN);
                tv_1.setBackgroundColor(Color.WHITE);
                status = "2";
                InitApp.edit.putString(Sputil.LAMP, "2").commit();
                InitApp.toast("成功！");
            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("1")) {
                    down = "1";
                    InitApp.edit.putString(Sputil.DOWN, "1").commit();
                    im_lamp.setImageResource(R.drawable.lamp2);
                    tv_3.setBackgroundColor(Color.GREEN);
                    tv_4.setBackgroundColor(Color.WHITE);
                } else {
                    InitApp.toast("必须先将路灯模式设置为手动");
                }
            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("1")) {
                    down = "2";
                    InitApp.edit.putString(Sputil.DOWN, "2").commit();
                    im_lamp.setImageResource(R.drawable.lamp1);
                    tv_3.setBackgroundColor(Color.WHITE);
                    tv_4.setBackgroundColor(Color.GREEN);
                } else {
                    InitApp.toast("必须先将路灯模式设置为手动");

                }
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int random = InitApp.random(1, 100);
                if (status.equals("2")) {
                    if (random > 60) {
                        down = "2";
                        InitApp.edit.putString(Sputil.DOWN, "2").commit();
                        im_lamp.setImageResource(R.drawable.lamp1);
                        tv_3.setBackgroundColor(Color.WHITE);
                        tv_4.setBackgroundColor(Color.GREEN);
                    } else {
                        down = "1";
                        InitApp.edit.putString(Sputil.DOWN, "1").commit();
                        im_lamp.setImageResource(R.drawable.lamp2);
                        tv_3.setBackgroundColor(Color.GREEN);
                        tv_4.setBackgroundColor(Color.WHITE);
                    }
                }

            }
        },3000,3000);
    }


    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

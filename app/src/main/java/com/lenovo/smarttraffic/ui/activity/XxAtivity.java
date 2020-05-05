package com.lenovo.smarttraffic.ui.activity;

import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.ui.fragment.BaseFragment;
import com.lenovo.smarttraffic.ui.fragment.XxCxFragment;
import com.lenovo.smarttraffic.ui.fragment.XxTjFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.net.wifi.WifiConfiguration.Status.strings;

public class XxAtivity extends BaseActivity{
    @Override
    protected int getLayout() {
        return R.layout.activity_xx;
    }
    @BindView(R.id.tabLayout_xx)
    public TabLayout tabLayout;
    @BindView(R.id.viewPager_xx)
    public ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.item4));
        init();
    }
    public void init(){
        ArrayList<String> strings = new ArrayList<String>() {{
            add("消息查询");
            add("消息统计");
        }};
        ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>(){{
            add(new XxCxFragment());
            add(new XxTjFragment());
        }};
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(),fragments,strings));
    }
}

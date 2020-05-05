package com.lenovo.smarttraffic.ui.activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Line;

import java.util.List;

import butterknife.BindView;

public class SubXqActivity extends BaseActivity{

    @BindView(R.id.tv_sub)
    TextView tv_sub;
    @BindView(R.id.tv_dsub)
    TextView tv_dsub;
    @BindView(R.id.tv_hc)
    TextView tv_hc;
    @BindView(R.id.tv_dmoney)
    TextView tv_dmoney;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.tv_sstime)
    TextView tv_sstime;
    @BindView(R.id.tv_setime)
    TextView tv_setime;
    @BindView(R.id.tv_end)
    TextView tv_end;
    @BindView(R.id.tv_estime)
    TextView tv_estime;
    @BindView(R.id.tv_eetime)
    TextView tv_eetime;
    @BindView(R.id.gv_sub)
    GridView gv_sub;
    private String site;
    private int load;
    private List<Line.ROWSDETAILBean> row;
    private SubXqActivity.subAdapter subAdapter;
    private List<String> sites;
    private int falg;
    private Line.ROWSDETAILBean bean;
    private PopupWindow popupWindow;

    @Override
    protected int getLayout() {
        return R.layout.activity_subxq;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        site = getIntent().getStringExtra("site");
        load = getIntent().getIntExtra("load", 0);
        initToolBar(findViewById(R.id.toolbar), true, "站点查询");
        row = new Gson().fromJson(getIntent().getStringExtra("row"), Line.class).getROWS_DETAIL();
        initUI();
    }

    private void initUI() {
        bean = row.get(load);
        sites = bean.getSites();
        tv_sub.setText(site);
        tv_dmoney.setText("全程票价：" + InitApp.random(2, 9) + "元");
        tv_dsub.setText(bean.getName());
        tv_hc.setText(isHc(site));
        List<Line.ROWSDETAILBean.TimeBean> time = bean.getTime();
        Line.ROWSDETAILBean.TimeBean timeBean = time.get(0);
        tv_start.setText(timeBean.getSite());
        tv_sstime.setText(timeBean.getStarttime());
        tv_setime.setText(timeBean.getEndtime());
        Line.ROWSDETAILBean.TimeBean timeBean1 = time.get(1);
        tv_end.setText(timeBean1.getSite());
        tv_estime.setText(timeBean1.getStarttime());
        tv_eetime.setText(timeBean1.getEndtime());
        int size = bean.getSites().size();
        gv_sub.setNumColumns(size);
        ViewGroup.LayoutParams layoutParams = gv_sub.getLayoutParams();
        if (size < 13) {
            layoutParams.width = 1280;
        }else {
            layoutParams.width = size * 120;
        }
        falg = -1;
        subAdapter = new subAdapter();
        gv_sub.setAdapter(subAdapter);
    }

    private String isHc(String site) {
        for (Line.ROWSDETAILBean bean : row) {
            if (bean.getTransfersites().indexOf(site) != -1) {
                return bean.getName();
            }
        }
        return "无";
    }

    private class subAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sites.size();
        }

        @Override
        public String getItem(int i) {
            return sites.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            subView subView;
            if (null == view) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub, viewGroup, false);
                subView = new subView();
                subView.tv_id = view.findViewById(R.id.tv_id);
                subView.tv_name = view.findViewById(R.id.tv_name);
                view.setTag(subView);
            }else {
                subView = (SubXqActivity.subAdapter.subView) view.getTag();
            }
            subView.tv_name.setText(getItem(i));
            if (falg != i) {
                subView.tv_id.setBackgroundResource(R.drawable.subxq_bg);
            }
            if (falg == -1 && getItem(i).equals(site)) {
                subView.tv_id.setBackgroundResource(R.drawable.subxq_bgy);
            }
            subView.tv_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    falg = i;
                    subView.tv_id.setBackgroundResource(R.drawable.subxq_bgy);
                    if (bean.getTransfersites().indexOf(getItem(i)) != -1) {
                        showPopWindow(view,getItem(i));
                    }
                    subAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }

        class subView {
            TextView tv_id;
            TextView tv_name;
        }
    }

    private void showPopWindow(View view, String item) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        int[] zb = new int[2];
        view.getLocationOnScreen(zb);
        View inflate = getLayoutInflater().inflate(R.layout.pop_subxq, null);
        popupWindow = new PopupWindow(inflate, -2, -2, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, zb[0] - 80, zb[1] - 50);
        TextView textView = inflate.findViewById(R.id.tv_qp);
        textView.setText("可换乘"+isHc(item));
    }
}

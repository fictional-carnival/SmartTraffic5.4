package com.lenovo.smarttraffic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lenovo.smarttraffic.Constant;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.util.Sputil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class XxCxFragment extends BaseFragment {

    private ListView lv_vv;
    private TextView tv_content;
    private JSONArray xiaoxis;
    private XiaoxiAdapter xiaoxiAdapter;
    private Spinner spinner;
    private String[] spArrays;
    private JSONArray xxArray;

    @Override
    protected View getSuccessView() {
        View view = View.inflate(getContext(), R.layout.fragment_xxcx,null);
        lv_vv = view.findViewById(R.id.lv_xx);
        tv_content = view.findViewById(R.id.tv_content);
        spinner = view.findViewById(R.id.sp_spiner);
        initData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        try {
            xiaoxis = new JSONArray(InitApp.sp.getString(Sputil.XIAOXI, "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        xxArray = xiaoxis;
        Log.i("safasfasf", "initData: "+xxArray.toString());
        xiaoxiAdapter = new XiaoxiAdapter();
        lv_vv.setAdapter(xiaoxiAdapter);
        spArrays = new String[]{"全部", "PM2.5报警", "路况报警", "光照阈值报警"};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                xxArray = new JSONArray();
                String name = spArrays[i];
                for (int j = xiaoxis.length()-1; j >= 0; j--) {
                    try {
                        if (xiaoxis.getJSONObject(j).getString("status").equals(name)||name.equals("全部")) {
                            xxArray.put(xiaoxis.getJSONObject(j));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (xxArray.length() == 0) {
                    tv_content.setVisibility(View.VISIBLE);
                    lv_vv.setVisibility(View.GONE);
                } else {
                    tv_content.setVisibility(View.GONE);
                    lv_vv.setVisibility(View.VISIBLE);
                }
                xiaoxiAdapter.notifyDataSetChanged();
                Log.i("safasfasf2", "initData: "+xxArray.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected Object requestData() {
        return Constant.STATE_SUCCEED;
    }

    @Override
    public void onClick(View view) {

    }

    private class XiaoxiAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return xxArray.length();
        }

        @Override
        public JSONObject getItem(int i) {
            try {
                return xxArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View cv;
            if (view == null) {
                cv = View.inflate(getContext(), R.layout.xx_list, null);
            } else {
                cv = view;
            }
            TextView tv_text1 = cv.findViewById(R.id.tv_text1);
            TextView tv_text2 = cv.findViewById(R.id.tv_text2);
            TextView tv_text3 = cv.findViewById(R.id.tv_text3);
            TextView tv_text4 = cv.findViewById(R.id.tv_text4);

            tv_text1.setText(i + 1 + "");
            try {
                tv_text2.setText(getItem(i).getString("status"));
                tv_text3.setText(getItem(i).getString("content"));
                tv_text4.setText(getItem(i).getString("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return cv;
        }
    }
}

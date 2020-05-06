package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Line;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class SubActivity extends BaseActivity{
    private Gson gson;
    private Intent intent;
    private List<Line.ROWSDETAILBean> beanList;
    private PopupWindow popupWindow;
    private ExpandableListView el_sub;

    @Override
    protected int getLayout() {
        return R.layout.activity_sub;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.item7));
        intent = new Intent(this, SubXqActivity.class);
        gson = new Gson();
        el_sub = findViewById(R.id.el_sub);
        initData();
    }
    private void initData() {
        HashMap map = new HashMap();
        map.put("UserName", "user1");
        map.put("Line", 0);
        InitApp.doPost("GetMetroInfo", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                intent.putExtra("row", String.valueOf(jsonObject));
                beanList = gson.fromJson(String.valueOf(jsonObject), Line.class).getROWS_DETAIL();
                el_sub.setAdapter(new subAdapter());
            }
        });
    }

    private class subAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return beanList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return beanList.get(i).getSites().size();
        }

        @Override
        public String getGroup(int i) {
            return beanList.get(i).getName();
        }

        @Override
        public String getChild(int i, int i1) {
            return beanList.get(i).getSites().get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            GroupView groupView;
            if (null == view) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group, viewGroup, false);
                groupView = new GroupView();
                groupView.tv_group = view.findViewById(R.id.tv_group);
                view.setTag(groupView);
            }else {
                groupView = (GroupView) view.getTag();
            }
            groupView.tv_group.setText(getGroup(i));
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            ChildView childView;
            if (null == view) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_child, viewGroup, false);
                childView = new ChildView();
                childView.tv_child = view.findViewById(R.id.tv_child);
                view.setTag(childView);
            }else {
                childView = (ChildView) view.getTag();
            }
            String child = getChild(i, i1);
            childView.tv_child.setText(child);
            childView.tv_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopWindow(view, child, i);
                }
            });
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        class GroupView {
            TextView tv_group;
        }
        class ChildView {
            TextView tv_child;
        }
    }

    private void showPopWindow(View view, String child, int i) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        int[] zb = new int[2];
        view.getLocationOnScreen(zb);
        View inflate = getLayoutInflater().inflate(R.layout.pop_sub, null);
        popupWindow = new PopupWindow(inflate, -2, -2, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, zb[0] + 100, zb[1] - 80);
        RelativeLayout rl_xq = inflate.findViewById(R.id.rl_xq);
        TextView textView = inflate.findViewById(R.id.tv_name);
        textView.setText(child);
        rl_xq.setOnClickListener(view1 -> {
            intent.putExtra("load", i);
            intent.putExtra("site", child);
            startActivity(intent);
            popupWindow.dismiss();
        });
    }
}

package com.lenovo.smarttraffic.ui.fragment;

import android.util.Log;
import android.view.View;

import com.lenovo.smarttraffic.Constant;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

public class XxCxFragment extends BaseFragment {
    @Override
    protected View getSuccessView() {
        View view = View.inflate(getContext(), R.layout.fragment_xxcx,null);
        return view;
    }

    @Override
    protected Object requestData() {
        return Constant.STATE_SUCCEED;
    }

    @Override
    public void onClick(View view) {

    }
}

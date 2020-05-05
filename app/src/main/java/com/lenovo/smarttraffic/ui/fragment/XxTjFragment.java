package com.lenovo.smarttraffic.ui.fragment;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lenovo.smarttraffic.Constant;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class XxTjFragment extends BaseFragment {
    public View view;
    public PieChart pieChart;

    @Override
    protected View getSuccessView() {
        view = View.inflate(getContext(), R.layout.fragment_xxtj,null);
        pieChart = view.findViewById(R.id.pieChart);
        initPieChart();
        return view;
    }

    public void initPieChart(){
        pieChart.setExtraOffsets(30,15,30,15);
        pieChart.setTouchEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationAngle(180);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(false);
        List<PieEntry> yVals = new ArrayList<PieEntry>(){
            {
                add(new PieEntry(InitApp.random(2,20), "15"));
                add(new PieEntry(InitApp.random(2,30), "25"));
                add(new PieEntry(InitApp.random(2,10), "24"));
            }
        };
        List<Integer> color = new ArrayList<Integer>(){
            {
                add(Color.parseColor("#2D91FE"));
                add(Color.parseColor("#ff5722"));
                add(Color.parseColor("#eeeeee"));
            }
        };
        PieDataSet pieDataSet = new PieDataSet(yVals,"");
        pieDataSet.setColors(color);
        pieDataSet.setValueLinePart1OffsetPercentage(80f);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setSliceSpace(1f);
        pieDataSet.setHighlightEnabled(true);
        Legend legend = new Legend();
        legend.setPosition(Legend.LegendPosition.PIECHART_CENTER);
        legend.setTextSize(30f);
        legend.setDrawInside(false);
        legend.setXEntrySpace(0f);
        legend.setYEntrySpace(10f);
        legend.setYOffset(20f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
    }

    @Override
    protected Object requestData() {
        return Constant.STATE_SUCCEED;
    }

    @Override
    public void onClick(View view) {

    }
}

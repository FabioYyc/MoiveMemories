package com.example.moivememoir.ui;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.helpers.RestHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReportFragment extends Fragment {
    PieChart pieChart;
    RestHelper restHelper;
    TextView tvStartDate;
    TextView tvEndDate;
    Button pieChartSearch;
    DatePickerDialog.OnDateSetListener onStartDateSetListener;
    DatePickerDialog.OnDateSetListener onEndDateSetListener;
    String startDate;
    String endDate;
    int personId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        pieChart = view.findViewById(R.id.piechartView);
        pieChart = (PieChart) view.findViewById(R.id.piechartView);
//        pieChart.setDescription("Sales by employee (In Thousands $) ");
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterTextSize(10);


        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        pieChartSearch = view.findViewById(R.id.confirmSearchButton);
        MainActivity activity = (MainActivity) getActivity();
        Person user = activity.getUser();
        personId = user.getPersonId();


        //shitty practice here, unfourtanetly did not found a way to not copy and paste yet

        onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // cool so I can set date format here, nice
                month = month + 1;
//                Log.d("RegisterActivity", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String dayStr = Integer.toString(day);
                if (dayStr.length() < 2) dayStr = "0" + dayStr;
                String monthStr = Integer.toString(month);
                if (monthStr.length() < 2) monthStr = "0" + monthStr;

                String date = year + "-" + monthStr + "-" + dayStr;
                tvStartDate.setText(date);
                startDate = date;
            }
        };

        View.OnClickListener startDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onStartDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        };

        onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // cool so I can set date format here, nice
                month = month + 1;
//                Log.d("RegisterActivity", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String dayStr = Integer.toString(day);
                if (dayStr.length() < 2) dayStr = "0" + dayStr;
                String monthStr = Integer.toString(month);
                if (monthStr.length() < 2) monthStr = "0" + monthStr;

                String date = year + "-" + monthStr + "-" + dayStr;
                tvEndDate.setText(date);
                endDate = date;
            }
        };


        tvStartDate.setOnClickListener(startDateListener);
        View.OnClickListener endDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onEndDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        };

        tvEndDate.setOnClickListener(endDateListener);


        pieChartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPieChartData getPieChartData = new GetPieChartData();
                getPieChartData.execute();
            }
        });


//        addPieChartDataSet();

        return view;
    }

    private void addPieChartDataSet(List<Integer> yData, List<String> xData) {
//        int[] yData = {1,2,3,4};
//        String[] xData = {"2000", "3000", "3163", "3171"};
        int total = 0;
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        //get total
        for (int i = 0; i < yData.size(); i++) {
            total += yData.get(i);
        }

        for (int i = 0; i < yData.size(); i++) {
            //get percentage
            float percentage = (float) yData.get(i) * 100 / total;

            yEntrys.add(new PieEntry(percentage, xData.get(i)));
        }

        for (int i = 1; i < xData.size(); i++) {
            xEntrys.add(xData.get(i));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Movie watched %");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);


        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    private class GetPieChartData extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            restHelper = new RestHelper();

            String result = restHelper.getWatchesPerPostcode(personId, startDate, endDate);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            List<Integer> yData = new ArrayList<>();
            List<String> xData = new ArrayList<>();
            if (!result.equals("failed")) {
                //[{"cinemaPostcode":"2007","total":1},{"cinemaPostcode":"3000","total":1},
                // {"cinemaPostcode":"3007","total":1},
                // {"cinemaPostcode":"3123","total":3},{"cinemaPostcode":"3148","total":2}]
                JSONArray array = null;
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < array.length(); i++) {
                    //String[] colHEAD = new String[]{"NAME", "RELEASE YEAR", "RATING"};
                    JSONObject obj = null;
                    try {
                        obj = array.getJSONObject(i);
                        yData.add(obj.getInt("total"));
                        xData.add(obj.getString("cinemaPostcode"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (array.length() > 0) {
                    addPieChartDataSet(yData, xData);
                }

            }
        }
    }


}

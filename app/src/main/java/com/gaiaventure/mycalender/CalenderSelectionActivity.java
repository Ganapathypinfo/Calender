package com.gaiaventure.mycalender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaiaventure.mycalender.adapter.CalenderCellAdapter;
import com.gaiaventure.mycalender.model.CalenderDateModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class CalenderSelectionActivity extends AppCompatActivity {
    private GridView calendarView;
    private static CalenderCellAdapter adapter1;
    private String APP_DATE, date_ser;
    private int yearValue;
//    private TextView selectedDayMonthYearButton;
    private int month, year;
    private Calendar _calendar;
    private TextView selectedDateTV, selectedmonthAndYear, nextMonth;
//    private ImageView nextMonth, prevMonth, backImageBtn, closeDialogIV;
//    private RelativeLayout finishLayout;
    private String fromScreen;
    private String DEFAULT_MONTH, DISPLAY, LIMITED_DAYS = null;
    public static final ArrayList<CalenderDateModel> calArray = new ArrayList<>();
    public static Boolean isnextArrowclicked = false;
    public static Boolean isprevArrowclicked = false;
    private TextView txtTitle;
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // testing sonar qube
        initializeViewControls();
        getValuesFromBundle();
        displayCalenderBasedOnSelectedYear();
        needToShowDefaultMonthFromAPI();

//        userSelectsNextMonth();

        try {
            calArray.clear();
            for (int i = 0; i <= adapter1.getCount(); i++) {
                CalenderDateModel calModel = new CalenderDateModel();
                calModel.setIsDateSelected("0");
                calArray.add(calModel);
            }
        } catch (Exception e) {

        }
    }
    /**
     * Initializing view controls
     */
    private void initializeViewControls() {
        selectedmonthAndYear = findViewById(R.id.selectedmonth_and_year);
        selectedDateTV = findViewById(R.id.seletctedDate);
        calendarView = findViewById(R.id.calendar);
        nextMonth = findViewById(R.id.nextMonth);
    }
    /**
     * get values from previou activty
     * <p>
     * DEFAULT_MONTH-->comes from API
     * yearValue-->selected year from popUp
     */
    private void getValuesFromBundle() {

        try {

            DEFAULT_MONTH =  String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
            yearValue = Calendar.getInstance().get(Calendar.YEAR);
            LIMITED_DAYS = null;
            fromScreen = DISPLAY = "Valid Date";

        } catch (Exception e) {

        }
    }


    /**
     * show default year calender
     */
    private void displayCalenderBasedOnSelectedYear() {

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = yearValue;

    }


    /**
     * This method is for showing default month of the calender from API
     */
    private void needToShowDefaultMonthFromAPI() {

        try {
            month = Integer.parseInt(DEFAULT_MONTH);
            adapter1 = new CalenderCellAdapter(this,
                        R.id.calendar_day_gridcell, month, year, calendarView, selectedDateTV, selectedmonthAndYear, nextMonth,/*finishLayout,*/ fromScreen, DEFAULT_MONTH);
            calendarView.setAdapter(adapter1);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * goes back to next month from current month when user clicks on nextMonth button
     */
   /* private void userSelectsNextMonth() {


        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isnextArrowclicked = true;

                if (month > 11) {
                    month = 1;
                    // year++;
                } else {
                    month++;
                }


                selectedmonthAndYear.setText("");

                setGridCellAdapterToDate(month, year);
            }
        });

    }*/
    /**
     * @param month -passing month to gridview to show calender dates
     * @param year  - passing year to gridview to show calender dates
     */
    private void setGridCellAdapterToDate(int month, int year) {

        try {

            adapter1 = new CalenderCellAdapter(this,
                        R.id.calendar_day_gridcell, month, year, calendarView, selectedDateTV, selectedmonthAndYear, nextMonth, fromScreen, DEFAULT_MONTH);

            _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
            adapter1.notifyDataSetChanged();
            calendarView.setAdapter(adapter1);

            calArray.clear();

            for (int i = 0; i <= adapter1.getCount(); i++) {
                CalenderDateModel calModel = new CalenderDateModel();
                calModel.setIsDateSelected("0");
                calArray.add(calModel);
            }
        } catch (Exception e) {

        }
    }
}
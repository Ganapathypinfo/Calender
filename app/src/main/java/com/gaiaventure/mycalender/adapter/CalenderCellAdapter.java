package com.gaiaventure.mycalender.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaiaventure.mycalender.CalenderSelectionActivity;
import com.gaiaventure.mycalender.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CalenderCellAdapter extends BaseAdapter {
    private static final String tag = "CalenderCellAdapter";
    private final Context _context;
    private String APP_DATE, date_ser, FROM;
    private final List<String> list;
    private static final int DAY_OFFSET = 1;
    private final String[] months = {"01", "02", "03",
            "04", "05", "06", "07", "08", "09",
            "10", "11", "12"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31};
    private static int daysInMonth;// SonarQube issue
    private int currentDayOfMonth;
    private int currentWeekDay;
    private TextView num_events_per_day, selectedDateTV, selectedDayMonthYearButton, nextMonth;
    String date_month_year;
    String ua;
    private final HashMap<String, Integer> eventsPerMonthMap;
    private final SimpleDateFormat dateFormatter;
    private GridView calendarView;
//    private RelativeLayout finishLayout;
    public static String selectedDOB, selectedKTPValidity, fromDateLocale, toDateLocale, fromDate, toDate;// SonarQube issue
    public static String serverRequiredDOBFormat, serverRequiredKTPValidityFormat;
    private ViewHolder holder = null;
    private ArrayList<String> daysArray = new ArrayList<>();
    // Days in Current Month
    private Boolean isSelected = false;
    private String DEFAULT_MONTH;
    Locale locale;

    private Boolean preMonthFlag = null;
    private int limitedDays;
    // SonarQube issue

    /**
     * @param context                    - set context
     * @param textViewResourceId         - set textview id
     * @param month                      - set month
     * @param year                       - set year
     * @param calendarview               - set calendarview
     * @param selectedTV                 - set text
     * @param selectedDayMonthYearButton - set selected day month year
     * @param fromm                      - set the from value
     * @param monthdefault               - set the default month
     */
    public CalenderCellAdapter(Context context, int textViewResourceId,
                               int month, int year, GridView calendarview, TextView selectedTV, TextView selectedDayMonthYearButton, TextView nextMonth, /*RelativeLayout finishlayout,*/ String fromm, String monthdefault) {
        super();
        this._context = context;
        this.list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        this.calendarView = calendarview;
        this.selectedDateTV = selectedTV;
        this.selectedDayMonthYearButton = selectedDayMonthYearButton;
        this.nextMonth = nextMonth;
//        this.finishLayout = finishlayout;
        this.FROM = fromm;
        this.DEFAULT_MONTH = monthdefault;
//        locale = new Locale(PreferenceManager.getInstance().getLanguageCode(_context), PreferenceManager.getInstance().getCountryCode(_context));
        locale = Locale.getDefault();
        // locale = new Locale("id","IDN");
        preMonthFlag = false;
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", locale);
        //  setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));


        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
//}

        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

        // Print Month
        printMonth(month, year);

        // Find Number of Events
        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
    }
    /**
     * @param context                    - set context
     * @param textViewResourceId         - set textview id
     * @param month                      - set month
     * @param year                       - set year
     * @param calendarview               - set calendarview
     * @param selectedTV                 - set text
     * @param selectedDayMonthYearButton - set selected day month year
     * @param fromm                      - set the from value
     * @param monthdefault               - set the default month
     */
    public CalenderCellAdapter(Context context, Boolean preMonthFlag, int limitedDays ,int textViewResourceId,
                               int month, int year, GridView calendarview, TextView selectedTV, TextView selectedDayMonthYearButton,TextView nextMonth, /*RelativeLayout finishlayout,*/ String fromm, String monthdefault) {
        super();
        this._context = context;
        this.list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        this.calendarView = calendarview;
        this.selectedDateTV = selectedTV;
        this.selectedDayMonthYearButton = selectedDayMonthYearButton;
        this.nextMonth = nextMonth;
        this.FROM = fromm;
        this.preMonthFlag = preMonthFlag;
        this.limitedDays = limitedDays;

        this.DEFAULT_MONTH = monthdefault;
//        locale = new Locale(PreferenceManager.getInstance().getLanguageCode(_context), PreferenceManager.getInstance().getCountryCode(_context));
        locale = Locale.getDefault();
        // locale = new Locale("id","IDN");

        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", locale);
        //  setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));


        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
//}

        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

        // Print Month
        printMonth(month, year);

        // Find Number of Events
        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
    }

    /**
     * @param i - month number
     * @return - returns month in string format
     */
    private String getMonthAsString(int i) {
        return months[i];
    }


    /**
     * @param i - days of month
     * @return - returns number of days of month
     */
    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    /**
     * @param position - set position
     * @return - returns the item
     */
    public String getItem(int position) {
        return list.get(position);
    }

    /**
     * @return - total list count
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * @param mm - set month
     * @param yy - set year
     *           Displays the month
     */
    private void printMonth(int mm, int yy) {
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = 0 ;

        if(preMonthFlag){
            currentMonth = mm-1;
        }else{
            currentMonth = mm;
        }
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        if (cal.isLeapYear(cal.get(Calendar.YEAR))) {
            if (mm == 2) {
                ++daysInMonth;
            } else if (mm == 3) {
                ++daysInPrevMonth;
            }
        }

        for (int i = 0; i < trailingSpaces; i++) {
            list.add(((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                    + i)
                    + "-GREY"
                    + "-"
                    + getMonthAsString(prevMonth)
                    + "-"
                    + prevYear);
        }

        boolean isGotCurrentDate = false;
        int defaultdate = (Integer.parseInt(DEFAULT_MONTH)-1);
        int findLimitedDate=0;
        for (int i = 1; i <= daysInMonth; i++) {
            if (i == getCurrentDayOfMonth()) {

                if(preMonthFlag != null && preMonthFlag && defaultdate== currentMonth){
                    isGotCurrentDate = true;
                    list.add(i + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }else if(preMonthFlag != null && !preMonthFlag){
                    list.add(i + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            } else {
                if(preMonthFlag != null && preMonthFlag){
                    if(!isGotCurrentDate && (defaultdate== currentMonth || defaultdate > currentMonth)){
                        list.add(i + "-GREY" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }else if((defaultdate+1) == currentMonth){
                        findLimitedDate++;
                        list.add(i + "-WHITE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }else if(isGotCurrentDate){
                        findLimitedDate++;
                        list.add(i + "-WHITE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }else{
                        findLimitedDate++;
                        list.add(i + "-WHITE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }

                    if(limitedDays == findLimitedDate){
                        break;
                    }
                }else if(preMonthFlag != null && !preMonthFlag){

                    list.add(i + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }


        }

        for (int i = 0; i < list.size() % 7; i++) {
            list.add((i + 1) + "-GREY" + "-"
                    + getMonthAsString(nextMonth) + "-" + nextYear);
        }
    }


    /**
     * @param year  - set year
     * @param month - set month
     * @return - returns number of events per month
     */
    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        return map;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        private Button gridcell;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.date_list_item, null);
            holder = new ViewHolder();
            holder.gridcell = row.findViewById(R.id.calendar_day_gridcell);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        try {
            if (CalenderSelectionActivity.calArray.get(position).getIsDateSelected().equalsIgnoreCase("0")) {
                holder.gridcell.setBackgroundResource(0);
                holder.gridcell.setTextColor(_context.getResources().getColor(R.color.take_beans_from));
            } else {
                holder.gridcell.setBackgroundResource(R.drawable.round_selecter);
                holder.gridcell.setTextColor(_context.getResources().getColor(R.color.white));
            }
           /* if (selectedDateTV.getText().length() == 0) {
                finishLayout.setEnabled(false);
                finishLayout.setClickable(false);
                finishLayout.setFocusable(false);
                finishLayout.setBackgroundColor(_context.getResources().getColor(R.color.grey_button));
            } else {
                finishLayout.setEnabled(true);
                finishLayout.setClickable(true);
                finishLayout.setFocusable(true);
                finishLayout.setBackgroundColor(_context.getResources().getColor(R.color.green_button));
            }*/
        } catch (Exception e) {

        }
        /**
         * to change Current Day color
         */
        holder.gridcell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userselectsDateInCalender(v, position);
            }
        });
        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];
        if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
            if (eventsPerMonthMap.containsKey(theday)) {
                num_events_per_day = row
                        .findViewById(R.id.num_events_per_day);
                Integer numEvents = eventsPerMonthMap.get(theday);
                //TODOOOOOooooooooo
                num_events_per_day.setText(numEvents.toString());
            }
        }
        holder.gridcell.setText(theday);

        holder.gridcell.setTag(theday + " " + themonth + " " + theyear);
        date_ser = theyear + "-" + themonth + "-" + theday;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", locale);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", locale);
        String formattedDate = df.format(c.getTime());
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(formattedDate);
            d2 = format.parse(date_ser);
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {


        }
        nextMonth.setText(formatMonth(String.valueOf(Integer.parseInt(themonth)+1)));

        if (day_color[1].equals("GREY")) {
//            holder.gridcell.setTextColor(_context.getResources()
//                    .getColor(R.color.lightgray));
            holder.gridcell.setText("");
            holder.gridcell.setClickable(false);

        }
        if (day_color[1].equals("WHITE")) {


            try {

                if (CalenderSelectionActivity.isprevArrowclicked == true) {
                    String[] splitthemth = selectedDateTV.getText().toString().split(" ");
                    String onlymonth = splitthemth[1];

                    //


                    if (selectedDayMonthYearButton.getText().toString().equals(onlymonth) && Integer.parseInt(holder.gridcell.getText().toString()) == Integer.parseInt(splitthemth[0])) {
                        holder.gridcell.setBackgroundResource(R.drawable.round_circle);
                        holder.gridcell.setTextColor(_context.getResources().getColor(R.color.link_green));

                    }
                }

                if (CalenderSelectionActivity.isnextArrowclicked == true) {
                    String[] splitthemth = selectedDateTV.getText().toString().split(" ");
                    String onlymonth = splitthemth[1];


                    if (selectedDayMonthYearButton.getText().toString().equals(onlymonth) && Integer.parseInt(holder.gridcell.getText().toString()) == Integer.parseInt(splitthemth[0])) {
                        holder.gridcell.setBackgroundResource(R.drawable.round_circle);

                        holder.gridcell.setTextColor(_context.getResources().getColor(R.color.link_green));
                    }
                }
            } catch (Exception e) {
            }


//            holder.gridcell.setTextColor(_context.getResources().getColor(R.color.take_beans_from));
            if (Integer.parseInt(theday) < 10) {
                theday = "0" + theday;
            }

            String date1 = theyear + "-" + themonth + "-" + theday;
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", locale);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", locale);
            String formattedDate1 = df1.format(c1.getTime());
            try {
                d1 = format.parse(formattedDate1);
                d2 = format.parse(date1);
                long diff = d2.getTime() - d1.getTime();
                if (diff == 0) {
                    holder.gridcell.setTextColor(_context.getResources().getColor(R.color.blue));
                } else {
                }
            } catch (Exception e) {

            }
            APP_DATE = theyear + "-" + themonth + "-" + theday;


            selectedDayMonthYearButton.setText(formatMonth(themonth) + "," + theyear);



        }
        if (day_color[1].equals("BLUE")) {
            if (Integer.parseInt(theday) < 10) {
                theday = "0" + theday;
            }
            String date1 = theyear + "-" + themonth + "-" + theday;
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", locale);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", locale);
            String formattedDate1 = df1.format(c1.getTime());
            try {
                d1 = format.parse(formattedDate1);
                d2 = format.parse(date1);
                long diff = d2.getTime() - d1.getTime();
                if (diff == 0) {
                    holder.gridcell.setTextColor(_context.getResources().getColor(R.color.blue));
                    holder.gridcell.setBackgroundResource(R.drawable.round_circle);
                } else {
                }
            } catch (Exception e) {

            }
        }
        return row;
    }


    /**
     * @param v        - set the view
     * @param position - set selected position
     */
    private void userselectsDateInCalender(View v, int position) {

        try {

            isSelected = true;
            Button selectedDateTv = (Button) v;
/**
 * Change the color of clicked Date
 */

            try {
                if (CalenderSelectionActivity.calArray.get(position).getIsDateSelected().equals("1")) {
                    CalenderSelectionActivity.calArray.get(position).setIsDateSelected("0");
                } else {
                    for (int i = 0; i < CalenderSelectionActivity.calArray.size(); i++) {
                        if (i == position) {
                            CalenderSelectionActivity.calArray.get(i).setIsDateSelected("1");
                        } else {
                            CalenderSelectionActivity.calArray.get(i).setIsDateSelected("0");
                        }
                    }
                }
                //CalandetrSelectionActivity.calArray.clear();
                notifyDataSetChanged();
            } catch (Exception e) {

            }


            date_month_year = (String) v.getTag();

            String daaatee = date_month_year;

            String[] separated = daaatee.split(" ");
            String selectedDate = separated[0];
            String selectedMonth = separated[1];
            String selectedYear = separated[2];

            APP_DATE = separated + " " + selectedMonth + " " + selectedYear;


            showSelectedDateInLabel(selectedDate, selectedMonth, selectedYear);


            Date parsedDate = dateFormatter.parse(date_month_year);


        } catch (Exception e) {

        }
    }

    /**
     * @param selectedDate  - set selected date
     * @param selectedMonth - set selected month
     * @param selectedYear  - set selected year
     *                      Displays the selected date
     */
    private void showSelectedDateInLabel(String selectedDate, String selectedMonth, String selectedYear) {

        try {

            if (FROM.equalsIgnoreCase("KTP")) {
                //  Constants.isDOBEdited = true;
//                Constants.isNationalIDValidityEdited = true;
                if (selectedDate.length() == 1) {
                    selectedDate = "0" + selectedDate;
                }
                serverRequiredKTPValidityFormat = selectedYear + "-" + selectedMonth + "-" + selectedDate;

//                PreferenceManager.getInstance().setUserKTPNumValidity(_context, serverRequiredKTPValidityFormat);
            } else {
//                Constants.isDOBEdited = true;

                if (selectedDate.length() == 1) {
                    selectedDate = "0" + selectedDate;
                }

                serverRequiredDOBFormat = selectedYear + "-" + selectedMonth + "-" + selectedDate;
//                PreferenceManager.getInstance().setUserDOB(_context, serverRequiredDOBFormat);

            }


            selectedDateTV.setText(selectedDate + " " + formatMonth(selectedMonth) + " " + selectedYear);


            selectedDateTV.setTag(selectedYear + "-" + selectedMonth + "-" + selectedDate);

        } catch (Exception e) {
            throw e;
        }


    }

    public String formatMonth(String month) {
        SimpleDateFormat monthParse = null;
        SimpleDateFormat monthDisplay = null;
        try {
            monthParse = new SimpleDateFormat("MM", locale);
            monthDisplay = new SimpleDateFormat("MMMM", locale);

            return monthDisplay.format(monthParse.parse(month));
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * @return - get current day of month
     */
    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    /**
     * @param currentDayOfMonth - set current day of month
     */
    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    /**
     * @param currentWeekDay - set current day of the week
     */
    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

}

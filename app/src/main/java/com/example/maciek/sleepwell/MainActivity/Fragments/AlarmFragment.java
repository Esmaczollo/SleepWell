package com.example.maciek.sleepwell.MainActivity.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.maciek.sleepwell.MainActivity.MainActivity;
import com.example.maciek.sleepwell.R;
import com.example.maciek.sleepwell.SleepingActivity.SleepingActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.lang.reflect.Field;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * Created by Maciek on 27.04.2017.
 */

public class AlarmFragment extends Fragment {

    TimePicker timePicker;
    Button setAlarmButton;
    TextView wakeUpTV, timeOfSleepTV;
    public String firstTimeOfWakeUp,secondTimeOfWakeUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.alarm_fragment);
        MainActivity.isBackToMainActivity = false;
        setAlarmButton = (Button)view.findViewById(R.id.setAlarm);
        timePicker = (TimePicker)view.findViewById(R.id.timePicker);
        wakeUpTV = (TextView)view.findViewById(R.id.wakeUpTV);
        timeOfSleepTV = (TextView)view.findViewById(R.id.timeOfSleepTV);

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstIndex = firstTimeOfWakeUp.indexOf("beetwen") + 8;
                int lastIndex = firstTimeOfWakeUp.length();

                MainActivity.isBackToMainActivity = false;
                Intent intent = new Intent(getActivity(),SleepingActivity.class).putExtra("firstTimeOfWakeUp", firstTimeOfWakeUp.substring(firstIndex, lastIndex)); // This is not working in class witch extends Fragment
                startActivity(intent);
            }
        });
        MainActivity.isBackToMainActivity = true;

        setTimepickerTextColour();
        timePickerVisualChanges();
        hourOfWakeUp();
    }



    private void timePickerVisualChanges(){
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    private void hourOfWakeUp(){

        final long oneMinuteInMilliseconds=60000;
        final int addedMinutes = 30;
        final int currentHour,currentMinute,afterAddedHour,afterAddedMinutes;

        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+02:00"));//MAking @Calendar object and manually setting Poland/Warsaw time zone
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        Date afterAddingMinutes = new Date(calendar.getTimeInMillis() + (addedMinutes * oneMinuteInMilliseconds)); // Adding minutes to current hour
        calendar.setTime(afterAddingMinutes); //update date after added minutes
        afterAddedHour = calendar.get(Calendar.HOUR_OF_DAY);
        afterAddedMinutes = calendar.get(Calendar.MINUTE);

        /*
        Setting current hour on timepicker
         */
        timePicker.setCurrentHour(currentHour);
        timePicker.setCurrentMinute(currentMinute);

        String firstTime = String.format("%02d:%02d",currentHour,currentMinute);
        String secondTime= String.format("%02d:%02d",afterAddedHour,afterAddedMinutes);
        wakeUpTV.setText("You will wake up beetwen " + firstTime + " and " + secondTime);

        firstTimeOfWakeUp = wakeUpTV.getText().toString();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int  firstHourOfWakeUp, firstMinutesOfWakeUp,secondHourOfWakeUp,secondMinutesOfWakeUp; //this is for wakeUpTV
                long hoursOfSleep=0, minutesOfSleep=0,differenceOfSleep=0,secondsOfSleep=0; //this is for timeOfSleepTV

                firstHourOfWakeUp =   timePicker.getCurrentHour(); //is deprecated in API 23 but we use API 21,
                firstMinutesOfWakeUp = timePicker.getCurrentMinute();

                /*
                This "if" calculate the time interval in witch user will wake up
                 */
                if(firstMinutesOfWakeUp>=60-addedMinutes){
                    secondHourOfWakeUp = firstHourOfWakeUp + 1;
                    secondMinutesOfWakeUp = firstMinutesOfWakeUp - (60-addedMinutes);
                }else{
                    secondHourOfWakeUp = firstHourOfWakeUp;
                    secondMinutesOfWakeUp = firstMinutesOfWakeUp + addedMinutes; //addedMinutes=30
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar tomorrowCalendar = Calendar.getInstance();
                Date currentDate = new Date();
                currentDate.setTime(tomorrowCalendar.getTimeInMillis()); //added 2 hours

                /*
                We provide properly setting a new day
                 */
                if((currentDate.getHours()>=timePicker.getCurrentHour() &&
                    currentDate.getMinutes()>timePicker.getCurrentMinute()) ||
                        currentDate.getHours()>timePicker.getCurrentHour()) {
                    tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                Date tomorrowDate = tomorrowCalendar.getTime();
                tomorrowDate.setHours(firstHourOfWakeUp);
                tomorrowDate.setMinutes(firstMinutesOfWakeUp);
                tomorrowDate.setSeconds(0);

                String wakeUpString = dateFormat.format(tomorrowDate);


                try {
                    Date wakeUpDate = dateFormat.parse(wakeUpString);

                    differenceOfSleep = wakeUpDate.getTime() - currentDate.getTime();
                    secondsOfSleep = differenceOfSleep / 1000;
                    minutesOfSleep = (secondsOfSleep + 60) / 60;
                    hoursOfSleep = minutesOfSleep / 60;
                    minutesOfSleep = ((secondsOfSleep + 60) / 60)%60; // +60 bo mi kurwa obcina jak formatuje do int'a
                } catch (ParseException e) {
                    Log.e("ERROR","Złapało w setOnTimeChangeListener");

                    e.printStackTrace();
                }

                String first= String.format("%02d:%02d",firstHourOfWakeUp,firstMinutesOfWakeUp);
                String second= String.format("%02d:%02d",secondHourOfWakeUp,secondMinutesOfWakeUp);
                String timeOfSleep= String.format("%02d:%02d",hoursOfSleep,minutesOfSleep);
                firstTimeOfWakeUp = wakeUpTV.getText().toString();

                wakeUpTV.setText("You will wake up beetwen " + first + " and " + second);
                timeOfSleepTV.setText("The time of your sleep is (at least) " + timeOfSleep);
            }
        });
    }



    private void setTimepickerTextColour(){
        Resources system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        //int line_numberpicker_id = system.getIdentifier("selectionDivider", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        //NumberPicker line_numberpicker = (NumberPicker) timePicker.findViewById(line_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        //set_numberpicker_text_colour(line_numberpicker);

    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color = getResources().getColor(R.color.numberPickerColor);

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                //Field linePaintField= number_picker.getClass().getDeclaredField("mSelectionDivider");
                //linePaintField.setAccessible(true);


                ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
                //((Paint)linePaintField.get(number_picker)).setColor(color);
                ((EditText)child).setTextColor(color);

                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
            }
            catch(IllegalAccessException e){
            }
            catch(IllegalArgumentException e){
            }
        }
    }



}

package ru.edu.qamid.ui.actions;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.TimePicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

public class TimePickerActions {
    public static ViewAction setTime(final int hour, final int minute) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(TimePicker.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "Set time to " + hour + ":" + minute;
            }

            @Override
            public void perform(UiController uiController, View view) {
                TimePicker timePicker = (TimePicker) view;
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            }
        };
    }
}
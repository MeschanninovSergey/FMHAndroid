package ru.edu.qamid.ui.actions;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

public class DatePickerActions {
    public static ViewAction setDate(final int year, final int month, final int day) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(DatePicker.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "Set date to " + year + "-" + month + "-" + day;
            }

            @Override
            public void perform(UiController uiController, View view) {
                DatePicker datePicker = (DatePicker) view;
                // Устанавливаем дату напрямую через API виджета (это быстрее и стабильнее прокрутки)
                datePicker.updateDate(year, month - 1, day);
            }
        };
    }
}
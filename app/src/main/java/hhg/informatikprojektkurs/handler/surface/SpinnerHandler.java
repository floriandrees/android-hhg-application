package hhg.informatikprojektkurs.handler.surface;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Settings;
import hhg.informatikprojektkurs.constants.design.SpinnerPadding;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;

public class SpinnerHandler {
    public static ArrayAdapter<String> createSpinnerAdapter(final Activity activity, final List<String> content, final Typeface typeface) {
        final boolean isTypefaceSpinner = activity.getClass() == Settings.class;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout._spinner_design, content) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(isTypefaceSpinner) {
                    textView.setTypeface(TypefaceHandler.getTypeface(activity, position));
                } else {
                    textView.setTypeface(typeface);
                }

                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);

                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(isTypefaceSpinner) {
                    textView.setTypeface(TypefaceHandler.getTypeface(activity, position));
                } else {
                    textView.setTypeface(typeface);
                }

                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);

                view.setPadding(SpinnerPadding.LEFT, SpinnerPadding.TOP, SpinnerPadding.RIGHT, SpinnerPadding.BOTTOM);
                return view;
            }
        };

        return arrayAdapter;
    }

    public static Spinner setupSpinner(Spinner spinner, ArrayAdapter<String> arrayAdapter, boolean maxHeight) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            if(maxHeight) {
                popupWindow.setHeight(Constants.SPINNER_POPUP_WINDOW_HEIGHT);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        spinner.setAdapter(arrayAdapter);
        return spinner;
    }
}

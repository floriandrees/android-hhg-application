package hhg.informatikprojektkurs.design;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.constants.Constants;

public class SnackbarDesign {

    public static final int NO_CONNECTION = 1;
    public static final int CHOSEN_CLASS = 2;

    public static final int LOGGED_IN = 11;
    public static final int LOGGED_OUT = 12;

    public static final int PASSWORD_HINT = 21;

    public static Snackbar getSnackbar(View view, Activity activity, Typeface typeface, String text, int type) {
        Snackbar sb = Snackbar.make(view, Html.fromHtml(text), Constants.SNACKBAR_DURATION);

        switch (type) {
            case NO_CONNECTION: case LOGGED_OUT: case PASSWORD_HINT:
                sb.getView().setBackgroundColor(activity.getResources().getColor(R.color.bootstrap_ALIZARIN));
                break;
            case CHOSEN_CLASS: case LOGGED_IN:
                sb.getView().setBackgroundColor(activity.getResources().getColor(R.color.bootstrap_EMERALD));
                break;
        }

        TextView textView = sb.getView().findViewById(R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(typeface);
        textView.setTextSize(Constants.SNACKBAR_TEXT_SIZE);

        return sb;
    }
}

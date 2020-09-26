package hhg.informatikprojektkurs.listener.spinner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;

public class SettingsTypefaceListener implements AdapterView.OnItemSelectedListener {

    private Activity activity;
    private SharedPreferences sharedPreferences;

    public SettingsTypefaceListener(Activity activity, SharedPreferences sharedPreferences){
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        TextView title = getActivity().findViewById(R.id.title);
        TextView developer = getActivity().findViewById(R.id.developer);
        TextView typefaceBanner = getActivity().findViewById(R.id.banner_typeface);

        if(sharedPreferences.getInt(KeySharedPreferences.TYPEFACE, Constants.ZERO) != position) {
            editor.putInt(KeySharedPreferences.TYPEFACE, position);
            editor.apply();

            typefaceHandler.setCurrentTypeface(TypefaceHandler.getTypeface(getActivity(), position));
            SnackbarDesign.getSnackbar(view, getActivity(), typefaceHandler.getCurrentTypeface(), "Ausgewählt - <b>" + parent.getItemAtPosition(position).toString() + "</b>", SnackbarDesign.CHOSEN_CLASS).show();
        }

        typefaceHandler.setTextViewTypeface(typefaceHandler.getCurrentTypeface(), title, typefaceBanner, developer);
        typefaceHandler.setTextViewColor(Color.WHITE, typefaceBanner, developer);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Activity getActivity() {
        return this.activity;
    }

    public SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }
}

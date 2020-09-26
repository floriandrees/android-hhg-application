package hhg.informatikprojektkurs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.handler.surface.SpinnerHandler;
import hhg.informatikprojektkurs.listener.spinner.SettingsTypefaceListener;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;

import static hhg.informatikprojektkurs.constants.Constants.ZERO;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.backToMenu(Settings.this);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(KeySharedPreferences.NAME, ZERO);

        TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;

        TextView developer = findViewById(R.id.developer);
        TextView typefaceBanner = findViewById(R.id.banner_typeface);

        developer.setText(Html.fromHtml(getString(R.string.activity_developer)));
        typefaceBanner.setText(Html.fromHtml(getString(R.string.settings_change_typeface_text)));

        Spinner spinner = findViewById(R.id.spinner_typeface);
        setupSpinner(spinner, typefaceHandler.getCurrentTypeface(), sharedPreferences);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setupSpinner(Spinner spinner, Typeface typeface, SharedPreferences sharedPreferences) {
        spinner.setOnItemSelectedListener( new SettingsTypefaceListener(Settings.this, sharedPreferences));

        SpinnerHandler.setupSpinner(spinner, SpinnerHandler.createSpinnerAdapter(Settings.this, getTypefaces(), typeface), false);
        spinner.setSelection(sharedPreferences.getInt(KeySharedPreferences.TYPEFACE, ZERO));
    }

    private List<String> getTypefaces() {
        List<String> content = new ArrayList<>();

        content.add("Source Sans Pro");
        content.add("Lato");
        content.add("Roboto");
        content.add("Marvel");

        return content;
    }
}
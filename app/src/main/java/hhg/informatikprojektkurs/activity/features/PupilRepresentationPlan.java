package hhg.informatikprojektkurs.activity.features;

import java.util.ArrayList;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.os.Bundle;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.graphics.Typeface;
import android.view.View;
import android.webkit.WebView;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.activity.Information;
import hhg.informatikprojektkurs.constants.background.Charsets;
import hhg.informatikprojektkurs.constants.background.Links;
import hhg.informatikprojektkurs.constants.background.FileNames;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.constants.background.Regex;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.handler.connection.ConnectionHandler;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.handler.surface.SpinnerHandler;
import hhg.informatikprojektkurs.handler.functions.PupilDataHandler;
import hhg.informatikprojektkurs.interfaces.IFunction;
import hhg.informatikprojektkurs.interfaces.IWebReader;
import hhg.informatikprojektkurs.listener.spinner.PupilListener;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;
import hhg.informatikprojektkurs.handler.files.FileHandler;
import hhg.informatikprojektkurs.download.FileDownloader;

public class PupilRepresentationPlan extends AppCompatActivity {

    private TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;
    private Typeface typeface;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> dataList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pupil_plan_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_down));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.backToMenu(PupilRepresentationPlan.this);
            }
        });

        // =========================================================================================
        // Use of global SharedPreferences

        sharedPreferences = getSharedPreferences(KeySharedPreferences.NAME, Constants.ZERO);
        int selection = sharedPreferences.getInt(KeySharedPreferences.STUDENT_REP_PLAN, Constants.ZERO);

        // =========================================================================================

        dataList = new ArrayList<>();

        typeface = typefaceHandler.getCurrentTypeface();

        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        TextView updated = findViewById(R.id.updated);

        typefaceHandler.setTextViewTypeface(typeface, title, date, updated);
        typefaceHandler.setTextViewColor(Color.WHITE, date, updated);

        checkIntentAndDoActions(date, updated);

        Spinner spinner = findViewById(R.id.spinner);
        setupSpinner(spinner, selection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.representation_plan_menu, menu);
        typefaceHandler.getMenu(menu, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String directoryPath = getCacheDir().getPath() + "/files/";

        HashMap<String, Object> intentExtras = new HashMap<>();

        switch(id) {
            case R.id.vertretungsplan_refresh:
                if(getIntent().hasExtra(KeyIntent.TOMORROW)) {
                    intentExtras.put(KeyIntent.TOMORROW, Constants.ZERO);
                } else {
                    intentExtras = null;
                    if(ConnectionHandler.isConnectionActive(this)) {
                        FileDownloader.download(
                                Links.Today.STUDENT_REP_PLAN,
                                directoryPath + FileNames.Today.STUDENT_REP_PLAN,
                                Charsets.ISO_8859_1);
                    }
                }
                this.finish();
                NavigationHandler.navigate(PupilRepresentationPlan.this, PupilRepresentationPlan.class, intentExtras, SlideDirection.LEFT);
                break;
            case R.id.vertretungsplan_naechster:
                if(ConnectionHandler.isConnectionActive(PupilRepresentationPlan.this)) {
                    intentExtras.put(KeyIntent.TOMORROW, Constants.ZERO);
                    this.finish();
                    NavigationHandler.navigate(PupilRepresentationPlan.this, PupilRepresentationPlan.class, intentExtras, SlideDirection.LEFT);
                } else {
                    SnackbarDesign.getSnackbar(findViewById(R.id.pupil_plan_content), PupilRepresentationPlan.this, typeface, "Keine Internetverbindung vorhanden.", SnackbarDesign.NO_CONNECTION).show();
                }
                break;
            case R.id.vertretungsplan_informationen:
                intentExtras.put(KeyIntent.CLASS_TO_NAVIGATE, this.getClass().getName());
                NavigationHandler.navigate(PupilRepresentationPlan.this, Information.class, intentExtras, SlideDirection.LEFT);
                break;
            case R.id.vertretungsplan_zurueck:
                this.finish();
                NavigationHandler.navigate(PupilRepresentationPlan.this, PupilRepresentationPlan.class, null, SlideDirection.RIGHT);
                break;
            case R.id.vertretungsplan_original:
                if(ConnectionHandler.isConnectionActive(PupilRepresentationPlan.this)) {
                    WebView webView;
                    if(getIntent().hasExtra(KeyIntent.TOMORROW)) {
                        webView = IWebReader.createWebsiteWebView(PupilRepresentationPlan.this, Links.Tomorrow.STUDENT_REP_PLAN);
                    } else {
                        webView = IWebReader.createWebsiteWebView(PupilRepresentationPlan.this, Links.Today.STUDENT_REP_PLAN);
                    }
                    setContentView(webView);
                } else {
                    SnackbarDesign.getSnackbar(findViewById(R.id.pupil_plan_content), PupilRepresentationPlan.this, typeface,
                            "<b>" + getString(R.string.snackbar_text_no_connection) + "</b>",
                            SnackbarDesign.NO_CONNECTION).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavigationHandler.backToMenu(PupilRepresentationPlan.this);
    }

    private void checkIntentAndDoActions(TextView date, TextView updated) {
        File directory = new File(getCacheDir().getPath() + "/files/");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        ArrayList<String> rawList = new ArrayList<>();
        File file;

        boolean isFromNavigation = getIntent().hasExtra(KeyIntent.FROM_NAVIGATION);
        boolean isTomorrowPlan = getIntent().hasExtra(KeyIntent.TOMORROW);

        String absolutePath = directory.getAbsolutePath() + "/";

        if(isFromNavigation) { // Called from Navigation (Homepage.java)
            rawList = getIntent().getStringArrayListExtra(KeyIntent.RAW_LIST);
            dataList = getIntent().getStringArrayListExtra(KeyIntent.DATA_LIST);
        } else if(isTomorrowPlan) { // Called by itself + showing tomorrows plan
            if(ConnectionHandler.isConnectionActive(this)) {
                FileDownloader.download(Links.Tomorrow.STUDENT_REP_PLAN, directory.getAbsolutePath() + "/" + FileNames.Tomorrow.STUDENT_REP_PLAN, Charsets.ISO_8859_1);
            }
            absolutePath += FileNames.Tomorrow.STUDENT_REP_PLAN;
        } else { // Called by itself, whether by refresh or back to today button
            absolutePath += FileNames.Today.STUDENT_REP_PLAN;
        }

        if(!absolutePath.equals(directory.getAbsolutePath() + "/")) {
            file = new File(absolutePath);

            if(file.exists()) {
                IFunction iFunction = new PupilDataHandler();

                rawList = iFunction.readFile(file);
                dataList = iFunction.prepareData(iFunction.findData(rawList));
            }
        }

        searchGlobalInformation(rawList, date, updated);
    }

    private void searchGlobalInformation(ArrayList<String> rawList, TextView date, TextView updated) {
        String dateContent = null;
        String updatedContent = null;

        for(String line : rawList) {
            if(line.contains("Stand")) {
                // TODO: Replace with Regex as possible (maybe use matcher?)

                line = line.replace("<span style=\"width:10px\">&nbsp;", ";");
                line = line.replace("</span>", "");
                line = line.substring(0, line.length() -4);
                String[] standArray = line.split(";");
                updatedContent = standArray[standArray.length - 1];
            }

            if(line.startsWith("<div class=\"mon_title\">")) {
                line = line.replaceAll(Regex.HTML_MATCHER, "").trim();
                dateContent = line;
                break;
            }
        }

        if(dateContent != null) {
            date.setText(Html.fromHtml("<b>" + dateContent + "</b>"));
        }
        if(updatedContent != null) {
            updated.setText(Html.fromHtml("<i>" + updatedContent + "</i>"));
        }
    }

    private void setupSpinner(Spinner spinner, int selection) {
        SpinnerHandler.setupSpinner(spinner, SpinnerHandler.createSpinnerAdapter(PupilRepresentationPlan.this, getClassesFromFile(), typeface), true);
        spinner.setOnItemSelectedListener(new PupilListener(PupilRepresentationPlan.this, sharedPreferences,dataList));
        spinner.setSelection(selection);
    }

    private List<String> getClassesFromFile() {
        List<String> content = new ArrayList<>();

        FileHandler fileHandler = new FileHandler(this);
        content.addAll(fileHandler.getLinesFromAssetsFile("classes.txt", false));

        return content;
    }
}
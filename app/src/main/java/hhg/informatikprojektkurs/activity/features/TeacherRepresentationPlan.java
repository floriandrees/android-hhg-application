package hhg.informatikprojektkurs.activity.features;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.activity.Information;
import hhg.informatikprojektkurs.constants.background.Charsets;
import hhg.informatikprojektkurs.constants.background.Links;
import hhg.informatikprojektkurs.constants.background.FileNames;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.background.Regex;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.handler.connection.ConnectionHandler;
import hhg.informatikprojektkurs.handler.functions.TeacherDataHandler;
import hhg.informatikprojektkurs.interfaces.IFunction;
import hhg.informatikprojektkurs.interfaces.IWebReader;
import hhg.informatikprojektkurs.listener.spinner.TeacherListener;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.handler.surface.SpinnerHandler;
import hhg.informatikprojektkurs.download.FileDownloader;

public class TeacherRepresentationPlan extends AppCompatActivity {

    private TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;
    private Typeface typeface;

    private List<String> rawList;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.teacher_plan_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_down));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.backToMenu(TeacherRepresentationPlan.this);
            }
        });

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
        setupSpinner(spinner, rawList);
    }

    @Override
    public void onBackPressed() {
        NavigationHandler.backToMenu(TeacherRepresentationPlan.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.representation_plan_menu, menu);
        Homepage.TYPEFACE_HANDLER.getMenu(menu, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String directoryPath = getCacheDir().getPath() + "/files/";

        HashMap<String, Object> intentExtras = new HashMap<>();

        boolean isTomorrowPlan = getIntent().hasExtra(KeyIntent.TOMORROW);

        switch(id) {
            case R.id.vertretungsplan_refresh:
                if(isTomorrowPlan) {
                    intentExtras.put(KeyIntent.TOMORROW, true);
                } else {
                    intentExtras = null;
                    if(ConnectionHandler.isConnectionActive(this)) {
                        FileDownloader.download(Links.Today.TEACHER_REP_PLAN, directoryPath + FileNames.Today.TEACHER_REP_PLAN, Charsets.ISO_8859_1);
                    }
                }

                this.finish();
                NavigationHandler.navigate(TeacherRepresentationPlan.this, TeacherRepresentationPlan.class, intentExtras, SlideDirection.LEFT);
                break;
            case R.id.vertretungsplan_naechster:
                if(ConnectionHandler.isConnectionActive(TeacherRepresentationPlan.this)) {
                    intentExtras.put(KeyIntent.TOMORROW, true);
                    this.finish();
                    NavigationHandler.navigate(TeacherRepresentationPlan.this, TeacherRepresentationPlan.class, intentExtras, SlideDirection.LEFT);
                } else {
                    SnackbarDesign.getSnackbar(findViewById(R.id.teacher_plan_content), TeacherRepresentationPlan.this, typeface, "Keine Internetverbindung vorhanden.", SnackbarDesign.NO_CONNECTION).show();
                }
                break;
            case R.id.vertretungsplan_informationen:
                intentExtras.put(KeyIntent.CLASS_TO_NAVIGATE, this.getClass().getName());
                NavigationHandler.navigate(TeacherRepresentationPlan.this, Information.class, intentExtras, SlideDirection.LEFT);
                break;
            case R.id.vertretungsplan_zurueck:
                this.finish();
                NavigationHandler.navigate(TeacherRepresentationPlan.this, TeacherRepresentationPlan.class, null, SlideDirection.RIGHT);
                break;
            case R.id.vertretungsplan_original:
                if(ConnectionHandler.isConnectionActive(TeacherRepresentationPlan.this)) {
                    WebView webView;

                    if(isTomorrowPlan) {
                        webView = IWebReader.createWebsiteWebView(this, Links.Tomorrow.TEACHER_REP_PLAN);
                    } else {
                        webView = IWebReader.createWebsiteWebView(this, Links.Today.TEACHER_REP_PLAN);
                    }
                    setContentView(webView);
                } else {
                    SnackbarDesign.getSnackbar(findViewById(R.id.teacher_plan_content), TeacherRepresentationPlan.this, typeface,
                            "<b>" + getString(R.string.snackbar_text_no_connection) + "</b>",
                            SnackbarDesign.NO_CONNECTION).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIntentAndDoActions(TextView date, TextView updated) {
        File directory = new File(getCacheDir().getPath() + "/files/");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        boolean isFromNavigation = getIntent().hasExtra(KeyIntent.FROM_NAVIGATION);
        boolean isTomorrowPlan = getIntent().hasExtra(KeyIntent.TOMORROW);

        String absolutePath = directory.getAbsolutePath() + "/";

        if(isFromNavigation) { // Called from Navigation (Homepage.java)
            rawList = getIntent().getStringArrayListExtra(KeyIntent.RAW_LIST);
            dataList = getIntent().getStringArrayListExtra(KeyIntent.DATA_LIST);
        } else if(isTomorrowPlan) { // Called by itself + showing tomorrows plan
            if(ConnectionHandler.isConnectionActive(this)) {
                FileDownloader.download(Links.Tomorrow.TEACHER_REP_PLAN, directory.getAbsolutePath() + "/" + FileNames.Tomorrow.TEACHER_REP_PLAN, Charsets.ISO_8859_1);
            }
            absolutePath += FileNames.Tomorrow.TEACHER_REP_PLAN;
        } else { // Called by itself, whether by refresh or back to today button
            absolutePath += FileNames.Today.TEACHER_REP_PLAN;
        }

        if(!absolutePath.equals(directory.getAbsolutePath() + "/")) {
            File file = new File(absolutePath);

            if(file.exists()) {
                IFunction iFunction = new TeacherDataHandler();

                rawList = iFunction.readFile(file);
                dataList = iFunction.prepareData(iFunction.findData(rawList));
            }
        }

        searchGlobalInformation(rawList, date, updated);
    }

    private void searchGlobalInformation(List<String> rawList, TextView date, TextView updated) {
        String dateContent = null;
        String updatedContent = null;

        for(String line : rawList) {
            if(line.contains("Stand")) {
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

    private void setupSpinner(Spinner spinner, List<String> rawList) {
        SpinnerHandler.setupSpinner(spinner, SpinnerHandler.createSpinnerAdapter(TeacherRepresentationPlan.this, getTeachersFromFile(rawList), typeface), true);
        spinner.setOnItemSelectedListener(new TeacherListener(TeacherRepresentationPlan.this, dataList));
    }

    private List<String> getTeachersFromFile(List<String> rawList) {
        List<String> content = new ArrayList<>();

        content.add("Keine Lehrkraft ausgewählt");

        for(String line : rawList) {
            if(line.contains("list inline_header")) {

                line = line.replaceAll(Regex.HTML_MATCHER, "");
                content.add(line);
            }
        }
        return content;
    }
}

package hhg.informatikprojektkurs.listener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.references.Appointments;
import hhg.informatikprojektkurs.activity.references.ExaminationSchedule;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.activity.features.PupilRepresentationPlan;
import hhg.informatikprojektkurs.activity.features.Staff;
import hhg.informatikprojektkurs.activity.features.TeacherRepresentationPlan;
import hhg.informatikprojektkurs.constants.background.Links;
import hhg.informatikprojektkurs.constants.background.FileNames;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.handler.connection.ConnectionHandler;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.handler.functions.PupilDataHandler;
import hhg.informatikprojektkurs.handler.functions.StaffDataHandler;
import hhg.informatikprojektkurs.handler.functions.TeacherDataHandler;
import hhg.informatikprojektkurs.interfaces.IFunction;

public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;

    private DrawerLayout drawerLayout;

    public NavigationListener(Activity activity, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        File file;
        File directory = new File(activity.getCacheDir().getPath() + "/files/");
        directory.mkdirs();

        ArrayList<String> rawList, dataList;
        IFunction iFunction;

        HashMap<String, Object> intentExtras = new HashMap<>();

        int direction = SlideDirection.LEFT;

        switch (id) {
            case R.id.nav_vertretungsplan: case R.id.nav_vertretungsplan_lehrer:
                Class destination;

                // Student's Representation Plan
                if(id == R.id.nav_vertretungsplan) {
                    destination = PupilRepresentationPlan.class;
                    iFunction = new PupilDataHandler();

                    file = new File(directory.getAbsolutePath() + "/" + FileNames.Today.STUDENT_REP_PLAN);

                    if(file.exists()) {
                        rawList = iFunction.readFile(file);
                        dataList = iFunction.prepareData(iFunction.findData(rawList));
                    } else {
                        setErrorSnackbar(drawerLayout, "Vertretungsplan kann <b>nicht</b> angezeigt werden.", "Die <b>notwendigen Daten</b> fehlen.");
                        break;
                    }
                }
                // Teacher's Representation Plan
                else {
                    destination = TeacherRepresentationPlan.class;
                    iFunction = new TeacherDataHandler();

                    file = new File(directory.getAbsolutePath() + "/" + FileNames.Today.TEACHER_REP_PLAN);

                    if(file.exists()) {
                        rawList = iFunction.readFile(file);
                        dataList = iFunction.prepareData(iFunction.findData(rawList));
                    } else {
                        setErrorSnackbar(drawerLayout, "Vertretungsplan kann <b>nicht</b> angezeigt werden.", "Die <b>notwendigen Daten</b> fehlen.");
                        break;
                    }
                }

                intentExtras.put(KeyIntent.FROM_NAVIGATION, true);
                intentExtras.put(KeyIntent.RAW_LIST, rawList);
                intentExtras.put(KeyIntent.DATA_LIST, dataList);

                NavigationHandler.navigate(activity, destination, intentExtras, direction);
                break;
            // =====================================================================================
            case R.id.nav_kollegium:
                iFunction = new StaffDataHandler();

                file = new File(directory.getAbsolutePath() + "/" + FileNames.STAFF);

                if(file.exists()) {
                    rawList = iFunction.readFile(file);
                    dataList = iFunction.prepareData(iFunction.findData(rawList));

                    intentExtras.put(KeyIntent.DATA_LIST, dataList);

                    NavigationHandler.navigate(activity, Staff.class, intentExtras, direction);

                } else {
                    setErrorSnackbar(drawerLayout, "Kollegium kann <b>nicht<b> angezeigt werden", "Die <b>notwendigen Daten</b> fehlen.");
                }
                break;
            // =====================================================================================
            case R.id.nav_termine:
                if(ConnectionHandler.isConnectionActive(activity)) {
                    NavigationHandler.navigate(activity, Appointments.class, null, direction);
                } else {
                    setErrorSnackbar(drawerLayout, "Termine können <b>nicht</b> angezeigt werden.", "Aktuell ist <b>keine Internetverbindung</b> vorhanden.");
                }
                break;
            // =====================================================================================
            case R.id.nav_klausurplan:
                if(ConnectionHandler.isConnectionActive(activity)) {
                    NavigationHandler.navigate(activity, ExaminationSchedule.class, null, direction);
                } else {
                    setErrorSnackbar(drawerLayout, "Klausurplan kann <b>nicht</b> angezeigt werden.", "Aktuell ist <b>keine Internetverbindung</b> vorhanden.");
                }
                break;
            // =====================================================================================
            case R.id.nav_webseite: case R.id.nav_moodle2: case R.id.nav_essen:
                String link = null;

                switch(id) {
                    case R.id.nav_webseite:
                        link = Links.Websites.HOMEPAGE;
                        break;
                    case R.id.nav_moodle2:
                        link = Links.Websites.MOODLE2;
                        break;
                    case R.id.nav_essen:
                        link = Links.Websites.FOOD_ORDER;
                        break;
                }

                if(ConnectionHandler.isConnectionActive(activity)) {
                    setNavigationURL(link);
                } else {
                    setErrorSnackbar(drawerLayout, "<b>Keine</b> Internetverbindung vorhanden.", null);
                }
                break;
        }
        return true;
    }

    private void setErrorSnackbar(DrawerLayout drawerLayout, String text, final String expandableErrorText) {
        drawerLayout.closeDrawer(Gravity.START);
        final Snackbar sb = SnackbarDesign.getSnackbar(activity.findViewById(R.id.homepage_content), activity, Homepage.TYPEFACE_HANDLER.getCurrentTypeface(), text, SnackbarDesign.NO_CONNECTION);

        if(expandableErrorText != null) {
            sb.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = sb.getView().findViewById(R.id.snackbar_text);
                    textView.append(Html.fromHtml("<br>" + expandableErrorText));
                }
            });
        }
        sb.show();
    }

    private void setNavigationURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

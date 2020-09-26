package hhg.informatikprojektkurs.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;

import android.support.v4.view.GravityCompat;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Typeface;
import android.view.View;

import hhg.informatikprojektkurs.constants.design.Fonts;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.design.SlideAdapter;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.listener.NavigationListener;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.download.FileDownloader;

public class Homepage extends AppCompatActivity {

    public static TypefaceHandler TYPEFACE_HANDLER = new TypefaceHandler();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_navigation_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_down));
        setSupportActionBar(toolbar);

        // =========================================================================================

        sharedPreferences = getApplicationContext().getSharedPreferences(KeySharedPreferences.NAME, Constants.ZERO);
        editor = sharedPreferences.edit();

        boolean isTeacherLogIn = sharedPreferences.getBoolean(KeySharedPreferences.LOGIN_TEACHER, false);
        boolean isNewLogin = getIntent().hasExtra(KeyIntent.NEW_LOGIN);
        boolean isAppStart = getIntent().hasExtra(KeyIntent.APP_START);

        // =========================================================================================

        // Get the NavigationView from the resources (called nav_view)
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupNavigationDrawer(navigationView, toolbar, isTeacherLogIn);

        setActiveTypeface();
        typeface = TYPEFACE_HANDLER.getCurrentTypeface();

        TextView title = findViewById(R.id.title);
        TextView developer = findViewById(R.id.developer);
        developer.setText(Html.fromHtml(getString(R.string.activity_developer)));

        View startseiteHeaderView = navigationView.getHeaderView(Constants.ZERO);
        TextView startseiteHeaderViewBanner = startseiteHeaderView.findViewById(R.id.shl_loginHeader);
        TextView startseiteHeaderViewInfoStatus = startseiteHeaderView.findViewById(R.id.shl_loginInfo);

        /*
        Frage ob, ob sich ein Lehrer angemeldet hat (LehrerLogIn == 1). Wenn ja, dann setze den Text auf "Lehrer" (im NavigationDrawer, die Anzeige)
        Ansonsten setze den Text auf Schüler.
         */

        if(isAppStart) {
            FileDownloader.downloadApplicationFiles(Homepage.this, isTeacherLogIn);
        }

        if(isTeacherLogIn) {
            startseiteHeaderViewInfoStatus.setText(R.string.startseite_lehrer_text);
        } else {
            startseiteHeaderViewInfoStatus.setText(R.string.startseite_schueler_text);
        }

        if(isNewLogin) {
            SnackbarDesign.getSnackbar(findViewById(R.id.homepage_content), Homepage.this, typeface, "Erfolgreich eingeloggt.", SnackbarDesign.LOGGED_IN).show();
            TYPEFACE_HANDLER.setCurrentTypeface(TypefaceHandler.getTypeface(Homepage.this, Fonts.SSP_EXTRA_LIGHT));
            editor.apply();
        }

        Homepage.TYPEFACE_HANDLER.setTextViewTypeface(typeface, title, developer, startseiteHeaderViewBanner, startseiteHeaderViewInfoStatus);
        Homepage.TYPEFACE_HANDLER.setTextViewColor(Color.WHITE, title, developer);

        setDrawerFont();
        createSlideView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finishActivity(Constants.ZERO);
            System.exit(Constants.ZERO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_menu, menu);
        TYPEFACE_HANDLER.getMenu(menu, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.startseite_logout) {
            editor.remove(KeySharedPreferences.LOGGED_IN);
            editor.remove(KeySharedPreferences.LOGIN_TEACHER);
            editor.remove(KeySharedPreferences.TYPEFACE);
            editor.remove(KeySharedPreferences.STUDENT_REP_PLAN);

            editor.putBoolean(KeySharedPreferences.LOGGED_OUT, true);
            editor.commit();

            NavigationHandler.navigate(Homepage.this, Login.class, null, SlideDirection.RIGHT);
            return true;
        } else if (id == R.id.startseite_informationen) {
            Intent intent = new Intent(this, Information.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.startseite_einstellungen) {
            Intent intent = new Intent(this, Settings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.startseite_refresh) {
            try {
                boolean isTeacherLogIn = sharedPreferences.getBoolean(KeySharedPreferences.LOGIN_TEACHER, false);

                SnackbarDesign.getSnackbar(findViewById(R.id.homepage_content), Homepage.this, typeface, "Daten werden heruntergeladen...", SnackbarDesign.CHOSEN_CLASS).show();
                FileDownloader.downloadApplicationFiles(Homepage.this, isTeacherLogIn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawer(final NavigationView navView, Toolbar toolbar, boolean isTeacherLogIn) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawer.setStatusBarBackgroundColor(getResources().getColor(android.R.color.transparent));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.homepage_content);

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                relativeLayout.setTranslationX(slideX);
                relativeLayout.setScaleX(1 - slideOffset);
                relativeLayout.setScaleY(1 - slideOffset);
            }
        };

        drawer.addDrawerListener(toggle);

        navView.setNavigationItemSelectedListener(new NavigationListener(Homepage.this, drawer));
        navView.setItemIconTintList(null);

        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_startseitenav);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        if(isTeacherLogIn) {
            navView.getMenu().findItem(R.id.nav_vertretungsplan_lehrer).setVisible(true);
            navView.getMenu().findItem(R.id.nav_vertretungsplan).setTitle(getString(R.string.nav_text_student_representation_plan));
        }
    }

    private void setActiveTypeface() {
        TYPEFACE_HANDLER.setCurrentTypeface(TypefaceHandler.getTypeface(Homepage.this, sharedPreferences.getInt(KeySharedPreferences.TYPEFACE, Constants.ZERO)));
    }

    private void createSlideView() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        SlideAdapter slideAdapter = new SlideAdapter(Homepage.this);
        viewPager.setAdapter(slideAdapter);
    }

    private void setDrawerFont() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        Homepage.TYPEFACE_HANDLER.getMenu(m, this);
    }
}
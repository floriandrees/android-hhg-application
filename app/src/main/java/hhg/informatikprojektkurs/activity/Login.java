package hhg.informatikprojektkurs.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.constants.design.Fonts;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.listener.LoginListener;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;

public class Login extends AppCompatActivity {

    private EditText inputField;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.login_activity);

        sharedPreferences = getSharedPreferences(KeySharedPreferences.NAME, Constants.ZERO);
        editor = sharedPreferences.edit();

        final TypefaceHandler typefaceHandler = new TypefaceHandler();

        inputField = findViewById(R.id.editText);

        TextView title = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);

        TextView developer = findViewById(R.id.developer);
        developer.setText(Html.fromHtml(getString(R.string.activity_developer)));

        final ImageView logo = findViewById(R.id.logo);
        final Typeface typeface = TypefaceHandler.getTypeface(Login.this, Fonts.SSP_EXTRA_LIGHT);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarDesign.getSnackbar(findViewById(R.id.login_content), Login.this, typeface, "Das Passwort zum Anmelden findest du in der Hausmeisterloge.", SnackbarDesign.PASSWORD_HINT).show();
            }
        });

        typefaceHandler.setTextViewTypeface(typeface, developer, title, subtitle, inputField);
        typefaceHandler.setTextViewColor(Color.WHITE, title, subtitle, developer, inputField);

        inputField.setHintTextColor(Color.WHITE);

        checkSharedPreferences(typeface);
        inputField.addTextChangedListener(new LoginListener(Login.this, sharedPreferences, inputField));
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void checkSharedPreferences(Typeface typeface) {
        System.out.println(sharedPreferences.contains(KeySharedPreferences.LOGGED_OUT));

        if(isLoggedOut()) {
            editor.remove(KeySharedPreferences.LOGGED_OUT);
            editor.commit();

            SnackbarDesign.getSnackbar(findViewById(R.id.login_content), Login.this, typeface, "<b>Du hast dich ausgeloggt.</b>", SnackbarDesign.LOGGED_OUT).show();
        } else if (isLoggedIn()) {
            HashMap<String, Object> intentExtras = new HashMap<>();
            intentExtras.put(KeyIntent.APP_START, true);

            NavigationHandler.navigate(Login.this, Homepage.class, intentExtras, SlideDirection.LEFT);
        }
    }

    private boolean isLoggedIn() {
        if(!sharedPreferences.contains(KeySharedPreferences.LOGGED_IN)) {
            return false;
        }

        if (sharedPreferences.getBoolean(KeySharedPreferences.LOGGED_IN, false)) {
            return true;
        }
        return false;
    }

    private boolean isLoggedOut() {
        if(!sharedPreferences.contains(KeySharedPreferences.LOGGED_OUT)) {
            return false;
        }

        if(sharedPreferences.getBoolean(KeySharedPreferences.LOGGED_OUT, true)) {
            return true;
        }
        return false;
    }
}

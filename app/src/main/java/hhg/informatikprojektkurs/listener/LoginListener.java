package hhg.informatikprojektkurs.listener;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;

public class LoginListener implements TextWatcher {

    private Activity activity;
    private EditText inputField;
    private SharedPreferences.Editor editor;

    public LoginListener(Activity activity, SharedPreferences sharedPreferences, EditText inputField) {
        this.activity = activity;
        this.inputField = inputField;
        this.editor = sharedPreferences.edit();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String userPassword = inputField.getText().toString();

        final String STUDENT_PASSWORD = activity.getApplicationContext().getString(R.string.student_password);
        final String PROF_PASSWORD = activity.getApplicationContext().getString(R.string.prof_password);

        if(userPassword.equals(STUDENT_PASSWORD) || userPassword.equals(PROF_PASSWORD)) {
            HashMap<String, Object> intentExtras = new HashMap<>();

            if (userPassword.equals(STUDENT_PASSWORD)) {
                editor.putBoolean(KeySharedPreferences.LOGIN_TEACHER, false);
            } else {
                editor.putBoolean(KeySharedPreferences.LOGIN_TEACHER, true);
            }
            intentExtras.put(KeyIntent.NEW_LOGIN, true);
            intentExtras.put(KeyIntent.APP_START, true);

            editor.putBoolean(KeySharedPreferences.LOGGED_IN, true);
            editor.commit();

            NavigationHandler.navigate(activity, Homepage.class, intentExtras, SlideDirection.LEFT);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Do nothing here, because there should not be done anything
    }
}

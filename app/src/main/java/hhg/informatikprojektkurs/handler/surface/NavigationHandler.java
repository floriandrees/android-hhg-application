package hhg.informatikprojektkurs.handler.surface;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.constants.design.SlideDirection;

public class NavigationHandler {
    public static void backToMenu(Activity activity) {
        activity.startActivity(createIntent(activity, Homepage.class));
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private static Intent createIntent(Activity activity, Class destination) {
        Intent intent = new Intent(activity, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static void navigateWithClassName(Activity activity, String className) {
        try {
            Class destination = Class.forName(className);
            NavigationHandler.navigate(activity, destination, null, SlideDirection.RIGHT);
        } catch(Exception e) {
            e.printStackTrace();
            NavigationHandler.navigate(activity, Homepage.class, null, SlideDirection.RIGHT);
        }
    }

    public static void navigate(Activity activity, Class destination, HashMap<String, Object> extras, int option) {

        Intent intent = createIntent(activity, destination);

        if(extras != null) {
            for(String key : extras.keySet()) {
                Object object = extras.get(key);

                if(object == null) {
                    continue;
                }

                if(object instanceof ArrayList) { // ArrayList<String>
                    ArrayList<String> list = (ArrayList<String>) object;
                    intent.putStringArrayListExtra(key, list);
                } else if(object instanceof Integer) { // Integer / int
                    intent.putExtra(key, ((Integer) object).intValue());
                } else if(object instanceof String) { // String
                    intent.putExtra(key, String.valueOf(object));
                } else if(object instanceof Boolean) { // Boolean / boolean
                    intent.putExtra(key, ((Boolean) object).booleanValue());
                } else {
                    Log.e("NavigationHandler", "Unable to read extra " + key + " from type " + object.getClass().getName());
                }
            }
        }

        activity.startActivity(intent);

        if(option == SlideDirection.LEFT) {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if(option == SlideDirection.RIGHT) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}

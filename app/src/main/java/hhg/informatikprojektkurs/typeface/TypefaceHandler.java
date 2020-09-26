package hhg.informatikprojektkurs.typeface;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.constants.design.Fonts;

/**
 * Eine Klasse, um alles um die Schriftarten zu verwalten
 * Es gibt Methoden zum Setzen und Holen der Schriftart und der Textfarbe
 * Des weiteren beinhaltet die Klasse Methoden bezüglich der Navigation-View und vom Overflow-Menü
 */
public class TypefaceHandler {
    private static Typeface typeface;

    public void setTextViewTypeface(Typeface tf, TextView... textViews) {
        for(int i = 0; i < textViews.length; i++) {
            textViews[i].setTypeface(tf);
        }
    }

    public void setTextViewColor(int color, TextView... textViews) {
        for(int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(color);
        }
    }

    /**
     * Getter-Methode, um sich die aktuelle Schriftart zurückgeben zu lassen
     * @return Die aktuelle Schriftart
     */
    public Typeface getCurrentTypeface() {
        return typeface;
    }

    /**
     * Setter-Methode zum Setzen der aktuellen Schriftart
     * @param pTypeface Die neue Schriftart als Fonts
     */
    public void setCurrentTypeface(Typeface pTypeface) {
        typeface = pTypeface;
    }

    // Hilfsmethode, die die Schriftarten für das Overflow-Menü und auf der Homepage für den Navigation-Drawer setzt

    /**
     * Hilfsmethode, die die Schriftarten für das Overflow-Menü und auf der Homepage für den Navigation-Drawer setzt
     * @param mi MenuItem, für das die Schriftart gesetzt werden soll
     * @param font Die Schriftart, die gesetzt werden soll
     * @param color Die Textfarbe, die jesetzt werden soll
     * @param context Um einen Context zu bekommen
     */
    public static void setTypefaceToMenu(MenuItem mi, Typeface font, @ColorRes int color, Context context) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new TypefaceNavigationHandler("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, color)), 0, mNewTitle.length(), 0);

        mi.setTitle(mNewTitle);
    }

    /**
     * Methode, um die Schriftart für das Overflow-Menü zu setzen
     * @param m Ist das gesamte Menü, was von einer Activity übergeben wird
     * @param context Um einen Context zu bekommen, in welchem Status sich die App gerade befindet
     */
    public void getMenu(Menu m, Context context) {
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    TypefaceHandler.setTypefaceToMenu(subMenuItem, Homepage.TYPEFACE_HANDLER.getCurrentTypeface(), android.R.color.white, context);
                }
            }
            TypefaceHandler.setTypefaceToMenu(mi, Homepage.TYPEFACE_HANDLER.getCurrentTypeface(), android.R.color.white, context);
        }
    }

    public static Typeface getTypeface(Activity activity, int typeface) {
        switch (typeface) {
            default:
            case Fonts.SSP_EXTRA_LIGHT:
                return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.typeface_ssp_extra_light));
            case Fonts.LATO_THIN:
                return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.typeface_lato_thin));
            case Fonts.ROBOTO_THIN:
                return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.typeface_roboto_thin));
            case Fonts.MARVEL_REGULAR:
                return Typeface.createFromAsset(activity.getAssets(), activity.getResources().getString(R.string.typeface_marvel_regular));
        }
    }

}

package hhg.informatikprojektkurs.listener.spinner;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;

public class StaffListener implements AdapterView.OnItemSelectedListener {

    private Activity activity;
    private List<String> dataList;

    public StaffListener(Activity activity, List<String> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linearLayout = activity.findViewById(R.id.linearLayout);
        TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;

        Typeface typeface = typefaceHandler.getCurrentTypeface();

        View template = activity.getLayoutInflater().inflate(R.layout.staff_design_entry, null);

        TextView header = template.findViewById(R.id.kollegiumHeader);
        TextView information = template.findViewById(R.id.kollegiumInfos);

        typefaceHandler.setTextViewTypeface(typeface, header, information);

        linearLayout.removeAllViews();
        if(position != 0) {
            SnackbarDesign.getSnackbar(view, activity, typeface, "Ausgewählt - <b>" + parent.getItemAtPosition(position) + "</b>", SnackbarDesign.CHOSEN_CLASS).show();

            String name = parent.getSelectedItem().toString();

            String[] entryData = name.split("  •  ");

            for(String current : dataList) {
                String[] lokalerListeLehrer = current.split("%");
                lokalerListeLehrer[1] = lokalerListeLehrer[1].replaceAll("\\s+", "");

                if(entryData[0].equals(lokalerListeLehrer[1])) {
                    header.setText(lokalerListeLehrer[0]);

                    information.setText(Html.fromHtml("<b>Kürzel: </b>" + lokalerListeLehrer[1] + "<br>" +
                            "<b>Sprechstunde: </b>" + lokalerListeLehrer[2] + "<br><br>" +
                            "<b>E-Mail-Adresse: </b>" + lokalerListeLehrer[3])
                    );

                    Linkify.addLinks(information, Linkify.EMAIL_ADDRESSES);
                }
            }
        } else {
            header.setText(activity.getString(R.string.staff_default_text_header));
            information.setText(Html.fromHtml(activity.getString(R.string.staff_default_text_information)));
        }
        linearLayout.addView(template);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

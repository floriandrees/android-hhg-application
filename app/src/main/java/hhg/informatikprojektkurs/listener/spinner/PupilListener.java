package hhg.informatikprojektkurs.listener.spinner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.constants.keys.KeySharedPreferences;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;

public class PupilListener implements AdapterView.OnItemSelectedListener {

    private Activity activity;
    private LinearLayout linearLayout;

    private SharedPreferences sharedPreferences;

    private TypefaceHandler typefaceHandler;

    private ArrayList<String> dataList;

    public PupilListener(Activity activity, SharedPreferences sharedPreferences, ArrayList<String> dataList) {
        this.activity = activity;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.sharedPreferences = sharedPreferences;

        this.typefaceHandler = Homepage.TYPEFACE_HANDLER;

        Collections.sort(dataList);
        this.dataList = dataList;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Typeface typeface = typefaceHandler.getCurrentTypeface();

        if(sharedPreferences.getInt(KeySharedPreferences.STUDENT_REP_PLAN, Constants.ZERO) != position) {
            SnackbarDesign.getSnackbar(view, activity, typeface, "Ausgewählt - <b>" + parent.getItemAtPosition(position) + "</b>", SnackbarDesign.CHOSEN_CLASS).show();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KeySharedPreferences.STUDENT_REP_PLAN, position);
        editor.apply();

        if(position != Constants.ZERO) {
            linearLayout.removeAllViews();

            String content = parent.getItemAtPosition(position).toString();
            String classId = content.split(" ")[0];

            boolean foundOne = false;

            for(String current : dataList) {
                if(dataList.size() == Constants.ZERO) {
                    break;
                }

                String[] entryData = current.split("; ");

                if(entryData[1].equals(entryData[2]) && entryData[3].equals(entryData[4])) {
                    continue;
                }

                if (entryData[0].startsWith(classId)) {
                    foundOne = true;

                    View viewEntry = activity.getLayoutInflater().inflate(R.layout.pupil_plan_design_entry, null);

                    TextView lesson = viewEntry.findViewById(R.id.lesson);
                    TextView subject = viewEntry.findViewById(R.id.subject);
                    TextView classroom = viewEntry.findViewById(R.id.classroom);
                    TextView type = viewEntry.findViewById(R.id.type);

                    lesson.setText(entryData[1]);
                    subject.setText(entryData[2]);
                    classroom.setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_classroom) + entryData[3]));
                    type.setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_type) + entryData[4]));

                    typefaceHandler.setTextViewTypeface(typeface, lesson, subject, classroom, type);
                    linearLayout.addView(viewEntry);
                }
            }
            if(!foundOne) {
                linearLayout.removeAllViews();

                View viewNoEntry = activity.getLayoutInflater().inflate(R.layout._error_message_design, null);
                TextView message = viewNoEntry.findViewById(R.id.message);
                message.setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_no_data)));
                message.setTypeface(typeface);

                linearLayout.addView(viewNoEntry);
            }
        } else {
            linearLayout.removeAllViews();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private Activity getActivity() {
        return this.activity;
    }
}
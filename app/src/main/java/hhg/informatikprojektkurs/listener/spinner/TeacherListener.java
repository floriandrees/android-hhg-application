package hhg.informatikprojektkurs.listener.spinner;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.design.SnackbarDesign;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.constants.Constants;

public class TeacherListener implements AdapterView.OnItemSelectedListener {

    private Activity activity;
    private LinearLayout linearLayout;

    private List<String> dataList;

    private TypefaceHandler typefaceHandler;

    public TeacherListener(Activity activity, List<String> dataList) {
        this.activity = activity;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.dataList = dataList;
        this.typefaceHandler = Homepage.TYPEFACE_HANDLER;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Typeface typeface = typefaceHandler.getCurrentTypeface();

        if(position != Constants.ZERO) {
            getLinearLayout().removeAllViews();

            SnackbarDesign.getSnackbar(view, getActivity(), typeface, "Ausgewählt - <b>" + parent.getItemAtPosition(position).toString() + "</b>", SnackbarDesign.CHOSEN_CLASS).show();

            for(String current : dataList) {
                String[] entryData = current.split("; ");

                if(entryData.length <= 2) {
                    continue;
                }

                if(entryData[0].split(" ")[0].contains(parent.getItemAtPosition(position).toString().split(" ")[0])) {
                    View viewEntry = activity.getLayoutInflater().inflate(R.layout.teacher_plan_design_entry, null);

                    TextView lesson  = viewEntry.findViewById(R.id.lesson);
                    TextView type = viewEntry.findViewById(R.id.type);
                    TextView subject = viewEntry.findViewById(R.id.subject);
                    TextView classroom  = viewEntry.findViewById(R.id.classroom);
                    TextView schoolClass = viewEntry.findViewById(R.id.schoolClass);
                    TextView absence = viewEntry.findViewById(R.id.absence);

                    lesson.setText(entryData[1]);
                    subject.setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_subject) + entryData[2]));
                    schoolClass.setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_classes) + entryData[3]));
                    classroom .setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_classroom) + entryData[4]));
                    absence.setText(Html.fromHtml(getActivity().getString(R.string.rep_plan_absence) + entryData[5]));
                    type.setText(entryData[6]);

                    typefaceHandler.setTextViewTypeface(typeface, subject, classroom, absence, schoolClass, type, lesson);

                    getLinearLayout().addView(viewEntry);
                }
            }
        } else {
            getLinearLayout().removeAllViews();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private Activity getActivity() {
        return this.activity;
    }

    private LinearLayout getLinearLayout() {
        return this.linearLayout;
    }
}

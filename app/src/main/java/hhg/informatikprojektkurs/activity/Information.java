package hhg.informatikprojektkurs.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.handler.files.FileHandler;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;

public class Information extends AppCompatActivity {

    private TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;

    /**
     * Standard-Methode von Android. Sozusagen die "public static void main(...) Methode" von Java-Programmen
     * Diese Methode wird beim Start der Aktivität ausgeführt
     * @param savedInstanceState ---
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });

        TextView title = findViewById(R.id.title);
        TextView developer = findViewById(R.id.developer);
        developer.setText(Html.fromHtml(getString(R.string.activity_developer)));

        typefaceHandler.setTextViewTypeface(typefaceHandler.getCurrentTypeface(), title, developer);

        createParagraphs();
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    public void navigateBack() {
        String destination = Homepage.class.getName();

        if(getIntent().hasExtra(KeyIntent.CLASS_TO_NAVIGATE)) {
            destination = getIntent().getStringExtra(KeyIntent.CLASS_TO_NAVIGATE);
        }
        NavigationHandler.navigateWithClassName(Information.this, destination);
        this.finish();
    }

    private void createParagraphs() {
        List<List<String>> lists = FileHandler.getContentFromAssetsFile(Information.this, "information.txt");
        List<String> headers = lists.get(0);
        List<String> content = lists.get(1);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        Typeface typeface = typefaceHandler.getCurrentTypeface();

        for(int i = 0; i < headers.size(); i++) {
            View infoLayout = getLayoutInflater().inflate(R.layout.information_design_entry, null);

            TextView header = infoLayout.findViewById(R.id.header);
            TextView message = infoLayout.findViewById(R.id.message);

            header.setTypeface(typeface);
            message.setTypeface(typeface);

            if(i == 0) {
                header.setTextColor(getResources().getColor(R.color.bootstrap_ALIZARIN));
            }

            header.setText(Html.fromHtml(headers.get(i)));
            message.setText(Html.fromHtml(content.get(i)));

            linearLayout.addView(infoLayout);
        }
    }
}

package hhg.informatikprojektkurs.activity.references;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.HashMap;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.activity.Information;
import hhg.informatikprojektkurs.constants.background.Links;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;
import hhg.informatikprojektkurs.handler.files.PdfFileHandler;

public class ExaminationSchedule extends AppCompatActivity {

    private TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examination_schedule_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_down));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.backToMenu(ExaminationSchedule.this);
            }
        });

        TextView title = findViewById(R.id.title);

        typefaceHandler.setTextViewTypeface(typefaceHandler.getCurrentTypeface(), title);

        PDFView pdfView = findViewById(R.id.pdfView);

        PdfFileHandler pdfFileHandler = new PdfFileHandler();
        pdfFileHandler.displayPdf(ExaminationSchedule.this, pdfView, Links.PDFFiles.EXAMINATION_SCHEDULE);
    }

    @Override
    public void onBackPressed() {
        NavigationHandler.backToMenu(ExaminationSchedule.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        menu.findItem(R.id.features_original).setVisible(false);

        typefaceHandler.getMenu(menu, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        HashMap<String, Object> intentExtras = new HashMap<>();

        if (id == R.id.features_informationen) {
            intentExtras.put(KeyIntent.CLASS_TO_NAVIGATE, this.getClass().getName());
            NavigationHandler.navigate(ExaminationSchedule.this, Information.class, intentExtras, SlideDirection.LEFT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
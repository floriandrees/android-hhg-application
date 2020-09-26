package hhg.informatikprojektkurs.activity.features;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.activity.Information;
import hhg.informatikprojektkurs.constants.background.Links;
import hhg.informatikprojektkurs.constants.background.FileNames;
import hhg.informatikprojektkurs.constants.keys.KeyIntent;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.handler.connection.ConnectionHandler;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;
import hhg.informatikprojektkurs.handler.surface.SpinnerHandler;
import hhg.informatikprojektkurs.handler.functions.StaffDataHandler;
import hhg.informatikprojektkurs.interfaces.IFunction;
import hhg.informatikprojektkurs.interfaces.IWebReader;
import hhg.informatikprojektkurs.listener.spinner.StaffListener;
import hhg.informatikprojektkurs.typeface.TypefaceHandler;

public class Staff extends AppCompatActivity {

    private List<String> dataList;
    private Spinner spinner;

    private Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.staff_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_down));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.backToMenu(Staff.this);
            }
        });

        typeface = Homepage.TYPEFACE_HANDLER.getCurrentTypeface();

        TextView title = findViewById(R.id.title);
        TextView header = findViewById(R.id.header);

        TypefaceHandler typefaceHandler = Homepage.TYPEFACE_HANDLER;
        typefaceHandler.setTextViewTypeface(typeface, title, header);

        checkIntent();

        spinner = findViewById(R.id.spinner);
        setupSpinner(spinner);
    }

    @Override
    public void onBackPressed() {
        NavigationHandler.backToMenu(Staff.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        HashMap<String, Object> intentExtras = new HashMap<>();

        if (id == R.id.features_informationen) {
            intentExtras.put(KeyIntent.CLASS_TO_NAVIGATE, this.getClass().getName());
            NavigationHandler.navigate(Staff.this, Information.class, intentExtras, SlideDirection.LEFT);
            return true;
        } else if(id == R.id.features_original) {
            if(ConnectionHandler.isConnectionActive(Staff.this)) {
                WebView webView = IWebReader.createWebsiteWebView(this, Links.STAFF);
                setContentView(webView);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        Homepage.TYPEFACE_HANDLER.getMenu(menu, this);
        return true;
    }

    /**
     * Checks the transferred parameters and acts accordingly. <br>
     * If the activity is called from the start page, the completed list is taken from the intent object
     */
    private void checkIntent() {
        boolean isFromNavigation = getIntent().hasExtra(KeyIntent.FROM_NAVIGATION);

        if(isFromNavigation) {
            dataList = getIntent().getStringArrayListExtra(KeyIntent.DATA_LIST);
        } else {
            File file = new File(getCacheDir().getPath() + "/files/" + FileNames.STAFF);
            IFunction iFunction = new StaffDataHandler();
            dataList = iFunction.prepareData(iFunction.findData(iFunction.readFile(file)));
        }
    }

    /**
     * Prepare the spinner for the selection.
     * @param spinner Spinner object, for which the following functions are called
     */
    private void setupSpinner(Spinner spinner) {
        SpinnerHandler.setupSpinner(spinner, SpinnerHandler.createSpinnerAdapter(Staff.this, getStaffsFromFile(), typeface), true);
        spinner.setOnItemSelectedListener(new StaffListener(Staff.this, dataList));

    }

    private List<String> getStaffsFromFile() {
        List<String> staffs = new ArrayList<>();
        staffs.add(getString(R.string.staff_default_text_header));

        for(String line : dataList) {
            String[] teacher = line.split("%");

            teacher[1] = teacher[1].replaceAll("\\s+", "");

            if(teacher[1].isEmpty() || teacher[0].isEmpty()) {
                continue;
            }
            staffs.add(teacher[1] + "  •  " + teacher[0]);
        }

        return staffs;
    }
}
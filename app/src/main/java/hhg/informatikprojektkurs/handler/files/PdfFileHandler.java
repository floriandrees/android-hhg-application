package hhg.informatikprojektkurs.handler.files;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Homepage;

public class PdfFileHandler {

    private PDFView pdfView;
    private Activity activity;

    public void displayPdf(Activity activity, PDFView pdfView, String url) {
        this.pdfView = pdfView;
        this.activity = activity;

        new RetrievePDFStream().execute(url);
    }

    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection.getResponseCode() == 200) {
                    return new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            if(inputStream != null) {
                pdfView.fromStream(inputStream).load();
            } else {
                pdfView.setVisibility(View.INVISIBLE);

                View template = activity.getLayoutInflater().inflate(R.layout._error_message_design, null);

                TextView message = template.findViewById(R.id.message);
                message.setText(Html.fromHtml("Das Dokument konnte <b><u>nicht</u></b> geladen werden."));
                message.setTypeface(Homepage.TYPEFACE_HANDLER.getCurrentTypeface());

                LinearLayout linearLayout = activity.findViewById(R.id.linearLayout);
                linearLayout.addView(template);
            }
        }
    }
}

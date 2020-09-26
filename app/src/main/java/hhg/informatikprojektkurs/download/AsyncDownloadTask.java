package hhg.informatikprojektkurs.download;

import android.os.AsyncTask;

public class AsyncDownloadTask extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        FileDownloader.download(params[0], params[1], params[2]);
        return null;
    }
}



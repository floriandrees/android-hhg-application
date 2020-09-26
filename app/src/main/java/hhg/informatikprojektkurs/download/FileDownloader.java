package hhg.informatikprojektkurs.download;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import hhg.informatikprojektkurs.constants.background.Charsets;
import hhg.informatikprojektkurs.constants.background.Links;
import hhg.informatikprojektkurs.constants.background.FileNames;
import hhg.informatikprojektkurs.handler.connection.ConnectionHandler;

public class FileDownloader {
    public static void downloadApplicationFiles(Activity activity, boolean isTeacherLogin) {
        String path = activity.getCacheDir().getPath() + "/files/";

        File directory = new File(path);
        directory.mkdirs();

        if(ConnectionHandler.isConnectionActive(activity)) {
            FileDownloader.downloadFile(activity, Links.Today.STUDENT_REP_PLAN, path + FileNames.Today.STUDENT_REP_PLAN, Charsets.ISO_8859_1);
            FileDownloader.downloadFile(activity, Links.STAFF, path + FileNames.STAFF, Charsets.UTF_8);

            if(isTeacherLogin) {
                FileDownloader.downloadFile(activity, Links.Today.TEACHER_REP_PLAN, path + FileNames.Today.TEACHER_REP_PLAN, Charsets.ISO_8859_1);
            }
        }
    }

    public static void download(String url, String absolutePath, String charset) {
        Log.d("FileDownloader", "Downloading from " +url + " into file " + absolutePath);

        try(InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName(charset)))) {

            File file = new File(absolutePath);

            if(!file.exists()) {
                file.createNewFile();
                Log.e("FileDownloader", "Create new file " + absolutePath);
            }

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                String line;

                while((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(Activity activity, String url, String fileName, String charset) {
        if(ConnectionHandler.isConnectionActive(activity)) {
            AsyncDownloadTask downloadTask = new AsyncDownloadTask();
            downloadTask.execute(url, fileName, charset);
        }
    }
}

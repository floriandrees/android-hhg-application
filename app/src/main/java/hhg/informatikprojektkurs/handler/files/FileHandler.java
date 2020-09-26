package hhg.informatikprojektkurs.handler.files;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hhg.informatikprojektkurs.activity.Information;
import hhg.informatikprojektkurs.utils.VersionUtils;

public class FileHandler {
    private Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    public List<String> getLinesFromAssetsFile(String assetsFileName, boolean emptyLinesAllowed) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(assetsFileName)))) {
            String line;

            List<String> lines = new ArrayList<>();

            while((line = br.readLine()) != null) {
                if(!emptyLinesAllowed && line.isEmpty()) {
                    continue;
                }

                lines.add(line);
            }
            return lines;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<List<String>> getContentFromAssetsFile(Activity activity, String fileName) {
        List<String> headerList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(activity.getAssets().open(fileName)))) {
            String content = "";

            String line;
            while((line = br.readLine()) != null) {

                if(activity.getClass() == Information.class) {
                    if(line.contains("{REPLACE_WITH_VERSION}")) {
                        line = line.replace("{REPLACE_WITH_VERSION}", VersionUtils.getVersionCode(activity));
                    }
                }

                if (line.startsWith("HEADER§==§")) {

                    if(!content.isEmpty()) {
                        contentList.add(content);
                    }

                    content = "";
                    headerList.add(line.split("§==§")[1]);
                }

                if(line.startsWith("CONTENT§==§")) {
                    content = line.split("§==§")[1];
                } else {
                    content += line;
                }
            }

            if(!content.isEmpty()) {
                contentList.add(content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<List<String>> returns = new ArrayList<>();
        returns.add(headerList);
        returns.add(contentList);
        return returns;
    }
}

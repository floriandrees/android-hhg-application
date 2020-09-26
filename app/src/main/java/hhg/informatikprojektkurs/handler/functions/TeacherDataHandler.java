package hhg.informatikprojektkurs.handler.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import hhg.informatikprojektkurs.constants.background.Regex;
import hhg.informatikprojektkurs.interfaces.IFunction;

public class TeacherDataHandler implements IFunction {
    @Override
    public ArrayList<String> readFile(File file) {
        ArrayList<String> content = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while((line = br.readLine()) != null) {
                Charset.forName("iso-8859-1").encode(line);
                content.add(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    public ArrayList<String> findData(List<String> htmlListe) {
        String keywordLine = "";
        ArrayList<String> tempListe = new ArrayList<>();
        for(String current : htmlListe) {
            if(current.contains("list inline_header")) {
                keywordLine = current;
            } else if(current.contains("list even") || current.contains("list odd")) {
                tempListe.add(keywordLine + "" + current);
            } else if(current.contains("<td class=\"list\" align=\"center\">")) {
                tempListe.add(keywordLine + "" + current);
            }
        }
        return tempListe;
    }

    @Override
    public ArrayList<String> prepareData(List<String> dataList) {
        ArrayList<String> entryList = new ArrayList<>();

        for(String line : dataList) {

            line = line.replaceAll(Regex.HTML_MATCHER_EXTENDS_TD, "");
            line = line.replace("</td>", "; ");
            line = line.replace("&nbsp;", "---");

            entryList.add(line);
        }
        return entryList;
    }
}

package hhg.informatikprojektkurs.handler.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import hhg.informatikprojektkurs.constants.background.Regex;
import hhg.informatikprojektkurs.interfaces.IFunction;

public class PupilDataHandler implements IFunction {

    @Override
    public ArrayList<String> readFile(File file) {
        String line;
        BufferedReader bf = null;

        ArrayList<String> rawList = new ArrayList<>();

        try { bf = new BufferedReader(new FileReader(file)); }
        catch(java.io.FileNotFoundException e) { e.printStackTrace(); }
        try {
            while((line = bf.readLine()) != null) { rawList.add(line); }
            bf.close();
        }
        catch (java.io.IOException | NullPointerException e) { e.printStackTrace(); }
        return rawList;
    }

    @Override
    public ArrayList<String> findData(List<String> rawList) {
        String keywordLine = "";
        ArrayList<String> dataList = new ArrayList<>();

        for(String current : rawList) {
            if(current.contains("list inline_header")) {
                keywordLine = current;
            } else if(current.contains("style=\"background-color: #")) {
                if(!current.contains("GTA5") && !current.contains("-----")) {
                    dataList.add(keywordLine + "" + current);
                }
            }
            else if(current.contains("<td class=\"list\" align=\"center\">")) {
                dataList.add(keywordLine + "" + current); }
        }
        return dataList;
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

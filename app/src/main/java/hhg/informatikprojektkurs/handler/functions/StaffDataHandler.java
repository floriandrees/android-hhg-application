package hhg.informatikprojektkurs.handler.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import hhg.informatikprojektkurs.interfaces.IFunction;

public class StaffDataHandler implements IFunction {
    @Override
    public ArrayList<String> readFile(File file) {
        String line;
        BufferedReader bf = null;
        ArrayList<String> tempListe = new ArrayList<>();

        try {
            if(file.exists() && file.isFile()) { bf = new BufferedReader(new FileReader(file)); }
        } catch (java.io.FileNotFoundException e) { e.printStackTrace(); }

        try {
            if (bf != null) {
                boolean bodyReached = false;
                while ((line = bf.readLine()) != null) {
                    if (line.length() > 0) {
                        if (line.contains("<tbody>")) {
                            bodyReached = true;
                            continue;
                        }
                        if(bodyReached) {
                            tempListe.add(line);
                        }
                    }
                }
            }

            if(bf != null) {
                bf.close();
            }
        } catch (java.io.IOException e) { e.printStackTrace(); }
        return tempListe;
    }

    @Override
    public ArrayList<String> findData(List<String> htmlListe) {
        boolean trReached = false;
        String kompakteLine = "";
        ArrayList<String> tempListe = new ArrayList<>();
        for (String line : htmlListe) {
            if (line.contains("<tr>")) { trReached = true; }

            if (trReached && !line.contains("</tr>")) {
                if (!line.contains("document.write")) {
                    if (line.contains("var ")) { if (line.contains("addy")) { kompakteLine = kompakteLine + "" + line; } }
                    else {
                        if (kompakteLine.equals("")) { kompakteLine = line; }
                        else {
                            if (line.contains("<td")) { kompakteLine = kompakteLine + "<<**§§§§**>>" + line; }
                            else { kompakteLine = kompakteLine + line; }
                        }
                    }
                }
            }

            if (line.contains("</tr>")) {
                tempListe.add(kompakteLine);
                trReached = false;
                kompakteLine = "";
            }
        }
        return tempListe;
    }

    @Override
    public ArrayList<String> prepareData(List<String> datenListe) {
        ArrayList<String> tempListe = new ArrayList<>();

        for(int d = 0; d < datenListe.size(); d++) {
            String line = datenListe.get(d);
            if (line.contains("<strong>")) {
                continue;
            }

            line = line.replace("<td style=\"border-color: #000000; border-style: solid; border-width: 1px;\">", "");
            line = line.replace("<td style=\"border-color: #000000; border-style: solid; border-width: 1px; text-align: left;\">", "");
            line = line.replace("<td style=\"border-color: #000000; border-style: solid; border-width: 1px; text-align: center;\">", "");
            line = line.replace("<td style=\"border: 1px solid #000000; text-align: left;\">", "");
            line = line.replace("</td>", "");

            line = line.replace("<span style=\"font-size: small; font-family: verdana,geneva;\">", "");
            line = line.replace("<span style=\"font-family: verdana,geneva; font-size: small;\">", "");
            line = line.replace("<span style=\"font-family: verdana, geneva; font-size: small;\">", "");
            line = line.replace("<span style=\"font-family: verdana,geneva;\">", "");
            line = line.replace("<span style=\"font-size: small;\"> ", "");
            line = line.replace("<span style=\"font-size: small;\">", "");
            line = line.replace("<span style=\"font-size: x-small;\">", "");

            line = line.replace("<script type='text/javascript'>", "");
            line = line.replace("<!--", "");
            line = line.replace("//-->", "");
            line = line.replace("</script>Diese E-Mail-Adresse ist vor Spambots geschützt! Zur Anzeige muss JavaScript eingeschaltet sein!", "");
            line = line.replace("<a href=\"mailto:", "");
            line = line.replace("var path = 'hr' + 'ef' + '=';", "");
            line = line.replace("</script>", "");

            line = line.replace("&#97;", "a");
            line = line.replace("&#101;", "e");
            line = line.replace("&#105;", "i");
            line = line.replace("&#111;", "o");
            line = line.replace("&#117;", "u");

            line = line.replace("'&#46;'", ".");
            line = line.replace("'&#64;'", "@");

            line = line.replace(" + ", "");
            line = line.replace("'", "");
            line = line.replace("<br />", "");
            line = line.replace(";", "");
            line = line.replace("</span>", "");

            line = line.replace("<tr><<**§§§§**>>", "");
            line = line.replace("<<**§§§§**>>", "%");

            line = line.replace("% ", "%");
            line = line.replace(" %", "%");
            line = line.replace("  ", "");

            line = line.replace("</p>", "");
            line = line.replace("</a>", "");

            line = line.replace("var addy", "");
            line = line.replace("addy", "");

            line = line.replaceAll("\\\\n", "");

            if (!line.contains(" = ")) {
                tempListe.add(line);
                continue;
            }

            String[] data = line.split(" = ");

            String temp = data[0];

            while (!temp.endsWith("%")) {
                temp = temp.substring(0, temp.length() - 1);
            }

            String tempEmail = data[data.length - 1];
            int length = tempEmail.length();
            for (int i = 0; i < length; i++) {
                char c = tempEmail.charAt(i);
                String temp333 = c + "";

                if (Character.isDigit(c)) {
                    tempEmail = tempEmail.replace(temp333, "$");
                }
            }

            tempEmail = tempEmail.replace("$", "");

            if (tempEmail.contains("\"")) {
                do {
                    if (tempEmail.endsWith(" \">")) {
                        tempEmail = tempEmail.substring(0, tempEmail.length() - 3);
                    } else {
                        tempEmail = tempEmail.substring(0, tempEmail.length() - 1);
                    }
                } while (!tempEmail.endsWith(" \">"));
            }

            tempEmail = tempEmail.replace("\">", "");

            String temp444 = "";
            String finished = "";
            if (data.length > 1) {
                temp444 = data[1];

                while (!temp444.endsWith("@")) {
                    temp444 = temp444.substring(0, temp444.length() - 1);
                }

                finished = temp + temp444 + tempEmail;
            } else {
                finished = temp + tempEmail;
            }

            int index = finished.indexOf("@");
            int lastIndex = finished.lastIndexOf("@");
            if (index != lastIndex) {
                String[] data3 = finished.split("@");

                finished = data3[0] + "@" + data3[2];
            }
            tempListe.add(finished);
        }
        return tempListe;
    }
}

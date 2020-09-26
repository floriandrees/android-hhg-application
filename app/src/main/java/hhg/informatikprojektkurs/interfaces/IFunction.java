package hhg.informatikprojektkurs.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface IFunction {
    ArrayList<String> readFile(File file);
    ArrayList<String> findData(List<String> rawList);
    ArrayList<String> prepareData(List<String> dataList);
}

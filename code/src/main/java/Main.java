import algorithms.*;
import constructives.*;
import LocalSearch.*;
import structure.*;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
    public static void main(String[] args){

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String date = String.format("%04d-%02d-%02d T%02d-%02d", year, month, day, hour, minute);

        BCInstanceFactory factory = new BCInstanceFactory();

        String dir = ((args.length == 0) ? "instances" : (args[1] + "/"));
        String outDir = "experiments/" + date;
        File outDirCreator = new File(outDir);
        outDirCreator.mkdirs();
        String[] extensions = new String[]{".txt"};

        Algorithm<BCInstance>[] execution = new Algorithm[]{
                new AlgConstructiveGVNS(new ConstructiveAllRowsBothCoincidences(),0.15),
        };

        for (int i = 0; i < execution.length; i++) {
            String outputFile = outDir + "/" + execution[i].toString() + "_" + i + ".xlsx";
            Experiment<BCInstance, BCInstanceFactory> experiment = new Experiment<>(execution[i], factory);
            experiment.launch(dir, outputFile, extensions);
        }
    }
}
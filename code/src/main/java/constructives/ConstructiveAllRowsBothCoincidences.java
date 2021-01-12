package constructives;

import grafo.optilib.metaheuristics.Constructive;

import structure.BCInstance;
import structure.BCSolution;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ConstructiveAllRowsBothCoincidences implements Constructive<BCInstance, BCSolution> {
    
    public BCSolution constructSolution(BCInstance instance) {
        BCSolution sol = new BCSolution(instance);
        BCSolution bestSol = new BCSolution(instance);
        BCSolution secondbestSol = new BCSolution(instance);
        final long startTime = sol.getInstance().getStartTime();
        int maxCoincidences = 0;
        int bestMark = 0x3f3f3f3f;
        for(int staticRow = 0; staticRow<sol.getRows();staticRow++) {
            ArrayList<Integer> rowsAvailable = new ArrayList<>();
            int totalCoincidences = 0;
            for (int i = 0; i < sol.getRows(); i++) {
                sol.setRowColocationIndex(0,staticRow);
                if(i==staticRow) continue;
                rowsAvailable.add(i);
            }
            int nextRow = 1;
            while (rowsAvailable.size() != 1) {
                int maxNumberOfCoincidences = 0;
                int selectedRow = -1;
                int lastRow = sol.getRowColocation()[nextRow - 1];
                for (int i = 0; i < rowsAvailable.size(); i++) {
                    int numberOfCoincidences = 0;
                    for (int j = 0; j < sol.getColumns(); j++) {
                        int curColumnInRowSelected = sol.getInstance().getBandCollocationMatrix()[lastRow][j];
                        int curColumnInCandidateRow = sol.getInstance().getBandCollocationMatrix()[rowsAvailable.get(i)][j];
                        if (curColumnInRowSelected == curColumnInCandidateRow) {
                            numberOfCoincidences++;
                        }
                    }
                    if (maxNumberOfCoincidences <= numberOfCoincidences) {
                        maxNumberOfCoincidences = numberOfCoincidences;
                        selectedRow = i;
                    }
                }
                totalCoincidences+=maxNumberOfCoincidences;
                sol.setRowColocationIndex(nextRow, rowsAvailable.get(selectedRow));
                rowsAvailable.remove(selectedRow);
                nextRow++;
            }
            sol.setRowColocationIndex(nextRow, rowsAvailable.get(0)); //last row
            sol.reevaluateMark();

            if(bestMark>sol.getMark()){
                secondbestSol.copy(bestSol);
                bestSol.copy(sol);
                bestMark = sol.getMark();
            }
        }
        return bestSol;
    }
}

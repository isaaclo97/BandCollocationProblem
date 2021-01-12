package LocalSearch;

import grafo.optilib.metaheuristics.Improvement;
import structure.BCSolution;

import java.util.ArrayList;

public class LocalSearch2Opt implements Improvement<BCSolution> {

    public void improve(BCSolution solution) {
        BCSolution newSolution = new BCSolution(solution);
        boolean improveResult = true;
        ArrayList<Integer> rowOrder  = new ArrayList<>();
        for(int i = 0; i<solution.getRows();i++){
            rowOrder.add(i);
        }
        while(improveResult){
            improveResult = false;
            for(int i=1; i<newSolution.getRows()-1 && !improveResult;i++){
                for(int j=rowOrder.get(i)+1; j<newSolution.getRows() && !improveResult;j++){
                    int iAux = rowOrder.get(i);
                    int jAux = j;
                    OptMovement(newSolution, rowOrder, iAux, jAux);
                    newSolution.reevaluateMark();
                    if(solution.getMark()>newSolution.getMark()){
                        solution.copy(newSolution);
                        improveResult = true;
                    }
                    OptMovement(newSolution, rowOrder, iAux, jAux);
                }
            }
        }
    }

    private void OptMovement(BCSolution newSolution, ArrayList<Integer> rowOrder, int iAux, int jAux) {
        while(iAux<jAux) {
            int swap = newSolution.getRowColocation()[rowOrder.get(iAux)];
            newSolution.setRowColocationIndex(rowOrder.get(iAux), newSolution.getRowColocation()[rowOrder.get(jAux)]);
            newSolution.setRowColocationIndex(rowOrder.get(jAux), swap);
            jAux--; iAux++;
        }
    }
}

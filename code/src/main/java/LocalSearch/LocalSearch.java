package LocalSearch;

import grafo.optilib.metaheuristics.Improvement;
import structure.BCSolution;

import java.util.ArrayList;


//SWAPS
public class LocalSearch implements Improvement<BCSolution> {

    public void improve(BCSolution solution) {
        BCSolution newSolution = new BCSolution(solution);
        boolean improveResult = true;
        ArrayList<Integer> rowOrder  = new ArrayList<>();
        for(int i = 0; i<solution.getRows();i++){
            rowOrder.add(i);
        }

        while(improveResult){
            improveResult = false;
            for(int i=0; i<newSolution.getRows() && !improveResult;i++){
                for(int j=rowOrder.get(i)+1; j<newSolution.getRows() && !improveResult;j++){
                    int realI = rowOrder.get(i);
                    int realJ = j;
                    swapMovement(newSolution, realI, realJ);
                    newSolution.reevaluateMark();
                    if(solution.getMark()>newSolution.getMark()){
                        solution.copy(newSolution);
                        improveResult = true;
                    }
                    swapMovement(newSolution, realI, realJ);
                }
            }
        }
    }

    private void swapMovement(BCSolution newSolution, int realI, int realJ) {
        int swap = newSolution.getRowColocation()[realI];
        newSolution.setRowColocationIndex(realI,newSolution.getRowColocation()[realJ]);
        newSolution.setRowColocationIndex(realJ,swap);
    }
}

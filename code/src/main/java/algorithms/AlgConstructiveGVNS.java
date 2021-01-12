package algorithms;

import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.metaheuristics.Improvement;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import grafo.optilib.tools.RandomManager;
import LocalSearch.LocalSearch;
import LocalSearch.LocalSearch2Opt;
import structure.BCInstance;
import structure.BCSolution;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class AlgConstructiveGVNS implements Algorithm<BCInstance> {

	final Constructive<BCInstance, BCSolution> constructive;

	// The output directory path, used for testing purposes only
	private Double kMax;
	private ArrayList<Improvement> improvement,improvement2;
	private BCSolution bcSolution;

	public AlgConstructiveGVNS(Constructive<BCInstance, BCSolution> constructive, Double kMax){
		this.constructive = constructive;
		this.kMax = kMax;

		this.improvement = new ArrayList<>();
		this.improvement.add(new LocalSearch2Opt());
		this.improvement.add(new LocalSearch());

		this.improvement2 = new ArrayList<>();
		this.improvement2.add(new LocalSearch());
		this.improvement2.add(new LocalSearch2Opt());

	}

	@Override
	public Result execute(BCInstance bcInstance){

		long startTime = bcInstance.getStartTime();
		long firstStart = startTime;
		Result r = new Result(bcInstance.getName());

		BCSolution bestSolution = null;

		for(int i=0; i<50;i++) {
			bcSolution = constructive.constructSolution(bcInstance);
			BCSolution bcVNS = new BCSolution(bcSolution);
			BCSolution bcVNSAux = new BCSolution(bcSolution);
			BCSolution bcVNSAuxRes = new BCSolution(bcSolution);

			double realKMax = this.improvement.size();
			double realKMaxIters = (110-bcSolution.getRows())*kMax;
			VNDAlgorithm(startTime, realKMax, bcVNS, improvement, "Curbest ", bcSolution);
			VNDAlgorithm(startTime, realKMax, bcVNSAux, improvement2, "CurbestAux ", bcVNSAuxRes);
			if(bcVNSAuxRes.getMark()<bcSolution.getMark())
				bcSolution.copy(bcVNSAuxRes);

			bcVNS.copy(bcSolution);
			bcVNSAux.copy(bcSolution);
			bcVNSAuxRes.copy(bcSolution);
			BCSolution bcVNSAuxRes2 = new BCSolution(bcVNSAuxRes);

			int k = 1;
			while (k <= realKMaxIters) {
				shakeRandomSwap(bcVNS, k);
				bcVNS.reevaluateMark();
				bcVNSAux.copy(bcVNS);
				VNDAlgorithm(startTime, realKMax, bcVNS, improvement, "Curbest ", bcVNSAuxRes);
				VNDAlgorithm(startTime, realKMax, bcVNSAux, improvement2, "CurbestAux ", bcVNSAuxRes2);
				if (bcVNSAuxRes2.getMark() < bcVNSAuxRes.getMark())
					bcVNSAuxRes.copy(bcVNSAuxRes2);

				k = neighborhoodChange(bcVNSAuxRes, k);
				bcVNS.copy(bcVNSAuxRes);
				if (bcVNS.checkTime(startTime)) break;
			}
			r.add("FO("+i+')', bcSolution.getMark());
			System.out.println("VND solution: " + bcSolution.getMark());

			if(bestSolution==null)
				bestSolution = new BCSolution(bcSolution);
			else if(bcSolution.getMark()<bestSolution.getMark())
				bestSolution.copy(bcSolution);
		}


		System.out.println(bestSolution.getMark());


		long timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - firstStart, TimeUnit.NANOSECONDS);


		double seconds = timeToSolution / 1000.0;
		System.out.println("Time (s): " + seconds);
		System.out.println("Solution: " + bestSolution.getMark());
		System.out.println("-----------");

		r.add("Time (s)", seconds);
		r.add("# Constructions", 1);
		r.add("# Global F.O Value", bestSolution.getMark());
		r.add("# Memorization", bcInstance.getMemoString().size());

        bcInstance.getMemoString().clear();
        bcInstance.getC().clear();
        bcInstance = null;

		System.gc();

		showMemoData();
		bestSolution.printSolution();

		return r;
	}

	private void VNDAlgorithm(long startTime, double realKMax, BCSolution bcVNS, ArrayList<Improvement> improvement, String s, BCSolution bcSolution) {
		startTime = bcVNS.getInstance().getStartTime();
		int k = 0;
		while (k < realKMax) {
			double actualResult = bcVNS.getMark();
			improvement.get(k).improve(bcVNS);
			double result = bcVNS.getMark();
			if (actualResult <= result) {
				k++;
			} else {
				k = 0;
				bcSolution.copy(bcVNS);
			}

			if (bcVNS.checkTime(startTime)) break;
		}
	}

	public void showMemoData(){
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();

		// Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		System.out.println(heapSize + " - " + heapMaxSize + " - " + heapFreeSize);
	}

	private int neighborhoodChange(BCSolution bcVNSImprove, int k) {
		if(bcVNSImprove.getMark()<bcSolution.getMark()){
			bcSolution.copy(bcVNSImprove);
			k=1;
		}
		else {
			bcVNSImprove.copy(bcSolution);
			k++;
		}
		return k;
	}

	private void shakeRandomSwap(BCSolution bcVNS, int k) {
		if(bcVNS.getRows()<3){
			return;
		}
		for(int i=0; i<k;i++){
			int firstRandom = RandomManager.getRandom().nextInt(bcVNS.getRows());
			int secondRandom = RandomManager.getRandom().nextInt(bcVNS.getRows());
			while(firstRandom==secondRandom){
				secondRandom = RandomManager.getRandom().nextInt(bcVNS.getRows());
			}
			swapMovement(bcVNS,firstRandom,secondRandom);
		}
		bcVNS.checkCorrectResult();
	}

	@Override
	public Solution getBestSolution() {
		return null;
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "(" + constructive + ")";
	}

	private void swapMovement(BCSolution newSolution, int realI, int realJ) {
		int swap = newSolution.getRowColocation()[realI];
		newSolution.setRowColocationIndex(realI,newSolution.getRowColocation()[realJ]);
		newSolution.setRowColocationIndex(realJ,swap);
	}
}

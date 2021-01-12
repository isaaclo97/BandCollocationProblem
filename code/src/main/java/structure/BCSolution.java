package structure;

import grafo.optilib.structure.Solution;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class BCSolution implements Solution {
    
    private Integer mark;
    private ArrayList<Integer> C = new ArrayList<>();
    private int columns,rows;
    private int[][] bandCollocationMatrix;
    private BCInstance instance;
    private ArrayList<Integer> values = new ArrayList<>();
    private HashMap<String,Integer> memoString;
    private String memoSubstring[];
    private int acuElements[][];
    private int sizeOfRow;
    private int[] rowColocation;
    private long startTime;
    private HashSet<Integer> validationAllRowsDiferent;
    private LRUCache lrucache;

    public BCSolution(BCInstance instance) {
        this.instance = instance;
        this.columns = instance.getColumns();
        this.rows = instance.getRows();
        this.sizeOfRow = this.rows;
        this.mark = null;
        this.bandCollocationMatrix = instance.getBandCollocationMatrix();
        this.C = instance.getC();
        this.startTime = instance.getStartTime();
        this.validationAllRowsDiferent = new HashSet<>();

        this.memoString = instance.getMemoString();
        this.lrucache = instance.getLrucache();

        this.rowColocation = new int[this.sizeOfRow];
        for(int i=0; i<this.sizeOfRow;i++){
            this.rowColocation[i]=i;
        }

        this.memoSubstring = new String[105];
        this.acuElements = new int[this.columns][this.sizeOfRow +1];

        for(int i=0; i<this.columns;i++){
            values.add(Integer.MAX_VALUE);
            recalculateAcumulateMatrix(i);
        }


    }
    public BCSolution(BCSolution solution) {
        copy(solution);
    }

    public void copy(BCSolution solution){
        this.instance = solution.getInstance();
        this.columns = instance.getColumns();
        this.rows = instance.getRows();
        this.mark = solution.getMark();
        this.bandCollocationMatrix = instance.getBandCollocationMatrix();
        this.memoSubstring = solution.getMemoSubstring();
        this.validationAllRowsDiferent = solution.getValidationAllRowsDiferent();

        this.C = instance.getC();
        this.startTime = instance.getStartTime();
        values = new ArrayList<>(solution.values);

        this.sizeOfRow = this.rows;
        this.memoString = instance.getMemoString();
        this.lrucache = instance.getLrucache();

        this.acuElements = new int[this.columns][];
        for(int i = 0; i<this.columns;i++) {
            this.acuElements[i] = Arrays.copyOf(solution.getAcuElements(i), this.sizeOfRow + 1);
        }
        this.rowColocation = Arrays.copyOf(solution.getRowColocation(),this.sizeOfRow);

    }


    private int evaluate() {
        int res = 0;
        for(int i=0; i<this.columns;i++){
            recalculateAcumulateMatrix(i);
            substringPatternPro(i);
            int sol =evaluateDPWithSubstrings(0,i);
            res+=sol;
        }
        this.mark = res;
        return res;
    }

    private int evaluateDPWithSubstrings(int end, int column) {
        if(end>sizeOfRow) return 0x3f3f3f3f; 
        if((acuElements[column][end]-acuElements[column][0])==acuElements[column][sizeOfRow]) return 0; 
        String pattern  = memoSubstring[sizeOfRow-end];
        int memorizationMap = lrucache.getEntry(pattern);
        if(memorizationMap!=-1) return memorizationMap;
        int res = 0x3f3f3f3f;
        for(int i=0; i<C.size();i++){
            int value = 0;
            if ((end + (int)Math.pow(2,i)) < (sizeOfRow + 1)) {
                int totalOnes = (acuElements[column][end + (int) Math.pow(2, i)] - acuElements[column][end]);
                if (totalOnes > 0) {
                    value = C.get(i);
                }
                res = Math.min(res,evaluateDPWithSubstrings(end+(int)Math.pow(2,i),column)+value);
            }
        }
        lrucache.putEntry(pattern,res);
        return res;
    }

    ///////////////// START GETS AND SETS ////////////

    public Integer getMark() {
        if(this.mark==null){
            return evaluate();
        }
        return this.mark;
    }


    public BCInstance getInstance() {
        return instance;
    }

    public int[] getAcuElements(int column) {
        return acuElements[column];
    }

    public int[] getRowColocation() {
        return rowColocation;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public String[] getMemoSubstring() {
        return memoSubstring;
    }

    public HashSet<Integer> getValidationAllRowsDiferent() {
        return validationAllRowsDiferent;
    }

    public void setRowColocationIndex(int index, int value) {
        this.rowColocation[index] = value;
    }

    ///////////////// END GETS AND SETS ////////////

    ///////////////// START AUX METHODS ////////////

    public Integer reevaluateMark() {
        return this.mark = evaluate();
    }

    public void recalculateAcumulateMatrix(int start){
        acuElements[start][0]=0;
        for(int x=1; x<=sizeOfRow;x++) {
            acuElements[start][x] = bandCollocationMatrix[rowColocation[x - 1]][start] + acuElements[start][x - 1];
        }
    }

    public void substringPatternPro(int column){
        StringBuilder builder = new StringBuilder();
        for(int i=this.sizeOfRow-1; i>=0;i--){
            int elem = this.bandCollocationMatrix[rowColocation[i]][column];
            builder.append(elem);
            memoSubstring[sizeOfRow - i] = builder.toString();
        }
    }

    public boolean checkTime(long startTime) {
        long timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        double seconds = timeToSolution / 1000.0;
        //System.out.println("Time (s): " + seconds);
        if(seconds>this.getInstance().getMinutes()){
            return true;
        }
        return false;
    }

    public void checkCorrectResult() {
        validationAllRowsDiferent.clear();

        for(int i =0; i<this.getRows();i++){
            validationAllRowsDiferent.add(this.getRowColocation()[i]);
        }
        if(validationAllRowsDiferent.size()!=this.getRows()) {
            System.out.println("Row duplicated, solve it!");
            throw new RuntimeException();
        }
    }

    public void printSolution(){
        try {
            PrintWriter writeToFile = new PrintWriter("./solutions/"+this.getInstance().getName());
            for(int i=0; i<this.getRows();i++) {
                for (int j = 0; j < this.getColumns(); j++) {
                    writeToFile.write(this.getInstance().getBandCollocationMatrix()[this.getRowColocation()[i]][j] + " ");
                }
                writeToFile.write("\n");
            }
            writeToFile.write("Row permutation\n");
            for(int i=0; i<this.getRows();i++) {
                writeToFile.write(this.getRowColocation()[i] + " ");
            }
            writeToFile.write("\nTotal cost: " + this.getMark());
            writeToFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    ///////////////// END AUX METHODS ////////////
}

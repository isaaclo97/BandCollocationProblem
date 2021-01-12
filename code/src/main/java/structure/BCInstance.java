package structure;

import grafo.optilib.structure.Instance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BCInstance implements Instance {

    private String name;
    private Double ratio;
    private ArrayList<Integer> C = new ArrayList<>();
    private int columns,rows,d;
    private int[][] bandCollocationMatrix;
    private HashMap<String,Integer> memoString;
    private long startTime;
    private long minutes = 150*60;
    private LRUCache lrucache;

    public BCInstance(String path) {
        readInstance(path);
        startTime = System.nanoTime();
    }

    @Override
    public void readInstance(String path) {
        this.name = path.substring(path.lastIndexOf('\\') + 1);
        System.out.println("Reading instance " + this.name);
        FileReader fr= null;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br=new BufferedReader(fr);
        // read line by line
        String line;
        try{
            int index = -1;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");
                if(index==-1) {
                    this.ratio = Double.parseDouble(numbers[0]);
                    if(path.contains("OT")){
                        this.rows = Integer.parseInt(numbers[3]);
                        this.columns = Integer.parseInt(numbers[4]);
                        this.d = Integer.parseInt(numbers[5]);
                        for(int i=6; i<numbers.length;i++){
                            C.add(Integer.parseInt(numbers[i]));
                        }
                    }
                    else{
                        this.rows = Integer.parseInt(numbers[1]);
                        this.columns = Integer.parseInt(numbers[2]);
                        this.d = Integer.parseInt(numbers[3]);
                        for(int i=4; i<numbers.length;i++){
                            C.add(Integer.parseInt(numbers[i]));
                        }
                    }

                    bandCollocationMatrix = new int[rows][columns];
                }
                else{
                    for(int i=0; i<columns;i++){
                        bandCollocationMatrix[index][i] = Integer.parseInt(numbers[i]);
                    }
                }
                index++;
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public ArrayList getC() {
        return C;
    }

    public Double getRatio() {
        return ratio;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int[][] getBandCollocationMatrix() {
        return bandCollocationMatrix;
    }

    public HashMap<String, Integer> getMemoString() {
        if(memoString==null){
            memoString = new HashMap<>();
        }
        return memoString;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getMinutes() {
        return minutes;
    }

    public LRUCache getLrucache() {
        if(lrucache==null){
            lrucache = new LRUCache();
        }
        return lrucache;
    }

}

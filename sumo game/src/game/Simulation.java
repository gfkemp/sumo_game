/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import java.awt.FlowLayout;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Class that creates a 'generation' of NNetworks and puts them against one another - it also uses the NNets' fitness (score) to remove/mutate the weaker nets
 * @author gregclemp
 */
public class Simulation {
    private GameWorld world;
    private Rikishi player1;
    private Rikishi player2;
    private NNetwork[] generation;
    private int genNo;
    private int roundNo;
    int rematchNo = 0;
    int rematches = 0;
    int freshNo = 0;
    private int changeGenNo;
    private int changeRematchNo;
    private int changeFreshNo;
    boolean settingsChanged = false;
    
    public Simulation(GameWorld world, Rikishi player1, Rikishi player2, int generationSize){
        this.world = world;
        this.player1 = player1;
        this.player2 = player2;
        
        generation = new NNetwork[generationSize];
        roundNo = 0;
        
        initGeneration();
    }
    
    /**
     * Creates an array of random NNetworks
     */
    public void initGeneration(){
        for (int i = 0; i < generation.length; i++){
            generation[i] = new NNetwork(null, null, null, "");
        }
    }
    
    /**
     * Puts two NNetworks in two Rikishi and lets them fight each other
     */
    public void runGen(){
        if (generation.length%2 != 0) throw new IllegalArgumentException("generation is an odd number");
        
        if (roundNo == generation.length/2 && rematches < rematchNo){
            generation = shuffle(generation);
            roundNo = 0;
            rematches++;
            world.getMode().addToLog("REMATCH " + rematches + "\n");
        } else if (roundNo*2 == generation.length){
            genNo++;
            roundNo = 0;
            rematches = 0;
            newGen();
            if (settingsChanged){
                changeSettings();
            }
        }
        
        if (generation[roundNo * 2] == null) throw new IllegalArgumentException("missing net at pos " + roundNo*2);
        player1.getBrain().setNNet(generation[roundNo * 2]);
        
        if (generation[roundNo * 2 + 1] == null) throw new IllegalArgumentException("missing net at pos " + ((roundNo*2)+1));
        player2.getBrain().setNNet(generation[roundNo * 2 + 1]);
        roundNo++;
        
        world.getMode().addToLog("Generation: " + genNo + " Round: " + roundNo + " | " 
                + player1.getBrain().getNNet().getName() + " vs "
                + player2.getBrain().getNNet().getName() + " | ");
        
        world.getMode().updateLog();
        
    }
    
    /**
     * Handles the end of generation mutation and culling
     * <p>
     * The method first calls insertionSort() on the generation to sort by their scores.
     * Then it generates a mutant copy of the top 50%.
     * The generation is then shuffled.
     */
    public void newGen(){
        Random r = new Random();
        System.out.print("\n");
        
        insertionSort(generation, generation.length-1);
        printScoreBoard(generation);
        
        for (int i = 0; i < (generation.length/2) ; i++){
            generation[i].setScore(0);
            generation[i+generation.length/2] = new NNetwork(null, null, null, generation[i].getName());
            generation[i+generation.length/2].setWeights(generation[i].getWeights());
            generation[i+generation.length/2].mutateNet();
        }
        
        for (int i = 0; i < freshNo; i++){
            generation[generation.length - (i+1)] = new NNetwork(null, null, null, genNo + "." + i);
        }
        
        generation = shuffle(generation);
    }
    
    /**
     * Shuffling algorithm from https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
     * @param array Array to be shuffled
     * @return shuffled array
     */
    public NNetwork[] shuffle(NNetwork[] array){
        Random r = new Random();
        
        //Durstenfeld
        for (int i = array.length-1; i > 0; i--){
            int index = r.nextInt(i);
            
            NNetwork temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        
        return array;
    }
    
    /**
     * Recursive insertion sort, from pseudocode displayed https://en.wikipedia.org/wiki/Insertion_sort
     * @param A Array to be sorted
     * @param n Array length - 1
     */
    private void insertionSort(NNetwork[] A, int n) {
        //insertion sort [from wikipedia pseudocode]
        if (n > 0){
            insertionSort(generation, n-1);
            NNetwork x = A[n];
            int j = n - 1;
            while (j >= 0 && A[j].getScore() < x.getScore()){
                A[j+1] = A[j];
                j--;
            }
            A[j+1] = x;
        }
    }
    
    /**
     * Prints the generation scoreboard to the simulation log
     * @param A NNetwork array
     */
    private void printScoreBoard(NNetwork[] A) {
        int nameLength = 0;
        world.getMode().addToLog("\nGENERATION " + (genNo-1) + " SCORES: \n");
        for (NNetwork net : A){
            if (nameLength < net.getName().length()){
                nameLength = net.getName().length();
            }
        }
        
        for (NNetwork net : A){
            String space = "";
            for (int i = 0; i < (nameLength - net.getName().length()); i++){
                space = space + "  ";
            }
            world.getMode().addToLog(net.getName() + space + " | " + net.getScore() + "\n");
        }
        world.getMode().addToLog("\n");
    }
    
    /**
     * Changes the settings
     */
    public void changeSettings(){
        if (changeGenNo != 0){
            NNetwork[] newGen = new NNetwork[generation.length + changeGenNo];
            
            if (generation.length > newGen.length){
                for (int i = 0; i < newGen.length; i++){
                    newGen[i] = generation[i];
                }
            } else if (generation.length < newGen.length){
                for (int i = 0; i < generation.length; i++){
                    newGen[i] = generation[i];
                } 
                for (int i = generation.length; i < newGen.length; i++){
                    newGen[i] = new NNetwork(null, null, null, genNo + "." + i);
                }
            }
            generation = newGen;
            changeGenNo = 0;
        }
        
        if (changeRematchNo != 0){
            rematchNo = rematchNo + changeRematchNo;
            changeRematchNo = 0;
        }
        
        if (changeFreshNo != 0){
            freshNo = freshNo + changeFreshNo;
            changeFreshNo = 0;
        }
        
        printNextSettings();
        settingsChanged = false;
    }
    
    /**
     * Increments the amount the generation will be changed by
     * @param num either +/-2
     */
    public void changeGenSize(int num){
        int newSize = changeGenNo + num;
        if (newSize + generation.length > freshNo + changeFreshNo && newSize + generation.length > 0){
            changeGenNo = changeGenNo + num;
            settingsChanged = true;
        }
    }
    
    /**
     * Increments the amount the rematch number will be changed by
     * @param num either +/-1
     */
    public void changeRematchSize(int num){
        if (changeRematchNo + num + rematchNo >= 0){
            changeRematchNo = changeRematchNo + num;
            settingsChanged = true;
        }
    }
    
    /**
     * Increments the amount the number of culled sumo will be changed by
     * @param num either +/-1
     */
    public void changeFreshSize(int num){
        int newFresh = changeFreshNo + num;
        if (newFresh + freshNo < generation.length + changeGenNo && newFresh + freshNo >= 0){
            changeFreshNo = newFresh;
            settingsChanged = true;
        }
    }
    
    /**
     * Prints the new settings to log on round end
     */
    public void printNextSettings(){
        int size = generation.length;
        int rematches = rematchNo;
        int freshMeat = freshNo;
        world.getMode().addToLog("Generation size: " + size + "\nNumber of rematches: " + rematches + "\nNumber of Sumo culled: " + freshMeat + "\n\n");
    }
}

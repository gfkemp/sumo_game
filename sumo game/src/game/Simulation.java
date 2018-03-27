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
 *
 * @author gregclemp
 */
public class Simulation {
    private GameWorld world;
    private Rikishi player1;
    private Rikishi player2;
    private NNetwork[] generation;
    private int genNo;
    private int roundNo;
    int rematchNo = 3;
    int rematches = 0;
    int freshNo = 2;
    
    public Simulation(GameWorld world, Rikishi player1, Rikishi player2, int generationSize){
        this.world = world;
        this.player1 = player1;
        this.player2 = player2;
        
        generation = new NNetwork[generationSize];
        roundNo = 0;
        
        initGeneration();
    }
    
    public void initGeneration(){
        for (int i = 0; i < generation.length; i++){
            generation[i] = new NNetwork(null, null, null, "");
        }
    }
    
    public void runGen(){
        if (generation.length%2 != 0) throw new IllegalArgumentException("generation is an odd number");
        
        if (roundNo == generation.length/2 && rematches < rematchNo){
            generation = shuffle(generation);
            roundNo = 0;
            rematches++;
            System.out.println("REMATCH " + rematches);
        } else if (roundNo*2 == generation.length){
            genNo++;
            roundNo = 0;
            rematches = 0;
            newGen();
        }
        
        if (generation[roundNo * 2] == null) throw new IllegalArgumentException("missing net at pos " + roundNo*2);
        player1.getBrain().setNNet(generation[roundNo * 2]);
        
        if (generation[roundNo * 2 + 1] == null) throw new IllegalArgumentException("missing net at pos " + ((roundNo*2)+1));
        player2.getBrain().setNNet(generation[roundNo * 2 + 1]);
        roundNo++;
        
        System.out.printf("Generation: " + genNo + " Round: " + roundNo + " | " 
                + player1.getBrain().getNNet().getName() + " vs "
                + player2.getBrain().getNNet().getName() + " | ");
        
        /**
        if (world.getMode().getSettingChange()){
            changeSettings();
        }**/
    }
    
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

    private void printScoreBoard(NNetwork[] A) {
        int nameLength = 0;
        System.out.println("GENERATION " + (genNo-1) + " SCORES:");
        for (NNetwork net : A){
            if (nameLength < net.getName().length()){
                nameLength = net.getName().length();
            }
        }
        
        for (NNetwork net : A){
            String space = new String(new char[nameLength - net.getName().length()]).replace("\0", " ");
            System.out.println(net.getName() + space + " | " + net.getScore());
        }
        System.out.print("\n");
    }
    
    public void changeSettings(){
        System.out.println("i want to change settings");
    }
}

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
        
        if (roundNo == generation.length/2){
            genNo++;
            roundNo = 0;
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
    }
    
    public void newGen(){
        Random r = new Random();
        
        NNetwork[] newGen = new NNetwork[generation.length];
        int count = 0;
        for (int i = 0; i < generation.length; i++){
            
            switch (generation[i].getScore()){
                case 2:
                    newGen[count] = new NNetwork(null, null, null, generation[i].getName());
                    newGen[count].setWeights(generation[i].getWeights());
                    count++;
                    newGen[count] = new NNetwork(null, null, null, generation[i].getName());
                    newGen[count].setWeights(generation[i].getWeights());
                    newGen[count].mutateNet();
                    count++;
                    break;
                case 1:
                    newGen[count] = new NNetwork(null, null, null, generation[i].getName());
                    newGen[count].setWeights(generation[i].getWeights());
                    if (r.nextDouble() > 0.2){
                        newGen[count].mutateNet();
                    } else {
                        newGen[count].getWeightArray().initWeights();
                        newGen[count].getWeightArray().setName("");
                    }
                    count++;
                    break;
                case 0:
                    break;
            }
        }
        
        if (newGen.length != generation.length) throw new IllegalArgumentException("miscounted generations?");
        
        generation = shuffle(newGen);
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
}

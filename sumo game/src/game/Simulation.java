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
    int rematchNo = 4;
    int rematches = 0;
    
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
    }
    
    public void newGen(){
        Random r = new Random();
        
        NNetwork[] newGen = new NNetwork[generation.length];
        int count = 0;
        for (int i = 0; i < generation.length/2; i++){
            
            NNetwork highScore = new NNetwork(null, null, null, "");
            
            for (NNetwork net : generation){
                if (highScore.getScore() < net.getScore()){
                    highScore = net;
                    net.setScore(0);
                }
            }
            
            newGen[i] = highScore;
            newGen[i+generation.length/2] = new NNetwork(null, null, null, highScore.getName());
            newGen[i+generation.length/2].setWeights(highScore.getWeights());
            newGen[i+generation.length/2].mutateNet();
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

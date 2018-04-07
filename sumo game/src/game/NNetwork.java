/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;

/**
 * Class that initialises the weightArray of a neural network and passes through the values for forward propagation to generate movement keys
 * @author gregclemp
 */
public class NNetwork {
    
    private Brain brain;
    private Rikishi player;
    private Rikishi opponent;
    private String name;
    private double[] inputs;
    private WeightArray weightArray;
    private double[][] weights;
    private int score = 0;
    private int depth;
    
    public NNetwork(Brain brain, Rikishi player, Rikishi opponent, String name){
        this.brain = brain;
        this.player = player;
        this.opponent = opponent;
        //this.name = name;
        
        inputs = new double[8];
        //updateInputs();
        depth = (int) (Math.random()*4);
        int[] neurons = new int[2 + depth];
        neurons[0] = 8;
        neurons[neurons.length - 1] = 2;
        
        for (int i = 1; i < neurons.length-1; i++){
            neurons[i] = (int) (Math.random()*6 + 2);
        }
        
        weightArray = new WeightArray(neurons);
        
        if (!name.equals("")) {setName(name);}
    }
    
    /**
     * Sets the parameters of this NNetwork
     * @param brain the player's Brain
     * @param player the player's Rikishi
     * @param opponent the opponent's Rikishi
     */
    public void setParams(Brain brain, Rikishi player, Rikishi opponent){
        this.brain = brain;
        this.player = player;
        this.opponent = opponent;
    }
    
    /**
     * Mutates the weights in the WeightArray
     */
    public void mutateNet(){
        weightArray.mutateWeights();
        /*for (int x= 0; x < weights.length; x++){
            for (int y = 0; y < weights[x].length; y++){
                weights[x][y] += ((Math.random()*2) - 1)/2;
            }
        }*/
    }
    
    /**
     * Generates the values for the 8 node input layer
     * <p>
     * inputs[0] = Player's x position / 20;
     * inputs[1] = Player's y position / 20;
     * inputs[2] = Player's angle / (Math.PI)) - 1;
     * inputs[3] = Player's velocity / 500;
     * inputs[4] = Opponent's x position / 20;
     * inputs[5] = Opponent's y position / 20;
     * inputs[6] = Opponent's angle / (Math.PI)) - 1;
     * inputs[7] = Opponent's velocity /500;
     * 
     * All normalised to be between 0 and 1
     */
    public void updateInputs(){ //update and normalize input
        inputs[0] = (player.getPosition().x) / 20;
        inputs[1] = (player.getPosition().y) / 20;
        inputs[2] = ((player.getFixedAngle()) / (Math.PI)) - 1;
        inputs[3] = (player.getLinearVelocity().lengthSquared())/500;
        inputs[4] = (opponent.getPosition().x) / 20;
        inputs[5] = (opponent.getPosition().y) / 20;
        inputs[6] = ((opponent.getFixedAngle()) / (Math.PI)) - 1;
        inputs[7] = (opponent.getLinearVelocity().lengthSquared())/500;
    }
    
    /**
     * Passes inputs into the WeightArray to generate values for the moveKeys
     * @param keys last tick's moveKeys
     * @return generated moveKeys
     */
    public int[] forwardPass(int[] keys){
        updateInputs();
        
        double A = 0.5;
        double B = 0.5;
        double[] values;
        /*
        for (int i = 0; i < weightArray.getSize(); i++){
            double[][] layer = weightArray.getLayer(i);
            for (int x = 0; x < layer.length; x++){
                
            }
            A = sigmoid(sumDotProduct(inputs, layer[0]));
            B = sigmoid(sumDotProduct(inputs, layer[1]));
        }*/
        
        values = weightArray.propagate(inputs);
        A = values[0];
        B = values[1];
        
        keys[0] = (A >= 0.5) ? 2 : (A <= -0.5) ? 0 : 1;
        keys[1] = (B >= 0.5) ? 2 : (B <= -0.5) ? 0 : 1;
        
        //System.out.println(A + " " + B + " " + Arrays.toString(keys));
        
        return keys;
    }
    
    /*
     * Sigmoid function: 1 / (1 + e^(-x))
     * @param input input value
     * @return output value
     *
    public double sigmoid(double input){
        
        input = 1 / (1 + Math.exp(-input));
                
        return input;
    }
    
    public double sumDotProduct(double[] x, double[] w){
        double output = 0;
        if (x.length != w.length) throw new IllegalArgumentException("input and weight arrays are mismatched " + x.length + ", " + w.length);
        
        for (int i = 0; i < x.length; i++){
            output = output + (x[i] * w[i]);
        }
        
        return output;
    }*/
    
    public int getScore(){
        return score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    /**
     * Increases score
     * @param inc amount to increase score by
     */
    public void incScore(int inc){
        this.score = this.score + inc;
    }
    
    public void setWeights(ArrayList weights){
        this.weightArray.setWeights(weights);
    }
    
    public ArrayList getWeights(){
        return weightArray.getWeights();
    }
    
    public String getName(){
        return weightArray.getName();
    }
    
    public void setName(String name){
        weightArray.setName(name);
    }

    public WeightArray getWeightArray() {
        return weightArray; //To change body of generated methods, choose Tools | Templates.
    }

    public Rikishi getOpponent() {
        return opponent;
    }

    public void setOpponent(Rikishi opponent) {
        this.opponent = opponent;
    }
}

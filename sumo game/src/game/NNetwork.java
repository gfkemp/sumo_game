/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;

/**
 *
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
    
    public NNetwork(Brain brain, Rikishi player, Rikishi opponent, String name){
        this.brain = brain;
        this.player = player;
        this.opponent = opponent;
        //this.name = name;
        
        inputs = new double[8];
        //updateInputs();
        int[] neurons = {8, 6, 2};
        weightArray = new WeightArray(neurons);
        
        if (!name.equals("")) {setName(name);}
    }
    
    public void initNet(String mode){
        weights = new double[2][6];
        for (int x= 0; x < weights.length; x++){
            for (int y = 0; y < weights[x].length; y++){
                weights[x][y] = (Math.random()*2) - 1;
            }
        }
    }
    
    public void setParams(Brain brain, Rikishi player, Rikishi opponent){
        this.brain = brain;
        this.player = player;
        this.opponent = opponent;
    }
    
    public void mutateNet(){
        weightArray.mutateWeights();
        /*for (int x= 0; x < weights.length; x++){
            for (int y = 0; y < weights[x].length; y++){
                weights[x][y] += ((Math.random()*2) - 1)/2;
            }
        }*/
    }
    
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
    }
    
    public int getScore(){
        return score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
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
}

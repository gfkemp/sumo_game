/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;

/**
 * Class that initialises a random ArrayList of Double[][]s as the weights for a NNet and does forward propagation
 * @author gregclemp
 */
public class WeightArray {
    private ArrayList weights;
    private String name;
    private String valuesText;
    private JLabel valueDisplay;
    
    public WeightArray(int[] neurons){
        //[6, 2]
        weights = new ArrayList();
        for (int i = 0; i < neurons.length-1; i++){
            newLayer(neurons[i], neurons[i+1]);
        }
        
        name = "";
        valuesText = "";
        initWeights();
        nameUpdate();
        valueDisplay = new JLabel("");
    }
    
    /**
     * Adds a new double[y][x] to ArrayList weights
     * @param x number of input neurons
     * @param y number of output neurons
     */
    public void newLayer(int x, int y){
        weights.add(new double[y][x]);
    }
    
    /**
     * Returns layer from ArrayList weights
     * @param num index of layer
     * @return double[][] corresponding to layer
     */
    public double[][] getLayer(int num){
        return (double[][]) weights.get(num);
    }
    
    public int getSize(){
        return weights.size();
    }
    
    /**
     * Gives a random value to every index in weights[layer][y][x]
     * <p>
     * Random number is between -1 and 1
     */
    public void initWeights(){
        for (int i = 0; i < weights.size(); i++){
            
            double[][] layer = (double[][]) weights.get(i);
            
            for (int x= 0; x < layer.length; x++){
                
                for (int y = 0; y < layer[x].length; y++){
                    
                    layer[x][y] = (Math.random()*2) - 1;
                }
            }
        }
    }
    
    /**
     * Calls feedForward(inputs, layer, layer number) for each weight layer
     * @param inputs a double[8] with input values for the neural network
     * @return a double[2] of output values
     */
    public double[] propagate(double[] inputs){
        double[] values = null;
        
        valuesText = "<html>";
        for (int i = 0; i < inputs.length; i++){
            addValuesText("[" + String.format("%.2f", inputs[i]) + "]");
        }
        addValuesText("<br><br>");
        
        for (int i = 0; i < getSize(); i++){
            inputs = feedForward(inputs, getLayer(i), i);
        }
        
        values = inputs;
        
        if (values[0] > 0.5){
            addValuesText("Forward ");
        } else if (values[0] <= -0.5){
            addValuesText("Stop ");
        }
        
        if (values[1] > 0.5){
            addValuesText("Left ");
        } else if (values[1] <= -0.5){
            addValuesText("Right ");
        }
        
        addValuesText("</html>");
        
        return values;
    }
    
    /**
     * Passes input values through one layer of the neural network
     * <p>
     * 1. Creates a double[] 'values' with the same length as layer's output dimension
     * 2. Assigns each values[i] to the output of sumDotProduct(inputs, layer[i], num)
     *      (if it is the last layer, then it calls sigmoid() on all the values too)
     * 3. It then returns values which is used as the input array for the next call of feeForward
     * @param inputs a double[] of input values
     * @param layer a double[output neurons][input neurons] of weight values
     * @param num current layer index (for debugging)
     * @return values, a double[]
     */
    public double[] feedForward(double[] inputs, double[][] layer, int num){
        double[] values = new double[layer.length];
        
        for (int i = 0; i < values.length; i++){
            
            if (i < values.length -1){
                values[i] = sumDotProduct(inputs, layer[i], num);
            } else {
                values[i] = sigmoid(sumDotProduct(inputs, layer[i], num));
            }
            addValuesText("[" + String.format("%.2f", values[i]) + "]");
        }
        addValuesText("<br><br>");
        return values;
    }
    
    /**
     * Adds a string to the valuesText
     * @param text string to be added
     */
    public void addValuesText(String text){
        valuesText = valuesText + text;
    }
    
    public String getValuesText(){
        return valuesText;
    }
    
    /**
     * Adds a random value to every index in weights[layer][y][x]
     * <p>
     * random value is between -1/5 and 1/5
     */
    public void mutateWeights(){
        Random r = new Random();
        
        for (int i = 0; i < weights.size(); i++){
            
            double[][] layer = (double[][]) weights.get(i);
            
            for (int x= 0; x < layer.length; x++){
                
                for (int y = 0; y < layer[x].length; y++){
                    
                    layer[x][y] += (r.nextInt(2) - 1)/ (r.nextInt(5) + 5);
                }
            }
        }
        nameUpdate();
    }
    
    /*
    public void printShape(){
        printNL();
        
        for (int x = 0; x < 8; x++){
            printNeuron();
        }
        System.out.print(" (input)");
        printNL();
        
        for (int i = 0; i < weights.size(); i++){
            
            double[][] layer = (double[][]) weights.get(i);
            
            
            for (int x = 0; x < layer.length; x++){
                printNeuron();
            }
            System.out.print(" (" + layer.length + ", " + layer[0].length + ")");
            printNL();
        }
    }
    
    public void printNeuron(){
        System.out.print(" o"); 
    }
    
    public void printNL(){
        System.out.print("\n");
    }*/
    
    /**
     * Sigmoid function, 1 / (1 + e^(-x))
     * @param input mathematical input, x
     * @return output value
     */
    public double sigmoid(double input){
        
        input = 1 / (1 + Math.exp(-input));
                
        return input;
    }
    
    /**
     * Sums the dot product of the input and weight arrays
     * @param x input array, double[]
     * @param w weight array, double[]
     * @param layerNum the index of the current layer, for debugging
     * @return double, sum of the dot product of x and w
     */
    public double sumDotProduct(double[] x, double[] w, int layerNum){
        double output = 0;
        if (x.length != w.length) throw new IllegalArgumentException("input and weight arrays are mismatched " + x.length + ", " + w.length + " in layer " + layerNum);
        
        for (int i = 0; i < x.length; i++){
            output = output + (x[i] * w[i]);
        }
        
        return output;
    }
    
    /**
     * Adds a random character to the weight arrays name
     */
    public void nameUpdate(){
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrztuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        this.name = name + alphabet.charAt(r.nextInt(alphabet.length()));
        
        if (name.length() > 12){
            name = name.substring(0, 5);
        }
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public ArrayList getWeights(){
        return weights;
    }
    
    public void setWeights(ArrayList weights){
        this.weights = weights;
    }
}

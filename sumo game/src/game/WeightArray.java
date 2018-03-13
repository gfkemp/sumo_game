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
 *
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
    
    public void newLayer(int x, int y){
        weights.add(new double[y][x]);
    }
    
    public double[][] getLayer(int num){
        return (double[][]) weights.get(num);
    }
    
    public int getSize(){
        return weights.size();
    }
    
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
        
        addValuesText("</html>");
        
        values = inputs;
        return values;
    }
    
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
    
    public void addValuesText(String text){
        valuesText = valuesText + text;
    }
    
    public String getValuesText(){
        return valuesText;
    }
    
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
    }
    
    public double sigmoid(double input){
        
        input = 1 / (1 + Math.exp(-input));
                
        return input;
    }
    
    public double sumDotProduct(double[] x, double[] w, int layerNum){
        double output = 0;
        if (x.length != w.length) throw new IllegalArgumentException("input and weight arrays are mismatched " + x.length + ", " + w.length + " in layer " + layerNum);
        
        for (int i = 0; i < x.length; i++){
            output = output + (x[i] * w[i]);
        }
        
        return output;
    }
    
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

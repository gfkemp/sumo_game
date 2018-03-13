/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.*;
import java.awt.FlowLayout;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * step listener designed to check if either player is knocked out of the ring and to act accordingly
 * @author gregclemp
 */
public class StepMover implements StepListener {
    private Rikishi player1;
    private Rikishi player2;
    private GUI gui;
    private GameWorld world;
    private Dohyo dohyo;
    private SimulationSettings sim;
    private boolean reset = false;
    private float resetCount = 1;
    private float movementCount = 20;
    private float timeBeforeDraw = 100;
    private float timer = timeBeforeDraw;
    private KeyListener controller;
    private static final float SPEED = 50f;
    private JFrame roundDisplay;
    private JLabel textArea;
    
    public StepMover(GUI gui, Rikishi[] players, GameWorld world, Dohyo dohyo) {
        this.player1 = players[0];
        this.player2 = players[1];
        this.world = world;
        this.dohyo = dohyo;
        this.gui = gui;
        sim = new SimulationSettings(60);
        controller = gui.getKeyListeners()[0];
        
        roundDisplay = new JFrame("neurons");
        roundDisplay.setSize(400, 300);
        roundDisplay.setLayout(new FlowLayout());
        
        textArea = new JLabel("");  
        textArea.setBounds(10, 10,300, 100);
        
        roundDisplay.add(textArea);
        //roundDisplay.pack();
        roundDisplay.setVisible(true);
    }
    
    @Override
    public void preStep(StepEvent e) {}
    
    @Override
    public void postStep(StepEvent e) {
        
        textArea.setText(player1.getBrain().getNNet().getWeightArray().getValuesText());
        roundDisplay.setTitle(player1.getBrain().getNNet().getWeightArray().getName());
        
        checkIntersect();
        
        movementCount--;
        
        if (movementCount <= 0){
            movementA();
            movementB();
            movementCount = 20;
        }
        //System.out.println(player1.getLinearVelocity().lengthSquared() + player1.getLinearVelocity().lengthSquared());
        if((player1.getLinearVelocity().lengthSquared() + player1.getLinearVelocity().lengthSquared()) < 0.5){
            timer--;
        }
        
        if (timer <= 0){
            Rikishi loser = (player1.getPosition().lengthSquared() > player2.getPosition().lengthSquared()) ? player1 : player2;
            Rikishi victor = (player1.getPosition().lengthSquared() <= player2.getPosition().lengthSquared()) ? player1 : player2;
            
            draw();
            timer = timeBeforeDraw;
        }
        
        if (reset){ //creates a delay after a player wins to make game more satisfying
            
            resetCount--;
            
            if (resetCount <= 0){
            reset();
            }
            
        }
    }
    
    public void checkIntersect(){ //checks if the players do not intersect with the ring every tick
        if (!reset){
            if (!player1.intersects(dohyo)){ 
                roundOver(player2, player1);
            } else if (!player2.intersects(dohyo)){
                roundOver(player1, player2);
            }
        }
    }
    
    public void movementA(){
        int[] keys;
        player1.getBrain().movement(0);
        keys = player1.getBrain().getMoveKeys();
        
        if (keys[0] == 2) {//W for player 1 to go forward and S to slow and reduce rotation
            player1.applyImpulse(player1.getThrustPosition());
        } else if (keys[0] == 0) {
            player1.setLinearVelocity(player1.getLinearVelocity().mul(0.5f)); 
            player1.setAngularVelocity(player1.getAngularVelocity()*0.5f);
        }

        if (keys[1] == 2) { //A and D for player 1 to rotate
            player1.applyTorque(-SPEED); 
        } else if (keys[1] == 0) {
            player1.applyTorque(SPEED); 
        }
    }
    
    public void movementB(){
        int[] keys;
        player2.getBrain().movement(0);
        keys = player2.getBrain().getMoveKeys();
        
        if (keys[0] == 2) {//W for player 1 to go forward and S to slow and reduce rotation
            player2.applyImpulse(player2.getThrustPosition());
        } else if (keys[0] == 0) {
            player2.setLinearVelocity(player2.getLinearVelocity().mul(0.5f)); 
            player2.setAngularVelocity(player2.getAngularVelocity()*0.5f);
        }

        if (keys[1] == 2) { //A and D for player 1 to rotate
            player2.applyTorque(-SPEED); 
        } else if (keys[1] == 0) {
            player2.applyTorque(SPEED); 
        }
    }
    
    public void roundOver(Rikishi victor, Rikishi loser){
        /*
        victor.addScore();
        gui.updateScore(); */
        victor.getBrain().getNNet().setScore(2);
        
        reset = true;
        
        timer = 1000;
        
        dohyo.roundOver(victor);
        /*
        if (loser.getBrain().getNNet() != null){
            loser.getBrain().getNNet().mutateNet();
        } */
        loser.die();
        System.out.println("Player " + victor.getPlayerNum() + " wins!"); //score: " + player1.getScore() + " - " + player2.getScore());
    }
    
    public void draw(){
        reset = true;
        
        if (player1.getPosition().lengthSquared() == player2.getPosition().lengthSquared()){
            player1.getBrain().getNNet().setScore(1);
            player2.getBrain().getNNet().setScore(1);
            //player2.die();
            System.out.println("Draw!");
        }
        
        if (player1.getPosition().lengthSquared() < player2.getPosition().lengthSquared()){
            player1.getBrain().getNNet().setScore(2);
            player2.getBrain().getNNet().setScore(0);
            player2.die();
            System.out.println("Player 1 wins by proximity!");
        }
        
        if (player1.getPosition().lengthSquared() > player2.getPosition().lengthSquared()){
            player1.getBrain().getNNet().setScore(0);
            player2.getBrain().getNNet().setScore(2);
            player1.die();
            System.out.println("Player 1 wins by proximity!");
        }
        
        /*
        if (player1.getBrain().getNNet() != null){
            player1.getBrain().getNNet().initNet("");
        }
        if (player2.getBrain().getNNet() != null){
            player2.getBrain().getNNet().initNet("");
        }*/
        
        //player1.die();
    }
    
    public void reset(){
        
        world.placeBodies();
        world.getSimulation().runGen();
        player1.getBrain().resetMoveKeys();
        player2.getBrain().resetMoveKeys();
        
        reset = false;
        resetCount = 1; //number of ticks to delay next round
        
        removeConfetti();
        timer = timeBeforeDraw;
    }
    
    public void removeConfetti(){
        
        Confetti[] confetti;
        confetti = world.getConfetti();
        try {
        for (Confetti i : confetti){
            i.destroy();
        }
        } catch (NullPointerException e){
            System.out.print(" {no confetti} ");
        }
    }
}


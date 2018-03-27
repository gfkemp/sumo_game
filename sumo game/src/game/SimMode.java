/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author gregclemp
 */
public class SimMode extends Mode {
    
    private boolean settingChange = false;
    
    public SimMode(GameWorld world, String playerType){
        super(world, playerType);
        this.modeName = "sim";
    }
    
    @Override
    public void initSimulation(){
        roundDisplay = new JFrame("neurons");
        roundDisplay.setSize(400, 300);
        roundDisplay.setLayout(new FlowLayout());
        
        textArea = new JLabel("");  
        textArea.setBounds(10, 10,300, 100);
        
        roundDisplay.add(textArea);
        roundDisplay.setVisible(true);
        
        //addListeners();
        
        simulation = new Simulation(world, players[0], players[1], 8);
        System.out.println("new game");
        simulation.runGen();
    }
    
    @Override
    public void stepMover(){
        textArea.setText(players[0].getBrain().getNNet().getWeightArray().getValuesText());
        roundDisplay.setTitle(players[0].getBrain().getNNet().getWeightArray().getName());
        
        checkIntersect();
        
        movementCount--;
        
        if (movementCount <= 0){
            movementA();
            movementB();
            movementCount = movementNumber;
        }
        //System.out.println(players[0].getLinearVelocity().lengthSquared() + players[0].getLinearVelocity().lengthSquared());
        if((players[0].getLinearVelocity().lengthSquared() + players[1].getLinearVelocity().lengthSquared()) < 0.1){
            timer--;
        }
        
        if (timer <= 0){
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
            if (!players[0].intersects(dohyo)){ 
                roundOver(players[1], players[0]);
            } else if (!players[1].intersects(dohyo)){
                roundOver(players[0], players[1]);
            }
        }
    }
    
    public void movementA(){
        int[] keys;
        players[0].getBrain().movement(0);
        keys = players[0].getBrain().getMoveKeys();
        
        if (keys[0] == 2) {//W for player 1 to go forward and S to slow and reduce rotation
            players[0].applyImpulse(players[0].getThrustPosition());
        } else if (keys[0] == 0) {
            players[0].setLinearVelocity(players[0].getLinearVelocity().mul(0.5f)); 
            players[0].setAngularVelocity(players[0].getAngularVelocity()*0.5f);
        }

        if (keys[1] == 2) { //A and D for player 1 to rotate
            players[0].applyTorque(-SPEED); 
        } else if (keys[1] == 0) {
            players[0].applyTorque(SPEED); 
        }
    }
    
    public void movementB(){
        int[] keys;
        players[1].getBrain().movement(0);
        keys = players[1].getBrain().getMoveKeys();
        
        if (keys[0] == 2) {//W for player 1 to go forward and S to slow and reduce rotation
            players[1].applyImpulse(players[1].getThrustPosition());
        } else if (keys[0] == 0) {
            players[1].setLinearVelocity(players[1].getLinearVelocity().mul(0.5f)); 
            players[1].setAngularVelocity(players[1].getAngularVelocity()*0.5f);
        }

        if (keys[1] == 2) { //A and D for player 1 to rotate
            players[1].applyTorque(-SPEED); 
        } else if (keys[1] == 0) {
            players[1].applyTorque(SPEED); 
        }
    }
    
    public void roundOver(Rikishi victor, Rikishi loser){
        /*
        victor.addScore();
        gui.updateScore(); */
        victor.getBrain().getNNet().incScore(2);
        
        reset = true;
        
        timer = 1000;
        
        dohyo.roundOver(victor);
        /*
        if (loser.getBrain().getNNet() != null){
            loser.getBrain().getNNet().mutateNet();
        } */
        loser.die();
        System.out.println("Player " + victor.getPlayerNum() + " wins!"); //score: " + players[0].getScore() + " - " + players[1].getScore());
    }
    
    public void draw(){
        reset = true;
        
        if (players[0].getPosition().lengthSquared() == players[1].getPosition().lengthSquared()){
            players[0].getBrain().getNNet().incScore(1);
            players[1].getBrain().getNNet().incScore(1);
            //players[1].die();
            System.out.println("Draw!");
        }
        
        if (players[0].getPosition().lengthSquared() < players[1].getPosition().lengthSquared()){
            players[0].getBrain().getNNet().incScore(2);
            players[1].die();
            System.out.println("Player 1 wins by proximity!");
        }
        
        if (players[0].getPosition().lengthSquared() > players[1].getPosition().lengthSquared()){
            players[1].getBrain().getNNet().incScore(2);
            players[0].die();
            System.out.println("Player 2 wins by proximity!");
        }
    }
    
    public void reset(){
        world.placeBodies();
        world.getSimulation().runGen();
        players[0].getBrain().resetMoveKeys();
        players[1].getBrain().resetMoveKeys();
        
        reset = false;
        resetCount = 1; //number of ticks to delay next round
        
        timer = timeBeforeDraw;
    }
    
    @Override
    public boolean getSettingChange(){
        return settingChange;
    }
}

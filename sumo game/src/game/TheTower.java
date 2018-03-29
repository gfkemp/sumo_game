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
public class TheTower extends Mode {
    
    public TheTower(GameWorld world, String player1Type, String player2Type, Rikishi[] players, Dohyo dohyo){
        super(world, "player 1", "random nodes", players, dohyo);
        this.modeName = "Level One";
    }
    
    @Override
    public void gui(){
        player2Display = new JFrame("Neurons");
        player2Display.setSize(400, 300);
        player2Display.setLayout(new FlowLayout());
        
        player2Text = new JLabel("");  
        player2Text.setBounds(10, 10,300, 100);
        
        player2Display.add(player2Text);
        player2Display.setVisible(true);
        player2Display.setFocusable(true);
        
        world.getGui().level("The Tower, floor " + floorNo + ": randomised neural net");
        running = true;
    }
    
    @Override
    public void newLevel(){
        TheTower nextFloor = new TheTower(world, "", "", this.players, this.dohyo);
        world.setMode(nextFloor);
        world.getMode().setLives(getLives());
        world.getMode().setFloorNo(getFloorNo() + 1);
        closeFrames();
    }
    
    @Override
    public void stepMover(){
        if (running == true){
            player2Text.setText(players[1].getBrain().getNNet().getWeightArray().getValuesText());
        }
        
        checkIntersect();
        
        movementCount--;
        
        if (movementCount <= 0){
            movementA();
            movementB();
            movementCount = movementNumber;
        }
        
        if (reset){ //creates a delay after a player wins to make game more satisfying
            
            resetCount--;
            
            if (resetCount <= 0){
            reset();
            }
        }
    }
    
    @Override
    public void closeFrames(){
        player2Display.setVisible(false);
    }
}

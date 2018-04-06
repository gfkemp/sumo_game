/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Extension of KeyAdapter to allow keyboard input
 * @author gregclemp
 */
public class Controller extends KeyAdapter{
    
    private GameWorld world;
    private Rikishi player1;
    private Rikishi player2;
    
    public Controller(GameWorld world, Rikishi[] players) {
        //this.world = world;
        this.player1 = players[0];
        this.player2 = players[1];
        System.out.println("Controller constructed");
    }
    
    /**
    * Sets player1 and player2 to the new Rikishi
    *
    * @param players Rikishi array containing the two new players
    */
    public void setPlayers(Rikishi[] players){
        this.player1 = players[0];
        this.player2 = players[1];
    }
    
    /**
    * Passes key input to movement methods
    *
    * @param e Keyboard input
    */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_Q) { // Q = quit
            System.exit(0);
        }
        System.out.println(code);
        //world.getMode().playerMovement(code);
        player1Movement(code);
        player2Movement(code);
    }
    
    /**
    * Passes key input to player 1's brain to generate moveKeys
    *
    * @param code Keyboard input
    */
    public void player1Movement(int code){
        player1.getBrain().movement(code);
    }
    
    /**
    * Passes key input to player 2's brain to generate moveKeys
    *
    * @param code Keyboard input
    */
    public void player2Movement(int code){
        player2.getBrain().movement(code);
    }
}

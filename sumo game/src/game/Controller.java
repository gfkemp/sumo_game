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
 * this allows for keyboard input and control of both characters
 * @author gregclemp
 */
public class Controller extends KeyAdapter{
    
    private GameWorld world;
    private Rikishi player1;
    private Rikishi player2;
    private static final float SPEED = 50f;
    private boolean run = true;
    
    public Controller(GameWorld world, Rikishi[] players) {
        //this.world = world;
        this.player1 = players[0];
        this.player2 = players[1];
        System.out.println("Controller constructed");
    }
    
    public void setPlayers(Rikishi[] players){
        this.player1 = players[0];
        this.player2 = players[1];
    }
    
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
    
    public void player1Movement(int code){
        player1.getBrain().movement(code);
    }
    
    public void player2Movement(int code){
        player2.getBrain().movement(code);
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
    }
    
    public void on(){
        run = true;
    }
    
    public void off(){
        run = false;
    }
    
    
}

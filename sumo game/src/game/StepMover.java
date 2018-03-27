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
    private boolean reset = false;
    private float resetCount = 1;
    private float movementCount = 20;
    private float timeBeforeDraw = 50;
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
        controller = gui.getKeyListeners()[0];
    }
    
    @Override
    public void preStep(StepEvent e) {}
    
    @Override
    public void postStep(StepEvent e) {
        world.getMode().stepMover();
        //world.getMode().print("postStep, StepMover");
    }

    void setBodies(Rikishi[] players, Dohyo dohyo) {
        this.player1 = players[0];
        this.player2 = players[1];
        
        this.dohyo = dohyo;
    }
    
    
}
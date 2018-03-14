/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.*;
import java.awt.Color;
import org.jbox2d.common.Vec2;

/**
 *
 * @author gregclemp
 */
public class GameWorld extends World {
    
    private Rikishi[] players;
    private Dohyo dohyo;
    private Confetti[] confetti;
    private Simulation simulation;
    
    public GameWorld() {
        super();
        
        players = new Rikishi[2];
        confetti = new Confetti[30]; //number of particles spawned on player death
        
        initBodies();
        
        simulation = new Simulation(this, players[0], players[1], 4);
        System.out.println("new game");
        simulation.runGen();
        players[0].getBrain().getNNet().getWeightArray().printShape();
        
    }
    
    public Rikishi[] getPlayers(){
        return players;
    }
    
    public Dohyo getDohyo(){
        return dohyo;
    }
    
    public Confetti[] getConfetti(){
        return confetti;
    }
    
    public void setConfetti(Confetti[] confetti){
        this.confetti = confetti;
    }
    
    public void initBodies(){ //create bodies
        players[0] = new Rikishi(this, 1, "two node");
        players[1] = new Rikishi(this, 2, "two node");
        placeBodies();
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);
        
        dohyo = new Dohyo(this);
        dohyo.setPosition(new Vec2(0, 0));
    }
    
    public void placeBodies(){ //replace bodies in start position
        players[0].setPosition(new Vec2(-7f, 0));
        players[0].setAngle(0);
        setStill(players[0]);
        
        players[1].setPosition(new Vec2(7f, 0));
        players[1].setAngle(3.14f);
        setStill(players[1]);
    }
    
    public void setStill(Rikishi player){
        player.setAngularVelocity(0f);
        player.setLinearVelocity(new Vec2(0, 0));
    }
    
    public Simulation getSimulation(){
        return simulation;
    }
}

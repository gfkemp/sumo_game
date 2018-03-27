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
    
    //private Rikishi[] players;
    //private Dohyo dohyo;
    //private Simulation simulation;
    private Mode mode;
    private UserView view;
    private GUI gui;
    private Game game;
    
    public GameWorld(Game game) {
        super();
        this.game = game;
        view = new UserView(this, 500, 500);
        gui = new GUI(this, view);
        newMenu();
    }
    
    public Rikishi[] getPlayers(){
        return mode.getPlayers();
    }
    
    public Dohyo getDohyo(){
        return mode.getDohyo();
    }
    
    public void initBodies(){ //create bodies
        /*
        players[0] = new Rikishi(this, 1, "two node");
        players[1] = new Rikishi(this, 2, "two node");
        placeBodies();
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);
        
        dohyo = new Dohyo(this);
        dohyo.setPosition(new Vec2(0, 0));
        */
        
        mode.initBodies(mode.getPlayer1Type(), mode.getPlayer2Type());
    }
    
    public void placeBodies(){ //replace bodies in start position
        /** players[0].setPosition(new Vec2(-7f, 0));
        players[0].setAngle(0);
        setStill(players[0]);
        
        players[1].setPosition(new Vec2(7f, 0));
        players[1].setAngle(3.14f);
        setStill(players[1]); **/
        
        mode.placeBodies();
    }
    
    public void setStill(Rikishi player){
        player.setAngularVelocity(0f);
        player.setLinearVelocity(new Vec2(0, 0));
    }
    
    public void newSimMode(){
        mode = null;
        mode = new SimMode(this, "two node");
        mode.initSimulation();
    } 
    
    public void newMenu(){
        mode = new Menu(this, "");
        mode.addListeners();
    } 
    
    public Simulation getSimulation(){
        return mode.getSimulation();
    }
    
    public Mode getMode(){
        return mode;
    }
    
    public void setMode(Mode mode){
        this.mode = mode;
    }

    public UserView getView() {
        return view;
    }

    public void setView(UserView view) {
        this.view = view;
    }

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

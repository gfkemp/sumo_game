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
 * Extension of World, initialises UserView, GUI and Mode
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
        //newSimMode();
        //gui.addKeyListener(new Controller(this, mode.getPlayers()));
    }
    
    public Rikishi[] getPlayers(){
        return mode.getPlayers();
    }
    
    public Dohyo getDohyo(){
        return mode.getDohyo();
    }
    
    public void initBodies(){
        mode.initBodies(mode.getPlayer1Type(), mode.getPlayer2Type());
    }
    
    public void placeBodies(){
        mode.placeBodies();
    }
    
    public void setStill(Rikishi player){
        player.setAngularVelocity(0f);
        player.setLinearVelocity(new Vec2(0, 0));
    }
    
    /** 
     * Sets up the Menu Mode
     */
    public void newMenu(){
        mode = new Menu(this, "");
        mode.initBodies(mode.getPlayer1Type(), mode.getPlayer2Type());
        //gui.addKeyListener(new Controller(this, getPlayers())); //creates controller to allow user control
        addStepListener(new StepMover(gui, getPlayers(), this, getDohyo()));
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

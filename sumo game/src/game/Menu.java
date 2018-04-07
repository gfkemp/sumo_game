/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 * Extension of Mode, allows user to select between Campaign and Simulation
 * @author gregclemp
 */
public class Menu extends Mode {
    public Menu(GameWorld world, String blank, String blank2, Rikishi[] players, Dohyo dohyo){
        super(world, "ded", "ded", players, dohyo);
        this.modeName = "menu";
        
        //addListeners();
        //changeListeners();
    }
    
    public Menu(GameWorld world, String blank){
        super(world, "ded");
        this.modeName = "menu";
        
        //addListeners();
        //changeListeners();
    }
    
    /**
     * Initialises SimMode and assigns it
     */
    @Override
    public void newSimMode(){
        SimMode simMode = new SimMode(world, "two node", "", this.players, this.dohyo);
        world.setMode(simMode);
        simMode.initSimulation();
    }
    
    /**
     * Initialises level 1 and assigns it
     */
    @Override
    public void newLevel(){
        LevelOne levelOne = new LevelOne(world, "", "", this.players, this.dohyo);
        world.setMode(levelOne);
        world.getMode().setLives(3);
        //world.getGui().addKeyListener(new Controller(world, world.getMode().getPlayers()));
    }
    
    @Override
    public void gui(){
        world.getGui().menu();
    }
    
    /**
     * Method overridden and left blank to pause movement on the menu
     */
    @Override
    public void stepMover(){
        
    }
}

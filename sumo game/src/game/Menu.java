/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
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
    
    @Override
    public void newSimMode(){
        SimMode simMode = new SimMode(world, "two node", "", this.players, this.dohyo);
        world.setMode(simMode);
        simMode.initSimulation();
    }
    
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
    
    @Override
    public void stepMover(){
        
    }
}

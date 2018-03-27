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
    public Menu(GameWorld world, String blank){
        super(world, "ded");
        this.modeName = "menu";
        
        addListeners();
    }
    
    @Override
    public void newSimMode(){
        destroyBodies();
        SimMode simMode = new SimMode(world, "two node");
        simMode.changeListeners(controller, stepMover);
        simMode.initSimulation();
        world.setMode(simMode);
    }
    
    @Override
    public void gui(){
        world.getGui().menu();
    }
}

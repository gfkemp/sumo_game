/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 * Extension of Mode, third level of the Campaign branch
 * @author gregclemp
 */
public class LevelThree extends Mode {
    
    public LevelThree(GameWorld world, String player1Type, String player2Type, Rikishi[] players, Dohyo dohyo){
        super(world, "player 1", "defensive", players, dohyo);
        this.modeName = "Level One";
    }
    
    @Override
    public void gui(){
        world.getGui().level("Level Three: defensive");
    }
    
    @Override
    public void newLevel(){
        TheTower nextFloor = new TheTower(world, "", "", this.players, this.dohyo);
        world.setMode(nextFloor);
        world.getMode().setLives(getLives());
    }
}

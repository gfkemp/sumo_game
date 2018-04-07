/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 * Extension of Mode, first level of the Campaign branch
 * @author gregclemp
 */
public class LevelOne extends Mode {
    
    public LevelOne(GameWorld world, String player1Type, String player2Type, Rikishi[] players, Dohyo dohyo){
        super(world, "player 1", "dead", players, dohyo);
        this.modeName = "Level One";
    }
    
    @Override
    public void gui(){
        world.getGui().level("Level One: He's dead");
    }
    
    /**
     * Initialises level 2 and assigns it
     */
    @Override
    public void newLevel(){
        LevelTwo levelTwo = new LevelTwo(world, "", "", this.players, this.dohyo);
        world.setMode(levelTwo);
        world.getMode().setLives(getLives());
    }
}

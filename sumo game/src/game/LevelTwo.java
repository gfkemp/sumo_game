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
public class LevelTwo extends Mode {
    
    public LevelTwo(GameWorld world, String player1Type, String player2Type, Rikishi[] players, Dohyo dohyo){
        super(world, "player 1", "charge", players, dohyo);
        this.modeName = "Level One";
    }
    
    @Override
    public void gui(){
        world.getGui().level("Level Two: the charger");
    }
    
    @Override
    public void newLevel(){
        LevelThree levelThree = new LevelThree(world, "", "", this.players, this.dohyo);
        world.setMode(levelThree);
        world.getMode().setLives(getLives());
    }
}

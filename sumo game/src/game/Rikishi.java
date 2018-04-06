/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.*;
import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import org.jbox2d.common.Vec2;

/**
 * Class for the bodies of the Sumo
 * @author George Kemp
 */
public final class Rikishi extends DynamicBody {
    
    private static final float radius = 1f;
    private static final Shape ballShape = 
                new CircleShape(radius);
    private static float SPEED = 0f;
    private int playerNum = 0;
    private int score = 0;
    private Color color;
    private Color dcolor;
    private Color lcolor;
    private GameWorld world;
    private Brain brain;
    private Rikishi opponent;
    
    public Rikishi(GameWorld world, int playerNum, String braintype){
        super(world, ballShape);
        
        this.world = world;
        this.playerNum = playerNum;
        this.opponent = null;
        
        setGravityScale(0);
        
        addImage(new BodyImage("data/bowl" + playerNum + ".png", 2*radius));
        
        switch (playerNum) {
            case 2:
                color = new Color(123/255f, 196/255f, 255/255f, 0.5f); //blue
                dcolor = new Color(10/255f, 48/255f, 87/255f);
                lcolor = new Color(63/255f, 136/255f, 209/255f);
                break;
            case 1:
                color = new Color(255/255f, 219/255f, 60/255f, 0.5f); //orange
                dcolor = new Color(87/255f, 58/255f, 10/255f);//
                lcolor = new Color(255/255f, 159/255f, 0/255f);
                break;
            default:
                color = BLACK;
                dcolor = BLACK;
                lcolor = BLACK;
                break;
        }
        
        this.brain = new Brain(this, world, braintype);
    };
    
    public void setOpponent(Rikishi opponent){
        this.opponent = opponent;
        brain.setOpponent(opponent);
    }
    
    public Rikishi getOpponent(){
        return opponent;
    }
    
    public float getSpeed(){ //not used
        return SPEED;
    }
    
    public void incrementSpeed(float speed){ //not used
        SPEED += speed;
    }
    
    public void setSpeed(float speed){ //not used
        SPEED = speed;
    }
    
    public Body getBody() { //not used
        return this;
    }
    
    public int getPlayerNum(){
        return playerNum;
    }
    
    public Vec2 getThrustPosition(){ //this is calculated because the force to move needs to be applied from behind the centre of mass
        double rotation = this.getAngle();
        float x = (float) cos(rotation);
        float y = (float) sin(rotation);
        return new Vec2(2.5f*x , 2.5f*y);
    }
    
    
    public void addScore(){
        score++;
    }
    
    public int getScore(){
        return score;
    }
    
    public void die(){
        setPosition(new Vec2(100, 100));
        brain.resetMoveKeys();
    }
    
    public Color getColor(){
        return color;
    }
    
    public Color getDColor(){
        return dcolor;
    }
    
    public Brain getBrain(){
        return brain;
    }
    
    public double getFixedAngle(){
        double angle = this.getAngle()%(2*Math.PI);
        if (angle < 0){
            angle = 2*Math.PI - Math.abs(angle)%(2*Math.PI);
        }
        return angle;
    }
    
    public GameWorld getWorld(){
        return world;
    }
    
    public void changeBrain(String braintype){
        this.brain = new Brain(this, world, braintype);
        
        if (braintype.equals("random nodes")){
            brain.setNNet(new NNetwork(this.brain, this, this.opponent, "jeff"));
        }
    }
}

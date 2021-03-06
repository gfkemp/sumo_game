/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.event.KeyEvent;
import static java.lang.Math.random;
import org.jbox2d.common.Vec2;

/**
 * Class that initialises/runs the AI for the sumos
 * @author gregclemp
 */
public class Brain {
    
    private Rikishi player;
    private Rikishi opponent;
    /**
    * String that dictates what method is used to generate the moveKeys, initialised at construction
    */
    private String braintype;
    private GameWorld world;
    /**
    * An int array that corresponds to arrow key control to decide how the sumo will move: [0] = 2, W; [0] = 0, S; [1] = 2, A; [1] = 0, D;
    */
    private int[] moveKeys;
    private String mode;
    /**
    * The last keyboard input
    */
    private int lastKeyEvent;
    private NNetwork nNet;
    
    public Brain(Rikishi player, GameWorld world, String braintype){
        this.player = player;
        this.braintype = braintype;
        this.world = world;
        this.nNet = null;
        
        moveKeys = new int[2];
        moveKeys[0] = 1;
        moveKeys[1] = 1;
        
        this.mode = "init";
    }
    
    /**
    * Sets the opponent param to be the other Rikishi
    * <p>
    * Sets the opponent to be the other Rikishi, this allows Brain to access the opponents position/velocity for more complicated AI
    *
    * @param opponent the opponent Rikishi of this brain
    */
    public void setOpponent(Rikishi opponent){
        this.opponent = opponent;
        if (nNet != null){
            nNet.setOpponent(opponent);
        }
    }
    
    /**
    * Calls method to generate moveKeys based on braintype
    * <p>
    * Uses a switch statement, on braintype, to call methods that take in parameters code and keys to return moveKeys
    *
    * @param code Keyboard input
    */
    public void movement(int code){
        int[] keys = new int[2];
        keys[0] = 1;
        keys[1] = 1;
        
        switch (braintype){
            case "player 1":
                keys = player1(code, keys);
                break;
            case "player 2":
                keys = player2(code, keys);
                break;
            case "dead":
                break;
            case "charge":
                keys = charge(keys);
                break;
            case "smartCharge":
                keys = smartCharge(keys);
                break;
            case "defensive":
                keys = defensive(keys);
                break;
            case "chaser":
                keys = chaser(keys);
                break;
            case "two node":
                keys = twoNode(keys);
                break;
            case "random nodes":
                keys = twoNode(keys);
                break;
            case "debug":
                //keys = debug(code, keys);
                break;
            default:
        }
        
        moveKeys = keys;
    }
    
    /**
    * Returns moveKeys
    *
    * @return moveKeys, int array that declares player movement
    */
    public int[] getMoveKeys(){
        return moveKeys;
    }
    
    /**
    * Resets moveKeys to {1, 1}, the neutral position
    */
    public void resetMoveKeys(){
        mode = "init";
        moveKeys[0] = 1;
        moveKeys[1] = 1;
    }
    
    /**
    * Returns nNet
    *
    * @return nNet, this Brain's NNetwork
    */
    public NNetwork getNNet(){
        return nNet;
    }
    
    /**
    * Changes this Brain's nNet to the one passed into the method
    *
    * @param nNet the new NNetwork for this Brain
    */
    public void setNNet(NNetwork nNet){
        if (nNet == null) throw new IllegalArgumentException("there is no Neural Net");
        this.nNet = nNet;
        nNet.setParams(this, player, opponent);
    }
    
    /**
    * Sets moveKeys to correspond with WASD control
    *
    * @param code Keyboard input
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] player1(int code, int[] keys){
        
        if (code == 0){
            code = lastKeyEvent;
        }
        
        if (code == KeyEvent.VK_W) {//W for player 1 to go forward and S to slow and reduce rotation
                keys[0] = 2;
            } else if (code == KeyEvent.VK_S) {
                keys[0] = 0;
            }
        
        if (code == KeyEvent.VK_D) { //A and D for player 1 to rotate
                keys[1] = 2;
            } else if (code == KeyEvent.VK_A) {
                keys[1] = 0;
        }
        
        lastKeyEvent = code;
        return keys;
    }
    
    /**
    * Sets moveKeys to correspond with arrow key control
    *
    * @param code Keyboard input
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] player2(int code, int[] keys){
        
        if (code == 0){
            code = lastKeyEvent;
        }
        
        if (code == KeyEvent.VK_UP) {//W for player 1 to go forward and S to slow and reduce rotation
                keys[0] = 2;
            } else if (code == KeyEvent.VK_DOWN) {
                keys[0] = 0;
            }
        
        if (code == KeyEvent.VK_RIGHT) { //A and D for player 1 to rotate
                keys[1] = 2;
            } else if (code == KeyEvent.VK_LEFT) {
                keys[1] = 0;
            }
        
        lastKeyEvent = code;
        return keys;
    }
    
    /**
    * Sets moveKeys[0] = 2 so the Rikishi charges in a straight line
    *
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] charge(int[] keys){
        keys[0] = 2;
        return keys;
    }
    
    /**
    * Sets moveKeys to charge if the opponent is in a straight line in front, if not the moveKeys are set to spin around and "seek"
    *
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] smartCharge(int[] keys){
        
        if (mode.equals("init") || mode.equals("seek") || mode.equals("charge")){
            if (opponent.contains(player.getPosition().add(player.getThrustPosition()))
                    || opponent.contains(player.getPosition().add(player.getThrustPosition().mul(2))) 
                    || opponent.contains(player.getPosition().add(player.getThrustPosition().mul(3)))
                    || opponent.contains(player.getPosition().add(player.getThrustPosition().mul(4)))
                    || opponent.contains(player.getPosition().add(player.getThrustPosition().mul(5)))){
                mode = "charge";
            } else {
                mode = "halt";
            }  
        } else if (mode.equals("halt")) {
            double rand = random();
            if (rand > 0.5){
                mode = "halt";
            } else {
                mode = "seek";
            }
        }
        
        switch (mode){
            case "init":
                keys[0] = 1;
                keys[1] = 1;
                break;
            case "charge":
                keys[0] = 2;
                keys[1] = 1;
                break;
            case "halt":
                keys[0] = 0;
                keys[1] = 1;
                break;
            case "seek":
                keys[0] = 1;
                keys[1] = 1;
                break;
        }
        
        return keys;
    }
    
    /**
    * Sets moveKeys in an attempt to reach the centre of the ring
    *
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] defensive(int[] keys){
        //if (mode.equals("init") || mode.equals("forward") || mode.equals("turn left") || mode.equals("turn right") || mode.equals("turn left")){
        double desiredAngle = Math.atan((player.getPosition().y / player.getPosition().x));
        if (player.getPosition().x >= 0){
            desiredAngle = desiredAngle + Math.PI;
        } else if (player.getPosition().x < 0 && player.getPosition().y >= 0){
            desiredAngle = (desiredAngle + 2*Math.PI);
        }
        
        double minOppositeAngle = (desiredAngle - Math.PI);
        double maxOppositeAngle = (desiredAngle + Math.PI);
        
        double angle = player.getAngle()%(2*Math.PI);
        if (angle < 0){
            angle = 2*Math.PI - Math.abs(angle)%(2*Math.PI);
        }
        
        double difference = (angle - desiredAngle - 2*Math.PI)%(2*Math.PI);
        
        if (player.getPosition().lengthSquared() >= 0.1){
            if (Math.abs(difference) <= 0.1){
                mode = "forward";
            } else if (Math.abs(difference) <= 0.3){
                if (mode.equals("halt")){
                    mode = "forward";
                } else {
                    mode = "halt";
                }
            } else if ((angle > desiredAngle || angle < minOppositeAngle) && angle < maxOppositeAngle){
                if (mode.equals("turn left")){
                    mode = "halt";
                } else {
                    mode = "turn right";
                }
            } else if ((angle < desiredAngle || angle > maxOppositeAngle) && angle > minOppositeAngle){
                if (mode.equals("turn right")){
                    mode = "halt";
                } else {
                    mode = "turn left";
                }
            }
        } else {
            mode = "halt";
        }
        
        //System.out.printf("position: (%.2f, %.2f) | angle: %.2f | desired angle: %.2f, %.2f, %.2f | %s \n", player.getPosition().x, player.getPosition().y, angle, minOppositeAngle, desiredAngle, maxOppositeAngle, mode);
        
        switch (mode){
            case "init":
                keys[0] = 1;
                keys[1] = 1;
                break;
            case "forward":
                keys[0] = 2;
                keys[1] = 1;
                break;
            case "turn left":
                keys[0] = 1;
                keys[1] = 0;
                break;
            case "turn right":
                keys[0] = 1;
                keys[1] = 2;
                break;
            case "halt":
                keys[0] = 0;
                keys[1] = 1;
                break;
        }
        return keys;
    }
    
    /**
    * Sets moveKeys in an attempt to chase down the opponent
    *
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] chaser(int[] keys){
        //if (mode.equals("init") || mode.equals("forward") || mode.equals("turn left") || mode.equals("turn right") || mode.equals("turn left")){
        Vec2 desiredPosition = player.getPosition().sub(opponent.getPosition());
        double desiredAngle = (Math.atan((desiredPosition.y) / (desiredPosition.x)));
        if (player.getPosition().x >= 0){
            desiredAngle = desiredAngle + Math.PI;
        } else if (player.getPosition().x < 0 && player.getPosition().y >= 0){
            desiredAngle = (desiredAngle + 2*Math.PI);
        }
        
        if (desiredAngle < 0){
            desiredAngle = desiredAngle + 2*Math.PI;
        }
        
        double minOppositeAngle = (desiredAngle - Math.PI);
        double maxOppositeAngle = (desiredAngle + Math.PI);
        
        double angle = player.getAngle()%(2*Math.PI);
        if (angle < 0){
            angle = 2*Math.PI - Math.abs(angle)%(2*Math.PI);
        }
        
        double difference = (angle - desiredAngle - 2*Math.PI)%(2*Math.PI);
        
        if (player.getPosition().lengthSquared() >= 0.1){
            if (Math.abs(difference) <= 0.1){
                mode = "forward";
            } else if (Math.abs(difference) <= 0.3){
                if (mode.equals("halt")){
                    mode = "forward";
                } else {
                    mode = "halt";
                }
            } else if ((angle > desiredAngle || angle < minOppositeAngle) && angle < maxOppositeAngle){
                if (mode.equals("turn left")){
                    mode = "halt";
                } else {
                    mode = "turn right";
                }
            } else if ((angle < desiredAngle || angle > maxOppositeAngle) && angle > minOppositeAngle){
                if (mode.equals("turn right")){
                    mode = "halt";
                } else {
                    mode = "turn left";
                }
            }
        } else {
            mode = "halt";
        }
        
        //System.out.printf("position: (%.2f, %.2f) | angle: %.2f | desired angle: %.2f, %.2f, %.2f | %s \n", player.getPosition().x, player.getPosition().y, angle, minOppositeAngle, desiredAngle, maxOppositeAngle, mode);
        
        switch (mode){
            case "init":
                keys[0] = 2;
                keys[1] = 1;
                break;
            case "forward":
                keys[0] = 2;
                keys[1] = 1;
                break;
            case "turn left":
                keys[0] = 1;
                keys[1] = 0;
                break;
            case "turn right":
                keys[0] = 1;
                keys[1] = 2;
                break;
            case "halt":
                keys[0] = 0;
                keys[1] = 1;
                break;
        }
        return keys;
    }
    
    /**
    * Sets moveKeys to the result of the forward propagation through a neural net
    *
    * @param keys int array
    * @return keys int array that declares player movement
    */
    public int[] twoNode(int[] keys){
        if (nNet == null){
            nNet = new NNetwork(this, player, opponent, "two node");
        }
        
        nNet.forwardPass(keys);
        
        return keys;
    }
     /*
    public int[] debug(int code, int[] keys){
        //TEST IF LOGIC HERE:
        Vec2 desiredPosition = player.getPosition().sub(opponent.getPosition());
        double desiredAngle = Math.atan(((desiredPosition.y) / (desiredPosition.x)));
        if (player.getPosition().x >= 0){
            desiredAngle = desiredAngle + Math.PI;
        } else if (player.getPosition().x < 0 && player.getPosition().y >= 0){
            desiredAngle = (desiredAngle + 2*Math.PI);
        }
        
        double minOppositeAngle = (desiredAngle - Math.PI);
        double maxOppositeAngle = (desiredAngle + Math.PI);
        
        double angle = player.getAngle()%(2*Math.PI);
        if (angle < 0){
            angle = 2*Math.PI - Math.abs(angle)%(2*Math.PI);
        }
        
        double difference = (angle - desiredAngle - 2*Math.PI)%(2*Math.PI);
        
        if (desiredPosition.lengthSquared() >= 0.1){
            if (Math.abs(difference) <= 0.1){
                mode = "forward";
            } else if (Math.abs(difference) <= 0.3){
                if (mode.equals("halt")){
                    mode = "forward";
                } else {
                    mode = "halt";
                }
            } else if ((angle > desiredAngle || angle < minOppositeAngle) && angle < maxOppositeAngle){
                if (mode.equals("turn left")){
                    mode = "halt";
                } else {
                    mode = "turn right";
                }
            } else if ((angle < desiredAngle || angle > maxOppositeAngle) && angle > minOppositeAngle){
                if (mode.equals("turn right")){
                    mode = "halt";
                } else {
                    mode = "turn left";
                }
            }
        } else {
            mode = "halt";
        }
        
        //System.out.printf("position: (%.2f, %.2f) (%.2f, %.2f) | angle: %.2f | desired angle: %.2f, %.2f, %.2f | %s \n", player.getPosition().x, player.getPosition().y, opponent.getPosition().x, opponent.getPosition().y, angle, minOppositeAngle, desiredAngle, maxOppositeAngle, mode);
        //------
        if (code == 0){
            code = lastKeyEvent;
        }
        
        if (code == KeyEvent.VK_W) {//W for player 1 to go forward and S to slow and reduce rotation
                keys[0] = 2;
            } else if (code == KeyEvent.VK_S) {
                keys[0] = 0;
        }
        
        if (code == KeyEvent.VK_D) { //A and D for player 1 to rotate
                keys[1] = 2;
            } else if (code == KeyEvent.VK_A) {
                keys[1] = 0;
        }
        
        lastKeyEvent = code;
        return keys; 
    } */
}

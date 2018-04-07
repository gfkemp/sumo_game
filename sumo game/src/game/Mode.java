/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jbox2d.common.Vec2;

/**
 * Abstract Class that handles the bodies and their interactions
 * @author gregclemp
 */
public abstract class Mode {
    protected GameWorld world;
    protected Rikishi[] players;
    protected Dohyo dohyo;
    protected Simulation simulation;
    protected String player1Type;
    protected String player2Type;
    protected String modeName;
    
    protected boolean reset = false;
    protected float resetCount = 25;
    protected float movementNumber = 20;
    protected float movementCount = movementNumber;
    protected float timeBeforeDraw = 50;
    protected float timer = timeBeforeDraw;
    protected Controller controller;
    protected StepMover stepMover;
    protected static final float SPEED = 50f;
    protected JFrame player1Display;
    protected JFrame player2Display;
    protected JLabel player1Text;
    protected JLabel player2Text;
    protected JFrame settings;
    protected int lives = 3;
    protected int floorNo = 1;
    protected JFrame logDisplay;
    protected JScrollPane logPane;
    protected JTextArea logArea;
    protected String log = "";
    protected boolean running = false;
    
    public Mode(GameWorld world, String player1Type, String player2Type){
        this.world = world;
        this.player1Type = player1Type;
        this.player2Type = player2Type;
        //initBodies(player1Type, player2Type);
        gui();
    }
    
    public Mode(GameWorld world, String player1Type){
        this.world = world;
        this.player1Type = player1Type;
        this.player2Type = player1Type;
        //initBodies(player1Type, player2Type);
        gui();
    }
    
    public Mode(GameWorld world, String player1Type, String player2Type, Rikishi[] players, Dohyo dohyo){
        this.world = world;
        this.player1Type = player1Type;
        this.player2Type = player2Type;
        
        this.players = players;
        
        this.players[0] = players[0];
        this.players[1] = players[1];
        placeBodies();
        this.players[0].setOpponent(players[1]);
        this.players[1].setOpponent(players[0]);
        
        this.players[0].changeBrain(this.player1Type);
        this.players[1].changeBrain(this.player2Type);
        
        this.players[0].setOpponent(players[1]);
        this.players[1].setOpponent(players[0]);
        
        this.dohyo = dohyo;
        this.dohyo.setPosition(new Vec2(0, 0));
        
        System.out.println("bodies copied");
        
        gui();
    }
    
    /**
     * Initialises both players and the ring
     * 
     * @param player1Type Brain type for player 1
     * @param player2Type Brain type for player 2
     */
    public void initBodies(String player1Type, String player2Type){ //create bodies
        players = new Rikishi[2];
        players[0] = new Rikishi(world, 1, player1Type);
        players[1] = new Rikishi(world, 2, player2Type);
        placeBodies();
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);
        
        dohyo = new Dohyo(world);
        dohyo.setPosition(new Vec2(0, 0));
        
        System.out.println("bodies initiated");
    }
    
    /**
     * Returns the players to their starting positions
     */
    public void placeBodies(){ //replace bodies in start position
        players[0].setPosition(new Vec2(-7f, 0));
        players[0].setAngle(0);
        setStill(players[0]);
        
        players[1].setPosition(new Vec2(7f, 0));
        players[1].setAngle(3.14f);
        setStill(players[1]);
    }
    
    /**
     * Sets a player's angular and linear velocities to 0
     * 
     * @param player the Rikishi to be set still
     */
    public void setStill(Rikishi player){
        player.setAngularVelocity(0f);
        player.setLinearVelocity(new Vec2(0, 0));
    }
    
    /**
     * Creates a new Menu Mode and assigns it
     */
    public void returnToMenu(){
        //destroyBodies();
        log = "";
        Menu menu = new Menu(world, "", "", this.players, this.dohyo);
        //menu.changeListeners();
        world.setMode(menu);
    }
    
    /**
     * Creates a new Menu Mode and assigns it (after dieing on the campaign)
     */
    public void newLossMenu(){
        Menu menu = new Menu(world, "", "", this.players, this.dohyo);
        world.setMode(menu);
        world.getGui().changeLevelName("You died! Try again?");
    }
    
    /**
     * Initialises Controller and StepMover
     */
    public void addListeners(){
        controller = new Controller(world, players);
        world.getGui().addKeyListener(controller);
        
        stepMover = new StepMover(world.getGui(), players, world, dohyo);
        world.addStepListener(stepMover);
        
        System.out.println("listeners added");
    }
    
    /**
     * Replaces the players in the established Controller
     */
    public void changeListeners(){
        controller = new Controller(world, players);
        world.getGui().removeKeyListener(world.getGui().getKeyListeners()[0]);
        world.getGui().addKeyListener(controller);
        //this.stepMover = stepMover;
        
        controller.setPlayers(this.players);
        //stepMover.setBodies(this.players, this.dohyo);
        
        System.out.println("listeners changed");
        
        //controller = new Controller(players); */
    }
    
    /**
     * Abstract method for generating the GUI of the mode
     */
    public void gui(){
        
    }
    
    public Simulation getSimulation(){
        return simulation;
    }
    
    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }

    public Rikishi[] getPlayers() {
        return players;
    }

    public void setPlayers(Rikishi[] players) {
        this.players = players;
    }

    public Dohyo getDohyo() {
        return dohyo;
    }

    public void setDohyo(Dohyo dohyo) {
        this.dohyo = dohyo;
    }

    public String getPlayer1Type() {
        return player1Type;
    }

    public void setPlayer1Type(String player1Type) {
        this.player1Type = player1Type;
    }

    public String getPlayer2Type() {
        return player2Type;
    }

    public void setPlayer2Type(String player2Type) {
        this.player2Type = player2Type;
    }
    
    /**
     * Returns the name of the current mode
     * <p>
     * Also returns and what method it was called in for debugging purposes
     * @param location A string that gives the programmer a hint where the function was called!
     */
    public void print(String location){
        System.out.println(modeName + " : " + location);
    }
    
    public void initSimulation(){
        
    }
    
    /**
     * Handles movement
     */
    public void stepMover(){
        checkIntersect();
        
        movementCount--;
        
        if (movementCount <= 0){
            movementA();
            movementB();
            movementCount = movementNumber;
        }
        
        if (reset){ //creates a delay after a player wins to make game more satisfying
            
            resetCount--;
            
            if (resetCount <= 0){
            reset();
            }
            
        }
    }
    
    /**
     * Checks whether each player has stopped intersecting the ring
     * <p>
     * Calls roundOver(Rikishi, Rikishi) passing in the Rikishi that didn't fall out and the one that did respectively
     */
    public void checkIntersect(){ //checks if the players do not intersect with the ring every tick
        if (!reset){
            if (!players[0].intersects(dohyo)){ 
                roundOver(players[1], players[0]);
            } else if (!players[1].intersects(dohyo)){
                roundOver(players[0], players[1]);
            }
        }
    }
    
    /**
     * Handles the movement for player 1
     * <p>
     * Gets moveKeys from player 1's Brain and applies physics to it accordingly:
     * keys[0] == 2, Applies an impulse forwards
     * keys[0] == 0, halves both the linear and angular velocity - slowing the player
     * keys[1] == 2, applies a counterclockwise torque
     * keys[1] == 0, applies a clockwise torque
     */
    public void movementA(){
        int[] keys;
        //players[0].getBrain().movement(0);
        keys = players[0].getBrain().getMoveKeys();
        
        if (keys[0] == 2) {//W for player 1 to go forward and S to slow and reduce rotation
            players[0].applyImpulse(players[0].getThrustPosition());
        } else if (keys[0] == 0) {
            players[0].setLinearVelocity(players[0].getLinearVelocity().mul(0.5f)); 
            players[0].setAngularVelocity(players[0].getAngularVelocity()*0.5f);
        }

        if (keys[1] == 2) { //A and D for player 1 to rotate
            players[0].applyTorque(-SPEED); 
        } else if (keys[1] == 0) {
            players[0].applyTorque(SPEED); 
        }
    }
    
    /**
     * Handles the movement for player 2
     * <p>
     * Gets moveKeys from player 2's Brain and applies physics to it accordingly:
     * keys[0] == 2, Applies an impulse forwards
     * keys[0] == 0, halves both the linear and angular velocity - slowing the player
     * keys[1] == 2, applies a counterclockwise torque
     * keys[1] == 0, applies a clockwise torque
     */
    public void movementB(){
        int[] keys;
        //players[1].getBrain().movement(0);
        keys = players[1].getBrain().getMoveKeys();
        
        if (keys[0] == 2) {//W for player 1 to go forward and S to slow and reduce rotation
            players[1].applyImpulse(players[1].getThrustPosition());
        } else if (keys[0] == 0) {
            players[1].setLinearVelocity(players[1].getLinearVelocity().mul(0.5f)); 
            players[1].setAngularVelocity(players[1].getAngularVelocity()*0.5f);
        }

        if (keys[1] == 2) { //A and D for player 1 to rotate
            players[1].applyTorque(-SPEED); 
        } else if (keys[1] == 0) {
            players[1].applyTorque(SPEED); 
        }
    }
    
    /**
     * Handles the end of round effects for the victor and loser
     * <p>
     * If the victor not the user then it calls loseLife()
     * @param victor Rikishi that won the round
     * @param loser Rikishi that lost the round
     */
    public void roundOver(Rikishi victor, Rikishi loser){
        reset = true;
        
        if (victor == players[0]){
        } else if (victor == players[1]){
            loseLife();
        }
        loser.die();
    }
    
    /**
     * Calls newLevel()
     */
    public void reset(){
        newLevel();
        
        /*
        world.placeBodies();
        reset = false;
        resetCount = 70; //number of ticks to delay next round */
    }

    public float getResetCount() {
        return resetCount;
    }

    public void setResetCount(float resetCount) {
        this.resetCount = resetCount;
    }

    public float getMovementNumber() {
        return movementNumber;
    }

    public void setMovementNumber(float movementNumber) {
        this.movementNumber = movementNumber;
    }

    public float getTimeBeforeDraw() {
        return timeBeforeDraw;
    }

    public void setTimeBeforeDraw(float timeBeforeDraw) {
        this.timeBeforeDraw = timeBeforeDraw;
    }
    
    public boolean getSettingChange(){
        return false;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public StepMover getStepMover() {
        return stepMover;
    }

    public void setStepMover(StepMover stepMover) {
        this.stepMover = stepMover;
    }
    
    public void newSimMode(){
    }
    
    /**
     * Abstract method that generates the next level on the campaign branch
     */
    public void newLevel(){
        System.out.println("new level");
    }
    
    /**
     * Destroys all bodies, Rikishi and Dohyo
     */
    public void destroyBodies(){
        players[0].destroy();
        players[1].destroy();
        dohyo.destroyFixture();
        dohyo.destroy();
    }
    
    /**
     * Displays the settings frame
    */
    public void displaySettings(){
    }
    
    /**
     * Closes all extra frames
     */
    public void closeFrames(){
    }
    
    /**
     * Removes a life and calls newLossMenu() if you run out
     */
    public void loseLife(){
        lives = lives - 1;
        world.getGui().updateLives(lives);
        
        if (lives <= 0){
            newLossMenu();
            floorNo = 1;
        }
    }
    
    public void setLives(int lives){
        this.lives = lives;
    }
    
    public int getLives(){
        return lives;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
        world.getGui().changeLevelName("The Tower, floor " + floorNo + ": randomised neural net");
    }
    
    public void playerMovement(int code){
        players[0].getBrain().movement(code);
        players[1].getBrain().movement(code);
    }
    
    /**
     * Adds text to the log
     * @param line Text to add to the log
     */
    public void addToLog(String line){
        log = log + line;
        //System.out.println(log);
    }
    
    /**
     * When called the log's display is updated
     */
    public void updateLog(){
        logArea.setText(log);
    }
}

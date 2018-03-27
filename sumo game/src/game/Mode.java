/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.jbox2d.common.Vec2;

/**
 *
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
    protected float resetCount = 1;
    protected float movementNumber = 20;
    protected float movementCount = movementNumber;
    protected float timeBeforeDraw = 50;
    protected float timer = timeBeforeDraw;
    protected Controller controller;
    protected StepMover stepMover;
    protected static final float SPEED = 50f;
    protected JFrame roundDisplay;
    protected JLabel textArea;
    
    public Mode(GameWorld world, String player1Type, String player2Type){
        this.world = world;
        this.player1Type = player1Type;
        this.player2Type = player2Type;
        this.modeName = "mode blank";
        initBodies(player1Type, player2Type);
    }
    
    public Mode(GameWorld world, String player1Type){
        this.world = world;
        this.player1Type = player1Type;
        this.player2Type = player1Type;
        initBodies(player1Type, player2Type);
    }
    
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
    
    public void placeBodies(){ //replace bodies in start position
        players[0].setPosition(new Vec2(-7f, 0));
        players[0].setAngle(0);
        setStill(players[0]);
        
        players[1].setPosition(new Vec2(7f, 0));
        players[1].setAngle(3.14f);
        setStill(players[1]);
    }
    
    public void setStill(Rikishi player){
        player.setAngularVelocity(0f);
        player.setLinearVelocity(new Vec2(0, 0));
    }
    
    public void nextMode(Mode mode){
        world.setMode(mode);
        addListeners();
    }
    
    public void addListeners(){
        controller = new Controller(players);
        world.getGui().addKeyListener(controller);
        
        stepMover = new StepMover(world.getGui(), players, world, dohyo);
        world.addStepListener(stepMover);
        
        //world.getMode().print("addListeners, Mode");
    }
    
    public void changeListeners(Controller controller, StepMover stepMover){
        this.controller = controller;
        this.stepMover = stepMover;
        
        this.controller.setPlayers(players);
        this.stepMover.setBodies(players, dohyo);
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
    
    public void print(String location){
        System.out.println(modeName + " : " + location);
    }
    
    public void initSimulation(){
        
    }
    
    public void stepMover(){
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
    
    public void destroyBodies(){
        players[0].destroy();
        players[1].destroy();
        dohyo.destroyFixture();
        dohyo.destroy();
    }
}

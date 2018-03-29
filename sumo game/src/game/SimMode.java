/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author gregclemp
 */
public class SimMode extends Mode implements ActionListener{
    
    private boolean settingChange = false;
    private JButton[] buttons;
    
    public SimMode(GameWorld world, String playerType, String playerType2, Rikishi[] players, Dohyo dohyo){
        super(world, playerType, playerType, players, dohyo);
        this.modeName = "sim";
        
        
    }
    
    @Override
    public void initSimulation(){
        logDisplay = new JFrame("log");
        logDisplay.setSize(400, 300);
        logDisplay.setLayout(new BorderLayout());
        
        logArea = new JTextArea(log);
        logArea.setEditable(false);
        
        logPane = new JScrollPane(logArea);
        logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        logDisplay.add(logPane);
        logDisplay.setVisible(true);
        
        player1Display = new JFrame("neurons");
        player1Display.setSize(400, 300);
        player1Display.setLayout(new FlowLayout());
        
        player1Text = new JLabel("");  
        player1Text.setBounds(10, 10,300, 100);
        
        player1Display.add(player1Text);
        
        player2Display = new JFrame("neurons");
        player2Display.setSize(400, 300);
        player2Display.setLayout(new FlowLayout());
        
        player2Text = new JLabel("");  
        player2Text.setBounds(10, 10,300, 100);
        
        player2Display.add(player2Text);
        
        settings = new JFrame("Settings");
        settings.setSize(250, 500);
        settings.setLayout(new FlowLayout());
        
        buttons = new JButton[3];
        
        buttons[0] = new JButton("Display Player 1 Values");
        settings.add(buttons[0]);
        buttons[0].setActionCommand("Display Player 1 Values");
        buttons[0].addActionListener(this);
        
        buttons[1] = new JButton("Display Player 2 Values");
        settings.add(buttons[1]);
        buttons[1].setActionCommand("Display Player 2 Values");
        buttons[1].addActionListener(this);
        
        buttons[2] = new JButton("Display Log");
        settings.add(buttons[2]);
        buttons[2].setActionCommand("Display Log");
        buttons[2].addActionListener(this);
        
        simulation = new Simulation(world, players[0], players[1], 16);
        System.out.println("new game");
        simulation.runGen();
    }
    
    @Override
    public void stepMover(){
        player1Text.setText(players[0].getBrain().getNNet().getWeightArray().getValuesText());
        player1Display.setTitle(players[0].getBrain().getNNet().getWeightArray().getName());
        
        player2Text.setText(players[1].getBrain().getNNet().getWeightArray().getValuesText());
        player2Display.setTitle(players[1].getBrain().getNNet().getWeightArray().getName());
        
        checkIntersect();
        
        movementCount--;
        
        if (movementCount <= 0){
            movementA();
            movementB();
            movementCount = movementNumber;
        }
        //System.out.println(players[0].getLinearVelocity().lengthSquared() + players[0].getLinearVelocity().lengthSquared());
        if((players[0].getLinearVelocity().lengthSquared() + players[1].getLinearVelocity().lengthSquared()) < 0.1){
            timer--;
        }
        
        if (timer <= 0){
            draw();
            timer = timeBeforeDraw;
        }
        
        if (reset){ //creates a delay after a player wins to make game more satisfying
            
            resetCount--;
            
            if (resetCount <= 0){
            reset();
            }
            
        }
    }
    
    @Override
    public void checkIntersect(){ //checks if the players do not intersect with the ring every tick
        if (!reset){
            if (!players[0].intersects(dohyo)){ 
                roundOver(players[1], players[0]);
            } else if (!players[1].intersects(dohyo)){
                roundOver(players[0], players[1]);
            }
        }
    }
        
    @Override
    public void movementA(){
        int[] keys;
        players[0].getBrain().movement(0);
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
    
    @Override    
    public void movementB(){
        int[] keys;
        players[1].getBrain().movement(0);
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
    
    @Override
    public void roundOver(Rikishi victor, Rikishi loser){
        /*
        victor.addScore();
        gui.updateScore(); */
        victor.getBrain().getNNet().incScore(2);
        
        reset = true;
        
        timer = 1000;
        
        dohyo.roundOver(victor);
        /*
        if (loser.getBrain().getNNet() != null){
            loser.getBrain().getNNet().mutateNet();
        } */
        loser.die();
        addToLog("Player " + victor.getPlayerNum() + " wins!"); //score: " + players[0].getScore() + " - " + players[1].getScore());
    }
    
    public void draw(){
        reset = true;
        
        if (players[0].getPosition().lengthSquared() == players[1].getPosition().lengthSquared()){
            players[0].getBrain().getNNet().incScore(1);
            players[1].getBrain().getNNet().incScore(1);
            //players[1].die();
            addToLog("Draw!");
        }
        
        if (players[0].getPosition().lengthSquared() < players[1].getPosition().lengthSquared()){
            players[0].getBrain().getNNet().incScore(2);
            players[1].die();
            addToLog("Player 1 wins by proximity!");
        }
        
        if (players[0].getPosition().lengthSquared() > players[1].getPosition().lengthSquared()){
            players[1].getBrain().getNNet().incScore(2);
            players[0].die();
            addToLog("Player 2 wins by proximity!");
        }
    }
    
    @Override
    public void reset(){
        addToLog("\n");
        updateLog();
        world.placeBodies();
        world.getSimulation().runGen();
        players[0].getBrain().resetMoveKeys();
        players[1].getBrain().resetMoveKeys();
        
        reset = false;
        resetCount = 1; //number of ticks to delay next round
        
        timer = timeBeforeDraw;
    }
    
    @Override
    public boolean getSettingChange(){
        return settingChange;
    }
    
    @Override
    public void gui(){
        world.getGui().simulation();
    }
    
    @Override
    public void displaySettings(){
        System.out.println("display settings");
        settings.setVisible(true);
    }
    
    @Override
    public void closeFrames(){
        settings.setVisible(false);
        player1Display.setVisible(false);
        player2Display.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (null != e.getActionCommand()) switch (e.getActionCommand()) {
            case "Display Player 1 Values":
                player1Display.setVisible(true);
                break;
            case "Display Player 2 Values":
                player2Display.setVisible(true);
                break;
            case "Display Log":
                logDisplay.setVisible(true);
                break;
        }
    }
}

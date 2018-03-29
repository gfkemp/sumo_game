/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.UserView;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author gregclemp
 */
public class GUI extends JFrame implements ActionListener{
    
    final private GameWorld world;
    final private UserView view;
    private JPanel sumoGame;
    private JPanel gui;
    private JLabel score;
    private GridBagLayout gridBagLayout;
    private JButton[] buttons; 
    private JLabel level;
    private JLabel lives;
    
    GUI(GameWorld world, UserView view){
        super("SUMO game"); //the title
        setLayout(new FlowLayout());
        
        System.out.println("GUI constructed");
        
        this.world = world;
        this.view = view;
        setSize(500,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        
        sumoGame = new JPanel();
        sumoGame.add(view);
        //add(sumoGame);
        
        gui = new JPanel();
        buttons = new JButton[0];
        
        setResizable(false);
        
        score = new JLabel(""); // + world.getPlayers()[0].getScore() + " - " + world.getPlayers()[1].getScore());
        //add(score); //displays current score at top of window
        setFocusable(true);
        setVisible(true);
    }
    
    public void updateScore(){
        score.setText("" + world.getPlayers()[0].getScore() + " - " + world.getPlayers()[1].getScore());
        //frame.pack();
    }
    
    public JFrame getFrame(){
        return null;
    }
    
    public void menu(){
        gui.removeAll();
        
        level = new JLabel("Menu");
        gui.add(level);
        
        buttons = new JButton[2]; 
        
        buttons[0] = new JButton("Campaign");
        gui.add(buttons[0]);
        buttons[0].setActionCommand("Campaign");
        buttons[0].addActionListener(this);
        
        buttons[1] = new JButton("Simulation");
        gui.add(buttons[1]);
        buttons[1].setActionCommand("Simulation");
        buttons[1].addActionListener(this);
        
        gui.setSize(500,500);
        add(gui);
        add(sumoGame);
        setVisible(true);
    }
    
    public void simulation(){
        gui.removeAll();
       
        level = new JLabel("Simulation");
        gui.add(level);
        
        buttons = new JButton[2]; 
        
        buttons[0] = new JButton("Exit");
        gui.add(buttons[0]);
        buttons[0].setActionCommand("Exit");
        buttons[0].addActionListener(this);
        
        buttons[1] = new JButton("Settings");
        gui.add(buttons[1]);
        buttons[1].setActionCommand("Settings");
        buttons[1].addActionListener(this);
        
        gui.setSize(500,500);
        add(gui);
        add(sumoGame);
        setVisible(true);
    }
    
    public void level(String name){
        gui.removeAll();
        
        level = new JLabel(name);
        gui.add(level);
       
        buttons = new JButton[1]; 
        
        buttons[0] = new JButton("Exit");
        gui.add(buttons[0]);
        buttons[0].setActionCommand("Exit");
        buttons[0].addActionListener(this);
        
        lives = new JLabel("Lives: " + world.getMode().getLives());
        gui.add(lives);
        
        gui.setSize(500,500);
        add(gui);
        add(sumoGame);
        setVisible(true);
    }
    
    public void simulationSettings(){
        world.getMode().displaySettings();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (null != e.getActionCommand()) switch (e.getActionCommand()) {
            case "Campaign":
                world.getMode().newLevel();
                break;
            case "Simulation":
                world.getMode().newSimMode();
                break;
            case "Exit":
                world.getMode().closeFrames();
                world.getMode().returnToMenu();
                break;
            case "Settings":
                simulationSettings();
                break;
            default:
                break;
        }
    }
    
    public void clearGui(){
        //remove(sumoGame);
        //remove(gui);
        gui.removeAll();
    }
    
    public void changeLevelName(String name){
        level.setText(name);
    }
    
    public void updateLives(int noLives){
        lives.setText("Lives: " + noLives);
    }
}


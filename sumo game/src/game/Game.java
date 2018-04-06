package game;

import city.cs.engine.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Main class, initialises GameWorld
 */
public class Game {

    private GameWorld world;
    private UserView view;
    private GUI gui;

    public Game() {

        // make the world
        world = new GameWorld(this);

        // make a view
        
        world.getGui().addKeyListener(new Controller(world, world.getPlayers()));
        

        /*gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationByPlatform(true);
        // display the world in the window
        gui.add(view);
        // don't let the game window be resized
        gui.setResizable(false);*/
        
        //addListeners();
        //gui.setSize(1000,500);
        //gui.setVisible(true);
        // start!
        world.start();
    }

    /** Run the game. */
    public static void main(String[] args) {
        new Game();
    }
    
    public void addListeners(){
        world.getGui().addKeyListener(new Controller(world, world.getPlayers())); //creates controller to allow user control
        //world.addStepListener(new StepMover(gui, world.getPlayers(), world, world.getDohyo()));
    }
}

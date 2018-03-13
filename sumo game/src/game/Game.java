package game;

import city.cs.engine.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 */
public class Game {

    private GameWorld world;
    private UserView view;
    private GUI gui;

    public Game() {

        // make the world
        world = new GameWorld();

        // make a view
        view = new UserView(world, 500, 500);
        
        gui = new GUI(world, view);

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationByPlatform(true);
        // display the world in the window
        gui.add(view);
        // don't let the game window be resized
        gui.setResizable(false);
        
        gui.addKeyListener(new Controller(world.getPlayers())); //creates controller to allow user control
        
        world.addStepListener(new StepMover(gui, world.getPlayers(), world, world.getDohyo()));
        gui.setSize(500,550);
        gui.setVisible(true);
        // start!
        world.start();
    }

    /** Run the game. */
    public static void main(String[] args) {
        new Game();
    }
}

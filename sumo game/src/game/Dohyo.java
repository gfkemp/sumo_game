/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.*;
import java.awt.Color;
import static java.awt.Color.*;

/**
 * This is a dynamic body with a circular fixture with no collision that acts as the ring for the players
 * @author gregclemp
 */
public class Dohyo extends DynamicBody {
    
    private static final float radius = 7.5f;
    private GhostlyFixture ring;
    private static final Shape dohyoShape = new CircleShape(radius);
    private BodyImage image = new BodyImage("data/logo.png", 2*radius);
    private DynamicBody logo;
    
    public Dohyo(World world){
        super(world);
        ring = new GhostlyFixture(this, dohyoShape);
        
        this.removeAllCollisionListeners();
        this.setGravityScale(0f);
        
        setFillColor(new Color(200/255f, 200/255f, 200/255f, 0.5f));
        setLineColor(BLACK);
        
        displayLogo(world);
    }
    
    public void displayLogo(World world){
        logo = new DynamicBody(world);
        GhostlyFixture logoRing;
        logoRing = new GhostlyFixture(logo, dohyoShape);
        logo.addImage(image);
        logo.setGravityScale(0);
    }
    
    public void roundOver(Rikishi victor){ // the colour of the ring is updated to match the previous winner
        setFillColor(victor.getColor());
        setLineColor(victor.getDColor());
    }
    
    public void destroyFixture(){
        logo.destroy();
    }
}

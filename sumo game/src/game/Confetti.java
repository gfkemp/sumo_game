/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import city.cs.engine.*;
import java.awt.Color;
import static java.lang.Math.random;
import org.jbox2d.common.Vec2;

/**
 * this is the particles generated on death of a player
 * @author gregclemp
 */
public class Confetti extends DynamicBody {
    
    private static final float radius = 0.18f;
    private static final Shape ballShape = 
                new CircleShape(radius);
    private static float xSPEED = 0f; 
    private static float ySPEED = 0f; 
    
    public Confetti(World world, Color dcolor, Color lcolor, Vec2 velocity){
        super(world, ballShape);
        setFillColor(lcolor);
        setLineColor(dcolor);
        
        setGravityScale(0);
        
        xSPEED = (float) (7f*(random()-0.5)); //velocity of the particle is the velocity of the player on death + noise
        ySPEED = (float) (7f*(random()-0.5));
        setLinearVelocity(velocity.add(new Vec2(xSPEED, ySPEED)));
    }
}

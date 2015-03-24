package com.tastysandwich.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.tastysandwich.gameworld.GameWorld;

import java.util.Random;

/**
 * Created by Filip on 10.2.2015.
 */
public class Asteroid {

    private Vector2 position;
    public Vector2 velocity;
    private float rotation;
    private float rotationSpeed;
    private int type;

    private float radius;

    private float[] aVertices;

    private Polygon boundingPolygon;

    private Random r;

    private GameWorld world;

    private static int velocityx = -300;

    public boolean readyToRestart = false;

    public Asteroid(float x, float y, int velocityy, GameWorld world) {
        r = new Random();
        this.world = world;
        rotationSpeed = r.nextInt(100)-50;
        rotation = 0;
        position = new Vector2(x,y);
        velocity = new Vector2(velocityx * world.getGameSpeed(), velocityy);
        type = r.nextInt(5) + 1;
        radius = Gdx.graphics.getWidth()/ (r.nextInt(2)+8);
        switch (type){
            case 1: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 2: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3, radius / 8, -radius / 16 * 5, radius / 4, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4} ;
                break;
            case 3: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 16 * 5, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 4: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, radius / 4, radius / 8 * 3, 0, radius / 8 * 3, -radius / 4, radius / 32 * 13, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 5: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 6: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 4, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 16 * 5, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
        }
        boundingPolygon= new Polygon();
        boundingPolygon.setVertices(aVertices);
        boundingPolygon.setPosition(position.x+radius/2, position.y+radius/2);
    }
    public void restart(){
        position.x = (float) (Gdx.graphics.getWidth()*1.5);
        position.y = (float)r.nextInt(Gdx.graphics.getHeight());
        type = r.nextInt(5) + 1;
        rotationSpeed = r.nextInt(100)-50;
        rotation = 0;
        velocity.x = velocityx * world.getGameSpeed();
        velocity.y = r.nextInt(200)- 100;
        switch (type){
            case 1: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 2: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3, radius / 8, -radius / 16 * 5, radius / 4, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4} ;
                break;
            case 3: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 16 * 5, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 4: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, radius / 4, radius / 8 * 3, 0, radius / 8 * 3, -radius / 4, radius / 32 * 13, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 5: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 8, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 4, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
            case 6: aVertices = new float[]{-radius / 8 * 3, -radius / 4, -radius / 8, -radius / 8 * 3,  radius / 4, -radius / 8 * 3, radius / 8 * 3, -radius / 4, radius / 16 * 7, 0,
                    radius / 16 * 5, radius / 16 * 5, 0, radius / 16 * 7, -radius / 8 * 3, radius / 4, -radius / 16 * 7, 0} ;
                break;
        }
        boundingPolygon.setVertices(aVertices);
        boundingPolygon.setPosition(position.x+radius/2, position.y+radius/2);
    }

    public void update(float delta) {

        position.add(velocity.cpy().scl(delta));
        if (position.x+radius <= 0){
            readyToRestart = true;
        }
        rotation += rotationSpeed * delta;
        if(rotation > 359 || rotation < -359){
            rotation = 0;
        }
        boundingPolygon.setPosition(position.x+radius/2, position.y+radius/2);
        boundingPolygon.setRotation(rotation);
    }
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getRadius() {
        return radius;
    }

    public float getRotation() { return rotation; }

    public int getType(){
        return type;
    }

    public boolean collides(Ship ship) {
        if (Intersector.overlapConvexPolygons(ship.getBoundingPolygon(), this.boundingPolygon)) {
            return true;
        } else {
            return false;
        }
    }

    public Polygon getBoundingPolygon() {
        return boundingPolygon;
    }

    public float[] getPole() {
        return boundingPolygon.getTransformedVertices();
    }


}

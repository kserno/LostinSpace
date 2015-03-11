package com.tastysandwich.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.tastysandwich.gameworld.GameWorld;

import java.util.Random;

/**
 * Created by Filip on 13.2.2015.
 */
public class Energy {

    private Vector2 position;
    private Vector2 velocity;

    public float radius;

    private boolean isActive = false;

    private float[] aVertices;

    private Polygon boundingPolygon;

    private Random r;


    public Energy(float x, float y, float radius) {
        position = new Vector2(x,y);
        velocity = new Vector2(-100,0);
        this.radius = radius;
        boundingPolygon = new Polygon();
        aVertices = new float[]{
                        radius, 0,
                        radius / 2, radius / 2,
                        0, radius,
                        -radius / 2, radius / 2,
                        -radius, 0,
                        -radius / 2, -radius / 2,
                        0, -radius,
                        radius / 2, -radius / 2,
                    };
        boundingPolygon.setVertices(aVertices);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        boundingPolygon.setPosition(position.x+radius, position.y+radius);
    }
    public void restart(){
        r = new Random();
        position.x = (float) (Gdx.graphics.getWidth()*1.5);
        position.y = (float)r.nextInt(Gdx.graphics.getHeight());
        boundingPolygon.setPosition(position.x+radius/2, position.y+radius/2);
    }

    public boolean collides(Ship ship) {
        if (Intersector.overlapConvexPolygons(ship.getBoundingPolygon(), boundingPolygon)) {
            return true;
        } else {
            return false;
        }

    }


    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return radius*2;
    }

    public float getHeight() {
        return radius*2;
    }

    public float getRadius() {
        return radius;
    }

    public float[] getaVertices() {
        return boundingPolygon.getTransformedVertices();
    }

}

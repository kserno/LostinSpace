package com.tastysandwich.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tastysandwich.gameworld.GameWorld;

/**
 * Created by solit_000 on 6.2.2015.
 */
public class Ship {

    private Vector2 position;
    private Vector2 velocity;

    private float prepona;

    private float currentRotation, destinedRotation, rotatePerFrame;
    private static float rotationSpeed = 50;
    private float height,width;
    private int goToY,goToX;

    private static float offset;
    private static float speed;

    private float originX, originY;

    private int energy = 0;
    private boolean shield = true;

    private Polygon boundingPolygon;

    private boolean isAlive= true;

    private static float[] aVertices;

    private float sx,sy;

    private GameWorld world;


    public Ship(float x, float y, float width, float height, GameWorld world,float screenHeight){
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        sx = x;
        sy = y;
        this.height = height;
        this.width = width;
        this.world = world;
        offset = screenHeight / 72;
        speed = screenHeight / 5.14f;
        boundingPolygon = new Polygon();
        goToY = (int) ((int) position.y+height/2);
        goToX = (int) position.x;
        aVertices = new float[] {
                width / 2, 0,
                width / 4 , height / 4 ,
                -width / 4, height / 16 * 7,
                -width / 4, -height / 16 * 7,
                width / 4, -height / 4};
        boundingPolygon.setVertices(aVertices);


    }
    public void update(float delta){
        originX = (position.x+width/2);
        originY = (position.y+height/2);

        position.add(velocity.cpy().scl(delta));

        if(world.currentState != GameWorld.GameState.DYING) {
            if (originY > goToY - offset && originY < goToY + offset) {
                velocity.y = 0;
            } else if (originY > goToY) {
                velocity.y = (-speed + currentRotation * 2) * world.getGameSpeed();
            } else if (originY < goToY) {
                velocity.y = (speed + currentRotation * 2) * world.getGameSpeed();
            }
        }else {
            if (originY > goToY - offset && originY < goToY + offset) {
                velocity.y = 5;
            } else if (originY > goToY) {
                velocity.y = (speed / 2 + currentRotation * 2) * world.getGameSpeed();
            } else if (originY < goToY) {
                velocity.y = (-speed / 2 + currentRotation * 2) * world.getGameSpeed();
            }
        }
        if(world.currentState != GameWorld.GameState.DYING) {
            prepona = (float) Math.sqrt(((goToX - originX) * (goToX - originX)) + ((originY - goToY) * (originY - goToY)));
            destinedRotation = (float) Math.toDegrees(Math.asin((originY - goToY) / prepona)) * -1;
        }else {
            prepona = (float) Math.sqrt(((goToX - originX) * (goToX - originX)) + ((originY - goToY) * (originY - goToY)));
            destinedRotation = (float) Math.toDegrees(Math.asin((originY - goToY) / prepona));
        }
        rotationChange(delta);

        boundingPolygon.setPosition(originX, originY);
        boundingPolygon.setRotation(currentRotation);
    }

    private void rotationChange(float delta) {
        rotatePerFrame = rotationSpeed * world.getGameSpeed() * delta;
        if(!((Math.abs(currentRotation - destinedRotation)) < 3)) {
            if (destinedRotation+2 > currentRotation) {
                currentRotation += rotatePerFrame;
            } else if (destinedRotation-2 < currentRotation) {
                currentRotation -= rotatePerFrame;
            }
        }
    }

    public void onClick(int screenX, int screenY){
        goToY = screenY;
        if(screenX<(width*6)/3){
            goToX = ((int) (width*6))/3;
        }else {
            goToX = screenX;
        }
    }
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
            return height;
        }

    public float getRotation() {
        return currentRotation;
    }

    public void addEnergy() {
        if (energy < 10 ) energy++;
        if (energy==10 && !shield) {
            energy = 0;
            shield = true;
            Gdx.app.log("Ship","Shield: true");
        }

    }

    public void die() {
        isAlive = false;
        Gdx.app.log("Ship","Died");

    }

    public void collide() {
        if (shield) {
            shield = false;
        } else die();
    }

    public Polygon getBoundingPolygon() {
        return boundingPolygon;
    }

    public float[] getPole() {
        return boundingPolygon.getTransformedVertices();
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void restart() {
        isAlive = true;
        position.set(sx, sy);
        velocity.set(0,0);
        goToY = (int) ((int) position.y+this.height/2);
        goToX = (int) position.x;
        shield = true;
        energy = 0;
    }

    public int getCurrentEnergy() {
        return energy;
    }
}


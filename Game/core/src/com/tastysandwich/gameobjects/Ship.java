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

    private float currentRotation, destinedRotation;
    private float rotationSpeed, difRotation;
    private float height,width;
    private int goToY,goToX;

    private float originX, originY;

    private int energy = 0;
    private boolean shield = true;

    private Polygon boundingPolygon;

    private boolean isAlive= true;

    private float[] aVertices;

    private float sx,sy;

    private GameWorld world;

    public Ship(float x, float y, float width, float height, GameWorld world){
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        sx = x;
        sy = y;
        this.height = height;
        this.width = width;
        this.world = world;
        Gdx.app.log("screen", String.valueOf(Gdx.graphics.getWidth()));
        Gdx.app.log("width", String.valueOf(width));
        boundingPolygon = new Polygon();
        goToY = (int) ((int) position.y+this.height/2+100);
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

        /*if (originY > goToY-5 && originY < goToY+5){
            velocity.y = 0;
        }else if(originY > goToY){
            velocity.y = (-140+currentRotation*2)*world.getGameSpeed();
        }else if (originY < goToY){
            velocity.y = (140+currentRotation*2)*world.getGameSpeed();
        }*/

        if (originY > goToY-10 && originY < goToY+10){
            velocity.y = 0;
        }else if(originY > goToY){
            velocity.y = -140*world.getGameSpeed();
        }else if (originY < goToY){
            velocity.y = 140*world.getGameSpeed();
        }

        prepona = (float) Math.sqrt(((goToX - originX)*(goToX - originX)) + ((originY - goToY)*(originY - goToY)));
        destinedRotation = (float) Math.toDegrees(Math.asin((originY - goToY) / prepona))*-1;

        difRotation = currentRotation - destinedRotation;
        if (difRotation < 0) difRotation = -difRotation;
        rotationSpeed = difRotation/ (0.2f/delta);
        Gdx.app.log("rotationSpeed", String.valueOf(rotationSpeed));
        Gdx.app.log("difRotation", String.valueOf(difRotation));

        if (destinedRotation > currentRotation) {
           currentRotation += rotationSpeed;
        } else {
           currentRotation -= rotationSpeed;
        }


        /*if (destinedRotation>currentRotation+2){
            currentRotation+=rotationSpeed*delta*world.getGameSpeed();
        }else if (destinedRotation<currentRotation-2){
            currentRotation-=rotationSpeed*delta*world.getGameSpeed();
        }*/

        Gdx.app.log("destined rotation",String.valueOf(destinedRotation));
        Gdx.app.log("current rotation", String.valueOf(currentRotation));

        boundingPolygon.setPosition(originX, originY);
        boundingPolygon.setRotation(currentRotation);
    }
    public void onClick(int screenX, int screenY){
        goToY = screenY;
        if(screenX<(width*6)/3){
            goToX = ((int) (width*6))/3;
        }else {
            goToX = screenX;
        }
    }
    public void onRelease(){
        goToX = (int) world.width;
        goToY = (int) originY;
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
        goToY = (int) ((int) position.y+this.height/2+100);
        goToX = (int) position.x;
        shield = true;
        energy = 0;
    }

    public int getCurrentEnergy() {
        return energy;
    }
}


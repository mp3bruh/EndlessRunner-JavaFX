package com.example.spiel_laurinwassmann;

import javafx.scene.image.Image;

public class Human extends GameObject {

    private static double WALKING_SPEED = 5;
    private static final double ANIMATION_SPEED = WALKING_SPEED / 2;
    private Image[] images;
    private int currentImageIndex;
    private int animationCounter;


    public Human(double x, double y, double width, double height, Image images[]) {
        super(x, y, width, height, images[0]);
        this.images = images;
        this.currentImageIndex = 0;
        this.animationCounter = 0;
        images[0] = new Image("walking1.png");      // Bilder für Spieleranimation
        images[1] = new Image("walking2.png");
        images[2] = new Image("walking3.png");
        images[3] = new Image("walking4.png");
        images[4] = new Image("walking5.png");
        images[5] = new Image("walking6.png");
        images[6] = new Image("walking7.png");

    }


    @Override
    public void update() {
        // Wechsle das Bild der Animation alle ANIMATION_SPEED Frames
        if (animationCounter % ANIMATION_SPEED == 0) {
            currentImageIndex = (currentImageIndex + 1) % images.length;
            image = images[currentImageIndex];
        }
        animationCounter++;
    }

    void moveRight(){

        this.setX(this.getX() + WALKING_SPEED);
    }
    void moveLeft(){
        this.setX(this.getX() - WALKING_SPEED);
    }
    void moveUp(){
        this.setY(this.getY() - WALKING_SPEED);
    }
    void moveDown(){
        this.setY(this.getY() + WALKING_SPEED);
    }

    void setWalkingSpeed(int speed){  // Geschwindigkeit auf 0 setzten wenn keine Taste grdrückt wird
        WALKING_SPEED = speed;
    }

}
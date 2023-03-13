package com.example.spiel_laurinwassmann;
import javafx.scene.image.Image;
import java.util.Random;
public class Car extends GameObject {


    private static final int CAR_WIDTH = 150;
    private static final double CAR_HEIGHT = 150;
    private static final String[] CAR_IMAGES = {"car1.png", "car2.png", "car3.png", "car5.png", "car6.png", "car7.png", "car8.png", "car9.png", "car10.png"};
    private static final Random random = new Random(); // Random für verschiedene Bilder von Autos

    public Car(double x, double y) {
        super(x, y, CAR_WIDTH, CAR_HEIGHT, new Image(CAR_IMAGES[random.nextInt(CAR_IMAGES.length)]));
    }

    @Override
    public void update() {
        // Bewege das Auto nach unten
        y += 5;
    }

}
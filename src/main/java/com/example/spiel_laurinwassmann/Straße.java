package com.example.spiel_laurinwassmann;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Straße {

    private static final int STREET_WIDTH = 200; // Breite einer Straße
    private static final int STREET_HEIGHT = 600; // Höhe des Spielbereichs
    private static final int NUM_STREETS = 4; // Anzahl der Straßen
    private static final int STREET_SPACING = 0; // Abstand zwischen den Straßen
    private static final int STREET_SPEED = 5; // Geschwindigkeit der Straßen

    private Image streetImage; // Bild der Straße
    private double streetY; // Y-Position der Straßen
    private double streetOffset; // "Laufband-Effekt"

    public Straße(Image streetImage, double streetY) {
        this.streetImage = streetImage;
        this.streetY = streetY;
        this.streetOffset = 0;
    }

    public void update() {
        streetOffset += STREET_SPEED;
        if (streetOffset > STREET_HEIGHT) {
            streetOffset -= STREET_HEIGHT;
        }
    }

    public void draw(GraphicsContext gc) {
        // Zeichne die Straßen nebeneinander
        for (int i = 0; i < NUM_STREETS; i++) {
            double x = i * (STREET_WIDTH + STREET_SPACING);
            gc.drawImage(streetImage, x, streetY + streetOffset - STREET_HEIGHT, STREET_WIDTH, STREET_HEIGHT);
            gc.drawImage(streetImage, x, streetY + streetOffset, STREET_WIDTH, STREET_HEIGHT);
        }
    }

}

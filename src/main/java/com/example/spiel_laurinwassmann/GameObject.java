package com.example.spiel_laurinwassmann;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.BoundingBox;

public abstract class GameObject {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    private int hitbox;

    protected Image image;



    public GameObject(double x, double y, double width, double height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public BoundingBox getBounds() {        // Hitboxen
        return new BoundingBox(x, y, width-140, height-50);
    }

    public abstract void update();

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

}
    package com.example.spiel_laurinwassmann;
    import javafx.animation.AnimationTimer;
    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.geometry.Bounds;
    import javafx.scene.Scene;
    import javafx.scene.canvas.Canvas;
    import javafx.scene.canvas.GraphicsContext;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.StackPane;
    import javafx.scene.paint.Color;
    import javafx.stage.Stage;
    import javafx.scene.control.Alert;
    import java.util.ArrayList;
    import java.util.List;


    public class HelloApplication extends Application {

        private static final int CANVAS_WIDTH = 800;
        private static final int CANVAS_HEIGHT = 600;
        private static final double CAR_HEIGHT = 100;
        private boolean isRunning = true;
        private Straße street;
        private Image streetImage;
        private String[] humanImages;
        private Image humanImage;
        private KeyCode currentKey = null;
        private Human player;
        private List<Car> cars = new ArrayList<>();
        private long lastCarSpawnTime = 0;
        private int timeElapsed = 0;
        private boolean isMoving = false;
        private int playerSpeed = 0;
        private int timer = 0;
        private int spawnRate = 1;
        private int speedIncrease = 1;
        private int maxSpawnRate = 5;
        private int maxSpeed = 10;
        private long score = 0;
        private Label scoreLabel = new Label("Score:");



        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("DodgeMaster");
            streetImage = new Image("street.png");
            street = new Straße(streetImage, 0); // Straßen erstellen
            Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // Player erstellen
            Image[] images = new Image[7];
            images[0] = new Image("walking1.png");
            images[1] = new Image("walking2.png");
            images[2] = new Image("walking3.png");
            images[3] = new Image("walking4.png");
            images[4] = new Image("walking5.png");
            images[5] = new Image("walking6.png");
            images[6] = new Image("walking7.png");

            player = new Human(300, 450, 200, 100, images);


            StackPane root = new StackPane();


            // Setze die Hintergrundfarbe und Schriftfarbe des Labels
            scoreLabel.setStyle("-fx-background-color: black; -fx-text-fill: white;");


            // Setze die Position des Labels oben links (oder auch nicht?)
            scoreLabel.setLayoutX(10);
            scoreLabel.setLayoutY(10);


            root.getChildren().addAll(canvas,scoreLabel);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();



            // Pfeiltasten lesen
            canvas.requestFocus();
            root.setOnKeyPressed(e -> {
                currentKey = e.getCode();
                if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN ||
                        e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                    isMoving = true;
                }
            });

            canvas.setOnKeyReleased(e -> {
                // Setze isMoving auf false, wenn die Pfeiltaste losgelassen wird
                if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN ||
                        e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                    isMoving = false;
                }
            });


            // Starte einen AnimationTimer, der die update- und draw-Methoden des Spiels in regelmäßigen Abständen aufruft
            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (lastUpdate == 0 || now - lastUpdate >= 16_666_666) { // Führe das Update alle 16,6 ms aus (~60fps)
                        update();
                        draw(gc);
                        lastUpdate = now;
                    }
                }
            };
            timer.start();
        }

        private boolean checkCollision(Human player, Car car) {
            Bounds playerBounds = player.getBounds();
            Bounds carBounds = car.getBounds();
            return playerBounds.intersects(carBounds);
        }

        private void update() {
            if(isRunning) {

                if(!isMoving){
                    playerSpeed = 0;
                    currentKey = null;
                }
                // Bewege den Spieler je nach aktuell gedrückter Pfeiltaste
                if (currentKey == KeyCode.UP) {
                    player.moveUp();
                    if (player.getY() < 0) {
                        player.setY(0);
                    }
                    isMoving = true;
                    playerSpeed = 5;
                } else if (currentKey == KeyCode.DOWN) {
                    player.moveDown();
                    if (player.getY() + player.getHeight() > CANVAS_HEIGHT) {
                        player.setY(CANVAS_HEIGHT - player.getHeight());
                    }
                    isMoving = true;
                    playerSpeed = 5;
                } else if (currentKey == KeyCode.LEFT) {
                    player.moveLeft();
                    if (player.getX() < 0) {
                        player.setX(0);
                    }
                    isMoving = true;
                    playerSpeed = 5;
                } else if (currentKey == KeyCode.RIGHT) {
                    player.moveRight();
                    if (player.getX() + player.getWidth() > CANVAS_WIDTH) {
                        player.setX(CANVAS_WIDTH - player.getWidth());
                    }
                    isMoving = true;
                    playerSpeed = 5;
                }else{
                    isMoving = false;
                }

                player.setWalkingSpeed(playerSpeed);
                timer++;
                timeElapsed++;
                street.update();
                player.update();

                // 3 Sekunden Spawnschutz
                if (timeElapsed <= 180) {
                    return;
                }

                // Erzeuge ein neues Auto
                if (System.currentTimeMillis() - lastCarSpawnTime > 1000 / spawnRate) {
                    Car car = new Car(Math.random() * CANVAS_WIDTH, -400);
                    spawnCar();
                    lastCarSpawnTime = System.currentTimeMillis();

                    // Erhöhe die Spawngeschwindigkeit, wenn der Spieler länger überlebt
                    if (timer % 30 == 0 && spawnRate < maxSpawnRate) {
                        spawnRate += 1;
                    }

                    // Erhöhe die Geschwindigkeit der Autos, wenn der Spieler länger überlebt
                    if (timer % 60 == 0 && maxSpeed < maxSpeed) {
                        maxSpeed += speedIncrease;
                    }

                    score++;
                    updateScoreLabel();
                }

                // Auto entfernen wenn es aus dem Canvas fährt
                cars.removeIf(car -> car.getY() > CANVAS_HEIGHT);

                // Kollisionsprüfung
                for (Car car : cars) {
                    if (checkCollision(player, car)) {
                        gameOver();
                        return;
                    }
                }

                // Aktualisiere alle Autos
                for (Car car : cars) {
                    car.update();
                }

            }
        }

        public void updateScoreLabel() {
            scoreLabel.setText("Score: " + score);
        }

        private void spawnCar() {
            double carX = Math.random() * CANVAS_WIDTH;
            double carY = -400;

            // Überprüfen, ob der Spawn-Punkt bereits belegt ist
            for (Car car : cars) {
                if (carX >= car.getX() - 150 && carX <= car.getX() + 150 &&
                        carY >= car.getY() - 150 && carY <= car.getY() + 150) {
                    return; // Wenn ja, nichts tun und die Funktion beenden
                }
            }

            Car car = new Car(carX, carY);
            cars.add(car);
        }


        private void draw(GraphicsContext gc) {
            // Zeichne den Hintergrund
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);



            street.draw(gc); // Zeichne die Straßen

            for (Car car : cars) {
                car.draw(gc);
            }
            player.draw(gc);


        }

        private void gameOver() {
            // Spiel stoppen
            isRunning = false;

            // Game-Over Alert
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Game Over");
                alert.setContentText("Your final score is: " + score);
                alert.showAndWait();
            });
        }

        public static void main(String[] args) {
            launch(args);
        }

    }

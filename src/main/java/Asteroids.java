import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Asteroids extends Application {
    private int killCounter = 0;
    private Text killDisplay = new Text("Enemies Killed: " + killCounter);
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private Pane root;
    private List<GameObject> bullets = new ArrayList<>();
    private List<GameObject> enemies = new ArrayList<>();

    private GameObject player;

    private Parent createContent(){
        root = new Pane();
        root.setPrefSize(600, 600);
        player = new Player();
        player.setVelocity(new Point2D(1,0));
        addGameObject(player, 300, 300);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();

        killCounter();

        return root;
    }

    private void killCounter(){
        killDisplay.setFont(Font.font(25));
        killDisplay.setY(20);
        root.getChildren().add(killDisplay);
    }

    private void addBullet(GameObject bullet, double x, double y){
        bullets.add(bullet);
        addGameObject(bullet, x, y);
    }

    private void addEnemy(GameObject enemy, double x, double y){
        enemies.add(enemy);
        addGameObject(enemy, x, y);
    }

    private void addGameObject(GameObject object, double x, double y){
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());

    }

    private void onUpdate(){
        if(isLeftPressed){
            player.rotateLeft();
        }else if(isRightPressed){
            player.rotateRight();
        }

        killDisplay.setText("Enemies Killed: " + killCounter);

        for(GameObject bullet : bullets){
            for(GameObject enemy : enemies){
                if(bullet.isColliding(enemy)){
                    killCounter++;
                    bullet.setAlive(false);
                    enemy.setAlive(false);
                    root.getChildren().removeAll(bullet.getView(), enemy.getView());
                }
            }
        }
        for(GameObject enemy : enemies){
        if(player.isColliding(enemy)) {
            player.setAlive(false);
            root.getChildren().remove(player.getView());
            }
        }
        bullets.removeIf(GameObject::isDead);
        enemies.removeIf(GameObject::isDead);

        bullets.forEach(GameObject::update);
        enemies.forEach(GameObject::update);

        player.update();

        if(Math.random() < .02 && !(player.isDead())){
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }

    private static class Player extends GameObject {
        Player(){
            super(new Rectangle(40, 20, Color.BLUE));
        }
    }
    private static class Enemy extends GameObject {
        Enemy(){
            super(new Circle(15, 15, 15, Color.RED));
        }
    }
    private static class Bullet extends GameObject {
            Bullet(){
                super(new Circle(5,5,5,Color.BROWN));
            }
        }


    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(createContent()));
        stage.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT && !(player.isDead()))
                isLeftPressed = true;
            if(e.getCode() == KeyCode.RIGHT && !(player.isDead()))
                isRightPressed = true;
            if(e.getCode() == KeyCode.SPACE && !(player.isDead())){
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(5));
                addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
            }
        });

        stage.getScene().setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.LEFT)
                isLeftPressed = false;
            if(e.getCode() == KeyCode.RIGHT)
                isRightPressed = false;
        });
        stage.show();
    }

    public static void main(String[] args){
        System.setProperty("quantum.multithreaded", "false");
        Application.launch(Asteroids.class, args);
    }
}

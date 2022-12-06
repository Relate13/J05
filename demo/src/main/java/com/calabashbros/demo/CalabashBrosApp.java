package com.calabashbros.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CalabashBrosApp extends Application {
    private double t;
    private Pane root=new Pane();
    private Player player=new Player(300,750,40,40,"player",Color.BLUE);
    private Parent createContent(){
        root.setPrefSize(600,800);
        Instantiate(player);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                CollisionDetect();
                ClearDead();
            }
        };
        timer.start();
        nextLevel();
        return root;
    }
    private void nextLevel(){
        for(int i=0;i<5;++i){
            Enemy enemy=new Enemy(90+i*100,-10,Color.RED);
            Instantiate(enemy);
        }
    }
    private List<Sprite> sprites(){
        return root.getChildren().stream().map(n->(Sprite)n).collect(Collectors.toList());
    }
    private void CollisionDetect(){
        sprites().forEach(s->{
            sprites().forEach(b->{
                if(s!=b){
                    if(s.getBoundsInParent().intersects(b.getBoundsInParent()))
                        s.OnCollision(b);
                }
            });
        });
    }
    private void ClearDead(){
        sprites().forEach(s->{
            if(s.dead)
                Destroy(s);
        });
    }
    private void Instantiate(Sprite s){
        root.getChildren().add(s);
        s.Activate();
    }
    private void Destroy(Sprite s){
        s.dead=true;
        s.DeActivate();
        s.OnDestroy();
        root.getChildren().remove(s);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene=new Scene(createContent());
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case UP ->{player.moveUp(Data.PLAYER_SPEED);}
                case DOWN -> {player.moveDown(Data.PLAYER_SPEED);}
                case LEFT -> {player.moveLeft(Data.PLAYER_SPEED);}
                case RIGHT -> {player.moveRight(Data.PLAYER_SPEED);}
                case SPACE -> {Instantiate(new Bullet((int) player.getTranslateX(), (int) player.getTranslateY(),Color.BLACK));}
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
class Player extends Sprite{

    Player(int x, int y, int w, int h, String type, Color color) {
        super(x, y, w, h, type, color);
    }

    @Override
    public void Update() {
        System.out.println("player is alive");
    }
    @Override
    public void OnCollision(Sprite Other){
        if(Other.type=="enemy")
            Die();
    }
}
class Enemy extends Sprite{

    Enemy(int x, int y, Color color) {
        super(x, y, 40, 40, "enemy", color);
    }
    @Override
    public void Update(){
        if(Math.random()<0.5){
            moveDown(1);
        }
    }
    @Override
    public void OnCollision(Sprite Other){
        if(Other.type=="bullet")
            Die();
    }
}
class Bullet extends Sprite{

    Bullet(int x, int y, Color color) {
        super(x, y, 5, 5, "bullet", color);
    }
    @Override
    public void Update(){
        moveUp(5);
        if(getTranslateY()<0)
            Die();
    }
    @Override
    public void OnCollision(Sprite Other){
        if(Other.type=="enemy")
            Die();
    }
}


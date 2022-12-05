package com.calabashbros.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.net.ssl.SSLProtocolException;
import java.util.List;
import java.util.stream.Collectors;

public class CalabashBrosApp extends Application {
    private double t;
    private Pane root=new Pane();
    private Sprite player=new Sprite(300,750,40,40,"player",Color.BLUE);
    private Parent createContent(){
        root.setPrefSize(600,800);
        root.getChildren().add(player);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        timer.start();
        nextLevel();
        return root;
    }
    private void nextLevel(){
        for(int i=0;i<5;++i){
            Sprite enemy=new Sprite(90+i*100,150,30,30,"enemy",Color.RED);
            root.getChildren().add(enemy);
        }
    }
    private List<Sprite> sprites(){
        return root.getChildren().stream().map(n->(Sprite)n).collect(Collectors.toList());
    }
    private void update(){
        t+=0.08;
        sprites().forEach(s->{
            switch (s.type){
                case "enemy-bullet"->{
                    s.moveDown(Data.BULLET_SPEED);
                    if(!player.dead && s.getBoundsInParent().intersects(player.getBoundsInParent())){
                        player.dead=true;
                        s.dead=true;
                    }
                }
                case "player-bullet"->{
                    s.moveUp(Data.BULLET_SPEED);
                    sprites().stream().filter(e->e.type.equals("enemy")).forEach(enemy->{
                        if(s.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                            enemy.dead=true;
                            s.dead=true;
                        }
                    });
                }
                case "enemy"->{
                    if (t > 2) {
                        if(Math.random()<0.05){
                            shoot(s);
                        }
                    }
                }
            }
        });

        root.getChildren().removeIf(n->{
            Sprite s = (Sprite) n;
            if(s.dead){
                System.out.println("removed "+s.type);
            }
            return s.dead;
        });
        if(t>2){
            t=0;
        }
    }
    private void shoot(Sprite shooter){
        Sprite bullet=new Sprite((int) (shooter.getTranslateX()+20),
                (int) shooter.getTranslateY(),
                5,20,shooter.type+"-bullet",Color.BLACK);
        root.getChildren().add(bullet);
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
                case SPACE -> {shoot(player);}
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private static class Sprite extends Rectangle{
        boolean dead=false;
        final String type;
        Sprite(int x,int y,int w,int h,String type, Color color){
            super(w,h,color);
            this.type=type;
            setTranslateX(x);
            setTranslateY(y);
        }
        void moveLeft(float speed){
            setTranslateX(getTranslateX() - speed);
        }
        void moveRight(float speed){
            setTranslateX(getTranslateX() + speed);
        }
        void moveUp(float speed){
            setTranslateY(getTranslateY() - speed);
        }
        void moveDown(float speed){
            setTranslateY(getTranslateY() + speed);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

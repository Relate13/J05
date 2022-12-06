package com.calabashbros.demo;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle implements Runnable{
    public boolean ThreadAlive=true;
    boolean dead=false;
    final String type;
    Sprite(int x,int y,int w,int h,String type, Color color){
        super(w,h,color);
        this.type=type;
        setTranslateX(x);
        setTranslateY(y);
    }
    void moveLeft(float speed){
        Platform.runLater(()->{setTranslateX(getTranslateX() - speed);});
    }
    void moveRight(float speed){
        Platform.runLater(()->{setTranslateX(getTranslateX() + speed);});
    }
    void moveUp(float speed){
        Platform.runLater(()->{setTranslateY(getTranslateY() - speed);});
    }
    void moveDown(float speed){
        Platform.runLater(()->{setTranslateY(getTranslateY() + speed);});
    }
    public void Start(){}
    public void Update(){}
    public void OnDestroy(){}
    public void OnCollision(Sprite Other){}
    private Thread t;
    public void Activate(){
        t=new Thread(this);
        t.start();
    }
    public void DeActivate(){
        ThreadAlive =false;
        OnDestroy();
    }
    public void Die(){
        System.out.println("Died");
        dead=true;
    }

    @Override
    public void run() {
        Start();
        while (ThreadAlive){
            try {
                Thread.sleep(Data.FRAME_LENGTH);
                Update();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
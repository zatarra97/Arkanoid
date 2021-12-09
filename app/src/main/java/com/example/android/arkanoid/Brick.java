package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Brick extends View {

    private Bitmap brick;
    private float x;
    private float y;
    private String power;
    private boolean isDestroyed = false;

    public Brick(Context context, float x, float y) {
        super(context);
        this.x = x;
        this.y = y;
        this.power = "";
        skin();
    }
    public Brick(Context context, float x, float y, String power) {
        super(context);
        this.x = x;
        this.y = y;
        this.power = power;
        skinSpecial(power);
    }

    // priradi random obrazok tehlicke
    //assegna una immagine casuale al mattone
    private void skin() {
        int a = (int) (Math.random() * 8);
        switch (a) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_green);
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange);
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink);
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple);
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red);
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow);
                break;
        }
    }

    private void skinSpecial(String power) {
        switch (power) {
            case "bomb":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_bomb);
                break;
            case "lifeup":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_lifeup);
                break;
            case "speedup":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_speedup);
                break;
        }

    }

    @Override
    public float getX() {
        return x;
    }

    public boolean isBomb() {
        return power.equals("bomb");
    }

    public boolean isLifeUp() { return power.equals("lifeup"); }

    public boolean isSpeedUp() { return power.equals("speedup"); }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBrick() {
        return brick;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public void distruggi() {
        setDestroyed(true);
        brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_transparent);
    }
}

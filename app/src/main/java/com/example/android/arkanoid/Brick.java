package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;

public class Brick extends View {

    private Bitmap brick;
    private float x;
    private float y;
    private String skin;
    private boolean isDestroyed = false;
    private ArrayList<String> powers = new ArrayList<String>(Arrays.asList("bomb", "lifeup", "speedup"));

    public Brick(Context context, float x, float y) {
        super(context);
        this.x = x;
        this.y = y;
        this.skin = "";
        skin();
    }
    public Brick(Context context, float x, float y, String skin) {
        super(context);
        this.x = x;
        this.y = y;
        this.skin = skin;
        if (powers.contains(skin)) {
            skinSpecial(skin);
        } else {
            skinColor(skin);
        }
    }

    // priradi random obrazok tehlicke
    //assegna una immagine casuale al mattone
    private void skin() {
        int a = (int) (Math.random() * 8);
        switch (a) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua);
                skin = "aqua";
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                skin = "blue";
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_green);
                skin = "green";
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange);
                skin = "orange";
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink);
                skin = "pink";
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple);
                skin = "purple";
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red);
                skin = "red";
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow);
                skin = "yellow";
                break;
        }
    }

    private void skinColor(String _) {
        switch (_) {
            case "aqua":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua);
                skin = "aqua";
                break;
            case "blue":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                skin = "blue";
                break;
            case "green":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_green);
                skin = "green";
                break;
            case "orange":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange);
                skin = "orange";
                break;
            case "pink":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink);
                skin = "pink";
                break;
            case "purple":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple);
                skin = "purple";
                break;
            case "red":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red);
                skin = "red";
                break;
            case "yellow":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow);
                skin = "yellow";
                break;
            case "black":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_black);
                skin = "black";
                break;
            case "trasparent":
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_transparent);
                skin = "trasparent";
                isDestroyed = true;
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
        return skin.equals("bomb");
    }

    public boolean isLifeUp() { return skin.equals("lifeup"); }

    public boolean isSpeedUp() { return skin.equals("speedup"); }

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
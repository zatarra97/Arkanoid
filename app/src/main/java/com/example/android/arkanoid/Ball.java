package com.example.android.arkanoid;

public class Ball {

    protected float xSpeed;
    protected float ySpeed;
    private float x;
    private float y;

    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        createSpeed();
    }

    // vytvorí random rýchlosť lopticky
    //crea una palla a velocità casuale
    protected void createSpeed() {
        int maxX = 13;
        int minX = 7;
        int maxY = -17;
        int minY = -23;
        int rangeX = maxX - minX + 1;
        int rangeY = maxY - minY + 1;

        xSpeed = (int) (Math.random() * rangeX) + minX;
        ySpeed = (int) (Math.random() * rangeY) + minY;
    }

    // zmeni smer podla rychlosti
    //cambia direzione in base alla velocità
    //zmen Smer = modificare direzione
    protected void changeDirection() {
        if (xSpeed > 0 && ySpeed < 0) {
            invertiXspeed();
        } else if (xSpeed < 0 && ySpeed < 0) {
            invertiYspeed();
        } else if (xSpeed < 0 && ySpeed > 0) {
            invertiXspeed();
        } else if (xSpeed > 0 && ySpeed > 0) {
            invertiYspeed();
        }
    }

    // zvyši rychlost na zaklade levelu
    //aumenta la velocità in base al livello
    //zvys Rychlost = aumentare velocità
    protected void increaseSpeed(int level) {
        xSpeed = xSpeed + level;
        ySpeed = ySpeed - level;
    }

    // zmeni smer podla toho akej steny sa dotkla a rychlosti
    //cambia direzione a seconda del muro toccato e della velocità
    //wall = parete
    protected void changeDirection(String wall) {
        if (xSpeed > 0 && ySpeed < 0 && wall.equals("prava")) {
            invertiXspeed();
        } else if (xSpeed > 0 && ySpeed < 0 && wall.equals("hore")) {
            invertiYspeed();
        } else if (xSpeed < 0 && ySpeed < 0 && wall.equals("hore")) {
            invertiYspeed();
        } else if (xSpeed < 0 && ySpeed < 0 && wall.equals("lava")) {
            invertiXspeed();
        } else if (xSpeed < 0 && ySpeed > 0 && wall.equals("lava")) {
            invertiXspeed();
        } else if (xSpeed > 0 && ySpeed > 0 && wall.equals("dole")) {
            invertiYspeed();
        } else if (xSpeed > 0 && ySpeed > 0 && wall.equals("prava")) {
            invertiXspeed();
        }
    }

    // zisti ci je lopticka blizko
    //scopre se la palla è vicina
    //je Blizko = è vicino
    private boolean isVicino(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        if ((Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow(ay - by, 2))) < 80) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 100) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 150) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        }
        return false;
    }

    // zisti či je lopticka blizko tehly
    //scopre se la palla è vicina a un mattone
    //je Blizko Brick = è vicino ad un mattone
    private boolean isVicinoMattoncino(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        double d = Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow((ay + 40) - by, 2));
        return d < 80;
    }

    // ak sa zrazila lopta s padlom tak zmeni smer
    //se la palla si scontra mentre cade con la pagaia, cambierà direzione
    protected void barraColpita(float xPaddle, float yPaddle) {
        if (isVicino(xPaddle, yPaddle, getX(), getY())) changeDirection();
    }

    // ak sa zrazila lopta s tehlou tak zmeni smer
    //se la palla si scontra contro un mattone cambia direzione
    protected boolean isMattoncinoColpito(float xBrick, float yBrick) {
        if (isVicinoMattoncino(xBrick, yBrick, getX(), getY())) {
            changeDirection();
            return true;
        } else return false;
    }

    // pohne sa o zadanu rychlost
    //si muove a velocità specificata
    protected void velocizza() {
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public void invertiXspeed() {
        xSpeed = -xSpeed;
    }

    public void invertiYspeed() {
        ySpeed = -ySpeed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }
}

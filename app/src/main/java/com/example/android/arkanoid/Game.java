package com.example.android.arkanoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Game extends View implements SensorEventListener, View.OnTouchListener {


    private SoundManager sm; //Gestione Suoni mattoncini
    private Bitmap sfondo;
    private Bitmap redBall;
    private Bitmap allungato;
    private Bitmap paddle_p;
    private Display display;
    private Point size;
    private Paint paint;
    private Ball palla;
    private ArrayList<Brick> mattoncini;
    private Paddle paddle;
    private RectF r;
    private SensorManager sManager;
    private Sensor accelerometer;
    private int lifes;
    private int score;
    private int level;
    private String gameTime = (String)String.valueOf(R.string.gametime);
    private String gameLevel = String.valueOf(R.string.gamelevel);
    private String gameScore = String.valueOf(R.string.score);
    private String timeAndDate= String.valueOf(R.string.timeanddate);
    private boolean start;
    private boolean playing;
    private long startTime;
    private long endTime;
    private boolean gameOver;
    private boolean newRecord;
    private Context context;
    private int controller;

    public Game(Context context, int lifes, int score, String custom_level, int controller) {
        super(context);
        paint = new Paint();

        // Imposta context, vite, punteggi, livelli e controller
        this.context = context;
        this.lifes = lifes;
        this.score = score;
        this.controller = controller;
        level = 1;
        sm.init(context); //SoundManager

        // start a gameOver na zistenie ci hra stoji a ci je hráč neprehral
        //avvia un gameover per scoprire se il gioco è in piedi e se il giocatore non l'ha perso
        start = false;
        gameOver = false;
        newRecord = false;
        playing = false;

        // vytvorí akcelerometer a SensorManager
        //crea un accelerometro e un SensorManager
        if(controller == 1){
            sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        leggiSfondo(context);

        // vytvori bitmap pre lopticku a pádlo
        //crea una bitmap per la palla e la pagaia
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
        paddle_p = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        // vytvorí novú lopticku, pádlo, a zoznam tehliciek
        //crea una nuova palla, pagaia e un elenco di mattoncini
        palla = new Ball(size.x / 2, size.y - 480);
        paddle = new Paddle(size.x / 2 - 100, size.y - 400);
        mattoncini = new ArrayList<Brick>();

        if (custom_level != null && !custom_level.isEmpty()) {
            generaMattonciniCustom(context, custom_level);
        } else {
            generaMattoncini(context);
        }
        this.setOnTouchListener(this);

    }

    // naplni zoznam tehlickami
    //riempi la lista di mattoncini
    //vygeneruj Bricks = generare mattoncini
    private void generaMattonciniCustom(Context context, String custom_level) {
        String[] custom_bricks = custom_level.split(",");
        int a = 0;
        for (int i = 3; i < 8; i++) {
            for (int j = 1; j < 6; j++) {
                String b = custom_bricks[a];
                mattoncini.add(new Brick(context, j * 150, i * 100, b));
                a++;
            }
        }
    }

    private void generaMattoncini(Context context) {
        boolean bomb = false;
        boolean lifeup = false;
        boolean speedup = false;

        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {
                if (!bomb){
                    if ((int)(Math.random()*10) == 5){
                        mattoncini.add(new Brick(context, j * 150, i * 100,"bomb"));
                        bomb = true;
                        continue;
                    }
                }
                if (!lifeup) {
                    if ((int)(Math.random()*10) == 5){
                        mattoncini.add(new Brick(context, j * 150, i * 100,"lifeup"));
                        lifeup = true;
                        continue;
                    }
                }
                if (!speedup) {
                    if ((int)(Math.random()*10) == 5){
                        mattoncini.add(new Brick(context, j * 150, i * 100,"speedup"));
                        speedup = true;
                        continue;
                    }
                }

                mattoncini.add(new Brick(context, j * 150, i * 100));
            }
        }
    }

    // nastavi pozadie
    //imposta lo sfondo
    //nacitaj Pozadie = leggi lo sfondo
    private void leggiSfondo(Context context) {
        sfondo = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    protected void onDraw(Canvas canvas) {
        // vytvori pozadie iba raz
        //crea lo sfondo solo una volta
        if (allungato == null) {
            allungato = Bitmap.createScaledBitmap(sfondo, size.x, size.y, false);
        }
        canvas.drawBitmap(allungato, 0, 0, paint);

        // vykresli lopticku
        //disegna una palla
        paint.setColor(Color.RED);
        canvas.drawBitmap(redBall, palla.getX(), palla.getY(), paint);

        // vykresli padlo
        //disegnato caduto
        paint.setColor(Color.WHITE);
        r = new RectF(paddle.getX(), paddle.getY(), paddle.getX() + 200, paddle.getY() + 40);
        canvas.drawBitmap(paddle_p, null, r, paint);

        // vykresli tehlicky
        //disegnare mattoni
        paint.setColor(Color.GREEN);
        for (int i = 0; i < mattoncini.size(); i++) {
            Brick b = mattoncini.get(i);
            r = new RectF(b.getX(), b.getY(), b.getX() + 120, b.getY() + 80);
            canvas.drawBitmap(b.getBrick(), null, r, paint);
        }

        // Mostra testo vite, punteggio, livello
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + lifes, 240, 120, paint);
        canvas.drawText("" + score, 520, 120, paint);
        canvas.drawText("" + level, 820, 120, paint);

        paint.setColor(Color.RED);
        canvas.drawText("Vite" , 200, 50, paint);
        canvas.drawText("Punti", 500, 50, paint);
        canvas.drawText("Lvl", 800, 50, paint);

        if (controller == 0) {
            Bitmap dxMap =  BitmapFactory.decodeResource(getResources(), R.drawable.arrow_dx);
            r = new RectF(size.x-300f,  size.y-150f, size.x-150f,size.y-300f);
            canvas.drawBitmap(dxMap, null, r, paint);

            Bitmap sxMap =  BitmapFactory.decodeResource(getResources(), R.drawable.arrow_sx);
            r = new RectF(150f,  size.y-150f, 300f,size.y-300f);
            canvas.drawBitmap(sxMap, null, r, paint);
        }

        // In caso di perdita appare la scritta "Game over!"
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", size.x / 4, size.y / 2, paint);

            if(newRecord){
                paint.setTextSize(70);
                canvas.drawText("Nuovo record: " + score, size.x / 4, size.y / 2 + 100, paint);
            }
        }
    }

    // controlla che la palla non abbia toccato il bordo
    //lopticka = sfera
    private void controllaBordi() {
        if (palla.getX() + palla.getxSpeed() >= size.x - 60) {
            palla.changeDirection("prava");
        } else if (palla.getX() + palla.getxSpeed() <= 0) {
            palla.changeDirection("lava");
        } else if (palla.getY() + palla.getySpeed() <= 150) {
            palla.changeDirection("hore");
        } else if (palla.getY() + palla.getySpeed() >= size.y - 200) {
            controllaVite();
        }
    }

    // controlla lo stato del gioco. se le mie vite sono finite o se il gioco è finito
    private void controllaVite() {
        if (lifes == 1) {
            gameOver = true;
            playing = false;
            start = false;
            checkScore(score);
            invalidate();
            endTime = System.nanoTime();
            salvaPartitaInFile();
        } else {
            lifes--;
            palla.setX(size.x / 2);
            palla.setY(size.y - 480);
            palla.createSpeed();
            palla.increaseSpeed(level);
            start = false;
        }
    }

    public void salvaPartitaInFile() {
        // Elapsed Play Time
        double elapsedTime = (Double.valueOf(String.valueOf(endTime - startTime)) * 0.000000001);
        DecimalFormat df = new DecimalFormat("#####.##");
        String formattedElapsedTime = df.format(elapsedTime);
        Log.i("ElapsedTime", formattedElapsedTime);

        // Date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm,dd/MM/yyyy");
        String currentTimeDate = sdf.format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        //Info che verranno scritte sul file di testo per la memorizzazione storricità delle partite
        stringBuilder.append("Time game: " + formattedElapsedTime).append(",");
        stringBuilder.append("Level: " + level).append(",").append("Game score: " + score).append(",").append("Time and Date: " + currentTimeDate).append(";\n");
        String textToWrite = stringBuilder.toString();

        // Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            //If it isn't mounted - we can't write into it.
            return;
        }

        // Create a new file that points to the root directory, with the given name:
        File file = new File(context.getExternalFilesDir(null), "partite.txt");

        // This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);
            outputStream.write(textToWrite.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // kazdy krok kontroluje ci nedoslo ku kolizii, k prehre alebo k vyhre atd
    //ogni passaggio controlla se c'è una collisione, una perdita o una vittoria, ecc
    public void update() {
        //carico l'impostazione della preferenza del suono in gioco
        SharedPreferences sharedprefsound = context.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);
        boolean flagSounds = sharedprefsound.getBoolean("switch_sounds",false); //flag di controllo del suono

        if (start) {
            vincita();
            controllaBordi();
            palla.barraColpita(paddle.getX(), paddle.getY());
            for (int i = 0; i < mattoncini.size(); i++) {
                if (mattoncini.get(i).isDestroyed()) continue;
                Brick b = mattoncini.get(i);
                if (palla.isMattoncinoColpito(b.getX(), b.getY())) {
                    if(b.isBomb()){
                        //logica da implementare se la palla è nera tipo scoppiano anche i mattonci vicino
                        distruggiBomba(i);
                        if(flagSounds) {
                            //riproduzione suono mattoncino bomba
                            sm.playBomb();
                        }
                    } else if (b.isLifeUp()) {
                        this.lifes += 1;
                        if(flagSounds) {
                            //riproduzione suono powerUp
                            sm.playPup();
                        }
                    } else if (b.isSpeedUp()) {
                        palla.increaseSpeed(level+5);
                        if(flagSounds) {
                            //riproduzione suono powerUp
                            sm.playPup();
                        }
                    }else{
                        if(flagSounds) {
                            //riproduzione suono mattoncino generico
                            sm.playHit();
                        }
                    }
                    b.distruggi();
                    score = score + 80;
                }
            }
            palla.velocizza();
        }
    }

    private void distruggiBomba(int mattoncinoBomba) {
        if (mattoncinoBomba - 1 >= 0 && mattoncinoBomba % 5 != 0) ditruggiMattoncino(mattoncinoBomba - 1);              // distrugge a sinistra
        if (mattoncinoBomba + 1 < mattoncini.size() && mattoncinoBomba % 4 != 0) ditruggiMattoncino(mattoncinoBomba + 1);   // distrugge a destra
        if (mattoncinoBomba - 5 >= 0) ditruggiMattoncino(mattoncinoBomba - 5);              // distrugge sopra
        if (mattoncinoBomba + 5 < mattoncini.size()) ditruggiMattoncino(mattoncinoBomba + 5);   // distrugge sotto
        if (mattoncinoBomba - 4 >= 0 && mattoncinoBomba % 4 != 0) ditruggiMattoncino(mattoncinoBomba - 4);  // distrugge alto-dx
        if (mattoncinoBomba - 6 >= 0 && mattoncinoBomba % 5 != 0) ditruggiMattoncino(mattoncinoBomba - 6);  // distrugge alto-sx
        if (mattoncinoBomba + 6 < mattoncini.size() && mattoncinoBomba % 4 != 0) ditruggiMattoncino(mattoncinoBomba + 6);  // distrugge basso-dx
        if (mattoncinoBomba + 4 < mattoncini.size() && mattoncinoBomba % 5 != 0) ditruggiMattoncino(mattoncinoBomba + 4);  // distrugge basso-sx
    }

    private void ditruggiMattoncino(int mattoncino) {
        Brick b = mattoncini.get(mattoncino);
        if (!b.isDestroyed()) {
            b.distruggi();
            score = score + 80;
        }
    }

    //Controlla il punteggio per salvarlo nella top 3
    public void checkScore(int score){
        Log.i("Punteggio", "Partita finita, controllo punteggio " + String.valueOf(score));
        SharedPreferences sp = context.getSharedPreferences("com.example.android.arkanoid", Context.MODE_PRIVATE);

        String sprimo = sp.getString("primo", "0");   //Se non dovesse esistere utilizza il secondo argomento (0)
        String ssecondo = sp.getString("secondo", "0");
        String sterzo = sp.getString("terzo", "0");
        int primo = Integer.parseInt(sprimo);
        int secondo = Integer.parseInt(ssecondo);
        int terzo = Integer.parseInt(sterzo);

        //Salva il punteggio
        if (score > primo){
            sp.edit().putString("primo", String.valueOf(score)).apply();
            Log.i("Punteggio", "Nuovo primo posto:" + String.valueOf(score));
            newRecord = true;
        }else if (score > secondo){
            sp.edit().putString("secondo", String.valueOf(score)).apply();
            Log.i("Punteggio", "Nuovo secondo posto:" + String.valueOf(score));
        }else if (score > terzo){
            sp.edit().putString("terzo", String.valueOf(score)).apply();
            Log.i("Punteggio", "Nuovo terzo posto:" + String.valueOf(score));
        }
    }

    //zastav Snimanie = smetti di sparare
    public void smettiDiSparare() {
        if(sManager != null) {
            sManager.unregisterListener(this);
        }
    }

    //spustiSnimanie = eseguire scansione
    public void iniziaSparare() {
        if(sManager != null) {
            sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    // cambia accelerometro
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            paddle.setX(paddle.getX() - event.values[0] - event.values[0]);

            if (paddle.getX() + event.values[0] > size.x - 240) {
                paddle.setX(size.x - 240);
            } else if (paddle.getX() - event.values[0] <= 20) {
                paddle.setX(20);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Cosa succede al touch dello schermo
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gameOver == true && start == false) {
            score = 0;
            lifes = 3;
            resetLevel();
            gameOver = false;
        } else {
            if (playing == false) {
                startTime = System.nanoTime();
                playing = true;
            } else {
                if (controller == 0) {
                    if (event.getX() > size.x / 2) {
                        paddle.setX(paddle.getX() + 100);
                        if (paddle.getX() > size.x - 200) {
                            paddle.setX(size.x - 200);
                        }
                    } else {
                        paddle.setX(paddle.getX() - 100);
                        if (paddle.getX() < 0) {
                            paddle.setX(0);
                        }
                    }
                }
            }
            start = true;
        }
        return false;
    }

    // Imposta il gioco per iniziare
    private void resetLevel() {
        palla.setX(size.x / 2);
        palla.setY(size.y - 480);
        palla.createSpeed();
        mattoncini = new ArrayList<Brick>();
        generaMattoncini(context);
    }

    private boolean tuttiMattonciniDistrutti() {
        for (Brick b : mattoncini) {
            if (!b.isDestroyed()) {
                return false;
            }
        }
       return true;
    }

    // zisti ci hrac vyhral alebo nie
    //scopre se il giocatore ha vinto o no
    //vyhra = vincita
    private void vincita() {

        if (tuttiMattonciniDistrutti()) {
            ++level;
            resetLevel();
            palla.increaseSpeed(level);
            start = false;
        }
    }
}

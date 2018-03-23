package com.example.josuegramajo.infinitywarpresalescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.Time;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.josuegramajo.infinitywarpresalescanner.objects.LogObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean STARTED = false;
    FloatingActionButton fab;
    Timer timer;
    static String URL_ESTRENOS = "https://www.cinepolis.com.gt/proximos-estrenos";
    static String URL_PREVENTAS = "https://www.cinepolis.com.gt/preventas";
    static ArrayList<LogObject> log = new ArrayList<LogObject>();
    MediaPlayer mPlayer = null;
    Timer vibrateTimer;
    Vibrator vibe;

    public static int repetitionInterval = 300000;
    public static ArrayList<String> palabrasClave = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        palabrasClave.add("infinito");
        palabrasClave.add("infinity");
        palabrasClave.add("avengers");
        palabrasClave.add("vengadores");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatButtonAction();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void floatButtonAction(){
        if(STARTED){
            STARTED = false;
            fab.setImageResource(R.drawable.ic_action_play);
            if(timer != null){
                stopTimer();
            }

            if(mPlayer != null){
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
            if(vibrateTimer != null){
                vibrateTimer.cancel();
                vibrateTimer.purge();
                vibrateTimer = null;
            }
        }else{
            STARTED = true;
            fab.setImageResource(R.drawable.ic_action_stop);
            startTimer();
        }
    }

    public void stopTimer(){
        timer.cancel();
        timer.purge();
        timer = null;
    }

    public void startTimer(){
        getWebsite(URL_ESTRENOS);
        getWebsite(URL_PREVENTAS);

        timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                getWebsite(URL_ESTRENOS);
                getWebsite(URL_PREVENTAS);
            }
        };

        timer.schedule(myTask, repetitionInterval, repetitionInterval);
    }

    private void getWebsite(final String current_url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(current_url).get();
                    String title = doc.title();
                    Elements links = doc.select("html");

                    builder.append(title).append("\n");

                    for (Element link : links) {
                        builder.append(link.text());
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        evaluateResponse(builder.toString().toLowerCase(), current_url);
                    }
                });
            }
        }).start();
    }

    public void evaluateResponse(String html, String url){
        if(contains(html)){

            Toast.makeText(this, "ALA BERTA YA SALIO LA PREVENTA DE INFINITY WAR!!!!!",Toast.LENGTH_LONG).show();

            log.add(new LogObject(url, "YA SALIO LA PREVENTA DE INFINITY WAR!!!!!", getTime()));

            if(timer != null){
                timer.cancel();
                timer.purge();
                timer = null;
            }

            mayhem();
        }else{
            log.add(new LogObject(url, "Aun no ha salido la preventa :'c", getTime()));

            Toast.makeText(this, "Aun no ha salido chavo :s",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean contains(String html){
        for(String palabra : palabrasClave){
            if(html.contains(palabra)){
                return true;
            }
        }
        return false;
    }

    public String getTime(){
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        String date1 = t.format("%Y/%m/%d");

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aa",
                Locale.ENGLISH);
        String var = dateFormat.format(date);
        String horafecha = var+ " - " + date1;

        return horafecha;
    }

    public void mayhem(){
        mPlayer = MediaPlayer.create(this, R.raw.marvel_theme);
        mPlayer.setLooping(true);
        mPlayer.start();

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        vibrateTimer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                vibe.vibrate(500);
            }
        };
        vibrateTimer.schedule(myTask, 1000, 1000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_agenda) {
            startActivity(new Intent(this, LogActivity.class));
        } else if (id == R.id.nav_config) {
            startActivityForResult(new Intent(this, ConfigActivity.class),1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK && STARTED){
                stopTimer();
                startTimer();
                Toast.makeText(MainActivity.this, "Timer reiniciado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

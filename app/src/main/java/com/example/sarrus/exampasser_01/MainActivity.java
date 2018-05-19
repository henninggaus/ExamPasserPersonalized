package com.example.sarrus.exampasser_01;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Variablen zum Start / Beenden eines Trainings
    int trainingGestartet;

    //Variablen um Texte auszugeben
    TextView aktuelleAufgabeString;
    TextView punkteAusgabeString;
    TextView fehlerAusgabeString;
    TextView levelAusgabeString;
    TextView countDownString;

    //Variablen für Spielefortschritt
    int punktAnzahl,fehlerAnzahl, level;

    //Zufallszahlengenerator zum Erstellen der Gleichungen
    Random rand;
    int generierteZahl1, generierteZahl2,ergebnis;

    //Array, um alle Zahlen zu speichern
    ArrayList<Integer> summanden;

    //Merkervariablen für gegebene Antwort
    int antwortKorrektGegeben, antwortFalschGegeben;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Textfelder mit passender Variable koppeln
        aktuelleAufgabeString = (TextView) findViewById(R.id.gleichungsAusgabe);
        punkteAusgabeString = (TextView) findViewById(R.id.punktausgabe);
        fehlerAusgabeString = (TextView) findViewById(R.id.fehlerAusgabe);
        countDownString = (TextView) findViewById(R.id.countDownAntwort);
        levelAusgabeString = (TextView) findViewById(R.id.levelAusgabe);

        //Toast Message ausgeben, dass alles okay ist
        Toast.makeText(getApplicationContext(),"Willkommen beim Exam Passer",Toast.LENGTH_SHORT).show();


        //Initalizieren des Zufallszahlengenerators
        rand = new Random();

        //ArrayList anlegen für die Summanden
        summanden = new ArrayList<Integer>();

        //Defaultwert zuweisen für Merkervariablen
        antwortKorrektGegeben=0; antwortFalschGegeben=0; trainingGestartet=0; level=0;

        //Punkte und Fehler auf Null setzen
        punktAnzahl = 0;
        fehlerAnzahl=0;
    }

    public void angabeKorrekt(View v){
        if(antwortKorrektGegeben==0 && antwortFalschGegeben==0){
            antwortKorrektGegeben++;
        }
    }
    public void angabeFalsch(View v){
        if(antwortKorrektGegeben==0 && antwortFalschGegeben==0){
            antwortFalschGegeben++;
        }
    }
    public void anzeigenAktualisieren(){
        //Ausgabe der aktuellen Punkte
        String ausgabeStringPunktAnzahl = "Punkte: " + punktAnzahl;
        punkteAusgabeString.setText(ausgabeStringPunktAnzahl);

        //Ausgabe der aktuellen Fehler
        String ausgabeStringFehlerAnzahl = "Fehler: " + fehlerAnzahl;
        fehlerAusgabeString.setText(ausgabeStringFehlerAnzahl);

        //Ausgabe der aktuellen Schwierigkeitsstufe
        String ausgabeLevelAktuell = "Level: " + level;
        levelAusgabeString.setText(ausgabeLevelAktuell);
    }
    public void gameOver(){
        //Toast Message der erreichten Punkte zeigen
        Toast.makeText(getApplicationContext(),"Dein Ergebnis: "+ punktAnzahl,Toast.LENGTH_LONG).show();
        //im Gleichungsfeld wird das letzte Ergebnis gedruckt
        aktuelleAufgabeString.setText("letzte Ergebnis: " + punktAnzahl + " Punkte");
        //CountDown - Textbox zeigt nichts mehr an
        countDownString.setText("");
        //Variablen resetten
        antwortKorrektGegeben=0;
        antwortFalschGegeben=0;
        trainingGestartet=0;
        level=0;
        punktAnzahl = 0;
        fehlerAnzahl=0;
    }

    public int trainingStarten(View v){
        if(trainingGestartet == 1){
            return 0;
        }

        trainingGestartet = 1;
        new CountDownTimer(3000, 100) {
            public void onTick(long millisUntilFinished) {
                aktuelleAufgabeString.setText("Bereit machen: " + millisUntilFinished / 1000 + " s");
            }
            public void onFinish() {
                aktuelleAufgabeString.setText("Start");
                aufgabeStellen();
            }
            private int aufgabeStellen() {
                //Antwortvariablen zurücksetzen
                antwortKorrektGegeben=0;
                antwortFalschGegeben=0;

                //Zufallszahlen für erstes Level generieren
                generierteZahl1 = rand.nextInt(10)+1;
                generierteZahl2 = rand.nextInt(10)+1;

                //Ergebnis berechnen
                ergebnis = generierteZahl1 + generierteZahl2;

                //Ergebnis durch Zufall verfälschen
                int verfaelschen = rand.nextInt(10)+1;
                if(verfaelschen>5){
                    int abweichung = rand.nextInt(3)+1;
                    ergebnis += abweichung;
                }

                //Array leeren
                summanden.clear();
                summanden.add(generierteZahl1);
                summanden.add(generierteZahl2);

                //Ausgeben des Punktestands
                anzeigenAktualisieren();

                //Ausgabe der aktuellen Gleichung
                String ausgabeStringGleichung = generierteZahl1 + " + " + generierteZahl2 + " = " + ergebnis + " ?";
                aktuelleAufgabeString.setText(ausgabeStringGleichung);

                new CountDownTimer(10000, 5) {
                    public void onTick(long millisUntilFinished) {
                        //CountDown Angabe
                        countDownString.setText("Antwortzeit: " + millisUntilFinished / 1000 + " s");

                        //Auswertem der Nutzereingaben
                        if( antwortKorrektGegeben == 1 && ergebnis == (generierteZahl2+generierteZahl1)  ){
                            punktAnzahl++;
                            anzeigenAktualisieren();
                            this.cancel();
                            aufgabeStellen();
                        }else if( antwortFalschGegeben == 1 && ergebnis != (generierteZahl2+generierteZahl1)  ){
                            punktAnzahl++;
                            anzeigenAktualisieren();
                            this.cancel();
                            aufgabeStellen();
                        }else if(antwortKorrektGegeben == 1 || antwortFalschGegeben == 1){
                            fehlerAnzahl++;
                            anzeigenAktualisieren();
                            this.cancel();
                            if(fehlerAnzahl<5){
                                aufgabeStellen();
                            }
                            else{
                                gameOver();
                            }
                        }
                    }
                    public void onFinish() {
                        fehlerAnzahl++;
                        anzeigenAktualisieren();
                        this.cancel();
                        if(fehlerAnzahl<5){
                            aufgabeStellen();
                        }
                        else{
                            gameOver();
                        }
                    }
                }.start();

                return 0;
            }
        }.start();

        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

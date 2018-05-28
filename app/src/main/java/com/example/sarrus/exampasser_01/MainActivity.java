package com.example.sarrus.exampasser_01;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private int highscore;
    private int startLevel;

    //Variablen für Sounds
    public MediaPlayer soundRichtigeAntwort;
    public MediaPlayer soundFalscheAntwort;
    public MediaPlayer soundNaechsteLevel;


    //Variablen zum Start / Beenden eines Trainings
    int trainingGestartet;

    //Variablen für Spielefortschritt
    int punktAnzahl, fehlerAnzahlInFolge, level, aktuellGroesstmoeglicheErgebnis, levelErhoehenMoeglich, korrekteAntwortenInFolge;

    //Zufallszahlengenerator zum Erstellen der Gleichungen
    Random rand;

    //Variablen für Gleichungenlogik
    int generierteZahl1, generierteZahl2,korrektesErgebnis, angezeigteErgebnis;

    //Merkervariablen für gegebene Antwort
    int antwortKorrektGegeben, antwortFalschGegeben;


    //Variablen um Texte auszugeben
    TextView verbleibendeTrainingszeitString;
    TextView aktuelleAufgabeString;
    TextView punkteAusgabeString;
    TextView fehlerAusgabeString;
    TextView levelAusgabeString;
    TextView countDownString;
    TextView fitnesLevelString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Textfelder mit passender Variable koppeln
        verbleibendeTrainingszeitString = (TextView) findViewById(R.id.verbleibendeTrainingszeit);
        aktuelleAufgabeString = (TextView) findViewById(R.id.gleichungsAusgabe);
        punkteAusgabeString = (TextView) findViewById(R.id.punktausgabe);
        fehlerAusgabeString = (TextView) findViewById(R.id.fehlerAusgabe);
        countDownString = (TextView) findViewById(R.id.countDownAntwort);
        levelAusgabeString = (TextView) findViewById(R.id.levelAusgabe);
        fitnesLevelString = (TextView) findViewById(R.id.fitnsesstandAusgabe);


        //Den Nutzer mit Toast Message begrüßen
        Toast.makeText(getApplicationContext(),"Willkommen beim Exam Passer",Toast.LENGTH_SHORT).show();

        //Initalizieren des Zufallszahlengenerators
        rand = new Random();

        //Defaultwert zuweisen für integer Variablen
        antwortKorrektGegeben=0; antwortFalschGegeben=0; trainingGestartet=0; angezeigteErgebnis=0; punktAnzahl = 0; fehlerAnzahlInFolge=0;
        aktuellGroesstmoeglicheErgebnis=10; levelErhoehenMoeglich=1; level=1; korrekteAntwortenInFolge=0;

        //Sounds zuweisen
        soundRichtigeAntwort = MediaPlayer.create(getApplicationContext(), R.raw.richtige_antwort);
        soundFalscheAntwort = MediaPlayer.create(getApplicationContext(), R.raw.falsche_antwort);
        soundNaechsteLevel = MediaPlayer.create(getApplicationContext(), R.raw.naechste_level);

        //Antwortbuttons ausblenden
        antwortButtonsAusblenden();

    }

    //Nutzereingaben auswerten
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

    //Sounds, Vibration usw. abspielen
    public void antwortKorrektSound(){
        //Sound abspielen bei korrekter Antwort
        //if(soundRichtigeAntwort.isPlaying()){
        //    soundRichtigeAntwort.pause();
        //}
        //soundRichtigeAntwort.seekTo(0);
        soundRichtigeAntwort.start();
    }
    public void falscheAntwortSound(){
        //Sound abspielen bei falscher Antwort
        //if(soundFalscheAntwort.isPlaying()){
        //   soundFalscheAntwort.pause();
        //}
        //mp.seekTo(0);
        soundFalscheAntwort.start();
    }
    public void naechsteLevelSound(){
        //Sound abspielen bei falscher Antwort
        //if(soundFalscheAntwort.isPlaying()){
        //   soundFalscheAntwort.pause();
        //}
        //mp.seekTo(0);
        soundNaechsteLevel.start();
    }

    //Sichtbarkeiten steuern
    public void anzeigenAktualisieren(){
        //Ausgabe der aktuellen Punkte
        String ausgabeStringPunktAnzahl = "Punkte: " + punktAnzahl;
        punkteAusgabeString.setText(ausgabeStringPunktAnzahl);

        //Ausgabe der aktuellen Fehler
        String ausgabeStringFehlerAnzahl = "Fehler: " + fehlerAnzahlInFolge;
        fehlerAusgabeString.setText(ausgabeStringFehlerAnzahl);

        //Ausgabe der aktuellen Schwierigkeitsstufe
        String ausgabeLevelAktuell = "Level: " + level;
        levelAusgabeString.setText(ausgabeLevelAktuell);
    }
    public void antwortButtonsAusblenden(){
        //Spielbuttons ausblenden
        Button korrektButtonSichtbarkeit = (Button) findViewById(R.id.korrektButton);
        korrektButtonSichtbarkeit.setVisibility(View.INVISIBLE);

        Button falschButtonSichtbarkeit = (Button) findViewById(R.id.falschButton);
        falschButtonSichtbarkeit.setVisibility(View.INVISIBLE);
    }
    public void antwortButtonsEinblenden(){
        //Spielbuttons ausblenden
        Button korrektButtonSichtbarkeit = (Button) findViewById(R.id.korrektButton);
        korrektButtonSichtbarkeit.setVisibility(View.VISIBLE);

        Button falschButtonSichtbarkeit = (Button) findViewById(R.id.falschButton);
        falschButtonSichtbarkeit.setVisibility(View.VISIBLE);
    }

    //Anleitung als Alert ausgeben
    public void anleitungPopUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Anleitung")
                .setMessage("Dir werden Gleichungen nach dem Muster 3 + 1 = 5 ? angezeigt. \n" +
                            "Deine Aufgabe ist es, so schnell wie möglich zu antworten, ob die angezeigte Lösung stimmt oder nicht. \n" +
                            "Zum Antworten bleiben nur 5 Sekunden Zeit. \nSolltest du zu langsam sein, gilt die Antwort als falsch. \n" +
                            "Mit steigendem Level werden die Gleichungen schwerer un du erhälst mehr Punkte pro Lösung. " +
                            "In jedem Level musst du mindestens 50% der Aufgaben richtig beantworten, um weiter zu kommen.");
        builder.create();
        builder.show();
    }

    //Spiel verwalten
    public void gleichungGenerieren(int groessteZahl){
        if(groessteZahl == 0){
            groessteZahl = 10;
            Log.w("Zufallszahlengenerator", "Die Funktion 'gleichungGenerieren' wurde mit Parameter = 0 aufgerufen.\n" +
                    "Der Zufallszahlengenerator hätte die App zum Absturz gebracht. Unbedingt prüfen!");
        }
        do{
            //Addition oder Subtration wird ausgewählt
            int rechenart = rand.nextInt(4)+1;

            //Rechenoperation auswählen
            switch (rechenart) {
                case 1:
                    do{
                        generierteZahl1 = rand.nextInt(groessteZahl)+1;
                        generierteZahl2 = rand.nextInt(groessteZahl)+1;
                        korrektesErgebnis = generierteZahl1 + generierteZahl2;
                    }while(korrektesErgebnis > groessteZahl);
                    break;
                case 2:
                    do{
                        generierteZahl1 = rand.nextInt(groessteZahl)+1;
                        generierteZahl2 = rand.nextInt(groessteZahl)+1;
                        korrektesErgebnis = generierteZahl1 - generierteZahl2;
                    }while(korrektesErgebnis > groessteZahl);
                    break;
                case 3:
                    int rest;
                    do{
                        generierteZahl1 = rand.nextInt(groessteZahl)+1;
                        generierteZahl2 = rand.nextInt(groessteZahl)+1;
                        korrektesErgebnis = generierteZahl1 / generierteZahl2;
                        rest = generierteZahl1 % generierteZahl2;
                    }while( korrektesErgebnis > groessteZahl || rest != 0 || generierteZahl1 == generierteZahl2 || generierteZahl2 == 1);
                    break;
                case 4:
                    double maximumFuerMultiplikationBerechnen;
                    maximumFuerMultiplikationBerechnen = Math.pow(groessteZahl,1);
                    int maximumFuerMultipikation = (int) maximumFuerMultiplikationBerechnen;
                    do{
                        generierteZahl1 = rand.nextInt(maximumFuerMultipikation)+1;
                        generierteZahl2 = rand.nextInt(maximumFuerMultipikation)+1;
                        korrektesErgebnis = generierteZahl1 * generierteZahl2;
                    }while( korrektesErgebnis > groessteZahl || generierteZahl1 == 1 || generierteZahl2 == 1);
                    break;
                default:
                    break;

            }

            //korrektesErgebnis durch Zufall verfälschen
            int verfaelschen = rand.nextInt(10)+1;
            //do{
            if(verfaelschen>5){
                //korrektesErgebnis wird verfälscht angezeigt
                int verfaelschungsRichtung = rand.nextInt(10)+1;
                int abweichung = rand.nextInt(3)+1;
                //Verfälschungsrichtung wird ausgewertet
                if(verfaelschungsRichtung>5){
                    angezeigteErgebnis = korrektesErgebnis + abweichung;
                }else{
                    angezeigteErgebnis = korrektesErgebnis - abweichung;
                }
            }else{
                //Korrekte korrektesErgebnis wird angezeigt
                angezeigteErgebnis = korrektesErgebnis;
            }
            //}while( angezeigteErgebnis == generierteZahl1 || angezeigteErgebnis == generierteZahl2 );

            //Animation der aktuellen Gleichung
            Animation animation;
            animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
            aktuelleAufgabeString.startAnimation(animation);

            //Ausgabe der aktuellen Gleichung
            String ausgabeStringGleichung;
            switch (rechenart) {
                case 1:
                    ausgabeStringGleichung = generierteZahl1 + " + " + generierteZahl2 + " = " + angezeigteErgebnis + " ?";
                    break;
                case 2:
                    ausgabeStringGleichung = generierteZahl1 + " - " + generierteZahl2 + " = " + angezeigteErgebnis + " ?";
                    break;
                case 3:
                    ausgabeStringGleichung = generierteZahl1 + " / " + generierteZahl2 + " = " + angezeigteErgebnis + " ?";
                    break;
                case 4:
                    ausgabeStringGleichung = generierteZahl1 + " * " + generierteZahl2 + " = " + angezeigteErgebnis + " ?";
                    break;
                default:
                    ausgabeStringGleichung = "";
            }
            aktuelleAufgabeString.setText(ausgabeStringGleichung);
        }while( angezeigteErgebnis == generierteZahl1 || angezeigteErgebnis == generierteZahl2 );
    }
    public void levelVerwalten(){
        if(korrekteAntwortenInFolge == 5 && level > 0){
            level++;
            naechsteLevelSound();
            korrekteAntwortenInFolge = 0;
            fehlerAnzahlInFolge = 0;
        }else if(fehlerAnzahlInFolge == 3 && level > 1){
            level--;
            korrekteAntwortenInFolge = 0;
            fehlerAnzahlInFolge = 0;
        }else if(fehlerAnzahlInFolge == 3){
            level = 1;
            korrekteAntwortenInFolge = 0;
            fehlerAnzahlInFolge = 0;
        }
        //Gleichung wird generiert
        aktuellGroesstmoeglicheErgebnis = (10 * level);
        gleichungGenerieren(aktuellGroesstmoeglicheErgebnis);
    }

    //Spielzeit ist vorbei
    public void gameOver(){
        //Antwortbuttons ausblenden
        antwortButtonsAusblenden();

        //Spielbuttons ausblenden
        Button korrektButtonSichtbarkeit = (Button) findViewById(R.id.korrektButton);
        korrektButtonSichtbarkeit.setVisibility(View.INVISIBLE);

        Button falschButtonSichtbarkeit = (Button) findViewById(R.id.falschButton);
        falschButtonSichtbarkeit.setVisibility(View.INVISIBLE);

        //Vibrieren initialisieren
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //letzte Level abspeichern in Gerät speichern
        SharedPreferences spLevel = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor eLevel = spLevel.edit();
        eLevel.putInt("letzteLevel",level);
        eLevel.commit();

        //HighScore auswerten und im Gerät speichern
        SharedPreferences sp1 = getPreferences(MODE_PRIVATE);
        highscore = sp1.getInt("highscore",0);
        if(highscore < punktAnzahl){
            highscore = punktAnzahl;
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putInt("highscore",highscore);
            e.commit();
            long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
            v.vibrate(pattern, -1);
            //Toast Message der erreichten Punkte zeigen
            Toast.makeText(getApplicationContext(),"neuer Bestwert!: "+ punktAnzahl,Toast.LENGTH_LONG).show();
        }else{
            v.vibrate(500);
            //Toast Message der erreichten Punkte zeigen
            Toast.makeText(getApplicationContext(),"Dein Ergebnis: "+ punktAnzahl,Toast.LENGTH_LONG).show();
        }

        //im Gleichungsfeld wird der HighScore gezeigt
        aktuelleAufgabeString.setText("Bestwert: " + highscore + " Punkte");
        //Ausgabe der letztem Punkte und des Fitneslevels
        fitneslevelErmitteln();
        String ausgabeStringPunktAnzahl = "letzte Ergebnis: " + punktAnzahl + " Punkte";
        punkteAusgabeString.setText(ausgabeStringPunktAnzahl);
        //Fehleranzeige leer setzen
        String ausgabeStringFehlerAnzahl = "";
        fehlerAusgabeString.setText(ausgabeStringFehlerAnzahl);
        //Schwierigkeitsstufe zurücksetzen
        String ausgabeLevelAktuell = "";
        levelAusgabeString.setText(ausgabeLevelAktuell);

        //CountDown - Textbox zeigt nichts mehr an
        countDownString.setText("");
        //Variablen resetten
        antwortKorrektGegeben=0;
        antwortFalschGegeben=0;
        trainingGestartet=0;
        punktAnzahl = 0;
        fehlerAnzahlInFolge=0;
    }

    //Level verbal ausgeben, zum Aufmuntern
    public void fitneslevelErmitteln(){
        switch(level){
            case 1: fitnesLevelString.setText("Dein Fitnesslevel: \nAnfänger"); break;
            case 2: fitnesLevelString.setText("Dein Fitnesslevel: \nAufstrebender"); break;
            case 3: fitnesLevelString.setText("Dein Fitnesslevel: \nFortgeschrittener"); break;
            case 4: fitnesLevelString.setText("Dein Fitnesslevel: \nSchüler"); break;
            case 5: fitnesLevelString.setText("Dein Fitnesslevel: \nMusterschüler"); break;
            case 6: fitnesLevelString.setText("Dein Fitnesslevel: \nWunschkind"); break;
            case 7: fitnesLevelString.setText("Dein Fitnesslevel: \nAbiturient"); break;
            case 8: fitnesLevelString.setText("Dein Fitnesslevel: \nMeisterschüler"); break;
            case 9: fitnesLevelString.setText("Dein Fitnesslevel: \nMeister"); break;
            case 10: fitnesLevelString.setText("Dein Fitnesslevel: \nAusbildungsleiter"); break;
            case 11: fitnesLevelString.setText("Dein Fitnesslevel: \nStudierender"); break;
            case 12: fitnesLevelString.setText("Dein Fitnesslevel: \nMusterstudent"); break;
            case 13: fitnesLevelString.setText("Dein Fitnesslevel: \nDiplomant"); break;
            case 14: fitnesLevelString.setText("Dein Fitnesslevel: \nAkademiker"); break;
            case 15: fitnesLevelString.setText("Dein Fitnesslevel: \nGelehrter"); break;
            case 16: fitnesLevelString.setText("Dein Fitnesslevel: \nWeiser"); break;
            case 17: fitnesLevelString.setText("Dein Fitnesslevel: \nDoktorand"); break;
            case 18: fitnesLevelString.setText("Dein Fitnesslevel: \nDoktor"); break;
            case 19: fitnesLevelString.setText("Dein Fitnesslevel: \njunior Professor"); break;
            case 20: fitnesLevelString.setText("Dein Fitnesslevel: \nProfessor"); break;
            case 21: fitnesLevelString.setText("Dein Fitnesslevel: \nDoktorvater"); break;
            case 22: fitnesLevelString.setText("Dein Fitnesslevel: \nStudiengangsleiter"); break;
            case 23: fitnesLevelString.setText("Dein Fitnesslevel: \nUniversitätsdirektor"); break;
            case 24: fitnesLevelString.setText("Dein Fitnesslevel: \nGenie"); break;
            case 25: fitnesLevelString.setText("Dein Fitnesslevel: \nMultigenie"); break;
            case 26: fitnesLevelString.setText("Dein Fitnesslevel: \nNobelpreisanwärter"); break;
            case 27: fitnesLevelString.setText("Dein Fitnesslevel: \nNobelpreisträger"); break;
            case 28: fitnesLevelString.setText("Dein Fitnesslevel: \nMulti Nobelpreisträger"); break;
            case 29: fitnesLevelString.setText("Dein Fitnesslevel: \nWissenschaftsreformierer"); break;
            case 30: fitnesLevelString.setText("Dein Fitnesslevel: \nGeschichts - Neuschreiber"); break;
            case 31: fitnesLevelString.setText("Dein Fitnesslevel: \nKoryphäe"); break;
            default: fitnesLevelString.setText("Dein Fitnesslevel: \nApollon"); break;
        }
        //Fitnesslevel einblenden
        TextView fitnesstandSichtbarkeit = (TextView) findViewById(R.id.fitnsesstandAusgabe);
        fitnesstandSichtbarkeit.setVisibility(View.VISIBLE);
    }

    //Spielzeit überwachen
    public void spielZeitFestlegen(){
        new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long verbleibendeTrainingszeitInMS) {
                verbleibendeTrainingszeitString.setText("Trainingszeit: " + verbleibendeTrainingszeitInMS / 1000 + " s");
            }

            public void onFinish() {
                if(trainingGestartet > 0){
                    gameOver();
                }
            }
        }.start();
    }

    //Training wird gestartet
    public int trainingStarten(View v){
        if(trainingGestartet == 1){
            return 0;
        }

        trainingGestartet = 1;
        aktuellGroesstmoeglicheErgebnis=10; levelErhoehenMoeglich=1; korrekteAntwortenInFolge=0;

        //Startlevel wird festgelegt
        SharedPreferences spLevel = getPreferences(MODE_PRIVATE);
        level = spLevel.getInt("letzteLevel",1);
        //level = startLevel; Startlevel kann vermutlich weg

        //Fitnessstufe ausblenden
        TextView fitnesstandSichtbarkeit = (TextView) findViewById(R.id.fitnsesstandAusgabe);
        fitnesstandSichtbarkeit.setVisibility(View.INVISIBLE);

        new CountDownTimer(4000, 100) {
            public void onTick(long millisUntilFinished) {
                aktuelleAufgabeString.setText("Bereit machen: " + millisUntilFinished / 1000 + " s");
            }
            public void onFinish() {
                aktuelleAufgabeString.setText("Start");
                spielZeitFestlegen();
                aufgabeStellen();
            }
            public void aufgabeStellen() {
                //Sollte die Trainingszeit abgelaufen sein, wird die Funktion sofort verlassen
                if( trainingGestartet==0 ){
                    return;
                }

                //Antwortvariablen zurücksetzen
                antwortKorrektGegeben=0;
                antwortFalschGegeben=0;

                //Antwortbuttons anzeigen
                antwortButtonsEinblenden();

                //Gleichung einblenden
                levelVerwalten();

                //Ausgeben des Punktestands
                anzeigenAktualisieren();

                //Antwortzeit des Nutzers beginnt
                new CountDownTimer(5000, 5) {
                    public void onTick(long millisUntilFinished) {
                        //CountDown Angabe
                        countDownString.setText("Antwortzeit: " + millisUntilFinished / 1000 + " s");

                        //Logik, die ausgewertet werden muss
                        if( trainingGestartet==0 ){
                            this.cancel();
                        }else if( antwortKorrektGegeben == 1 && korrektesErgebnis == angezeigteErgebnis  ){
                            this.cancel();
                            punktAnzahl+=level;
                            korrekteAntwortenInFolge++;
                            fehlerAnzahlInFolge = 0;
                            anzeigenAktualisieren();
                            //Sound abspielen bei korrekter Antwort
                            antwortKorrektSound();
                            //gleiche Funktion rekursiv aufrufen
                            aufgabeStellen();
                        }else if( antwortFalschGegeben == 1 && korrektesErgebnis != angezeigteErgebnis ){
                            this.cancel();
                            punktAnzahl+=level;
                            korrekteAntwortenInFolge++;
                            fehlerAnzahlInFolge = 0;
                            anzeigenAktualisieren();
                            //Sound abspielen bei korrekter Antwort
                            antwortKorrektSound();
                            //gleiche Funktion rekursiv aufrufen
                            aufgabeStellen();
                        }else if(antwortKorrektGegeben == 1 || antwortFalschGegeben == 1){
                            //Logik bei falscher Antwort
                            this.cancel();
                            fehlerAnzahlInFolge++;
                            korrekteAntwortenInFolge = 0;
                            anzeigenAktualisieren();
                            falscheAntwortSound();
                            //Vibrieren, wenn die Antwort falsch war, und das Spiel weiter geht
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(100);
                            v.vibrate(100);
                            aufgabeStellen();
                        }
                    }
                    public void onFinish() {
                        if(trainingGestartet == 0) {
                            this.cancel();
                        }
                        else{
                            //Logik bei falscher Antwort
                            this.cancel();
                            fehlerAnzahlInFolge++;
                            korrekteAntwortenInFolge = 0;
                            anzeigenAktualisieren();
                            falscheAntwortSound();
                            //Vibrieren, wenn der Nutzer nicht geantwortet hat und die Antwort daher als falsch gilt
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(100);
                            v.vibrate(100);
                            aufgabeStellen();
                        }
                    }
                }.start();
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
            //naechsteLevelSound();
            anleitungPopUp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

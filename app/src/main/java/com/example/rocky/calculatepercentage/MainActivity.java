package com.example.rocky.calculatepercentage;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    double calculatedNumber = 0;
    double storedValue = 0;
    double percentage = 0;
    double finalNumber = 0;
    double rawFinal = 0;
    double poundCalc = 0;
    double convertedWeight = 0;
    double ormWeight = 0;
    double oneRepMax = 0;

    int repetitions = 0;
    int intPart = 0;
    int lastDigit = 0;

    Boolean switchCheck = false;

    DecimalFormat df = new DecimalFormat();

    EditText poundWeight, weight, weight2, reps, Percentage;
    TextView result, LBresult, timer;
    ImageView image;

    Switch xmlSwitch;

    Button start, pause, reset, submitButton, submit1, submit2;
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, MilliSeconds ;

    Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // locate elements on screen we need to work with

        submitButton = findViewById(R.id.submit);
        weight = findViewById(R.id.cpWeight);
        Percentage = findViewById(R.id.percentageText);
        result = findViewById(R.id.KGresult);
        LBresult = findViewById(R.id.LBresult);
        image = findViewById(R.id.imageView);
        poundWeight = findViewById(R.id.weightInKg);
        submit1 = findViewById(R.id.submit1);
        xmlSwitch = findViewById(R.id.switch1);
        weight2 = findViewById(R.id.ORMWeight);
        reps = findViewById(R.id.Reps);
        submit2 = findViewById(R.id.submit2);
        timer = findViewById(R.id.tvTimer);
        start = findViewById(R.id.btStart);
        pause = findViewById(R.id.btPause);
        reset = findViewById(R.id.btReset);

        handler = new Handler() ;

        df.setMaximumFractionDigits(3);

        calculateORM();
        stopwatchHandler();
        weightConverter();
        percentageCalculator();


    }

    private void calculateORM() {
        submit2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                // store the weight
                ormWeight = Double.valueOf(weight2.getText().toString());

                //store the reps
                repetitions = Integer.valueOf(reps.getText().toString());

                //formula to get 1RM
                if(repetitions>1) {
                    oneRepMax = ormWeight * (1 + repetitions / 30f);
                }
                else{
                    // for retards who enter 1 rep
                    oneRepMax = ormWeight;
                    result.setText((int)oneRepMax + "Kg" + "\n");
                    result.setTextSize(80);
                }

                //pound conversion
                convertedWeight = (int)oneRepMax * 2.20462;
                lastDigit = (int) convertedWeight;



                weight.setText(String.valueOf((int)oneRepMax)); // pre-set percentage calculator weight value to the ORM

                // show the picture of the weight
                image.setVisibility(View.VISIBLE);

                if(oneRepMax!=ormWeight) {
                    result.setText((int) oneRepMax + "Kg" + "\n");
                    result.setTextSize(80);
                }
                LBresult.setText((df.format(lastDigit)) + "Lb" + "\n");
                LBresult.setTextSize(80);

            }
        });
    }

    public Runnable runnable = new Runnable() {

        int seconds = 30;
        int minutes = 1;

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);

            // play notification sound every 30 seconds
            if(Seconds == seconds) {

                    mediaPlayer();
                    mediaPlayer.start();

            }
            // play notification sound every 1 minute
                else if(Minutes == minutes && Seconds == 0){

                    mediaPlayer();
                    mediaPlayer.start();
                    minutes+=1;
                }

        }

    };

    private void stopwatchHandler(){

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                timer.setVisibility(View.VISIBLE);
                reset.setEnabled(false);

                mediaPlayer();

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                reset.setEnabled(true);

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                timer.setText("00:00:00");




            }
        });
    }

    private void weightConverter(){

        // on tap remove the hint
        poundWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    poundWeight.setHint(" ");
                }
            }
        });

        //change the hint based of slider position
        xmlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchCheck = true;
                    poundWeight.setHint("Pounds");
                } else {
                    switchCheck = false;
                    poundWeight.setHint("Kilograms");
                }
            }
        });

        // calculate either pounds or kgs based on slider position
        submit1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if(!switchCheck) {

                    poundCalc = Double.valueOf( poundWeight.getText().toString());

                    convertedWeight = poundCalc * 2.20462;

                    lastDigit = (int) convertedWeight; // get the last digit as well as the decimals

                    // show the picture of the weight
                    image.setVisibility(View.VISIBLE);

                    result.setText((df.format(lastDigit)) + "Lb" + "\n");
                    result.setTextSize(80);

                }
                else{

                    poundCalc = Double.valueOf(poundWeight.getText().toString());

                    convertedWeight = poundCalc / 2.20462;

                    lastDigit = (int) convertedWeight; // get the last digit as well as the decimals

                    // show the picture of the weight
                    image.setVisibility(View.VISIBLE);

                    result.setText((df.format(lastDigit)) + "Kg" + "\n");
                    result.setTextSize(80);
                }

            }
        });

    }

    private void percentageCalculator(){

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //store the percentage chosen
                percentage = Double.valueOf(Percentage.getText().toString());


                //store the weight
                storedValue = Float.parseFloat(weight.getText().toString().trim());

                // show the picture of the weight
                image.setVisibility(View.VISIBLE);

                // get the percentage
                calculatedNumber = storedValue*(percentage/100.0f);
                calculatedNumber = storedValue - calculatedNumber;

                // get the weight i should be lifting
                finalNumber = storedValue-calculatedNumber;
                rawFinal = finalNumber;
                intPart = (int) finalNumber;

                double lastDigit = finalNumber % 10; // get the last digit as well as the decimals

                // cases for weight between depending in what range it falls
                if(lastDigit > 1.25 && lastDigit <= 3.75){
                    finalNumber = finalNumber - lastDigit;
                    finalNumber = finalNumber + 2.5;
                }
                else if(lastDigit <= 1.25){
                    finalNumber = finalNumber - lastDigit; // round down to the nearest 10
                }
                else if(lastDigit > 3.75 && lastDigit <= 6.25){
                    finalNumber = finalNumber - lastDigit; // get the rounded down number to the closest 10
                    finalNumber = finalNumber + 5;
                }
                else if(lastDigit > 6.25 && lastDigit <= 8.75){
                    finalNumber = finalNumber - lastDigit;
                    finalNumber = finalNumber + 7.5;
                }
                else if(lastDigit > 8.75){
                    finalNumber = finalNumber - lastDigit;
                    finalNumber = finalNumber + 10;
                }

                result.setText((df.format(finalNumber)) + "Kg" + "\n");
                result.setTextSize(80);

                //pound conversion
                double poundWeight = finalNumber * 2.20462;
                LBresult.setText((df.format((int)poundWeight)) + "Lb" + "\n");
                LBresult.setTextSize(80);


                /**
                 ----- 10KG ROUNDING LOGIC------

                 To decide whether the weight will be flat value ending in 0 or if it will sit between 2.5 | 5 | 7.5
                 Below 1.25 is considered a round value of eg 80
                 Above 1.25 but less than 3.75 is considered a 2.5kg addition eg 82.5
                 Above 3.75 but less that 6.25 is considered a 5kg addition to the whole number eg 85
                 Above 6.75 but lower than 8.75 i considered a 7,5kg addition eg 87.5
                 Anything above 8.75 is automatically a +10kg addition to the next whole number
                 */

            }
        });
    }

    private void mediaPlayer(){

            try {
                mediaPlayer.setDataSource(this, defaultRingtoneUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        //mediaPlayer.stop();
                        mediaPlayer.reset();

                    }
                });

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}



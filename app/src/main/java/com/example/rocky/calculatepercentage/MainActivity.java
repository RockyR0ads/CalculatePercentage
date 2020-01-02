package com.example.rocky.calculatepercentage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
    Boolean resetValues = false;

    DecimalFormat df = new DecimalFormat();

    Button submitButton;
    Button submit1;
    Button submit2;

    EditText poundWeight;
    EditText weight;
    EditText weight2;
    EditText reps;
    EditText Percentage;

    TextView result;
    TextView LBresult;

    ImageView image;

    Switch xmlSwitch;


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

        df.setMaximumFractionDigits(3);

        image.setVisibility(View.INVISIBLE);

        calculateORM();

        xmlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        switchCheck = true;
                        poundWeight.setHint("Pounds");
                } else {
                       switchCheck = false;
                       poundWeight.setHint("Kgs");
                }
            }
        });


        // reset application when attempting to calculate a new number
        weight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(resetValues){
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else{
                    resetValues = true;
                }

            }
        });

        poundWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   poundWeight.setHint(" ");
                }
            }
        });

        submit1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(!switchCheck) {

                    poundCalc = Integer.valueOf( poundWeight.getText().toString());

                    convertedWeight = poundCalc * 2.20462;

                    lastDigit = (int) convertedWeight; // get the last digit as well as the decimals

                    // show the picture of the weight
                    image.setVisibility(View.VISIBLE);

                    result.setText((df.format(lastDigit)) + " Lbs" + "\n");
                    result.setTextSize(50);

                }
                else{

                    poundCalc = Integer.valueOf(poundWeight.getText().toString());

                    convertedWeight = poundCalc / 2.20462;

                    lastDigit = (int) convertedWeight; // get the last digit as well as the decimals

                    // show the picture of the weight
                    image.setVisibility(View.VISIBLE);

                    result.setText((df.format(lastDigit)) + " Kgs" + "\n");
                    result.setTextSize(50);
                }

            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //store the percentage chosen
                percentage = Double.valueOf(Percentage.getText().toString());


                //store the weight
                storedValue = Float.parseFloat(weight.getText().toString().trim());

                // show the picture of the weight
                image.setVisibility(View.VISIBLE);

                // get the percentage
                calculatedNumber = storedValue*(percentage/100.0f);

                // get the weight i should be lifting
                finalNumber = storedValue-calculatedNumber;
                rawFinal = finalNumber;
                intPart = (int) finalNumber;


                /**
                                            ----- 10KG ROUNDING LOGIC------

                    To decide whether the weight will be flat value ending in 0 or if it will sit between 2.5 | 5 | 7.5
                    Below 1.25 is considered a round value of eg 80
                    Above 1.25 but less than 3.75 is considered a 2.5kg addition eg 82.5
                    Above 3.75 but less that 6.25 is considered a 5kg addition to the whole number eg 85
                    Above 6.75 but lower than 8.75 i considered a 7,5kg addition eg 87.5
                    Anything above 8.75 is automatically a +10kg addition to the next whole number
                */

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
            }
        });


    }

    public void calculateORM() {
        submit2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

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
                    result.setText((int)oneRepMax + " Kg" + "\n");
                    result.setTextSize(50);
                }

                //pound conversion
                convertedWeight = (int)oneRepMax * 2.20462;
                lastDigit = (int) convertedWeight;



                weight.setText(String.valueOf((int)oneRepMax)); // pre-set percentage calculator weight value to the ORM

                // show the picture of the weight
                image.setVisibility(View.VISIBLE);

                if(oneRepMax!=ormWeight) {
                    result.setText((int) oneRepMax + " Kgs" + "\n");
                    result.setTextSize(50);
                }
                LBresult.setText((df.format(lastDigit)) + " Lbs" + "\n");
                LBresult.setTextSize(50);

            }
        });
    }

}

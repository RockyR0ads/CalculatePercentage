package com.example.rocky.calculatepercentage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    double calculatedNumber = 0;
    double storedValue = 0;
    double percentage = 0;
    double finalNumber = 0;
    double lastDigit = 0;
    double rawFinal = 0;
    int intPart = 0;

    Boolean resetValues = false;

    DecimalFormat df = new DecimalFormat();

    Button submitButton;
    Button five;
    Button ten;
    Button fifteen;
    Button twenty;
    Button twentyFive;
    EditText editText;
    TextView result;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // locate elements on screen we need to work with
        submitButton = findViewById(R.id.submit);
        editText = findViewById(R.id.editText);
        result = findViewById(R.id.result);
        five = findViewById(R.id.five);
        ten = findViewById(R.id.ten);
        fifteen = findViewById(R.id.fifteen);
        twenty = findViewById(R.id.twenty);
        twentyFive = findViewById(R.id.twentyfive);
        image = findViewById(R.id.imageView);

        df.setMaximumFractionDigits(3);

        image.setVisibility(View.INVISIBLE);

        five.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                five.setBackgroundResource(R.color.buttonPressed);
                percentage = 5.0f;

            }
        });

        ten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ten.setBackgroundResource(R.color.buttonPressed);
                percentage = 10.0f;
            }
        });

        fifteen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fifteen.setBackgroundResource(R.color.buttonPressed);
                percentage = 15.0f;
            }
        });

        twenty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twenty.setBackgroundResource(R.color.buttonPressed);
                percentage = 20.0f;
            }
        });

        twentyFive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twentyFive.setBackgroundResource(R.color.buttonPressed);
                percentage = 25.0f;
            }
        });
        // reset application when attempting to calculate a new number
        editText.setOnClickListener(new View.OnClickListener() {
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



        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                storedValue = Float.parseFloat(editText.getText().toString().trim());
                image.setVisibility(View.VISIBLE);
                calculatedNumber = storedValue*(percentage/100.0f); // get the percentage
                finalNumber = storedValue-calculatedNumber; // get the weight i should be lifting
                rawFinal = finalNumber;
                intPart = (int) finalNumber;
                //storedDecimal = finalNumber - intPart; //get the decimal of the result

                /**
                                            ----- 10KG ROUNDING LOGIC------

                    To decide whether the weight will be flat value ending in 0 or if it will sit between 2.5 | 5 | 7.5
                    Below 1.25 is considered a round value of eg 80
                    Above 1.25 but less than 3.75 is considered a 2.5kg addition eg 82.5
                    Above 3.75 but less that 6.25 is considered a 5kg addition to the whole number eg 85
                    Above 6.75 but lower than 8.75 i considered a 7,5kg addition eg 87.5
                    Anything above 8.75 is automatically a +10kg addition to the next whole number
                */

                lastDigit = finalNumber % 10; // get the last digit as well as the decimals

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



}

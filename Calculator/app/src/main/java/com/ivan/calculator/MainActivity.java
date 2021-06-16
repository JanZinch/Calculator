package com.ivan.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final byte FIELD_SIZE = 15;

    private Calculator calc;
    private TextView ioField;

    private Button butC;
    private Button butLeftBracket;
    private Button butRightBracket;
    private Button but0;
    private Button but1;
    private Button but2;
    private Button but3;
    private Button but4;
    private Button but5;
    private Button but6;
    private Button but7;
    private Button but8;
    private Button but9;
    private Button butMultiple;
    private Button butDivide;
    private Button butPlus;
    private Button butMinus;
    private Button butSeparator;
    private Button butPow;
    private Button butEquals;


    private void AddSymbol(String symbol, boolean isOperator){

        if(ioField.length() == 0 && isOperator) return;
        if (ioField.length() < FIELD_SIZE) ioField.append(symbol);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calc = new Calculator();

        ioField = findViewById(R.id.exp_field);

        but0 = findViewById(R.id.but0);
        but1 = findViewById(R.id.but1);
        but2 = findViewById(R.id.but2);
        but3 = findViewById(R.id.but3);
        but4 = findViewById(R.id.but4);
        but5 = findViewById(R.id.but5);
        but6 = findViewById(R.id.but6);
        but7 = findViewById(R.id.but7);
        but8 = findViewById(R.id.but8);
        but9 = findViewById(R.id.but9);

        butC = findViewById(R.id.butC);
        butLeftBracket = findViewById(R.id.butLB);
        butRightBracket = findViewById(R.id.butRB);
        butMultiple = findViewById(R.id.butMULT);
        butDivide = findViewById(R.id.butDIV);
        butPlus = findViewById(R.id.butPLUS);
        butMinus = findViewById(R.id.butMINUS);
        butEquals = findViewById(R.id.butEQ);
        butPow = findViewById(R.id.butPOW);
        butSeparator = findViewById(R.id.butSepar);

    }

    @Override
    protected void onStart() {

        super.onStart();


        but0.setOnClickListener(v -> AddSymbol("0", false));
        but1.setOnClickListener(v -> AddSymbol("1", false));
        but2.setOnClickListener(v -> AddSymbol("2", false));
        but3.setOnClickListener(v -> AddSymbol("3", false));
        but4.setOnClickListener(v -> AddSymbol("4", false));
        but5.setOnClickListener(v -> AddSymbol("5", false));
        but6.setOnClickListener(v -> AddSymbol("6", false));
        but7.setOnClickListener(v -> AddSymbol("7", false));
        but8.setOnClickListener(v -> AddSymbol("8", false));
        but9.setOnClickListener(v -> AddSymbol("9", false));
        butC.setOnClickListener(v -> ioField.setText(""));

        butLeftBracket.setOnClickListener(v -> AddSymbol("(", false));
        butRightBracket.setOnClickListener( v -> AddSymbol(")", false));
        butMultiple.setOnClickListener( v -> AddSymbol("*", true));
        butDivide.setOnClickListener( v -> AddSymbol("/", true));
        butMinus.setOnClickListener(v ->{ AddSymbol("-", false);});

        butPlus.setOnClickListener(v -> AddSymbol("+", true));
        butPow.setOnClickListener(v -> AddSymbol("^", true));
        butSeparator.setOnClickListener(v ->{ AddSymbol(".", false);});

        View.OnClickListener clEQ = v -> {

            StringBuffer source = new StringBuffer(ioField.getText());

            calc.UpdateInput(source);

            char[] sourceArray = (source + "").toCharArray();


            Double result;
            int intResult;

            if (calc.CheckSource(sourceArray)){

                try{

                    result = calc.Calculation(sourceArray);


                    intResult = (int)result.doubleValue();
                    if(intResult >= Integer.MAX_VALUE - 2) throw new Exception("Value is very big");
                    else if (intResult <= Integer.MIN_VALUE + 2) throw new Exception("Value is very little");

                    if (result < 0){

                        if (result % 1 == 0){

                            ioField.setText("(" + result.intValue() + ")");
                        }
                        else {

                            ioField.setText("(" + result.floatValue() + ")");
                        }

                    }
                    else {

                        if (result % 1 == 0){

                            ioField.setText("" + result.intValue());
                        }
                        else {

                            ioField.setText("" + result.floatValue());
                        }
                    }

                }
                catch (SecurityException ex){

                    ioField.setText("Uncertainty ");

                }
                catch (NumberFormatException ex){

                    ioField.setText("Infinity");

                }
                catch (Exception ex){

                    ioField.setText(ex.getMessage());

                }

            }
            else {

                ioField.setText("Incorrect format");
            }

        };
        butEquals.setOnClickListener(clEQ);


    }


}
package com.example.mcalcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import ca.roumani.i2c.MPro;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.tts = new TextToSpeech(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View v) {

        MPro mp = new MPro();
        String princS = ((EditText) findViewById(R.id.pBox)).getText().toString();
        double princD = Double.parseDouble(princS);
        mp.setPrinciple(princS);
        String amorS = ((EditText) findViewById(R.id.aBox)).getText().toString();
        mp.setAmortization(amorS);
        int amorD = Integer.parseInt(amorS);
        String intS = ((EditText) findViewById(R.id.iBox)).getText().toString();
        mp.setInterest(intS);
        double intD = Double.parseDouble(intS);
        String s = "Monthly Payment = " + mp.computePayment("%,.2f");
        s += "\n\n";
        s += "By making this payment monthly for " + amorD;
        s += " years, the mortgage will be paid in full. " +
                "But if you terminate the mortgage on its nth anniversary, " +
                "the balance still owing depends on n as shown below: \n\n";
        for (int i = 0; i <= amorD; i++) {
            s+= String.format("%8d",i) + mp.outstandingAfter(i,"%,16.0f");
            s+= "\n\n";
            i = (i >= 5) ? i + 4 : i;
        }
        ((TextView) findViewById(R.id.output)).setText(s);
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void onInit(int initStatus)
    {
        this.tts.setLanguage(Locale.US);
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }
    public void onSensorChanged(SensorEvent event) {
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double a = Math.sqrt(ax*ax + ay*ay + az*az);
        if (a > 20) {
            ((EditText) findViewById(R.id.pBox)).setText("");
            ((EditText) findViewById(R.id.aBox)).setText("");
            ((EditText) findViewById(R.id.iBox)).setText("");
            ((TextView) findViewById(R.id.output)).setText("");
        }
    }

}
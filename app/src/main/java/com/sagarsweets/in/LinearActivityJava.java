package com.sagarsweets.in;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LinearActivityJava extends AppCompatActivity {

    Button btnCalc ;
    EditText edKmDriven, edAvg, edPriceOil;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_linear_java);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnCalc = findViewById(R.id.btnCalc);
        edPriceOil = findViewById(R.id.edPriceOil);
        edAvg = findViewById(R.id.edAvg);
        edKmDriven = findViewById(R.id.edKmDriven);
        txtResult = findViewById(R.id.txtResult);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer p = Integer.parseInt(edPriceOil.getText().toString());
                Integer a = Integer.parseInt(edAvg.getText().toString());
                Integer k = Integer.parseInt(edKmDriven.getText().toString());
                Integer res = (k/a)*p;
                txtResult.setText(" "+ res +" INR");
            }
        });
    }

}
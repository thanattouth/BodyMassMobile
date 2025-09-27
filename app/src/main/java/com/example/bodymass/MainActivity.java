package com.example.bodymass;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText edtWeight = findViewById(R.id.edtWeight);
        final EditText edtHeight = findViewById(R.id.edtHeight);
        final Button btnCalculate = findViewById(R.id.btnCalculate);
        final TextView txtBMIResult = findViewById(R.id.txtBMIResult);
        final TextView txtBMICategory = findViewById(R.id.txtBMICategory);

        btnCalculate.setOnClickListener(v -> {
            try {
                // input weight and height
                String weightStr = edtWeight.getText().toString().trim();
                String heightStr = edtHeight.getText().toString().trim();

                // chack speacbar
                if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
                    double weight = Double.parseDouble(weightStr);
                    double height = Double.parseDouble(heightStr) / 100.0; // cm -> m

                    // find BMI
                    double bmi = weight / (height * height);

                    // show BMI
                    txtBMIResult.setText(String.format("%.2f", bmi));
                } else {
                    txtBMIResult.setText("Incomplete information filled in");
                }
            } catch (Exception e) {
                txtBMIResult.setText("Incorrect information");
            }
            // === Person3: เขียนโค้ดแสดงผลลัพธ์และ Category ตรงนี้ ===
            // txtBMIResult.setText(String.format("%.2f", bmi));
            // txtBMICategory.setText(getBMICategory(bmi));

        });
    }

    // === Person3: Helper function สำหรับแสดงผล Category ===
    // เช่น "Underweight", "Normal", "Overweight", "Obese"
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return getString(R.string.underweight);
        else if (bmi < 25) return getString(R.string.normal);
        else if (bmi < 30) return getString(R.string.overweight);
        else return getString(R.string.obese);
    }
}
package com.example.bodymass;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.res.Configuration; // 👈 Apawan Kongkanan: import Configuration สำหรับ onConfigurationChanged

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
                    txtBMICategory.setText(getBMICategory(bmi));
                } else {
                    txtBMIResult.setText("Incomplete information filled in");
                    txtBMICategory.setText("");
                }
            } catch (Exception e) {
                txtBMIResult.setText("Incorrect information");
                txtBMICategory.setText("");
            }
        });

        // ถ้ามีการ restore state หลัง rotate/เปลี่ยนภาษา
        if (savedInstanceState != null) {
            txtBMIResult.setText(savedInstanceState.getString("bmi_result", getString(R.string.defaultBMI)));
            txtBMICategory.setText(savedInstanceState.getString("bmi_category", getString(R.string.defaultCategory)));
        }
    }

    // เก็บค่า BMI และ Category ก่อนที่ Activity จะถูก destroy
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        final TextView txtBMIResult = findViewById(R.id.txtBMIResult);
        final TextView txtBMICategory = findViewById(R.id.txtBMICategory);
        outState.putString("bmi_result", txtBMIResult.getText().toString());
        outState.putString("bmi_category", txtBMICategory.getText().toString());
    }

    // === Person3: Helper function สำหรับแสดงผล Category ===
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return getString(R.string.underweight);
        else if (bmi < 25) return getString(R.string.normal);
        else if (bmi < 30) return getString(R.string.overweight);
        else return getString(R.string.obese);
    }

    // : รองรับ Runtime Changes (ผู้ใช้เปลี่ยน Font size หรือหมุนจอ)
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // : เมื่อผู้ใช้เปลี่ยน Font size ใน Settings ของเครื่อง
        // ให้รีโหลด Activity เพื่ออัปเดตขนาดตัวอักษรทั้งหมดอัตโนมัติ
        if (newConfig.fontScale != 1f) {
            recreate();
        }
    }

}

package com.example.bodymass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.res.Configuration; // 👈 Apawan Kongkanan: import Configuration สำหรับ onConfigurationChanged

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
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

        edtWeight.setFilters(new InputFilter[]{ new DecimalDigitsInputFilter(4, 3) });
        edtHeight.setFilters(new InputFilter[]{ new DecimalDigitsInputFilter(4, 3) });

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
                    txtBMIResult.setText(String.format("%,.2f", bmi));
                    txtBMICategory.setText(getBMICategory(bmi));

                    int color;
                    if (bmi < 18.5) {
                        color = ContextCompat.getColor(this, R.color.bmi_underweight); // สีฟ้า
                    } else if (bmi < 25) {
                        color = ContextCompat.getColor(this, R.color.bmi_normal);      // สีเขียว
                    } else if (bmi < 30) {
                        color = ContextCompat.getColor(this, R.color.bmi_overweight); // สีส้ม
                    } else {
                        color = ContextCompat.getColor(this, R.color.bmi_obese);       // สีแดง
                    }
                    txtBMICategory.setTextColor(color);
                } else {
                    txtBMIResult.setText("Incomplete information filled in");
                    txtBMICategory.setText("-");
                }
            } catch (Exception e) {
                txtBMIResult.setText("Incorrect information");
                txtBMICategory.setText("Incorrect information");
            }
        });

        // ถ้ามีการ restore state หลัง rotate/เปลี่ยนภาษา
        if (savedInstanceState != null) {
            // คืนค่าข้อความ (เหมือนเดิม)
            txtBMIResult.setText(savedInstanceState.getString("bmi_result", getString(R.string.defaultBMI)));
            txtBMICategory.setText(savedInstanceState.getString("bmi_category", getString(R.string.defaultCategory)));

            // เพิ่มการคืนค่าสีให้กับ Category
            // ดึงค่าสีที่บันทึกไว้ หากไม่มี ให้ใช้สีดำเป็นค่าเริ่มต้น
            int color = savedInstanceState.getInt("bmi_category_color", ContextCompat.getColor(this, android.R.color.black));
            txtBMICategory.setTextColor(color);
        }
    }

    // เก็บค่า BMI และ Category ก่อนที่ Activity จะถูก destroy
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        final TextView txtBMIResult = findViewById(R.id.txtBMIResult);
        final TextView txtBMICategory = findViewById(R.id.txtBMICategory);

        // บันทึกข้อความ (เหมือนเดิม)
        outState.putString("bmi_result", txtBMIResult.getText().toString());
        outState.putString("bmi_category", txtBMICategory.getText().toString());

        // เพิ่มการบันทึกสีปัจจุบันของ Category
        outState.putInt("bmi_category_color", txtBMICategory.getCurrentTextColor());
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

package com.example.electricbillestimate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView txtDetailMonth, txtDetailUnit, txtDetailTotal, txtDetailRebate, txtDetailFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize views
        txtDetailMonth = findViewById(R.id.txtDetailMonth);
        txtDetailUnit = findViewById(R.id.txtDetailUnit);
        txtDetailTotal = findViewById(R.id.txtDetailTotal);
        txtDetailRebate = findViewById(R.id.txtDetailRebate);
        txtDetailFinal = findViewById(R.id.txtDetailFinal);

        // Set up back button to simply finish this activity
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Get Bill object from intent and display details
        Bill bill = (Bill) getIntent().getSerializableExtra("bill");

        if (bill != null) {
            txtDetailMonth.setText(bill.getMonth());
            txtDetailUnit.setText(bill.getUnitUsed() + " kWh");
            txtDetailTotal.setText("RM " + String.format("%.2f", bill.getTotalCharges()));

            // Calculate and display the rebate as a percentage
            if (bill.getTotalCharges() > 0) {
                double rebatePercent = (bill.getRebate() / bill.getTotalCharges()) * 100;
                txtDetailRebate.setText(String.format("%.0f%%", rebatePercent));
            } else {
                txtDetailRebate.setText("0%");
            }

            txtDetailFinal.setText("RM " + String.format("%.2f", bill.getFinalCost()));
        }
    }
}
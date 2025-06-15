package com.example.electricbillestimate.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.electricbillestimate.Bill;
import com.example.electricbillestimate.DBHelper;
import com.example.electricbillestimate.R;

public class HomeFragment extends Fragment {

    Spinner spinnerMonth;
    EditText editUnit;
    SeekBar seekRebate;
    TextView txtRebateValue;

    Button btnCalculate;
    DBHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        editUnit = view.findViewById(R.id.editUnit);
        seekRebate = view.findViewById(R.id.seekRebate);
        txtRebateValue = view.findViewById(R.id.txtRebateValue);
        btnCalculate = view.findViewById(R.id.btnCalculate);


        dbHelper = new DBHelper(getContext());

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        seekRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRebateValue.setText("Rebate: " + progress + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnCalculate.setOnClickListener(v -> {
            String month = spinnerMonth.getSelectedItem().toString();
            String unitStr = editUnit.getText().toString().trim();

            if (unitStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter unit used", Toast.LENGTH_SHORT).show();
                return;
            }

            int unit = Integer.parseInt(unitStr);
            double totalCharges = calculateCharges(unit);
            double rebatePercentage = seekRebate.getProgress();
            double rebate = totalCharges * (rebatePercentage / 100.0);
            double finalCost = totalCharges - rebate;

            new AlertDialog.Builder(getContext())
                    .setTitle("Save Calculation")
                    .setMessage("Do you want to save this bill for " + month + "?")
                    .setPositiveButton("Save", (dialog, which) -> {
                        if (dbHelper.isMonthExists(month)) {
                            Toast.makeText(getContext(), "A bill for this month already exists.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bill bill = new Bill(0, month, unit, totalCharges, rebate, finalCost);
                        dbHelper.insertBill(bill);

                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View currentFocus = requireActivity().getCurrentFocus();
                        if (imm != null && currentFocus != null) {
                            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        }

                        editUnit.setText("");
                        Toast.makeText(getContext(), "Calculation saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    private double calculateCharges(int unit) {
        double total = 0;
        if (unit <= 200) total = unit * 0.218;
        else if (unit <= 300) total = (200 * 0.218) + ((unit - 200) * 0.334);
        else if (unit <= 600) total = (200 * 0.218) + (100 * 0.334) + ((unit - 300) * 0.516);
        else if (unit <= 900) total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unit - 600) * 0.546);
        else total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + (300 * 0.546) + ((unit - 900) * 0.571);
        return total;
    }
}
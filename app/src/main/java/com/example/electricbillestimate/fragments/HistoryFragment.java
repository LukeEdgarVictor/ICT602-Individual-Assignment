package com.example.electricbillestimate.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.electricbillestimate.Bill;
import com.example.electricbillestimate.BillAdapter;
import com.example.electricbillestimate.DBHelper;
import com.example.electricbillestimate.DetailActivity;
import com.example.electricbillestimate.R;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    ListView listView;
    BillAdapter adapter;
    ArrayList<Bill> billList;
    DBHelper dbHelper;

    Button btnDeleteAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = view.findViewById(R.id.historyListView);

        dbHelper = new DBHelper(getContext());
        billList = dbHelper.getAllBills();

        adapter = new BillAdapter(getContext(), billList);
        listView.setAdapter(adapter);

        btnDeleteAll = view.findViewById(R.id.btnDeleteAll);

        // Item click: show bill details
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Bill selectedBill = billList.get(position);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("bill", selectedBill);
            startActivity(intent);
        });

        // Optional: long click to delete
        listView.setOnItemLongClickListener((parent, view1, position, id) -> {
            Bill billToDelete = billList.get(position);
            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Delete Bill")
                    .setMessage("Are you sure you want to delete this bill?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteBillById(billToDelete.getId());
                        billList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Bill deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        btnDeleteAll.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete All")
                    .setMessage("Are you sure you want to delete all bills?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteAllBills();
                        Toast.makeText(getContext(), "All bills deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return view;
    }
}
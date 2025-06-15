package com.example.electricbillestimate;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

public class BillAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bill> bills;

    public BillAdapter(Context context, ArrayList<Bill> bills) {
        this.context = context;
        this.bills = bills;
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int position) {
        return bills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bills.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bill bill = bills.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView txtMonth = convertView.findViewById(R.id.txtMonth);
        TextView txtFinalCost = convertView.findViewById(R.id.txtFinalCost);

        txtMonth.setText(bill.getMonth());
        txtFinalCost.setText(String.format("Final Cost: RM %.2f", bill.getFinalCost()));

        return convertView;
    }
}
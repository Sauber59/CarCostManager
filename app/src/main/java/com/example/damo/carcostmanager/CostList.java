package com.example.damo.carcostmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CostList extends ArrayAdapter<Cost> {

    private Activity context;
    private List<Cost> costList;

    public CostList(Activity context, List<Cost> costList) {
        super(context, R.layout.list_layout, costList);
        this.context = context;
        this.costList = costList;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView dataTV = (TextView)listViewItem.findViewById(R.id.dataTV);
        TextView costTV = (TextView)listViewItem.findViewById(R.id.costTV);
        TextView quantityTV = (TextView)listViewItem.findViewById(R.id.quantityTV);
        TextView distanceTV = (TextView)listViewItem.findViewById(R.id.distanceTV);

        Cost cost = costList.get(position);

        dataTV.setText(cost.getData());
        costTV.setText(cost.getCost() + " zł");
        quantityTV.setText(cost.getQuantity() + " l");
        distanceTV.setText(cost.getDistance() + " km");

        return listViewItem;
    }
}

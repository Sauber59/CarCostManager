package com.example.damo.carcostmanager.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.damo.carcostmanager.R;
import com.example.damo.carcostmanager.classes.Cost;

import java.util.List;

/**adpater wykorzystywany pzy wyswieltaniu krótkich list pobranych danych z bazy danych,
takich jak na przyklad informacje o serwisach, przegladach i ubezpieczeniach
 */

public class CostShortAdpater extends ArrayAdapter<Cost> {

    private Activity context;
    private List<Cost> costShortList;

    public CostShortAdpater(Activity context, List<Cost> costShortList) {
        super(context, R.layout.list_short_layout, costShortList);
        this.context = context;
        this.costShortList = costShortList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_short_layout, null, true);

       TextView dataTV = (TextView) listViewItem.findViewById(R.id.dataTV);
        TextView costShortTV = (TextView) listViewItem.findViewById(R.id.costShortTV);
        TextView commentTV = (TextView) listViewItem.findViewById(R.id.commentTV);

        Cost cost = costShortList.get(position);

        dataTV.setText(cost.getData());
        costShortTV.setText(Float.toString(cost.getCost()) + " zł");
        commentTV.setText(cost.getComments());



        return listViewItem;
    }
}

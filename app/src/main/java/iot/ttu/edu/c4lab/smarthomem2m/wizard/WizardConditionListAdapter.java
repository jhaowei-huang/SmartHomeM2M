package iot.ttu.edu.c4lab.smarthomem2m.wizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import iot.ttu.edu.c4lab.smarthomem2m.R;
import iot.ttu.edu.c4lab.smarthomem2m.data.Rule;

/**
 * Created by jhaowei on 2016/11/11.
 */

public class WizardConditionListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Rule.Condition> conditions;

    public WizardConditionListAdapter(Context context, ArrayList<Rule.Condition> conditions) {
        super();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conditions = conditions;
    }

    @Override
    public int getCount() {
        return conditions.size();
    }

    @Override
    public Object getItem(int i) {
        return conditions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        View item = layoutInflater.inflate(R.layout.wizard_step2_condition_listitem, viewGroup, false);
        TextView textView_condition = (TextView) item.findViewById(R.id.textView_condition);
        textView_condition.setText(conditions.get(i).text);

        Button btn_remove = (Button) item.findViewById(R.id.button_remove);
        btn_remove.setOnClickListener(view -> {
            conditions.remove(getItem(i));
            notifyDataSetChanged();
        });
        return item;
    }
}

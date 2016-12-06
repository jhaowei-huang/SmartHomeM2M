package iot.ttu.edu.c4lab.smarthomem2m;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import iot.ttu.edu.c4lab.smarthomem2m.data.Rule;
import iot.ttu.edu.c4lab.smarthomem2m.wizard.WizardActivity;

/**
 * Created by jhaowei on 2016/11/11.
 */

public class RuleListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;

    public RuleListAdapter(Context context) {
        super();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return M2MCoapClient.ruleMap.size();
    }

    @Override
    public Object getItem(int i) {
        Rule rule = (Rule) M2MCoapClient.ruleMap.values().toArray()[i];
        return rule.getRuleName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View ruleItem = layoutInflater.inflate(R.layout.rule_item, parent, false);
        TextView textView_ruleName = (TextView) ruleItem.findViewById(R.id.textView_ruleListId);
        textView_ruleName.setText(getItem(i).toString());
        ImageButton button_edit = (ImageButton) ruleItem.findViewById(R.id.button_edit);
        ImageButton button_remove = (ImageButton) ruleItem.findViewById(R.id.button_remove);
//        Switch switch_enable = (Switch) ruleItem.findViewById(R.id.switch_enable);
//        switch_enable.setChecked(true);

        button_edit.setOnClickListener(event -> {
            Snackbar.make(parent, "press " + i + " edit",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            Intent wizardIntent = new Intent(context, WizardActivity.class);
            wizardIntent.putExtra("requestCode", SmartHomeActivity.EDIT_RULE);
            wizardIntent.putExtra("ruleName", getItem(i).toString());
            ((Activity) context).startActivityForResult(wizardIntent, SmartHomeActivity.EDIT_RULE);

            RuleListAdapter.this.notifyDataSetChanged();
        });

        button_remove.setOnClickListener(event -> {
            Snackbar.make(parent, "press " + i + " remove",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            if (M2MCoapClientService.loopTask != null && M2MCoapClientService.loopTask.isRunning())
                return;

            TextView textView_ruleListId = (TextView) ruleItem.findViewById(R.id.textView_ruleListId);
            String key = textView_ruleListId.getText().toString();
            M2MCoapClient.ruleMap.remove(key);
            RuleListAdapter.this.notifyDataSetChanged();
        });

        return ruleItem;
    }
}

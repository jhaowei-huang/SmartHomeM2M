package iot.ttu.edu.c4lab.smarthomem2m.wizard;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import iot.ttu.edu.c4lab.smarthomem2m.M2MCoapClient;
import iot.ttu.edu.c4lab.smarthomem2m.R;
import iot.ttu.edu.c4lab.smarthomem2m.data.Rule;

/**
 * Created by jhaowei on 2016-10-31.
 */

public class WizardFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";
    private static View[] steps = new View[WizardActivity.TOTAL_STEP];
    private static Rule rule;

    public static void init(Rule rule) {
        WizardFragment.rule = rule;
        steps = new View[WizardActivity.TOTAL_STEP];
    }

    public static WizardFragment newInstance(int sectionNumber) {
        WizardFragment fragment = new WizardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int currentPage = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = null;

        if (steps[currentPage] != null)
            return steps[currentPage];

        switch (currentPage) {
            case 0:
                steps[currentPage] = rootView = inflater.inflate(R.layout.wizard_step1, container, false);
                initStep1();
                break;
            case 1:
                steps[currentPage] = rootView = inflater.inflate(R.layout.wizard_step2, container, false);
                initStep2();
                break;
            case 2:
                steps[currentPage] = rootView = inflater.inflate(R.layout.wizard_step3, container, false);
                initStep3();
                break;
            case 3:
                steps[currentPage] = rootView = inflater.inflate(R.layout.wizard_step4, container, false);
                initStep4();
                break;
            default:
                break;
        }

        return rootView;
    }

    public static View getPage(int index) {
        return steps[index];
    }

    private void initStep1() {
        EditText editText_ruleName = (EditText) steps[0].findViewById(R.id.editText_ruleName);
        TextView textView_error = (TextView) steps[0].findViewById(R.id.textView_error);
        editText_ruleName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() <= 0) {
                    textView_error.setVisibility(View.VISIBLE);
                    rule.setRuleName("");
                } else {
                    textView_error.setVisibility(View.INVISIBLE);
                    rule.setRuleName(editText_ruleName.getText().toString());
                }
            }
        });
    }

    private void initStep2() {
        Button btn_chooseDevice = (Button) steps[1].findViewById(R.id.btn_chooseDevice);
        Button btn_chooseResource = (Button) steps[1].findViewById(R.id.btn_chooseResource);
        Button btn_chooseOperator = (Button) steps[1].findViewById(R.id.btn_chooseOperator);
        Button btn_inputValue = (Button) steps[1].findViewById(R.id.btn_inputValue);
        TextView textView_result = (TextView) steps[1].findViewById(R.id.textView_result);
        Button btn_addCondition = (Button) steps[1].findViewById(R.id.btn_addCondition);
        ListView listView_conditions = (ListView) steps[1].findViewById(R.id.listView_conditions);
        WizardConditionListAdapter adapter = new WizardConditionListAdapter(this.getContext(), rule.getConditions());

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Object[] obj = (Object[]) msg.obj;
                if (obj[0].getClass().isEnum()) {
                    if (obj[0].equals(WizardDialogBuilder.TYPE.DEVICE)) {
                        btn_chooseDevice.setText((String) obj[1]);
                    }
                    if (obj[0].equals(WizardDialogBuilder.TYPE.RESOURCE_SENSOR)) {
                        btn_chooseResource.setText((String) obj[1]);
                    }
                    if (obj[0].equals(WizardDialogBuilder.TYPE.OPERATOR)) {
                        btn_chooseOperator.setText((String) obj[1]);
                    }
                    if (obj[0].equals(WizardDialogBuilder.TYPE.INTPUT_VALUE)) {
                        btn_inputValue.setText((String) obj[1]);
                    }

                    textView_result.setText(parseCondition(
                            btn_chooseDevice.getText().toString(),
                            btn_chooseResource.getText().toString(),
                            btn_chooseOperator.getText().toString(),
                            btn_inputValue.getText().toString()));
                }
            }
        };

        btn_chooseDevice.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.DEVICE, handler, null);
            dialogBuilder.showDialog();
        });

        btn_chooseResource.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.RESOURCE_SENSOR, handler, ((String) btn_chooseDevice.getText()).replaceAll(getString(R.string.smartHomeAddRuleWizard_step2_chooseDevice), ""));
            dialogBuilder.showDialog();
        });

        btn_chooseOperator.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.OPERATOR, handler, null);
            dialogBuilder.showDialog();
        });

        btn_inputValue.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.INTPUT_VALUE, handler, null);
            dialogBuilder.showDialog();
        });

        btn_addCondition.setOnClickListener(view -> {
            String text = textView_result.getText().toString();
            if (text.equals(getString(R.string.smartHomeAddRuleWizard_step2_result)))
                return;

            String op = "";
            String[] ops = getResources().getStringArray(R.array.operator);

            if (btn_chooseOperator.getText().toString().equals(ops[0])) {
                op = ">";
            } else if (btn_chooseOperator.getText().toString().equals(ops[1])) {
                op = "=";
            } else if (btn_chooseOperator.getText().toString().equals(ops[2])) {
                op = "<";
            }

            rule.addCondition("AND",
                    removePrefix(btn_chooseDevice.getText().toString()),
                    removePrefix(btn_chooseResource.getText().toString()),
                    op,
                    removePrefix(btn_inputValue.getText().toString()),
                    text);

            listView_conditions.setAdapter(adapter);
            listView_conditions.deferNotifyDataSetChanged();
        });
    }

    private String parseCondition(String device, String resource, String op, String value) {
        try {
            device = device.substring(device.indexOf("(") + 1, device.indexOf(")"));
            resource = resource.substring(resource.indexOf("(") + 1, resource.indexOf(")"));
            value = value.substring(value.indexOf(":") + 1);

            if (device.equals("") || resource.equals("") || value.equals(""))
                return getString(R.string.smartHomeAddRuleWizard_step2_result);
            else
                return device + resource + op + value;
        } catch (Exception e) {
            return getString(R.string.smartHomeAddRuleWizard_step2_result);
        }
    }

    private void initStep3() {
        Button btn_chooseDevice = (Button) steps[2].findViewById(R.id.btn_chooseDevice);
        Button btn_chooseResource = (Button) steps[2].findViewById(R.id.btn_chooseResource);
        Button btn_inputValue = (Button) steps[2].findViewById(R.id.btn_inputValue);
        TextView textView_result = (TextView) steps[2].findViewById(R.id.textView_result);
        Button btn_addAction = (Button) steps[2].findViewById(R.id.btn_addAction);
        ListView listView_actions = (ListView) steps[2].findViewById(R.id.listView_actions);
        WizardActionListAdapter adapter = new WizardActionListAdapter(this.getContext(), rule.getActions());

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Object[] obj = (Object[]) msg.obj;
                if (obj[0].getClass().isEnum()) {
                    if (obj[0].equals(WizardDialogBuilder.TYPE.DEVICE)) {
                        btn_chooseDevice.setText((String) obj[1]);
                    }
                    if (obj[0].equals(WizardDialogBuilder.TYPE.RESOURCE_ACTUATOR)) {
                        btn_chooseResource.setText((String) obj[1]);
                    }
                    if (obj[0].equals(WizardDialogBuilder.TYPE.INTPUT_VALUE)) {
                        btn_inputValue.setText((String) obj[1]);
                    }

                    textView_result.setText(parseAction(
                            btn_chooseDevice.getText().toString(),
                            btn_chooseResource.getText().toString(),
                            btn_inputValue.getText().toString()));
                }
            }
        };

        btn_chooseDevice.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.DEVICE, handler, null);
            dialogBuilder.showDialog();
        });

        btn_chooseResource.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.RESOURCE_ACTUATOR, handler, ((String) btn_chooseDevice.getText()).replaceAll(getString(R.string.smartHomeAddRuleWizard_step2_chooseDevice), ""));
            dialogBuilder.showDialog();
        });

        btn_inputValue.setOnClickListener(view -> {
            WizardDialogBuilder dialogBuilder = new WizardDialogBuilder(getActivity(), WizardDialogBuilder.TYPE.INTPUT_VALUE, handler, null);
            dialogBuilder.showDialog();
        });

        btn_addAction.setOnClickListener(view -> {
            String text = textView_result.getText().toString();
            if (text.equals(getString(R.string.smartHomeAddRuleWizard_step3_result)))
                return;

            rule.addAction(removePrefix(btn_chooseDevice.getText().toString()),
                    removePrefix(btn_chooseResource.getText().toString()),
                    removePrefix(btn_inputValue.getText().toString()),
                    textView_result.getText().toString());

            listView_actions.setAdapter(adapter);
            listView_actions.deferNotifyDataSetChanged();
        });

    }

    private String parseAction(String device, String resource, String value) {
        try {
            device = device.substring(device.indexOf("(") + 1, device.indexOf(")"));
            resource = resource.substring(resource.indexOf("(") + 1, resource.indexOf(")"));
            value = value.substring(value.indexOf(":") + 1);

            if (device.equals("") || resource.equals("") || value.equals(""))
                return getString(R.string.smartHomeAddRuleWizard_step3_result);
            else
                return device + resource + getString(R.string.smartHomeAddRuleWizard_step3_set) + value;
        } catch (Exception e) {
            return getString(R.string.smartHomeAddRuleWizard_step3_result);
        }
    }

    private void initStep4() {
        Button button_refresh = (Button) steps[3].findViewById(R.id.button_refresh);
        Button button_addRule = (Button) steps[3].findViewById(R.id.button_addRule);

        button_refresh.setOnClickListener(view -> {
            updateResult();
        });

        button_addRule.setOnClickListener(view -> {
            if (rule.getRuleName() == null || rule.getRuleName().equals(""))
                Toast.makeText(getContext(), R.string.smartHomeAddRuleWizard_step1_error, Toast.LENGTH_SHORT).show();
            else if (rule.getConditions().size() <= 0)
                Toast.makeText(getContext(), R.string.smartHomeAddRuleWizard_step2_error, Toast.LENGTH_SHORT).show();
            else if (rule.getActions().size() <= 0)
                Toast.makeText(getContext(), R.string.smartHomeAddRuleWizard_step3_error, Toast.LENGTH_SHORT).show();
            else if (M2MCoapClient.ruleMap.get(rule.getRuleName()) != null)
                Toast.makeText(getContext(), R.string.smartHomeAddRuleWizard_step4_error, Toast.LENGTH_SHORT).show();
            else {
                M2MCoapClient.ruleMap.put(rule.getRuleName(), rule);
                Message message = new Message();
                message.setTarget(WizardActivity.wizardHandler);
                message.sendToTarget();
            }
        });

        updateResult();
    }

    public void updateResult() {
        if (steps[3] == null) {
            return;
        }

        TextView textView_ruleName = (TextView) steps[3].findViewById(R.id.textView_ruleName);
        TextView textView_condition = (TextView) steps[3].findViewById(R.id.textView_condition);
        TextView textView_action = (TextView) steps[3].findViewById(R.id.textView_action);

        textView_ruleName.setText(getActivity().getString(R.string.smartHomeAddRuleWizard_step4_ruleName) +
                rule.getRuleName());

        String conditionText = getActivity().getString(R.string.smartHomeAddRuleWizard_step4_condition) + "\n";
        for (Rule.Condition c : rule.getConditions()) {
            conditionText += c.text + "\n";
        }
        textView_condition.setText(conditionText);

        String actionText = getActivity().getString(R.string.smartHomeAddRuleWizard_step4_action) + "\n";
        for (Rule.Action a : rule.getActions()) {
            actionText += a.text + "\n";
        }
        textView_action.setText(actionText);
    }

    private String removePrefix(String string) {
        return string.substring(string.indexOf(":") + 1);
    }
}
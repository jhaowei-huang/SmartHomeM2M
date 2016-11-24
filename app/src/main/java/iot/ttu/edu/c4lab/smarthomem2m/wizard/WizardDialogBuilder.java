package iot.ttu.edu.c4lab.smarthomem2m.wizard;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import iot.ttu.edu.c4lab.smarthomem2m.M2MCoapClient;
import iot.ttu.edu.c4lab.smarthomem2m.R;

/**
 * Created by jhaowei on 2016-11-01.
 */

public class WizardDialogBuilder extends AlertDialog.Builder {
    private static AlertDialog alertDialog = null;

    public enum TYPE {
        DEVICE, RESOURCE_SENSOR, RESOURCE_ACTUATOR, OPERATOR, INTPUT_VALUE
    }

    public WizardDialogBuilder(Context context) {
        super(context);
    }

    public WizardDialogBuilder(Context context, TYPE id, Handler responseHandler, String key) {
        super(context);

        if (id == TYPE.DEVICE) {
            setChooseDeviceDialog(responseHandler);
        } else if (id == TYPE.RESOURCE_SENSOR) {
            setChooseResourceDialog(responseHandler, key, true);
        } else if (id == TYPE.RESOURCE_ACTUATOR) {
            setChooseResourceDialog(responseHandler, key, false);
        } else if (id == TYPE.OPERATOR) {
            setChooseOperatorDialog(responseHandler);
        } else if (id == TYPE.INTPUT_VALUE) {
            setInputValueDialog(responseHandler);
        }
    }

    private void setChooseDeviceDialog(Handler responseHandler) {
        setTitle(R.string.smartHomeAddRuleWizard_step2_chooseDevice);
        setSingleChoiceItems(M2MCoapClient.deviceStringArray, 0, null);
        setPositiveButton(R.string.dialog_dismiss_confirm_postive, (dialogInterface, i) -> {
            try {
                ListView lv = ((AlertDialog) dialogInterface).getListView();
                Object checkedItem = lv.getAdapter().getItem(lv.getCheckedItemPosition());
                Message msg = new Message();
                msg.obj = new Object[]{
                        TYPE.DEVICE,
                        getContext().getString(R.string.smartHomeAddRuleWizard_step2_chooseDevice) + checkedItem};
                msg.setTarget(responseHandler);
                msg.sendToTarget();
            } catch (Exception e) {
                return;
            }
        });

        setNegativeButton(R.string.dialog_dismiss_confirm_negative, null);

        alertDialog = create();
    }

    private void setChooseResourceDialog(Handler responseHandler, String key, boolean isSensor) {
        setTitle(R.string.smartHomeAddRuleWizard_step2_chooseResource);
        setSingleChoiceItems(M2MCoapClient.getResourceStringArray(key, isSensor), 0, null);
        setPositiveButton(R.string.dialog_dismiss_confirm_postive, (dialogInterface, i) -> {
            try {
                ListView lv = ((AlertDialog) dialogInterface).getListView();
                Object checkedItem = lv.getAdapter().getItem(lv.getCheckedItemPosition());

                Message msg = new Message();
                if (isSensor) {
                    msg.obj = new Object[]{
                            TYPE.RESOURCE_SENSOR,
                            getContext().getString(R.string.smartHomeAddRuleWizard_step2_chooseResource) + checkedItem
                    };
                } else {
                    msg.obj = new Object[]{
                            TYPE.RESOURCE_ACTUATOR,
                            getContext().getString(R.string.smartHomeAddRuleWizard_step2_chooseResource) + checkedItem
                    };
                }
                msg.setTarget(responseHandler);
                msg.sendToTarget();
            } catch (Exception e) {
                return;
            }
        });

        setNegativeButton(R.string.dialog_dismiss_confirm_negative, null);

        alertDialog = create();
    }

    private void setChooseOperatorDialog(Handler responseHandler) {
        String[] op = getContext().getResources().getStringArray(R.array.operator);
        setSingleChoiceItems(op, 0, null);
        setPositiveButton(R.string.dialog_dismiss_confirm_postive, (dialogInterface, i) -> {
            try {
                ListView lv = ((AlertDialog) dialogInterface).getListView();
                Object checkedItem = lv.getAdapter().getItem(lv.getCheckedItemPosition());

                Message msg = new Message();
                msg.obj = new Object[]{TYPE.OPERATOR, checkedItem};
                msg.setTarget(responseHandler);
                msg.sendToTarget();
            } catch (Exception e) {
                return;
            }
        });

        setNegativeButton(R.string.dialog_dismiss_confirm_negative, null);

        alertDialog = create();
    }

    private void setInputValueDialog(Handler responseHandler) {
        setTitle(R.string.smartHomeAddRuleWizard_step2_inputValue);
        final EditText input = new EditText(getContext());

        setPositiveButton(R.string.dialog_dismiss_confirm_postive, (dialogInterface, i) -> {
            Message msg = new Message();
            msg.obj = new Object[]{
                    TYPE.INTPUT_VALUE,
                    getContext().getString(R.string.smartHomeAddRuleWizard_step2_inputValue) + input.getText().toString()};
            msg.setTarget(responseHandler);
            msg.sendToTarget();
        });

        setNegativeButton(R.string.dialog_dismiss_confirm_negative, null);
//        input.setBackgroundResource(android.R.color.transparent);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) { // Accept only letter & digits ; otherwise just return
                        return "";
                    }
                }

                Button btn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setEnabled(true);
                return null;
            }
        };

        input.setFilters(new InputFilter[]{filter});
        setView(input);
        alertDialog = create();
    }

    public void showDialog() {
        alertDialog.show();
    }
}

package iot.ttu.edu.c4lab.smarthomem2m.data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Rule implements Serializable {
    private String ruleName = "";
    private ArrayList<Condition> conditions;
    private ArrayList<Action> actions;
    private final String regex = "[(].*[)]";
    private final String[] logs = {"AND", "OR"};
    private final String[] ops = {">", "<", "=", "!=", ">=", "<="};

    public Rule() {
        this.ruleName = "";
        conditions = new ArrayList<Condition>();
        actions = new ArrayList<Action>();
    }

    public Rule(String ruleName) {
        this.ruleName = ruleName;
        conditions = new ArrayList<Condition>();
        actions = new ArrayList<Action>();
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void addCondition(String logic, String sensorId, String resourceId, String op, String value, String text) {
        sensorId = sensorId.replaceAll(regex, "");
        resourceId = resourceId.replaceAll(regex, "");

        Log.d("MainService", sensorId + " , " + resourceId);
        Condition c = new Condition(logic, sensorId, resourceId, op, value);
        c.addConditionText(text);
        conditions.add(c);
    }

    public void addCondition(String[] condition) {
        if (condition.length != 5) {
            return;
        }

        conditions.add(new Condition(condition[0],
                condition[1].replaceAll(regex, ""),
                condition[2].replaceAll(regex, ""),
                condition[3],
                condition[4]));
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public void addAction(String actuatorId, String resourceId, String value, String text) {
        actuatorId = actuatorId.replaceAll(regex, "");
        resourceId = resourceId.replaceAll(regex, "");

        Action a = new Action(actuatorId, resourceId, value);
        a.addActionText(text);
        actions.add(a);

        Log.d("MainService", actuatorId + " , " + resourceId);
    }

    public void addAction(String[] action) {
        actions.add(new Action(action[0].replaceAll(regex, ""),
                action[1].replaceAll(regex, ""),
                action[2]));
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public class Condition implements Serializable {
        public String logic;
        public String sensorId;
        public String resourceId;
        public String op;
        public String value;
        public boolean enable = false;
        public String text = "";

        public Condition(String logic, String sensorId, String resourceId, String op, String value) {
            this.logic = logic;
            this.sensorId = sensorId;
            this.resourceId = resourceId;
            this.op = op;
            this.value = value;
        }

        public void addConditionText(String text) {
            this.text = text;
        }

        public boolean run(String value) {
            switch (op) {
                case ">":
                    if (Float.parseFloat(value) > Float.parseFloat(this.value)) {
                        enable = true;
                        return true;
                    } else {
                        enable = false;
                    }
                    break;
                case "<":
                    if (Float.parseFloat(value) < Float.parseFloat(this.value)) {
                        enable = true;
                        return true;
                    } else {
                        enable = false;
                    }
                    break;
                case "=":
                    if (value.equals(this.value)) {
                        enable = true;
                        return true;
                    } else {
                        enable = false;
                    }
                    break;
                case "!=":
                    if (!value.equals(this.value)) {
                        enable = true;
                        return true;
                    } else {
                        enable = false;
                    }
                    break;
                default:
                    System.out.println("unknown op");
                    break;
            }

            return false;
        }
    }

    public class Action implements Serializable {
        public String actuatorId;
        public String resourceId;
        public String value;
        public String text;

        public Action(String actuatorId, String resourceId, String value) {
            this.actuatorId = actuatorId;
            this.resourceId = resourceId;
            this.value = value;
        }

        public void addActionText(String text) {
            this.text = text;
        }
    }
}

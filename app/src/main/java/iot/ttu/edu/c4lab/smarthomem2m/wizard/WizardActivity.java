package iot.ttu.edu.c4lab.smarthomem2m.wizard;

import com.github.clans.fab.FloatingActionButton;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import iot.ttu.edu.c4lab.smarthomem2m.M2MCoapClient;
import iot.ttu.edu.c4lab.smarthomem2m.R;
import iot.ttu.edu.c4lab.smarthomem2m.SmartHomeActivity;
import iot.ttu.edu.c4lab.smarthomem2m.data.Rule;

public class WizardActivity extends AppCompatActivity {
    private FloatingActionButton fab_next;
    private FloatingActionButton fab_previous;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private boolean doubleClickToExit = false;

    public static final int TOTAL_STEP = 4;
    public static Handler wizardHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule_wizard);
        // disable show keyboard automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int requestCode = getIntent().getIntExtra("requestCode", SmartHomeActivity.NEW_RULE);

        if (requestCode == SmartHomeActivity.NEW_RULE) {
            Rule rule = new Rule();
            WizardFragment.init(rule);
        } else if (requestCode == SmartHomeActivity.EDIT_RULE) {
            String ruleName = getIntent().getStringExtra("ruleName");
            Rule rule = M2MCoapClient.ruleMap.get(ruleName);
            if (rule == null) {
                setResult(RESULT_CANCELED);
                finish();
            } else {
                WizardFragment.init(rule);
            }
        }

        wizardHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        fab_next = (FloatingActionButton) findViewById(R.id.fab_next);
        fab_previous = (FloatingActionButton) findViewById(R.id.fab_previous);
        fab_previous.setVisibility(View.INVISIBLE);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setEnabled(false);
        for (int i = 0; i < TOTAL_STEP; i++) {
            RadioButton btn = new RadioButton(this);
            btn.setClickable(false);
            radioGroup.addView(btn);
        }
        radioGroup.check(radioGroup.getChildAt(mViewPager.getCurrentItem()).getId());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position <= 0)
                    fab_previous.setVisibility(View.INVISIBLE);
                else
                    fab_previous.setVisibility(View.VISIBLE);

                if (position >= TOTAL_STEP - 1)
                    fab_next.setVisibility(View.INVISIBLE);
                else
                    fab_next.setVisibility(View.VISIBLE);

                if (radioGroup.getChildAt(position) != null)
                    radioGroup.check(radioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab_next.setOnClickListener(view -> {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        });

        fab_previous.setOnClickListener(view -> {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            WizardFragment fragment = WizardFragment.newInstance(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return TOTAL_STEP;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (doubleClickToExit) {
            super.onBackPressed();
            WizardFragment.init(null);
            return;
        }

        this.doubleClickToExit = true;
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.smartHomeAddRuleWizard_cancel_message),
                Snackbar.LENGTH_INDEFINITE);
        final View view = snackbar.getView();
        final TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        snackbar.show();

        new Handler().postDelayed(() -> {
            doubleClickToExit = false;
            snackbar.dismiss();
        }, 2000);
    }
}

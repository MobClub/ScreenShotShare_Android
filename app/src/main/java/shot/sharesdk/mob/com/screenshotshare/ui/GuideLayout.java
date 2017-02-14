package shot.sharesdk.mob.com.screenshotshare.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import shot.sharesdk.mob.com.screenshotshare.R;

/**
 * Created by yjin on 2017/2/14.
 */

public class GuideLayout extends RelativeLayout implements View.OnClickListener {
    private static final String GUIDE_NAME = "guideLayout";
    private static final String GUIDE_VALUE = "guideValue";
    private ImageView stepOne;
    private ImageView stepTwo;
    private View oneView;
    private View twoView;

    public GuideLayout(Context context) {
        super(context);
        init(context);
    }

    public GuideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init(Context context) {
        setBackgroundColor(Color.BLACK);
        setAlpha(0.7f);
        oneView = LayoutInflater.from(context).inflate(R.layout.guide_step_one, null);
        stepOne = (ImageView) oneView.findViewById(R.id.nextStep);
        stepOne.setOnClickListener(this);
        twoView = LayoutInflater.from(context).inflate(R.layout.guide_step_two, null);
        stepTwo = (ImageView) twoView.findViewById(R.id.finish);
        stepTwo.setOnClickListener(this);
        boolean isUse = getGuideValue(context);
        if (!isUse) {
            showGuide(context);
        }else{
            setVisibility(View.GONE);
        }
    }

    private void nextStep() {
        removeView(getStepOne(getContext()));
        addView(getStepTwo(getContext()),getLayoutParam());
    }

    private RelativeLayout.LayoutParams getLayoutParam(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        return layoutParams;
    }

    private void showGuide(Context context) {
        addView(getStepOne(context),getLayoutParam());
    }

    private View getStepOne(Context context) {
        return oneView;
    }

    private View getStepTwo(Context context) {
        return twoView;
    }

    private void setGuideValue(Context context, boolean used) {
        SharedPreferences sp = context.getSharedPreferences(GUIDE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(GUIDE_VALUE, used);
        editor.commit();

    }

    private boolean getGuideValue(Context context) {
        SharedPreferences sp = context.getSharedPreferences(GUIDE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(GUIDE_VALUE, false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.nextStep:
                nextStep();
                break;
            case R.id.finish:
                this.setVisibility(View.GONE);
                setGuideValue(getContext(), true);
                break;
        }
    }
}

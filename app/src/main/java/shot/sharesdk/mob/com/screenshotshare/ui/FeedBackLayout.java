package shot.sharesdk.mob.com.screenshotshare.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import shot.sharesdk.mob.com.screenshotshare.R;

/**
 * Created by yjin on 2017/2/10.
 */

public class FeedBackLayout extends LinearLayout {
    private LayoutInflater inflater;
    public FeedBackLayout(Context context) {
        super(context);
        initView(context);
    }

    public FeedBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        View view = inflater.from(context).inflate(R.layout.feedback_layout,null);
        addView(view);
    }
}

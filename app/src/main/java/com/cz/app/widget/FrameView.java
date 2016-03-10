package com.cz.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.cz.library.widget.TemplateView;


/**
 * Created by cz on 16/3/7.
 * is my custom TemplateView view
 *
 * @see @link{com.cz.library.widget.TemplateView}
 */
public class FrameView extends TemplateView {
    private TextView progressText;
    private TextView displayText;
    private ImageView displayImage;
    private ImageView retryImage;
    private TextView retryText;
    private TextView retryButton;
    private Runnable retryAction;

    public FrameView(Context context) {
        this(context, null, 0);
    }

    public FrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        progressText = (TextView) findViewById(R.id.tv_info);
//        displayImage = (ImageView) findViewById(R.id.iv_display_image);
//        displayText = (TextView) findViewById(R.id.tv_display_info);
//        retryImage = (ImageView) findViewById(R.id.iv_error_image);
//        retryText = (TextView) findViewById(R.id.tv_error_info);
//        retryButton = (TextView) findViewById(R.id.tv_error);
//        retryButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != retryAction) {
//                    retryAction.run();
//                }
//            }
//        });
    }

    public void setProgressText(String text) {
        progressText.setText(text);
    }

    public void setProgerssText(@StringRes int res) {
        progressText.setText(res);
    }

    public void setDisplayInfo(String text) {
        displayText.setText(text);
    }

    public void setDisplayText(@StringRes int res) {
        displayText.setText(res);
    }

    public void setDisplayImage(@DrawableRes int res) {
        displayImage.setImageResource(res);
    }

    public void setDiaplayImage(Drawable drawable) {
        displayImage.setImageDrawable(drawable);
    }

    public void setRetryAction(Runnable action) {
        this.retryAction = action;
    }

    public void setRetryText(String text) {
        retryText.setText(text);
    }

    public void setRetryText(@StringRes int res) {
        retryText.setText(res);
    }

    public void setRetryImage(@DrawableRes int res) {
        retryImage.setImageResource(res);
    }

    public void setRetryImage(Drawable drawable) {
        retryImage.setImageDrawable(drawable);
    }


}

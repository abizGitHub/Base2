package com.tut.abiz.base.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

import com.tut.abiz.base.R;

/**
 * Created by abiz on 5/16/2019.
 */

public class ImageV extends android.support.v7.widget.AppCompatImageView {

    boolean checked;
    int checkedImg = -1, unCheckedImg = -1;
    int checkedColor = -1, unCheckedColor = -1;

    public ImageV(Context context) {
        super(context);
    }

    public ImageV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChecked(boolean stared) {
        checked = stared;
        if (checked) {
            if (checkedImg == -1)
                setImageResource(R.drawable.baseline_star_black_18);
            else
                setImageResource(checkedImg);
            if (checkedColor == -1)
                setColorFilter(getResources().getColor(R.color.colorOrange));
            else
                setColorFilter(getResources().getColor(checkedColor));
        } else {
            if (unCheckedImg == -1)
                setImageResource(R.drawable.baseline_star_border_black_18);
            else
                setImageResource(unCheckedImg);
            if (unCheckedColor == -1)
                setColorFilter(Color.BLACK);
            else
                setColorFilter(getResources().getColor(unCheckedColor));
        }
    }

    public void setOnCheckedChangeListener(final CompoundButton.OnCheckedChangeListener checkListener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState();
                checkListener.onCheckedChanged(null, checked);
            }
        });
    }

    private void changeState() {
        checked = !checked;
        setChecked(checked);
    }

    public void setCheckedImg(int checkedImg) {
        this.checkedImg = checkedImg;
    }

    public void setUnCheckedImg(int unCheckedImg) {
        this.unCheckedImg = unCheckedImg;
    }

    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
    }

    public void setUnCheckedColor(int unCheckedColor) {
        this.unCheckedColor = unCheckedColor;
    }

    public void setScale(float x, float y) {
        this.setScaleX(x);
        this.setScaleY(y);
    }

    public void setScale(float xy) {
        setScale(xy, xy);
    }
}

package com.cz.app.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.cz.app.R;
import com.cz.app.widget.ColorItem;
import com.cz.app.widget.RadioLayout;
import com.cz.app.widget.SeekLayout;
import com.cz.library.widget.drawable.DrawableBuilder;
import com.cz.library.widget.drawable.RectDrawableBuilder;
import com.cz.library.widget.drawable.RingBuilder;

/**
 * Created by cz on 16/3/7.
 */
public class ShapeDrawableFragment extends Fragment {
    private static final String TAG = "ShapeDrawableFragment";
    private ImageView selectView1;
    private ImageView selectView2;
    private int shapeType;
    private int solidColor = Color.CYAN;
    private int strokeWidth = 2;
    private int ltRadius;
    private int lbRadius;
    private int rtRadius;
    private int rbRadius;
    private int strokeColor = Color.BLUE;
    private int dashWidth;
    private int dashGap;
    private int width = 40;
    private int height = 40;
    private int innerRadiusRatio;
    private int innerRadius;
    private int thicknessRatio;
    private int thickness;
    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;
    private int gradientType;
    private int gradientAngle;
    private int gradientRadius;
    private float centerX = 0.5f;
    private float centerY = 0.5f;
    private int startColor = Color.RED;
    private int centerColor = Color.GREEN;
    private int endColor = Color.YELLOW;
    private boolean useLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectView1 = (ImageView) view.findViewById(R.id.view1);
        selectView2 = (ImageView) view.findViewById(R.id.view2);
        RadioLayout shapeTypeLayout = (RadioLayout) view.findViewById(R.id.rl_shape_type);
        shapeTypeLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                shapeType = position;
                createDrawable();
            }
        });
        final ColorItem solidColorItem = (ColorItem) view.findViewById(R.id.ci_solid_color);
        solidColorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicketDialog picketDialog = new ColorPicketDialog();
                picketDialog.setOnColorSelectListener(new ColorPicketDialog.OnColorSelectListener() {
                    @Override
                    public void onSelected(View v, int color) {
                        //选中的solidColor
                        solidColor = color;
                        solidColorItem.setColor(color);
                        createDrawable();
                    }
                });
                picketDialog.show(getFragmentManager(), null);
            }
        });
        SeekLayout seekStroke = (SeekLayout) view.findViewById(R.id.sl_stroke_width);
        seekStroke.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                strokeWidth = progress;
                createDrawable();
            }
        });

        SeekLayout innerRadiusRatioSeek = (SeekLayout) view.findViewById(R.id.sl_inner_radius_ratio);
        innerRadiusRatioSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                innerRadiusRatio = progress;
                createDrawable();
            }
        });

        SeekLayout thicknessRatioSeek = (SeekLayout) view.findViewById(R.id.sl_thickness_ratio);
        thicknessRatioSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                thicknessRatio = progress;
                createDrawable();
            }
        });
        SeekLayout innerRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_inner_radius);
        innerRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                innerRadius = progress;
                createDrawable();
            }
        });

        SeekLayout thicknessSeek = (SeekLayout) view.findViewById(R.id.sl_thickness);
        thicknessSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                thickness = progress;
                createDrawable();
            }
        });

        SeekLayout ltRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_lt_radius);
        ltRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                ltRadius = progress;
                createDrawable();
            }
        });

        SeekLayout lbRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_lb_radius);
        lbRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                lbRadius = progress;
                createDrawable();
            }
        });

        SeekLayout rtRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_rt_radius);
        rtRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                rtRadius = progress;
                createDrawable();
            }
        });

        SeekLayout rbRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_rb_radius);
        rbRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                rbRadius = progress;
                createDrawable();
            }
        });
        final ColorItem strokeColorItem = (ColorItem) view.findViewById(R.id.ci_stroke_color);
        strokeColorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicketDialog picketDialog = new ColorPicketDialog();
                picketDialog.setOnColorSelectListener(new ColorPicketDialog.OnColorSelectListener() {
                    @Override
                    public void onSelected(View v, int color) {
                        //选中的solidColor
                        strokeColor = color;
                        strokeColorItem.setColor(color);
                        createDrawable();
                    }
                });
                picketDialog.show(getFragmentManager(), null);
            }
        });

        final SeekLayout dashWidthSeek = (SeekLayout) view.findViewById(R.id.sl_dash_width);
        dashWidthSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                dashWidth = progress;
                createDrawable();
            }
        });

        SeekLayout dashGapSeek = (SeekLayout) view.findViewById(R.id.sl_dash_gap);
        dashGapSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                dashGap = progress;
                createDrawable();
            }
        });

        SeekLayout widthSeek = (SeekLayout) view.findViewById(R.id.sl_width);
        widthSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                width = 40 + progress;
                createDrawable();
            }
        });

        SeekLayout heightSeek = (SeekLayout) view.findViewById(R.id.sl_height);
        heightSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                height = 40 + progress;
                createDrawable();
            }
        });

        SeekLayout leftPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_top_padding);
        leftPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                leftPadding = progress;
                createDrawable();
            }
        });

        SeekLayout topPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_top_padding);
        topPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                topPadding = progress;
                createDrawable();
            }
        });

        SeekLayout rightPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_right_padding);
        rightPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                rightPadding = progress;
                createDrawable();
            }
        });

        final SeekLayout bottomPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_bottom_padding);
        bottomPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                bottomPadding = progress;
                createDrawable();
            }
        });

        RadioLayout gradientTypeLayout = (RadioLayout) view.findViewById(R.id.rl_gradient_type);
        gradientTypeLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                gradientType = position;
                createDrawable();
            }
        });

        final SeekLayout gradientAngleSeek = (SeekLayout) view.findViewById(R.id.sl_gradient_angle);
        gradientAngleSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                gradientAngle = progress * 45;
                createDrawable();
            }
        });
        final SeekLayout gradientRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_gradient_radius);
        gradientRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                gradientRadius = progress;
                createDrawable();
            }
        });

        final SeekLayout centerXLayout = (SeekLayout) view.findViewById(R.id.sl_center_x);
        centerXLayout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                float value = progress * 1f / seekBar.getMax();
                centerX = value;
                createDrawable();
            }
        });
        final SeekLayout centerYLayout = (SeekLayout) view.findViewById(R.id.sl_center_y);
        centerYLayout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                float value = progress * 1f / seekBar.getMax();
                centerY = value;
                createDrawable();
            }
        });
        final ColorItem startColorItem = (ColorItem) view.findViewById(R.id.ci_start_color);
        startColorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicketDialog picketDialog = new ColorPicketDialog();
                picketDialog.setOnColorSelectListener(new ColorPicketDialog.OnColorSelectListener() {
                    @Override
                    public void onSelected(View v, int color) {
                        //选中的solidColor
                        startColorItem.setColor(color);
                        startColor = color;
                        createDrawable();
                    }
                });
                picketDialog.show(getFragmentManager(), null);
            }
        });
        final ColorItem centerColorItem = (ColorItem) view.findViewById(R.id.ci_center_color);
        view.findViewById(R.id.ci_center_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicketDialog picketDialog = new ColorPicketDialog();
                picketDialog.setOnColorSelectListener(new ColorPicketDialog.OnColorSelectListener() {
                    @Override
                    public void onSelected(View v, int color) {
                        //选中的solidColor
                        centerColorItem.setColor(color);
                        centerColor = color;
                        createDrawable();
                    }
                });
                picketDialog.show(getFragmentManager(), null);
            }
        });
        final ColorItem endColorItem = (ColorItem) view.findViewById(R.id.ci_end_color);
        view.findViewById(R.id.ci_end_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicketDialog picketDialog = new ColorPicketDialog();
                picketDialog.setOnColorSelectListener(new ColorPicketDialog.OnColorSelectListener() {
                    @Override
                    public void onSelected(View v, int color) {
                        //选中的solidColor
                        endColorItem.setColor(color);
                        endColor = color;
                        createDrawable();
                    }
                });
                picketDialog.show(getFragmentManager(), null);
            }
        });
        SwitchCompat useLevelSwitch = (SwitchCompat) view.findViewById(R.id.use_level);
        useLevelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useLevel = isChecked;
                createDrawable();
            }
        });
    }

    /**
     * 创建drawable
     */
    private void createDrawable() {
        DrawableBuilder builder = null;
        switch (shapeType) {
            case GradientDrawable.RECTANGLE:
                RectDrawableBuilder rectDrawableBuilder = DrawableBuilder.rect();
                rectDrawableBuilder.setLeftTopRadius(ltRadius).
                        setLeftBottomRadius(lbRadius).
                        setRightTopRadius(rtRadius).
                        setRightBottomRadius(rbRadius);
                builder = rectDrawableBuilder;
                break;
            case GradientDrawable.OVAL:
                builder = DrawableBuilder.oval();
                break;
            case GradientDrawable.RING:
                RingBuilder ring = DrawableBuilder.ring();
                ring.setInnerRadiusRatio(innerRadiusRatio).
                        setInnerRadius(innerRadius).
                        setThicknessRatio(thicknessRatio).
                        setThickness(thickness);
                builder = ring;
                break;
            case GradientDrawable.LINE:
                builder = DrawableBuilder.line();
                break;
        }
        GradientDrawable drawable = builder.setSolidColor(solidColor).
                setStrokeColor(strokeColor).
                setStrokeWidth(strokeWidth).
                setLeftPadding(leftPadding).
                setTopPadding(topPadding).
                setRightPadding(rightPadding).
                setBottomPadding(bottomPadding).
                setDashWidth(dashWidth).
                setDashGap(dashGap).
                setWidth(width).
                setHeight(height).
                setGradientType(gradientType).
                setUseLevel(useLevel).
                setCenterX(centerX).
                setCenterY(centerY).
                setGradientAngle(gradientAngle).
                setGradientRadius(gradientRadius).
                setGradientStartColor(startColor).
                setGradientCenterColor(centerColor).
                setGradientEndColor(endColor).build();
        selectView1.setImageDrawable(drawable);
        selectView2.setImageDrawable(drawable);
    }
}

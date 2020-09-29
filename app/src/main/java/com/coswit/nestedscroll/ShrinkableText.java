package com.coswit.nestedscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.coswit.R;

/**
 * @author Created by zhengjing on 2020/9/18.
 */
public class ShrinkableText extends ConstraintLayout {

    private Context context;

    boolean isMeasured = false;
    boolean initial;
    int lineEnd = 0;
    int lineCount;
    float percent = 0;

    public static final int MAX = 2;
    private TextView text;
    private TextView hint;
    private String mContent = "";

    public ShrinkableText(@NonNull Context context) {
        this(context, null);
    }

    public ShrinkableText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShrinkableText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.item_tex_shrink, this);
        text = findViewById(R.id.tv_desc);
        hint = findViewById(R.id.tv_hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint.setSelected(!hint.isSelected());
                ShrinkableText.this.requestLayout();
                isMeasured = false;
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        show();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public void show() {
        if (isMeasured) {
            return;
        }
        if (!initial) {
            lineCount = text.getLineCount();
            if (lineCount > MAX) {
                Layout layout = text.getLayout();
                lineEnd = layout.getLineEnd(1);
                float lastLineWidth = layout.getLineWidth(Math.max(lineCount - 1, 0));
                int width = text.getMeasuredWidth();
                percent = lastLineWidth / width;
            }
            initial = true;
        }
        if (lineCount > MAX) {
            hint.setVisibility(View.VISIBLE);
            if (!hint.isSelected()) {
                text.setLines(2);
                String part = mContent.substring(0, Math.max(lineEnd - 2, 0)) + "...";
                text.setText(part);
                hint.setText("全部");
                hint.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(context, R.mipmap.arrow_down_teacher), null);
            } else {
                //最后一行文字剩余空白
                boolean isOut = percent > 0.83f;
                text.setLines(isOut ? lineCount + 1 : lineCount);
                text.setText(mContent);
                hint.setText("收起");
                hint.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(context, R.mipmap.arrow_up__teacher), null);
            }
        } else {
            text.setText(mContent);
            hint.setVisibility(View.GONE);
        }

        isMeasured = true;
    }

    public void setText(final String content) {
        mContent = content;
        text.setText(content);
        isMeasured = false;
        requestLayout();
    }
}

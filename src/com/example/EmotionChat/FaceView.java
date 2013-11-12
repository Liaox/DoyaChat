package com.example.EmotionChat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * 顔を表すView
 */
public class FaceView extends RelativeLayout {
    private ImageView faceBody;
    private ImageView leftBrow, rightBrow;
    private ImageView leftEye, rightEye;
    private ImageView mouth;

    private static final List<Integer> LEFT_BROW_DRAWABLES = ImmutableList.of(
            R.drawable.brow1,
            R.drawable.left_brow2
    );
    private static final List<Integer> RIGHT_BROW_DRAWABLES = ImmutableList.of(
            R.drawable.brow1,
            R.drawable.right_brow2
    );
    private static final List<Integer> LEFT_EYE_DRAWABLES = ImmutableList.of(
            R.drawable.eye1,
            R.drawable.eye2
    );
    private static final List<Integer> RIGHT_EYE_DRAWABLES = ImmutableList.of(
            R.drawable.eye1,
            R.drawable.eye2
    );
    private static final List<Integer> MOUTH_DRAWABLES = ImmutableList.of(
            R.drawable.mouth1,
            R.drawable.mouth2
    );

    public enum SEX {
        MALE, FEMALE
    }

    public FaceView(Context context) {
        super(context);
        init(context);
    }

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.face_view, this);
        faceBody = (ImageView) layout.findViewById(R.id.face_body);
        leftBrow = (ImageView) layout.findViewById(R.id.brow_left);
        rightBrow = (ImageView) layout.findViewById(R.id.brow_right);
        leftEye = (ImageView) layout.findViewById(R.id.eve_left);
        rightEye = (ImageView) layout.findViewById(R.id.eve_right);
        mouth = (ImageView) layout.findViewById(R.id.mouth);
    }

    public FaceView setSex(SEX sex) {
        if (sex == SEX.MALE) {
            faceBody.setImageDrawable(getResources().getDrawable(R.drawable.face_man));
        } else if (sex == SEX.FEMALE) {
            faceBody.setImageDrawable(getResources().getDrawable(R.drawable.girl));
        }
        return this;
    }

    public FaceView setMouth(int id) {
        mouth.setImageDrawable(getResources().getDrawable(MOUTH_DRAWABLES.get(id)));
        return this;
    }

    public FaceView setLeftBrow(int id) {
        leftBrow.setImageDrawable(getResources().getDrawable(LEFT_BROW_DRAWABLES.get(id)));
        return this;
    }

    public FaceView setRightBrow(int id) {
        rightBrow.setImageDrawable(getResources().getDrawable(RIGHT_BROW_DRAWABLES.get(id)));
        return this;
    }

    public FaceView setLeftEye(int id) {
        leftEye.setImageDrawable(getResources().getDrawable(LEFT_EYE_DRAWABLES.get(id)));
        return this;
    }

    public FaceView setRightEye(int id) {
        rightEye.setImageDrawable(getResources().getDrawable(RIGHT_EYE_DRAWABLES.get(id)));
        return this;
    }
}
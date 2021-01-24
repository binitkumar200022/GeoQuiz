package com.binit.android.familyquiz;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mPressed;

    public Question(int textResId, boolean answerTrue, boolean pressed) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mPressed = pressed;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public boolean isPressed() {
        return mPressed;
    }

    public void setPressed(boolean pressed) {
        this.mPressed = pressed;
    }
}

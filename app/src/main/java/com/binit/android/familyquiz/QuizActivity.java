package com.binit.android.familyquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final int TOTAL = 5;
    
    private int count = 0;
    private int correctAns = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true, false),
            new Question(R.string.question_mideast, false, false),
            new Question(R.string.question_africa, false, false),
            new Question(R.string.question_americas, true, false),
            new Question(R.string.question_asia, true, false)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater[] = new boolean[mQuestionBank.length];

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if(count == TOTAL) {
            double per = correctAns*100;
            per = per/5;
            Toast.makeText(this, "Percentage = " + Double.toString(per) + "%", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateButtons() {
        boolean currentState = mQuestionBank[mCurrentIndex].isPressed();
        mTrueButton.setEnabled(!currentState);
        mFalseButton.setEnabled(!currentState);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        count++;

        if(mIsCheater[mCurrentIndex]) {
            messageResId = R.string.judgement_toast;
            Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        }
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            correctAns++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex =(mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                updateButtons();
            }
        });
        updateQuestion();

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mQuestionBank[mCurrentIndex].setPressed(true);
                checkAnswer(true);
                updateButtons();
            }
        });


        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mQuestionBank[mCurrentIndex].setPressed(true);
                checkAnswer(false);
                updateButtons();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex =(mCurrentIndex - 1) % mQuestionBank.length;
                if(mCurrentIndex < 0) {
                    mCurrentIndex = 4;
                }
                updateQuestion();
                updateButtons();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex =(mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                updateButtons();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
}
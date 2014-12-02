package com.capstone.symptommanagement.ui;

import com.capstone.symptommanagement.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class CheckInQuestionFragment extends Fragment implements OnClickListener {

	// Callback interface to send data to the main Acitivty (CheckInActivity)
	public interface OnAnswerSelectedListener {
        public void answer(int id, String answ);
	}
	
	// static Strings to retrieve the  question and its answers and show them.
	public static final String QUESTION = "question";
	public static final String ANSWERS = "answers";
	public static final String ID = "id";
	
	private OnAnswerSelectedListener mCallback;
	
	private TextView tvQuestion;
	private TextView tvAnswer1;
	private TextView tvAnswer2;
	private TextView tvAnswer3;
	
	private int Id;
	
	public static final CheckInQuestionFragment newInstance(int id, String question, String[] answers) {
		CheckInQuestionFragment fragment = new CheckInQuestionFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ID, id);
		bundle.putString(QUESTION, question);
		bundle.putStringArray(ANSWERS, answers);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_check_in_question,container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tvQuestion = (TextView)getView().findViewById(R.id.check_in_question_textView);
		tvAnswer1 = (TextView)getView().findViewById(R.id.check_in_answer1_textView);
		tvAnswer2 = (TextView)getView().findViewById(R.id.check_in_answer2_textView);
		tvAnswer3 = (TextView)getView().findViewById(R.id.check_in_answer3_textView);

		tvAnswer1.setOnClickListener(this);
		tvAnswer2.setOnClickListener(this);
		tvAnswer3.setOnClickListener(this);
	
		Id = getArguments().getInt(ID);
		setQuestion(getArguments().getString(QUESTION));
		setAnswers(getArguments().getStringArray(ANSWERS));
		
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnAnswerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAnswerSelectedListener");
        }
	}

	
	public void setQuestion(String question){
		tvQuestion.setText(question);
	}
	
	private void setAnswers(String[] answers){
		tvAnswer1.setText(answers[0]);
		tvAnswer2.setText(answers[1]);
		tvAnswer3.setText(answers[2]);
	}

	@Override
	public void onClick(View v) {
		mCallback.answer(Id, ((TextView) v).getText().toString());
	}
}

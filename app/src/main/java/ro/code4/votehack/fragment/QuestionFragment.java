package ro.code4.votehack.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ro.code4.votehack.BaseFragment;
import ro.code4.votehack.R;
import ro.code4.votehack.net.model.Question;
import ro.code4.votehack.util.FormRenderer;
import ro.code4.votehack.util.QuestionNavigator;

public class QuestionFragment extends BaseFragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_SIZE = "numberOfQuestions";
    private static final String ARG_INDEX = "indexOfQuestion";
    private Question question;
    private QuestionNavigator navigator;
    private int numberOfQuestions;
    private int questionIndex;

    public static QuestionFragment newInstance(List<Question> questions, int index) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, questions.get(index));
        args.putInt(ARG_INDEX, index + 1);
        args.putInt(ARG_SIZE, questions.size());
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getIdentifier() {
        return "QuestionFragment";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() == null || !(getParentFragment() instanceof QuestionNavigator)) {
            throw new RuntimeException("Fragment must be a child of a fragment implementing QuestionNavigator");
        } else {
            navigator = (QuestionNavigator) getParentFragment();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.question = (Question) getArguments().getSerializable(ARG_QUESTION);
        this.questionIndex = getArguments().getInt(ARG_INDEX);
        this.numberOfQuestions = getArguments().getInt(ARG_SIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        TextView progress = (TextView) rootView.findViewById(R.id.question_progress);
        progress.setText(questionIndex + " / " + numberOfQuestions);

        ViewGroup questionContainer = (ViewGroup) rootView.findViewById(R.id.question_container);
        questionContainer.addView(FormRenderer.renderQuestion(getActivity(), question));
        rootView.findViewById(R.id.button_question_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.onNext();
            }
        });
        rootView.findViewById(R.id.button_question_notes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.onNotes();
            }
        });
        return rootView;
    }
}
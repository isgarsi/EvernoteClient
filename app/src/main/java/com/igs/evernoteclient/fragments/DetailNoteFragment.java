package com.igs.evernoteclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.utils.Constants;

public class DetailNoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "ID";
    private static final String ARG_NOTE_TITLE = "TITLE";

    private Note note;
    private String noteId;
    private String title;
    private TextView content;
    private ProgressBar progressBar;
    private FloatingActionButton fabEdit;
    private OnDetailNoteFragmentInteractionListener mListener;

    public DetailNoteFragment() {
        // Required empty public constructor
    }

    public static DetailNoteFragment newInstance(String noteId, String title) {
        DetailNoteFragment fragment = new DetailNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE_ID, noteId);
        args.putString(ARG_NOTE_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getString(ARG_NOTE_ID);
            title = getArguments().getString(ARG_NOTE_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_note, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);

        progressBar = (ProgressBar) view.findViewById(R.id.detail_note_progress_bar);
        content = (TextView) view.findViewById(R.id.detail_note_tv_content);
        fabEdit = (FloatingActionButton) view.findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditNoteButtonClicked(note);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailNoteFragmentInteractionListener) {
            mListener = (OnDetailNoteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailNoteFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNoteData();
    }

    private void getNoteData() {
        content.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.getNoteAsync(noteId, true, false, false, false, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                note = result;
                try {
                    content.setText(Html.fromHtml(note.getContent()));
                } catch (Exception e) {
                    showError(e);
                }
                content.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onException(Exception exception) {
                showError(exception);
                content.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showError(Exception exception){
        Log.e(Constants.DEBUG_TAG, "Error getting the note data", exception);
        Snackbar.make(content, getString(R.string.error_getting_note_data), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNoteData();
                    }
                })
                .show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDetailNoteFragmentInteractionListener {
        void onEditNoteButtonClicked(Note note);
    }
}

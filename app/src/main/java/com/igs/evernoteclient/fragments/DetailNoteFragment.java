package com.igs.evernoteclient.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

    private static final String ARG_NOTE_ID = "NOTE";

    private Note note;
    private String noteId;
    private TextView title;
    private TextView content;
    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    public DetailNoteFragment() {
        // Required empty public constructor
    }

    public static DetailNoteFragment newInstance(String noteId) {
        DetailNoteFragment fragment = new DetailNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getString(ARG_NOTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_note, container, false);
        mainLayout = (LinearLayout) view.findViewById(R.id.detail_note_linear_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.detail_note_progress_bar);
        title = (TextView) view.findViewById(R.id.detail_note_tv_title);
        content = (TextView) view.findViewById(R.id.detail_note_tv_content);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNoteData();
    }

    private void getNoteData() {
        mainLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.getNoteAsync(noteId, true, false, false, false, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                note = result;
                try {
                    title.setText(note.getTitle());
                    content.setText(Html.fromHtml(note.getContent()));
                } catch (Exception e) {
                    showError(e);
                }
                mainLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onException(Exception exception) {
                showError(exception);
                mainLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showError(Exception exception){
        Log.e(Constants.DEBUG_TAG, "Error getting the note data", exception);
        Snackbar.make(mainLayout, getString(R.string.error_getting_note_data), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNoteData();
                    }
                })
                .show();
    }
}

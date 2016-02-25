package com.igs.evernoteclient.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.utils.Constants;

public class EditKeyboardNoteFragment extends Fragment {
    private EditText title;
    private EditText content;
    private Button butSave;
    private RelativeLayout mainLayout;
    private ProgressBar progressBar;
    private Note note;
    private boolean updating;

    public EditKeyboardNoteFragment() {
        // Required empty public constructor
    }

    public static EditKeyboardNoteFragment newInstance() {
        EditKeyboardNoteFragment fragment = new EditKeyboardNoteFragment();
        return fragment;
    }

    public static EditKeyboardNoteFragment newInstance(Note note) {
        EditKeyboardNoteFragment fragment = new EditKeyboardNoteFragment();
        fragment.setNote(note);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_keyboard_note, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_new_note));

        title = (EditText) view.findViewById(R.id.edit_note_tv_title);
        content = (EditText) view.findViewById(R.id.edit_note_tv_content);
        mainLayout = (RelativeLayout) view.findViewById(R.id.edit_notes_main_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.edit_notes_progress_bar);

        if(note == null){
            note = new Note();
            updating = false;
        }else{
            title.setText(note.getTitle());
            content.setText(Html.fromHtml(note.getContent()));
            updating = true;
        }

        butSave = (Button) view.findViewById(R.id.edit_note_btn_save);
        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim().equals("") ||
                        content.getText().toString().trim().equals(""))
                {
                    Snackbar.make(butSave, getString(R.string.error_empty_fields),
                            Snackbar.LENGTH_LONG).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.INVISIBLE);

                    //fill new data
                    note.setTitle(title.getText().toString());
                    note.setContent(EvernoteUtil.NOTE_PREFIX + content.getText().toString() + EvernoteUtil.NOTE_SUFFIX);

                    //save data
                    EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
                    if (note.getGuid() == null) {
                        noteStoreClient.createNoteAsync(note, evernoteCallback);
                    } else {
                        noteStoreClient.updateNoteAsync(note, evernoteCallback);
                    }
                }
            }
        });

        return view;
    }

    public void setNote(Note note){
        this.note = note;
    }

    EvernoteCallback<Note> evernoteCallback = new EvernoteCallback<Note>() {
        @Override
        public void onSuccess(Note result) {
            String message = (updating) ? getString(R.string.note_updated) : getString(R.string.note_saved);
            Snackbar.make(butSave, title.getText().toString() + " " + message,
                    Snackbar.LENGTH_LONG).show();
            getActivity().onBackPressed();

            if(updating){
                getActivity().onBackPressed();
            }
        }

        @Override
        public void onException(Exception exception) {
            progressBar.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            Snackbar.make(butSave, getString(R.string.error_saving_note), Snackbar.LENGTH_LONG).show();
        }
    };
}

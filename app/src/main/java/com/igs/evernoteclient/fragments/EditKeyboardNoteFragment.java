package com.igs.evernoteclient.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    public EditKeyboardNoteFragment() {
        // Required empty public constructor
    }

    public static EditKeyboardNoteFragment newInstance() {
        EditKeyboardNoteFragment fragment = new EditKeyboardNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_keyboard_note, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_new_note));

        title = (EditText) view.findViewById(R.id.edit_note_tv_title);
        content = (EditText) view.findViewById(R.id.edit_note_tv_content);
        butSave = (Button) view.findViewById(R.id.edit_note_btn_save);
        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

                Note note = new Note();
                note.setTitle(title.getText().toString());
                note.setContent(EvernoteUtil.NOTE_PREFIX + content.getText().toString() + EvernoteUtil.NOTE_SUFFIX);

                noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
                    @Override
                    public void onSuccess(Note result) {
                        Snackbar.make(butSave, title.getText().toString() + " " + getString(R.string.note_saved),
                                Snackbar.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onException(Exception exception) {
                        Log.e(Constants.DEBUG_TAG, "Error creating note", exception);
                        Snackbar.make(butSave,getString(R.string.error_saving_note),Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}

package com.igs.evernoteclient.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.evernote.edam.notestore.NoteMetadata;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.fragments.DetailNoteFragment;
import com.igs.evernoteclient.fragments.NotesFragment;
import com.igs.evernoteclient.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity implements NotesFragment.OnNotesFragmentInteractionListener{

    private static final int CONTAINER_ID = R.id.fragment_container;

    private FragmentUtil fUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
            });

            //Put the notes fragmetn
            fUtils = new FragmentUtil(this);
            fUtils.changeFragment(CONTAINER_ID, NotesFragment.newInstance(),false);
            }

    @Override
    public void onListSelected(String noteId) {
        fUtils.changeFragment(CONTAINER_ID, DetailNoteFragment.newInstance(noteId),true);
    }
}

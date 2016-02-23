package com.igs.evernoteclient.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.evernote.edam.notestore.NoteMetadata;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.fragments.DetailNoteFragment;
import com.igs.evernoteclient.fragments.EditKeyboardNoteFragment;
import com.igs.evernoteclient.fragments.NotesFragment;
import com.igs.evernoteclient.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity implements NotesFragment.OnNotesFragmentInteractionListener{

    private static final int CONTAINER_ID = R.id.fragment_container;

    private FragmentUtil fUtils;
    private LinearLayout orderToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderToolbarLayout = (LinearLayout) findViewById(R.id.main_order_llayout);

        //Put the notes fragmetn
        fUtils = new FragmentUtil(this);
        fUtils.changeFragment(CONTAINER_ID, NotesFragment.newInstance(),false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //When go back to NotesFragment, show the order spinner
        orderToolbarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoteSelected(String noteId) {
        orderToolbarLayout.setVisibility(View.GONE);
        fUtils.changeFragment(CONTAINER_ID, DetailNoteFragment.newInstance(noteId),true);
    }

    @Override
    public void onNewKeyboardNoteClick() {
        orderToolbarLayout.setVisibility(View.GONE);
        fUtils.changeFragment(CONTAINER_ID, EditKeyboardNoteFragment.newInstance(),true);
    }

    @Override
    public void onNewHandWritingNoteClick() {
//        orderToolbarLayout.setVisibility(View.GONE);

    }
}

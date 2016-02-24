package com.igs.evernoteclient.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.type.Note;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.fragments.DetailNoteFragment;
import com.igs.evernoteclient.fragments.EditKeyboardNoteFragment;
import com.igs.evernoteclient.fragments.NotesFragment;
import com.igs.evernoteclient.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity implements NotesFragment.OnNotesFragmentInteractionListener, DetailNoteFragment.OnDetailNoteFragmentInteractionListener{

    private static final int CONTAINER_ID = R.id.fragment_container;

    private FragmentUtil fUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Put the notes fragmetn
        fUtils = new FragmentUtil(this);
        fUtils.changeFragment(CONTAINER_ID, NotesFragment.newInstance(),false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNoteSelected(String noteId, String title) {
        fUtils.changeFragment(CONTAINER_ID, DetailNoteFragment.newInstance(noteId, title), true);
    }

    @Override
    public void onNewKeyboardNoteClick() {
        fUtils.changeFragment(CONTAINER_ID, EditKeyboardNoteFragment.newInstance(),true);
    }

    @Override
    public void onNewHandWritingNoteClick() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                EvernoteSession.getInstance().logOut();
                Intent splashActivity = new Intent(this,SplashScreenActivity.class);
                startActivity(splashActivity);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onEditNoteButtonClicked(Note note) {
        fUtils.changeFragment(CONTAINER_ID, EditKeyboardNoteFragment.newInstance(note),true);
    }
}

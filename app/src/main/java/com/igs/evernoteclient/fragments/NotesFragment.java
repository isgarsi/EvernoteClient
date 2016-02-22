package com.igs.evernoteclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.type.NoteSortOrder;
import com.igs.evernoteclient.DividerItemDecoration;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.adapters.NotesAdapter;
import com.igs.evernoteclient.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    private OnNotesFragmentInteractionListener mListener;
    private List<NoteMetadata> mNotes;
    private NotesAdapter adapter;
    private RecyclerView recView;
    private ProgressBar progressBar;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotes = new ArrayList<NoteMetadata>();
        adapter = new NotesAdapter(mNotes);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recView.getChildAdapterPosition(v);
                mListener.onListSelected(mNotes.get(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recView = (RecyclerView) view.findViewById(R.id.notes_rv_notes);
        recView.setHasFixedSize(true);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        progressBar = (ProgressBar) view.findViewById(R.id.notes_progress_bar);
        recView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotesFragmentInteractionListener) {
            mListener = (OnNotesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotes();
    }

    private void getNotes(){
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        NoteFilter filter = new NoteFilter();
        filter.setOrder(NoteSortOrder.UPDATED.getValue());

        NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
        spec.setIncludeTitle(true);


        noteStoreClient.findNotesMetadataAsync(filter, 0, Integer.MAX_VALUE, spec, new EvernoteCallback<NotesMetadataList>() {
            @Override
            public void onSuccess(NotesMetadataList notes) {
                updateList(notes.getNotes());
            }

            @Override
            public void onException(Exception exception) {
                Log.e(Constants.DEBUG_TAG, "Error getting notes", exception);
                Snackbar.make(recView, getString(R.string.error_getting_notes), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.action_try_again), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getNotes();
                            }
                        })
                        .show();
            }
        });

    }

    private void updateList(List<NoteMetadata> notes){
        try {
            recView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            mNotes.clear();
            if (notes != null && notes.size() > 0) {
                mNotes.addAll(notes);
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.e(Constants.DEBUG_TAG,"Error updating the list", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNotesFragmentInteractionListener {
        void onListSelected(NoteMetadata note);
    }
}

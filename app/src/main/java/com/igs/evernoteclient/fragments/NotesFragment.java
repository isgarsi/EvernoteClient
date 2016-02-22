package com.igs.evernoteclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.evernote.edam.notestore.NoteMetadata;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.utils.Constants;

import java.util.List;

public class NotesFragment extends Fragment {

    private OnNotesFragmentInteractionListener mListener;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recView = (RecyclerView) view.findViewById(R.id.notes_rv_notes);

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
    }


    private void updateList(List<NoteMetadata> notes){
        try {
            recView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

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
        void onListSelected(int idNote);
    }
}

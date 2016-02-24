package com.igs.evernoteclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.igs.evernoteclient.DividerItemDecoration;
import com.igs.evernoteclient.R;
import com.igs.evernoteclient.adapters.NotesAdapter;
import com.igs.evernoteclient.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment implements View.OnClickListener{

    private final static int ORDER_CREATED = 1;
    private final static int ORDER_UPDATED = 2;
    private final static int ORDER_RELEVANCE = 3;
    private final static int ORDER_UPDATE_SEQUENCE_NUMBER = 4;
    private final static int ORDER_TITLE = 5;

    private OnNotesFragmentInteractionListener mListener;
    private List<NoteMetadata> mNotes;
    private NotesAdapter adapter;
    private RecyclerView recView;
    private ProgressBar progressBar;
    private Spinner spinOrder;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabKeyboardAdd;
    private FloatingActionButton fabHandwritingAdd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout orderToolbarLayout;
    private int sortOrder;

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
                mListener.onNoteSelected(mNotes.get(position).getGuid(),mNotes.get(position).getTitle());
            }
        });
        //Default
        sortOrder = ORDER_UPDATED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        recView = (RecyclerView) view.findViewById(R.id.notes_rv_notes);
        recView.setHasFixedSize(true);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        progressBar = (ProgressBar) view.findViewById(R.id.notes_progress_bar);
        orderToolbarLayout = (LinearLayout) getActivity().findViewById(R.id.main_order_llayout);

        //Order by
        spinOrder = (Spinner) getActivity().findViewById(R.id.spinner_order);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),R.array.order_by,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinOrder.setAdapter(adapter);
        spinOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                sortOrder = pos + 1;//because spinner starts with 0
                getNotes();//reload notes with the new order
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinOrder.setSelection(sortOrder - 1);

        //Add buttons
        fabMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu_add);
        fabKeyboardAdd = (FloatingActionButton) view.findViewById(R.id.fab_menu_keyboard_add);
        fabHandwritingAdd = (FloatingActionButton) view.findViewById(R.id.fab_menu_handwriting_add);
        fabKeyboardAdd.setOnClickListener(this);
        fabHandwritingAdd.setOnClickListener(this);

        //Se añade refresco por swipe
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notes_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotes();
            }
        });

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
        orderToolbarLayout.setVisibility(View.VISIBLE);
        getNotes();
    }

    private void getNotes(){
        recView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        NoteFilter filter = new NoteFilter();
        //Order
        //filter.setOrder(NoteSortOrder.UPDATED.getValue());
        filter.setOrder(sortOrder);

        NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
        spec.setIncludeTitle(true);
        spec.setIncludeCreated(true);
        spec.setIncludeUpdated(true);
        noteStoreClient.findNotesMetadataAsync(filter, 0, Integer.MAX_VALUE, spec, new EvernoteCallback<NotesMetadataList>() {
            @Override
            public void onSuccess(NotesMetadataList notes) {
                updateList(notes.getNotes());
            }

            @Override
            public void onException(Exception exception) {
                Log.e(Constants.DEBUG_TAG, "Error getting notes", exception);
                Snackbar.make(recView, getString(R.string.error_getting_notes), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.action_try_again), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getNotes();
                            }
                        })
                        .show();
                recView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void updateList(List<NoteMetadata> notes){
        try {
            recView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
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
    public void onPause() {
        super.onPause();
        orderToolbarLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_menu_keyboard_add:
                mListener.onNewKeyboardNoteClick();
                fabMenu.collapse();
                break;
            case R.id.fab_menu_handwriting_add:
                mListener.onNewHandWritingNoteClick();
                fabMenu.collapse();
                break;
            default:
                break;
        }
    }

    public interface OnNotesFragmentInteractionListener {
        void onNoteSelected(String noteId, String title);
        void onNewKeyboardNoteClick();
        void onNewHandWritingNoteClick();
    }
}
package com.igs.evernoteclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evernote.edam.notestore.NoteMetadata;
import com.igs.evernoteclient.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> implements View.OnClickListener{
    private List<NoteMetadata> notes;
    private View.OnClickListener clickListener;

    public NotesAdapter(List<NoteMetadata> data) {
        this.notes = data;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_item, viewGroup, false);
        itemView.setOnClickListener(this);
        NotesViewHolder tvh = new NotesViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int pos) {
        NoteMetadata n = notes.get(pos);
        viewHolder.bindNote(n);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if(clickListener != null) {
            clickListener.onClick(view);
        }
    }
}

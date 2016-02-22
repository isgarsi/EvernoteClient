package com.igs.evernoteclient.adapters;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.evernote.edam.notestore.NoteMetadata;
import com.igs.evernoteclient.R;

import java.util.Date;

public class NotesViewHolder extends RecyclerView.ViewHolder{

    private TextView txtName;
    private TextView txtDate;

    public NotesViewHolder(View itemView) {
        super(itemView);
        txtName = (TextView)itemView.findViewById(R.id.note_item_tv_name);
        txtDate = (TextView)itemView.findViewById(R.id.note_item_tv_date);
    }

    public void bindNote(NoteMetadata n) {
        txtName.setText(n.getTitle());
        String updateTime= (n.getUpdated() == 0) ?
                DateFormat.format("dd/MM/yyyy HH:mm", new Date(n.getCreated())).toString()
                    : DateFormat.format("dd/MM/yyyy HH:mm", new Date(n.getUpdated())).toString();
        txtDate.setText(updateTime);
    }
}

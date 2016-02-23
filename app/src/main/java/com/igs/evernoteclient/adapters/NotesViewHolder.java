package com.igs.evernoteclient.adapters;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.evernote.edam.notestore.NoteMetadata;
import com.igs.evernoteclient.R;

import java.util.Date;

public class NotesViewHolder extends RecyclerView.ViewHolder{

    private TextView txtName;
    private TextView txtCDate;
    private TextView txtUDate;

    public NotesViewHolder(View itemView) {
        super(itemView);
        txtName = (TextView)itemView.findViewById(R.id.note_item_tv_name);
        txtCDate = (TextView)itemView.findViewById(R.id.note_item_tv_cdate);
        txtUDate = (TextView)itemView.findViewById(R.id.note_item_tv_udate);
    }

    public void bindNote(NoteMetadata n) {
        txtName.setText(n.getTitle());
        String createdTime= DateFormat.format("dd/MM/yyyy HH:mm", new Date(n.getCreated())).toString();
        txtCDate.setText(createdTime);
        String updatedTime= DateFormat.format("dd/MM/yyyy HH:mm", new Date(n.getUpdated())).toString();
        txtUDate.setText(updatedTime);
    }
}

package com.hhtt.hhtt_note;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter  extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options , Context context) {
        super(options);
        this.context= context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleview.setText(note.title);
        holder.contentview.setText(note.content);
        holder.timestampview.setText(Utility.timestampToString(note.Timestamp));


        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, NoteDetailActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleview, contentview,timestampview;

        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleview = itemView.findViewById(R.id.note_title_text_view);
            contentview = itemView.findViewById(R.id.note_text);
            timestampview = itemView.findViewById(R.id.note_timestamp_text);



        }
    }
}

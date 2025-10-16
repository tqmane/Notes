package com.tqmane.notesapp.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tqmane.notesapp.R;
import com.tqmane.notesapp.models.Note;
import com.tqmane.notesapp.storage.NoteStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * ノート一覧表示用アダプター
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    
    private List<Note> notes;
    private NoteStorage storage;
    private OnNoteClickListener listener;
    
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
    }
    
    public NotesAdapter(NoteStorage storage, OnNoteClickListener listener) {
        this.notes = new ArrayList<>();
        this.storage = storage;
        this.listener = listener;
    }
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
    
    public void addNote(Note note) {
        notes.add(0, note);
        notifyItemInserted(0);
    }
    
    public void removeNote(Note note) {
        int position = notes.indexOf(note);
        if (position >= 0) {
            notes.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }
    
    @Override
    public int getItemCount() {
        return notes.size();
    }
    
    class NoteViewHolder extends RecyclerView.ViewHolder {
        
        private TextView titleText;
        private TextView dateText;
        private ImageView thumbnailImage;
        
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.note_title);
            dateText = itemView.findViewById(R.id.note_date);
            thumbnailImage = itemView.findViewById(R.id.note_thumbnail);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNoteClick(notes.get(position));
                }
            });
            
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNoteLongClick(notes.get(position));
                    return true;
                }
                return false;
            });
        }
        
        public void bind(Note note) {
            titleText.setText(note.getTitle());
            dateText.setText(note.getFormattedDate());
            
            // サムネイル画像を読み込み
            if (note.getImagePath() != null) {
                Bitmap bitmap = storage.loadImage(note.getImagePath());
                if (bitmap != null) {
                    thumbnailImage.setImageBitmap(bitmap);
                } else {
                    thumbnailImage.setImageResource(R.drawable.ic_note);
                }
            } else {
                thumbnailImage.setImageResource(R.drawable.ic_note);
            }
        }
    }
}


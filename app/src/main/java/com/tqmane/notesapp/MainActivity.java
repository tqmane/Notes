package com.tqmane.notesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tqmane.notesapp.adapters.NotesAdapter;
import com.tqmane.notesapp.models.Note;
import com.tqmane.notesapp.storage.NoteStorage;

import java.util.List;

/**
 * メインアクティビティ - ノート一覧を表示
 */
public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    private static final int REQUEST_DRAWING = 1001;
    
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private NoteStorage storage;
    private FloatingActionButton fab;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // ストレージを初期化
        storage = new NoteStorage(this);
        
        // ビューを初期化
        setupViews();
        
        // ノート一覧を読み込み
        loadNotes();
    }
    
    private void setupViews() {
        // RecyclerViewのセットアップ
        recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        adapter = new NotesAdapter(storage, new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                openNote(note);
            }
            
            @Override
            public void onNoteLongClick(Note note) {
                showDeleteDialog(note);
            }
        });
        
        recyclerView.setAdapter(adapter);
        
        // FABのセットアップ
        fab = findViewById(R.id.fab_new_note);
        fab.setOnClickListener(v -> createNewNote());
    }
    
    private void loadNotes() {
        List<Note> notes = storage.getAllNotes();
        adapter.setNotes(notes);
        
        if (notes.isEmpty()) {
            Toast.makeText(this, "右下の+ボタンで新しいノートを作成", Toast.LENGTH_LONG).show();
        }
    }
    
    private void createNewNote() {
        Note note = new Note();
        note.setTitle("新規ノート " + (adapter.getItemCount() + 1));
        
        // ノートを保存
        if (storage.saveNote(note)) {
            openNote(note);
        } else {
            Toast.makeText(this, "ノートの作成に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openNote(Note note) {
        Intent intent = new Intent(this, DrawingActivity.class);
        intent.putExtra("note_id", note.getId());
        startActivityForResult(intent, REQUEST_DRAWING);
    }
    
    private void showDeleteDialog(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("ノートを削除")
                .setMessage("「" + note.getTitle() + "」を削除しますか？")
                .setPositiveButton("削除", (dialog, which) -> {
                    if (storage.deleteNote(note)) {
                        adapter.removeNote(note);
                        Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "削除に失敗しました", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_DRAWING && resultCode == RESULT_OK) {
            // ノート一覧を更新
            loadNotes();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("NotesAppについて")
                .setMessage("OPPO Stylus APIを使用した手書きノートアプリ\n\n" +
                        "機能:\n" +
                        "• 筆圧対応の描画\n" +
                        "• モーション予測\n" +
                        "• 複数ノート管理\n" +
                        "• カラーパレット\n\n" +
                        "バージョン: 1.0")
                .setPositiveButton("OK", null)
                .show();
    }
}


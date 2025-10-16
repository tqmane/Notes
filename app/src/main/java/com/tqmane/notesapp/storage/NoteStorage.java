package com.tqmane.notesapp.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmane.notesapp.models.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * ノートの保存・読み込みを管理するクラス
 */
public class NoteStorage {
    
    private static final String TAG = "NoteStorage";
    private static final String NOTES_FILE = "notes.json";
    private static final String IMAGES_DIR = "note_images";
    
    private Context context;
    private Gson gson;
    
    public NoteStorage(Context context) {
        this.context = context;
        this.gson = new Gson();
        
        // 画像ディレクトリを作成
        File imagesDir = new File(context.getFilesDir(), IMAGES_DIR);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
    }
    
    /**
     * すべてのノートを取得
     */
    public List<Note> getAllNotes() {
        try {
            File file = new File(context.getFilesDir(), NOTES_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            
            String json = new String(data, "UTF-8");
            Type listType = new TypeToken<ArrayList<Note>>(){}.getType();
            List<Note> notes = gson.fromJson(json, listType);
            
            return notes != null ? notes : new ArrayList<>();
            
        } catch (IOException e) {
            android.util.Log.e(TAG, "Error reading notes", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * ノートを保存
     */
    public boolean saveNote(Note note) {
        try {
            List<Note> notes = getAllNotes();
            
            // 既存のノートを更新、なければ追加
            boolean found = false;
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getId().equals(note.getId())) {
                    notes.set(i, note);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                notes.add(0, note); // 先頭に追加
            }
            
            return saveAllNotes(notes);
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error saving note", e);
            return false;
        }
    }
    
    /**
     * すべてのノートを保存
     */
    private boolean saveAllNotes(List<Note> notes) {
        try {
            String json = gson.toJson(notes);
            
            File file = new File(context.getFilesDir(), NOTES_FILE);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes("UTF-8"));
            fos.close();
            
            return true;
            
        } catch (IOException e) {
            android.util.Log.e(TAG, "Error saving all notes", e);
            return false;
        }
    }
    
    /**
     * ノートを削除
     */
    public boolean deleteNote(Note note) {
        try {
            List<Note> notes = getAllNotes();
            notes.removeIf(n -> n.getId().equals(note.getId()));
            
            // 画像も削除
            if (note.getImagePath() != null) {
                deleteImage(note.getImagePath());
            }
            
            return saveAllNotes(notes);
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error deleting note", e);
            return false;
        }
    }
    
    /**
     * 画像を保存
     */
    public String saveImage(Bitmap bitmap, String noteId) {
        try {
            File imagesDir = new File(context.getFilesDir(), IMAGES_DIR);
            String fileName = "note_" + noteId + ".png";
            File imageFile = new File(imagesDir, fileName);
            
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            
            return imageFile.getAbsolutePath();
            
        } catch (IOException e) {
            android.util.Log.e(TAG, "Error saving image", e);
            return null;
        }
    }
    
    /**
     * 画像を読み込み
     */
    public Bitmap loadImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                return null;
            }
            
            return BitmapFactory.decodeFile(imagePath);
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading image", e);
            return null;
        }
    }
    
    /**
     * 画像を削除
     */
    private boolean deleteImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                return file.delete();
            }
            return true;
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error deleting image", e);
            return false;
        }
    }
    
    /**
     * IDでノートを取得
     */
    public Note getNoteById(String noteId) {
        List<Note> notes = getAllNotes();
        for (Note note : notes) {
            if (note.getId().equals(noteId)) {
                return note;
            }
        }
        return null;
    }
}


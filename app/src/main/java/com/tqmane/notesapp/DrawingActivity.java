package com.tqmane.notesapp;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tqmane.notesapp.models.Note;
import com.tqmane.notesapp.storage.NoteStorage;
import com.tqmane.notesapp.views.DrawingView;

/**
 * 描画アクティビティ - ノートの編集画面
 */
public class DrawingActivity extends AppCompatActivity {
    
    private static final String TAG = "DrawingActivity";
    
    private DrawingView drawingView;
    private NoteStorage storage;
    private Note currentNote;
    
    // ツールバー
    private LinearLayout toolBar;
    private ImageButton btnUndo;
    private ImageButton btnClear;
    private ImageButton btnColor;
    private ImageButton btnStrokeWidth;
    private TextView tvStylusInfo;
    
    // カラーパレット
    private int[] colors = {
        Color.BLACK,
        Color.rgb(255, 0, 0),      // 赤
        Color.rgb(0, 0, 255),      // 青
        Color.rgb(0, 150, 0),      // 緑
        Color.rgb(255, 165, 0),    // オレンジ
        Color.rgb(128, 0, 128),    // 紫
        Color.rgb(255, 192, 203),  // ピンク
        Color.rgb(165, 42, 42)     // 茶色
    };
    
    private int currentColorIndex = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        
        // ストレージを初期化
        storage = new NoteStorage(this);
        
        // ノートを読み込み
        String noteId = getIntent().getStringExtra("note_id");
        if (noteId != null) {
            currentNote = storage.getNoteById(noteId);
            if (currentNote != null) {
                setTitle(currentNote.getTitle());
            }
        }
        
        // ビューを初期化
        setupViews();
        
        // 既存の描画を読み込み
        loadDrawing();
    }
    
    private void setupViews() {
        drawingView = findViewById(R.id.drawing_view);
        toolBar = findViewById(R.id.tool_bar);
        btnUndo = findViewById(R.id.btn_undo);
        btnClear = findViewById(R.id.btn_clear);
        btnColor = findViewById(R.id.btn_color);
        btnStrokeWidth = findViewById(R.id.btn_stroke_width);
        tvStylusInfo = findViewById(R.id.tv_stylus_info);
        
        // スタイラス情報を表示
        if (drawingView != null) {
            // スタイラスマネージャーの情報を取得する処理を追加できます
            tvStylusInfo.setText("タップして描画開始");
        }
        
        // ボタンのリスナーを設定
        btnUndo.setOnClickListener(v -> drawingView.undo());
        
        btnClear.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("クリア")
                    .setMessage("すべての描画を消去しますか？")
                    .setPositiveButton("消去", (dialog, which) -> drawingView.clear())
                    .setNegativeButton("キャンセル", null)
                    .show();
        });
        
        btnColor.setOnClickListener(v -> showColorPicker());
        
        btnStrokeWidth.setOnClickListener(v -> showStrokeWidthPicker());
        
        // カラーボタンの色を設定
        updateColorButton();
    }
    
    private void loadDrawing() {
        if (currentNote != null && currentNote.getImagePath() != null) {
            Bitmap bitmap = storage.loadImage(currentNote.getImagePath());
            if (bitmap != null) {
                drawingView.setBitmap(bitmap);
            }
        }
    }
    
    private void showColorPicker() {
        String[] colorNames = {"黒", "赤", "青", "緑", "オレンジ", "紫", "ピンク", "茶色"};
        
        new AlertDialog.Builder(this)
                .setTitle("色を選択")
                .setSingleChoiceItems(colorNames, currentColorIndex, (dialog, which) -> {
                    currentColorIndex = which;
                    drawingView.setColor(colors[which]);
                    updateColorButton();
                    dialog.dismiss();
                })
                .show();
    }
    
    private void showStrokeWidthPicker() {
        String[] widths = {"細い (3px)", "普通 (5px)", "太い (8px)", "極太 (12px)"};
        float[] widthValues = {3f, 5f, 8f, 12f};
        
        new AlertDialog.Builder(this)
                .setTitle("線の太さを選択")
                .setItems(widths, (dialog, which) -> {
                    drawingView.setStrokeWidth(widthValues[which]);
                })
                .show();
    }
    
    private void updateColorButton() {
        btnColor.setColorFilter(colors[currentColorIndex]);
    }
    
    private void saveDrawing() {
        if (currentNote == null) {
            return;
        }
        
        // ビットマップを取得
        Bitmap bitmap = drawingView.getBitmap();
        if (bitmap != null) {
            // 画像を保存
            String imagePath = storage.saveImage(bitmap, currentNote.getId());
            if (imagePath != null) {
                currentNote.setImagePath(imagePath);
                
                // ノートを保存
                if (storage.saveNote(currentNote)) {
                    Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private void showRenamedialog() {
        EditText editText = new EditText(this);
        editText.setText(currentNote.getTitle());
        editText.setSelectAllOnFocus(true);
        
        new AlertDialog.Builder(this)
                .setTitle("ノート名を変更")
                .setView(editText)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newTitle = editText.getText().toString().trim();
                    if (!newTitle.isEmpty()) {
                        currentNote.setTitle(newTitle);
                        setTitle(newTitle);
                        storage.saveNote(currentNote);
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawing, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_save) {
            saveDrawing();
            return true;
        } else if (id == R.id.action_rename) {
            showRenamedialog();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        // 自動保存
        saveDrawing();
        
        setResult(RESULT_OK);
        super.onBackPressed();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // バックグラウンドに移行する際も自動保存
        saveDrawing();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (drawingView != null) {
            drawingView.cleanup();
        }
    }
}


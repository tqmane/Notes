package com.tqmane.notesapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tqmane.notesapp.stylus.TouchPointInfo;
import com.tqmane.notesapp.stylus.UnifiedStylusManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 筆圧対応の描画ビュー
 * UnifiedStylusManagerと統合して高度な描画機能を提供
 */
public class DrawingView extends View {
    
    private static final String TAG = "DrawingView";
    
    // 描画関連
    private Paint paint;
    private Paint predictedPaint;
    private Path currentPath;
    private List<DrawingStroke> strokes;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    
    // スタイラス管理
    private UnifiedStylusManager stylusManager;
    
    // 設定
    private int currentColor = Color.BLACK;
    private float baseStrokeWidth = 5f;
    private boolean showPrediction = true;
    private List<TouchPointInfo> currentPredictedPoints;
    
    /**
     * 一つの筆跡を表すクラス
     */
    private static class DrawingStroke {
        Path path;
        Paint paint;
        List<TouchPointInfo> points;
        
        DrawingStroke(Paint paint) {
            this.path = new Path();
            this.paint = new Paint(paint);
            this.points = new ArrayList<>();
        }
    }
    
    public DrawingView(Context context) {
        super(context);
        init();
    }
    
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        // メイン描画用ペイント
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(baseStrokeWidth);
        
        // 予測線用ペイント
        predictedPaint = new Paint(paint);
        predictedPaint.setAlpha(100);
        predictedPaint.setStrokeWidth(baseStrokeWidth * 0.7f);
        
        strokes = new ArrayList<>();
        currentPath = new Path();
        currentPredictedPoints = new ArrayList<>();
        
        // スタイラスマネージャーの初期化
        initStylusManager();
    }
    
    private void initStylusManager() {
        stylusManager = new UnifiedStylusManager(getContext(), new UnifiedStylusManager.StylusEventCallback() {
            @Override
            public void onStylusDown(TouchPointInfo point) {
                handleStylusDown(point);
            }
            
            @Override
            public void onStylusMove(TouchPointInfo point, List<TouchPointInfo> predictedPoints) {
                handleStylusMove(point, predictedPoints);
            }
            
            @Override
            public void onStylusUp(TouchPointInfo point) {
                handleStylusUp(point);
            }
        });
        stylusManager.initialize();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        if (w > 0 && h > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
            bitmapCanvas.drawColor(Color.WHITE);
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // ビットマップに描画された内容を表示
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        
        // 現在の筆跡を描画
        if (currentPath != null) {
            canvas.drawPath(currentPath, paint);
        }
        
        // 予測ポイントを描画
        if (showPrediction && currentPredictedPoints != null && !currentPredictedPoints.isEmpty()) {
            Path predictedPath = new Path();
            TouchPointInfo first = currentPredictedPoints.get(0);
            predictedPath.moveTo(first.getX(), first.getY());
            
            for (int i = 1; i < currentPredictedPoints.size(); i++) {
                TouchPointInfo point = currentPredictedPoints.get(i);
                predictedPath.lineTo(point.getX(), point.getY());
            }
            
            canvas.drawPath(predictedPath, predictedPaint);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (stylusManager != null) {
            stylusManager.handleTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }
    
    private void handleStylusDown(TouchPointInfo point) {
        currentPath = new Path();
        currentPath.moveTo(point.getX(), point.getY());
        
        // 筆圧に応じて線の太さを調整
        float pressureWidth = baseStrokeWidth * (0.5f + point.getPressure() * 1.5f);
        paint.setStrokeWidth(pressureWidth);
        
        invalidate();
    }
    
    private void handleStylusMove(TouchPointInfo point, List<TouchPointInfo> predictedPoints) {
        if (currentPath != null) {
            currentPath.lineTo(point.getX(), point.getY());
            
            // 筆圧に応じて線の太さを調整
            float pressureWidth = baseStrokeWidth * (0.5f + point.getPressure() * 1.5f);
            paint.setStrokeWidth(pressureWidth);
            
            // 予測ポイントを保存
            currentPredictedPoints = predictedPoints;
            
            invalidate();
        }
    }
    
    private void handleStylusUp(TouchPointInfo point) {
        if (currentPath != null) {
            // 最終ポイントを追加
            currentPath.lineTo(point.getX(), point.getY());
            
            // ビットマップに描画を確定
            if (bitmapCanvas != null) {
                bitmapCanvas.drawPath(currentPath, paint);
            }
            
            // 筆跡を保存
            DrawingStroke stroke = new DrawingStroke(paint);
            stroke.path = new Path(currentPath);
            strokes.add(stroke);
            
            currentPath = new Path();
            currentPredictedPoints.clear();
            
            invalidate();
        }
    }
    
    /**
     * 線の色を設定
     */
    public void setColor(int color) {
        this.currentColor = color;
        paint.setColor(color);
        predictedPaint.setColor(color);
        predictedPaint.setAlpha(100);
    }
    
    /**
     * 線の太さを設定
     */
    public void setStrokeWidth(float width) {
        this.baseStrokeWidth = width;
        paint.setStrokeWidth(width);
        predictedPaint.setStrokeWidth(width * 0.7f);
    }
    
    /**
     * キャンバスをクリア
     */
    public void clear() {
        strokes.clear();
        currentPath = new Path();
        currentPredictedPoints.clear();
        
        if (bitmapCanvas != null) {
            bitmapCanvas.drawColor(Color.WHITE);
        }
        
        invalidate();
    }
    
    /**
     * 最後の筆跡を削除（Undo）
     */
    public void undo() {
        if (!strokes.isEmpty()) {
            strokes.remove(strokes.size() - 1);
            redrawAll();
        }
    }
    
    /**
     * すべての筆跡を再描画
     */
    private void redrawAll() {
        if (bitmapCanvas != null) {
            bitmapCanvas.drawColor(Color.WHITE);
            
            for (DrawingStroke stroke : strokes) {
                bitmapCanvas.drawPath(stroke.path, stroke.paint);
            }
            
            invalidate();
        }
    }
    
    /**
     * ビットマップを取得
     */
    public Bitmap getBitmap() {
        return bitmap;
    }
    
    /**
     * ビットマップから復元
     */
    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null && bitmapCanvas != null) {
            bitmapCanvas.drawBitmap(bitmap, 0, 0, null);
            invalidate();
        }
    }
    
    /**
     * 予測表示の有効/無効を設定
     */
    public void setShowPrediction(boolean show) {
        this.showPrediction = show;
        invalidate();
    }
    
    /**
     * クリーンアップ
     */
    public void cleanup() {
        if (stylusManager != null) {
            stylusManager.cleanup();
        }
        
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}


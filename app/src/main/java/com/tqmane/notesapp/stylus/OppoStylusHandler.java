package com.tqmane.notesapp.stylus;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * OPPO/OnePlus デバイス用スタイラスハンドラー
 * VFX SDK の機能を統合
 */
public class OppoStylusHandler implements StylusHandler {
    
    private static final String TAG = "OppoStylusHandler";
    private Context context;
    private boolean isInitialized = false;
    private List<TouchPointInfo> pointHistory = new ArrayList<>();
    private static final int HISTORY_SIZE = 10;
    
    @Override
    public void initialize(Context context) {
        this.context = context;
        
        // OPPO/OnePlus デバイスかチェック
        if (isSupported()) {
            isInitialized = true;
            // ネイティブライブラリの読み込みを試行（オプション）
            try {
                System.loadLibrary("forecast");
            } catch (UnsatisfiedLinkError e) {
                // ライブラリが利用できない場合は標準処理にフォールバック
                android.util.Log.w(TAG, "Native forecast library not available, using fallback");
            }
        }
    }
    
    @Override
    public boolean isSupported() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        return manufacturer.contains("oppo") || manufacturer.contains("oneplus");
    }
    
    @Override
    public TouchPointInfo processTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float pressure = event.getPressure();
        
        // 傾き情報を取得（利用可能な場合）
        float tiltX = 0, tiltY = 0;
        if (event.getAxisValue(MotionEvent.AXIS_TILT) != 0) {
            tiltX = event.getAxisValue(MotionEvent.AXIS_TILT);
            tiltY = event.getAxisValue(MotionEvent.AXIS_ORIENTATION);
        }
        
        TouchPointInfo point = new TouchPointInfo(x, y, pressure, tiltX, tiltY);
        
        // 履歴に追加
        pointHistory.add(point);
        if (pointHistory.size() > HISTORY_SIZE) {
            pointHistory.remove(0);
        }
        
        return point;
    }
    
    @Override
    public List<TouchPointInfo> getPredictedPoints(TouchPointInfo currentPoint) {
        List<TouchPointInfo> predictedPoints = new ArrayList<>();
        
        // 簡易的な線形予測（実際のVFX SDKでは高度なアルゴリズムを使用）
        if (pointHistory.size() >= 2) {
            TouchPointInfo lastPoint = pointHistory.get(pointHistory.size() - 1);
            TouchPointInfo secondLastPoint = pointHistory.get(pointHistory.size() - 2);
            
            float dx = lastPoint.getX() - secondLastPoint.getX();
            float dy = lastPoint.getY() - secondLastPoint.getY();
            
            // 3つの予測ポイントを生成
            for (int i = 1; i <= 3; i++) {
                float predX = lastPoint.getX() + (dx * i * 0.8f);
                float predY = lastPoint.getY() + (dy * i * 0.8f);
                predictedPoints.add(new TouchPointInfo(predX, predY, lastPoint.getPressure()));
            }
        }
        
        return predictedPoints;
    }
    
    @Override
    public void cleanup() {
        pointHistory.clear();
        isInitialized = false;
    }
    
    @Override
    public String getVendorName() {
        return "OPPO/OnePlus";
    }
}


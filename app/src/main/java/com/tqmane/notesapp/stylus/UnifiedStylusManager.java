package com.tqmane.notesapp.stylus;

import android.content.Context;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 統合スタイラスマネージャー
 * デバイスを自動検出し、適切なハンドラーを使用
 */
public class UnifiedStylusManager {
    
    private static final String TAG = "UnifiedStylusManager";
    private Context context;
    private StylusHandler currentHandler;
    private StylusEventCallback callback;
    private List<StylusHandler> availableHandlers;
    
    public interface StylusEventCallback {
        void onStylusDown(TouchPointInfo point);
        void onStylusMove(TouchPointInfo point, List<TouchPointInfo> predictedPoints);
        void onStylusUp(TouchPointInfo point);
    }
    
    public UnifiedStylusManager(Context context, StylusEventCallback callback) {
        this.context = context;
        this.callback = callback;
        this.availableHandlers = new ArrayList<>();
        
        // 利用可能なハンドラーを登録
        availableHandlers.add(new OppoStylusHandler());
        // 他のベンダーのハンドラーもここに追加可能
        // availableHandlers.add(new SamsungSPenHandler());
        // availableHandlers.add(new HuaweiHonorStylusHandler());
        
        // 汎用ハンドラーは最後のフォールバック
        availableHandlers.add(new GenericStylusHandler());
    }
    
    /**
     * マネージャーを初期化し、最適なハンドラーを選択
     */
    public void initialize() {
        for (StylusHandler handler : availableHandlers) {
            handler.initialize(context);
            if (handler.isSupported()) {
                currentHandler = handler;
                android.util.Log.i(TAG, "Selected handler: " + handler.getVendorName());
                break;
            }
        }
        
        if (currentHandler == null) {
            android.util.Log.w(TAG, "No supported handler found, using generic");
            currentHandler = new GenericStylusHandler();
            currentHandler.initialize(context);
        }
    }
    
    /**
     * タッチイベントを処理
     */
    public boolean handleTouchEvent(MotionEvent event) {
        if (currentHandler == null) {
            return false;
        }
        
        TouchPointInfo point = currentHandler.processTouchEvent(event);
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (callback != null) {
                    callback.onStylusDown(point);
                }
                break;
                
            case MotionEvent.ACTION_MOVE:
                if (callback != null) {
                    List<TouchPointInfo> predictedPoints = currentHandler.getPredictedPoints(point);
                    callback.onStylusMove(point, predictedPoints);
                }
                break;
                
            case MotionEvent.ACTION_UP:
                if (callback != null) {
                    callback.onStylusUp(point);
                }
                break;
        }
        
        return true;
    }
    
    /**
     * 現在のハンドラーを取得
     */
    public StylusHandler getCurrentHandler() {
        return currentHandler;
    }
    
    /**
     * リソースを解放
     */
    public void cleanup() {
        if (currentHandler != null) {
            currentHandler.cleanup();
        }
    }
    
    /**
     * コールバックを設定
     */
    public void setCallback(StylusEventCallback callback) {
        this.callback = callback;
    }
}


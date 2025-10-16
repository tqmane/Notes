package com.tqmane.notesapp.stylus;

import android.content.Context;
import android.view.MotionEvent;
import java.util.List;

/**
 * スタイラスハンドラーの基底インターフェース
 */
public interface StylusHandler {
    
    /**
     * ハンドラーを初期化
     */
    void initialize(Context context);
    
    /**
     * このハンドラーが現在のデバイスをサポートしているか
     */
    boolean isSupported();
    
    /**
     * タッチイベントを処理
     */
    TouchPointInfo processTouchEvent(MotionEvent event);
    
    /**
     * 予測ポイントを取得（サポートしている場合）
     */
    List<TouchPointInfo> getPredictedPoints(TouchPointInfo currentPoint);
    
    /**
     * リソースを解放
     */
    void cleanup();
    
    /**
     * ベンダー名を取得
     */
    String getVendorName();
}


package com.tqmane.notesapp.stylus;

import android.content.Context;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 汎用スタイラスハンドラー
 * すべてのAndroidデバイスで動作するフォールバック実装
 */
public class GenericStylusHandler implements StylusHandler {
    
    private static final String TAG = "GenericStylusHandler";
    private Context context;
    private boolean isInitialized = false;
    
    @Override
    public void initialize(Context context) {
        this.context = context;
        isInitialized = true;
    }
    
    @Override
    public boolean isSupported() {
        // 汎用ハンドラーは常にサポート
        return true;
    }
    
    @Override
    public TouchPointInfo processTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float pressure = event.getPressure();
        
        // ツールタイプをチェック（スタイラスか指か）
        int toolType = event.getToolType(0);
        if (toolType == MotionEvent.TOOL_TYPE_STYLUS) {
            // スタイラス使用時は筆圧情報を活用
            pressure = Math.max(pressure, 0.1f);
        } else {
            // 指の場合はデフォルトの筆圧
            pressure = 0.5f;
        }
        
        return new TouchPointInfo(x, y, pressure);
    }
    
    @Override
    public List<TouchPointInfo> getPredictedPoints(TouchPointInfo currentPoint) {
        // 汎用ハンドラーでは予測なし
        return new ArrayList<>();
    }
    
    @Override
    public void cleanup() {
        isInitialized = false;
    }
    
    @Override
    public String getVendorName() {
        return "Generic";
    }
}


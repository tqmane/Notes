package com.tqmane.notesapp.stylus;

/**
 * タッチポイント情報を保持するクラス
 * OPPO/OnePlus VFX SDKから抽出
 */
public class TouchPointInfo {
    private float x;
    private float y;
    private float pressure;
    private float tiltX;
    private float tiltY;
    private long timestamp;
    
    public TouchPointInfo(float x, float y, float pressure) {
        this.x = x;
        this.y = y;
        this.pressure = pressure;
        this.tiltX = 0;
        this.tiltY = 0;
        this.timestamp = System.currentTimeMillis();
    }
    
    public TouchPointInfo(float x, float y, float pressure, float tiltX, float tiltY) {
        this.x = x;
        this.y = y;
        this.pressure = pressure;
        this.tiltX = tiltX;
        this.tiltY = tiltY;
        this.timestamp = System.currentTimeMillis();
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getPressure() { return pressure; }
    public float getTiltX() { return tiltX; }
    public float getTiltY() { return tiltY; }
    public long getTimestamp() { return timestamp; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setPressure(float pressure) { this.pressure = pressure; }
    public void setTiltX(float tiltX) { this.tiltX = tiltX; }
    public void setTiltY(float tiltY) { this.tiltY = tiltY; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}


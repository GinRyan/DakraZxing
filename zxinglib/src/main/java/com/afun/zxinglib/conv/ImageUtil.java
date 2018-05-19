package com.afun.zxinglib.conv;

import android.graphics.Bitmap;
import android.hardware.Camera;

public interface ImageUtil {
    public Bitmap onPreviewFrame(byte[] data, Camera camera);
}

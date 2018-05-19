package com.afun.zxinglib.conv;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

import com.afun.zxinglib.DisplayUtils;

import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;

public class YUVImageUtil implements ImageUtil {
    private Context ctx;

    public YUVImageUtil(Context ctx) {
        this.ctx = ctx;
    }


    public Bitmap onPreviewFrame(byte[] data, Camera camera) {
        int prevSizeW;
        int prevSizeH;
        prevSizeH = camera.getParameters().getPreviewSize().height;
        prevSizeW = camera.getParameters().getPreviewSize().width;

        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                prevSizeW,
                prevSizeH,
                null);//data是onPreviewFrame参数提供

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, yuvimage.getWidth(), yuvimage.getHeight()), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.RGB_565;   //默认8888
        //options.inSampleSize = 8;
        SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options));//方便回收
        Bitmap bitmap = softRef.get();
        return bitmap;

    }
}

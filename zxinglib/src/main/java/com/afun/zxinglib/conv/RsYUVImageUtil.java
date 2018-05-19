package com.afun.zxinglib.conv;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
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

/**
 * API 17以上使用这个类，可以加速生成Bitmap
 */
public class RsYUVImageUtil implements ImageUtil {
    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType, rgbaType;
    private Allocation in, out;
    private Context ctx;

    public RsYUVImageUtil(Context ctx) {
        this.ctx = ctx;
        rs = RenderScript.create(ctx);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }


    public Bitmap onPreviewFrame(byte[] data, Camera camera) {
        int prevSizeW;
        int prevSizeH;
        prevSizeH = camera.getParameters().getPreviewSize().height;
        prevSizeW = camera.getParameters().getPreviewSize().width;

        if (yuvType == null) {
            yuvType = new Type.Builder(rs, Element.U8(rs)).setX(data.length);
            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(prevSizeW).setY(prevSizeH);
            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        }

        in.copyFrom(data);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);

        Matrix matrix = new Matrix();
        if (DisplayUtils.getScreenOrientation(ctx) == Configuration.ORIENTATION_PORTRAIT) {
            matrix.setRotate(90);
        }

        Bitmap bmpout = Bitmap.createBitmap(prevSizeW, prevSizeH, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);

        Bitmap bitmap2 = Bitmap.createBitmap(bmpout, 0, 0, bmpout.getWidth(), bmpout.getHeight(), matrix, false);
        return bitmap2;

    }
}

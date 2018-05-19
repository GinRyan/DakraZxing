package com.afun.zxinglib.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afun.zxinglib.R;
import com.afun.zxinglib.ScanView.ZXingScannerViewNew;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * QR reader demo.
 */
public class QRReaderActivity extends Activity {

    ImageView ret_image;
    private android.widget.EditText result;
    ZXingScannerViewNew scanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanView = new ZXingScannerViewNew(this);
        scanView.setContentView(R.layout.activity_qrreader);
        setContentView(scanView);
        result = findViewById(R.id.result);
        ret_image = findViewById(R.id.ret_image);

        scanView.setQrSize(new ZXingScannerViewNew.QrSize() {
            @Override
            public Rect getDetectRect() {
                return QRReaderActivity.this.getDetectRect();
            }
        });
        scanView.setOnResultCapture(new ZXingScannerViewNew.OnResultCapture() {
            @Override
            public void onCapture(Bitmap bitmap) {
                ret_image.setImageBitmap(bitmap);
            }
        });
        ret_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ret_image.setImageBitmap(null);
            }
        });
        setupFormats();
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanView.setResultHandler(new ZXingScannerViewNew.ResultHandler() {
            @Override
            public void handleResult(Result rawResult) {
                String ret = rawResult.getText();
                result.setText(ret);
            }
        });
        scanView.startCamera(-1);
        scanView.setFlash(false);
        scanView.setAutoFocus(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanView.stopCamera();
    }

    /**
     * 管他IC……IP……IQ码通通给我扫出来
     */
    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<>(Arrays.asList(BarcodeFormat.values()));
        if (scanView != null) {
            scanView.setFormats(formats);
        }
    }

    public Rect getDetectRect() {
        View view = findViewById(R.id.scan_window);
        int top = ((View) view.getParent()).getTop() + view.getTop();
        int left = view.getLeft();
        int width = view.getWidth();
        int height = view.getHeight();
        //用于画线的矩形
        Rect rect = null;
        if (width != 0 && height != 0) {
            rect = new Rect(left - 60, top - 60, left + width + 60, top + height + 60);
            addLineAnim(rect);
        }
        //但是识别仍然要放在全屏扫描
        Rect fullScreenRect = new Rect();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        fullScreenRect.set(0, 0, dm.widthPixels, dm.heightPixels);
        return rect;
    }

    private void addLineAnim(Rect rect) {
        ImageView imageView = (ImageView) findViewById(R.id.scanner_line);
        imageView.setVisibility(View.VISIBLE);
        if (imageView.getAnimation() == null) {
            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, rect.height());
            anim.setDuration(3500);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);
        }
    }

}

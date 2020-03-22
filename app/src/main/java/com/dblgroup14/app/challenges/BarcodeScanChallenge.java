package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.dblgroup14.app.R;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class BarcodeScanChallenge extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    
    private ZXingScannerView scannerView;
    private static final int REQUEST_CAMERA = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan_challenge);
        
        
        //permission
        int currentApiVersion = Build.VERSION.SDK_INT;
    
        if(currentApiVersion >=  Build.VERSION_CODES.M) {
            if(!checkPermission()) {
                requestPermission();
            }
        }
    }
    
    public void scanCode(View view) {
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(this::handleResult);
        setContentView(scannerView);
        scannerView.startCamera();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void handleResult(Result result) {
        String resultcode = result.getText();
        Toast.makeText(BarcodeScanChallenge.this, resultcode + "Well done, you have woken up!", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_barcode_scan_challenge);
        scannerView.stopCamera();
        finish();
    }
    
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(BarcodeScanChallenge.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }
}

package com.dblgroup14.app.challenges;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.dblgroup14.app.R;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

/**
 * For reading the barcodes and QR codes in the barcode challenge we used zxing libary 'me.dm7.barcodescanner:zxing:1.9'
 */

public class BarcodeChallengeFragment extends ChallengeFragment implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA_ID = 1;
    
    private ViewGroup alarmActivityContainer;
    private ViewGroup alarmActivityScrollView;
    
    private ZXingScannerView scannerView;
    private boolean scannerIsRunning = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_barcode, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get alarm activity components
        alarmActivityContainer = getActivity().findViewById(R.id.alarmActivityContainer);
        alarmActivityScrollView = getActivity().findViewById(R.id.alarmActivityScroll);
        
        // Request camera permission
        checkAndRequestPermission();
        
        // Add click listener to scan button
        view.findViewById(R.id.scanBtn).setOnClickListener(v -> {
            if (checkAndRequestPermission()) {
                startScanner();
            }
        });
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                Toast.makeText(getContext(), "You must allow this app to use the camera! Otherwise, the challenge cannot be completed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        if (scannerView != null && scannerIsRunning) {
            stopScanner();
        }
    }
    
    private boolean checkAndRequestPermission() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
                return false;
            }
        }
        return true;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermission() {
        return getContext().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String[] {CAMERA}, REQUEST_CAMERA_ID);
    }
    
    private void startScanner() {
        // Initialize scanner view
        if (scannerView == null) {
            scannerView = new ZXingScannerView(getActivity());
            scannerView.setResultHandler(this);
        }
        
        // Swap scanner view
        alarmActivityContainer.removeAllViews();
        alarmActivityContainer.addView(scannerView);
        
        // Start camera
        scannerView.startCamera();
        scannerIsRunning = true;
    }
    
    private void stopScanner() {
        scannerIsRunning = false;
        
        // Stop camera
        scannerView.stopCamera();
        
        // Swap back views
        alarmActivityContainer.removeAllViews();
        alarmActivityContainer.addView(alarmActivityScrollView);
    }
    
    @Override
    public void handleResult(Result result) {
        // Stop scanner
        stopScanner();
        
        // Show message to user
        String resultCode = result.getText();
        Toast.makeText(getContext(), resultCode + "\nWell done, you have woken up!", Toast.LENGTH_LONG).show();
        
        // Complete challenge
        completeChallenge();
    }
}

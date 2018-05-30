package com.katherineplazas.lab02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by KATHE on 12/05/2018.
 */

public class zsingscanner extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    String dato;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        dato = result.getText();
        Intent intent =new Intent(zsingscanner.this,Principal_Conductor_Activity.class);
        intent.putExtra("DATO",dato);
        startActivity(intent);
        finish();
    }

    private void goPrincipalConductor() {
        Intent intent =new Intent(zsingscanner.this,Principal_Conductor_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goPrincipalConductor();

    }
}

package com.app.barbearias;

import android.app.AlertDialog;
import android.content.Context;

public class loadingActivity {
    private AlertDialog dialog;
    public void init(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.activity_loading);
        // This should be called once in your Fragment's onViewCreated() or in Activity onCreate() method to avoid dialog duplicates.
        this.dialog = builder.create();
    }

    public void show() {
        this.dialog.show();
    }

    public void hide() {
        this.dialog.hide();
    }
}
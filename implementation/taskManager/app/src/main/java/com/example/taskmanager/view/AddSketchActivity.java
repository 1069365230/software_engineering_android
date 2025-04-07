package com.example.taskmanager.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;

public class AddSketchActivity extends AppCompatActivity {

    private MyCanvas canvas;

    private Button closeButton;
    private Button saveButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_sketch);
        linkResourcesFromXml();

        setSaveButton();
        setCloseButton();
    }

    private void linkResourcesFromXml() {
        canvas = new MyCanvas(this, null);

        saveButton = findViewById(R.id.button_save_sketch);
        closeButton = findViewById(R.id.button_close_sketch);
    }

    private void setSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_add_task);
                AddSketchActivity.this.finish();
            }
        });
    }

    private void setCloseButton() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_add_task);
            }
        });
    }
}
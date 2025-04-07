package com.example.taskmanager.view;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UI extends AppCompatActivity {

    private Context context;

    public UI(Context context) {
        this.context = context;
    }

    public void createMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

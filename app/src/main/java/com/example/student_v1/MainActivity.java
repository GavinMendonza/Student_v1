package com.example.student_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit;
        EditText name,uid,className;

        name=findViewById(R.id.editTextForName);
        uid=findViewById(R.id.editTextForUID);
        className=findViewById(R.id.editTextForClass);
        submit=findViewById(R.id.button2);

        submit.setOnClickListener(view -> {
            Intent i = new Intent(this,MainActivity2.class);
            i.putExtra("message_key", name.getText()+"#"+uid.getText()+"#"+className.getText());
            startActivity(i);
        });

    }
}
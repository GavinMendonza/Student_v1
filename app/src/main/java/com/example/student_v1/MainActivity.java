package com.example.student_v1;

//v1: added qr generation
//v1.2: added login,
//      sxc acc verification
//      app now directly takes student data from db

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView nameTV,scoreTV;
    Button logoutBtn;

    String name, uid, className;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit;
//        EditText name,uid,className;

//        name=findViewById(R.id.editTextForName);
//        uid=findViewById(R.id.editTextForUID);
//        className=findViewById(R.id.editTextForClass);
        submit=findViewById(R.id.button2);

//        verifying valid sxc acc starts here
        nameTV=findViewById(R.id.textView);
        logoutBtn=findViewById(R.id.button);
        scoreTV=findViewById(R.id.textView2);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);

//        firebase assistant code for reading starts here
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://eccloginmoduletest-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap value = (HashMap) snapshot.getValue();


//                inefficient method, change this later
                int flag=0;
                String scoreMessage="";
                for(Object s: value.keySet()){
                    HashMap switchMap = (HashMap) value.get(s);
                    if(switchMap.containsValue(account.getEmail())){
                        nameTV.setText("Welcome "+switchMap.get("name"));
                        name= (String) switchMap.get("name");
                        uid= (String) s;
                        className= (String) switchMap.get("class");
//                        scoreTV.setText( "Ecc score: "+(Long)switchMap.get("score"));
//                        scoreMessage="Ecc score: "+switchMap.get("score");
                        scoreTV.setText("Ecc Score: "+switchMap.get("score"));
                        Log.d("database", "onDataChange: "+switchMap.get("score"));
                        flag=1;
                    }
                }
//                scoreTV.setText(scoreMessage);
                if (flag==0){
                    Toast.makeText(getApplicationContext(), "not a valid sxc acc", Toast.LENGTH_SHORT).show();
                    logOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("database error", "Failed to read value.", error.toException());
            }
        });
//        firebase assistant code for reading ends here
//        verifying valid sxc acc ends here

        logoutBtn.setOnClickListener(view -> {
            logOut();

        });


        submit.setOnClickListener(view -> {
            Intent i = new Intent(this,MainActivity2.class);
//            i.putExtra("message_key", name.getText()+"#"+uid.getText()+"#"+className.getText());
            i.putExtra("message_key", name+"#"+uid+"#"+className);
            startActivity(i);
        });

    }

    private void logOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
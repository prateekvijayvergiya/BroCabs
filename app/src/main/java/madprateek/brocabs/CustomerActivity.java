package madprateek.brocabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerActivity extends AppCompatActivity {

    private TextInputLayout mEmail,mPass;
    private Button mLogin;
    private TextView mReg;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;
    private ProgressDialog mRegProgress;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        mEmail = (TextInputLayout) findViewById(R.id.customerEmailBtn);
        mPass = (TextInputLayout) findViewById(R.id.customerPassBtn);
        mLogin = (Button) findViewById(R.id.customerLoginBtn);
        mReg = (TextView) findViewById(R.id.customerRegBtn);
        mAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);
        mRegProgress = new ProgressDialog(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPass.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please Wait");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email,password);
                }
            }
        });

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPass.getEditText().getText().toString();

                if ( !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Loading");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    registerUser(email,password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(CustomerActivity.this,MapActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else {
                    mLoginProgress.hide();
                    Toast.makeText(CustomerActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            String email = mEmail.getEditText().getText().toString();

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(uid);

                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("Email",email);
                            databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CustomerActivity.this, "Authentication Done.", Toast.LENGTH_SHORT).show();
                                    mRegProgress.dismiss();
                                    Intent mainIntent = new Intent(CustomerActivity.this,MapActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            });

                        } else {
                            mRegProgress.hide();
                            Toast.makeText(CustomerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}

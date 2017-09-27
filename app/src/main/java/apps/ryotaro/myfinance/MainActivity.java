package apps.ryotaro.myfinance;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        loginButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // already Login
            ((TextView) findViewById(R.id.textView)).setText("ログインしてるよ");
        } else {
            // not login
            ((TextView) findViewById(R.id.textView)).setText("ログインしてないよ");
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.login_button) {
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    ((TextView) findViewById(R.id.textView)).setText("ログインしてるよ");
                                } else {
                                    // If sign in fails, display a message to the user.
                                    ((TextView) findViewById(R.id.textView)).setText("ログイン失敗");
                                }
                            }
                        });
            } else if (v.getId() == R.id.logout_button) {
                mAuth.signOut();
            }
        }
    }
}

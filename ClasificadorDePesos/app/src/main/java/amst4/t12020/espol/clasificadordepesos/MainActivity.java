package amst4.t12020.espol.clasificadordepesos;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.Serializable;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //Variables para nuestro login
    private EditText nombre,pasdword;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    private Button btnGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        //Hilo usado para mostrar el splahs activity, luego de esto
        //no es recomendable dormir al hilo en el mainactivity
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //isntaciamos nuestros edistext de sesion
        nombre=(EditText)findViewById(R.id.etUsuario);
        pasdword=(EditText)findViewById(R.id.edContrasena);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("532856539478-8v7llo0hoopoc26gvpq0kgr31tem2i6q.apps.googleusercontent.com")
                .requestEmail()
                .build();

        btnGoogleLogin = findViewById(R.id.btnLoginGoogle);

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        //to remove "information bar" above the action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove the action bar (title bar)
        getSupportActionBar().hide();
    }

    //Metodo para el boton acceder

    public void Acceder(View view){
        HashMap<String,String> datos= new HashMap<>();
        datos.put("jadriandelgado17@gmail.com","Hola123");

        String nombreV=nombre.getText().toString();
        String password=pasdword.getText().toString();

        if(nombre.length()==0){
            Toast.makeText(this,"Debe Ingresar un usuaio valido", Toast.LENGTH_LONG).show();
        }

        //if (password != datos.get("jadriandelgado17@gmail.com")){
        if (password.length() == 0 ){
            Toast.makeText(this,"Contraseña Incorrecta",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Contraseña correcta",Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        String nombre;
        String correo;
        String urlFoto;

        if(user!= null){
            nombre = user.getDisplayName();
            correo = user.getEmail();
            urlFoto = user.getPhotoUrl().toString();
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            intent.putExtra("nombreUser",nombre);
            intent.putExtra("correoUser",correo);
            intent.putExtra("uriFotoUser",urlFoto);
            startActivity(intent);
        }
    }


}



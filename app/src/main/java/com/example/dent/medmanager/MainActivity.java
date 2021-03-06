package com.example.dent.medmanager;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    ImageView imageView;
    TextView userNameTextView;
    TextView userEmailTextView;
    String personName;
    String personEmail;
    String personGivenName;
    String personFamilyName;
    String personId;
    Uri personPhoto;
    Toolbar toolbar;
    SharedPreferencesHelper sph;
    private final String NAME = "person_name";
    private final String EMAIL = "person_email";
    private final String PHOTO = "person_photo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //initialize sharedPreferences for handling user profile
        sph = new SharedPreferencesHelper(this);

        //setup the action bar for navigational support
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (false);

        //activate the navigation drawer
        DrawerUtil.getDrawer(this,toolbar, this);

        //use specify
        TextView medManager = (TextView)findViewById(R.id.tv_med_manager);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/RaviPrakash-Regular.ttf");

        medManager.setTypeface(custom_font);
    }
    @Override
    public void onStart(){
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Intent intent = this.getIntent();
        if(acct != null && intent.hasExtra("logout")){
            signOut();
        }
        else if(acct != null){
            userCredentialsSetup(acct);
        }
    }

    //a custom setup to fill the login details into the profile sharedPreference
    public void userCredentialsSetup(GoogleSignInAccount acct){
        personName = acct.getDisplayName();
        personGivenName = acct.getGivenName();
        personFamilyName = acct.getFamilyName();
        personEmail = acct.getEmail();
        personId = acct.getId();
        personPhoto = acct.getPhotoUrl();
        Log.d("signed_in_before", personName);
        Log.d("signed_in_be", personEmail);
        Log.d("signed_in_befe", personPhoto.toString());
        sph.putString(NAME, personName);
        sph.putString(EMAIL, personEmail);
        sph.putString(PHOTO, personPhoto.toString());
        DrawerUtil.getDrawer(this,toolbar, this);
        Intent intent = new Intent(this, MedicationGalleryActivity.class);
        startActivity(intent);
    }

    //The handler for goole sign in request intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //The function for handling googlesignIn response
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            userCredentialsSetup(account);
            // Signed in successfully, show authenticated UI.
            Log.d("signed_in_now", account.toString());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signed_in_failed", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,
                    "Unable to signin please try again later", Toast.LENGTH_LONG).show();
        }
    }

    //signin handler for the signin button
    public void signIn(View view){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //the signout handler
    public void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sph.putString(NAME, "");
                        sph.putString(EMAIL, "");
                        sph.putString(PHOTO, "");
                    }
                });
    }
}

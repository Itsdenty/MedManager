package com.example.dent.medmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText phoneNumberEditView;
    EditText ageEditView;
    EditText locationEditView;
    SharedPreferencesHelper sph;
    private final String PHONE = "person_phone";
    private final String LOCATION = "person_location";
    private final String AGE = "person_age";
    final String PHOTO = "person_photo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar4);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        phoneNumberEditView = (EditText) findViewById(R.id.et_phone);
        ageEditView = (EditText) findViewById(R.id.et_age);
        locationEditView = (EditText) findViewById(R.id.et_location);
        sph = new SharedPreferencesHelper(this);
        if(sph.getString(PHONE) != ""){
            String phone = sph.getString(PHONE);
            phoneNumberEditView.setText(phone);
        }
        if(sph.getString(LOCATION) != ""){
            String location = sph.getString(LOCATION);
            locationEditView.setText(location);
        }
        if(sph.getInt(AGE) != 0){
            int age = sph.getInt(AGE);
            ageEditView.setText(String.valueOf(age));
        }
        DrawerUtil.getDrawer(this,toolbar, this);
    }
    public void onSubmit(View view){
        String phoneNumber = phoneNumberEditView.getText().toString();
        String location = locationEditView.getText().toString();
        int age = Integer.parseInt(ageEditView.getText().toString());
        sph.putString(PHONE, phoneNumber);
        sph.putString(LOCATION, location);
        sph.putInt(AGE, age);
        Toast.makeText(this, "Profile updated successfully",
                Toast.LENGTH_LONG).show();
    }
}

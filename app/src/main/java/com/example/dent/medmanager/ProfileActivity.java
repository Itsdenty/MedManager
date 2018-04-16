package com.example.dent.medmanager;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dent.medmanager.Utillities.DrawerUtil;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

public class ProfileActivity extends AppCompatActivity {
    TextView nameTextView;
    TextView emailTextView;
    TextView phoneTextView;
    TextView ageTextView;
    TextView locationTextView;
    ImageView avatarImageView;
    private final String PHONE = "person_phone";
    private final String LOCATION = "person_location";
    private final String AGE = "person_age";
    private final String NAME = "person_name";
    private final String EMAIL = "person_email";
    private final String PHOTO = "person_photo";
    SharedPreferencesHelper sph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar5);
        toolbar.setTitle(getResources().getString(R.string.med_manager_title));
        setSupportActionBar(toolbar);
        getSupportActionBar (). setDisplayHomeAsUpEnabled (true);
        nameTextView = (TextView) findViewById(R.id.tv_username);
        emailTextView = (TextView) findViewById(R.id.tv_email);
        ageTextView = (TextView) findViewById(R.id.tv_age);
        locationTextView = (TextView) findViewById(R.id.tv_location);
        phoneTextView = (TextView) findViewById(R.id.tv_phone);
        avatarImageView = (ImageView) findViewById(R.id.iv_avatar);

        sph = new SharedPreferencesHelper(this);
        if(sph.getString(PHONE) != ""){
            String phone = sph.getString(PHONE);
            phoneTextView.setText(phone);
        }
        if(sph.getString(LOCATION) != ""){
            String location = sph.getString(LOCATION);
            locationTextView.setText(location);
        }
        if(sph.getString(NAME) != ""){
            String name = sph.getString(NAME);
            nameTextView.setText(name);
        }
        if(sph.getString(EMAIL) != ""){
            String email = sph.getString(EMAIL);
            emailTextView.setText(email);
        }
        if(sph.getInt(AGE) != 0){
            int age = sph.getInt(AGE);
            ageTextView.setText(String.valueOf(age) + " years");
        }
        if(sph.getString(PHOTO) != ""){
            String photo = sph.getString(PHOTO);
            final Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLUE)
                    .borderWidthDp(1)
                    .cornerRadiusDp(80)
                    .oval(false)
                    .build();

            Picasso.with(avatarImageView.getContext()).load(Uri.parse(photo)).resize(150, 150)
                    .transform(transformation).into(avatarImageView);
        }
        DrawerUtil.getDrawer(this,toolbar, this);
    }
}

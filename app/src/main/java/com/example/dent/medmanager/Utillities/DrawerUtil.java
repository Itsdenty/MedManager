package com.example.dent.medmanager.Utillities;

/**
 * Created by dent4 on 4/3/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.dent.medmanager.AddMedicationActivity;
import com.example.dent.medmanager.MainActivity;
import com.example.dent.medmanager.MedicationGalleryActivity;
import com.example.dent.medmanager.ProfileActivity;
import com.example.dent.medmanager.R;
import com.example.dent.medmanager.UpdateProfileActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;


public class DrawerUtil {
    public static void getDrawer(final Activity activity, Toolbar toolbar, Context context) {
        final String PHOTO = "person_photo";
        String firstName ="med-manager guest";
        String myEmail ="guest@med-manager.com";
        Boolean isSet = false;
        SharedPreferencesHelper sph;
        ImageView imageView;
        //if you want to update the items at a later time it is recommended to keep it in a variable
        sph = new SharedPreferencesHelper(context);
            firstName = sph.getString("person_name", null);
            myEmail = sph.getString("person_email", null);
        final String  img = sph.getString(PHOTO, null);
//        Log.d("sign_Test", firstName);
        PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
        drawerEmptyItem.withEnabled(false);

        PrimaryDrawerItem drawerItemSearch = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.menu_search).withIcon(R.drawable.ic_menu_search);
        PrimaryDrawerItem drawerItemViewMedication = new PrimaryDrawerItem()
                .withIdentifier(2).withName(R.string.menu_view).withIcon(R.drawable.ic_menu_library);


        SecondaryDrawerItem drawerItemUpdateProfile = new SecondaryDrawerItem().withIdentifier(3)
                .withName(R.string.menu_update).withIcon(R.drawable.ic_menu_edit);
        SecondaryDrawerItem drawerItemAddMedication = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.menu_add).withIcon(R.drawable.ic_menu_add);

//        SecondaryDrawerItem drawerItemHelp = new SecondaryDrawerItem().withIdentifier(5)
//                .withName(R.string.help).withIcon(R.drawable.ic_help_black_24px);
//        SecondaryDrawerItem drawerItemDonate = new SecondaryDrawerItem().withIdentifier(6)
//                .withName(R.string.donate).withIcon(R.drawable.ic_payment_black_24px);

            final Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLUE)
                    .borderWidthDp(1)
                    .cornerRadiusDp(30)
                    .oval(false)
                    .build();
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).resize(120, 120)
                        .transform(transformation)
                        .placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

    /*
    @Override
    public Drawable placeholder(Context ctx) {
        return super.placeholder(ctx);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return super.placeholder(ctx, tag);
    }
    */
        });
        AccountHeader headerResult;
        if(firstName.length() > 1){
             headerResult = new AccountHeaderBuilder()
                    .withActivity(activity)
                    .withHeaderBackground(R.drawable.side_nav_bar)
                    .addProfiles(
                            new ProfileDrawerItem().withName(firstName).withEmail(myEmail).withIcon(Uri.parse(img))
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            if (profile instanceof  IProfile) {
                                // load tournament screen
                                Intent intent = new Intent(activity, ProfileActivity.class);
                                view.getContext().startActivity(intent);
                            }
                            return false;
                        }

                    })
                    .build();
        }
        else{
            headerResult = new AccountHeaderBuilder()
                    .withActivity(activity)
                    .withHeaderBackground(R.drawable.side_nav_bar)
                    .addProfiles(
                            new ProfileDrawerItem().withName("med-manager guest")
                                    .withEmail("guest@med-manager.com")
                                    .withIcon(R.drawable.ic_menu_slideshow)
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .build();
        }
        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar (true)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
                        drawerItemSearch,
                        drawerItemViewMedication,
                        new DividerDrawerItem(),
                        drawerItemUpdateProfile,
                        drawerItemAddMedication
//                        drawerItemHelp,
//                        drawerItemDonate
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 2 && !(activity instanceof MainActivity)) {
                            // load MedicationGallery screen
                            Intent intent = new Intent(activity, MedicationGalleryActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        else if (drawerItem.getIdentifier() == 4 && !(activity instanceof MainActivity)) {
                            // load add medication screen
                            Intent intent = new Intent(activity, AddMedicationActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        else if (drawerItem.getIdentifier() == 3 && !(activity instanceof MainActivity)) {
                            // load Update profile screen
                            Intent intent = new Intent(activity, UpdateProfileActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();
        result.getActionBarDrawerToggle (). setDrawerIndicatorEnabled (false);
    }
}

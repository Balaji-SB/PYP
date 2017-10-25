package com.android.pyp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.pyp.R;
import com.android.pyp.cms.SettingsFragment;
import com.android.pyp.property.ListingsFragment;
import com.android.pyp.property.MyFavoriteProperty;
import com.android.pyp.property.MyProperty;
import com.android.pyp.usermodule.LoginActivity;
import com.android.pyp.usermodule.MyProfileFragment;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private SessionManager manager;
    private FragmentActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        manager = Utils.getSessionManager(mContext);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.menuhome);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);


    }

    private void updateDisplay(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame, fragment).addToBackStack(null).commit();
    }

    public void updateNavItem(int itemId) {
        navigation.setSelectedItemId(itemId);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuhome:
                    Fragment fragment = new ListingsFragment();
                    Utils.updateHomeDisplay(fragment, mContext);
                    return true;

                case R.id.menumyProfile:
                    if (manager.isLoggedIn()) {
                        Fragment fragment1 = new MyProfileFragment();
                        updateDisplay(fragment1);
                        return true;
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }


                case R.id.addProperty:
                    if (manager.isLoggedIn()) {
                        Fragment fragment2 = new MyFavoriteProperty();
                        updateDisplay(fragment2);
                        return true;
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }
                case R.id.menumyProperty:
                    if (manager.isLoggedIn()) {
                        Fragment fragment3 = new MyProperty();
                        updateDisplay(fragment3);
                        return true;
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }

                case R.id.menusettings:
                    Fragment fragment4 = new SettingsFragment();
                    updateDisplay(fragment4);
                    return true;
            }
            return false;
        }
    };

}

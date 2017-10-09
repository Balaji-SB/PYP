package com.android.pyp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.pyp.R;
import com.android.pyp.addproperty.AddPropertyHome;
import com.android.pyp.cms.SettingsFragment;
import com.android.pyp.property.ListingsFragment;
import com.android.pyp.property.MyProperty;
import com.android.pyp.usermodule.MyProfileFragment;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.Utils;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        manager = Utils.getSessionManager(this);
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
                    updateDisplay(fragment);

                    return true;
                case R.id.menumyProfile:
                    if (manager.checkLogin()) {
                        Fragment fragment1 = new MyProfileFragment();
                        updateDisplay(fragment1);
                    }
                    return true;
               /* case R.id.menufavorite:
                    if (manager.checkLogin()) {
                        Fragment fragment2 = new MyFavoriteProperty();
                        updateDisplay(fragment2);
                    }
                    return true;*/

                case R.id.addProperty:
                    Intent intent=new Intent(HomeActivity.this, AddPropertyHome.class);
                    startActivity(intent);
                case R.id.menumyProperty:
                    if (manager.checkLogin()) {
                        Fragment fragment3 = new MyProperty();
                        updateDisplay(fragment3);
                    }
                    return true;
                case R.id.menusettings:
                    Fragment fragment4 = new SettingsFragment();
                    updateDisplay(fragment4);
                    return true;
            }
            return false;
        }
    };

}

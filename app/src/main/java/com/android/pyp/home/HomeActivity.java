package com.android.pyp.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.pyp.R;
import com.android.pyp.cms.SettingsFragment;
import com.android.pyp.property.ListingsFragment;
import com.android.pyp.usermodule.MyProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.menuhome);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


    }

    private void updateDisplay(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame,fragment).addToBackStack(null).commit();
    }

    public void updateNavItem(int itemId){
        navigation.setSelectedItemId(itemId);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuhome:
                    Fragment fragment=new ListingsFragment();
                    updateDisplay(fragment);

                    return true;
                case R.id.menumyProfile:
                    Fragment fragment1=new MyProfileFragment();
                    updateDisplay(fragment1);
                    return true;
                case R.id.menufavorite:
                    return true;
                case R.id.menumyProperty:
                    return true;
                case R.id.menusettings:
                    Fragment fragment4=new SettingsFragment();
                    updateDisplay(fragment4);
                    return true;
            }
            return false;
        }
    };

}

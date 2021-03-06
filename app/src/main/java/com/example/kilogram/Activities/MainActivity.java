package com.example.kilogram.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kilogram.Adapters.PostAdapter;
import com.example.kilogram.Fragments.ComposeFragment;
import com.example.kilogram.Fragments.PostsFragment;
import com.example.kilogram.Fragments.ProfileFragment;
import com.example.kilogram.Models.Post;
import com.example.kilogram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNavigation();

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Bottom navigation items selected
    private void setupBottomNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                PostsFragment.GoToProfileListener goToProfileListener = new PostsFragment.GoToProfileListener() {
                    @Override
                    public void onProfileClick(ParseUser user) {
//                        ProfileFragment profileFragment = ProfileFragment.newInstance(user);
//                        fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment).commit();

                        Intent intent = new Intent(MainActivity.this, PostProfileActivity.class);
                        intent.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(user));
                        startActivity(intent);
                    }
                };
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = PostsFragment.newInstance(goToProfileListener);
                        break;
                    case R.id.action_create:
                        ComposeFragment.OnComposeFragmentSubmitListener onComposeFragmentSubmitListener = new ComposeFragment.OnComposeFragmentSubmitListener() {
                            @Override
                            public void onButtonClick(Post post) {
                                bottomNavigationView.setSelectedItemId(R.id.action_home);
                                PostsFragment postsFragment = PostsFragment.newInstance(post, goToProfileListener);
                                fragmentManager.beginTransaction().replace(R.id.flContainer, postsFragment).commit();
                            }
                        };
                        fragment = new ComposeFragment(onComposeFragmentSubmitListener);
                        break;
                    case R.id.action_profile:
                        fragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
                        break;
                    default:
                        return true;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miLogout:
                ParseUser.logOut();
                goLoginActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
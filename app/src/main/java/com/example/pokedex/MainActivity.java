package com.example.pokedex;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterPokodex;
    private RecyclerView.LayoutManager layoutManager;
    private Button bt_gallery;
    private ImageView iv_image;
    private FloatingActionButton fab;
    Toolbar toolbar;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // app night/dark mode is disable throughout the app activities
        // mobile night/dark mode doesn't affect the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        bt_gallery = findViewById(R.id.bt_gallery);
        iv_image = findViewById(R.id.iv_image);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);





/*        adapterPokodex = new PokedexAdapter(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapterPokodex);
        recyclerView.setLayoutManager(layoutManager);                   */

//        or below two lines

        recyclerView.setAdapter(new PokedexAdapter(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(galleryIntent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab_popupMenu(fab);
            }
        });


    }

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            uri = result.getData().getData();
                            iv_image.setImageURI(uri);
                        }
                    }
                }
            }
    );


    public void fab_popupMenu(FloatingActionButton fab) {
//        Using PopupMenu of android.widget with inflating menu_popup.xml

        PopupMenu popupMenu = new PopupMenu(MainActivity.this, fab);
        popupMenu.inflate(R.menu.menu_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_1:
                        // perform action on clicking popup menu option_1
                        Toast.makeText(MainActivity.this, "Option 1 is clidked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_2:
                        // perform action on clicking popup menu option_2
                        Toast.makeText(MainActivity.this, "Option 2 is clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return true;
                }
            }
        });
        popupMenu.show();
    }

}
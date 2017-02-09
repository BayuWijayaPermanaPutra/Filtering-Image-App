package id.imageeffectsapp.views.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.imageeffectsapp.R;
import id.imageeffectsapp.adapter.AdapterMain;
import id.imageeffectsapp.model.ImageGallery;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private final int SELECT_PHOTO = 1;
    private android.net.Uri uriProfile;

    RecyclerView recyclerView;
    private List<ImageGallery> imageGalleryList;
    AdapterMain adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setupToolbar();

        imageGalleryList = new ArrayList<>();
        adapter = new AdapterMain(this,imageGalleryList);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Image"), SELECT_PHOTO);

            }
        });
        prepareData();
    }

    private void prepareData() {
        imageGalleryList.clear();

        File path = new File(Environment.getExternalStorageDirectory() + "/Ereening");
        String[] fileNames = new String[0];
        if(path.exists())
        {
            fileNames = path.list();
        }
        for(int i = 0; i < fileNames.length; i++)
        {
            Bitmap mBitmap = BitmapFactory.decodeFile(path.getPath()+"/"+ fileNames[i]);
            ///Now set this bitmap on imageview
            imageGalleryList.add(new ImageGallery(mBitmap,fileNames[i]));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Please wait...\nThis Image will be processing with Ereening Effects", Toast.LENGTH_LONG).show();
                    try {


                        uriProfile = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(uriProfile);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        //Write file
                        String filename = "bitmap.png";
                        FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 40, stream);

                        //Cleanup
                        stream.close();
                        selectedImage.recycle();
                        Intent intent = new Intent(MainActivity.this, ImageProcessingActivity.class);
                        intent.putExtra("inputImage",filename);
                        startActivity(intent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "File not found!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleviewgallery);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_image_main);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.title_toolbar_main_activity);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

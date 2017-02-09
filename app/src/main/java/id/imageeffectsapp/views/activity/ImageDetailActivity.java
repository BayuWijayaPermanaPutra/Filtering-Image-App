package id.imageeffectsapp.views.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import id.imageeffectsapp.R;
import id.imageeffectsapp.model.ImageGallery;

public class ImageDetailActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    String filename = null;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        initView();
        setupToolbar();
        filename = getIntent().getStringExtra("imageDetail");

        File path = new File(Environment.getExternalStorageDirectory() + "/Ereening");
        String[] fileNames = new String[0];
        if(path.exists())
        {
            fileNames = path.list();
        }
        for(int i = 0; i < fileNames.length; i++)
        {
            ///Now set this bitmap on imageview
            if(filename.equalsIgnoreCase(fileNames[i])){
                bitmap = BitmapFactory.decodeFile(path.getPath()+"/"+ fileNames[i]);
                break;
            }
        }
        if(bitmap!= null){
            imageView.setImageBitmap(bitmap);
        }
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.image_detail);
    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_toolbar_image_detail_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        if (id == R.id.main_action_delete) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.delete_content)
                    .setPositiveButton(R.string.dialog_action_yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteImage(filename);
                            Intent intent = new Intent(ImageDetailActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.dialog_action_no, null)
                    .show();
            return true;
        }
        if (id == R.id.main_action_share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 55, bytes);
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
            startActivity(Intent.createChooser(share, "Share Image"));
            return true;
        }

        if (id == R.id.main_action_edit) {
            try {
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 55, stream);

                //Cleanup
                stream.close();
                bitmap.recycle();

                Intent intent = new Intent(ImageDetailActivity.this, ImageProcessingActivity.class);
                intent.putExtra("inputImage",filename);
                startActivity(intent);
                finish();
            }  catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ImageDetailActivity.this, "File not found!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        if (id == R.id.main_action_setpictureas) {
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(getImageUri(ImageDetailActivity.this,bitmap), "image/jpeg");
            intent.putExtra("mimeType", "image/jpeg");
            this.startActivity(Intent.createChooser(intent, "Set as:"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void deleteImage(String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Ereening");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/Ereening/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Ereening/"), fileName);
        if (file.exists()) {
            file.delete();
        }
    }

}

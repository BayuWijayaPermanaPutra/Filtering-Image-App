package id.imageeffectsapp.views.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.imageeffectsapp.R;
import id.imageeffectsapp.utility.ImageEffect;

public class ImageProcessingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageUtama, imageNormal, imageEngrave, imageEmboss, imageGausianBlur, imageHighlight, imageGreyscale, imageInvert, imageReflection, imageSnow, imageMeanRemoval;
    LinearLayout linearNormal, linearEngrave, linearEmboss, linearGausianBlur, linearHighlight, linearGreyscale, linearInvert, linearReflection, linearSnow, linearMeanRemoval;
    Bitmap bmp = null;
    Bitmap bmpEffect = null;
    String filename;
    private int pilihanEffect=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);
        setupToolbar();
        initView();
        filename = getIntent().getStringExtra("inputImage");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            bmpEffect = bmp;
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(ImageProcessingActivity.this, "Image is success loaded with Ereening Effects", Toast.LENGTH_SHORT).show();
        Toast.makeText(ImageProcessingActivity.this, "Choice effect what you want for this Image", Toast.LENGTH_SHORT).show();
        imageUtama.setImageBitmap(bmp);

        imageNormal.setImageBitmap(bmp);

        imageEngrave.setImageBitmap(ImageEffect.engrave(bmp));

        imageEmboss.setImageBitmap(ImageEffect.emboss(bmp));

        imageGausianBlur.setImageBitmap(ImageEffect.applyGaussianBlur(bmp));

        imageHighlight.setImageBitmap(ImageEffect.doHighlightImage(bmp));

        imageGreyscale.setImageBitmap(ImageEffect.doGreyscale(bmp));

        imageInvert.setImageBitmap(ImageEffect.doInvert(bmp));

        imageReflection.setImageBitmap(ImageEffect.applyReflection(bmp));

        imageSnow.setImageBitmap(ImageEffect.applySnowEffect(bmp));

        imageMeanRemoval.setImageBitmap(ImageEffect.applyMeanRemoval(bmp));

        linearNormal.setOnClickListener(this);
        linearEngrave.setOnClickListener(this);
        linearEmboss.setOnClickListener(this);
        linearGausianBlur.setOnClickListener(this);
        linearHighlight.setOnClickListener(this);
        linearGreyscale.setOnClickListener(this);
        linearInvert.setOnClickListener(this);
        linearReflection.setOnClickListener(this);
        linearSnow.setOnClickListener(this);
        linearMeanRemoval.setOnClickListener(this);
    }

    private void initView() {
        imageUtama = (ImageView) findViewById(R.id.imageproc_imageview_image);
        imageNormal = (ImageView) findViewById(R.id.imageview_normal_effect);
        imageEngrave = (ImageView) findViewById(R.id.imageview_engrave_effect);
        imageEmboss = (ImageView) findViewById(R.id.imageview_emboss_effect);
        imageGausianBlur = (ImageView) findViewById(R.id.imageview_gausianblur_effect);
        imageHighlight = (ImageView) findViewById(R.id.imageview_highlight_effect);
        imageGreyscale = (ImageView) findViewById(R.id.imageview_greyscale_effect);
        imageInvert = (ImageView) findViewById(R.id.imageview_invert_effect);
        imageReflection = (ImageView) findViewById(R.id.imageview_reflection_effect);
        imageSnow = (ImageView) findViewById(R.id.imageview_snow_effect);
        imageMeanRemoval = (ImageView) findViewById(R.id.imageview_meanremoval_effect);

        linearNormal = (LinearLayout) findViewById(R.id.linear_normal_effect);
        linearEngrave = (LinearLayout) findViewById(R.id.linear_engrave_effect);
        linearEmboss = (LinearLayout) findViewById(R.id.linear_emboss_effect);
        linearGausianBlur = (LinearLayout) findViewById(R.id.linear_gausianblur_effect);
        linearHighlight = (LinearLayout) findViewById(R.id.linear_highlight_effect);
        linearGreyscale = (LinearLayout) findViewById(R.id.linear_greyscale_effect);
        linearInvert = (LinearLayout) findViewById(R.id.linear_invert_effect);
        linearReflection = (LinearLayout) findViewById(R.id.linear_reflection_effect);
        linearSnow = (LinearLayout) findViewById(R.id.linear_snow_effect);
        linearMeanRemoval = (LinearLayout) findViewById(R.id.linear_meanremoval_effect);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_toolbar_image_processing_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_process_image, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.button_save_or_share_image :
                CharSequence colors[] = new CharSequence[] {"Save", "Save and Share"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choice");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        DateFormat dateFormatter;
                        Date today;
                        String s;
                        switch (which){
                            case 0 :
                                dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                dateFormatter.setLenient(false);
                                today = new Date();
                                s = dateFormatter.format(today);
                                createDirectoryAndSaveFile(bmpEffect,"ereening"+s+"-"+filename);
                                Toast.makeText(ImageProcessingActivity.this, "Image has been saved to Internal Storage/Ereening", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ImageProcessingActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                                break;
                            case 1 :
                                dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                dateFormatter.setLenient(false);
                                today = new Date();
                                s = dateFormatter.format(today);
                                createDirectoryAndSaveFile(bmpEffect,"ereening"+s+"-"+filename);
                                Toast.makeText(ImageProcessingActivity.this, "Image has been saved to Internal Storage/Ereening", Toast.LENGTH_LONG).show();

                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bmpEffect.compress(Bitmap.CompressFormat.JPEG, 55, bytes);
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

                                break;
                            default: break;
                        }
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_engrave_effect :
                bmpEffect = ImageEffect.engrave(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 1;
                break;
            case R.id.linear_emboss_effect :
                bmpEffect = ImageEffect.emboss(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 2;
                break;
            case R.id.linear_gausianblur_effect :
                bmpEffect = ImageEffect.applyGaussianBlur(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 3;
                break;
            case R.id.linear_highlight_effect :
                bmpEffect = ImageEffect.doHighlightImage(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 4;
                break;
            case R.id.linear_greyscale_effect:
                bmpEffect = ImageEffect.doGreyscale(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 5;
                break;
            case R.id.linear_invert_effect:
                bmpEffect = ImageEffect.doInvert(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 6;
                break;
            case R.id.linear_reflection_effect :
                bmpEffect = ImageEffect.applyReflection(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 7;
                break;
            case R.id.linear_snow_effect:
                bmpEffect = ImageEffect.applySnowEffect(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 8;
                break;
            case R.id.linear_meanremoval_effect:
                bmpEffect = ImageEffect.applyMeanRemoval(bmp);
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 9;
                break;
            default:
                bmpEffect = bmp;
                imageUtama.setImageBitmap(bmpEffect);
                pilihanEffect = 0;
                break;
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Ereening");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/Ereening/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Ereening/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 45, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

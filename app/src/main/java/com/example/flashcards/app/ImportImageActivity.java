package com.example.flashcards.app;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportImageActivity extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 2;

    String mCurrentPhotoPath;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_image);

        Button camera = (Button) findViewById(R.id.btn_take_camera);
        Button gallery = (Button) findViewById(R.id.btn_select_gallery);

        mImageView = (ImageView) findViewById(R.id.imagePreview);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.setType("image/jpeg");
                // Ensure that the intent can be handled
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // get reference to file
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    }
                    catch (IOException ex) {
                        System.err.println(ex);
                    }

                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
/*
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                String title = getResources().getString(R.string.chooser_title);
                Intent chooser = Intent.createChooser(intent, title);

                if (chooser.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(chooser, PICK_IMAGE);
                }
            }
        });
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();

            Log.i(ImportImageActivity.class.toString(), "data.getData(): " + data.getData());

            Log.i(ImportImageActivity.class.toString(), "Photo path: " + mCurrentPhotoPath);
            // TODO: save mCurrentPhotoPath

            previewImage();
        }
        /*
        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            mCurrentPhotoPath = getPath(data.getData());

            data.getExtras().
            Log.i(ImportImageActivity.class.toString(), "Photo path: " + mCurrentPhotoPath);
            // TODO: save mCurrentPhotoPath

            previewImage();
        }
        */
    }
/*
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
*/
    private void previewImage() {
        Uri selectedImage = Uri.parse(mCurrentPhotoPath);
        getContentResolver().notifyChange(selectedImage, null);
        ContentResolver cr = getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media
                    .getBitmap(cr, selectedImage);

            mImageView.setImageBitmap(bitmap);
            Toast.makeText(this, selectedImage.toString(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                    .show();
            Log.e("Camera", e.toString());
        }

        /*
        Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath);
        // mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));


        System.out.println("hi");

        mImageView.setImageBitmap(b);
        mImageView.setVisibility(View.VISIBLE);
/*
        mImageView = new ImageView(this);
        mImageView.setImageBitmap(b);
        mImageView.setVisibility(View.VISIBLE);


        addView(mImageView);
*/

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.import_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}

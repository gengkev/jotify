package com.example.flashcards.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNotecardActivity extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 2;

    private MySQLiteHelper db;
    private Category cParent;
    private Notecard notecard;

    Uri mCurrentPhotoPath;

    EditText editText;
    ImageView image;
    ImageView image2;

    int current;
    boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notecard);

        editText = (EditText) findViewById(R.id.notecard_name);
        image = (ImageView) findViewById(R.id.imageView);
        image2 = (ImageView) findViewById(R.id.imageView2);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = 1;
                DialogFragment dialog = new ImportImageFragment();
                dialog.show(getFragmentManager(), AddNotecardActivity.class.toString());
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = 2;
                DialogFragment dialog = new ImportImageFragment();
                dialog.show(getFragmentManager(), AddNotecardActivity.class.toString());
            }
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra(GroupListActivity.EXTRA_GROUP_ID, -1);

        db = new MySQLiteHelper(this);
        cParent = db.getCategory(id);

        getActionBar().setTitle(cParent.title);

        // See if we were passed an existing notecard
        int data = intent.getIntExtra(GroupListActivity.EXTRA_NOTECARD_ID, -1);
        if (data == -1) {
            // new notecard
            notecard = new Notecard();
            notecard.category_id = cParent._id;
        }
        else {
            // existing notecard
            editing = true;
            notecard = db.getNotecard(data);

            current = 1;
            previewImage(Uri.parse(notecard.path1));
            current = 2;
            previewImage(Uri.parse(notecard.path2));

            editText.setText(notecard.caption);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            Log.i(AddNotecardActivity.class.getName(), "Photo path: " + mCurrentPhotoPath);
            // TODO: save mCurrentPhotoPath

            // previewImage();

            imageCallback(mCurrentPhotoPath);
        }

        else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            mCurrentPhotoPath = data.getData(); // a content:// thingy

            Log.i(AddNotecardActivity.class.toString(), "Photo path: " + mCurrentPhotoPath);
            // TODO: save mCurrentPhotoPath

            // previewImage();

            imageCallback(mCurrentPhotoPath);
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File("file:" + mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    // TODO: remove
    public void imageCallback(Uri path) {
        if (current == 1)
            notecard.path1 = path.toString();
        else
            notecard.path2 = path.toString();

        previewImage(path);
    }

    private void previewImage(Uri selectedImage) {
        ContentResolver cr = getContentResolver();
        cr.notifyChange(selectedImage, null);
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

            int nh = Math.round( bitmap.getHeight() * (1000F / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1000, nh, true);

            if (current == 1)
                image.setImageBitmap(scaled);
            else
                image2.setImageBitmap(scaled);

            Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.e("Camera", e.toString());
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = Uri.fromFile(image);
        return image;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_notecard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.add_notecard_done:
                // TODO save notecard

                notecard.caption = editText.getText().toString();
                if (editing)
                    db.editNotecard(notecard);
                else
                    db.addNotecard(notecard);

                Intent intent = new Intent(this, TestingActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_NOTECARD_ID, notecard._id);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

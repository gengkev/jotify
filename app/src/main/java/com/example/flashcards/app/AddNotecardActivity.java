package com.example.flashcards.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNotecardActivity extends ActionBarActivity implements ImportImageFragment.OnSelectListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 2;

    private MySQLiteHelper db;
    private Category category;
    private Notecard notecard;

    private Uri mCurrentPhotoPath;

    private EditText editText;
    private ImageView image;
    private ImageView image2;

    private boolean frontSide = true;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notecard);

        db = new MySQLiteHelper(this);
        editText = (EditText) findViewById(R.id.notecard_name);
        image = (ImageView) findViewById(R.id.imageView);
        image2 = (ImageView) findViewById(R.id.imageView2);

        Intent intent = getIntent();
        int id = intent.getIntExtra(GroupListActivity.EXTRA_GROUP_ID, -1);

        category = db.getCategory(id);

        getActionBar().setTitle(category.title);

        // See if we were passed an existing notecard
        int data = intent.getIntExtra(GroupListActivity.EXTRA_NOTECARD_ID, -1);
        if (data == -1) {
            // new notecard
            editing = false;
            notecard = new Notecard();
            notecard.category_id = category._id;
        } else {
            // existing notecard
            editing = true;
            notecard = db.getNotecard(data);

            frontSide = true;
            previewImage(Uri.parse(notecard.path1));
            frontSide = false;
            previewImage(Uri.parse(notecard.path2));

            editText.setText(notecard.caption);
        }

        // Click listeners for images
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frontSide = true;
                DialogFragment dialog = new ImportImageFragment();
                dialog.show(getFragmentManager(), AddNotecardActivity.class.toString());
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frontSide = false;
                DialogFragment dialog = new ImportImageFragment();
                dialog.show(getFragmentManager(), AddNotecardActivity.class.toString());
            }
        });

    }

    // Called by ImportImageFragment
    @Override
    public void onSelect(int which) {
        if (which == 0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(getPackageManager()) != null) {
                // get reference to file
                // TODO: remove
                File photoFile = createImageFile();

                if (photoFile != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else if (which == 1) {

            /*
             This doesn't work on KitKat: using this intent only allows us to open the
                image right after we select it. Otherwise it SecurityExceptions, saying
                the android.permission.MANAGE_DOCUMENTS permission is needed.
                So here is a pretty lazy workaround.
             Ref:
                http://stackoverflow.com/q/19837358
                http://stackoverflow.com/q/22178041
                http://stackoverflow.com/q/19834842
            */

            Intent intent = new Intent(Intent.ACTION_PICK, /* Intent.ACTION_GET_CONTENT, */
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/jpeg");

            String title = getString(R.string.chooser_title);
            Intent chooser = Intent.createChooser(intent, title);

            if (chooser.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(chooser, PICK_IMAGE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();

            Log.i(AddNotecardActivity.class.getName(), "Photo path (camera): " + mCurrentPhotoPath);
            imageCallback(mCurrentPhotoPath);
        } else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            mCurrentPhotoPath = data.getData(); // a content:// thingy

            Log.i(AddNotecardActivity.class.getName(), "Photo path (gallery): " + mCurrentPhotoPath);
            imageCallback(mCurrentPhotoPath);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // TODO: what does this do?
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File("file:" + mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    // TODO: remove
    public void imageCallback(Uri path) {
        if (frontSide)
            notecard.path1 = path.toString();
        else
            notecard.path2 = path.toString();

        previewImage(path);
    }

    private void previewImage(Uri selectedImage) {
        // TODO: what does this do?
        ContentResolver cr = getContentResolver();
        cr.notifyChange(selectedImage, null);

        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

            int nh = Math.round(bitmap.getHeight() * (1000F / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1000, nh, true);

            if (frontSide)
                image.setImageBitmap(scaled);
            else
                image2.setImageBitmap(scaled);

            // Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.e(AddNotecardActivity.class.getName(), "Failed to load: " + e.toString());
        }
    }

    public File createImageFile() {
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.add_notecard_done:
                // Save the notecard
                notecard.caption = editText.getText().toString();
                if (editing)
                    db.editNotecard(notecard);
                else
                    db.addNotecard(notecard);

                // Create a toast
                Toast.makeText(this, R.string.notecard_saved, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, TestingActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_NOTECARD_ID, notecard._id);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

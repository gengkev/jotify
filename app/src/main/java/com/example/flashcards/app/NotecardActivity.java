package com.example.flashcards.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

public class NotecardActivity extends Activity {
    private LinearLayout mainLayout;

    private Notecard notecard;

    private Bitmap side1;
    private Bitmap side2;
    private boolean frontSide = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MySQLiteHelper db = new MySQLiteHelper(this);
        mainLayout = (LinearLayout) findViewById(R.id._linearLayout);

        // Get intent data
        Intent intent = getIntent();
        int data = intent.getIntExtra(GroupListActivity.EXTRA_NOTECARD_ID, -1);

        // Try the Bundle
        if (data == -1 && savedInstanceState != null) {
            data = savedInstanceState.getInt("notecard-id", -1);
        }

        if (data == -1) {
            // Go back up to GroupListActivity
            // http://stackoverflow.com/a/20306670/1435804
            Intent up = new Intent(this, GroupListActivity.class);
            up.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, up);
            return;
        }

        // Load the notecard from the database
        notecard = db.getNotecard(data);

        // Set title
        getActionBar().setTitle(notecard.caption);


        // See if current side is stored in Bundle
        if (savedInstanceState != null) {
            frontSide = savedInstanceState.getBoolean("frontSide", true);
        }

        // Load bitmaps
        try {
            if (notecard.path1 != null) {
                side1 = decodeSampledBitmapFromUri(Uri.parse(notecard.path1), 1000);
            }
            if (notecard.path2 != null) {
                side2 = decodeSampledBitmapFromUri(Uri.parse(notecard.path2), 1000);
            }
        } catch (Exception e) {
            Log.e(NotecardActivity.class.getName(), e.toString());
        }

        // Create ImageView
        final ImageView imageView = new ImageView(this);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                frontSide = !frontSide;

                if (frontSide)
                    imageView.setImageBitmap(side1);
                else
                    imageView.setImageBitmap(side2);
            }
        });

        // Display current side
        if (frontSide)
            imageView.setImageBitmap(side1);
        else
            imageView.setImageBitmap(side2);

        // Add imageView to layout
        mainLayout.addView(imageView);
    }

    // From: http://stackoverflow.com/q/13353839
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        // Raw height and width of image
        final int width = options.outWidth;
        final int height = options.outHeight;

        int reqHeight = Math.round(options.outHeight * (1000F / width));
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth) {
        ContentResolver cr = getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
        } catch (IOException e) {
            return null;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqWidth);

        Bitmap out;
        try {
            out = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
        } catch (IOException e) {
            out = null;
        }
        return out;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.testing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.edit_notecard:
                // Create an AddNotecardActivity to edit the notecard
                Intent intent = new Intent(this, AddNotecardActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_GROUP_ID, notecard.category_id);
                intent.putExtra(GroupListActivity.EXTRA_NOTECARD_ID, notecard._id);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("notecard-id", notecard._id);
        savedInstanceState.putBoolean("frontSide", frontSide);
    }
}
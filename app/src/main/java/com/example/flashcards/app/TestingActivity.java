package com.example.flashcards.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class TestingActivity extends Activity {

    // mainLayout is the child of the HorizontalScrollView ...
    private LinearLayout mainLayout;

    // this is an array that holds the IDs of the drawables ...
    /*
    private int[] images = {R.drawable.a, R.drawable.b};
    private int[] imagesB = {R.drawable.b, R.drawable.a};
    private int[] currentSide = {0,0};
    */

    private Bitmap side1;
    private Bitmap side2;

    private boolean frontSide = true;

    private View cell;
    private TextView text;

    private Notecard notecard;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.main);

        MySQLiteHelper db = new MySQLiteHelper(this);

        Intent intent = getIntent();

        int data = intent.getIntExtra(GroupListActivity.EXTRA_NOTECARD_ID, -1);
        notecard = db.getNotecard(data);

        getActionBar().setTitle(notecard.caption);

        mainLayout = (LinearLayout) findViewById(R.id._linearLayout);

        // load bitmaps
        ContentResolver cr = getContentResolver();
        try {
            if (notecard.path1 != null) {
                side1 = decodeSampledBitmapFromUri(Uri.parse(notecard.path1), 1000);
            }
            if (notecard.path2 != null) {
                side2 = decodeSampledBitmapFromUri(Uri.parse(notecard.path2), 1000);
            }
        } catch (Exception e) {
            Log.e(TestingActivity.class.getName(), e.toString());
        }


        final ImageView imageView = new ImageView(this);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frontSide)
                    imageView.setImageBitmap(side2);
                else
                    imageView.setImageBitmap(side1);

                frontSide = !frontSide;
            }
        });

        imageView.setImageBitmap(side1);


        mainLayout.addView(imageView);
    }

    // From: http://stackoverflow.com/q/13353839

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        // Raw height and width of image
        final int width = options.outWidth;
        final int height = options.outHeight;

        int reqHeight = Math.round(options.outHeight * (1000F / width) );
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (IOException e) {
            return null;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqWidth);

        Bitmap out;
        try {
            out = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
        case android.R.id.home:
            // hacks: back action preserves the previous activity
            super.onBackPressed();
            return true;
        case R.id.edit_notecard:

            Intent intent = new Intent(this, AddNotecardActivity.class);
            intent.putExtra(GroupListActivity.EXTRA_GROUP_ID, notecard.category_id);
            intent.putExtra(GroupListActivity.EXTRA_NOTECARD_ID, notecard._id);
            startActivity(intent);

        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
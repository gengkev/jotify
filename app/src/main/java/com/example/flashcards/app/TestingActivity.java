package com.example.flashcards.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestingActivity extends Activity {

    // mainLayout is the child of the HorizontalScrollView ...
    private LinearLayout mainLayout;

    // this is an array that holds the IDs of the drawables ...
    private int[] images = {R.drawable.a, R.drawable.b};
    private int[] imagesB = {R.drawable.b, R.drawable.a};
    private int[] currentSide = {0,0};

    private View cell;
    private TextView text;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.main);

        mainLayout = (LinearLayout) findViewById(R.id._linearLayout);

        for (int i = 0; i < images.length; i++) {
            final int CurrentSide = i;
            cell = getLayoutInflater().inflate(R.layout.cell, null);


            final ImageView imageView = (ImageView) cell.findViewById(R.id._image);
            imageView.setOnClickListener(new OnClickListener() {


            //private int Side = 0;
                @Override
                public void onClick(View v) {
                    // do whatever you want ...
                  //  Toast.makeText(TestingActivity.this,
                    //        (CharSequence) imageView.getTag(), Toast.LENGTH_SHORT).show();
                       // if(Side == 0){
                    if(currentSide[CurrentSide] == 0){
                    imageView.setImageResource(imagesB[CurrentSide]);
                        currentSide[CurrentSide] = 1;
                    }
                    else{
                        imageView.setImageResource(images[CurrentSide]);
                        currentSide[CurrentSide] = 0;
                    }

                    //Side = 1;
                       // }
                    //else {
                        //    imageView.setImageResource(images[i]);
                          //  Side = 0;

                      //  }
                }
            });

            imageView.setTag("Image#"+(i+1));

            text = (TextView) cell.findViewById(R.id._imageName);

            imageView.setImageResource(images[i]);
            text.setText("Image#"+(i+1));

            mainLayout.addView(cell);
        }
    }
}
package com.example.flashcards.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportImageFragment extends DialogFragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 2;

    ImageView mImageView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.title_activity_import_image);

        builder.setItems(new String[] { "Take from Camera", "Pick from Gallery" }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();

                if (which == 0) {
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // get reference to file
                        File photoFile = null;
                        try {
                            photoFile = ((AddNotecardActivity) getActivity()).createImageFile();
                        }
                        catch (IOException ex) {
                            System.err.println(ex);
                        }

                        if (photoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            getActivity().startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                }
                else { // which == 1

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

                    // intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image/jpeg");

                    String title = getResources().getString(R.string.chooser_title);
                    Intent chooser = Intent.createChooser(intent, title);

                    if (chooser.resolveActivity(getActivity().getPackageManager()) != null) {
                        getActivity().startActivityForResult(chooser, PICK_IMAGE);
                    }
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((AddNotecardActivity) getActivity()).onActivityResult(requestCode, resultCode, data);
    }
/*
    private void previewImage() {
        Uri selectedImage = mCurrentPhotoPath;
        ContentResolver cr = getActivity().getContentResolver();
        cr.notifyChange(selectedImage, null);
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            mImageView.setImageBitmap(scaled);

            Toast.makeText(getActivity(), selectedImage.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
            Log.e("Camera", e.toString());
        }
    }
*/

/*
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File("file:" + mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
    */
}

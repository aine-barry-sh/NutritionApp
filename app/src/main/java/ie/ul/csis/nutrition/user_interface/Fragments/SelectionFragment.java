package ie.ul.csis.nutrition.user_interface.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.user_interface.MainActivity;
import ie.ul.csis.nutrition.utilities.GPSTracker;
import ie.ul.csis.nutrition.utilities.Tools;



public class SelectionFragment extends Fragment {

    private Button btnCameraLaunch;
    private Button btnGalleryLaunch;
    private View view;
    private GPSTracker gpsTracker;
    private String imageFileName;
    private File imageFile;

    public SelectionFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(getContext());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_selection, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        btnCameraLaunch = (Button) view.findViewById(R.id.btn_takenPic);
        btnGalleryLaunch = (Button) view.findViewById(R.id.btn_test);
        ((MainActivity) getActivity()).setImageFile(null);

        configureButtons();
    }

    private void configureButtons() {
        btnCameraLaunch.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnGalleryLaunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchPickImageFromGalleryIntent();
            }
        });
    }



    private void dispatchPickImageFromGalleryIntent() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, view.getResources().getInteger(R.integer.PICK_IMAGE));

    }



    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(
                        takePictureIntent,
                        getContext().getResources().getInteger((R.integer.REQUEST_TAKE_PHOTO))
                );
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String location = "null_null";

            gpsTracker.getLocation();
        //Tools.toast(getContext(), gpsTracker.getLatitude() + ", " + gpsTracker.getLongitude());
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.toString();
        }
        imageFileName = "JPEG_" + timeStamp + "_" + location + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //ERRORTHERE      mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        ((MainActivity) getActivity()).setImageFile(imageFile);
        return imageFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == view.getResources().getInteger(R.integer.PICK_IMAGE)) {
            //getExtraInfo();
            Uri selectedImageUri = data.getData();
            try {

                //load image from gallery and load into imageview
                InputStream image_stream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
                ((MainActivity) getActivity()).getImageView().setImageBitmap(bitmap);

                //copy bitmap to caching folder so that it will be uploaded

                saveBitmapToFile(createImageFile(),bitmap,Bitmap.CompressFormat.PNG,100);

            } catch (Exception E) {
                Tools.toast(getContext(), "error, file was not found");
                Tools.log("MainActivity", "OnActivityResult", "file not found after returning from gallery");
            }
        }

        if (requestCode == view.getResources().getInteger(R.integer.REQUEST_TAKE_PHOTO)) {


            if (((MainActivity)getActivity()).getImageView() != null) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                ((MainActivity)getActivity()).getImageView().setImageBitmap(myBitmap);

            } else {
                Tools.toast(getContext(), "Could not find photo. Try again.");
            }
        }
        ((MainActivity) getActivity()).updateFragment();
    }


    private boolean saveBitmapToFile(File imageFile, Bitmap bm,
                                    Bitmap.CompressFormat format, int quality) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bm.compress(format,quality,fos);
            fos.close();
            return true;
        }
        catch (IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }
}

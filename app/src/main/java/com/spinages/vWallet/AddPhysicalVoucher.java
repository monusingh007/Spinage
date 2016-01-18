package com.spinages.vWallet;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spinages.app.AppConstants;
import com.spinages.app.R;
import com.spinages.utilities.CustomDialogOk;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPhysicalVoucher extends Activity implements View.OnClickListener {
    Button upload;
    ImageView front_image, back_image;

    final int REQUEST_CAMERA = 1;
    final int SELECT_FILE = 2;
    int IMAGE_SELECTOR = 0;
    int IMAGE_WIDTH = 600;
    int IMAGE_HEIGHT = 200;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    Bitmap frontImage;
    Bitmap backImage;
    Uri imageUri = null;
    static TextView imageDetails = null;
    public static ImageView showImg = null;
    AddPhysicalVoucher CameraActivity = null;
    EditText description;
    EditText expiryDate;
    String descrip;
    String expdate;
    PrefManager prefManager;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TextView title;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vwallet);
        prefManager = new PrefManager(this);
        title = (TextView)findViewById(R.id.title);
        title.setTypeface(Util.setTextFontOpenSans(this,"OpenSans-Light.ttf"));
        front_image = (ImageView) findViewById(R.id.front);
        back_image = (ImageView) findViewById(R.id.back);
        front_image.setOnClickListener(this);
        back_image.setOnClickListener(this);
        upload = (Button) findViewById(R.id.upload_voucher);
        upload.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Light.ttf"));
        upload.setOnClickListener(this);
        mainLayout = (RelativeLayout)findViewById(R.id.mainlayout);
        description = (EditText) findViewById(R.id.descriptionData);
        expiryDate = (EditText) findViewById(R.id.date_Data);
        description.setOnClickListener(this);
        expiryDate.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        openAlertDialog();
    }
    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        expiryDate.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload_voucher:
                descrip = description.getText().toString().trim().toString();
                expdate = expiryDate.getText().toString().trim().toString();
                if(descrip.equals("")||expdate.equals("")||null==backImage||null==frontImage)
                {
                    Toast.makeText(getBaseContext(),"please Enter all the fields",Toast.LENGTH_LONG).show();
                }else
                {
                    new UploadImage().execute();
                }
                break;
            case R.id.front:
                IMAGE_SELECTOR = 0;
                selectImage();
                break;
            case  R.id.back:
                IMAGE_SELECTOR = 1;
                selectImage();
                break;
            case  R.id.date_Data:
                new DatePickerDialog(AddPhysicalVoucher.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

        }



    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddPhysicalVoucher.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // thumbnail = getResizedBitmap(thumbnail, IMAGE_WIDTH, IMAGE_HEIGHT);
        if (IMAGE_SELECTOR == 0) {
            front_image.setImageBitmap(thumbnail);
            frontImage = thumbnail;
        }
        if (IMAGE_SELECTOR == 1) {

            back_image.setImageBitmap(thumbnail);
            backImage = thumbnail;
        }
    }
    public Bitmap rotateBitmapOrientation(String file) {

        // Create and configure BitmapFactory

        // Read EXIF Data
        try {
            ExifInterface exif = new ExifInterface(file);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file, bounds);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(file, opts);

            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
            // Rotate Bitmap
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
            // Return result
            return rotatedBitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);


       // bm = getResizedBitmap(bm, IMAGE_WIDTH, IMAGE_HEIGHT);
        if (IMAGE_SELECTOR == 0) {
            frontImage = bm;
            front_image.setImageBitmap(bm);
        }
        if (IMAGE_SELECTOR == 1) {
            backImage = bm;
            back_image.setImageBitmap(bm);
        }
    }




      class UploadImage extends AsyncTask<String, String, String> {
        byte[] frontImageEncoded,backImageEncoded;

        String url=AppConstants.INSOMNIX_WEBSRVICES_URL+"app/upload/jpg";
        String uploadUrl=AppConstants.INSOMNIX_WEBSRVICES_URL+"user/uploadPV";
        ProgressDialog progressDialog;

        String  gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddPhysicalVoucher.this,R.style.ProgerssDialogTheme);
            progressDialog.setMessage("uploading voucher..");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
            frontImageEncoded = imageToBytesCoverter(frontImage);
            backImageEncoded = imageToBytesCoverter(backImage);
           //  byte[] b = imageToBytesCoverter(frontImage);





        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject SendJson=new JSONObject();
            try {
                Log.e("imagae data",frontImageEncoded.toString());
               // JSONObject fronImeResponse= URLconnectionService.uploadData(url, frontImageEncoded,
                     //   AppConstants.POST_TEXT_REQUEST, AppConstants.APP_GMAIL, AppConstants.APP_PASSWORD);
               JSONObject fronImageResponse= URLconnectionService.getWebServiceData(url,byteArrayToHex(frontImageEncoded) ,
                        AppConstants.POST_TEXT_REQUEST, AppConstants.APP_GMAIL, AppConstants.APP_PASSWORD);
                JSONObject backImageResponse= URLconnectionService.getWebServiceData(url, byteArrayToHex(backImageEncoded),
                        AppConstants.POST_TEXT_REQUEST, AppConstants.APP_GMAIL, AppConstants.APP_PASSWORD);
                String frontImagePath=fronImageResponse.opt("imgPath").toString();
               // frontImagePath.replaceAll("/","\\/");
                String backImagePath =backImageResponse.opt("imgPath").toString();
               // backImagePath.replaceAll("/","\\/");

                /*{"backImage":"/spinages/images/iYHfUoC20x9TVin8_15Oct2015042651GMT_1444883211304.jpg","description":"Everyday
                Voucher","expiryDate":"22-12-2015","frontImage":"/spinages/images/ZcQErK6bTMGe8vcB_15Oct2015041459GMT_1444882499648.jpg"}*/
               SendJson.accumulate("backImage",backImagePath);
                SendJson.accumulate("description",descrip);
                SendJson.accumulate("expiryDate",expdate);
                SendJson.accumulate("frontImage", frontImagePath);
                String payLoad = SendJson.toString().replaceAll("\\/", "/");

                URLconnectionService.getWebServiceData(uploadUrl, payLoad, AppConstants.POST_JSON_REQUEST,gmail,password);

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddPhysicalVoucher.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

                e.printStackTrace();
            }


            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(getBaseContext(),VwalletMainActivity.class);
            startActivity(intent);
            finish();

        }

        String imageToStringCoverter(Bitmap bmp) {
            Bitmap bm = bmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
        byte[] imageToBytesCoverter(Bitmap bmp) {
            Bitmap bm = bmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return b;
        }
    }
    public void openAlertDialog() {
       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This function is for storing personal physical voucher. " +
                "It acts only as a reminder for you to use them before they expire again.");

        alertDialogBuilder.setNeutralButton("!Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alertDialogBuilder.show();*/


        CustomDialogOk customDialogOk =  new CustomDialogOk(this,"This function is for storing personal physical voucher. " +
        "It acts only as a reminder for you to use them before they expire again.","black");
        customDialogOk.show();
    }
    public class CustomDialogOk extends Dialog implements
            View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        String messsage;
        TextView textView;
        RelativeLayout relativeLayout;
        String background;

        public CustomDialogOk(Activity a, String message, String background) {
            super(a, R.style.AlertDialogTheme);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.messsage =message;
            this.background = background;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);
            yes = (Button) findViewById(R.id.okay_but);
            yes.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            relativeLayout = (RelativeLayout) findViewById(R.id.layout);
            textView = (TextView)findViewById(R.id.dialog_text);
            textView.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            textView.setText(messsage);

            // no = (Button) findViewById(R.id.btn_no);
            yes.setOnClickListener(this);

            //no.setOnClickListener(this);

                relativeLayout.setBackgroundResource(R.drawable.transparent_box);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.okay_but:
                    //c.finish();
                    mainLayout.setVisibility(View.VISIBLE);
                    break;
                //case R.id.btn_no:
                //  dismiss();
                // break;
                default:
                    break;
            }
            dismiss();
        }

        @Override
        public void onBackPressed() {
           // super.onBackPressed();
        }
    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }
}


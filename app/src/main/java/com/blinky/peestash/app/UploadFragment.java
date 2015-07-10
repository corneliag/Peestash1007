package com.blinky.peestash.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;


public class UploadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String id_user = "",type="", tag="upload_img";
    private OnFragmentInteractionListener mListener;
    View rootView;
    String msg;
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    ProgressDialog progress;
    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();

    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_user = getArguments().getString("id_user");
            type = getArguments().getString("type");
            /*Toast.makeText(getActivity(), "id user: " +id_user +" type: "  + type,
                    Toast.LENGTH_LONG).show();*/
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type=="artiste"){
                    int position = 1;
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("id_user", id_user);
                    i.putExtra("position", position);
                    startActivity(i);
                    getActivity().finish();
                }else {
                    int position = 1;
                    Intent i = new Intent(getActivity(), MainEtbActivity.class);
                    i.putExtra("id_user", id_user);
                    i.putExtra("position", position);
                    startActivity(i);
                    getActivity().finish();
                }

            }
        });



        Button btnLoad = (Button) rootView.findViewById(R.id.buttonLoadPicture);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        Button btnUpload = (Button) rootView.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When Image is selected from Gallery
                if (imgPath != null && !imgPath.isEmpty()) {
                   // prgDialog.setMessage("Conversion de l'image...");
                    Toast.makeText(getActivity(), "Conversion de l'image...", Toast.LENGTH_LONG).show();
                    //prgDialog.show();
                    // Convert image to String using Base64
                    //encodeImagetoString();
                    new AsyncTask<Void, Void, String>() {

                        protected void onPreExecute() {

                        };

                        @Override
                        protected String doInBackground(Void... params) {
                            BitmapFactory.Options options = null;
                            options = new BitmapFactory.Options();
                            options.inSampleSize = 3;
                            bitmap = BitmapFactory.decodeFile(imgPath,
                                    options);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            // Must compress the Image to reduce image size to make upload easy
                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                            byte[] byte_arr = stream.toByteArray();
                            // Encode Image to String
                            encodedString = Base64.encodeToString(byte_arr, 0);
                            return "";
                        }

                        @Override
                        protected void onPostExecute(String msg) {
//                prgDialog.setMessage("Enregistrement de l'image...");
                            Toast.makeText(getActivity(), "Enregistrement de l'image...", Toast.LENGTH_LONG).show();
                            // Put converted Image string into Async Http Post param
                            params.put("image", encodedString);
                            params.put("id", id_user);
                            params.put("type", type);
                            params.put("tag", tag);

                            // Trigger Image upload
                            AsyncHttpClient client = new AsyncHttpClient();
                            // Don't forget to change the IP address to your LAN address. Port no as well.
                            client.post("http://peestash.peestash.fr/index.php", params, new AsyncHttpResponseHandler() {
                                // When the response returned by REST has Http
                                // response code '200'
                                @Override
                                public void onSuccess(String response) {
                                    if (type.equals("artiste")) {

                                        int position = 1;
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        i.putExtra("id_user", id_user);
                                        i.putExtra("position", position);
                                        startActivity(i);
                                        getActivity().finish();

                                    } else {

                                        int position = 1;
                                        Intent i = new Intent(getActivity(), MainEtbActivity.class);
                                        i.putExtra("id_user", id_user);
                                        i.putExtra("position", position);
                                        startActivity(i);
                                        getActivity().finish();
                                    }
                                }

                                // When the response returned by REST has Http
                                // response code other than '200' such as '404',
                                // '500' or '403' etc

                                public void onFailure(int statusCode, Throwable error,
                                                      String content) {
                                    // Hide Progress Dialog
                                    prgDialog.hide();
                                    // When Http response code is '404'
                                    if (statusCode == 404) {
                                        Toast.makeText(getActivity(),
                                                "Requested resource not found",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    // When Http response code is '500'
                                    else if (statusCode == 500) {
                                        Toast.makeText(getActivity(),
                                                "Something went wrong at server end",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    // When Http response code other than 404, 500
                                    else {
                                        Toast.makeText(
                                                getActivity(),
                                                "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                                        + statusCode, Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                        }
                    }.execute(null, null, null);
                    
                    // When Image is not selected from Gallery
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Vous devez choisir une image pour lancer l'enregistrement.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    // When Image is selected from Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                params.put("imgPath", imgPath);

            } else {
                Toast.makeText(getActivity(), "Vous n'avez selectionne aucune image.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Une erreur est survenue", Toast.LENGTH_LONG)
                    .show();
        }

    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }
}

/**
 * Copyright 2017 by Intuition Systems.
 
package com.example.yourapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import static com.example.intuition.humanity.MainActivity.DATABASE_NAME;
import static com.example.intuition.humanity.MainActivity.PACKAGE_NAME;

/**
 * Android Drive Quickstart activity. This activity takes a photo and saves it
 * in Google Drive. The user is prompted with a pre-made dialog which allows
 * them to choose the file location.
 */
public class CloudBackup extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "drive_Humanity";
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    public static DriveFile mfile;

    private static GoogleApiClient mGoogleApiClient;
    private static final String DATABASE_PATH =
            "/data/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME;
    private static final String MIME_TYPE = "application/x-sqlite-3";
    private Bitmap mBitmapToSave;

    /**
     * Create a new file and save it to Drive.
     */
    public void saveFileToDrive() {
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");
            Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toast.makeText(CloudBackup.this, "Error while trying to create new file contents", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(MIME_TYPE);
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(DATABASE_NAME) // Google Drive File name
                            .setMimeType(mimeType)
                            .setStarred(true).build();
                    // create a file on root folder
                    Drive.DriveApi.getRootFolder(mGoogleApiClient)
                            .createFile(mGoogleApiClient, changeSet, result.getDriveContents());
//                        .setResultCallback(backupFileCallback);
                }
               });




    }
    public static void doDriveBackup () {
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(backupContentsCallback);
    }


    static final private ResultCallback<DriveApi.DriveContentsResult> backupContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(MIME_TYPE);
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(DATABASE_NAME) // Google Drive File name
                    .setMimeType(mimeType)
                    .setStarred(true).build();
            // create a file on root folder
            Drive.DriveApi.getRootFolder(mGoogleApiClient)
                    .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
                    .setResultCallback(backupFileCallback);
        }
    };

    static final private ResultCallback<DriveFolder.DriveFileResult> backupFileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            mfile = result.getDriveFile();
            mfile.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, new DriveFile.DownloadProgressListener() {
                @Override
                public void onProgress(long bytesDownloaded, long bytesExpected) {
                }
            }).setResultCallback(backupContentsOpenedCallback);
        }
    };

    static final private ResultCallback<DriveApi.DriveContentsResult> backupContentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }

//            DialogFragment_Sync.setProgressText("Backing up..");

            DriveContents contents = result.getDriveContents();
            BufferedOutputStream bos = new BufferedOutputStream(contents.getOutputStream());
            byte[] buffer = new byte[1024];
            int n;

            try {
                FileInputStream is = new FileInputStream(DATABASE_PATH);
                BufferedInputStream bis = new BufferedInputStream(is);

                while( ( n = bis.read(buffer) ) > 0 ) {
                    bos.write(buffer, 0, n);
//                    DialogFragment_Sync.setProgressText("Backing up...");
                }
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            contents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
//                    DialogFragment_Sync.setProgressText("Backup completed!");
//                    mToast(act.getResources().getString(R.string.backupComplete));
//                    DialogFragment_Sync.dismissDialog();
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CloudBackup.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK){

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");

        saveFileToDrive();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }
}

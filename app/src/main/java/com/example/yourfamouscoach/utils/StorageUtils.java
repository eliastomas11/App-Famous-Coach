package com.example.yourfamouscoach.utils;

import static java.security.AccessController.getContext;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessControlContext;
import java.util.Objects;

public class StorageUtils {

    public final static String GREATER = "GREATER";
    public final static String LOWER = "LOWER";

    public final static int SYSTEM_VERSION = Build.VERSION.SDK_INT;
    public static boolean checkBuildPermissions(int versionToCheck, String limitToCheck) {
        switch (limitToCheck) {
            case GREATER:
                return SYSTEM_VERSION >= versionToCheck;
            case LOWER:
                return SYSTEM_VERSION <= versionToCheck;
            default:
                return SYSTEM_VERSION == versionToCheck;
        }

    }

    public static boolean checkPermission(String permission,Context context) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean writeToFile(ContentResolver contentResolver, Uri fileDestiny,Bitmap fileSource){
        OutputStream outs = null;
        try {
            outs = contentResolver.openOutputStream(fileDestiny);
            fileSource.compress(Bitmap.CompressFormat.JPEG, 100, outs);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            outs.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}

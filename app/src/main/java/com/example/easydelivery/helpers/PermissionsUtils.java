package com.example.easydelivery.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {
    public static final int REQUEST_PERMISSION_MULTIPLE = 0;
    public static final int REQUEST_PERMISSION_CAMERA = 1;
    public static final int REQUEST_PERMISSION_LOCATION = 2;
    public static final int REQUEST_WRITE_EXTERNAL = 3;

    public static boolean checkAndRequestPermissions(Activity activity) {
        System.out.println("Comienza el chequeo de permisos");
        int permissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int permissionLocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionWriteExternal = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Permission List
        List<String> listPermissionsNeeded = new ArrayList<>();

        // Camera Permission
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(activity, "El acceso a la cámara es necesario", Toast.LENGTH_SHORT).show();
            }*/
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        // Read/Write Permission
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // Location Permission
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION_MULTIPLE);
            return false;
        }

        return true;
    }

    /**
     * Requests the Camera permission. If the permission has been denied
     * previously, a SnackBar will prompt the user to grant the permission,
     * otherwise it is requested directly.
     */
    public static void requestCameraPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                    Toast.makeText(activity, "El acceso a la cámara es necesario", Toast.LENGTH_SHORT).show();
                }*/
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.CAMERA }, REQUEST_PERMISSION_CAMERA);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.CAMERA }, REQUEST_PERMISSION_CAMERA);
            }
        } else {
            System.out.println("requestCameraPermission() PERMISSION ALREADY GRANTED");
        }
    }

    public static void requestLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(activity, "El acceso a la localización es necesario", Toast.LENGTH_SHORT).show();
                }*/
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION_LOCATION);
            }
        } else {
            System.out.println("requestLocationPermission() PERMISSION ALREADY GRANTED");
        }
    }

    public static void requestWriteExternalPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity, "El acceso a los archivos es necesario", Toast.LENGTH_SHORT).show();
                }*/
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_EXTERNAL);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_EXTERNAL);
            }
        } else {
            System.out.println("requestWriteExternalPermission() PERMISSION ALREADY GRANTED");
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

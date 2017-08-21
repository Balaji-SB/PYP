package com.android.pyp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.android.pyp.R;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by devel-73 on 19/8/17.
 */

public class Utils {

    private Bitmap bmp;
    public static Pattern pattern;
    public static Matcher matcher;
    public static SessionManager manager;
    public static SharedPreferences preferences;
    public Typeface ralewayMedium, ralewaySemiBold, ralewayBold, arial;
    private Context mContext;


    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized SessionManager getSessionManager(Context mContext) {
        if (null == manager) {
            manager = new SessionManager(mContext);
        }
        return manager;
    }

    public static synchronized SharedPreferences getSharedPreferences(Context mContext) {
        if (null == preferences)
            preferences = mContext.getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        return preferences;
    }


    /**
     * To validate if given string is valid email or not
     **/

    public static boolean validateEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }

    /**
     * To validate if given string is valid url or not
     **/

    public static boolean validateURL(String url) {
        if (url == null) {
            return false;
        }
        String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        return url.matches(urlPattern);
    }

    /**
     * To validate if given string is valid password or not.
     * <p>
     * Password must contains lowercase,uppercase,special character and number with minimum 6 characters and maximum 20 characters
     **/

    public static boolean validatePassword(String password) {
        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()]).{6,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * To validate if given string is valid date of birth or not(minimum 18 years old)
     **/

    public static boolean validateDOB(Calendar newDate) {
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        return !minAdultAge.before(newDate);
    }


    /**
     * To encrypt the given string into md5 format
     **/

    public static String encryptToMD5(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.trim().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * To get android device unique id
     **/

    public static String getUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * To show toast
     * type 0-short toast,1-long toast
     **/

    public static void presentToast(Context context, String message, int type) {
        if (type == 0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else if (type == 1) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * To show toast
     * type 0-short snackbar,1-long snackbar,2-indefinite
     **/

    public static void presentSnackBar(View view, String message, int type) {
        if (type == 0) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else if (type == 1) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else if (type == 2) {
            Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    /**
     * To show progress dialog
     **/

    public static void showProgressDialog(Context mContext, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * To dismiss progress dialog
     **/

    public static void dismissProgressDialog(Context mContext) {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.dismiss();
    }

    /**
     * To show alert
     */

    public static void showAlertDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("PYP");
        builder.setMessage("Are you sure want to exit?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void updateHomeDisplayWithBackstack(Fragment fragment, FragmentActivity mContext) {
        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame, fragment).addToBackStack(null).commit();

    }

    public static void updateHomeDisplay(Fragment fragment, FragmentActivity mContext) {
        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame, fragment).commit();

    }


    public Bitmap getBitmapFromURL(String new_url) {
        try {
            URL url = new URL(new_url);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            System.out.print(e);
        }
        return bmp;
    }
}

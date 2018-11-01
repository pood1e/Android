package me.pood1e.converter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String docId = DocumentsContract.getDocumentId(uri);
                String id = docId.split(":")[1];
                return getDataColumn(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        , "_id=?", new String[]{id});
            } else {
                return getDataColumn(context, uri, null, null);
            }

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor;
        String[] projection = {"_data"};
        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            return cursor.getString(column_index);
        }
        return null;
    }

    public static String getDstFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "vvv");
        if (!file.exists()) {
            file.mkdir();
        }
        File dir = new File(file, "mp3");
        if (!dir.exists()) {
            dir.mkdir();
        }
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String name;
        name = s.format(new Date());


        return new File(dir, name + ".mp3").getAbsolutePath();
    }


}

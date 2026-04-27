package com.example.textbookmarketplace.utils;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class FileHelper {

    public static String saveImage(Context context, Uri uri) {
        return copyToPrivate(context, uri, "images", ".jpg");
    }

    public static String saveDocument(Context context, Uri uri) {
        String ext = getExtension(context, uri);
        if (ext == null) ext = ".pdf";
        return copyToPrivate(context, uri, "documents", ext);
    }

    private static String copyToPrivate(Context context, Uri uri, String folder, String ext) {
        try {
            File dir = new File(context.getFilesDir(), folder);
            if (!dir.exists()) dir.mkdirs();
            String name = UUID.randomUUID().toString() + ext;
            File out = new File(dir, name);

            InputStream is = context.getContentResolver().openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(out);
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
            is.close();
            fos.close();
            return out.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getExtension(Context context, Uri uri) {
        String type = context.getContentResolver().getType(uri);
        if (type == null) return ".bin";
        if (type.contains("pdf")) return ".pdf";
        if (type.contains("word") || type.contains("document")) return ".docx";
        return ".bin";
    }

    public static String getFileTypeFromPath(String path) {
        if (path == null) return null;
        if (path.endsWith(".pdf")) return "pdf";
        if (path.endsWith(".docx")) return "docx";
        return null;
    }
}
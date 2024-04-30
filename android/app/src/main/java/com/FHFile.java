package com;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FHFile {

    // Public
    //----------------------------------------------------------------------------------------------------

    public static void copyAssets(Context context, String path) throws IOException {

        CharSequence dot = ".";

        String       rootdir = context.getFilesDir().getAbsolutePath() + File.separator + path;
        AssetManager manager = context.getAssets();

        String[] assets_list = manager.list(path);

        File dir = new File(rootdir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        for (String asset : assets_list) {

            String src_path = path + File.separator + asset;
            String dst_path = rootdir + File.separator + asset;

            File src_file = new File(src_path);
            File dst_file = new File(dst_path);

            if (src_path.contains(dot)) {
                if (!dst_file.exists()) {

                    InputStream  in  = null;
                    OutputStream out = null;

                    byte[] buffer = new byte[1024];
                    int len;

                    in  = manager.open(src_path);
                    out = new FileOutputStream(dst_path);

                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }

                    out.flush();

                    in.close();
                    out.close();

                    in  = null;
                    out = null;
                }
            } else {
                copyAssets(context, src_path);
            }
        }
    }

    //----------------------------------------------------------------------------------------------------
}

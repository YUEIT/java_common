package cn.yue.base.common.utils.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.yue.base.common.utils.Utils;

/**
 * Description : SAF 用户选择操作
 * Created by yue on 2020/5/21
 */
public class SAFHelper {

    private static final int DOCUMENT_REQUEST_CODE = 0xaf;

    public void toSearch(Activity activity, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        activity.startActivityForResult(intent, DOCUMENT_REQUEST_CODE);
    }

    public void toCreate(Activity activity, String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        activity.startActivityForResult(intent, DOCUMENT_REQUEST_CODE);
    }

    public void toDelete(Uri uri) {
        try {
            if (AndroidQFileUtils.fileUriIsExists(uri)) {
                DocumentsContract.deleteDocument(Utils.getContext().getContentResolver(), uri);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void toWrite(Uri uri, byte[] bytes) {
        try {
            ParcelFileDescriptor pfd = Utils.getContext().getContentResolver().
                    openFileDescriptor(uri, "w");
            if (pfd != null) {
                FileOutputStream fileOutputStream =
                        new FileOutputStream(pfd.getFileDescriptor());
                fileOutputStream.write(bytes);
                // Let the document provider know you're done by closing the stream.
                fileOutputStream.close();
                pfd.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == DOCUMENT_REQUEST_CODE) {
            if (data != null) {
                cacheUri = data.getData();
            }
        }
    }

    private Uri cacheUri;

    @Nullable
    public Uri getCacheUri() {
        return cacheUri;
    }
}

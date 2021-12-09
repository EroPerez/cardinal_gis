package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Michel on 6/2/2019.
 */

public class StubContentProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs,
                        final String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull final Uri uri, final String selection,
                      final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        return 0;
    }
}

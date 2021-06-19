package cu.phibrain.cardinal.app.helpers;

import android.content.Context;
import android.os.StatFs;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.text.DecimalFormat;

public class StorageUtilities {

    public static String getTotalExternalMemorySize(Context context) {
        File dirs[] = ContextCompat.getExternalFilesDirs(context, null);
        // dirs[0] refers to internal memory and dirs[1] gives you external. Call the
        // following methods to get total and available memory details.
        if (dirs.length > 1) {
            StatFs stat = new StatFs(dirs[1].getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return readableFileSize(totalBlocks * blockSize);
        }
        return "NA";

    }

    public static long getTotalExternalMemorySizeAsLong(Context context) {
        File dirs[] = ContextCompat.getExternalFilesDirs(context, null);
        // dirs[0] refers to internal memory and dirs[1] gives you external. Call the
        // following methods to get total and available memory details.
        if (dirs.length > 1) {
            StatFs stat = new StatFs(dirs[1].getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return readableFileLongSize(totalBlocks * blockSize);
        }
        return -1;

    }

    public static String getAvailableExternalMemorySize(Context context) {
        File dirs[] = ContextCompat.getExternalFilesDirs(context, null);
        if (dirs.length > 1) {
            StatFs stat = new StatFs(dirs[1].getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return readableFileSize(availableBlocks * blockSize);
        }
        return "NA";

    }

    public static long getAvailableExternalMemorySizeAsLong(Context context) {
        File dirs[] = ContextCompat.getExternalFilesDirs(context, null);
        if (dirs.length > 1) {
            StatFs stat = new StatFs(dirs[1].getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return readableFileLongSize(availableBlocks * blockSize);
        }
        return -1;

    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static long readableFileLongSize(long size) {
        if (size <= 0) return 0;
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return (long) (size / Math.pow(1024, digitGroups));
    }
}

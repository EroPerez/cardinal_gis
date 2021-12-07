package cu.phibrain.cardinal.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectHasImagesOperations;
import eu.geopaparazzi.library.camera.AbstractCameraActivity;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.sensors.OrientationSensor;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;

public class CameraMapObjectDefectActivity extends AbstractCameraActivity {
    protected double lon;
    protected double lat;
    protected double elevation;
    protected long defectId = -1;
    protected OrientationSensor orientationSensor;

    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);

        Bundle extras = getIntent().getExtras();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        orientationSensor = new OrientationSensor(sensorManager, null);
        orientationSensor.register(this, SensorManager.SENSOR_DELAY_NORMAL);
        defectId = extras.getLong(LibraryConstants.DATABASE_ID);
        lon = extras.getDouble(LibraryConstants.LONGITUDE);
        lat = extras.getDouble(LibraryConstants.LATITUDE);
        elevation = extras.getDouble(LibraryConstants.ELEVATION);


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean warningAlreadyShown = preferences.getBoolean(LibraryConstants.PREFS_KEY_CAMERA_WARNING_SHOWN, false);
        if (warningAlreadyShown) {
            doTakePicture(icicle);
        } else {
            GPDialogs.infoDialog(this, getString(eu.geopaparazzi.library.R.string.first_camera_open_warning), new Runnable() {
                @Override
                public void run() {
                    doTakePicture(icicle);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(LibraryConstants.PREFS_KEY_CAMERA_WARNING_SHOWN, true);
                    editor.apply();
                }
            });
        }
    }

    @Override
    public void doSaveData() {
        StringBuilder infoBuilder = new StringBuilder();
        try {
            Intent intent = getIntent();
            byte[] imageDataArray = ImageUtilities.getImageFromPath(imageFilePath, 5);

            double azimuth = orientationSensor.getAzimuthDegrees();
            if (Double.isNaN(azimuth)) {
                azimuth = -1;
            }

            infoBuilder.append("doSaveData - INFO\n");
            infoBuilder.append("lon: ").append(lon).append("\n");
            infoBuilder.append("lat: ").append(lat).append("\n");
            infoBuilder.append("elevation: ").append(elevation).append("\n");
            infoBuilder.append("azimuth: ").append(azimuth).append("\n");
            infoBuilder.append("currentDate: ").append(currentDate).append("\n");
            infoBuilder.append("imageFile: ").append(imageFile).append("\n");
            infoBuilder.append("defectId: ").append(defectId).append("\n");
            if (imageDataArray != null)
                infoBuilder.append("imageDataArray size: ").append(imageDataArray.length).append("\n");

            MapObjectHasDefectHasImages image = new MapObjectHasDefectHasImages(
                    null,
                    defectId,
                    imagenTratada(imageDataArray),
                    currentDate, lon, lat, elevation, azimuth
            );
            MapObjectHasDefectHasImagesOperations.getInstance().insert(image);


            intent.putExtra(LibraryConstants.DATABASE_ID, image.getId());
            intent.putExtra(LibraryConstants.OBJECT_EXISTS, true);

            Log.d("CameraMapObjectDefect", infoBuilder.toString());

            // delete the file after insertion in db
            imageFile.delete();
        } catch (Exception e) {
            GPLog.error(this, infoBuilder.toString(), e);
            GPDialogs.errorDialog(this, e, null);
        }

    }

    @Override
    public void finish() {
        orientationSensor.unregister();
        super.finish();
    }


    /**
     * Gets the last image id from the media store.
     *
     * @return the last image id from the media store.
     */
    private int getLastImageMediaId() {
        final String[] imageColumns = {MediaStore.Images.Media._ID};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        final String imageWhere = null;
        final String[] imageArguments = null;
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments,
                imageOrderBy);
        if (imageCursor.moveToFirst()) {
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            imageCursor.close();
            return id;
        } else {
            return 0;
        }
    }

    private byte[] imagenTratada(byte[] imagem_img) {

        if (imagem_img!=null && imagem_img.length > 800000) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(
                    bitmap,
                    768,
                    1024,
                    true
            );
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;

    }

}

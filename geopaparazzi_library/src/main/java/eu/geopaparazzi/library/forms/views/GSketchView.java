/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2016  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.geopaparazzi.library.forms.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.geopaparazzi.library.R;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.DefaultHelperClasses;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.IImagesDbHelper;
import eu.geopaparazzi.library.database.Image;
import eu.geopaparazzi.library.forms.FormDetailFragment;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.plugin.style.StyleHelper;
import eu.geopaparazzi.library.sketch.SketchUtilities;
import eu.geopaparazzi.library.util.AppsUtilities;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.PositionUtilities;

import static eu.geopaparazzi.library.forms.FormUtilities.COLON;
import static eu.geopaparazzi.library.forms.FormUtilities.UNDERSCORE;

/**
 * A custom Sketch view.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
@SuppressWarnings("ALL")
public class GSketchView extends View implements GView {

    private long noteId;
    private String _value;

    private List<String> addedImages = new ArrayList<String>();

    private LinearLayout imageLayout;

    /**
     * @param context  the context to use.
     * @param attrs    attributes.
     * @param defStyle def style.
     */
    public GSketchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param context the context to use.
     * @param attrs   attributes.
     */
    public GSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param noteId                the id of the note this image belows to.
     * @param fragmentDetail        the fragment detail  to use.
     * @param attrs                 attributes.
     * @param requestCode           the code for starting the activity with result.
     * @param parentView            parent
     * @param label                 label
     * @param value                 value
     * @param constraintDescription constraints
     */
    public GSketchView(final long noteId, final FormDetailFragment fragmentDetail, AttributeSet attrs, final int requestCode, LinearLayout parentView, String label, String value,
                       String constraintDescription) {
        super(fragmentDetail.getActivity(), attrs);
        this.noteId = noteId;

        _value = value;

        final FragmentActivity activity = fragmentDetail.getActivity();
        LinearLayout textLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        textLayout.setLayoutParams(layoutParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        parentView.addView(textLayout);

        TextView textView = new TextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        textView.setPadding(2, 2, 2, 2);
        textView.setText(label.replace(UNDERSCORE, " ").replace(COLON, " ") + " " + constraintDescription);
        textView.setTextColor(Compat.getColor(activity, R.color.formcolor));
        textLayout.addView(textView);

        final Button button = new Button(activity);
        StyleHelper.styleButton(activity, button);
        button.setText(R.string.draw_sketch);
        textLayout.addView(button);

        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                    double[] gpsLocation = PositionUtilities.getGpsLocationFromPreferences(preferences);

                    Date currentDate = new Date();
                    String sketchImageName = ImageUtilities.getSketchImageName(currentDate);

                    File tempDir = ResourcesManager.getInstance(getContext()).getTempDir();
                    File sketchFile = new File(tempDir, sketchImageName);
                    /*
                     * open markers for new sketch
                     */
                    SketchUtilities.launch(fragmentDetail, sketchFile, gpsLocation, requestCode);
                } catch (Exception e) {
                    GPLog.error(this, null, e);
                }
            }
        });

        ScrollView scrollView = new ScrollView(activity);
        ScrollView.LayoutParams scrollLayoutParams = new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        scrollView.setLayoutParams(scrollLayoutParams);
        parentView.addView(scrollView);

        imageLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        imageLayout.setLayoutParams(imageLayoutParams);
        // imageLayout.setMinimumHeight(200);
        imageLayout.setOrientation(LinearLayout.HORIZONTAL);
        scrollView.addView(imageLayout);
        // scrollView.setFillViewport(true);

        try {
            refresh(activity);
        } catch (Exception e) {
            GPLog.error(this, null, e);
        }
    }

    public void refresh(final Context context) throws Exception {
        log("Entering refresh....");

        if (_value != null && _value.length() > 0) {
            String[] imageSplit = _value.split(";");
            log("Handling images: " + _value);


            final IImagesDbHelper imagesDbHelper = DefaultHelperClasses.getDefaulfImageHelper();

            for (String imageId : imageSplit) {
                log("img: " + imageId);

                if (imageId.length() == 0) {
                    continue;
                }
                final long imageIdLong;
                try {
                    imageIdLong = Long.parseLong(imageId);
                } catch (Exception e) {
                    GPLog.error(this, null, e);
                    continue;
                }
                if (addedImages.contains(imageId.trim())) {
                    continue;
                }

                byte[] imageThumbnail = imagesDbHelper.getImageThumbnail(imageIdLong);
                Bitmap thumbnail = ImageUtilities.getImageFromImageData(imageThumbnail);

                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
                lp.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(lp);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setImageBitmap(thumbnail);
                imageView.setBackground(Compat.getDrawable(context, R.drawable.border_black_1px));
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        /*
                         * open in markers to edit it
                         */
//                        SketchUtilities.launchOnImage(context, image);
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Image image = imagesDbHelper.getImage(imageIdLong);
                            File tempDir = ResourcesManager.getInstance(context).getTempDir();
                            String ext = ".jpg";
                            if (image.getName().endsWith(".png"))
                                ext = ".png";
                            File imageFile = new File(tempDir, ImageUtilities.getTempImageName(ext));
                            byte[] imageData = imagesDbHelper.getImageData(image.getId());
                            ImageUtilities.writeImageDataToFile(imageData, imageFile.getAbsolutePath());


                            AppsUtilities.showImage(imageFile, context);
                        } catch (Exception e) {
                            GPLog.error(this, null, e);
                        }
                    }
                });
                log("Creating thumb and adding it: " + imageId);
                imageLayout.addView(imageView);
                imageLayout.invalidate();

                addedImages.add(imageId);
            }

            if (addedImages.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (String imagePath : addedImages) {
                    sb.append(";").append(imagePath);
                }
                _value = sb.substring(1);

                log("New img paths: " + _value);

            }
            log("Exiting refresh....");

        }
    }

    private void log(String msg) {
        if (GPLog.LOG_HEAVY)
            GPLog.addLogEntry(this, null, null, msg);
    }

    public String getValue() {
        return _value;
    }

    @Override
    public void setOnActivityResult(Intent data) {
        String absoluteImagePath = data.getStringExtra(LibraryConstants.PREFS_KEY_PATH);
        if (absoluteImagePath != null) {
            File imgFile = new File(absoluteImagePath);
            if (!imgFile.exists()) {
                return;
            }
            try {
                IImagesDbHelper imageHelper = DefaultHelperClasses.getDefaulfImageHelper();


                double lat = data.getDoubleExtra(LibraryConstants.LATITUDE, 0.0);
                double lon = data.getDoubleExtra(LibraryConstants.LONGITUDE, 0.0);
                double elev = data.getDoubleExtra(LibraryConstants.ELEVATION, 0.0);

                byte[][] imageAndThumbnailArray = ImageUtilities.getImageAndThumbnailFromPath(absoluteImagePath, 10);

                Date currentDate = new Date();
                String name = ImageUtilities.getSketchImageName(currentDate);
                long imageId = imageHelper.addImage(lon, lat, elev, -9999.0, currentDate.getTime(), name, imageAndThumbnailArray[0], imageAndThumbnailArray[1], noteId);

                // delete the file after insertion in db
                imgFile.delete();


                _value = _value + GPictureView.IMAGE_ID_SEPARATOR + imageId;
                try {
                    refresh(getContext());
                } catch (Exception e) {
                    GPLog.error(this, null, e);
                }
            } catch (Exception e) {
                GPLog.error(this, null, e);
            }
        }
    }

}
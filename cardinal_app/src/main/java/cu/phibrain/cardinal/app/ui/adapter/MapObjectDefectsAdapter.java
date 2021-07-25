package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.ui.activities.CameraMapObjectDefectActivity;
import cu.phibrain.cardinal.app.ui.activities.MapObjectDefectImageGalleryActivity;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectOperations;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.PositionUtilities;

public class MapObjectDefectsAdapter extends RecyclerView.Adapter<MapObjectDefectsAdapter.MapObjectDefectsViewHolder> {
    private List<MapObjectHasDefect> defectList;
    private List<MapObjecTypeDefect> typeDefectList;
    private MapObject mapObject;
    private Context context;
    private FragmentActivity inspector;

    public MapObjectDefectsAdapter(FragmentActivity inspector, List<MapObjecTypeDefect> typeDefects, MapObject mapObject) {
        this.inspector = inspector;
        this.defectList = mapObject.getDefects();
        this.typeDefectList = typeDefects;
        this.mapObject = mapObject;
    }

    @NonNull
    @Override
    public MapObjectDefectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();

        View rootView = LayoutInflater.from(context).inflate(R.layout.recyclerview_object_defects_item, parent, false);
        return new MapObjectDefectsViewHolder(rootView, i);
    }

    @Override
    public void onBindViewHolder(@NonNull MapObjectDefectsViewHolder holder, int position) {
        MapObjecTypeDefect defect = typeDefectList.get(position);
        boolean isChecked = this.contains(defect);

        holder.checkBox.setText(defect.getDescription());
        holder.checkBox.setChecked(isChecked);
        if (!isChecked)
            holder.imgBtnAddDefectImage.setVisibility(View.INVISIBLE);
        else
            holder.imgBtnAddDefectImage.setVisibility(View.VISIBLE);
        try {
            byte[] icon = defect.getIcon();
            if (icon != null) {
                holder.icon.setImageBitmap(
                        ImageUtil.getScaledBitmap(
                                ImageUtilities.getImageFromImageData(icon),
                                48,
                                48,
                                true
                        )
                );
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return typeDefectList.size();
    }

    public void setDefectList(List<MapObjectHasDefect> defectList) {
        this.defectList = defectList;
        notifyDataSetChanged();
    }

    private boolean contains(MapObjecTypeDefect defect) {
        for (MapObjectHasDefect hasDefect :
                defectList) {
            if (hasDefect.getMapObjectDefectId() == defect.getId())
                return true;
        }

        return false;
    }

    private MapObjectHasDefect load(MapObjecTypeDefect defect) {
        for (MapObjectHasDefect hasDefect :
                defectList) {
            if (hasDefect.getMapObjectDefectId() == defect.getId())
                return hasDefect;
        }

        return null;
    }

    public class MapObjectDefectsViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        // Delete
        public ImageButton imgBtnAddDefectImage;
        public ImageView icon;

        private int position;

        public MapObjectDefectsViewHolder(@NonNull View itemView, int i) {
            super(itemView);
            this.setPosition(i);

            icon = itemView.findViewById(R.id.defect_icon);
            imgBtnAddDefectImage = itemView.findViewById(R.id.imgBtnAddDefectImage);

            checkBox = itemView.findViewById(R.id.CheckBoxDefects);
            checkBox.setOnClickListener(v -> {
                // Is the view now checked?
                boolean isSelected = ((CheckBox) v).isChecked();
                MapObjecTypeDefect defect = typeDefectList.get(position);

                if (isSelected) {
                    MapObjectHasDefect hasDefect = new MapObjectHasDefect(mapObject.getId(), defect.getId());
                    MapObjectHasDefectOperations.getInstance().save(hasDefect);
                    defectList.add(hasDefect);
                    notifyDataSetChanged();


                } else if (!isSelected) {
                    MapObjectHasDefect hasDefect = load(defect);
                    if (hasDefect != null) {
                        MapObjectHasDefectOperations.getInstance().delete(hasDefect);
                        defectList.remove(hasDefect);
                        notifyDataSetChanged();
                    }
                }
                Log.d("Defect", "isClicked: " + position);


            });

            //Show defects image list
            checkBox.setOnLongClickListener(view -> {
                MapObjecTypeDefect defect = typeDefectList.get(position);
                MapObjectHasDefect object = load(defect);
                if (object != null) {
                    Intent intent = new Intent(inspector, MapObjectDefectImageGalleryActivity.class);
                    intent.putExtra(LibraryConstants.DATABASE_ID, object.getId());
                    intent.putExtra(LibraryConstants.PREFS_KEY_TEXT, defect.getDescription());
                    inspector.startActivity(intent);
                }
                return true;
            });


            // Delete

            imgBtnAddDefectImage.setOnClickListener(v -> {
                if (inspector == null)
                    return;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(inspector);
                double[] gpsLocation = PositionUtilities.getGpsLocationFromPreferences(preferences);
                MapObjecTypeDefect defect = typeDefectList.get(position);
                MapObjectHasDefect object = load(defect);
                if (object != null) {

                    String imageName = ImageUtilities.getCameraImageName(null);
                    Intent cameraIntent = new Intent(inspector, CameraMapObjectDefectActivity.class);
                    cameraIntent.putExtra(LibraryConstants.PREFS_KEY_CAMERA_IMAGENAME, imageName);
                    cameraIntent.putExtra(LibraryConstants.DATABASE_ID, object.getId());

                    if (gpsLocation != null) {
                        cameraIntent.putExtra(LibraryConstants.LATITUDE, gpsLocation[1]);
                        cameraIntent.putExtra(LibraryConstants.LONGITUDE, gpsLocation[0]);
                        cameraIntent.putExtra(LibraryConstants.ELEVATION, gpsLocation[2]);
                    }

                    inspector.startActivityForResult(cameraIntent, 667);
                }

            });

        }


        public void setPosition(int position) {
            this.position = position;
        }
    }
}

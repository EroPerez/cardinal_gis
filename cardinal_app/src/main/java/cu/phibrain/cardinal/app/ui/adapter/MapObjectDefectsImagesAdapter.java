package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectHasImagesOperations;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.util.AppsUtilities;
import eu.geopaparazzi.library.util.GPDialogs;

public class MapObjectDefectsImagesAdapter extends RecyclerView.Adapter<MapObjectDefectsImagesAdapter.MapObjectDefectsImagesViewHolder> {

    private List<MapObjectHasDefectHasImages> defectImagestList;
    private Context context;
    private FragmentActivity inspector;

    public MapObjectDefectsImagesAdapter(FragmentActivity inspector, List<MapObjectHasDefectHasImages> defectsImages) {
        this.inspector = inspector;
        this.defectImagestList = defectsImages;

    }

    @NonNull
    @Override
    public MapObjectDefectsImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();

        View rootView = LayoutInflater.from(context).inflate(R.layout.recyclerview_object_defects_photo_item, parent, false);
        return new MapObjectDefectsImagesViewHolder(rootView, i);
    }

    @Override
    public void onBindViewHolder(@NonNull MapObjectDefectsImagesViewHolder holder, int position) {
        MapObjectHasDefectHasImages defect = defectImagestList.get(position);

        holder.imageView.setImageBitmap(
                ImageUtil.getScaledBitmap(
                        ImageUtilities.getImageFromImageData(defect.getImage()),
                        200,
                        200,
                        false
                )
        );

        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return defectImagestList.size();
    }

    public void setDefecImagestList(List<MapObjectHasDefectHasImages> defectHasImages) {
        this.defectImagestList = defectHasImages;
        notifyDataSetChanged();
    }


    public class MapObjectDefectsImagesViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        // Delete
        private ImageButton imgBttnDelete;

        private int position;

        protected boolean toggle;

        public MapObjectDefectsImagesViewHolder(@NonNull View itemView, int i) {
            super(itemView);
            this.setPosition(i);
            toggle = false;
            // Delete
            imgBttnDelete = itemView.findViewById(R.id.imageButton);
            imgBttnDelete.setVisibility(View.INVISIBLE);
            // Image placeholder
            imageView = itemView.findViewById(R.id.imgvObjectDefectPhoto);
            //Show defects image list
            imageView.setOnClickListener(view -> {

                try {
                    MapObjectHasDefectHasImages image = defectImagestList.get(this.position);
                    String imageName =  ImageUtilities.getCameraImageName(image.getCreatedAt());
                    AppsUtilities.showImage(image.getImage(), imageName, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            imageView.setOnLongClickListener(view -> {
                if (!toggle) {
                    imgBttnDelete.setVisibility(View.VISIBLE);
                    toggle = true;
                } else {
                    imgBttnDelete.setVisibility(View.INVISIBLE);
                    toggle = false;
                }
                return true;
            });

            imgBttnDelete.setOnClickListener(v -> {
                if (inspector == null)
                    return;

                GPDialogs.yesNoMessageDialog(inspector, inspector.getString(R.string.do_you_want_to_delete_this_image),
                        () -> inspector.runOnUiThread(() -> {
                            // stop logging
                            MapObjectHasDefectHasImages image = defectImagestList.get(this.position);
                            MapObjectHasDefectHasImagesOperations.getInstance().delete(image);
                            defectImagestList.remove(this.position);
                            notifyDataSetChanged();

                        }), null
                );

            });

        }


        public void setPosition(int position) {
            this.position = position;
        }
    }
}

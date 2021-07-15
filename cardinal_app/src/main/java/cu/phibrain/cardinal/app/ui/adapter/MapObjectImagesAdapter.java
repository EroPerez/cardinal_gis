package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.util.AppsUtilities;
import eu.geopaparazzi.library.util.GPDialogs;

public class MapObjectImagesAdapter extends RecyclerView.Adapter<MapObjectImagesAdapter.MapObjectImagesViewHolder> {
    private List<MapObjectImages> mapObjectImages;
    private Context context;
    private FragmentActivity inspector;

    public MapObjectImagesAdapter(FragmentActivity inspector, List<MapObjectImages> images) {
        this.inspector = inspector;
        this.setMapObjectImages(images);
    }

    @NonNull
    @Override
    public MapObjectImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        return new MapObjectImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_object_photo_item, null), i);
    }

    @Override
    public void onBindViewHolder(@NonNull MapObjectImagesViewHolder holder, int position) {
        MapObjectImages image = mapObjectImages.get(position);
        byte[] icon = image.getImage();
        if (icon != null)
            holder.updateIcon(icon);

        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mapObjectImages.size();
    }

    public void setMapObjectImages(List<MapObjectImages> mapObjectImages) {
        this.mapObjectImages = mapObjectImages;
        notifyDataSetChanged();
    }

    public class MapObjectImagesViewHolder extends RecyclerView.ViewHolder {
        private ImageView imIcon;
        // Delete
        private ImageButton imgBttnDelete;

        private int position;

        private boolean toggleDelete = false;


        public MapObjectImagesViewHolder(@NonNull View itemView, int i) {
            super(itemView);
            this.setPosition(i);
            imIcon = itemView.findViewById(R.id.imgvObjectPhoto);
            // Delete
            imgBttnDelete = itemView.findViewById(R.id.button_trash);
            imgBttnDelete.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(view -> {

                try {
                    MapObjectImages image = mapObjectImages.get(this.position);
                    String imageName =  ImageUtilities.getCameraImageName(image.getCreatedAt());
                    AppsUtilities.showImage(image.getImage(), imageName, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            itemView.setOnLongClickListener(view -> {
                if (!toggleDelete) {
                    imgBttnDelete.setVisibility(View.VISIBLE);
                    toggleDelete = true;
                } else {
                    imgBttnDelete.setVisibility(View.INVISIBLE);
                    toggleDelete = false;
                }
                return true;
            });

            imgBttnDelete.setOnClickListener(v -> {
                if (inspector == null)
                    return;

                GPDialogs.yesNoMessageDialog(inspector, inspector.getString(R.string.do_you_want_to_delete_this_image),
                        () -> inspector.runOnUiThread(() -> {
                            // stop logging
                            MapObjectImages image = mapObjectImages.get(this.position);
                            MapObjectImagesOperations.getInstance().delete(image);
                            mapObjectImages.remove(this.position);
                            notifyDataSetChanged();

                        }), null
                );

            });


        }

        public void updateIcon(byte[] icon) {
            Bitmap bmp = ImageUtilities.getImageFromImageData(icon);

            imIcon.setImageBitmap(ImageUtil.getScaledBitmap(bmp, 300, 250, false));
        }


        public void setPosition(int position) {
            this.position = position;
        }
    }
}

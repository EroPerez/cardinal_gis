package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.images.ImageUtilities;

public class MapObjectImagesAdapter extends RecyclerView.Adapter<MapObjectImagesAdapter.MapObjectImagesViewHolder> {
    private List<MapObjectImages> mapObjectImages;
    private Context context;

    public MapObjectImagesAdapter(List<MapObjectImages> images) {

        this.mapObjectImages = images;
    }

    @NonNull
    @Override
    public MapObjectImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        return new MapObjectImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_object_photo_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MapObjectImagesViewHolder holder, int position) {
        MapObjectImages image = mapObjectImages.get(position);
        byte[] icon = image.getImage();
        if (icon != null)
            holder.updateIcon(icon);
    }

    @Override
    public int getItemCount() {
        return  mapObjectImages.size();
    }

    public class MapObjectImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imIcon;

        public MapObjectImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imgvObjectPhoto);

            itemView.setOnClickListener(view -> {

            });
        }

        public void updateIcon(byte[] icon) {
            Bitmap bmp = ImageUtilities.getImageFromImageData(icon);

            imIcon.setImageBitmap(ImageUtil.getScaledBitmap(bmp, 250,250,false));
        }
    }
}

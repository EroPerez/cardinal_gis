package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttribute;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectMetadataOperations;
import eu.geopaparazzi.library.forms.FormUtilities;
import eu.geopaparazzi.library.forms.views.GView;

public class MapObjectAttrAdapter extends RecyclerView.Adapter<MapObjectAttrAdapter.MapObjectMetadataViewHolder> {

    private final FragmentActivity inspector;

    private List<MapObjectMetadata> metadata;

    private Context context;

    public MapObjectAttrAdapter(FragmentActivity inspector, List<MapObjectMetadata> objectMetadata) {
        this.metadata = objectMetadata;
        this.inspector = inspector;

    }

    @NonNull
    @Override
    public MapObjectMetadataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();

        View rootView = LayoutInflater.from(context).inflate(R.layout.recyclerview_object_attribute_item, parent, false);
        return new MapObjectMetadataViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MapObjectMetadataViewHolder holder, int position) {
        MapObjectMetadata data = metadata.get(position);

        holder.refreshView(data);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return metadata.size();
    }


    public class MapObjectMetadataViewHolder extends RecyclerView.ViewHolder {

        private GView addedView;
        private LinearLayout mainView;
        public ImageButton imgBtnSave;


        private int position;

        public MapObjectMetadataViewHolder(@NonNull View itemView) {
            super(itemView);
            mainView = itemView.findViewById(R.id.form_main);
            imgBtnSave = itemView.findViewById(R.id.imgBtnSave);
            imgBtnSave.setOnClickListener(v -> {
                if (addedView == null)
                    return;

                MapObjectMetadata meta = metadata.get(position);
                meta.setValue(addedView.getValue());
                MapObjectMetadataOperations.getInstance().save(meta);


            });
        }

        public void refreshView(MapObjectMetadata data) {
            MapObjectTypeAttribute attr = data.getAttribute();
            String constraintDescription = "[ " +("" + attr.getDescription()).replaceAll("\\<.*?\\>", "").replaceAll("\\p{P}","") + " ]";
            String label = attr.getName();
            String value = !(data.getValue().isEmpty()) ? data.getValue() : attr.getDefaultValue();
            int type = attr.getAtype();

            switch (type) {
                case 0: //TYPE_INTEGER
                    this.addedView = FormUtilities.addEditText(itemView.getContext(), mainView, label, value, 4, 0, constraintDescription,
                            false);
                    break;

                case 1: //TYPE_DOUBLE
                    this.addedView = FormUtilities.addEditText(itemView.getContext(), mainView, label, value, 1, 0, constraintDescription,
                            false);
                    break;

                case 4: //TYPE_BOOLEAN
                    this.addedView = FormUtilities.addBooleanView(itemView.getContext(), mainView, label, value, constraintDescription, false);
                    break;

                default: //TYPE_CHAR y TYPE_STRING
                    this.addedView = FormUtilities.addEditText(itemView.getContext(), mainView, label, value, 0, 0, constraintDescription,
                            false);
                    break;
            }

        }


        public void setPosition(int position) {
            this.position = position;
        }

    }
}

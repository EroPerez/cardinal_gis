package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasState;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasStateOperations;
import eu.geopaparazzi.library.style.ColorUtilities;

public class MapObjectStatesAdapter extends RecyclerView.Adapter<MapObjectStatesAdapter.MapObjectStatesViewHolder> {
    private List<MapObjectHasState> stateList;
    private List<MapObjecTypeState> typeStateList;
    private MapObject mapObject;
    private Context context;

    public MapObjectStatesAdapter(List<MapObjecTypeState> typeStates, MapObject mapObject) {
        this.stateList = mapObject.getStates();
        this.typeStateList = typeStates;
        this.mapObject = mapObject;
    }

    @NonNull
    @Override
    public MapObjectStatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();

        View rootView = LayoutInflater.from(context).inflate(R.layout.recyclerview_object_states_item, parent, false);
        return new MapObjectStatesViewHolder(rootView, i);
    }

    @Override
    public void onBindViewHolder(@NonNull MapObjectStatesViewHolder holder, int position) {
        MapObjecTypeState state = typeStateList.get(position);

        holder.checkBox.setText(state.getDescription());
        holder.checkBox.setChecked(this.contains(state));
        holder.checkBox.setBackgroundColor(ColorUtilities.toColor(state.getColor()));

        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return typeStateList.size();
    }

    public void setStateList(List<MapObjectHasState> stateList) {
        this.stateList = stateList;
        notifyDataSetChanged();
    }

    private boolean contains(MapObjecTypeState defect) {
        for (MapObjectHasState hasDefect :
                stateList) {
            if (hasDefect.getMapObjectStateId() == defect.getId())
                return true;
        }

        return false;
    }

    private MapObjectHasState load(MapObjecTypeState defect) {
        for (MapObjectHasState hasDefect :
                stateList) {
            if (hasDefect.getMapObjectStateId() == defect.getId())
                return hasDefect;
        }

        return null;
    }

    public class MapObjectStatesViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        private int position;

        public MapObjectStatesViewHolder(@NonNull View itemView, int i) {
            super(itemView);
            this.setPosition(i);

            checkBox = itemView.findViewById(R.id.CheckBoxStates);
            checkBox.setOnClickListener(v -> {
                // Is the view now checked?
                boolean isSelected = ((CheckBox) v).isChecked();
                MapObjecTypeState defect = typeStateList.get(position);

                if (isSelected) {
                    MapObjectHasState hasState = new MapObjectHasState(mapObject.getId(), defect.getId());
                    MapObjectHasStateOperations.getInstance().save(hasState);
                    stateList.add(hasState);
                    notifyDataSetChanged();


                } else if (!isSelected) {
                    MapObjectHasState hasState = load(defect);
                    if (hasState != null) {
                        MapObjectHasStateOperations.getInstance().delete(hasState);
                        stateList.remove(hasState);
                        notifyDataSetChanged();
                    }
                }
//              Log.d("Defect", "isClicked: " + position);


            });


        }


        public void setPosition(int position) {
            this.position = position;
        }
    }
}

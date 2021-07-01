package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;

public class LabelSubLotAdapter extends ArrayAdapter<LabelSubLot> {
    public LabelSubLotAdapter(@NonNull Context context, int resource, List<LabelSubLot> networksList) {
        super(context, resource, networksList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position) {
        return  !getItem(position).getGeolocated();
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_inv, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.tvSpinnerValue);

        LabelSubLot currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getLabelObj().getCode());
            if (isEnabled(position)) {
                textViewName.setTextColor(Color.BLACK);
            } else {
                textViewName.setTextColor(Color.GRAY);
            }

        }
        return convertView;
    }

    public int select(String code, Long workSession){
        LabelSubLot item = LabelSubLotOperations.getInstance().load(workSession, code);
        return getPosition(item);
    }
}

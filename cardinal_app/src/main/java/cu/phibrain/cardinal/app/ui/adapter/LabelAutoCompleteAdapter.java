package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;

public class LabelAutoCompleteAdapter extends ArrayAdapter<LabelSubLot> {

    Context context;
    int resource, textViewResourceId;
    List<LabelSubLot> items, tempItems, suggestions;

    public LabelAutoCompleteAdapter(Context context, int resource, int textViewResourceId, List<LabelSubLot> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }
        LabelSubLot subLot = items.get(position);
        if (subLot != null) {
            TextView lblName =  view.findViewById(textViewResourceId);
            if (lblName != null) {
                lblName.setText(subLot.getLabelObj().getCode());
                if (isEnabled(position)) {
                    lblName.setTextColor(Color.GRAY);
                } else {
                    lblName.setTextColor(Color.BLACK);
                }
            }
        }
        return view;
    }
    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).getGeolocated();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((LabelSubLot) resultValue).toString();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (LabelSubLot subLot : tempItems) {
                    if (subLot.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(subLot);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }


            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<LabelSubLot> filterList = (ArrayList<LabelSubLot>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (LabelSubLot subLot : filterList) {
                    add(subLot);
                }
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public int select(String code, Long workSession) {
        LabelSubLot item = LabelSubLotOperations.getInstance().load(workSession, code);
        return getPosition(item);
    }
}

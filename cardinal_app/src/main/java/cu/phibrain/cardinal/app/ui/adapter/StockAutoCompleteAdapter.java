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

import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;

public class StockAutoCompleteAdapter extends ArrayAdapter<Stock> {

    Context context;
    int resource, textViewResourceId;
    List<Stock> items, tempItems, suggestions;

    public StockAutoCompleteAdapter(Context context, int resource, int textViewResourceId, List<Stock> items) {
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
        Stock stock = items.get(position);
        if (stock != null) {
            TextView lblName =  view.findViewById(textViewResourceId);
            if (lblName != null) {
                lblName.setText(stock.getCode());
                if (isEnabled(position)) {
                    lblName.setTextColor(Color.BLACK);
                } else {
                    lblName.setTextColor(Color.RED);
                }
            }
        }
        return view;
    }
    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).getLocated();
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
            String str = ((Stock) resultValue).getCode();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Stock Stock : tempItems) {
                    if (Stock.getCode().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(Stock);
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
            List<Stock> filterList = (ArrayList<Stock>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Stock Stock : filterList) {
                    add(Stock);
                    notifyDataSetChanged();
                }
            } else {
                notifyDataSetInvalidated();
            }
        }
    };
}

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
import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;

public class StockAdapter extends ArrayAdapter<Stock> {
    public StockAdapter(@NonNull Context context, int resource, List<Stock> stockList) {
        super(context, resource, stockList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }

        return initView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).getLocated();
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_inv, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.tvSpinnerValue);

        position = position - 1; // Adjust for initial selection item

        Stock currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null && textViewName != null) {
            textViewName.setText(currentItem.getCode());
            if (isEnabled(position)) {
                textViewName.setTextColor(Color.GRAY);
            } else {
                textViewName.setTextColor(Color.BLACK);
            }

        }
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }


    public int select(Stock item) {
        return getPosition(item);
    }

    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(getContext());
        view.setText(R.string.select_one);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller);
        view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }
}

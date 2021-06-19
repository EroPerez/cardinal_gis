package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.model.WorkSession;

public class WorkSessionAdapter extends ArrayAdapter<WorkSession> {

    public interface OnClickCallback {
        void OnClickListener(WorkSession aSession);
    }

    ;
    private OnClickCallback mOclickCallback;

    class ViewHolder {
        CheckBox checkButton;
        TextView sessionText;
        ImageButton goButton;
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public WorkSessionAdapter(@NonNull Context context, int resource, List<WorkSession> workSessionList) {
        super(context, resource, workSessionList);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        ViewHolder holder;
        // Recycle existing view if passed as parameter
        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.activity_work_session_list_row, parent, false);
            holder = new ViewHolder();
            holder.checkButton = rowView.findViewById(R.id.selectedCheckBox);
            holder.sessionText = rowView.findViewById(R.id.worksessionrowtext);
            holder.goButton = rowView.findViewById(R.id.activatebutton);
            holder.goButton.setVisibility(View.GONE);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
            holder.goButton.setVisibility(View.GONE);
        }

        final WorkSession workSession = getItem(position);

        final CheckBox checkBox = holder.checkButton;

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBox.setChecked(isChecked);
            holder.goButton.setVisibility(View.VISIBLE);
        });


        holder.sessionText.setText(workSession.toString());

        holder.goButton.setOnClickListener(v -> {
            if (mOclickCallback != null)
                mOclickCallback.OnClickListener(workSession);
        });

        return rowView;
    }

    public void setOnClickListener(OnClickCallback listener) {

        mOclickCallback = listener;

    }
}

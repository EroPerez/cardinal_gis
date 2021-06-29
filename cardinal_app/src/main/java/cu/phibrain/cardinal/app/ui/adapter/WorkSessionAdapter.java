package cu.phibrain.cardinal.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.utils.JodaTimeHelper;

public class WorkSessionAdapter extends ArrayAdapter<WorkSession> {

    public interface OnClickCallback {
        void OnClickListener(WorkSession aSession);
    }

    ;
    private OnClickCallback mOclickCallback;

    class ViewHolder {
        ImageView ivAvatar;
        TextView sessionWorkerText;
        TextView sessionZoneText;
        TextView sessionStartDateText;
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
            holder.ivAvatar = rowView.findViewById(R.id.imgvavatar);
            holder.sessionWorkerText = rowView.findViewById(R.id.worksession_nametext);
            holder.sessionZoneText = rowView.findViewById(R.id.worksessionrowtext);
            holder.sessionStartDateText = rowView.findViewById(R.id.worksessionstartdatetext);
            holder.goButton = rowView.findViewById(R.id.activatebutton);
            holder.goButton.setVisibility(View.VISIBLE);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
            holder.goButton.setVisibility(View.VISIBLE);
        }

        final WorkSession workSession = getItem(position);
        final Worker worker = workSession.getContractObj().getTheWorker();

        if (!worker.getAvatar().isEmpty()) {
            byte [] avatarByteCodes = worker.getAvatarAsByteArray();
            Bitmap avatar = BitmapFactory.decodeStream(new ByteArrayInputStream(avatarByteCodes));
            holder.ivAvatar.setImageBitmap(avatar);

        }

        holder.sessionWorkerText.setText("Worker: " + worker.getFullName());
        holder.sessionZoneText.setText("Zone: " + workSession.getZoneObj().getName());
        holder.sessionStartDateText.setText("Start Date: " + JodaTimeHelper.formatDate("yyyy-MM-dd", workSession.getStartDate()));
        if (!workSession.getActive())
            holder.goButton.setVisibility(View.GONE);

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

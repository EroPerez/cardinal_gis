package cu.phibrain.cardinal.app.ui.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.plugins.cardinal.io.R;
import cu.phibrain.plugins.cardinal.io.database.entity.NetworksOperations;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;

public class MtoAdapter extends RecyclerView.Adapter<MtoAdapter.MtoAdapterVh> implements Filterable {

    private List<MapObjecType> MtoModelList;
    private List<MapObjecType> getMtoModelListFiltered;
    private Context context;
    private SelectedMto selectedMto;

    public MtoAdapter(List<MapObjecType> MtoModelList, SelectedMto selectedMto) {
        this.MtoModelList = MtoModelList;
        this.getMtoModelListFiltered = MtoModelList;
        this.selectedMto = selectedMto;
    }

    @NonNull
    @Override
    public MtoAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new MtoAdapter.MtoAdapterVh(LayoutInflater.from(context).inflate(R.layout.recyclerview_item,null));
    }


    @Override
    public void onBindViewHolder(@NonNull MtoAdapter.MtoAdapterVh holder, int position) {

        MapObjecType mapObjecType = MtoModelList.get(position);
        byte [] icon = mapObjecType.getIconAsByteArray();
        if (icon != null)
            holder.updateIcon(icon);
    }

    @Override
    public int getItemCount() {
        return MtoModelList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                AppContainer appContainer = ((CardinalApplication)CardinalApplication.getInstance()).appContainer;
                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getMtoModelListFiltered.size();
                    filterResults.values = getMtoModelListFiltered;

                }else{
                    List<MapObjecType> resultData = new ArrayList<>();
                    resultData = NetworksOperations.getInstance().getMapObjectTypes(appContainer.NetworksActive);
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                MtoModelList = (List<MapObjecType>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public interface SelectedMto {
        void selectedMto(MapObjecType _mtoModel);
    }

    public class MtoAdapterVh extends RecyclerView.ViewHolder {
        ImageView imIcon;
        public MtoAdapterVh(@NonNull View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedMto.selectedMto(MtoModelList.get(getAdapterPosition()));
                }
            });
        }

        public void updateIcon(byte[] icon){
            Bitmap bmp = BitmapFactory.decodeByteArray(icon, 0, icon.length);
            imIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp,30,
                    30, false));
        }
    }
}

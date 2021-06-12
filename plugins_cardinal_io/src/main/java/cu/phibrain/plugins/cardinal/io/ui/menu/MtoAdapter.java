package cu.phibrain.plugins.cardinal.io.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.R;

public class MtoAdapter extends RecyclerView.Adapter<MtoAdapter.MtoAdapterVh> implements Filterable {

    private List<MtoModel> MtoModelList;
    private List<MtoModel> getMtoModelListFiltered;
    private Context context;
    private SelectedMto selectedMto;

    public MtoAdapter(List<MtoModel> MtoModelList, SelectedMto selectedMto) {
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

        MtoModel MtoModel = MtoModelList.get(position);

        String username = MtoModel.getUserName();
        String prefix = MtoModel.getUserName().substring(0,1);

       // holder.tvUsername.setText(username);
        //holder.tvPrefix.setText(prefix);

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

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getMtoModelListFiltered.size();
                    filterResults.values = getMtoModelListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<MtoModel> resultData = new ArrayList<>();

                    for(MtoModel MtoModel: getMtoModelListFiltered){
                        if(MtoModel.getUserName().toLowerCase().contains(searchChr)){
                            resultData.add(MtoModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                MtoModelList = (List<MtoModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }


    public interface SelectedMto {

        void selectedMto(MtoModel _mtoModel);

    }

    public class MtoAdapterVh extends RecyclerView.ViewHolder {

        TextView tvPrefix;
        TextView tvUsername;
        ImageView imIcon;
        public MtoAdapterVh(@NonNull View itemView) {
            super(itemView);
//            tvPrefix = itemView.findViewById(R.id.prefix);
//            tvUsername = itemView.findViewById(R.id.username);
            imIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedMto.selectedMto(MtoModelList.get(getAdapterPosition()));
                }
            });


        }
    }
}

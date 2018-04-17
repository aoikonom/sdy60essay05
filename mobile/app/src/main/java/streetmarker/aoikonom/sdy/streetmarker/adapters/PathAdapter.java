package streetmarker.aoikonom.sdy.streetmarker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.data.PathFB;
import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.utils.PathType;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.PathViewHolder> {
    private List<PathFB> mPathsFB;
    private ClickListener mListener;

    public interface ClickListener {
        void onItemClickListener(int position);
    }

    public class PathViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView;
        public TextView createByTextView;
        public ImageView pathTypeImageView;

        public PathViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.path_name);
            createByTextView = itemView.findViewById(R.id.crated_by);
            pathTypeImageView = itemView.findViewById(R.id.path_type_image);
        }



        public void bind(final int position, final PathFB path, final ClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position);
                }
            });
        }
    }


    public PathAdapter(List<PathFB> paths, ClickListener listener) {
        super();
        mPathsFB = paths;
        mListener = listener;
    }

    @NonNull
    @Override
    public PathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_row_layout, parent,  false);
        return new PathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathViewHolder holder, int position) {
        PathFB pathFB = mPathsFB.get(position);

        holder.nameTextView.setText(pathFB.getName());
        holder.createByTextView.setText(pathFB.getCreatedByUser());
        holder.pathTypeImageView.setImageResource(PathType.valueOf(pathFB.getType()).getIcon());

        holder.bind(position, pathFB, mListener);

    }

    @Override
    public int getItemCount() {
        return mPathsFB.size();
    }
}

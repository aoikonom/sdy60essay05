package streetmarker.aoikonom.sdy.streetmarker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.utils.PathType;

public class PathTypeAdapter extends ArrayAdapter<PathType> {
    private Context mContext;

    public PathTypeAdapter(Context context) {
        super(context, R.layout.path_type_spinner_row, PathType.values());
        mContext = context;
        this.setDropDownViewResource(R.layout.path_type_spinner_row);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public PathType getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.path_type_spinner_row, parent, false);
        TextView textView = rowView.findViewById(R.id.type_name);
        ImageView imageView = rowView.findViewById(R.id.type_image);
        PathType pathType = getItem(position);
        textView.setText(pathType.name());
        imageView.setImageResource(pathType.getIcon());

        return rowView;
    }
}

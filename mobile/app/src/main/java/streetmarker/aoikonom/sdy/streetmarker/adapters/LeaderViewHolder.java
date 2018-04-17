package streetmarker.aoikonom.sdy.streetmarker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import streetmarker.aoikonom.sdy.streetmarker.R;

public class LeaderViewHolder extends RecyclerView.ViewHolder{

    public TextView userNameTextView;
    public TextView scoreTextView;

    public LeaderViewHolder(View itemView) {
        super(itemView);
        userNameTextView = itemView.findViewById(R.id.user_name);
        scoreTextView = itemView.findViewById(R.id.score);
    }
}
package streetmarker.aoikonom.sdy.streetmarker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.model.Review;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;

public class LeaderBoardAdapter extends FirebaseRecyclerAdapter<UserInfo, LeaderViewHolder>{

    public static LeaderBoardAdapter newAdapter(Query query, Context context) {
        FirebaseRecyclerOptions<UserInfo> options =
                new FirebaseRecyclerOptions.Builder<UserInfo>()
                        .setQuery(query, UserInfo.class)
                        .build();
        return new LeaderBoardAdapter(options);
    }

    private LeaderBoardAdapter(FirebaseRecyclerOptions<UserInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(LeaderViewHolder viewHolder,int position, UserInfo userInfo) {
        viewHolder.userNameTextView.setText(userInfo.getUserName());
        viewHolder.scoreTextView.setText(String.valueOf(userInfo.getPoints()));
    }


    @Override
    public LeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_row_layout, parent, false);

        return new LeaderViewHolder(view);
    }

    @NonNull
    @Override
    public UserInfo getItem(int position) {
        return super.getItem(getItemCount() - 1 - position);
    }
}
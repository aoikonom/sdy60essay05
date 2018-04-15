package streetmarker.aoikonom.sdy.streetmarker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.model.Review;

public class ReviewAdapter extends FirebaseRecyclerAdapter<Review, ReviewViewHolder>{
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private Context context;

    public static ReviewAdapter newAdapter(Query query, Context context) {
        FirebaseRecyclerOptions<Review> options =
                new FirebaseRecyclerOptions.Builder<Review>()
                        .setQuery(query, Review.class)
                        .build();
        return new ReviewAdapter(options, context);
    }

    private ReviewAdapter(FirebaseRecyclerOptions<Review> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(ReviewViewHolder viewHolder,int position, Review review) {
        viewHolder.writtenByTextView.setText(review.getWrittenByUserName());
        viewHolder.commentsTextView.setText(review.getComments());
        viewHolder.ratingBar.setRating(review.getRating());
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row_layout, parent, false);

        return new ReviewViewHolder(view);
    }


}
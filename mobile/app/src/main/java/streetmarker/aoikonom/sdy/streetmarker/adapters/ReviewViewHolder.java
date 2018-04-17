package streetmarker.aoikonom.sdy.streetmarker.adapters;

        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import streetmarker.aoikonom.sdy.streetmarker.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = ReviewViewHolder.class.getSimpleName();

    public TextView writtenByTextView;
    public RatingBar ratingBar;
    public TextView commentsTextView;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        writtenByTextView = itemView.findViewById(R.id.written_by_user);
        ratingBar = itemView.findViewById(R.id.rating);
        commentsTextView = itemView.findViewById(R.id.review_comments);
    }
}
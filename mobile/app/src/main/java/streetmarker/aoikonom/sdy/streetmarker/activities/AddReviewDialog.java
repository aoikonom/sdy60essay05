package streetmarker.aoikonom.sdy.streetmarker.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.data.DB;
import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.model.Review;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;

public class AddReviewDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private TextView mUserNameTextView;
    private TextView mCommentsTextView;
    private RatingBar mRatingBar;

    public interface ReviewListener {
        void onReviewAdded(Path path,Review review);
    }

    private Path mPath;
    private UserInfo mReviewer;
    private ReviewListener mListener;

    private AddReviewDialog(UserInfo reviewer,Path path,ReviewListener listener) {
        super();
        this.mPath = path;
        this.mReviewer = reviewer;
        this.mListener = listener;
    }

    public static AddReviewDialog newInstance(UserInfo mReviewer,Path path,ReviewListener listener) {
        return new AddReviewDialog(mReviewer, path, listener);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.write_review_layout, null);
        mUserNameTextView = dialogView.findViewById(R.id.written_by_user);
        mCommentsTextView = dialogView.findViewById(R.id.comments);
        mRatingBar = dialogView.findViewById(R.id.rating);
        mRatingBar.setNumStars(5);

        mUserNameTextView.setText(mReviewer.getUserName());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.write_review)
                .setPositiveButton("OK", this)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .setView(dialogView);

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        String userName = mReviewer.getUserName();
        String userId = mReviewer.getKey();
        String comments = mCommentsTextView.getText().toString();
        float rating = mRatingBar.getRating();

        Review review = new Review(userId, userName, rating, comments);
        DB.addReview(mReviewer, mPath, review);
    }
}

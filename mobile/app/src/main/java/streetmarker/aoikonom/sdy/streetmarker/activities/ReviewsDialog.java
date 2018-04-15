package streetmarker.aoikonom.sdy.streetmarker.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.adapters.ReviewAdapter;
import streetmarker.aoikonom.sdy.streetmarker.data.IPathRetrieval;
import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.model.Review;
import streetmarker.aoikonom.sdy.streetmarker.utils.PathType;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class ReviewsDialog extends DialogFragment implements View.OnClickListener, AddReviewDialog.ReviewListener {
    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private ImageView mTypeImageView;
    private TextView mRatingTextView;
    private RatingBar mRatingBar;
    private Button mReviewBtn;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mAdapater;

    private Path mPath;


    private ReviewsDialog(Path path) {
        super();
        this.mPath = path;
    }

    public static ReviewsDialog newInstance(Path path) {
        return new ReviewsDialog(path);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.path_reviews_layout, null);
        mNameTextView = dialogView.findViewById(R.id.path_name);
        mDescriptionTextView = dialogView.findViewById(R.id.path_description);
        mTypeImageView = dialogView.findViewById(R.id.path_type_image);
        mRatingTextView = dialogView.findViewById(R.id.avg_rating_text);
        mRatingBar = dialogView.findViewById(R.id.avg_rating);
        mRatingBar.setNumStars(5);
        mRecyclerView = dialogView.findViewById(R.id.reviews);
        mReviewBtn = dialogView.findViewById(R.id.add_review_btn);

        mReviewBtn.setOnClickListener(this);

        if (mPath != null) {
            Query query = FirebaseDatabase.getInstance().getReference("StreetMarker/reviews/").child(mPath.getKey());
            mAdapater = ReviewAdapter.newAdapter(query, getActivity());
            mRecyclerView.setAdapter(mAdapater);
        }

        if (mPath != null) {
            mNameTextView.setText(mPath.getName());
            mDescriptionTextView.setText(mPath.getDescription());
            mTypeImageView.setImageResource(mPath.getPathType().getIcon());
            mRatingTextView.setText(String.valueOf(mPath.getAvgRating()));
            if (mPath.getRatingsCount() != 0)
                mRatingBar.setRating(mPath.getAvgRating());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.reviews)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .setView(dialogView);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapater.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapater.stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        if (v == mReviewBtn) {
            AddReviewDialog dialog = AddReviewDialog.newInstance(mPath, this);
            dialog.show(getFragmentManager(), "Add Review");

        }
    }

    @Override
    public void onReviewAdded(Path path, Review review) {

    }
}

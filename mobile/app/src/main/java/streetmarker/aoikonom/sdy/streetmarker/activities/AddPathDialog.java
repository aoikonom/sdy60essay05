package streetmarker.aoikonom.sdy.streetmarker.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.adapters.PathTypeAdapter;
import streetmarker.aoikonom.sdy.streetmarker.data.IPathRetrieval;
import streetmarker.aoikonom.sdy.streetmarker.model.Coordinates;
import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.utils.PathType;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class AddPathDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private Spinner mTypeSpinner;
    private IPathRetrieval mListener;

    private Coordinates mCoordinates;
    private String mUserName;


    private AddPathDialog(String userName,Coordinates coordinates) {
        super();
        this.mUserName = userName;
        this.mCoordinates = coordinates;
    }

    public static AddPathDialog newInstance(String userName,Coordinates coordinates) {
        return new AddPathDialog(userName, coordinates);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_path_layout, null);
        mNameTextView = dialogView.findViewById(R.id.nameTextView);
        mDescriptionTextView = dialogView.findViewById(R.id.descroptionTextView);
        mTypeSpinner = dialogView.findViewById(R.id.typeSpinner);

        mTypeSpinner.setAdapter(new PathTypeAdapter(getActivity()));


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_path)
                .setPositiveButton("OK", this)
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (IPathRetrieval) context;

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                String name = mNameTextView.getText().toString();
                String description = mNameTextView.getText().toString();
                PathType pathType = (PathType) mTypeSpinner.getSelectedItem();
                Path path = new Path(null, name, description, mUserName,  mCoordinates, pathType, 0, 0);
                if (mListener != null)
                    mListener.onPathAdded(path, true);
        }
    }
}

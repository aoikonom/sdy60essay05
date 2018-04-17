package streetmarker.aoikonom.sdy.streetmarker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.adapters.PathAdapter;
import streetmarker.aoikonom.sdy.streetmarker.data.PathFB;


public class PathListActivity extends AppCompatActivity implements PathAdapter.ClickListener {
    RecyclerView mRecyclerView;
    ArrayList<PathFB> mPathsFB;

    public static String PATHS_ARGUMENT = "paths";
    public static String PATH_POSITION = "path_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mPathsFB = (ArrayList<PathFB>) getIntent().getExtras().getSerializable(PATHS_ARGUMENT);
        }
        catch (ClassCastException e) {
            e.printStackTrace();
            mPathsFB = null;
        }

        setContentView(R.layout.activity_path_list);

        mRecyclerView = findViewById(R.id.path_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new PathAdapter(mPathsFB, this));
    }

    @Override
    public void onItemClickListener(int position) {
        Intent result = new Intent();
        result.putExtra(PATH_POSITION, position);
        setResult(RESULT_OK, result);
        finish();
    }
}

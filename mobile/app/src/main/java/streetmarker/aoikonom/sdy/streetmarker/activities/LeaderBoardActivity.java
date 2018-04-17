package streetmarker.aoikonom.sdy.streetmarker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import streetmarker.aoikonom.sdy.streetmarker.R;
import streetmarker.aoikonom.sdy.streetmarker.adapters.LeaderBoardAdapter;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;

public class LeaderBoardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LeaderBoardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        mRecyclerView = findViewById(R.id.user_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference("StreetMarker").child("Users").orderByChild("points").limitToLast(10);

        mAdapter = LeaderBoardAdapter.newAdapter(query, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.stopListening();
    }
}

package streetmarker.aoikonom.sdy.streetmarker.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


import streetmarker.aoikonom.sdy.streetmarker.model.Path;
import streetmarker.aoikonom.sdy.streetmarker.model.UserInfo;

/**
 * Created by aoiko on 5/2/2018.
 */

public class DB {

    public static void retrievePaths(final IPathsRetrieval retrieval) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference targetRef = db.getReference("/StreetMarker/Paths");
        targetRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PathFB pathFB = dataSnapshot.getValue(PathFB.class);
                if (retrieval != null)
                    retrieval.onPathAdded(Path.fromPathFB(pathFB));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void retrieveUserInfo(final String userId, final IUserRetrieval retrieval) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference targetRef = db.getReference("/StreetGame/Users/" + userId);
        targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                if (retrieval != null)
                    retrieval.onUserRetrieved(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }


        });
    }

    public static void saveUserLocation(LatLng location) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StreetGame/Traversals/");
        String key = ref.push().getKey();
        ref.child(key).child("location").setValue(location);
        ref.child(key).child("at_time").setValue(ServerValue.TIMESTAMP);
    }


}

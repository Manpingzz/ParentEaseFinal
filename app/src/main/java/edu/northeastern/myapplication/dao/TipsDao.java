package edu.northeastern.myapplication.dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import edu.northeastern.myapplication.entity.Tip;
import edu.northeastern.myapplication.entity.User;

public class TipsDao {
    private final String PATH_TIPS = "tips";
    private DatabaseReference databaseReference;

    /**
     * No argument constructor for the class.
     */
    public TipsDao() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Creates a tip in the Firebase Realtime Database.
     *
     * @param tipId the tip id
     * @param tip   the tip object
     * @return a task
     */
    public Task<Void> create(String tipId, Tip tip) {
        Objects.requireNonNull(tipId);
        Objects.requireNonNull(tip);
        return databaseReference.child(PATH_TIPS).child(tipId).setValue(tip);
    }

    /**
     * Gets all the tips from the database.
     *
     * @return all the tips
     */
    public Task<DataSnapshot> getTips() {
        return databaseReference.child(PATH_TIPS).get();
    }
}

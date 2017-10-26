package com.mina.collegehelper.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mina on 19/10/17.
 */

public class DatabaseHelper {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();

    static String USERS_REF = "users";

    public static void getUsers(final ServerCallback callback) {
        DatabaseReference ref = database.getReference(USERS_REF);

        final ArrayList<User> users = new ArrayList<User>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> index = dataSnapshot.getChildren().iterator();
                while (index.hasNext()) {
                    users.add(index.next().getValue(User.class));
                }
                callback.onFinish(ServerResponse.success(users));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }

    public static void getUser(String id, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(USERS_REF).child(id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                callback.onFinish(ServerResponse.success(u));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }

    public static void saveUser(User user, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(USERS_REF).child(user.id);
        ref.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null) {
                    callback.onFinish(ServerResponse.success(null));
                } else {
                    callback.onFinish(ServerResponse.error(databaseError.getMessage()));
                }
            }
        });
    }



}

package com.mina.collegehelper.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.model.datastructure.Code;
import com.mina.collegehelper.model.datastructure.Course;
import com.mina.collegehelper.model.datastructure.Post;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;
import com.mina.collegehelper.model.datastructure.Year;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mina on 19/10/17.
 */
//TODO: check if firebase listens for more than one time
public class DatabaseHelper {

    private static String CODE_NOT_FOUND = "Code is not available";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static StorageReference imagesStorage = FirebaseStorage.getInstance().getReference().child("images");

    private static String USERS_REF = "users";
    private static String CODES_REF = "codes";
    private static String COURSES_REF = "courses";
    private static String POSTS_REF = "coursePosts";
    private static String YEARS_REF = "years";

    private static String CODE_VALID_REF = "valid";
    private static String IMAGE_REF = "image";

    public static void getUsers(final ServerCallback callback) {
        DatabaseReference ref = database.getReference(USERS_REF);

        final ArrayList<User> users = new ArrayList();
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

    public static void checkCode(final String id, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(CODES_REF);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(id)) {
                    Code c = snapshot.child(id).getValue(Code.class);
                    callback.onFinish(ServerResponse.success(c));
                } else {
                    callback.onFinish(ServerResponse.error(CODE_NOT_FOUND));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }

    public static void useCode(String id, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(CODES_REF).child(id).child(CODE_VALID_REF);
        ref.setValue(true, new DatabaseReference.CompletionListener() {
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

    public static void saveImageToUser(final String userID, Uri image, final ServerCallback callback) {
        StorageReference userImage = imagesStorage.child(userID);
        UploadTask uploadTask = userImage.putFile(image);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DatabaseReference ref = database.getReference(USERS_REF).child(userID).child(IMAGE_REF);
                ref.setValue(taskSnapshot.getDownloadUrl().toString(), new DatabaseReference.CompletionListener() {
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
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.onFinish(ServerResponse.error(exception.getMessage()));
            }
        });
    }

    public static void getUserCourses(String userID, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(USERS_REF).child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(u != null) {
                    if(u.courses != null) {
                        ArrayList<String> ids = new ArrayList<>(u.courses.keySet());
                        getCoursesByIds(ids, callback);
                    } else {
                        getCoursesByIds(new ArrayList<String>(), callback);
                    }
                } else {
                    callback.onFinish(ServerResponse.error("can't get courses"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }

    public static void getCourseDetails(String courseId, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(COURSES_REF).child(courseId);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course c = dataSnapshot.getValue(Course.class);
                if(c != null){
                    callback.onFinish(ServerResponse.success(c));
                } else {
                    callback.onFinish(ServerResponse.error("couldn't load course details"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }

    public static void getCoursePosts(String courseId, final ServerCallback callback) {
        DatabaseReference ref = database.getReference(POSTS_REF).child(courseId);
        final Map<String, Post> posts = new HashMap<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    posts.put(item.getKey(), item.getValue(Post.class));
                }
                callback.onFinish(ServerResponse.success(new ArrayList<>(posts.values())));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }


    public static void getYears(final ServerCallback callback) {
        DatabaseReference ref = database.getReference(YEARS_REF);
        final Map<String, Year> years = new HashMap<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    years.put(item.getKey(), item.getValue(Year.class));
                }
                callback.onFinish(ServerResponse.success(new ArrayList<>(years.values())));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFinish(ServerResponse.error(databaseError.getMessage()));
            }
        });
    }

    public static void getYearCourses(Year year, final ServerCallback callback) {
        getCoursesByIds(new ArrayList<>(year.courses.keySet()), callback);
    }

    private static void getCoursesByIds(ArrayList<String> ids, final ServerCallback callback) {
        final Map<String, Course> courses = new HashMap<>();
        for (String courseID: ids) {
            DatabaseReference ref = database.getReference(COURSES_REF).child(courseID);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Course c = dataSnapshot.getValue(Course.class);
                    if(c != null){
                        courses.put(c.id, c);
                        callback.onFinish(ServerResponse.success(new ArrayList<>(courses.values())));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onFinish(ServerResponse.error(databaseError.getMessage()));
                }
            });
        }
    }



}

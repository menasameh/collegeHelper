package com.mina.collegehelper.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mina.collegehelper.model.datastructure.Code;
import com.mina.collegehelper.model.datastructure.Course;
import com.mina.collegehelper.model.datastructure.Post;
import com.mina.collegehelper.model.datastructure.PostNotification;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;
import com.mina.collegehelper.model.datastructure.Year;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mina on 19/10/17.
 */
public class DatabaseHelper {

    private static String CODE_NOT_FOUND = "Code is not available";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static StorageReference imagesStorage = FirebaseStorage.getInstance().getReference().child("images");

    private static String USERS_REF = "users";
    private static String CODES_REF = "codes";
    private static String COURSES_REF = "courses";
    private static String POSTS_REF = "coursePosts";
    private static String YEARS_REF = "years";
    private static String TIMESTAMP_REF = "timestamp";
    private static String NOTIFICATIONS_REF = "notifications";



    private static String CODE_VALID_REF = "valid";
    private static String IMAGE_REF = "profilePictureUrl";

    public static void setupPersistence() {
        try {
            database.setPersistenceEnabled(true);
        } catch(Exception e) {
            Log.d("log", e.getLocalizedMessage());
        }
    }

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

    public static void getCurrentUser(final ServerCallback callback) {
        getUser(AuthenticationHelper.getCurrentUserId(), callback);
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
        ref.setValue(false, new DatabaseReference.CompletionListener() {
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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(u != null) {
                    DatabaseReference ref = database.getReference(CODES_REF).child(u.code);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Code c = snapshot.getValue(Code.class);
                            if(c.courses != null) {
                                ArrayList<String> ids = new ArrayList<>(c.courses.keySet());
                                getCoursesByIds(ids, callback);
                            } else {
                            getCoursesByIds(new ArrayList<String>(), callback);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onFinish(ServerResponse.error(databaseError.getMessage()));
                        }
                    });
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


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Post> posts = new HashMap<>();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Post current = item.getValue(Post.class);
                    current.id = item.getKey();
                    posts.put(item.getKey(), current);
                }
                ArrayList<Post> postsList = new ArrayList<>(posts.values());
                Collections.sort(postsList, new Comparator<Post>() {
                    @Override
                    public int compare(Post lhs, Post rhs) {
                        return -1 * lhs.timestamp.compareTo(rhs.timestamp);
                    }
                });
                callback.onFinish(ServerResponse.success(postsList));
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

    public static void addPost(String courseID, String postText, final ServerCallback callback){
        Post newPost = new Post();
        newPost.teacherId = AuthenticationHelper.getCurrentUserId();
        newPost.text = postText;
        newPost.timestamp = System.currentTimeMillis() + "";

        DatabaseReference ref = database.getReference(POSTS_REF).child(courseID);
        ref.push().setValue(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onFinish(ServerResponse.success(""));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFinish(ServerResponse.error(e.getMessage()));
            }
        });

        PostNotification postNotification = new PostNotification();
        postNotification.courseId = courseID;
        postNotification.teacherId = newPost.teacherId;
        postNotification.text = newPost.text;

        database.getReference(NOTIFICATIONS_REF)
                .push().setValue(postNotification);
    }

    public static void editPost(String courseID, String postId, String postText, final ServerCallback callback){
        Post newPost = new Post();
        newPost.teacherId = AuthenticationHelper.getCurrentUserId();
        newPost.text = postText;
        newPost.timestamp = System.currentTimeMillis() + "";

        DatabaseReference ref = database.getReference(POSTS_REF).child(courseID);
        ref.child(postId).setValue(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onFinish(ServerResponse.success(""));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFinish(ServerResponse.error(e.getMessage()));
            }
        });
    }

    public static void deletePost(String courseID, String postId, final ServerCallback callback){

        DatabaseReference ref = database.getReference(POSTS_REF).child(courseID).child(postId);
        ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                callback.onFinish(ServerResponse.success(""));
            }
        });

    }

    public static void toggleSubscription(final boolean subscribe) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = database.getReference(USERS_REF).child(currentUserId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(u != null) {
                    DatabaseReference ref = database.getReference(CODES_REF).child(u.code);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Code c = snapshot.getValue(Code.class);
                            if(c.courses != null) {
                                ArrayList<String> ids = new ArrayList<>(c.courses.keySet());
                                for(int i=0;i<ids.size();i++) {
                                    if(subscribe)
                                        FirebaseMessaging.getInstance().subscribeToTopic(ids.get(i));
                                    else
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(ids.get(i));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

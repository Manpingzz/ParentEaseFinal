package edu.northeastern.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.net.URL;
import java.util.List;

public class Tip {
    private String title;
    private String username;
    private int imageID;

    public Tip(String title, String username, int imageID) {
        this.title = title;
        this.username = username;
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}

//public class Tip implements Parcelable {
//    private String title;
//    private String username;
//    private String filter;
//    private String content;
//    private List comments;
//    private URL url;
//
//    public Tip() {
//
//    }
//
//    public Tip(String title, String username, String filter, String content, List comments, URL url) {
//        this.title = title;
//        this.username = username;
//        this.filter = filter;
//        this.content = content;
//        this.comments = comments;
//        this.url = url;
//    }
//
//    protected Tip(Parcel in) {
//        title = in.readString();
//        username = in.readString();
//        filter = in.readString();
//        content = in.readString();
////        comments = in.readArrayList();
////        url = in.r
//
//    }
//    public static final Creator<Tip> CREATOR = new Creator<Tip>() {
//        @Override
//        public Tip createFromParcel(Parcel in) {
//            return new Tip(in);
//        }
//
//        @Override
//        public Tip[] newArray(int size) {
//            return new Tip[size];
//        }
//    };
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getFilter() {
//        return filter;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public List getComments() {
//        return comments;
//    }
//
//    public URL getUrl() {
//        return url;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel parcel, int i) {
//        parcel.writeString(title);
//        parcel.writeString(username);
//        parcel.writeString(filter);
//        parcel.writeString(content);
//    }
//}

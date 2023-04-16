package edu.northeastern.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * The tip class.
 */

public class Tip implements Parcelable {
    private String tipId;
    private String userId;
    private String userName;
    private String title;
    private String pictureUrl;
    private String content;
    private String filter;
    private String userAvatarUrl;

    public Tip() {
    }

    public Tip(String tipId, String userId, String userName, String title, String pictureUrl, String content, String filter, String userAvatarUrl) {
        this.tipId = tipId;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.content = content;
        this.filter = filter;
        this.userAvatarUrl = userAvatarUrl;
    }

    protected Tip(Parcel in) {
        tipId = in.readString();
        userId = in.readString();
        userName = in.readString();
        title = in.readString();
        pictureUrl = in.readString();
        content = in.readString();
        filter = in.readString();
        userAvatarUrl = in.readString();
    }

    public static final Creator<Tip> CREATOR = new Creator<Tip>() {
        @Override
        public Tip createFromParcel(Parcel in) {
            return new Tip(in);
        }

        @Override
        public Tip[] newArray(int size) {
            return new Tip[size];
        }
    };

    public String getTipId() {
        return tipId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getContent() {
        return content;
    }

    public String getFilter() {
        return filter;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tipId);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(title);
        dest.writeString(pictureUrl);
        dest.writeString(content);
        dest.writeString(filter);
        dest.writeString(userAvatarUrl);

    }

    @Override
    public String toString() {
        return "Tip{" +
                "tipId='" + tipId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", content='" + content + '\'' +
                ", filter='" + filter + '\'' +
                ", userAvatarUrl='" + userAvatarUrl + '\'' +
                '}';
    }
}

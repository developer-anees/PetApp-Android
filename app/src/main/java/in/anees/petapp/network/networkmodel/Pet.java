package in.anees.petapp.network.networkmodel;

import com.google.gson.annotations.SerializedName;

public class Pet {
    @SerializedName("content_url")
    private String mContentUrl;
    @SerializedName("date_added")
    private String mDateAdded;
    @SerializedName("image_url")
    private String mImageUrl;
    @SerializedName("title")
    private String mTitle;

    public String getContentUrl() {
        return mContentUrl;
    }

    public void setContentUrl(String contentUrl) {
        mContentUrl = contentUrl;
    }

    public String getDateAdded() {
        return mDateAdded;
    }

    public void setDateAdded(String dateAdded) {
        mDateAdded = dateAdded;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}

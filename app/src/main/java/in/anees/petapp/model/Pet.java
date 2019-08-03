
package in.anees.petapp.model;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class Pet {
    private String mContentUrl;
    private String mDateAdded;
    private String mImageUrl;
    private String mTitle;

    public Pet(String mContentUrl, String mDateAdded, String mImageUrl, String mTitle) {
        this.mContentUrl = mContentUrl;
        this.mDateAdded = mDateAdded;
        this.mImageUrl = mImageUrl;
        this.mTitle = mTitle;
    }

    public String getContentUrl() {
        return mContentUrl;
    }

    public Pet setContentUrl(String contentUrl) {
        mContentUrl = contentUrl;
        return this;
    }

    public String getDateAdded() {
        return mDateAdded;
    }

    public Pet setDateAdded(String dateAdded) {
        mDateAdded = dateAdded;
        return this;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public Pet setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public Pet setTitle(String title) {
        mTitle = title;
        return this;
    }
}

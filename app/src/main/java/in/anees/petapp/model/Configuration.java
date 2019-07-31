package in.anees.petapp.model;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class Configuration {
    private boolean mIsChatEnabled;
    private boolean mIsCallEnabled;
    private String mWorkHours;
    private WorkingTime mWorkingTime;

    public boolean isIsChatEnabled() {
        return mIsChatEnabled;
    }

    public Configuration setIsChatEnabled(boolean mIsChatEnabled) {
        this.mIsChatEnabled = mIsChatEnabled;
        return this;
    }

    public boolean isIsCallEnabled() {
        return mIsCallEnabled;
    }

    public Configuration setIsCallEnabled(boolean mIsCallEnabled) {
        this.mIsCallEnabled = mIsCallEnabled;
        return this;
    }

    public String getWorkHours() {
        return mWorkHours;
    }

    public Configuration setWorkHours(String mWorkHours) {
        this.mWorkHours = mWorkHours;
        return this;
    }

    public WorkingTime getWorkingTime() {
        return mWorkingTime;
    }

    public Configuration setWorkingTime(WorkingTime mWorkingTime) {
        this.mWorkingTime = mWorkingTime;
        return this;
    }
}

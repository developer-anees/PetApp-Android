package in.anees.petapp.network.networkmodel;

import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("isCallEnabled")
    private Boolean mIsCallEnabled;
    @SerializedName("isChatEnabled")
    private Boolean mIsChatEnabled;
    @SerializedName("workHours")
    private String mWorkHours;

    public Boolean getIsCallEnabled() {
        return mIsCallEnabled;
    }

    public void setIsCallEnabled(Boolean isCallEnabled) {
        mIsCallEnabled = isCallEnabled;
    }

    public Boolean getIsChatEnabled() {
        return mIsChatEnabled;
    }

    public void setIsChatEnabled(Boolean isChatEnabled) {
        mIsChatEnabled = isChatEnabled;
    }

    public String getWorkHours() {
        return mWorkHours;
    }

    public void setWorkHours(String workHours) {
        mWorkHours = workHours;
    }

}

package in.anees.petapp.network.networkmodel;

import com.google.gson.annotations.SerializedName;

public class ConfigurationSchema {
    @SerializedName("settings")
    private Settings mSettings;

    public Settings getSettings() {
        return mSettings;
    }

    public void setSettings(Settings settings) {
        mSettings = settings;
    }
}

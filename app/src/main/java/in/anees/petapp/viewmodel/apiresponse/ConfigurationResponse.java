package in.anees.petapp.viewmodel.apiresponse;

import androidx.annotation.Nullable;

import in.anees.petapp.data.model.Configuration;

/**
 * Created by Anees Thyrantakath on 2019-08-14.
 */
public class ConfigurationResponse extends BaseResponse<Configuration> {

    public ConfigurationResponse(State state, @Nullable Configuration configuration, @Nullable String errorMessage) {
        this.currentState = state;
        this.data = configuration;
        this.error = errorMessage;
    }

    public static ConfigurationResponse ERROR_STATE = new ConfigurationResponse( State.FAILED, null, "");
    public static ConfigurationResponse LOADING_STATE = new ConfigurationResponse( State.LOADING, null, null);
    public static ConfigurationResponse SUCCESS_STATE = new ConfigurationResponse( State.SUCCESS, new Configuration(), null);
}

package in.anees.petapp.viewmodel.apiresponse;

import in.anees.petapp.data.model.Configuration;

/**
 * Created by Anees Thyrantakath on 2019-08-14.
 */
public class ConfigurationResponse {
    private Configuration configuration;
    private String message;

    public ConfigurationResponse(Configuration configuration) {
        this.configuration = configuration;
        this.message = null;
    }

    public ConfigurationResponse(String error) {
        this.message = error;
        this.configuration = null;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

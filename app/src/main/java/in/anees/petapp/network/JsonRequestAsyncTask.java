package in.anees.petapp.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.anees.petapp.constant.Constants;
import in.anees.petapp.model.Configuration;
import in.anees.petapp.model.Pet;
import in.anees.petapp.model.PetDetails;
import in.anees.petapp.model.WorkingTime;
import in.anees.petapp.ui.fragment.PetListFragment;
import in.anees.petapp.ui.listeners.NetworkRequestResponseListener;
import in.anees.petapp.utils.DateUtils;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class JsonRequestAsyncTask extends AsyncTask<String, String, Object> {

    private NetworkRequestResponseListener mListener;
    private final String  TAG = "JsonRequestAsyncTask";

    public JsonRequestAsyncTask(PetListFragment context) {
            this.mListener = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Object doInBackground(String... params) {

            Object response = null;
            HttpURLConnection urlConnection;
            try {
                URL uri = new URL(params[0]);
                urlConnection = (HttpURLConnection) uri.openConnection();
                urlConnection.setConnectTimeout(30000);
                urlConnection.setReadTimeout(30000);
                int statusCode = urlConnection.getResponseCode();
                // Presently we consider on 200 as success response
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Received statusCode : " + statusCode);
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                response = convertStringToObject(inputStream, params[0]);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            // Better to check whether its already cancelled or not
            if (!isCancelled()) {
                if (result != null) {
                    mListener.onResponseSuccess(result);
                } else {
                    mListener.onResponseFailure(null);
                }
            }
        }

        private Object convertStringToObject(InputStream is, String requestUrl) throws IOException,
                JSONException, ParseException {
            String response = convertStreamToString(is);

            JSONObject jsonObject = new JSONObject(response);
            switch (requestUrl) {
                case Constants.PET_LIST_URL:
                    return getPetDetailsFromJson(jsonObject);
                case Constants.CONFIG_URL:
                    return getConfigurationFromJson(jsonObject);
                default:
                    Log.w(TAG, "Unexpected request : " + requestUrl);
                    return null;
            }
        }


    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new IOException("Error while converting to String :" + e.getMessage());
        } finally {
            is.close();
        }

        return sb.toString();
    }

    private Object getConfigurationFromJson(JSONObject jsonObject) throws JSONException, ParseException {
            // Get the configuration details from JSON
            Configuration configuration = new Configuration();

            JSONObject settings = jsonObject.getJSONObject("settings");
            boolean isChatEnabled = settings.getBoolean("isChatEnabled");
            boolean isCallEnabled = settings.getBoolean("isCallEnabled");
            String workHours = settings.getString("workHours");

            WorkingTime workingTime = DateUtils.getWorkingTimings(workHours);

            configuration.setIsCallEnabled(isCallEnabled).setIsChatEnabled(isChatEnabled)
                    .setWorkHours(workHours).setWorkingTime(workingTime);

            return configuration;
        }

        private Object getPetDetailsFromJson(JSONObject jsonObject) throws JSONException {
            // Get Pet details from JSON
            PetDetails petDetails = new PetDetails();
            List<Pet> petList = new ArrayList<>();

            JSONArray petsArray = jsonObject.getJSONArray("pets");
            int lengthOfPetsArray = petsArray.length();

            for (int i = 0; i< lengthOfPetsArray; i++) {
                JSONObject pet = petsArray.getJSONObject(i);
                String image_url = pet.getString("image_url");
                String title = pet.getString("title");
                String content_url = pet.getString("content_url");
                String date_added = pet.getString("date_added");

                Pet pets = new Pet();
                pets.setImageUrl(image_url).setTitle(title).setContentUrl(content_url).setDateAdded(date_added);
                petList.add(pets);
            }
            petDetails.setPets(petList);
            return petDetails;
        }
}

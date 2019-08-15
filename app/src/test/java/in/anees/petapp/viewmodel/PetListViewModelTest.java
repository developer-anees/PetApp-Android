package in.anees.petapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.anees.petapp.data.model.Configuration;
import in.anees.petapp.data.model.Pet;
import in.anees.petapp.data.network.networkmodel.ConfigurationSchema;
import in.anees.petapp.data.network.networkmodel.PetsSchema;
import in.anees.petapp.utils.DateUtils;
import in.anees.petapp.viewmodel.apiresponse.ConfigurationResponse;
import in.anees.petapp.viewmodel.apiresponse.PetListResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Anees Thyrantakath on 2019-08-15.
 */
public class PetListViewModelTest {
    private final String ERROR_MSG_PET_LIST = "Error while Fetching PetList";
    private final String ERROR_MSG_CONFIGURATION = "Error while Fetching Configuration";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Application mApplication;

    @Mock
    Observer<PetListResponse> mObserverPetList;

    @Mock
    Observer<ConfigurationResponse> mObserverConfiguration;

    @Mock
    LifecycleOwner mLifecycleOwner;
    private Lifecycle mLifecycle;

    private MockPetListViewModel mPetListViewModel;


    @Before
    public void setUp() throws Exception {
        mLifecycle = new LifecycleRegistry(mLifecycleOwner);
        ((LifecycleRegistry) mLifecycle).handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        mPetListViewModel = new MockPetListViewModel(mApplication);

        mPetListViewModel.getPets().observeForever(mObserverPetList);
        mPetListViewModel.getConfiguration().observeForever(mObserverConfiguration);
    }

    @Test
    public void testPetsNull() {
        mPetListViewModel.getPets();
        assertNotNull(mPetListViewModel.getPets());
        assertTrue(mPetListViewModel.getPets().hasObservers());
    }

    @Test
    public void testFetchPetListSuccess() {
        mPetListViewModel.petsSuccess = true;
        mPetListViewModel.getPets();

        verify(mObserverPetList).onChanged(PetListResponse.LOADING_STATE);
        verify(mObserverPetList).onChanged(PetListResponse.SUCCESS_STATE);
    }

    @Test
    public void testFetchPetListError() {
        mPetListViewModel.petsFailed = true;
        mPetListViewModel.getPets();
        verify(mObserverPetList).onChanged(PetListResponse.LOADING_STATE);
        verify(mObserverPetList).onChanged(PetListResponse.ERROR_STATE);
    }

    @Test
    public void testConfigurationNull() {
        mPetListViewModel.getConfiguration();
        assertNotNull(mPetListViewModel.getConfiguration());
        assertTrue(mPetListViewModel.getConfiguration().hasObservers());
    }

    @Test
    public void testFetchConfigurationSuccess() {
        mPetListViewModel.configurationSuccess = true;
        mPetListViewModel.getConfiguration();
        verify(mObserverConfiguration).onChanged(ConfigurationResponse.LOADING_STATE);
        verify(mObserverConfiguration).onChanged(ConfigurationResponse.SUCCESS_STATE);
    }

    @Test
    public void testFetchConfigurationError() {
        mPetListViewModel.configurationFailed = true;
        mPetListViewModel.getConfiguration();
        verify(mObserverConfiguration).onChanged(ConfigurationResponse.LOADING_STATE);
        verify(mObserverConfiguration).onChanged(ConfigurationResponse.ERROR_STATE);
    }

    public class MockPetListViewModel extends PetListViewModel {
        private boolean petsSuccess;
        private boolean petsFailed;
        private boolean configurationSuccess;
        private boolean configurationFailed;

        public MockPetListViewModel(@NonNull Application application) {
            super(application);
        }

        @Override
        public LiveData<PetListResponse> getPets() {
            if (mPetList == null) {
                mPetList = new MutableLiveData<PetListResponse>();
                mPetList.postValue(PetListResponse.LOADING_STATE);
            }

            if (petsSuccess) {
                loadSuccessPetListResponse();
            } else if (petsFailed) {
                loadFailedPetListResponse();
            }
            return mPetList;
        }

        @Override
        public LiveData<ConfigurationResponse> getConfiguration() {
            if (mConfiguration == null) {
                mConfiguration = new MutableLiveData<ConfigurationResponse>();
                mConfiguration.postValue(ConfigurationResponse.LOADING_STATE);
            }

            if (configurationSuccess) {
                try {
                    loadConfigurationSuccessResponse();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (configurationFailed) {
                loadConfigurationFailedResponse();
            }

            return mConfiguration;
        }

        private void loadConfigurationFailedResponse() {
            ConfigurationResponse.ERROR_STATE.setError(ERROR_MSG_CONFIGURATION);
            mConfiguration.postValue(ConfigurationResponse.ERROR_STATE);
        }

        private void loadConfigurationSuccessResponse() throws ParseException {
            ConfigurationResponse.SUCCESS_STATE.setData(getLoadedConfiguration());
            mConfiguration.postValue(ConfigurationResponse.SUCCESS_STATE);
        }


        private void loadFailedPetListResponse() {
            PetListResponse.SUCCESS_STATE.setError(ERROR_MSG_PET_LIST);
            mPetList.postValue(PetListResponse.ERROR_STATE);
        }

        private void loadSuccessPetListResponse() {
            PetListResponse.SUCCESS_STATE.setData(getPetList());
            mPetList.postValue(PetListResponse.SUCCESS_STATE);
        }

    }

    private List<Pet> getPetList() {
        String result = null;
        try {
            result = readFromFile("/pet_list.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setLenient().create();
        final PetsSchema petsSchema = gson.fromJson(result, PetsSchema.class);

        List<Pet> petList = new ArrayList<>();

        for (in.anees.petapp.data.network.networkmodel.Pet pet : petsSchema.getPets()) {
            petList.add(new in.anees.petapp.data.model.Pet(
                    pet.getContentUrl(),
                    pet.getDateAdded(),
                    pet.getImageUrl(),
                    pet.getTitle()
            ));
        }

        return petList;
    }

    private Configuration getLoadedConfiguration() throws ParseException {
        String result = null;
        try {
            result = readFromFile("/configuration.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setLenient().create();
        final ConfigurationSchema configurationSchema = gson.fromJson(result, ConfigurationSchema.class);

        return new Configuration(
                configurationSchema.getSettings().getIsChatEnabled(),
                configurationSchema.getSettings().getIsCallEnabled(),
                configurationSchema.getSettings().getWorkHours(),
                DateUtils.getWorkingTimings(configurationSchema.getSettings().getWorkHours())
        );
    }

    private String readFromFile(String filename) throws IOException {
        StringBuffer buffer = null;
        try (InputStream response = getClass().getResourceAsStream(filename)) {
            buffer = new StringBuffer();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, Charset.defaultCharset()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    buffer.append(line);
                }
            }
        }
        return buffer.toString();
    }

}
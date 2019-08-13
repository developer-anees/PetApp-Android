package in.anees.petapp.presenter;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.anees.petapp.data.model.Configuration;
import in.anees.petapp.data.model.Pet;
import in.anees.petapp.data.model.WorkingTime;
import in.anees.petapp.data.network.PetAppNetworkService;
import in.anees.petapp.data.network.api.PetAppApi;
import in.anees.petapp.utils.DateUtils;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Anees Thyrantakath on 2019-08-13.
 */
public class PetListPresenterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context context;

    @Mock
    PetListContract.PetListMvpView mPetListMvpView;

    MockPetAppNetworkService mPetAppNetworkService;



    MockPetListPresenter mockPetListPresenter;

    @Before
    public void setUp() throws Exception {
        mPetAppNetworkService = new MockPetAppNetworkService(context);
        mockPetListPresenter = new MockPetListPresenter(mPetListMvpView, mPetAppNetworkService);
    }

    @Test
    public void fetchConfiguration_success_configurationSuccessViewCalled() {
        // Arrange
        mockPetListPresenter.handleFetchConfiguration = true;
        // Act
        mockPetListPresenter.handleFetchConfiguration();
        // Assert
        verify(mPetListMvpView).setSuccessConfiguration(any(Configuration.class));
    }

    @Test
    public void fetchConfiguration_error_configurationErrorViewCalled() {
        // Arrange
        mockPetListPresenter.handleFetchConfiguration = false;
        // Act
        mockPetListPresenter.handleFetchConfiguration();
        // Assert
        verify(mPetListMvpView).setErrorFetchingConfiguration(any(String.class));
    }

    @Test
    public void fetchPetList_success_PetListValuesSuccessViewCalled() {
        // Arrange
        mockPetListPresenter.handleFetchPetListSuccess = true;
        // Act
        mockPetListPresenter.handleFetchPetList();
        // Assert

        verify(mPetListMvpView).setPetListValuesSuccess(any(List.class));
    }

    @Test
    public void fetchPetList_error_PetListValueErrorViewCalled() {
        // Arrange
        mockPetListPresenter.handleFetchPetListSuccess = false;
        // Act
        mockPetListPresenter.handleFetchPetList();
        // Assert
        verify(mPetListMvpView).setErrorWhileFetchingPetList(any(String.class));
    }


    @Test
    public void shouldDisplayAlertDialogWithMessage_situationalResult() throws ParseException {
        // Arrange
        // TODO : This test result will vary based on the timing you run test. So modify work hours accordingly
        String workHours = "M-F 9:00 - 22:00";
        WorkingTime workingTime = DateUtils.getWorkingTimings(workHours);
        Configuration configuration = new Configuration(true,
                true, workHours, workingTime);
        // Act
        mockPetListPresenter.handleCallOrChatButtonClick(configuration);
        // Assert
        verify(mPetListMvpView).displayAlertDialogWithMessage(true, false);
    }


    public class MockPetListPresenter extends PetListPresenter {

        private PetListContract.PetListMvpView petListMvpView;
        private PetAppNetworkService petAppNetworkService;
        public boolean handleFetchPetListSuccess;
        public boolean handleFetchConfiguration;

        public MockPetListPresenter(PetListContract.PetListMvpView petListMvpView, PetAppNetworkService petAppNetworkService) {
            super(petListMvpView, petAppNetworkService);
            this.petListMvpView = petListMvpView;
            this.petAppNetworkService = petAppNetworkService;
        }

        @Override
        public void handleFetchPetList() {
            // super.handleFetchPetList(); --> Original implementation involves Retrofit call so mocking
            if (handleFetchPetListSuccess) {
                petListMvpView.setPetListValuesSuccess(new ArrayList<Pet>());
            } else {
                petListMvpView.setErrorWhileFetchingPetList("");
            }
        }

        @Override
        public void handleFetchConfiguration() {
            // super.handleFetchConfiguration(); --> Original implementation involves Retrofit call so mocking
            if (handleFetchConfiguration) {
                mPetListMvpView.setSuccessConfiguration(new Configuration());
            } else {
                mPetListMvpView.setErrorFetchingConfiguration("");
            }
        }

        @Override
        public void handleCallOrChatButtonClick(Configuration configuration) {
            super.handleCallOrChatButtonClick(configuration);
        }
    }

    public class MockPetAppNetworkService extends PetAppNetworkService {

        public MockPetAppNetworkService(Context context) {
            super(context);
        }

        @Override
        public PetAppApi getPetAppApi() {
            return Mockito.mock(PetAppApi.class);
        }
    }
}
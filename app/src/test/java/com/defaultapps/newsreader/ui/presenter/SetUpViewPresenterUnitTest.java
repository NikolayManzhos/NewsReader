package com.defaultapps.newsreader.ui.presenter;

import android.util.Log;

import com.defaultapps.newsreader.data.interactor.SetUpViewInteractor;
import com.defaultapps.newsreader.ui.fragment.SetUpViewImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class SetUpViewPresenterUnitTest {

    private SetUpViewPresenterImpl mainViewPresenter;

    private List<String> data1;
    private List<List<String>> data2;

    @Mock
    SetUpViewInteractor setUpViewInteractor;

    @Mock
    SetUpViewImpl mainView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);
        mainViewPresenter = new SetUpViewPresenterImpl(setUpViewInteractor);
        mainViewPresenter.setView(mainView);
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();

    }
    @Test
    public void requestUpdateTest() throws Exception {
        mainViewPresenter.requestSourceUpdate("en");

        verify(mainView).showLoading();
        verify(mainView).hideSourcesList();
        verify(mainView).hideError();
    }


    @Test
    public void onFailureTest() throws Exception {
        mainViewPresenter.onFailure();

        verify(mainView).hideLoading();
        verify(mainView).hideSourcesList();
        verify(mainView).showError();
    }

    @Test
    public void onSuccessTest() throws Exception {
        mainViewPresenter.onSuccess(data1, data1, data1, data1, data2);

        verify(mainView).hideLoading();
        verify(mainView).hideError();
        verify(mainView).showSourcesList();
        verify(mainView).updateView(data1, data1, data1, data1, data2);
    }

    @Test
    public void detachViewTest() throws Exception {
        mainViewPresenter.detachView();
    }

    /**
     * Testing config changes behavior.
     * In this test case data is still loading, so when config appears progressBar will be shown.
     */
    @Test
    public void restoreViewStateTestLoading() throws Exception {
        mainViewPresenter.setTaskStatus(true);

        mainViewPresenter.restoreViewState();

        verify(mainView).showLoading();
        verify(mainView).hideSourcesList();
        verify(mainView).hideError();
    }

    /**
     * Testing config changes behavior.
     * In this test case data is failed to load and error screen displayed.
     */
    @Test
    public void restoreViewStateTestError() throws Exception {
        mainViewPresenter.setTaskStatus(false);
        mainViewPresenter.setErrorVisibilityStatus(true);
        mainViewPresenter.restoreViewState();

        verify(mainView).showError();
        verify(mainView).hideLoading();
        verify(mainView).hideSourcesList();
    }

}
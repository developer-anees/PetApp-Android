package in.anees.petapp.ui.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import in.anees.petapp.R;
import in.anees.petapp.controller.PetListController;
import in.anees.petapp.utils.CommonAlert;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 * TODO: Not checking Network connection now because we check it before launching this fragment.
 * There might be scenarios we may need to load the page again, Or the user want to check
 * sub-links in the present page. If requirement comes then will implement it later.
 */
public class PetDetailsFragment extends BaseFragment {

    public static final String TAG_PET_DETAILS = "PET_DETAILS";
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "url";

    private String mTitle;
    private String mPetDetailsUrl;
    private WebView mWebView;
    private ProgressBar mProgressBar;


    public PetDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param url Parameter 2.
     * @return A new instance of fragment PetDetailsFragment.
     */
    public static PetDetailsFragment newInstance(String title, String url) {
        PetDetailsFragment fragment = new PetDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mPetDetailsUrl = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mWebView = (WebView) view.findViewById(R.id.webViewPetDetails);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (mPetDetailsUrl != null) {
            mWebView.loadUrl(mPetDetailsUrl);
        }

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP
                        && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (errorCode==-2){
                CommonAlert.showToast(mContext,"Check internet connection!");
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}

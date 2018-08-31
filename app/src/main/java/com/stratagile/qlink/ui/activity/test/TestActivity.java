package com.stratagile.qlink.ui.activity.test;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.test.component.DaggerTestComponent;
import com.stratagile.qlink.ui.activity.test.contract.TestContract;
import com.stratagile.qlink.ui.activity.test.module.TestModule;
import com.stratagile.qlink.ui.activity.test.presenter.TestPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.test
 * @Description: $description
 * @date 2018/08/31 17:23:42
 */

public class TestActivity extends BaseActivity implements TestContract.View {

    @Inject
    TestPresenter mPresenter;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.bt_2)
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultFontSize(16);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.addJavascriptInterface(new JavaScriptinterface(this, webView), "wif");
        webView.loadUrl("file:///android_asset/demo.html");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
        DaggerTestComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .testModule(new TestModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(TestContract.TestContractPresenter presenter) {
        mPresenter = (TestPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.bt_2)
    public void onViewClicked() {
        webView.post(new Runnable() {
            @Override
            public void run() {

                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                String string = "content";
                String wif = "";
                String call = "demo:alertSign(\"" + string + "\", + \"" + wif + "\")";
                webView.loadUrl(call);
            }
        });
    }

//    public class JavaScriptinterface {
//        Context context;
//
//        public JavaScriptinterface(Context c, WebView mWebView) {
//            context = c;
//        }
//
//        /**
//         * 与js交互时用到的方法，在js里直接调用的(window.XJYJS.getMsg(str))
//         */
//        @JavascriptInterface
//        public void getMsg() {
//
//            String string = "content";
//            String wif = "";
//            call = "javascript:alertSign(\"" + string + "\", + \"" + wif + "\")";
//            webView.post(new Runnable() {
//                @Override
//                public void run() {
//
//                    webView.loadUrl(call);
//                }
//            });
//        }
//
//    }
}
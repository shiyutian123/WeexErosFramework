package com.benmu.framework.extend.comoponents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.benmu.framework.utils.AssetsUtil;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.ui.view.WXWebView;
import com.taobao.weex.utils.WXUtils;

/**
 * Created by Carry on 2017/11/16.
 */

public class BMChart extends WXComponent implements IWebView.OnPageListener {

    private float mWidth;
    private float mHeight;
    private WXWebView mWebView;
    private WebView mWeb;
    private String mChartJs;
    private String mCharInfo;

    public BMChart(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, int type) {
        super(instance, dom, parent, type);
        init();
    }

    public BMChart(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, String
            instanceId, boolean isLazy) {
        super(instance, dom, parent, instanceId, isLazy);
        init();
    }

    public BMChart(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
        super(instance, dom, parent, isLazy);
        init();
    }

    public BMChart(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
        init();
    }

    private void init() {
        mWebView = new WXWebView(getContext());
        mChartJs = AssetsUtil.getFromAssets(getContext(), "echarts.min.js");
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        View view = mWebView.getView();
        mWeb = mWebView.getWebView();
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setOnPageListener(this);
        mWeb.loadUrl("file:///android_asset/bm-chart.html");
        mWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }
        });
        return view;
    }


    @Override
    protected void onFinishLayout() {
        super.onFinishLayout();
        WXStyle styles = getDomObject().getStyles();
        if (styles != null) {
            Object width = styles.get(Constants.Name.WIDTH);
            if (width != null) {
                mWidth = WXUtils.getFloat(width);
            }

            Object height = styles.get(Constants.Name.HEIGHT);
            if (height != null) {
                mHeight = WXUtils.getFloat(height);
            }

        }
    }

    @Override
    public void onReceivedTitle(String title) {

    }

    @Override
    public void onPageStart(String url) {

    }

    @WXComponentProp(name = "chartInfo")
    public void setChartInfo(String info) {
        this.mCharInfo = info;

    }


    @Override
    public void onPageFinish(String url, boolean canGoBack, boolean canGoForward) {

            mWeb.evaluateJavascript(mChartJs, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    mWeb.loadUrl("javascript:setHeight(" + mHeight + ")");
                    mWeb.loadUrl("javascript:setOption(" + mCharInfo + ")");
                    fireEvent("finish");
                }
            });

    }
}

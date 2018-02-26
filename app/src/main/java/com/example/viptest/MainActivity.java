package com.example.viptest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String current_web;
    private TextView mTextView;
    private TextView realurl;
    private WebView webView;
    private View customView;
    private myWebClient client ;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS
            = new FrameLayout.LayoutParams
            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private String[] path = {
            "http://yun.zihu.tv/yunparse/?url=",
            "http://www.wmxz.wang/video.php?url=",
            "http://v.72du.com/api/?url=",
            "http://api.47ks.com/webcloud/?v=",
            "http://api.baiyug.cn/vip/index.php?url=",
            "http://api.pucms.com/?url=",
            "http://api.91exp.com/svip/?url=",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.web_view);
        mTextView = findViewById(R.id.url_view);
        realurl = findViewById(R.id.realurl_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//不加载缓存内容
        webView.getSettings().setSupportZoom(true); // 支持缩放
        //不跳转流浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //显示网站
               	Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //自定义客户端
        client=new myWebClient();
        webView.setWebChromeClient(client);

    }

    class myWebClient extends WebChromeClient {

        /*** 视频播放相关的方法 **/

        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(MainActivity.this);
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(p);
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            showCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }

        /** 视频播放全屏 **/
        private void showCustomView(View view, CustomViewCallback callback) {
            // if a view already exists then immediately terminate the new one
            if (customView != null) {
                callback.onCustomViewHidden();
                return;
            }

            MainActivity.this.getWindow().getDecorView();

            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            fullscreenContainer = new FullscreenHolder(MainActivity.this);
            fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
            customView = view;
            setStatusBarVisibility(false);
            customViewCallback = callback;
            MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        /** 隐藏视频全屏 */
        private void hideCustomView() {
            if (customView == null) {
                return;
            }

            setStatusBarVisibility(true);
            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            decor.removeView(fullscreenContainer);
            fullscreenContainer = null;
            customView = null;
            customViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
            MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        /** 全屏容器界面 */
        class FullscreenHolder extends FrameLayout {

            public FullscreenHolder(Context ctx) {
                super(ctx);
                setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            }
        }

        private void setStatusBarVisibility(boolean visible) {
            int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                current_web = "";
                mTextView.setText(null);
                realurl.setText(null);
                return true;
            }
            else
            {
                finish();
                current_web = "";
                mTextView.setText(null);
                realurl.setText(null);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void OpenYouku(View v){
        webView.loadUrl("http://www.youku.com/");
        current_web = "";
        mTextView.setText(null);
        realurl.setText(null);
    }

    public void OpenAiqiyi(View v){
        webView.loadUrl("http://www.iqiyi.com/");
        current_web = "";
        mTextView.setText(null);
        realurl.setText(null);
    }

    public void play(View v){
        if(current_web == ""){
            current_web = webView.getUrl();
            String parseString = path[path_index] + current_web ;
            webView.loadUrl(parseString);
        }else{
            Toast.makeText(MainActivity.this, "点击change换线尝试", Toast.LENGTH_LONG).show();
        }
    }


    private int path_index = 0;
    public void change(View v){
//        String url = webView.getUrl();
//        mTextView.setText(url);
//        url = url.substring(url.indexOf("=") + 1);
        if (path_index == (path.length -1 ) ) {
            mTextView.setText("线路："+(path_index+1));
            path_index=0;
        }else{
            mTextView.setText("线路："+(path_index+1));
            path_index+=1;
        }
        webView.loadUrl(path[path_index] + current_web);
        realurl.setText(path[path_index] + current_web);
//        Toast.makeText(MainActivity.this, path[path_index] + current_web, Toast.LENGTH_LONG).show();
    }
}

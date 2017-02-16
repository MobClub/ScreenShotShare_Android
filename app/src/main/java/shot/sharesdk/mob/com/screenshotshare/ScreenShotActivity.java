package shot.sharesdk.mob.com.screenshotshare;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.sharesdk.common.mobscreenshotcommon.ScreenShotListenManager;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import shot.sharesdk.mob.com.screenshotshare.ui.ThumbnailLayout;

public class ScreenShotActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ScreenShotActivity";
    String imageUri = "";

    ScreenShotListenManager manager;
    private ImageView iconImg;
    private Bitmap bitmap;
    private ContentLoadingProgressBar progress;
    private TextView shareImage;
    private WebView webView;
    private ThumbnailLayout thumbLayout;
    private BitmapFactory.Options options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        initData();
        initView();
        initScreenShotListener();
    }

    private void initScreenShotListener(){
        manager = ScreenShotListenManager.newInstance(this);
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        progress.setVisibility(View.VISIBLE);
                        setImageView(imagePath);
                    }
                }
        );
    }
    private void setImageView(final String file){
        imageUri = file;
        if(thumbLayout != null && (thumbLayout.getVisibility() == View.GONE)){
            thumbLayout.setVisibility(View.VISIBLE);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(file,options);
                if(bitmap != null){
                     iconImg.setImageBitmap(bitmap);
                     progress.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager.startListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.stopListen();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        ShareSDK.initSDK(this,"1b45a03014ef7");
    }

    /**
     * 初始化组件
     */
    private void initView(){
        thumbLayout = (ThumbnailLayout)findViewById(R.id.thumbLayout);
        thumbLayout.setAutoHide(true);//设置自动消失。
        iconImg = (ImageView)findViewById(R.id.thumb);
        progress = (ContentLoadingProgressBar)findViewById(R.id.startShow);
        progress.setVisibility(View.GONE);
        shareImage = (TextView)findViewById(R.id.shareFriends);
        shareImage.setOnClickListener(this);
        webView = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true); // 充满全屏幕
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webView.setHorizontalScrollBarEnabled(false); // 水平不显示滚动条
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER); // 禁止即在网页顶出现一个空白，又自动回去。
        webView.setWebChromeClient(new webChromClient());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                handler.proceed(); // 接受网站证书
            }
        });
        webView.setWebChromeClient(new webChromClient());
        webView.loadUrl("http://www.mob.com");
    }
    private class webChromClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
            super.onProgressChanged(view, newProgress);
        }
    }

    private void OneKeyShare(String imageUrl){
        if(!TextUtils.isEmpty(imageUrl)){
            OnekeyShare oks = new OnekeyShare();
            oks.setTitle(getString(R.string.app_name));
            oks.setText("截屏分享");
            oks.setImagePath(imageUrl);
            oks.show(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.shareFriends:
                OneKeyShare(imageUri);
        }
    }
}

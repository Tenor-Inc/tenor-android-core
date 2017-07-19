Tenor Android Core Documentation
================================
This documentation is for `tenor-android-core` module.

* [Include `tenor-android-core` in your project](include-tenor-android-core-in-your-project)
* [Initialize Tenor Service](#initialize-tenor-service)
  * [Customized User-Agent](#customized-user-agent)
  * [Customized Interceptor](#customized-interceptor)
* [Use Tenor Service](#use-tenor-service)
* [File Provider](#file-provider)

## Include `tenor-android-core` in your project
Copy `tenor-android-core.aar` into your app's `libs` folder, then add the following setup to your app's `build.gradle` file:
```java
repositories {
    flatDir{
        dirs 'libs'
    }
}

dependencies {
  compile(name: 'tenor-android-core', ext: 'aar') {
      transitive = true
  }
  compile 'com.android.support:recyclerview-v7:25.3.1'
  compile 'com.squareup.retrofit2:converter-gson:2.3.0'
  compile 'com.github.bumptech.glide:glide:3.8.0'
}
```



## Initialize Tenor Service
On the `onCreate()` of your `Application` class, add:
```java
if (BuildConfig.DEBUG) {
    ApiClient.setProtocolType(ApiClient.HTTP);
    ApiClient.setServer("qa-api");
}

// put your tenor API key here
ApiClient.setApiKey("");

// initialize ApiClient and request keyboard id
ApiClient.init(this);
```



### Customized User-Agent
If you want to customize the User-Agent of all your API requests, you need to supply an `Interceptor` when initializing the `ApiClient`:
```java
/*
 * initialize custom user-agent for all requests
 * by using CustomUserAgent and UserAgentInterceptor
 */
AbstractNetworkUtils.initUserAgent(
        new CustomUserAgent.Builder(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
                .locale(AbstractLocaleUtils.getCurrentLocaleName(this))
                .build()
);
final Interceptor interceptor = new UserAgentInterceptor(this, AbstractNetworkUtils.getUserAgent(this));

// request keyboard id with custom user-agent interceptor
ApiClient.init(this, interceptor);
```



### Customized Interceptor
You can also construct and supply your own `Interceptor` when initializing the `ApiClient`:
```java
final Interceptor interceptor = new Interceptor() {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
};

// request keyboard id with custom interceptor
ApiClient.init(this, interceptor);
```



## Use Tenor Service
Assuming your project follows Model View Presenter (MVP) pattern, it is recommended to create a presenter to perform the API calls.  Some example implementation would be shown as following:

`ITenorGifView` and `ITenorGifPresenter` are the interfaces communicating between your presenter and view/activity.
```java
public interface ITenorGifView extends IBaseView {
    void onReceiveSearchResultsSucceed(GifsResponse response, boolean isAppend);

    void onReceiveSearchResultsFailed(BaseError error);

    void onReceiveTrendingSucceeded(List<Result> list, String nextPageId, boolean isAppend);

    void onReceiveTrendingFailed(BaseError error);
}
```

```java
public interface ITenorGifPresenter extends IBasePresenter<ITenorGifView> {
    Call<GifsResponse> search(String query, int limit, String pos, boolean isAppend);

    Call<GifsResponse> getTrending(int limit, String pos, boolean isAppend);
}
```

`TenorGifPresenter` is the implementation of the presenter, where API calls are made.
```java
public class TenorGifPresenter extends BasePresenter<ITenorGifView> implements ITenorGifPresenter {

    public GifPresenter(IKeyboardView view) {
        super(view);
    }

    @Override
    public Call<GifsResponse> search(String query, int limit, String pos, final boolean isAppend) {

        final String qry = !TextUtils.isEmpty(query) ? query : StringConstant.EMPTY;

        Call<GifsResponse> call = ApiClient.getInstance(getView().getContext()).search(ApiClient.getApiKey(),
                qry, AbstractLocaleUtils.getCurrentLocaleName(getView().getContext()),
                AbstractSessionUtils.getKeyboardId(getView().getContext()), limit, pos);

        call.enqueue(new WeakRefCallback<GifsResponse, IKeyboardView>(getWeakRef()) {
            @Override
            public void success(@NonNull IKeyboardView view, GifsResponse response) {
                view.onReceiveSearchResultsSucceed(response, isAppend);
            }

            @Override
            public void failure(@NonNull IKeyboardView view, BaseError error) {
                view.onReceiveSearchResultsFailed(error);
            }
        });
        return call;
    }

    @Override
    public Call<GifsResponse> getTrending(int limit, String pos, final boolean isAppend) {
        Call<GifsResponse> call = ApiClient.getInstance(getView().getContext()).getTrending(ApiClient.getApiKey(),
                limit, !TextUtils.isEmpty(pos) ? pos : StringConstant.EMPTY,
                AbstractLocaleUtils.getCurrentLocaleName(getView().getContext()),
                AbstractSessionUtils.getKeyboardId(getView().getContext()));

        call.enqueue(new WeakRefCallback<GifsResponse, IKeyboardView>(getWeakRef()) {
            @Override
            public void success(@NonNull IKeyboardView view, GifsResponse response) {
                if (response == null || (!isAppend && AbstractListUtils.isEmpty(response.getResults()))) {
                    // TODO: getView().onReceiveTrendingFailed();
                    return;
                }
                view.onReceiveTrendingSucceeded(response.getResults(), response.getNext(), isAppend);
            }

            @Override
            public void failure(@NonNull IKeyboardView view, BaseError error) {
                view.onReceiveTrendingFailed(error);
            }
        });
        return call;
    }
}
```

`MainActivity` is your activity, where you want to use the presenter.
```java
public class MainActivity extends Activity implements ITenorGifView {

    private ITenorGifPresenter mPresenter;
    // initialize the next page id for pagination
    private String mNextPageId = StringConstant.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the presenter
        mPresenter = new TenorGifPresenter(this);

        // perform the gif search
        mPresenter.getTrending(24, mNextPageId, isAppend);

        /*
         * Your other works...
         */

         // reset pagination
         mNextPageId = StringConstant.EMPTY;
    }

    @Override
    public void onReceiveSearchResultsSucceed(GifsResponse response, boolean isAppend) {
      mNextPageId = response.getNext();
      // handle the result gifs
    }

    @Override
    public void onReceiveSearchResultsFailed(BaseError error) {}

    @Override
    public void onReceiveTrendingSucceeded(List<Result> list, String nextPageId, boolean isAppend) {
      mNextPageId = nextPageId;
      // handle the result gifs
    }

    @Override
    public void onReceiveTrendingFailed(BaseError error) {}
}
```


## Commit Gif using RCS
You can use `AbstractKeyboardUtils.commitGif();` methods to commit a gif or other supported `@ContentFormat`; the `ContentFormats.java` listed the supported formats.

```java
public static void commitGif(@NonNull final InputMethodService inputMethodService,
                             @NonNull final Uri uri);

public static void commitContent(@NonNull final InputMethodService inputMethodService,
                                 @NonNull final Uri uri,
                                 @Nullable @ContentFormat final String... mimeTypes)
```



## File Provider
In order to properly access the file on Android 24+, we need to configure and utilize the `FileProvider` provided by the support library.  Copy and paste the following code snippet into your app's manifest file.

```xml
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/tenor_android_core_file_provider_paths"/>
</provider>
```

You should then be able to retrieve a `Uri` of a given `File` class object in a API 24+ compatible manner using `com.tenor.android.core.util.LocalStorageUtils.getUriForFileCompat()`.

Also make sure you have `applicationId` defined on your `build.gradle` file.




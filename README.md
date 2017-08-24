Tenor Android Core
==================

Tenor allows for a fast and easy way to search for and view GIFs inside your Android application.

## Download
### To be replaced with JCenter gradle
If you wish to add the full libraray, copy `tenor-android-core.aar` into your app's `libs` folder.  You will also need to add three additional dependencies used by the core:
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

  compile 'com.squareup.retrofit2:converter-gson:2.3.0'
  compile 'com.github.bumptech.glide:glide:3.8.0'
}
```



## Initialize Tenor Core Service
Create an application class, if you don't already have one.
Then, add the following lines of code to you `onCreate()` function.
You will need an `API_KEY` from Tenor.  For development purposes, you may use our developer key, `"LIVDSRZULELA"`.

However, if you wish to continue using Tenor core services for your application, please request a unique api key [here](https://tenor.com/gifapi#apikey)

```java
@Override
public void onCreate() {
    super.onCreate();

    // Create a builder for ApiService
    ApiService.IBuilder<IApiClient> builder = new ApiService.Builder<>(this, IApiClient.class)

    // add your tenor API key here
    builder.apiKey(API_KEY);

    // initialize the Tenor ApiClient
    ApiClient.init(this, builder);
}
```


## Retrieving GIFs from Tenor
The Tenor API offers robust ways to fetch the right at the right time for your users.
For now, we look at the simplest ways to fetch a stream of GIFs, the `trending` and the `search` API end points.

### Trending GIFs
The `trending` stream fetches whatever the most popular GIFs are at the time of the request, in the order of most popular.
If there is no specific search term selected yet, we reccomend displaying trending `GIFS`.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).getTrending(
                    ApiClient.getServiceIds(getContext()),limit, pos, type);

    call.enqueue(new WeakRefCallback<GifsResponse, IBaseView>(getWeakRef()) {
        @Override
        public void success(IBaseView view, GifsResponse response) {
            // handle success case
        }

        @Override
        public void failure(IBaseView view, BaseError error) {
            // handle failure case
        }
    });
```
ApiClient.getServiceIds(getContext()) will pass in all the fields stored on the ApiClient as a mapping object.
The only additional fields required are `limit` and `pos`:

* `limit` (type `string`) - Fetch up to a specified number of results (max: 50).
* `pos` (type `integer`) - Get results starting at position "value".  Use "" empty string for the initial pos.  


### Searching GIFS by specific search term
The Tenor API's crowing feature is its ever improving search engine, with the presenting the user with the right GIF at the right time.
By adding only an additional `query` field, either as a single word or multiple words, you can display GIFs that capture the reactions your users are searching for.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).search(
                    ApiClient.getServiceIds(getContext()), query, limit, pos);

    call.enqueue(new WeakRefCallback<GifsResponse, IBaseView>(getWeakRef()) {
        @Override
        public void success(IBaseView view, GifsResponse response) {
            // handle success case
        }

        @Override
        public void failure(IBaseView view, BaseError error) {
            // handle failure case
        }
    });
```

To see a detailed look of the GIF response json object, see [here](https://tenor.com/gifapi#responseobjects)

## Displaying GIFs
Once the GIFs have been retrieved, they can now be loaded into and ImageView.
First, use the `AbstractGifUtils` class to fetch the url you wish to display.  For a stream of multiple GIFs being displayed at once,
as well for smaller bundles used for sharing, we reccommend `AbstractGifUtils.getTinyGifUrl(gif_result_object)`.  
Alternatively for full size GIFs, you may use `AbstractGifUtils.getGifUrl(gif_result_object)`.

```java
    call.enqueue(new WeakRefCallback<GifsResponse, IBaseView>(getWeakRef()) {
            @Override
            public void success(IBaseView view, GifsResponse response) {
                Result gif_result_object = response.getResults().get(0);
                String gif_url = AbstractGifUtils.getTinyGifUrl(gif_result_object);
            }
    }
```

Next, we need to construct a `GlidePayload` so that gif can be loaded into the ImageView.  `GlidePayload` makes use of the [glide library](https://github.com/bumptech/glide).
Additionally, you have the option to add a callback for when a GIF has finished loading.
Please note if the ImageView or its parent has `WRAP_CONTENT` for both the height and width, the GIF rendering may not succeed (and neither callback will fire).  When this happens, the ImagView will be interpreted as having both a height and width of 0.
If the GIF fails to render, check that bounds have been given in either the xml or in the java code.
```java
    GlidePayload payload = new GlidePayload(mImageView, gif_url)
                    .setListener(new WeakRefLoadImageListener<>(this) {
                        @Override
                        public void onLoadImageSucceeded(GifSearchItemVH<CTX> ctx, Drawable drawable) {
                            // handle the success case
                        }

                        @Override
                        public void onLoadImageFailed(GifSearchItemVH<CTX> ctx, Drawable drawable) {
                            // handle the failure case
                        }
                    });
```

Finally, once the payload has been constructed, add the following line of code to finish the process:
```java
    AbstractGlideUtils.loadGif(getContext(), payload);
``` 

This should be enough to get you started and display GIFs to your users.
A working demo using search and GIF image loading can be found in the `Demo` folder above.
Full documentation of our API, including ways to further refine user searches with suggestions, can be found [here](https://tenor.com/gifapi)




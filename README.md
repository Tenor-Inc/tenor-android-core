Tenor Android Core
==================
## Introduction
Tenor's API delivers the most relevant GIFs for users anywhere in the world, and the Tenor Android Core provides Android developers with the code necessary to integrate basic Tenor GIF functionality into mobile apps.

## Download
### *__NOTE FOR US: Replace with JCenter gradle once it is setup__*
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
  compile "com.android.support:support-annotations:${build_version_number}"
}
```
Additionally, Tenor Android Core runs on Android version 26.0.1.  Please make sure to add the following lines, if they have not already been added, to the root `build.gradle`
```java
    allprojects {
        repositories {
            maven { url 'https://maven.google.com' }
        }
    }
```



## Initialize Tenor Core Service
Create an application class, if you don't already have one.
Then, add the following lines of code to you `onCreate()` function.
You will need an `API_KEY` from Tenor.  For development purposes, you may use our developer key, `"LIVDSRZULELA"`.

However, if you wish to continue using Tenor core services for your application, please request a unique api key [here](https://tenor.com/gifapi#apikey).

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
The Tenor API offers robust ways to fetch highly shareable GIF for your users.
For now, we will look at the simplest ways to fetch a stream of GIFs, the `trending` and the `search` API end points.

### Trending GIFs
The `trending` stream fetches whatever the most popular GIFs are at the time of the request, in the order of most popular.
If you wish to display GIFs to a user before a specific search term has been selected or entered, we recommend displaying `trending` GIFS.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).getTrending(
                    ApiClient.getServiceIds(getContext()),limit, pos, type);

    call.enqueue(new WeakRefCallback<GifsResponse, Activity>(this) {
        @Override
        public void success(Activity activity, GifsResponse response) {
            // handle success case
        }

        @Override
        public void failure(Activity activity, BaseError error) {
            // handle failure case
        }
    });
```
ApiClient.getServiceIds(getContext()) will pass in all the required fields stored on the ApiClient as a mapped object.
The only additional fields required are `limit` and `pos`:

* `limit` (type `string`) - Fetch up to a specified number of results (max: 50).
* `pos` (type `integer`) - Get results starting at position "value".  Use "" empty string for the initial pos.  


### Searching GIFS by specific search term
Search is where Tenor's API particularly excels. Our understanding of what daily users search, share, upload, favorite, and collect allows us to continually return with precision the most relevant and shareable GIFs.

`search` uses the same fields as `trending`, but with an additional `tag` field that takes a single or multiple words, to return the most precise GIFs possible.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).search(
                    ApiClient.getServiceIds(getContext()), tag, limit, pos);

    call.enqueue(new WeakRefCallback<GifsResponse, Activity>(this) {
        @Override
        public void success(Activity activity, GifsResponse response) {
            // handle success case
        }

        @Override
        public void failure(Activity activity, BaseError error) {
            // handle failure case
        }
    });
```

To see a detailed look of the GIF response json object, click [here](https://tenor.com/gifapi#responseobjects).

## Displaying GIFs
Once the GIFs have been retrieved, they can now be loaded into an ImageView.
First, use the `AbstractGifUtils` class to fetch the url you wish to display.  For a stream of multiple GIFs being displayed at once,
as well as creating smaller bundles used for sharing, we reccommend `AbstractGifUtils.getTinyGifUrl(gif_result_object)`.  
Alternatively, for full size GIFs, you may use `AbstractGifUtils.getGifUrl(gif_result_object)`.

```java
    // using the response variable from the success callback
    Result gif_result_object = response.getResults().get(0);
    String gif_url = AbstractGifUtils.getTinyGifUrl(gif_result_object);
```

Next, we need to construct a `GlidePayload` so that gif can be loaded into the ImageView.  `GlidePayload` makes use of the [glide library](https://github.com/bumptech/glide), which offers full support for loading GIFs inside an ImageView.
Additionally, you have the option to add a callback for when a GIF has finished loading.
```java
    GlideTaskParams<ImageView> params = new GlideTaskParams<>(mImageView, gif_url)
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

Finally, once the payload has been constructed, add the following line of code to finish the process.
Please note, if the ImageView or its parent has `WRAP_CONTENT` for both the height and width, the GIF rendering may not succeed (and neither callback of the `WeakRefLoadImageListener` will fire).  When this happens, the ImagView will be interpreted as having both a height and width of 0.
If the GIF fails to render, check that bounds have been given in either the xml or in the java code.
```java
    GifLoader.loadGif(getContext(), payload);
``` 

This should be enough to get you started and displaying GIFs to your users.
A working demo - which showcases `search`,`trending`, and GIF image loading as well as other Tenor API features like `tags` and `search suggestions` - can be found in the `Demo` folder above.

Full documentation of our API, which details out further ways to refine and bolster the GIF experience, can be found [here](https://tenor.com/gifapi).



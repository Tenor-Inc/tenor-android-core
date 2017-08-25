Tenor Android Core
==================
## Introduction
Tenor's API delivers the most relevant GIFs for users anywhere in the world, and the Tenor Android Core provides Android developers with the code necessary to integrate basic Tenor GIF functionality into mobile apps.

## Download
### *__NOTE FOR US: Replace with JCenter gradle once it is setup__*
If you wish to add the full libraray, copy `tenor-android-core.aar` into your app's `libs` folder:  
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
    }
```
Alternatively, if you are using a module structure in Android Studio by right-clicking the project, `New -> Module -> Import .JAR/.AAR` and selecting `tenor-android-core`, you can add this single line instead:
```java
    dependencies {
        compile project(':tenor-android-core')
    }
```

For any of the import methods used, you will also need to add three additional dependencies used by the core:  
```java
  compile 'com.squareup.retrofit2:converter-gson:2.3.0'
  compile 'com.github.bumptech.glide:glide:3.8.0'
  compile "com.android.support:support-annotations:${build_version_number}"
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
You will need an `API_KEY` from Tenor.  To request an api key, click [here](https://tenor.com/gifapi#apikey).

```java
@Override
public void onCreate() {
    super.onCreate();

    // Create a builder for ApiService
    ApiService.IBuilder<IApiClient> builder = new ApiService.Builder<>(this, IApiClient.class)

    // add your tenor API key here
    builder.apiKey("");

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
To access the `trending` API endpoint, add these lines of code to your application:
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
ApiClient.getServiceIds(getContext()) contains tge necessary info for API authentication and accurate content results.
The only additional fields that need to be supplied are `limit` and `pos`:

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

A full list of available url types may be found in `MediaCollectionFormats.class`.
If you wish to manually access a url type from the `gif_result_object`, you can access them this way as well:
```java
    gif_result_object.getMedias().get(0).get(type).getUrl(); 
```

Next, we need to construct a `GlidePayload` so that gif can be loaded into the ImageView.  `GlidePayload` makes use of the [glide library](https://github.com/bumptech/glide), which offers full support for loading GIFs inside an ImageView.
Additionally, you have the option to add a callback for when a GIF has finished loading.
To access the `search` API endpoint, add these lines of code to your application:
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

Finally, once the payload has been constructed, add the following line of code to finish the process:
```java
    GifLoader.loadGif(getContext(), payload);
``` 

This should be enough to get you started and displaying GIFs to your users.
A working demo - which showcases `search`,`trending`, and GIF image loading as well as other Tenor API features like `tags` and `search suggestions` - can be found in the `Demo` folder above.

Full documentation of our API, which details out further ways to refine and bolster the GIF experience, can be found [here](https://tenor.com/gifapi).

## FAQs

1. Why does my GIF not apear after I call loadGif?
Check what the height and width are set to in the ImageView, and its parent widgets. If the ImageView or its parent has `WRAP_CONTENT` for both the height and width, the GIF rendering may not succeed (and neither callback of the `WeakRefLoadImageListener` will fire).  When this happens, the ImagView will be interpreted as having both a height and width of 0. Check that bounds have been given in either the xml or in the java code. Depending on your view structure, a `MATCH_PARENT` in either the height or width may be suffcient.



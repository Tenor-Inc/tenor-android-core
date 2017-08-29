Tenor Android Core
==================

- [Introduction](#introduction)
- [Setup](#setup)
  * [Option 1: Embed as a Module](#option-1-embed-as-a-module)
  * [Option 2: Embed as a .aar File](#option-2-embed-as-a-aar-file)
  * [Additional Required Dependencies](#additional-required-dependencies)
- [Initialize Tenor Core](#initialize-tenor-core)
- [Retrieving GIFs from Tenor](#retrieving-gifs-from-tenor)
  * [Trending GIFs](#trending-gifs)
  * [Searching GIFs by a Specific Search Term](#searching-gifs-by-a-specific-search-term)
- [Displaying GIFs](#displaying-gifs)
- [FAQs](#faqs)

## Introduction
Tenor's API delivers the most relevant GIFs for users anywhere in the world. 
The Tenor Android Core provides Android developers with the code necessary to integrate essential Tenor GIF functionality into android mobile apps. 

For example, the core provides all the requisite tools for an easy way to **search for GIFs** and to **display GIFs** - either as one large embedded GIF or as a stream of content.

To add Tenor GIF content to your app, please follow the steps below.

## Setup 

### Option 1: Embed as a Module
If you are using a module structure in Android Studio, you can add the Tenor Android Core as a module by right-clicking the project, `New -> Module -> Import .JAR/.AAR` and selecting `tenor-android-core`.  Finally, add this line to your `build.gradle` dependencies:
```java
    dependencies {
        compile project(':tenor-android-core')
    }
```

### Option 2: Embed as a `.aar` File
Alternatively, if you wish to import Tenor Android Core as a `.aar` file, copy `tenor-android-core.aar` into your app's `libs` folder.  Then add the following lines of code to your `build.gradle` file:  
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

### Additional Required Dependencies 
For either of the import options used, you will also need to add three additional dependencies that are used by the core:  
```java
  compile 'com.squareup.retrofit2:converter-gson:2.3.0'
  compile 'com.github.bumptech.glide:glide:3.8.0'
  compile "com.android.support:support-annotations:${build_version_number}"
```


## Initialize Tenor Core
Create an `Application` class, if you don't already have one.
Then, add the following lines of code to the application class' `onCreate()` function.
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
                    ApiClient.getServiceIds(getContext()),limit, pos);

    call.enqueue(new WeakRefCallback<GifsResponse, Activity>(this) {
        @Override
        public void success(@NonNull Activity activity, @Nullable GifsResponse response) {
            // handle success case
        }

        @Override
        public void failure(@NonNull Activity activity, @Nullable BaseError error) {
            // handle failure case
        }
    });
```
`ApiClient.getServiceIds(getContext())` contains the necessary info for API authentication and accurate content results.
The only additional fields that need to be supplied are `limit` and `pos`:

* `limit` (type `string`) - Fetch up to a specified number of results (max: 50).
* `pos` (type `integer`) - Get results starting at position "value".  Use "" empty string for the initial pos.  


### Searching GIFs by a Specific Search Term
Search is where Tenor's API particularly excels. Our understanding of what daily users search, share, upload, favorite, and collect allows us to continually return with precision the most relevant and shareable GIFs.

`search` uses the same fields as `trending`, but with an additional `tag` field that takes a single or multiple words, to return the most precise GIFs possible.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).search(
                    ApiClient.getServiceIds(getContext()), tag, limit, pos);

    call.enqueue(new WeakRefCallback<GifsResponse, Activity>(this) {
        @Override
        public void success(@NonNull Activity activity, @Nullable GifsResponse response) {
            // handle success case
        }

        @Override
        public void failure(@NonNull Activity activity, @Nullable BaseError error) {
            // handle failure case
        }
    });
```

To see a detailed look of the **GIF response json object**, click [here](https://tenor.com/gifapi#responseobjects).

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

Next, we need to construct a `GlideTaskParams` so that gif can be loaded into the ImageView.  `GlideTaskParams` makes use of the [glide library](https://github.com/bumptech/glide), which offers full support for loading GIFs inside an ImageView.
Additionally, you have the option to add a callback for when a GIF has finished loading.
To access the `search` API endpoint, add these lines of code to your application:
```java
    GlideTaskParams<ImageView> params = new GlideTaskParams<>(mImageView, gif_url);
    params.setListener(new WeakRefContentLoaderTaskListener<CTX, ImageView>(getRef()) {
            @Override
            public void success(@NonNull CTX ctx, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                // handle the success case
            }

            @Override
            public void failure(@NonNull CTX ctx, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                // handle the success case
            }
        });
```

Finally, once the `params` has been constructed, add the following line of code to finish the process:
```java
    GifLoader.loadGif(getContext(), params);
``` 

This should be enough to get you started and displaying GIFs to your users.
A working demo - which showcases `search`,`trending`, and GIF image loading as well as other Tenor API features like `tags` and `search suggestions` - can be found in our [Demo repository]().

Full documentation of our API, which details out further ways to refine and bolster the GIF experience, can be found [here](https://tenor.com/gifapi).

## FAQs

1. Why does my GIF not apear after I call loadGif?
    * Check what the height and width are set to in the ImageView, and its parent widgets. If the ImageView or its parent has `WRAP_CONTENT` for both the height and width, the GIF rendering may not succeed (and neither callback of the `WeakRefLoadImageListener` will fire).  When this happens, the ImagView will be interpreted as having both a height and width of 0. Check that bounds have been given in either the xml or in the java code. Depending on your view structure, a `MATCH_PARENT` in either the height or width may be suffcient.

2. I get a gradle error message `Error:Failed to resolve: com.android.support:support-v4:26.0.1`.  What is causing this?
    * If your app is building to Android API 26 and up, the following lines are required in order for the Tenor Android Core to build.
    Add them to your application's root `build.gradle` file.
    ```java
        allprojects {
            repositories {
                maven { url 'https://maven.google.com' }
            }
        }
    ```



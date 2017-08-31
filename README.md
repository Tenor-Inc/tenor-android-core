Tenor Android Core
==================

- [Introduction](#introduction)
- [Setup](#setup)
  * [Option 1: Embed as a Module](#option-1-embed-as-a-module)
  * [Option 2: Embed as a Library File](#option-2-embed-as-a-library-file)
  * [Additional Required Dependencies](#additional-required-dependencies)
- [Initialize Tenor Core](#initialize-tenor-core)
- [Retrieving GIFs from Tenor](#retrieving-gifs-from-tenor)
  * [Trending GIFs](#trending-gifs)
  * [Searching GIFs by a Specific Search Term](#searching-gifs-by-a-specific-search-term)
- [Displaying GIFs](#displaying-gifs)
- [FAQs](#faqs)

## Introduction
Tenor's API delivers the most relevant GIFs for users anywhere in the world, across more than 30 languages. 
The Tenor Android Core provides Android developers with the code necessary to integrate essential Tenor GIF functionality into android mobile apps. 

The Tenor Android Core provides all the requisite tools for an easy way to **search for GIFs** and to **display GIFs** - either as a single GIF, or as a single or multi-column list of GIFs.

To add Tenor GIF search to your app, please follow the steps below.

## Setup
The following instructions are based on Android Studio.

### Option 1: Embed as a Module
To embed the `tenor-android-core.aar` file as a module, right-click the project and select `New` -> `Module` -> `Import .JAR/.AAR`, then select the `tenor-android-core.arr`.  

Once the file has been imported, you will need to include the `tenor-android-core` module as a dependency of any module that will use the core. Add the following lines to the `build.gradle` files of these modules:
```java
    dependencies {
        compile project(':tenor-android-core')
    }
```

### Option 2: Embed as a Library File
Alternatively, you can copy the `tenor-android-core.aar` into the `libs` folder of your `app` module.  

This approach requires some additional setup on the `build.gradle` file of your `app` module, by adding the following lines:  
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
For either of the import options used, you will also need to add the following dependencies to every module where `tenor-android-core` has been added, in order to use the full functionality of the Tenor Android Core:  
```java
  compile 'com.squareup.retrofit2:converter-gson:2.3.0'
  compile 'com.github.bumptech.glide:glide:3.8.0'
  compile 'com.android.support:support-annotations:26.0.1'
```


## Initialize Tenor Core
**You will need an `API_KEY` from Tenor.**  To request an api key, click [here](https://tenor.com/gifapi#apikey).

On your app's subclass of the `Application` class, add the following lines of code to the `onCreate()` function:
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
Alternatively, if you don't have a subclass of `Application`, then you should add these lines into your launcher activity.


## Retrieving GIFs from Tenor
The Tenor API offers robust ways to fetch the most relevant GIFs your application’s users are most likely to share.
The following sections will describe the simplest ways to fetch a stream of GIFs: the `trending` and the `search` API end points.

### Trending GIFs
The `trending` stream fetches whatever the most popular GIFs are at the time of the request, in the order of most popular.
If you wish to display GIFs to a user before a specific search term has been selected or entered, Tenor recommends displaying `trending` GIFs.
To access the `trending` API endpoint, add these lines of code to your application:
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).getTrending(
                    ApiClient.getServiceIds(getContext()),limit, pos);

    call.enqueue(new WeakRefCallback<Activity, GifsResponse>(this) {
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

* `limit` (type `integer`) - Fetch up to a specified number of results (max: 50).
* `pos` (type `string`) - Get results starting at position "value".  Use empty string `""` for the initial pos.  


### Searching GIFs by a Specific Search Term
Search is where Tenor's API particularly excels. Our understanding of what daily users search, share, upload, favorite, and collect allows us to continually return with precision the most relevant and shareable GIFs.

`search` uses the same fields as `trending`, but with an additional `q` field that takes a single or multiple words, to return the most precise GIFs possible.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).search(
                    ApiClient.getServiceIds(getContext()), q, limit, pos);

    call.enqueue(new WeakRefCallback<Activity, GifsResponse>(this) {
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

To see a detailed look of the **GIF response JSON object**, click [here](https://tenor.com/gifapi#responseobjects).

## Displaying GIFs
Once the `search` or `trending` response is received, the GIFs can now be loaded into an ImageView.  There are two steps involved in displaying GIFs.

* [Getting the GIF Url](#getting-the-gif-url)
* [Loading the GIF into an `ImageView`](#loading-the-gif-into-an-imageview)

### Getting the GIF Url
For a stream of multiple GIFs being displayed at once, as well as creating smaller bundles used for sharing, Tenor reccommends using `MediaCollectionFormats.GIF_TINY`.  Whereas for full size GIFs, you may use `MediaCollectionFormats.GIF`.

```java
    // using the response variable from the success callback
    Result gif_result_object = response.getResults().get(i); // `i` being the index of the result
    String gif_url = gif_result_object.getMedias().get(i).get(type).getUrl();
```
A full list of available url types may be found in the `MediaCollectionFormats` class.


### Loading the GIF into an `ImageView`
Once you have the url of the GIF, you can load it into an `ImageView`.  There are many content loading libraries available, but Tenor reccomend's [Glide](https://github.com/bumptech/glide), as it offers full support for loading GIFs into an `ImageView`.

We start by constructing a `GlideTaskParams`, which takes full advantage of the Glide library:
```java
    GlideTaskParams<ImageView> params = new GlideTaskParams<>(mImageView, gif_url);
    params.setListener(new WeakRefContentLoaderTaskListener<Context, ImageView>(getContext()) {
            @Override
            public void success(@NonNull Context context, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                // handle the success case
            }

            @Override
            public void failure(@NonNull Context context, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                // handle the success case
            }
        });
```
`mImageView` is the reference to the `ImageView` where the GIF will be displayed, and you can also add a callback for when the gif loading has completed.  For the full list of available loading options, see the `GlideTaskParams` class.

Once the `params` variable is constructed, use the `GifLoader` class to load the GIF:
```java
    GifLoader.loadGif(getContext(), params);
``` 

Following the instructions above will enable your app to search and display GIFs to your users.
A working demo - which showcases `search`,`trending`, and GIF image loading, as well as other Tenor API features like `tags` and `search suggestions` - can be found [here](https://github.com/Tenor-Inc/tenor-android-demo-search).

For full documentation of the Tenor API, with details on how to further refine and bolster your users' GIF experience, click [here](https://tenor.com/gifapi).


## FAQs

1. Why does my GIF not appear after I call loadGif?
    * Check what the height and width are set to in the ImageView, and its parent widgets. If the ImageView or its parent has WRAP_CONTENT for both the height and width, the GIF rendering may not succeed (and neither callback of the WeakRefLoadImageListener will fire). When this happens, the ImageView will be interpreted as having both a height and width of 0. Check that bounds have been given in either the xml or in the java code. Depending on your view structure, a MATCH_PARENT in either the height or width may be sufficient.

2. I get a gradle error message `Error:Failed to resolve: com.android.support:support-v4:26.0.1`. What is causing this?
    * If your app is building to Android API 26 and up, the following lines are required in order for the Tenor Android Core to build. Add them to your application's root `build.gradle` file.

    ```java
        allprojects {
            repositories {
                maven { url 'https://maven.google.com' }
            }
        }
    ```
    
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
Tenor's API delivers the most relevant GIFs for users anywhere in the world. 
The Tenor Android Core provides Android developers with the code necessary to integrate essential Tenor GIF functionality into android mobile apps. 

For example, the core provides all the requisite tools for an easy way to **search for GIFs** and to **display GIFs** - either as one large embedded GIF or as a stream of content.

To add Tenor GIF content to your app, please follow the steps below.

## Setup
The following instruction is based on Android Studio.

### Option 1: Embed as a Module
If you prefer to embed our `tenor-android-core.aar` file as a module, you can do so by right-clicking the project, select `New` -> `Module` -> `Import .JAR/.AAR` and choosing `tenor-android-core.arr`.  

Once the importing is done, you can use include the module as a dependency of your other modules by going to their module `build.gradle` file and add the following lines:
```java
    dependencies {
        compile project(':tenor-android-core')
    }
```

### Option 2: Embed as a Library File
Alternatively, you can copy our `tenor-android-core.aar` into the `libs` folder of your `app` module.  

This approach requires some additional setup on the `build.gradle` file of your `app` module:  
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
For either of the import options used, you will also need to add the following dependencies that are used by the core:  
```java
  compile 'com.squareup.retrofit2:converter-gson:2.3.0'
  compile 'com.github.bumptech.glide:glide:3.8.0'
  compile 'com.android.support:support-annotations:26.0.1'
```


## Initialize Tenor Core
First of all, you will need an `API_KEY` from Tenor.  To request an api key, click [here](https://tenor.com/gifapi#apikey).

On your subclass of `Application` class, add the following lines of code to under its `onCreate()`:
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
If you don't use subclass of `Application`, then you should add the code in above to your launcher activity.


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

`search` uses the same fields as `trending`, but with an additional `tag` field that takes a single or multiple words, to return the most precise GIFs possible.
```java
    Call<GifsResponse> call = ApiClient.getInstance(getContext()).search(
                    ApiClient.getServiceIds(getContext()), tag, limit, pos);

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

* [Getting GIF Url](#getting-gif-url)
* [Load GIF into `ImageView`](#load-gif-into-imageview)

### Getting GIF Url
For a stream of multiple GIFs being displayed at once, as well as creating smaller bundles used for sharing, we reccommend using `MediaCollectionFormats.GIF_TINY`.  Whereas for full size GIFs, you may use `MediaCollectionFormats.GIF`.

```java
    // using the response variable from the success callback
    Result gif_result_object = response.getResults().get(i); // `i` being the index of the result
    String gif_url = gif_result_object.getMedias().get(i).get(type).getUrl();
```
A full list of available url types may be found in the `MediaCollectionFormats` class.


### Load GIF into `ImageView`
Once we have the url of the GIF, we can load it into a `ImageView`.  There are many content loading library available, and our choice is [Glide](https://github.com/bumptech/glide), since it offers full support for loading GIFs into an `ImageView`.

We start by constructing a `GlideTaskParams`:
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
`mImageView` is the referenced to the `ImageView` that the GIF will be displayed, and you can also add a callback on the gif loading is completed.  For full list of parameters, take a look at the `GlideTaskParams` class.

Once the `params` is constructed, use the `GifLoader` class to load the GIF:
```java
    GifLoader.loadGif(getContext(), params);
``` 

This should be enough to get you started and displaying GIFs to your users.
A working demo - which showcases `search`,`trending`, and GIF image loading as well as other Tenor API features like `tags` and `search suggestions` - can be found in [here]().

Full documentation of our API, which details out further ways to refine and bolster the GIF experience, can be found in [here](https://tenor.com/gifapi).


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



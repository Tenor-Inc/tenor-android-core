package com.tenor.android.core.network;


import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.model.impl.Suggestions;
import com.tenor.android.core.response.impl.AnonIdResponse;
import com.tenor.android.core.response.impl.EmojiResponse;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.core.response.impl.PackResponse;
import com.tenor.android.core.response.impl.SearchSuggestionResponse;
import com.tenor.android.core.response.impl.TagsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * The network calls
 */
public interface IApiClient {
    /**
     * Search for gifs based on a query tag
     *
     * @param apiKey    api key to access the endpoint
     * @param query     <b>term</b> being searched for
     * @param locale    <b>current language</b> set by the user (used to determine the query term's language of origin)
     * @param anonId    a non id
     * @param limit     <b>bucket</b> size of each response
     * @param pos       <b>index</b> for where the first result should come from.  If <b>empty</b>, start at the first result
     * @param aaid      (Optional {@link NonNull}) Android Advertise Id
     * @param component the location of the app where the search occurs
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("search")
    @NonNull
    Call<GifsResponse> search(@Query("key") @NonNull String apiKey,
                              @Query("tag") @NonNull String query,
                              @Query("locale") @NonNull String locale,
                              @Query("limit") int limit,
                              @Query("pos") @NonNull String pos,
                              @Query("aaid") @NonNull String aaid,
                              @Query("component") @NonNull String component,
                              @QueryMap Map<String, String> anonId);

    /**
     * Retrieves a set of tags to be displayed as gif categories,
     * which when clicked will perform a gif search of that tag
     *
     * @param apiKey    api key to access the endpoint
     * @param type      what <b>subset of tags</b> you wish to retrieve
     * @param locale    <b>current language</b> set by the user
     * @param anonId    a non id
     * @param utcOffset the timezone
     * @return {@link Call}<{@link TagsResponse}>
     */
    @GET("tags")
    Call<TagsResponse> getTags(@Query("key") String apiKey,
                               @Query("type") String type,
                               @Query("locale") String locale,
                               @Query("timezone") String utcOffset,
                               @QueryMap Map<String, String> anonId);

    /**
     * Retrieves a set of emojis to use in gif searching.
     * Clicking on these emojis can perform searches with the emoji itselft as the query
     *
     * @param apiKey api key to access the endpoint
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link EmojiResponse}>
     */
    @GET("tags?platform=android&type=emoji")
    Call<EmojiResponse> getEmojiTags(@Query("key") String apiKey,
                                     @Query("locale") String locale,
                                     @QueryMap Map<String, String> anonId);

    /**
     * Retrieves a set of gifs representing those that are currently trending
     *
     * @param apiKey api key to access the endpoint
     * @param limit  <b>bucket</b> size of each response
     * @param pos    <b>index</b> for where the first result should come from.  If <b>empty</b>, start at the first result
     * @param type   <i>optional.</i> Qualifier for specific searches.
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("trending")
    Call<GifsResponse> getTrending(@Query("key") String apiKey,
                                   @Query("limit") Integer limit,
                                   @Query("pos") String pos,
                                   @Query("type") String type,
                                   @Query("locale") String locale,
                                   @QueryMap Map<String, String> anonId);

    /**
     * Retrieves a set of mp4s containing audio
     *
     * @param apiKey api key to access the endpoint
     * @param limit  <b>bucket</b> size of each response
     * @param pos    <b>index</b> for where the first result should come from.  If <b>empty</b>, start at the first result
     * @param type   <i>optional.</i> Qualifier for specific searches.
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("music")
    Call<GifsResponse> getMusic(@Query("key") String apiKey,
                                @Query("limit") Integer limit,
                                @Query("pos") String pos,
                                @Query("type") String type,
                                @Query("locale") String locale,
                                @QueryMap Map<String, String> anonId);

    /**
     * Retrieve gifs with specific ids
     *
     * @param apiKey api key to access the endpoint
     * @param ids    a comma separated string containing a list of unique ids of the desired gifs
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("gifs")
    Call<GifsResponse> getGifs(@Query("key") String apiKey,
                               @Query("ids") String ids,
                               @Query("locale") String locale,
                               @QueryMap Map<String, String> anonId);

    /**
     * Search
     * Takes in a tag, and a set of related and similar tags, represented as a Pivot object
     *
     * @param apiKey api key to access the endpoint
     * @param tag    initial term
     * @param limit  <b>bucket</b> size of each response
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link SearchSuggestionResponse}>
     */
    @GET("search_suggestions?platform=android")
    Call<SearchSuggestionResponse> getSearchSuggestions(@Query("key") String apiKey,
                                                        @Query("tag") String tag,
                                                        @Query("limit") @IntRange(from = 1, to = 50) Integer limit,
                                                        @Query("locale") String locale,
                                                        @QueryMap Map<String, String> anonId);

    /**
     * Returns a set of gifs that fall into the category of two related tags.
     *
     * @param apiKey api key to access the endpoint
     * @param tag1   original tag term used in the search
     * @param tag2   selected pivot
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("intersection")
    Call<GifsResponse> getIntersections(@Query("key") String apiKey,
                                        @Query("tags") String tag1,
                                        @Query("tag2") String tag2,
                                        @Query("locale") String locale,
                                        @QueryMap Map<String, String> anonId);

    /**
     * Returns a set of gifs that fall into the category of two related tags.
     *
     * @param apiKey api key to access the endpoint
     * @param tags   comma separated string of the original tag and pivot tag
     * @param locale <b>current language</b> set by the user
     * @param anonId a non id
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("intersection")
    Call<GifsResponse> getIntersections(@Query("key") String apiKey,
                                        @Query("tags") String tags,
                                        @Query("locale") String locale,
                                        @QueryMap Map<String, String> anonId);

    /**
     * Returns the set of gifs uploaded by the current user
     *
     * @param apiKey      api key to access the endpoint
     * @param userId      <b>id</b> of the user
     * @param accessToken login <b>token</b>
     * @param locale      <b>current language</b> set by the user
     * @param anonId      a non id
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("user")
    Call<GifsResponse> getUserGifs(@Query("key") String apiKey,
                                   @Query("id") String userId,
                                   @Query("access_token") String accessToken,
                                   @Query("locale") String locale,
                                   @QueryMap Map<String, String> anonId);

    @HEAD
    Call<Void> getImageSize(@Url String imageUrl);

    @GET("suggest")
    Call<Suggestions> getSuggestions(@Query("key") String apiKey,
                                     @Query("tag") String tag,
                                     @Query("limit") Integer limit,
                                     @Query("type") String type,
                                     @Query("timezone") String utcOffset,
                                     @Query("locale") String locale,
                                     @Query("allterms") boolean isAllTerms,
                                     @QueryMap Map<String, String> anonId);

    @GET("registershare")
    Call<GifsResponse> registerShare(@Query("key") String apiKey,
                                     @Query("id") Integer id,
                                     @Query("locale") String locale,
                                     @QueryMap Map<String, String> anonId);

    /**
     * Get keyboard id
     *
     * @param apiKey api key to access the endpoint
     * @param locale <b>current language</b> set by the user
     * @return {@link Call}<{@link AnonIdResponse}>
     */
    @GET("anonid?platform=android")
    Call<AnonIdResponse> getAnonId(@Query("key") String apiKey,
                                   @Query("locale") String locale);


    @GET("pack")
    Call<PackResponse> getPack(@Query("key") String apiKey,
                               @Query("publicid") String publicId);

    /**
     * Register view
     *
     * @param apiKey          api key to access the endpoint
     * @param sourceId        the source id of a {@link Result}
     * @param aaid            (Optional {@link NonNull}) Android Advertise Id
     * @param count           number of times a GIF has been viewed within a short time span (~5 minutes)
     * @param timestamp       the client time when a GIF was initially viewed
     * @param utcOffset       the client utc offset for the given timestamp when a GIF was initially viewed
     * @param duration        the total time in milliseconds that the featured GIF has been visible
     * @param visibleFraction the fraction of the GIF visible, range between 0f and 1f
     * @return {@link Call}<{@link Void}>
     */
    @POST("registerview")
    @FormUrlEncoded
    @NonNull
    Call<Void> registerView(@Field("key") @NonNull String apiKey,
                            @Field("source_id") @NonNull String sourceId,
                            @Field("aaid") @NonNull String aaid,
                            @Field("visual_pos") String visualPos,
                            @Field("count") int count,
                            @Field("timestamp") float timestamp,
                            @Field("timezone") @NonNull String utcOffset,
                            @Field("elapsed_ms") int duration,
                            @Field("visible_fraction") float visibleFraction,
                            @FieldMap Map<String, String> anonId);

    /**
     * Register batch action
     *
     * @param apiKey api key to access the endpoint
     * @param aaid   (Optional {@link NonNull}) Android Advertise Id
     * @param data   the serialized data
     * @return {@link Call}<{@link Void}>
     */
    @POST("registeraction")
    @FormUrlEncoded
    @NonNull
    Call<Void> registerActions(@Field("key") @NonNull String apiKey,
                               @Field("aaid") @NonNull String aaid,
                               @Field("data") @NonNull String data,
                               @FieldMap Map<String, String> anonId);

    /**
     * Register action
     *
     * @param apiKey    api key to access the endpoint
     * @param sourceId  the source id of a {@link Result}
     * @param aaid      (Optional {@link NonNull}) Android Advertise Id
     * @param action    the action
     * @param timestamp the time stamp for wehn the action was registered
     * @param utcOffset the client utc offset for the given timestamp when a GIF was initially viewed
     * @return {@link Call}<{@link Void}>
     */
    @POST("registeraction")
    @FormUrlEncoded
    @NonNull
    Call<Void> registerAction(@Field("key") @NonNull String apiKey,
                              @Field("source_id") @NonNull String sourceId,
                              @Field("aaid") @NonNull String aaid,
                              @Field("visual_pos") String visualPos,
                              @Field("action") @NonNull String action,
                              @Field("timestamp") float timestamp,
                              @Field("timezone") @NonNull String utcOffset,
                              @FieldMap Map<String, String> anonId);
}

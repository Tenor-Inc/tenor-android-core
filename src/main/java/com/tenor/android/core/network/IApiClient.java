package com.tenor.android.core.network;


import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.tenor.android.core.measurable.MeasurableViewHolderEvent;
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
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @param query      <b>term</b> being searched for
     * @param limit      <b>bucket</b> size of each response
     * @param pos        <b>index</b> for where the first result should come from.  If <b>empty</b>, start at the first result
     * @param component  the location of the app where the search occurs
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("search")
    @NonNull
    Call<GifsResponse> search(@QueryMap Map<String, String> serviceIds,
                              @Query("tag") @NonNull String query,
                              @Query("limit") int limit,
                              @Query("pos") @NonNull String pos,
                              @Query("component") @NonNull String component);

    /**
     * Retrieves a set of tags to be displayed as gif categories,
     * which when clicked will perform a gif search of that tag
     *
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @param type       what <b>subset of tags</b> you wish to retrieve
     * @param utcOffset  the timezone
     * @return {@link Call}<{@link TagsResponse}>
     */
    @GET("tags")
    Call<TagsResponse> getTags(@QueryMap Map<String, String> serviceIds, @Query("type") String type,
                               @Query("timezone") String utcOffset);

    /**
     * Retrieves a set of emojis to use in gif searching.
     * Clicking on these emojis can perform searches with the emoji itselft as the query
     *
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @return {@link Call}<{@link EmojiResponse}>
     */
    @GET("tags?platform=android&type=emoji")
    Call<EmojiResponse> getEmojiTags(@QueryMap Map<String, String> serviceIds);

    /**
     * Retrieves a set of gifs representing those that are currently trending
     *
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @param limit      <b>bucket</b> size of each response
     * @param pos        <b>index</b> for where the first result should come from.  If <b>empty</b>, start at the first result
     * @param type       <i>optional.</i> Qualifier for specific searches.
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("trending")
    Call<GifsResponse> getTrending(@QueryMap Map<String, String> serviceIds,
                                   @Query("limit") Integer limit,
                                   @Query("pos") String pos,
                                   @Query("type") String type);

    /**
     * Retrieve gifs with specific ids
     *
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @param ids        a comma separated string containing a list of unique ids of the desired gifs
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("gifs")
    Call<GifsResponse> getGifs(@QueryMap Map<String, String> serviceIds,
                               @Query("ids") String ids);

    /**
     * Search
     * Takes in a tag, and a set of related and similar tags, represented as a Pivot object
     *
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @param tag        initial term
     * @param limit      <b>bucket</b> size of each response
     * @return {@link Call}<{@link SearchSuggestionResponse}>
     */
    @GET("search_suggestions?platform=android")
    Call<SearchSuggestionResponse> getSearchSuggestions(@QueryMap Map<String, String> serviceIds,
                                                        @Query("tag") String tag,
                                                        @Query("limit") @IntRange(from = 1, to = 50) Integer limit);

    /**
     * Returns the set of gifs uploaded by the current user
     *
     * @param serviceIds  a {@link Map} of a collection of ids for better content delivery experience
     * @param userId      <b>id</b> of the user
     * @param accessToken login <b>token</b>
     * @return {@link Call}<{@link GifsResponse}>
     */
    @GET("user")
    Call<GifsResponse> getUserGifs(@QueryMap Map<String, String> serviceIds,
                                   @Query("id") String userId,
                                   @Query("access_token") String accessToken);

    @HEAD
    Call<Void> getImageSize(@Url String imageUrl);

    @GET("suggest")
    Call<Suggestions> getSuggestions(@QueryMap Map<String, String> serviceIds,
                                     @Query("tag") String tag,
                                     @Query("limit") Integer limit,
                                     @Query("type") String type,
                                     @Query("timezone") String utcOffset,
                                     @Query("allterms") boolean isAllTerms);

    @GET("registershare")
    Call<Void> registerShare(@QueryMap Map<String, String> serviceIds,
                             @Query("id") Integer id);

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
     * Register batch action
     *
     * @param serviceIds a {@link Map} of a collection of ids for better content delivery experience
     * @param data       a serialized list of {@link MeasurableViewHolderEvent}
     * @return {@link Call}<{@link Void}>
     */
    @POST("registeraction")
    @FormUrlEncoded
    @NonNull
    Call<Void> registerActions(@FieldMap Map<String, String> serviceIds,
                               @Field("data") @NonNull String data);
}

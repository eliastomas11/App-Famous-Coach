package com.example.yourfamouscoach.data.datasources.remote.ai;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AiService {

    @Headers("Authorization: Bearer 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")
    @POST("google/flan-t5-xxl")
    Single<List<AiResponseModel>> aiAnswer(@Body AiRequestModel inputs);
}

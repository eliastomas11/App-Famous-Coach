package data.datasources.remote.ai;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AiTest {

    private String URL = "https://api-inference.huggingface.co/models/";

    private AiService retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build().create(AiService.class);

    public AiService getRetrofit(){
        return retrofit;
    }
}

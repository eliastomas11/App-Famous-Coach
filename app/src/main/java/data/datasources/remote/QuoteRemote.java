package data.datasources.remote;

import java.util.List;

import data.datasources.remote.quotesource.service.ApiService;
import data.model.QuoteDto;
import io.reactivex.rxjava3.core.Single;

public class QuoteRemote {

    private final ApiService apiService;


    public QuoteRemote(ApiService apiService){
        this.apiService = apiService;
    }
    public Single<List<QuoteDto>> getRemoteQuotes(){
        return apiService.getQuotes();
    }

}

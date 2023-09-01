package data.datasources.remote.quotesource.service;

import java.util.List;

import data.model.QuoteDto;
import domain.model.Quote;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiService {

    @GET("quotes")
    Single<List<QuoteDto>> getQuotes();

}

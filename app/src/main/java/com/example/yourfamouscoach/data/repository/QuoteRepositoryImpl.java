package com.example.yourfamouscoach.data.repository;


import android.util.Log;

import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionsDao;
import com.example.yourfamouscoach.data.datasources.remote.QuoteRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteDao;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteEntity;
import com.example.yourfamouscoach.data.datasources.local.ailocal.AiClient;
import com.example.yourfamouscoach.data.datasources.local.cache.QuoteMemCache;
import com.example.yourfamouscoach.data.mapper.QuoteDataMapper;
import com.example.yourfamouscoach.data.model.Emotions;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;

public class QuoteRepositoryImpl implements IQuotesRepository {

    private final QuoteRemote quoteRemote;
    private final QuoteDao quoteLocal;
    private final AiClient aiClient;
    private final QuoteDataMapper mapper;

    private final QuoteMemCache quoteCache;
    private final EmotionsDao emotionLocal;

    public QuoteRepositoryImpl(QuoteRemote quoteRemote, QuoteDao quoteLocal, AiClient aiClient, QuoteDataMapper mapper, QuoteMemCache quoteCache, EmotionsDao emotionLocal) {
        this.quoteRemote = quoteRemote;
        this.quoteLocal = quoteLocal;
        this.aiClient = aiClient;
        this.mapper = mapper;
        this.quoteCache = quoteCache;
        this.emotionLocal = emotionLocal;
    }

    @Override
    public Single<List<Quote>> getQuotes() {
        return quoteRemote.getRemoteQuotes().subscribeOn(Schedulers.io())
                .doOnSuccess(quoteCache::saveQuotes)
                .map((quote) -> mapper.dtoToDomain(quote,Emotions.RANDOM.getName()));
    }

    @Override
    public @NonNull Completable saveQuote(Quote quote) {
        //CompletableFuture.runAsync(() -> quoteLocal.saveQuotes(mapper.domainToDb(quotes))).join();
        //Completable.fromAction(quoteLocal::deleteQuotes).subscribeOn(Schedulers.io());
        //QuoteEntity quote = mapper.transformDomain(quote)
        Log.i("Emotion",quote.getEmotion());
        return Completable.fromAction(() -> quoteLocal.saveQuotes(quote.getQuote(),quote.getAuthor(),quote.getEmotion()));
    }


    private List<QuoteEntity> getQuotesLocal() {
        //return CompletableFuture.supplyAsync(quoteLocal::getAll).join();
        return null;
    }

    @Override
    public Single<Quote> getQuoteWithEmotion(String emotion) {
        List<QuoteDto> quoteDtos = quoteCache.getQuotes();
        if (emotion.equalsIgnoreCase("Random")) {
            return getRandomQuote(quoteDtos,emotion);
        }
        return aiClient.getAnswer(emotion, quoteDtos).
                flatMap((quote) -> {
                    Quote quoteConverted = mapper.aiToDomain(quote,getAuthor(quote.get(0).getGenerated_text(),quoteDtos));
                    if(quoteConverted.getAuthor().isEmpty()){
                        return getRandomQuote(quoteDtos,emotion);
                    }
                    return Single.just(quoteConverted);
                });
    }

    private Single<Quote> getRandomQuote(List<QuoteDto> quoteDtos,String emotion) {
        Log.i("Respuesta","Entro random");
        return Single.just(mapper.transformDto(quoteDtos.get(new Random().nextInt(quoteDtos.size())),emotion));
    }


    @Override
    public void delete() {
        CompletableFuture.runAsync(quoteLocal::deleteQuotes).join();
    }

    @Override
    public Single<List<Quote>> getFavoritesQuotes() {
        return quoteLocal.getQuoteWithEmotion().map(quoteWithEmotions -> {
            ArrayList<Quote> quotesList = new ArrayList<>();
            quoteWithEmotions.forEach(quoteWithEmotion -> quotesList.add(
                    mapper.transformEntity(quoteWithEmotion.quoteEntity,quoteWithEmotion.emotionEntity.getName().getName())));
            return quotesList;
        });
    }


    private String getAuthor(String quote, List<QuoteDto> quoteList) {
        String author = "";
        for (QuoteDto quoteDto : quoteList) {
            String quoteString = quoteDto.getQuote();
            if (quoteString.equalsIgnoreCase(quote)) {
                author = quoteDto.getAuthor();
            }
        }
        return author;
    }

    @Override
    public Call<List<QuoteDto>> getSingleQuote(){
        quoteRemote.getSingleQuote();
    }

}

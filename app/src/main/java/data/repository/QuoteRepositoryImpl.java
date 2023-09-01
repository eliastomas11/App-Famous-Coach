package data.repository;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import data.datasources.local.dataBase.emotion.EmotionEntity;
import data.datasources.local.dataBase.emotion.EmotionsDao;
import data.datasources.local.dataBase.quote.QuoteDao;
import data.datasources.local.dataBase.quote.QuoteEntity;
import data.datasources.local.ailocal.AiClient;
import data.datasources.local.cache.QuoteMemCache;
import data.datasources.remote.QuoteRemote;
import data.mapper.QuoteDataMapper;
import data.model.Emotions;
import data.model.QuoteDto;
import domain.interfaces.IQuotesRepository;
import domain.model.Quote;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

}

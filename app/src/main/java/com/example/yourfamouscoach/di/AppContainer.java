package com.example.yourfamouscoach.di;

import android.content.Context;

import com.example.yourfamouscoach.data.datasources.local.ailocal.AiClient;
import com.example.yourfamouscoach.data.datasources.local.cache.QuoteMemCache;
import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionsDao;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteDao;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteDatabase;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteLocal;
import com.example.yourfamouscoach.data.datasources.remote.QuoteRemote;
import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiClient;
import com.example.yourfamouscoach.data.mapper.QuoteDataMapper;
import com.example.yourfamouscoach.data.repository.QuoteRepositoryImpl;
import com.example.yourfamouscoach.domain.usecase.favoritequotes.DeleteSavedQuote;
import com.example.yourfamouscoach.domain.usecase.homescreen.CheckSaved;
import com.example.yourfamouscoach.domain.usecase.homescreen.GetQuotes;
import com.example.yourfamouscoach.domain.usecase.homescreen.SaveQuote;
import com.example.yourfamouscoach.domain.usecase.homescreen.SpecificQuote;
import com.example.yourfamouscoach.ui.interfaces.IHomePresenter;
import com.example.yourfamouscoach.ui.interfaces.IHomeView;
import com.example.yourfamouscoach.ui.presenters.HomePresenter;

public class AppContainer {

    private IHomePresenter quotePresenter;

    private final QuoteDatabase db;
    private final QuoteRemote quoteRemote;
    public final QuoteRepositoryImpl quotesRepository;
    private final GetQuotes getQuotesUseCase;
    private final SaveQuote saveQuoteUseCase;
    private final SpecificQuote specificQuoteUseCase;

    private final DeleteSavedQuote deleteSavedQuoteUseCase;
    private final CheckSaved checkSavedUseCase;
    private final AiClient aiClient;
    private final QuoteDataMapper quoteDataMapper;
    private final QuoteMemCache quoteMemCache;

    public FavoriteQuotesContainer favoriteQuotesContainer = null;

    public AppContainer(Context context){
        db = QuoteLocal.getDb(context);
        QuoteDao quoteDao = db.getQuoteDao();
        EmotionsDao emotionDao = db.getEmotionDao();
        quoteRemote = new QuoteRemote(ApiClient.getApiClientInstance());
        aiClient = new AiClient();
        quoteDataMapper = new QuoteDataMapper();
        quoteMemCache = new QuoteMemCache();
        quotesRepository =  new QuoteRepositoryImpl(quoteRemote, quoteDao,aiClient,quoteDataMapper,quoteMemCache,emotionDao);
        getQuotesUseCase = new GetQuotes(quotesRepository);
        saveQuoteUseCase = new SaveQuote(quotesRepository);
        specificQuoteUseCase = new SpecificQuote(quotesRepository);
        checkSavedUseCase = new CheckSaved(quotesRepository);
        deleteSavedQuoteUseCase = new DeleteSavedQuote(quotesRepository);
    }

    public IHomePresenter providePresenter(IHomeView view){
        return quotePresenter = new HomePresenter(view,getQuotesUseCase, saveQuoteUseCase,specificQuoteUseCase,deleteSavedQuoteUseCase,checkSavedUseCase);
    }
}

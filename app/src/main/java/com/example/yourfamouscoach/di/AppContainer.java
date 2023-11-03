package com.example.yourfamouscoach.di;

import android.content.Context;

import com.example.yourfamouscoach.data.datasources.local.ailocal.AiClient;
import com.example.yourfamouscoach.data.datasources.local.cache.QuoteCaching;
import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionsDao;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteCacheDao;
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
import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;
import com.example.yourfamouscoach.ui.presenters.HomePresenter;
import com.example.yourfamouscoach.ui.presenters.MainPresenter;

public class AppContainer {

    private IHomePresenter quotePresenter;
    private IMainPresenter mainPresenter;
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
    private final QuoteCaching quoteCaching;

    public FavoriteQuotesContainer favoriteQuotesContainer = null;

    public AppContainer(Context context){
        db = QuoteLocal.getDb(context);
        QuoteDao quoteDao = db.getQuoteDao();
        EmotionsDao emotionDao = db.getEmotionDao();
        QuoteCacheDao quoteCacheDao = db.getQuoteCacheDao();
        quoteRemote = new QuoteRemote(ApiClient.getApiClientInstance());
        aiClient = new AiClient();
        quoteDataMapper = new QuoteDataMapper();
        quoteCaching = new QuoteCaching(quoteCacheDao, quoteDataMapper);
        quotesRepository =  new QuoteRepositoryImpl(quoteRemote, quoteDao,aiClient,quoteDataMapper, quoteCaching);
        getQuotesUseCase = new GetQuotes(quotesRepository);
        saveQuoteUseCase = new SaveQuote(quotesRepository);
        specificQuoteUseCase = new SpecificQuote(quotesRepository);
        checkSavedUseCase = new CheckSaved(quotesRepository);
        deleteSavedQuoteUseCase = new DeleteSavedQuote(quotesRepository);
    }

    public IHomePresenter providePresenter(IHomeView view){
        return quotePresenter = new HomePresenter(view,getQuotesUseCase, saveQuoteUseCase,specificQuoteUseCase,deleteSavedQuoteUseCase,checkSavedUseCase);
    }

    public IMainPresenter providePresenter(IMainView view){
        return mainPresenter = new MainPresenter(view);
    }
}

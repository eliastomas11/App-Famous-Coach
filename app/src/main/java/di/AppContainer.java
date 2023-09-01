package di;

import android.content.Context;

import com.example.yourfamouscoach.ui.interfaces.IHomePresenter;
import com.example.yourfamouscoach.ui.interfaces.IHomeView;
import com.example.yourfamouscoach.ui.presenters.HomePresenter;

import data.datasources.local.dataBase.emotion.EmotionsDao;
import data.datasources.local.dataBase.quote.QuoteDao;
import data.datasources.local.dataBase.quote.QuoteDatabase;
import data.datasources.local.dataBase.quote.QuoteLocal;
import data.datasources.local.ailocal.AiClient;
import data.datasources.local.cache.QuoteMemCache;
import data.datasources.remote.QuoteRemote;
import data.datasources.remote.quotesource.service.ApiClient;
import data.mapper.QuoteDataMapper;
import data.repository.QuoteRepositoryImpl;
import domain.usecase.homescreen.GetQuotes;
import domain.usecase.homescreen.SaveQuote;
import domain.usecase.homescreen.SpecificQuote;

public class AppContainer {

    private IHomePresenter quotePresenter;

    private final QuoteDatabase db;
    private final QuoteRemote quoteRemote;
    public final QuoteRepositoryImpl quotesRepository;
    private final GetQuotes getQuotesUseCase;
    private final SaveQuote saveQuoteUseCase;
    private final SpecificQuote specificQuoteUseCase;
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
    }

    public IHomePresenter providePresenter(IHomeView view){
        return quotePresenter = new HomePresenter(view,getQuotesUseCase, saveQuoteUseCase,specificQuoteUseCase);
    }
}

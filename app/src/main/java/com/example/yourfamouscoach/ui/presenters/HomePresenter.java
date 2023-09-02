package com.example.yourfamouscoach.ui.presenters;

import android.util.Log;

import com.example.yourfamouscoach.ui.interfaces.IHomePresenter;
import com.example.yourfamouscoach.ui.interfaces.IHomeView;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

import com.example.yourfamouscoach.domain.model.Quote;
import com.example.yourfamouscoach.domain.usecase.homescreen.GetQuotes;
import com.example.yourfamouscoach.domain.usecase.homescreen.SaveQuote;
import com.example.yourfamouscoach.domain.usecase.homescreen.SpecificQuote;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.example.yourfamouscoach.utils.QuoteMapper;

public class HomePresenter implements IHomePresenter {

    private final IHomeView view;
    private final GetQuotes getQuotesUseCase;
    private final SaveQuote saveQuoteUseCase;
    private final SpecificQuote specificQuoteUseCase;

    public HomePresenter(IHomeView view, GetQuotes getQuotesUseCase, SaveQuote saveQuoteUseCase, SpecificQuote specificQuote) {
        this.view = view;
        this.getQuotesUseCase = getQuotesUseCase;
        this.saveQuoteUseCase = saveQuoteUseCase;
        this.specificQuoteUseCase = specificQuote;
    }

    @Override
    public void fetchData() {
        view.showProgressBar();

        getQuotesUseCase.getQuotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Quote>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Quote> quotes) {
                        Log.i("SAVED", "saved again");
                        view.showQuote(quotes.get(0).getQuote(), quotes.get(0).getAuthor());
                        view.adaptText();
                        view.showBuddha();
                        view.hideProgressBar();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressBar();
                        view.showQuote(e.getMessage(), "");
                    }
                });
    }


    @Override
    public void fetchSpecificQuote(String emotion) {
        view.showProgressBar();
        view.hideQuoteAndAuthorText();
        specificQuoteUseCase.getAnswer(emotion).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Quote>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Quote quote) {
                        view.hideProgressBar();
                        view.showFavUnsaved();
                        QuotePresentation quotePresentation = QuoteMapper.mapDomainToPresentation(quote);
                        view.showQuote(quotePresentation.getQuote(), quotePresentation.getAuthor());
                        view.adaptText();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void onInitView() {
        view.initViews();
    }

    @Override
    public void onFavClicked(String quote, String author, String emotion) {
        view.showFavSaved();
        saveQuoteUseCase.saveQuote(QuoteMapper.mapPresentationToDomain(new QuotePresentation(quote, author), emotion))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        view.showFavSaved();
                        view.showMessage("Saved successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showFavUnsaved();
                        view.showMessage(e.getMessage());
                        Log.i("base", e.getMessage());
                    }
                });
    }

    @Override
    public void onShareClicked() {
        //view.launchPermissions();
        view.shareQuote();
    }


}

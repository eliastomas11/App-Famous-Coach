package com.example.yourfamouscoach.ui.presenters;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.yourfamouscoach.domain.usecase.favoritequotes.DeleteSavedQuote;
import com.example.yourfamouscoach.domain.usecase.homescreen.CheckSaved;
import com.example.yourfamouscoach.ui.interfaces.IHomePresenter;
import com.example.yourfamouscoach.ui.interfaces.IHomeView;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import com.example.yourfamouscoach.domain.model.Quote;
import com.example.yourfamouscoach.domain.usecase.homescreen.GetQuotes;
import com.example.yourfamouscoach.domain.usecase.homescreen.SaveQuote;
import com.example.yourfamouscoach.domain.usecase.homescreen.SpecificQuote;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.example.yourfamouscoach.utils.QuoteMapper;

public class HomePresenter implements IHomePresenter {

    private final IHomeView view;
    private final GetQuotes getQuotesUseCase;
    private final SaveQuote saveQuoteUseCase;
    private final SpecificQuote specificQuoteUseCase;

    private final DeleteSavedQuote deleteSavedQuoteUseCase;
    private final CheckSaved checkSavedUseCase;

    private final CompositeDisposable subscribers = new CompositeDisposable();

    public HomePresenter(IHomeView view, GetQuotes getQuotesUseCase, SaveQuote saveQuoteUseCase, SpecificQuote specificQuote, DeleteSavedQuote deleteSavedQuote, CheckSaved checkSavedUseCase) {
        this.view = view;
        this.getQuotesUseCase = getQuotesUseCase;
        this.saveQuoteUseCase = saveQuoteUseCase;
        this.specificQuoteUseCase = specificQuote;
        this.deleteSavedQuoteUseCase = deleteSavedQuote;
        this.checkSavedUseCase = checkSavedUseCase;
    }

    @Override
    public void fetchData() {
        view.showProgressBar();
        getQuotesUseCase.getQuotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Quote>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        subscribers.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Quote quote) {
                        checkIfIsSaved(quote);
                        view.showAuthorImage(makeUrl(quote.getAuthor()), quote.getQuote(), quote.getAuthor());
                        view.hideLoadingScreen();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", e.getMessage());
                        view.hideProgressBar();
                        view.showQuote("Unexpected Error try again later", "");
                        view.hideLoadingScreen();
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
                        subscribers.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Quote quote) {
                        checkIfIsSaved(quote);
                        QuotePresentation quotePresentation = QuoteMapper.mapDomainToPresentation(quote);
                        view.showAuthorImage(makeUrl(quotePresentation.getAuthor()), quotePresentation.getQuote(), quotePresentation.getAuthor());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", e.getMessage());
                        view.hideProgressBar();
                        view.showMessage("Error Loading the Quote");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void checkIfIsSaved(Quote quote) {
        String quoteText = String.format("\"%s\"", quote.getQuote());
        Quote quoteToCheck = new Quote(quoteText,quote.getAuthor(),quote.getEmotion());
        Disposable disposable = checkSavedUseCase.checkSaved(quoteToCheck)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saved -> {
                    if (saved) {
                        view.showFavSaved();
                        view.checkSavedState(true);
                    } else {
                        view.showFavUnsaved();
                        view.checkSavedState(false);
                    }
                }, throwable -> {
                    view.checkSavedState(false);
                    view.showFavUnsaved();
                });
        subscribers.add(disposable);
    }

    @Override
    public void onInitView() {
        view.initViews();
    }

    @Override
    public void onFavClicked(boolean saved, String quote, String author, String emotion) {
        Quote quoteToDomain = QuoteMapper.mapPresentationToDomain(new QuotePresentation(quote, author), emotion);
        if (saved) {
            saveQuoteUseCase.saveQuote(quoteToDomain)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            subscribers.add(d);
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
        } else {
            deleteSavedQuoteUseCase.deleteSavedQuote(QuoteMapper.mapPresentationToDomain(new QuotePresentation(quote, author), emotion))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            subscribers.add(d);
                        }

                        @Override
                        public void onComplete() {
                            view.showFavUnsaved();
                            view.showMessage("Unsaved");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            view.showFavSaved();
                            view.showMessage(e.getMessage());
                            Log.i("base", e.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onShareClicked() {
        //view.launchPermissions();
        view.shareQuote();
    }

    @Override
    public void onImageLoad(String quote, String author) {
        view.hideProgressBar();
        view.showQuote(quote, author);
        view.adaptText();
        view.showAuthorImageAnimation();
    }

    @Override
    public void onErrorImageLoad(String quote, String author) {
        view.hideProgressBar();
        view.showQuote(quote, author);
        view.adaptText();
        view.showErrorAuthorImage();
        view.showAuthorImageAnimation();
    }

    @Override
    public void onNotificationQuote(String quote, String author) {
        view.showAuthorImage(makeUrl(author), quote, author);
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void onDestroy() {
        subscribers.clear();
    }

    @Override
    public void onRestore(String quote, String author, Boolean isSaved) {
        view.showAuthorImage(makeUrl(author), quote, author);
        view.checkSavedState(isSaved);
        if(isSaved){
            view.showFavSaved();
        }else{
            view.showFavUnsaved();
        }
    }


    private String makeUrl(String author) {
        String modifyAuthorName = author;
        modifyAuthorName = modifyAuthorName.replace("-", "--").replace(".", "_").replace(" ", "-");
        return "https://zenquotes.io/img/" + modifyAuthorName.toLowerCase() + ".jpg";
    }
}

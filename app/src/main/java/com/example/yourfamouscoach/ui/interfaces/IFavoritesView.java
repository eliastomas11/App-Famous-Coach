package com.example.yourfamouscoach.ui.interfaces;

import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

public interface IFavoritesView {
    void showProgressBar();
    void hideProgressBar();

    void showEmptyMessage(String message);

    void showQuoteList(List<QuotePresentation> quotePresentationList);

    void showMessage(String message);
    void copyText(String quoteTextToCopy, String authorTextToCopy);

    void shareQuoteSaved(String quote,String author);

}

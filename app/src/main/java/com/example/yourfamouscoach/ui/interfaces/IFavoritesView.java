package com.example.yourfamouscoach.ui.interfaces;

import com.example.yourfamouscoach.ui.model.QuotePresentation;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.util.List;

public interface IFavoritesView {
    void showProgressBar();
    void hideProgressBar();

    void showEmptyMessage(String message);

    void showQuoteList(List<QuotePresentation> quotePresentationList);
}

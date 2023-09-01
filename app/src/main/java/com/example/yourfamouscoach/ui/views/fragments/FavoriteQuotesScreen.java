package com.example.yourfamouscoach.ui.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yourfamouscoach.databinding.FragmentSavedQuotesBinding;
import com.example.yourfamouscoach.ui.adapters.QuotesFavAdapter;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuotesPresenter;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.model.QuotePresentation;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import di.AppContainer;
import di.FavoriteQuotesContainer;
import di.MyApplication;


public class FavoriteQuotesScreen extends Fragment implements IFavoritesView {


    private FragmentSavedQuotesBinding binding;
    private IFavoritesQuotesPresenter presenter;

    private List<Emojis> emojisList = new ArrayList<>(Arrays.asList(Emojis.values()));

    private AppContainer appContainer;

    public FavoriteQuotesScreen() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSavedQuotesBinding.inflate(inflater, container, false);
        appContainer = ((MyApplication) requireActivity().getApplication()).appContainer;
        appContainer.favoriteQuotesContainer = new FavoriteQuotesContainer(appContainer.quotesRepository);
        presenter = appContainer.favoriteQuotesContainer.providePresenter(this);
        presenter.fetchQuotes();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        appContainer.favoriteQuotesContainer = null;
    }

    @Override
    public void showProgressBar() {
        binding.pbLoadingFavQuotes.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.pbLoadingFavQuotes.setVisibility(View.GONE);

    }

    @Override
    public void showEmptyMessage(String message) {
        binding.tvEmptyList.setText(message);
    }

    @Override
    public void showQuoteList(List<QuotePresentation> quotePresentationList) {
        binding.rvFavQuotes.setAdapter(new QuotesFavAdapter(quotePresentationList,emojisList,requireContext()));
        binding.rvFavQuotes.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
    }
}
package com.example.yourfamouscoach.ui.adapters;

import static android.content.Context.CLIPBOARD_SERVICE;

import static com.example.yourfamouscoach.utils.StorageUtils.GREATER;
import static com.example.yourfamouscoach.utils.StorageUtils.LOWER;
import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.QuoteItemBinding;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuoteListView;
import com.example.yourfamouscoach.ui.model.QuotePresentation;
import com.example.yourfamouscoach.utils.StorageUtils;

class QuoteListItemViewHolder extends RecyclerView.ViewHolder {

    private QuoteItemBinding binding;

    public QuoteListItemViewHolder(@NonNull QuoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(QuotePresentation quotePresentation,IFavoritesQuoteListView view) {
        binding.tvQuotePreview.setText(quotePresentation.getQuote());
        binding.tvQuoteAuthorPreview.setText(quotePresentation.getAuthor());
        binding.ivDeleteSavedQuote.setOnClickListener(v -> {
            view.deleteClicked(quotePresentation);
        });
        binding.ivCopyTextQuote.setOnClickListener(v -> view.copyClicked(binding.tvQuotePreview.getText().toString(),binding.tvQuoteAuthorPreview.getText().toString()));
        binding.ivShareSavedQuote.setOnClickListener(v -> view.shareClicked(binding.tvQuotePreview.getText().toString(),binding.tvQuoteAuthorPreview.getText().toString()));
    }
}

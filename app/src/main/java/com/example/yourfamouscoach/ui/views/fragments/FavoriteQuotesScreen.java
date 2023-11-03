package com.example.yourfamouscoach.ui.views.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.yourfamouscoach.utils.StorageUtils.LOWER;
import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.FragmentSavedQuotesBinding;
import com.example.yourfamouscoach.ui.adapters.QuotesFavAdapter;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuoteListView;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuotesPresenter;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.model.QuotePresentation;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.yourfamouscoach.di.AppContainer;
import com.example.yourfamouscoach.di.FavoriteQuotesContainer;
import com.example.yourfamouscoach.di.MyApplication;
import com.squareup.picasso.Picasso;


public class FavoriteQuotesScreen extends Fragment implements IFavoritesView, IFavoritesQuoteListView {


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
        binding.rvFavQuotes.setAdapter(new QuotesFavAdapter(quotePresentationList,emojisList,requireContext(),this));
        binding.rvFavQuotes.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void copyText(String quoteTextToCopy, String authorTextToCopy) {
        ClipboardManager clipboardManager = (ClipboardManager) requireContext().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", "\"\"" + quoteTextToCopy + "\"\"" + "\n-" + authorTextToCopy));
        if (checkBuildPermissions(Build.VERSION_CODES.S_V2, LOWER)) {
            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareQuoteSaved(String quote,String author) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        Bitmap bitmap = createBitmap(quote,author);
        ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytesStream); // Comprimir el Bitmap en JPEG
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "quote", null);
        Uri imageUri = Uri.parse(path); //Usar lo mas actualizado que esta en hombe screen
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        requireActivity().startActivity(Intent.createChooser(intent, "Share Quote"));
    }


    private Bitmap createBitmap(String quote,String author){
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.share_item, null);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        TextView quoteTextToShareview = view.findViewById(R.id.tvQuoteShareItem);
        TextView authorTextToShareview = view.findViewById(R.id.tvAuthorShareItem);
        ImageView quoteImageToShareview = view.findViewById(R.id.ivLogoShareItem);
        quoteTextToShareview.setText(quote);
        authorTextToShareview.setText(author);
        Picasso.get().load(makeUrl(author)).into(quoteImageToShareview);
        Bitmap screenBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas screenView = new Canvas(screenBitmap);
        view.draw(screenView);
        return screenBitmap;
    }

    private String makeUrl(String author) {
        String modifyAuthorName = author;
        modifyAuthorName = modifyAuthorName.replace("-", "--").replace(".", "_").replace(" ", "-");
        return "https://zenquotes.io/img/" + modifyAuthorName.toLowerCase() + ".jpg";
    }

    @Override
    public void deleteClicked(QuotePresentation quotePresentation) {
        presenter.onDeleteClicked(quotePresentation);
    }

    @Override
    public void shareClicked(String quote, String author) {
        presenter.onShareClicked(quote,author);
    }

    @Override
    public void copyClicked(String quote,String author) {
        presenter.onCopyClicked(quote,author);
    }

}
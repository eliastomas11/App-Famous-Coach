package com.example.yourfamouscoach.ui.views.fragments;

import static android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY;

import static com.example.yourfamouscoach.utils.StorageUtils.GREATER;
import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;
import static com.example.yourfamouscoach.utils.StorageUtils.checkPermission;
import static com.example.yourfamouscoach.utils.StorageUtils.writeToFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.FragmentHomeScreenBinding;
import com.example.yourfamouscoach.ui.adapters.EmojiAdapter;
import com.example.yourfamouscoach.ui.interfaces.IHomePresenter;
import com.example.yourfamouscoach.ui.interfaces.IHomeView;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.example.yourfamouscoach.di.AppContainer;
import com.example.yourfamouscoach.di.MyApplication;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.example.yourfamouscoach.ui.views.widgets.QuoteWidget;
import com.example.yourfamouscoach.utils.StorageUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class HomeScreen extends Fragment implements IHomeView {

    private FragmentHomeScreenBinding binding;

    private IHomePresenter presenter;
    private AppContainer appContainer;
    private final ArrayList<Emojis> emojiList = new ArrayList<>();
    private float originalTextSize;

    private ActivityResultLauncher<String[]> permissionLauncher;
    private boolean readPermission = false;
    private boolean writePermission = false;

    private boolean isSaved = false;

    public HomeScreen() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContainer = ((MyApplication) requireActivity().getApplication()).appContainer;
        //launchPermissions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
        presenter = appContainer.providePresenter(this);
        originalTextSize = binding.tvQuote.getTextSize();
        presenter.onInitView();
        if(getArguments() == null){
            presenter.fetchData(true);
        }else{
            presenter.fetchData(false);
            presenter.onNotificationQuote(getArguments().getString("quote"), getArguments().getString("author"));
        }

        binding.clScreen.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                Log.i("EMOTION",activity.getEmotion());
                presenter.fetchSpecificQuote(activity.getEmotion());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(requireContext().getApplicationContext());
        RemoteViews remoteViews = new RemoteViews(requireContext().getApplicationContext().getPackageName(), R.layout.quote_widget);
        remoteViews.setTextViewText(R.id.appwidget_text, binding.tvQuote.getText());
        appWidgetManager.partiallyUpdateAppWidget(appWidgetManager.getAppWidgetIds(new ComponentName(requireContext().getApplicationContext(), QuoteWidget.class)), remoteViews);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Picasso.get().cancelRequest(binding.ivLogo);
        binding = null;
        presenter = null;
    }



    @Override
    public void showQuote(String quoteText, String quoteAuthor) {
        binding.cardView.setVisibility(View.VISIBLE);
        binding.tvQuote.setText("\"" + quoteText + "\"");
        binding.tvAuthor.setText(quoteAuthor);
    }


    @Override
    public void showProgressBar() {
        binding.pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void adaptText() {
        binding.tvQuote.post(() -> {
            int lineCount = binding.tvQuote.getLineCount();
            if (lineCount > 5) {
                float newTextSize = originalTextSize * 6 / lineCount;
                binding.tvQuote.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            } else {
                binding.tvQuote.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
            }
        });
        showTextAnimations();

    }

    @Override
    public void initViews() {
        initListeners();
    }

    @Override
    public void showTextAnimations() {
        YoYo.with(Techniques.FadeInUp).duration(500).playOn(binding.cardView);
        YoYo.with(Techniques.FadeInUp).duration(500).playOn(binding.tvQuote);
        YoYo.with(Techniques.FadeInUp).duration(500).playOn(binding.tvAuthor);
    }

    @Override
    public void showBuddha() {
        binding.ivLogo.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(1500).playOn(binding.ivLogo);
    }



    @Override
    public void showFavSaved() {
        binding.ivFav.setImageResource(R.drawable.baseline_favorite_24);
    }

    @Override
    public void showFavUnsaved() {
        binding.ivFav.setImageResource(R.drawable.fav_border);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideQuoteAndAuthorText() {
        binding.cardView.setVisibility(View.INVISIBLE);
        binding.tvAuthor.setText("");
        binding.tvQuote.setText("");
    }


    private void initListeners() {
        binding.clScreen.setOnClickListener(v -> {
           // presenter.fetchSpecificQuote(emojiList.get(binding.vpCarousel.getCurrentItem()).toString());
        });
        binding.ivFav.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) requireActivity();
            isSaved = !isSaved;
            presenter.onFavClicked(isSaved,binding.tvQuote.getText().toString(),
                    binding.tvAuthor.getText().toString(),mainActivity.getEmotion());
        });
        //binding.ivMenu.setOnClickListener(v -> presenter.onMenuClicked());
        binding.ivShareQuote.setOnClickListener(v -> presenter.onShareClicked());
    }

    private Bitmap getScreenBitmap() {
        Bitmap screenBitmap = Bitmap.createBitmap(binding.getRoot().getWidth(), binding.getRoot().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas screenView = new Canvas(screenBitmap);
        binding.getRoot().draw(screenView);
        return screenBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri saveScreenBitmap(Bitmap screenShot) {
        Uri imageUri;
        ContentResolver contentResolver = requireContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, UUID.randomUUID().toString() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "Image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + "Quotes_Shared");
        if (checkBuildPermissions(Build.VERSION_CODES.Q, GREATER)) {
            imageUri = contentResolver.insert(MediaStore.Images.Media.getContentUri(VOLUME_EXTERNAL_PRIMARY), contentValues);
            writeToFile(contentResolver, imageUri, screenShot);
        } else {
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            writeToFile(contentResolver, imageUri, screenShot);
        }
        return imageUri;
    }

    @Override
    public void launchPermissions() {
        String readPermissionRequest = Manifest.permission.READ_EXTERNAL_STORAGE;
        String writePermissionRequest = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        ArrayList<String> permissions = new ArrayList<>();
        if (checkPermission(readPermissionRequest, requireContext())) {
            readPermission = true;
        }
        if (checkPermission(writePermissionRequest, requireContext()) || checkBuildPermissions(Build.VERSION_CODES.Q, StorageUtils.GREATER)) {
            writePermission = true;
        }
        if (!readPermission) {
            permissions.add(readPermissionRequest);
        }
        if (!writePermission) {
            permissions.add(writePermissionRequest);
        }
        if (!permissions.isEmpty()) {
            permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    permission -> {
                        for (Map.Entry<String, Boolean> entry : permission.entrySet()) {
                            String perm = entry.getKey();
                            Boolean isGranted = entry.getValue();

                            if (isGranted) {

                            } else {

                            }
                        }
                    });
            permissionLauncher.launch(permissions.toArray(new String[0]));
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void shareQuote() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, saveScreenBitmap(getScreenBitmap()));
        requireActivity().startActivity(Intent.createChooser(intent, "Share Quote"));
    }

    @Override
    public void checkSavedState(Boolean savedState) {
        isSaved = savedState;
    }
    @Override
    public void showAuthorImage(String author,String quote){
        Picasso.get().load(makeUrl(author)).into(binding.ivLogo, new Callback() {
                    @Override
                    public void onSuccess() {
                        presenter.onImageLoad(author,quote);
                    }

                    @Override
                    public void onError(Exception e) {
                        binding.ivLogo.setImageResource(R.drawable.profile_pic_default_2);
                        presenter.onImageLoad(author,quote);
                    }
                });
    }

    public String makeUrl(String author){
        String modifyAuthorName = author;
        modifyAuthorName = modifyAuthorName.replace("-","--").replace(".","_").replace(" ","-");
        Log.i("AUTHOR",modifyAuthorName);
        return "https://zenquotes.io/img/" + modifyAuthorName.toLowerCase() + ".jpg";
    }
}
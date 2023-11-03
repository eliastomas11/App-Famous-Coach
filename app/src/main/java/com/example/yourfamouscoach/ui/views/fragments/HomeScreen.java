package com.example.yourfamouscoach.ui.views.fragments;

import static android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY;

import static com.example.yourfamouscoach.utils.AppConstants.ARG_AUTHOR_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.ARG_QUOTE_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.AUTHOR_SAVED_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.FAV_SAVED_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.QUOTE_SAVED_KEY;
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
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.FragmentHomeScreenBinding;
import com.example.yourfamouscoach.ui.interfaces.IHomePresenter;
import com.example.yourfamouscoach.ui.interfaces.IHomeView;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.example.yourfamouscoach.di.AppContainer;
import com.example.yourfamouscoach.di.MyApplication;
import com.example.yourfamouscoach.ui.views.activitys.HomeListener;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.example.yourfamouscoach.ui.views.widgets.QuoteWidget;
import com.example.yourfamouscoach.utils.StorageUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class HomeScreen extends Fragment implements IHomeView {

    private FragmentHomeScreenBinding binding;
    private IHomePresenter presenter;
    private AppContainer appContainer;
    private float originalTextSize;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private boolean readPermission = false;
    private boolean writePermission = false;

    private HomeListener homeListener;
    private boolean isSaved = false;

    public HomeScreen() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        homeListener = (HomeListener) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContainer = ((MyApplication) requireActivity().getApplication()).appContainer;
        presenter = appContainer.providePresenter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
        originalTextSize = binding.tvQuote.getTextSize();
        presenter.onInitView();
        if (savedInstanceState != null) {
            presenter.onRestore(savedInstanceState.getString(QUOTE_SAVED_KEY)
                    ,savedInstanceState.getString(AUTHOR_SAVED_KEY),
                    savedInstanceState.getBoolean(FAV_SAVED_KEY));
        }else{
            if (getArguments() == null) {
                presenter.fetchData();
            } else {
                //presenter.fetchData();
                presenter.onNotificationQuote(getArguments().getString(ARG_QUOTE_KEY), getArguments().getString(ARG_AUTHOR_KEY));
            }
        }

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
        presenter.onDestroy();
        binding = null;
        //presenter = null;
    }


    @Override
    public void showQuote(String quoteText, String quoteAuthor) {
        binding.cardView.setVisibility(View.VISIBLE);
        binding.tvQuote.setText(String.format("\"%s\"", quoteText));
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
    public void showAuthorImageAnimation() {
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
            presenter.fetchSpecificQuote(homeListener.getActualEmotion());
        });
        binding.ivFav.setOnClickListener(v -> {
            isSaved = !isSaved;
            presenter.onFavClicked(isSaved, binding.tvQuote.getText().toString(),
                    binding.tvAuthor.getText().toString(), homeListener.getActualEmotion());
        });
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
        if (checkPermission(writePermissionRequest, requireContext()) || checkBuildPermissions(Build.VERSION_CODES.Q, GREATER)) {
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    shareQuote();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Permission Needed to share", Toast.LENGTH_SHORT).show();
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
    public void showAuthorImage(String url, String quote, String author) {
        Picasso.get().load(url).into(binding.ivLogo, new Callback() {
            @Override
            public void onSuccess() {
                presenter.onImageLoad(quote, author);
            }

            @Override
            public void onError(Exception e) {
                presenter.onErrorImageLoad(quote, author);
            }
        });
    }

    @Override
    public void hideLoadingScreen() {
        homeListener.dataIsLoaded(false);
    }

    @Override
    public void showErrorAuthorImage() {
        binding.ivLogo.setImageResource(R.drawable.profile_pic_default_2);
    }

}
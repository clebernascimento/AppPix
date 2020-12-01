package com.itbam.pixceler.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.itbam.pixceler.R;
import com.itbam.pixceler.controller.RequestUserPermission;
import com.itbam.pixceler.databinding.ActivityMainBinding;
import com.itbam.pixceler.service.datasource.MainDataSource;
import com.itbam.pixceler.util.FullScreen;
import com.itbam.pixceler.view.fragment.HomeFragment;
import com.itbam.pixceler.view.fragment.ReadQrcodeFragment;
import com.itbam.pixceler.viewmodel.HomeViewModel;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FullScreen fullScreen = null;

    private MainDataSource dataSource;
    HomeViewModel viewModel;

    private int count = 0;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        adjustFontScale(getResources().getConfiguration());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        appCenter();
        components();
    }

    /**
     * Metodo para inicializar o fragmento principal
     */
    public void components() {
        /* get permission mobile for gps */
        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyStoragePermissions();

        dataSource = MainDataSource.getInstance();
        viewModel = new ViewModelProvider(getViewModelStore(), HomeViewModel.factory).get(HomeViewModel.class);
        viewModel.authenticate();

        fragments();

    }

    /**
     * Metodo para publicar no APPCenter
     */
    public void appCenter(){
        AppCenter.start(getApplication(), "c1626559-449c-41f0-898a-98b615ce4e01",
                Analytics.class, Crashes.class);
    }

    /**
     * Metodo para iniciar Fragmento Home
     */
    public void fragments() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, new HomeFragment(), HomeFragment.TAG)
                .commit();
    }

    /**
     * Metodo para Ajustar o size do dispositivo
     */
    public void adjustFontScale(Configuration configuration) {
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    /**
     * Metodo para o clique no botao nativo BACK ANDROID
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Fechar Aplicação!")
                .setMessage("Você deseja sair da aplicação? ")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}

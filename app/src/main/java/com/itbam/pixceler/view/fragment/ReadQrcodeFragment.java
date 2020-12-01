package com.itbam.pixceler.view.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itbam.pixceler.R;
import com.itbam.pixceler.controller.SessionController;
import com.itbam.pixceler.databinding.FragmentReadQrcodeBinding;
import com.itbam.pixceler.service.model.Order;
import com.itbam.pixceler.util.Formatters;
import com.itbam.pixceler.view.activity.MainActivity;
import com.itbam.pixceler.viewmodel.HomeViewModel;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONException;

import java.text.NumberFormat;


public class ReadQrcodeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "READ_FRAGMENT";

    FragmentReadQrcodeBinding binding;
    CompoundBarcodeView qrReader;

    HomeViewModel viewModel;
    MainActivity activity;

    private SessionController session;
    private NumberFormat nf = Formatters.numFormat();

    public ReadQrcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_read_qrcode, container, false);

        setComponents();

        return binding.getRoot();
    }

    /**
     * Metodo para inicializar os componentes
     */
    public void setComponents(){

        session = SessionController.getInstance(getContext());
        updateBalance();

        initCamera();
        activity = ((MainActivity)getActivity());
        binding.btnBack.setOnClickListener(this);
        binding.tryAgain.setOnClickListener(this);
        configureObservers();
    }


    private void updateBalance() {
        binding.editValue.setText(nf.format(session.getBalance()));
    }

    private void configureObservers() {
        viewModel = new ViewModelProvider(
                activity.getViewModelStore(),
                HomeViewModel.factory
        ).get(HomeViewModel.class);

        viewModel.getCloseOrderLiveData().observe(
                activity,
                status -> {
                    if (status) {
                        onCloseOrder();

                    }
                }
        );

        viewModel.getCloseOrderErrorLiveData().observe(
                activity,
                status -> {
                    if (status) {
                        viewModel.setMutables();
                        qrReader.resume();
                        binding.tryAgain.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void onCloseOrder() {
        if (!Order.flag) {
            new Handler().postDelayed(this::goBack, 500);
            Order.flag = true;
            session.remove((float) Order.total);
            Toast.makeText(activity, "Pago com sucesso", Toast.LENGTH_LONG).show();
            viewModel.setMutables();
        }
    }

    /**
     * Metodo para click
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                goBack();
                break;
            case R.id.tryAgain:
                binding.tryAgain.setVisibility(View.GONE);
                qrReader.resume();
                break;
        }
    }

    /**
     * Metodo para iniciar camera
     */
    public void initCamera(){
        qrReader = binding.cameraReader;
        qrReader.setStatusText("Scannear o QRCode");
        qrReader.decodeContinuous(result -> {
            playBeep();
            Log.e("data", result.getText());
            try {
                qrReader.pause();
                Order.setOrder(result.getText());
                if (session.getBalance() < Order.total) {
                    Toast.makeText(getContext(), "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                    binding.tryAgain.setVisibility(View.VISIBLE);
                } else {
                    viewModel.closeOrder(result.getText());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        qrReader.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        qrReader.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Metodo para Beep do scanner
     */
    private void playBeep() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP,500);
    }

    /**
     * Metodo para voltar a tela home
     */
    private void goBack() {
        activity.getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
        Order.flag = false;
    }
}
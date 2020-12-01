package com.itbam.pixceler.view.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.zxing.integration.android.IntentIntegrator;
import com.itbam.pixceler.R;
import com.itbam.pixceler.controller.SessionController;
import com.itbam.pixceler.databinding.FragmentHomeBinding;
import com.itbam.pixceler.service.datasource.MainDataSource;
import com.itbam.pixceler.util.Formatters;
import com.itbam.pixceler.view.activity.MainActivity;

import net.glxn.qrgen.android.QRCode;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    FragmentHomeBinding binding;
    public static final String TAG = "HOME_FRAGMENT";

    private SessionController session;
    private NumberFormat nf = Formatters.numFormat();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        setComponents();

        return binding.getRoot();
    }

    /**
     * Metodo para inicializar os componentes
     */
    private void setComponents() {

        session = SessionController.getInstance(getContext());
        updateBalance();
        binding.layoutPay.setOnClickListener(this);
        binding.layoutToReceiver.setOnClickListener(this);
        getQRCode();
    }

    private void updateBalance() {
        binding.editValue.setText(nf.format(session.getBalance()));
    }

    /**
     * Metodo para click
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_pay:

                if (MainDataSource.getInstance().getApi() != null) {

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new ReadQrcodeFragment(), ReadQrcodeFragment.TAG)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Sem Conexão", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.layout_toReceiver:
                session.automaticRecharge();
                updateBalance();
                break;
        }
    }

    /**
     * Metodo para gerar QRCode
     */
    public void getQRCode(){
        Bitmap bitmap = QRCode.from("PIX-CELER, Pablito El Chapo, Chave: 268.962.080-40, Banco: Ag-00001 Conta-12345-6").withSize(250, 250).bitmap();
//        bitmap.eraseColor(ContextCompat.getColor(this, R.color.blue));
        binding.imgQrCode.setImageBitmap(bitmap);
    }
}
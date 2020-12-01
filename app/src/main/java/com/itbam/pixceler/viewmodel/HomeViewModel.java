package com.itbam.pixceler.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.itbam.pixceler.service.datasource.MainDataSource;
import com.itbam.pixceler.service.model.AuthResponse;
import com.itbam.pixceler.service.model.Order;
import com.itbam.pixceler.service.model.OrderCloseDTO;
import com.itbam.pixceler.view.activity.MainActivity;

import org.json.JSONException;

import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
public class HomeViewModel extends ViewModel {

    private boolean statusSuccess;
    private boolean statusError;

    private AuthResponse auth;
    private MutableLiveData<AuthResponse> authLiveData;
    private MutableLiveData<Boolean> closeOrderLiveData;
    private MutableLiveData<Boolean> closeOrderErrorLiveData;
    private MainDataSource dataSource;

    public static ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == HomeViewModel.class) {
                return (T) new HomeViewModel();
            }
            return null;
        }
    };

    public HomeViewModel() {
        dataSource = MainDataSource.getInstance();
        authLiveData = new MutableLiveData<>();
        closeOrderLiveData = new MutableLiveData<>();
        closeOrderErrorLiveData = new MutableLiveData<>();
    }

    public void setMutables() {
        closeOrderLiveData = new MutableLiveData<>();
        closeOrderErrorLiveData = new MutableLiveData<>();
    }

    public void authenticate() {
        dataSource.authenticate(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    dataSource.createBearerService(response.body().getAccess_token());
                    dataSource.setApi();
                    authLiveData.postValue(response.body());
                } else {
                    authLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                authLiveData.postValue(null);
            }
        });
    }

    public void closeOrder(String qrCodeData) throws JSONException {
        Order.setOrder(qrCodeData);
        dataSource.closeOrder(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    statusSuccess = true;
                    closeOrderLiveData.postValue(statusSuccess);
                } else {
                    Log.e("closeOder()", "error --> " + response.message());
                    statusError = true;
                    closeOrderErrorLiveData.postValue(statusError);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("closeOder()", "onFailure --> " + t.getMessage());
                statusError = true;
                closeOrderErrorLiveData.postValue(statusError);
            }
        });
    }

}

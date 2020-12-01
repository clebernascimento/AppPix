package com.itbam.pixceler.service.datasource;

import com.itbam.pixceler.BuildConfig;
import com.itbam.pixceler.service.api.ApiController;
import com.itbam.pixceler.service.model.AuthResponse;
import com.itbam.pixceler.service.model.OrderCloseDTO;

import retrofit2.Call;
import retrofit2.Callback;

public class MainDataSource extends BaseDataSource {

    private static MainDataSource instance;
    private ApiController api;
    private ApiController authApi = retrofitBasic.create(ApiController.class);

    private static final String CLIENT = BuildConfig.CLIENT;
    private static final String GRANT_TYPE = BuildConfig.GRANT_TYPE;
    private static final String USERNAME = BuildConfig.SERIAL_NUMBER;
    private static final String PASSWORD = BuildConfig.PASSWORD;

    public static MainDataSource getInstance() {
        if (instance == null) {
            instance = new MainDataSource();
        }
        return instance;
    }

    public MainDataSource() { }

    public void setApi() {
        this.api = retrofit.create(ApiController.class);
    }

    public ApiController getApi() {
        return api;
    }

    public void authenticate(Callback<AuthResponse> callback) {
        Call<AuthResponse> call = authApi.authenticate(
                CLIENT,
                GRANT_TYPE,
                USERNAME,
                PASSWORD
        );
        call.enqueue(callback);
    }

    public void closeOrder(Callback<Void> callback) {
        OrderCloseDTO closeDTO = new OrderCloseDTO();
        Call<Void> call = api.closeOrder(closeDTO);
        call.enqueue(callback);
    }

}

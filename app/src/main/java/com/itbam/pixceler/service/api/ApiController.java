package com.itbam.pixceler.service.api;

import com.itbam.pixceler.service.model.AuthResponse;
import com.itbam.pixceler.service.model.OrderCloseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiController {

    @PUT("/orders/close")
    Call<Void> closeOrder(@Body OrderCloseDTO paymentDTO);

    @POST("/oauth/token")
    Call<AuthResponse> authenticate(
            @Query("client") String client,
            @Query("grant_type") String grantType,
            @Query("username") String name,
            @Query("password") String password
    );

}

package com.ua.riks.Retrofit;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {
    @POST("adduser")
    @FormUrlEncoded
    Call<String> adduser(@Field("email") String email,
                                  @Field("username") String username,
                                  @Field("displayname") String displayname,
                                  @Field("cnic") String cnic,
                                  @Field("address") String address,
                                  @Field("contactno") String contactno,
                                  @Field("profilepicname") String profilepicname,
                                  @Field("password") String password,
                                  @Field("type") String type,
                                  @Field("registrationtoken") String registrationtoken);

    @POST("authenticate")
    @FormUrlEncoded
    Call<String> authenticate(@Field("email") String email,
                                  @Field("password") String password);

    @POST("updateattribute")
    @FormUrlEncoded
    Call<String> updateattribute(@Field("email") String email,
                              @Field("attributename") String attributename,
                                 @Field("newvalue") String newvalue);

    @POST("deleteUser")
    @FormUrlEncoded
    Call<String> deleteUser(@Field("email") String email);

    @GET("allPackages")
    Call<String> allPackages();

    @POST("addNewPackage")
    @FormUrlEncoded
    Call<String> addNewPackage(@Field("packageId") String packageId,
                                 @Field("packagename") String packagename,
                                 @Field("picurl") String picUrl,
                                 @Field("description") String description);

    @POST("updatePackage")
    @FormUrlEncoded
    Call<String> updatePackage(@Field("packageId") String packageId,
                                 @Field("attributename") String attributename,
                                 @Field("newvalue") String newvalue);

    @GET("allUsers")
    Call<String> allUsers();

    @GET("allEmployees")
    Call<String> allEmployees();

    @POST("generateRegistrationToken")
    @FormUrlEncoded
    Call<String> generateRegistrationToken(@Field("email") String email);

    @POST("addinProcessPackage")
    @FormUrlEncoded
    Call<String> addinProcessPackage(@Field("packageId") String packageId,
                               @Field("userId") int userId);

    @POST("addNewPackageRegisterationComplete")
    @FormUrlEncoded
    Call<String> addNewPackageRegisterationComplete(@Field("inprocessPackageId") int userId);

    @POST("allInProcessPackages")
    @FormUrlEncoded
    Call<String> allInProcessPackages(@Field("userId") int userId,
                                      @Field("type") String type);

    @POST("allRegisteredPackages")
    @FormUrlEncoded
    Call<String> allRegisteredPackages(@Field("userId") int userId);

    @POST("fileUrlUpdate")
    @FormUrlEncoded
    Call<String> fileUrlUpdate(@Field("userId") int userId,
                                      @Field("fileUrl") String fileUrl);

    @POST("getfileUrl")
    @FormUrlEncoded
    Call<String> getfileUrl(@Field("userId") int userId);

}

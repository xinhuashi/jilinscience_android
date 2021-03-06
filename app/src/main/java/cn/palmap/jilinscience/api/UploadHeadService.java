package cn.palmap.jilinscience.api;

import cn.palmap.jilinscience.model.ApiCode;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by stone on 2017/5/15.
 */

public interface UploadHeadService {
    @Multipart
    @POST("/jlsci/upload/uploadHeadImage")
    Call<ApiCode> uploadImage(@Header("authentication-customer") String customer,
                              @Part("file\"; filename=\"image.png\"") RequestBody imgs);
}

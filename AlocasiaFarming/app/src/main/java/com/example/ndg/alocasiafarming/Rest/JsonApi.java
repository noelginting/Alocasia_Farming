package com.example.ndg.alocasiafarming.Rest;

import com.example.ndg.alocasiafarming.Model.CatatanModel;
import com.example.ndg.alocasiafarming.Model.HistoryModel;
import com.example.ndg.alocasiafarming.Model.LoginModel;
import com.example.ndg.alocasiafarming.Model.PenanamanModel;
import com.example.ndg.alocasiafarming.Model.PupukModel;
import com.example.ndg.alocasiafarming.Model.StatusModel;
import com.example.ndg.alocasiafarming.Model.TanamModel;
import com.example.ndg.alocasiafarming.Model.TumbuhanModel;

import org.w3c.dom.Text;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonApi {
    @GET("penanamanjson.php")
    Call<TanamModel> getPenanaman(@Query("tipe") int tipe);

    @FormUrlEncoded
    @POST("pesanmqtt.php")
    Call<ResponseBody> createPost(@Field("idtanam") String idtanam,
                                  @Field("status") String status
        );
    @GET("status.php")
    Call<StatusModel> getStatus(@Query("tipe") int tipe);

    @GET("historyjson.php")
    Call<HistoryModel> getHistory(@Query("typedata") int typedata,
                                  @Query("id_tanam") String id_tanam);
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginModel> createLogin(@Field("username") String username,
                               @Field("password") String password);
    @GET("selectdata.php")
    Call<PenanamanModel> getDataPenanaman(@Query("id") int id);

    @GET("selectdata.php")
    Call<TumbuhanModel> getDataTumbuhan(@Query("id") int id);

    @GET("selectdata.php")
    Call<PupukModel> getDataPupuk(@Query("id") int id);

    @GET("datachart.php")
    Call<HistoryModel> getChart(@Query("id") int id,
                                @Query("offset") int offset,
                                @Query("id_tanam") String id_tanam);
    @GET("datachart.php")
    Call<HistoryModel> getChartJam(@Query("id") int id,
                                @Query("offset") int offset,
                                @Query("id_tanam") String id_tanam,
                                @Query("tanggal") String tanggal);
    @GET("datachart.php")
    Call<HistoryModel> getChartMenit(@Query("id") int id,
                                   @Query("offset") int offset,
                                   @Query("id_tanam") String id_tanam,
                                   @Query("tanggal") String tanggal,
                                     @Query("jam") String jam);
    @GET("catatan.php")
    Call<CatatanModel> getCatatan(@Query("id_tanam") String id_tanam);

    @FormUrlEncoded
    @POST("penanaman.php")
    Call<ResponseBody> createPostPenanaman(@Field("aksi") int aksi,
                                           @Field("id_tanam") String id_tanam,
                                           @Field("id_tumbuhan") int id_tumbuhan,
                                           @Field("tgl_penanaman") String tgl_penanaman
                                           );
    @FormUrlEncoded
    @POST("tumbuhan.php")
    Call<ResponseBody> createPostTumbuhan(@Field("aksi") int aksi,
                                         @Field("id_tumbuhan") Integer id_tumbuhan,
                                         @Field("nama_tumbuhan") String nama_tumbuhan,
                                         @Field("nama_gambar") String nama_gambar);
    @FormUrlEncoded
    @POST("pupuk.php")
    Call<ResponseBody> createPostPupuk(@Field("aksi") int aksi,
                                       @Field("id_pupuk") Integer id_pupuk,
                                       @Field("umur_tumbuhan") int umur_tumbuhan,
                                       @Field("id_tumbuhan") int id_tumbuhan,
                                       @Field("nilai_ec") int nilai_ec);
    @FormUrlEncoded
    @POST("tumbuhan.php")
    Call<ResponseBody> deleteTumbuhan(@Field("aksi") int aksi,
                                      @Field("id_tumbuhan") int tumbuhan);
    @FormUrlEncoded
    @POST("pupuk.php")
    Call<ResponseBody> deletePupuk(@Field("aksi") int aksi,
                                   @Field("id_pupuk") int id_pupuk);
    @FormUrlEncoded
    @POST("penanaman.php")
    Call<ResponseBody> deletePenanaman(@Field("aksi") int aksi,
                                       @Field("id_tanam") String penanaman);
    @FormUrlEncoded
    @POST("catatan.php")
    Call<ResponseBody> createPostCatatan(@Field("id_tanam") String id_tanam,
                                        @Field("catatan") String catatan,
                                        @Field("penanganan") String penanganan);
    @FormUrlEncoded
    @POST("status.php")
    Call<ResponseBody> createSelesai(@Field("id_tanam") String id_tanam);
}

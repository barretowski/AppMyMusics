package br.unoeste.appmymusics.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicService {
    @GET("search.php")
    Call<Letra> buscarLetra(@Query("apikey") String apikey, @Query("busca") String artista , @Query("musica") String musica);
}

package cl.sebastianmena.funnytrivia.network;

import java.util.List;

import cl.sebastianmena.funnytrivia.models.Trivia;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sebastián Mena on 31-10-2017.
 */

public interface GetTrivia {

    @GET("random/?type=trivia&fragment=true&json=true")
    Call<Trivia> get(@Query("min") int min,@Query("max") int max);

}

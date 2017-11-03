package cl.sebastianmena.funnytrivia.network;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sebastián Mena on 31-10-2017.
 */

public class TriviaInterceptor {

    public static final String BASE_URL = "https://numbersapi.p.mashape.com/";

    public GetTrivia get() {

//        ConnectionSpec spec = new
//                ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_2)
//                .build();

        HttpLoggingInterceptor log_interceptor = new HttpLoggingInterceptor();
        log_interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                //.connectionSpecs(Collections.singletonList(spec))
                .addInterceptor(log_interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        try {
            URL url = new URL(BASE_URL);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);
            httpClient.sslSocketFactory(NoSSLv3Factory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request request = originalRequest.newBuilder()
                    /*Common headers*/
                        //.header("Accept-Version", "v1")
                        .header("X-Mashape-Key", "gOpxYLLk8PmshmPHoqdUu5wiq2jRp1ANrjKjsnNZ3P06iWtdt1")
                        .header("Accept", "application/json")
                        .build();

                Response response = chain.proceed(request);

            /*If the request fail then you get 3 retrys*/
                int retryCount = 0;
                while (!response.isSuccessful() && retryCount < 3) {
                    retryCount++;
                    response = chain.proceed(request);
                }

                return response;
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit interceptor = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        GetTrivia request = interceptor.create(GetTrivia.class);

        return request;



    }

}

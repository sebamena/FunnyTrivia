package cl.sebastianmena.funnytrivia.background;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.sebastianmena.funnytrivia.models.Trivia;
import cl.sebastianmena.funnytrivia.network.GetTrivia;
import cl.sebastianmena.funnytrivia.network.TriviaInterceptor;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Sebastián Mena on 31-10-2017.
 */

public class GetTriviaTask extends AsyncTask<Void, Void, List<Trivia>> {
    @Override
    protected List<Trivia> doInBackground(Void... voids) {

        GetTrivia request = new TriviaInterceptor().get();
        Call<Trivia> call = request.get(1, 100);

        List<Trivia> list = new ArrayList<>();


        try {
            Response<Trivia> response = call.execute();

            Log.d("Code:", String.valueOf(response.code()));

            if (response.code() == 200 && response.isSuccessful()) {

                try {

                    String text = response.body().getText();
                    int number = response.body().getNumber();

                    Trivia trivia_ = new Trivia(text, number);

                    list.add(trivia_);

                    if (list.size() > 0) {

                        for (Trivia trivia : list) {
                            Log.d("Question:", trivia.getText());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }
}

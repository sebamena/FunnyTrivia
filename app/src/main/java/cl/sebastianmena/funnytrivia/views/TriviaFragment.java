package cl.sebastianmena.funnytrivia.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.sebastianmena.funnytrivia.R;
import cl.sebastianmena.funnytrivia.adapter.TriviaAdapter;
import cl.sebastianmena.funnytrivia.background.GetTriviaTask;
import cl.sebastianmena.funnytrivia.models.Trivia;

import static android.content.Context.TELEPHONY_SERVICE;


public class TriviaFragment extends Fragment {

    private TriviaAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private boolean pendingRequest = false;
    private TextView titleTvActivity;
    private RecyclerView recyclerView;

    public TriviaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        return lf.inflate(R.layout.fragment_trivia, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTvActivity = (TextView) view.findViewById(R.id.titleTv);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.reloadSrl);
        recyclerView = (RecyclerView) view.findViewById(R.id.triviasRv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TriviaAdapter();
        recyclerView.setAdapter(adapter);

        if (isOnline()) {
            okinternet();
            new GetTrivias().execute();
        } else {
            badinternet();
        }

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pendingRequest = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (isOnline()) {
                            okinternet();
                            new GetTrivias().execute();
                        } else {
                            badinternet();
                            refreshLayout.setRefreshing(false);
                        }

                        TelephonyManager mngr = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
                        String imei = mngr.getDeviceId();
                        String datacel = Build.MODEL + "+" + String.valueOf(Build.VERSION.SDK_INT) + "+" + imei;
                        String date_start = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

                        Answers.getInstance().logCustom(new CustomEvent("PLAY")
                                .putCustomAttribute("DATE-INFO", date_start + datacel)
                        );

                    }
                }, 100);
            }
        });

    }

    private class GetTrivias extends GetTriviaTask {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Searching questions, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Trivia> trivias) {
            pendingRequest = false;
            adapter.update(trivias);
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void okinternet() {
        titleTvActivity.setText("*Click in the question to show the answer: \n\n *Swipe down the question to reload!");
    }

    public void badinternet() {
        titleTvActivity.setText("You need an internet connection to download the questions.");
    }
}

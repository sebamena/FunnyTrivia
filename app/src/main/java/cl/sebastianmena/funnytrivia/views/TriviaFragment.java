package cl.sebastianmena.funnytrivia.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cl.sebastianmena.funnytrivia.R;
import cl.sebastianmena.funnytrivia.adapter.TriviaAdapter;
import cl.sebastianmena.funnytrivia.background.GetTriviaTask;
import cl.sebastianmena.funnytrivia.models.Trivia;


public class TriviaFragment extends Fragment {

    private TriviaAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private boolean pendingRequest = false;

    public TriviaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trivia, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.reloadSrl);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.triviasRv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TriviaAdapter();
        recyclerView.setAdapter(adapter);

        new GetTrivias().execute();

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pendingRequest = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetTrivias().execute();

                    }
                },100);
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
}

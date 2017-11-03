package cl.sebastianmena.funnytrivia.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cl.sebastianmena.funnytrivia.R;
import cl.sebastianmena.funnytrivia.models.Trivia;

/**
 * Created by Sebastián Mena on 31-10-2017.
 */

public class TriviaAdapter extends RecyclerView.Adapter<TriviaAdapter.ViewHolder> {

    private List<Trivia> triviaList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trivia_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Trivia trivia = triviaList.get(position);

        holder.questionTv.setText(trivia.getText());
        holder.answerTv.setVisibility(View.INVISIBLE);
        holder.answerTv.setText(String.valueOf(trivia.getNumber()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.answerTv.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return triviaList.size();
    }

    public void update(List<Trivia> trivias) {
        triviaList.clear();
        triviaList.addAll(trivias);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView questionTv,answerTv;

        public ViewHolder(View itemView) {
            super(itemView);

            questionTv = (TextView) itemView.findViewById(R.id.questionTv);
            answerTv = (TextView) itemView.findViewById(R.id.answerTv);
        }
    }
}

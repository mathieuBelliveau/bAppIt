package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class HighScoresActivity extends AppCompatActivity {
   // private ArrayList<HighScoreCard> mHScoreCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        ArrayList<HighScoreCard> mHScoreCards = getHiScores();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.hi_score_list);
        mRecyclerView.setAdapter(new HiScoreRecyclerViewAdapter(mHScoreCards));

        //grab from JSON file storing our high scores.

    }

    private ArrayList<HighScoreCard> getHiScores()
    {
        HighScoreCard h1 = new HighScoreCard("Suzannah", 32);
        HighScoreCard h2 = new HighScoreCard("Mathieu", 29);
        HighScoreCard h3 = new HighScoreCard("Gabe", 30);

        ArrayList<HighScoreCard> hcd = new ArrayList<HighScoreCard>();//first position is rank 1
        hcd.add(h1);
        hcd.add(h2);
        hcd.add(h3);

        Collections.sort(hcd);//sorts

        return hcd;
    }


    public class HiScoreRecyclerViewAdapter
            extends RecyclerView.Adapter<HiScoreRecyclerViewAdapter.ViewHolder>
    {
        private final ArrayList<HighScoreCard> hScoreCardList;

        public HiScoreRecyclerViewAdapter(ArrayList<HighScoreCard> hsc)
        {
            hScoreCardList = hsc;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hs_card, parent, false);
            return new ViewHolder(view);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView name;
            public final TextView score;
            public final TextView rank;
            public HighScoreCard mHighScoreCard;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.name);
                score = (TextView) view.findViewById(R.id.score);
                rank = (TextView) view.findViewById(R.id.rank);
            }

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mHighScoreCard = hScoreCardList.get(position);
            holder.name.setText(hScoreCardList.get(position).getName());
            holder.score.setText(hScoreCardList.get(position).getScore());
            holder.rank.setText(position);//position in a sorted list
        }

        @Override
        public int getItemCount() {
            return hScoreCardList.size();
        }




    }


}

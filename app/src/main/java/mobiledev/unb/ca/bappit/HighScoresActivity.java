package mobiledev.unb.ca.bappit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HighScoresActivity extends AppCompatActivity {
   // private ArrayList<HighScoreCard> mHScoreCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        ArrayList<HighScoreCard> mHScoreCards = getHiScores();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.hi_score_list);
        mRecyclerView.setAdapter(new HiScoreRecyclerAdapter(mHScoreCards));

        //grab from JSON file storing our high scores.

    }

    protected ArrayList<HighScoreCard> getHiScores()
    {
        HighScoreCard h1 = new HighScoreCard("Suzannah", 32);
        HighScoreCard h2 = new HighScoreCard("Mathieu", 29);

        ArrayList<HighScoreCard> hcd = new ArrayList<HighScoreCard>();
        hcd.add(h1);
        hcd.add(h2);

        return hcd;
    }


    public class HiScoreRecyclerAdapter extends RecyclerView.Adapter<HiScoreRecyclerAdapter.ViewHolder>
    {
        private final ArrayList<HighScoreCard> mHSCards;

        public HiScoreRecyclerAdapter(ArrayList<HighScoreCard> hsc)
        {
            mHSCards = hsc;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hi_score_card, parent, false);
            return new ViewHolder(view);
        }


        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final TextView name;
            public final TextView score;
            public final TextView rank;
            public HighScoreCard mHighScoreCard;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.Name);
                score = (TextView) view.findViewById(R.id.score);
                rank = (TextView) view.findViewById(R.id.Rank);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }



            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                holder.mHighScoreCard = mHSCards.get(position);
                holder.name.setText(mHSCards.get(position).getName());
                holder.score.setText(mHSCards.get(position).getScore());
                holder.rank.setText(mHSCards.get(position));

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * Setting the data to be sent to the Detail portion of the template.
                         * Here, we send the title, longitude, and latitude of the Earthquake
                         * that was clicked in the RecyclerView. The Detail Activity/Fragment
                         * will then display this information. Condition check is whether we
                         * are twoPane on a Tablet, which varies how we pass arguments to the
                         * participating activity/fragment.
                         */
                        String title = holder.mGeoData.title;
                        String lng = holder.mGeoData.longitude;
                        String lat = holder.mGeoData.latitude;
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putString(GeoDataDetailFragment.TITLE, title);
                            arguments.putString(GeoDataDetailFragment.LNG, lng);
                            arguments.putString(GeoDataDetailFragment.LAT, lat);
                            GeoDataDetailFragment fragment = new GeoDataDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.geodata_detail_container, fragment)
                                    .commit();
                        } else {
                            Intent intent = new Intent(GeoDataListActivity.this, GeoDataDetailActivity.class);
                            Bundle args = new Bundle();
                            args.putString(GeoDataDetailFragment.TITLE, title);
                            args.putString(GeoDataDetailFragment.LNG, lng);
                            args.putString(GeoDataDetailFragment.LAT, lat);
                            intent.putExtras(args);
                            startActivity(intent);
                            // TODO Create an Intent to start GeoDataDetailActivity. You'll need
                            // to add some extras to this intent. Look at that class, and the
                            // example Fragment transaction for the two pane case above, to
                            // figure out what you need to add.

                        }
                    }
                });
            }


        }

    }


}

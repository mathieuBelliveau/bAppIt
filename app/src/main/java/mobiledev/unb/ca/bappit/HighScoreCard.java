package mobiledev.unb.ca.bappit;

/**
 * Created by win10-ads on 2/17/2018.
 */

public class HighScoreCard implements Comparable<HighScoreCard>
{
    private String name;
    private int score;

    public HighScoreCard(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }

    @Override
    public int compareTo(HighScoreCard hs)
    {
        return hs.score - this.score;//Descending order
    }
}

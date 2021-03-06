package modelPack;

import java.util.ArrayList;

import javax.websocket.Session;

public class User
{
    private String key;
    private String name;

    private int wishPlayerCount = 0;

    private boolean isFirst = false;
    private boolean isOlympic = true;
    private boolean isShowHint = false;

    private int miss;
    private int win;

    private int winCount;
    private int loseCount;
    private int drawCount;
    private int highScore;
    private int timeOutCnt = 0;

    private boolean isResultWatch = false;

    private ArrayList<Panel> selectedPanel = new ArrayList<Panel>();

    public static final int NONE = 0;
    public static final int DRAW = -1;
    public static final int WIN = 1;
    public static final int LOSE = 2;

    public Session session = null;

    public int getTotalScoreBase()
    {
        int scoreBase = 0;

        for (Panel panel : selectedPanel)
        {
            Word word = panel.getSelectedWord();
            scoreBase += word.getBaseScore();
        }

        return scoreBase;
    }

    public int getTotalScoreBonus()
    {
        int scoreBonus = 0;

        for (Panel panel : selectedPanel)
        {
            Word word = panel.getSelectedWord();
            scoreBonus += word.getBonusScore();
        }

        return scoreBonus;
    }

    public double getScoreMultiple()
    {
        double scoreMulti = 1.0;

        if (isShowHint)
        {
            scoreMulti *= 0.5;
        }

        return scoreMulti;
    }

    public String getScoreExam()
    {
        return getTotalScoreBase() + " + [" + getTotalScoreBonus() + "] * " + getScoreMultiple() + " = " + getScore();
    }

    public User()
    {
        key = "";
        name = "";
        miss = 0;
        win = -1;
        isResultWatch = false;
        isFirst = false;
    }

    public boolean isFirst()
    {
        return isFirst;
    }

    public void setFirst(boolean isFirst)
    {
        this.isFirst = isFirst;
    }

    public boolean isOlympic()
    {
        return isOlympic;
    }

    public void setOlympic(boolean isOlympic)
    {
        this.isOlympic = isOlympic;
    }

    public User(String key, String name)
    {
        this.key = key;
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getWishPlayerCount()
    {
        return wishPlayerCount;
    }

    public void setWishPlayerCount(int wishPlayerCount)
    {
        this.wishPlayerCount = wishPlayerCount;
    }

    public int getScore()
    {
        return (int) ((getTotalScoreBase() + getTotalScoreBonus()) * getScoreMultiple());
    }

    public int getMiss()
    {
        return miss;
    }

    public void setMiss(int miss)
    {
        this.miss = miss;
    }

    public void addMiss()
    {
        this.miss++;
    }

    public int getWin()
    {
        return win;
    }

    public void setWin(int win)
    {
        this.win = win;
    }

    public int getWinCount()
    {
        return winCount;
    }

    public void setWinCount(int winCount)
    {
        this.winCount = winCount;
    }

    public int getLoseCount()
    {
        return loseCount;
    }

    public void setLoseCount(int loseCount)
    {
        this.loseCount = loseCount;
    }

    public int getDrawCount()
    {
        return drawCount;
    }

    public void setDrawCount(int drawCount)
    {
        this.drawCount = drawCount;
    }

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public boolean isResultWatch()
    {
        return isResultWatch;
    }

    public void setResultWatch(boolean isResultWatch)
    {
        this.isResultWatch = isResultWatch;
    }

    public boolean isShowHint()
    {
        return isShowHint;
    }

    public void setShowHint(boolean isShowHint)
    {
        this.isShowHint = isShowHint;
    }

    public void resetTimeOut()
    {
        timeOutCnt = 0;
    }

    public boolean isTimeOut()
    {
        return timeOutCnt >= 1;
    }

    public boolean isTimeOutTwice()
    {
        return timeOutCnt >= 2;
    }

    public void addTimeOutCnt()
    {
        timeOutCnt++;
    }

    public ArrayList<Panel> getSelectedPanel()
    {
        return selectedPanel;
    }

    public void setSelectedPanel(ArrayList<Panel> selectedPanel)
    {
        this.selectedPanel = selectedPanel;
    }

    public void addSelectedPanel(Panel panel)
    {
        selectedPanel.add(panel);
    }

}

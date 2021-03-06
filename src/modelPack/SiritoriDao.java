package modelPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Utility.GameSetting;

/**
 * データベース:PaneTori に対して処理を行うDAOクラスです
 */
public class SiritoriDao
{
    /**
     * DB接続情報
     */
    private Connection connection;

    public SiritoriDao() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");

        String server = GameSetting.SERVER_ADDRESS;
        String database = GameSetting.DB_DATABASE;
        String user = GameSetting.DB_USER;
        String password = GameSetting.DB_PASS;
        String encoding = GameSetting.DB_ENCOFING;

        String strConn = "jdbc:mysql://" + server + "/" + database + "?user=" + user + "&password=" + password + "&useUnicode=true&characterEncoding=" + encoding;

        connection = DriverManager.getConnection(strConn);
    }

    public void close()
    {
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ゲームIDの最大値を取得します
     * 
     * @return ゲームIDの最大値
     * @throws SQLException
     */
    public int getMaxMatchNumber() throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        int ret = 0;

        try
        {
            String sql = ""
                    + " SELECT MAX(id) as max"
                    + "   FROM tbl_play_result ";

            pstatement = connection.prepareStatement(sql);
            rs = pstatement.executeQuery();
            if (rs.next())
            {
                ret = rs.getInt("max");
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * ユーザキーを元に、登録されたユーザ情報を取得します
     * 
     * @param key
     *            ユーザキー
     * @return 取得した情報を収めたUserクラス
     * @throws SQLException
     */
    public User selectUser(String key) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        User ret = null;

        try
        {
            String sql = ""
                    + " SELECT user_key, "
                    + "        name "
                    + "   FROM tbl_user "
                    + "  WHERE user_key = ? ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, key);
            rs = pstatement.executeQuery();
            if (rs.next())
            {
                ret = new User(rs.getString("user_key"), rs.getString("name"));
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * 対象ユーザをDBに登録します
     * 
     * @param user
     *            対象ユーザ
     * @throws SQLException
     */
    public void insertUser(User user) throws SQLException
    {
        PreparedStatement pstatement = null;

        try
        {
            String sql = ""
                    + " INSERT INTO tbl_user "
                    + "             (user_key, name) "
                    + "      VALUES (?, ?) ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, user.getKey());
            pstatement.setString(2, user.getName());
            pstatement.execute();
        }
        finally
        {
            pstatement.close();
        }
    }

    /**
     * 対象ユーザの情報でDBへ更新をかけます
     * 
     * @param user
     *            対象ユーザ
     * @throws SQLException
     */
    public void updateUser(User user) throws SQLException
    {
        PreparedStatement pstatement = null;

        try
        {
            String sql = ""
                    + " UPDATE tbl_user "
                    + "    SET name = ? "
                    + "  WHERE user_key  = ? ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, user.getName());
            pstatement.setString(2, user.getKey());
            pstatement.execute();
        }
        finally
        {
            pstatement.close();
        }
    }

    /**
     * 指定された枚数、指定された条件でパネルの一覧をランダムに取得します
     * 
     * @param panelCount
     *            パネルの枚数
     * @param isOlynpic
     *            オリンピックモード
     * @return パネルを収めたリスト
     * @throws SQLException
     */
    public ArrayList<Panel> getRandomPanel(int panelCount, boolean isOlynpic) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<Panel> ret = null;

        try
        {
            // パネルの基本となる情報を取得
            String sql = ""
                    + " SELECT ath_id, "
                    + "        name, "
                    + "        picture, "
                    + "        original "
                    + "   FROM tbl_word_base ";
            if (isOlynpic)
            {
                sql += "WHERE original = 1 ";
            }
            sql += "  ORDER BY RAND() "
                    + "  LIMIT 0, ?";

            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, panelCount);
            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<Panel>();
                }

                Panel p = new Panel();

                p.setId(rs.getInt("ath_id"));
                p.setBaseWord(rs.getString("name"));
                p.setPicture(rs.getString("picture"));
                p.setOriginal(rs.getInt("original") == 1);

                // パネルそれぞれに登録された情報を取得
                PreparedStatement pStatement2 = null;
                ResultSet rs2 = null;

                String sql2 = ""
                        + "SELECT word_disp, "
                        + "       word_read, "
                        + "       level, "
                        + "       LEFT(word_read, 1) AS word_head, "
                        + "       IF(right(word_read, 1) != \"ー\", "
                        + "                right(word_read, 1), "
                        + "                left(right(word_read, 2), 1)) AS word_tail "
                        + "  FROM tbl_word_siritori "
                        + " WHERE ath_id = ? "
                        + " ORDER BY level ";

                pStatement2 = connection.prepareStatement(sql2);
                pStatement2.setInt(1, p.getId());
                rs2 = pStatement2.executeQuery();
                try
                {
                    while (rs2.next())
                    {
                        int level = rs2.getInt("level");
                        String word = rs2.getString("word_disp");
                        String wordRead = rs2.getString("word_read");
                        String wordHead = rs2.getString("word_head");
                        String wordTail = rs2.getString("word_tail");

                        Word w = new Word(level, word, wordRead, wordHead, wordTail);

                        p.addWordList(w);
                    }

                    rs2.close();
                }
                finally
                {
                    pStatement2.close();
                }

                ret.add(p);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * ゲーム終了後の結果をDBに登録します
     * 
     * @param result
     * @throws SQLException
     */
    public void insertPlayResult(PlayResult result) throws SQLException
    {
        PreparedStatement pstatement = null;

        try
        {
            String sql = ""
                    + " INSERT INTO tbl_play_result "
                    + "             (id, user_key, playdate, winlose, score, playerCount) "
                    + "      VALUES (?, ?, ?, ?, ?, ?) ";

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, getMaxMatchNumber() + 1);
            pstatement.setString(2, result.getKey());
            pstatement.setDate(3, new java.sql.Date(result.getPlayDate().getTime()));
            pstatement.setInt(4, result.getWinLose());
            pstatement.setInt(5, result.getScore());
            pstatement.setInt(6, result.getPlayerCount());

            pstatement.execute();
        }
        finally
        {
            pstatement.close();
        }
    }

    /**
     * 指定された人数でプレイされたゲームから、最高得点を出したユーザを一定数取得します
     * 
     * @param playerCount
     *            プレイ人数
     * @return ユーザ一覧
     * @throws SQLException
     */
    public ArrayList<User> getHighScoreRanking(int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = "" +
                    " SELECT user_key, " +
                    "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, " +
                    "       MAX(score) as highScore " +
                    "  FROM tbl_play_result AS base " +
                    " WHERE playerCount = ? " +
                    " GROUP by user_key " +
                    " ORDER BY highScore desc " +
                    " LIMIT 0, " + GameSetting.RANKING_COUNT;

            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, playerCount);
            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setHighScore(rs.getInt("highScore"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * 対象ユーザが過去にプレイしたゲームから、最高得点を出した順番に一定数取得します
     * 
     * @param key
     *            ユーザキー
     * @param playerCount
     *            プレイ人数
     * @return ユーザ一覧(プレイ一覧)
     * @throws SQLException
     */
    public ArrayList<User> getHighScoreRanking(String key, int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = "" +
                    " SELECT user_key, " +
                    "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, " +
                    "       MAX(score) as highScore " +
                    "  FROM tbl_play_result AS base " +
                    " WHERE playerCount = ? " +
                    "   AND user_key = ? " +
                    " GROUP by user_key " +
                    " ORDER BY highScore desc " +
                    " LIMIT 0, " + GameSetting.RANKING_COUNT;

            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, playerCount);
            pstatement.setString(2, key);
            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setHighScore(rs.getInt("highScore"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * 過去にプレイされた履歴から、勝敗数に応じて優秀な順番で、一定数のユーザを取得します
     * 
     * @param playerCount
     *            プレイ人数
     * @return ユーザ一覧
     * @throws SQLException
     */
    public ArrayList<User> getWinLoseRanking(int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = "" +
                    " SELECT user_key, " +
                    "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 1) AS win, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 2) AS lose, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = -1) AS draw " +
                    "  FROM tbl_play_result AS base " +
                    " GROUP BY user_key " +
                    " ORDER BY win desc, draw, lose desc " +
                    " LIMIT 0, " + GameSetting.RANKING_COUNT;

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, playerCount);
            pstatement.setInt(2, playerCount);
            pstatement.setInt(3, playerCount);

            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setWinCount(rs.getInt("win"));
                user.setLoseCount(rs.getInt("lose"));
                user.setDrawCount(rs.getInt("draw"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * 対象ユーザの勝敗履歴を取得します
     * 
     * @param key
     *            ユーザキー
     * @param playerCount
     *            プレイ人数
     * @return プレイ結果
     * @throws SQLException
     */
    public ArrayList<User> getWinLoseRanking(String key, int playerCount) throws SQLException
    {
        // TODO : 戻り値がArrayList<User> だが、User にして問題ない(単に時間がなくて対応していないだけです)
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = ""
                    + " SELECT user_key, " +
                    "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 1) AS win, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 2) AS lose, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = -1) AS draw " +
                    "  FROM tbl_play_result AS base " +
                    " WHERE user_key = ? " +
                    " GROUP BY user_key " +
                    " ORDER BY win desc, draw, lose desc " +
                    " LIMIT 0, " + GameSetting.RANKING_COUNT;

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, playerCount);
            pstatement.setInt(2, playerCount);
            pstatement.setInt(3, playerCount);
            pstatement.setString(4, key);

            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setWinCount(rs.getInt("win"));
                user.setLoseCount(rs.getInt("lose"));
                user.setDrawCount(rs.getInt("draw"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * パネル登録用の最大IDを取得します
     * 
     * @return 最大ID
     * @throws SQLException
     */
    public int getMaxPanelNumber() throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        int ret = 0;

        try
        {
            String sql = ""
                    + " SELECT MAX(ath_id) as max"
                    + "   FROM tbl_word_base ";

            pstatement = connection.prepareStatement(sql);
            rs = pstatement.executeQuery();
            if (rs.next())
            {
                ret = rs.getInt("max");
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    /**
     * ユーザが登録した内容でもってパネルをDBに登録します
     * 
     * @param panel
     *            登録対象のパネル
     * @throws SQLException
     */
    public void insertUserPanel(Panel panel) throws SQLException
    {
        PreparedStatement pstatement = null;

        try
        {
            // 各SQLの実行に際して、即時反映をさせないようにする
            // ※トランザクション等で検索すると理解が深まるハズ
            connection.setAutoCommit(false);

            int athId = getMaxPanelNumber() + 1;

            // パネルの基本情報の登録
            String sql = ""
                    + " INSERT INTO tbl_word_base "
                    + "             (ath_id, name, picture, original, approval) "
                    + "      VALUES (?, ?, ?, ?, ?) ";

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, athId);
            pstatement.setString(2, panel.getBaseWord());
            pstatement.setString(3, panel.getPicture());
            pstatement.setInt(4, 0);
            pstatement.setInt(5, 0);

            pstatement.execute();

            // 各単語の登録
            ArrayList<Word> wordList = panel.getWordList();
            for (int i = 0; i < wordList.size(); i++)
            {
                Word word = wordList.get(i);

                pstatement = connection.prepareStatement(sql);

                sql = "" +
                        "INSERT INTO tbl_word_siritori " +
                        "            (ath_id, word_id, word_disp, word_read, level)" +
                        "     VALUES (?, ?, ?, ?, ?) ";

                pstatement = connection.prepareStatement(sql);

                pstatement.setInt(1, athId);
                pstatement.setInt(2, i + 1);
                pstatement.setString(3, word.getWord());
                pstatement.setString(4, word.getWordRead());
                pstatement.setInt(5, 0);

                pstatement.execute();
            }

            // AutoCommit(false)を設定してから、間に実行されたSQL文すべてをDBに反映する
            connection.commit();
        }
        finally
        {
            // AutoCommit(false)を設定してから、間に実行されたSQL文すべてをDBに反映させず、削除する
            connection.rollback();
            // SQL実行から即時反映する状態に設定を戻す
            connection.setAutoCommit(true);
            pstatement.close();
        }
    }

}

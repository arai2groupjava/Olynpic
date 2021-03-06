package ControlPack;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import modelPack.Match;
import modelPack.MatchList;

/**
 * URL:gameにおけるWebSocket
 */
@ServerEndpoint(value = "/game")
public class GameWebSocket
{
    private static Set<Session> ses = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session)
    {
        ses.add(session);
    }

    @OnMessage
    public void onMessage(String text, Session session)
    {
        if (session == null || !session.isOpen())
        {
            return;
        }

        // ※不正な情報がWebSocket経由で送信された場合について対応していない
        String[] receive = text.split(",");
        String key = receive[0];

        Match match = MatchList.getMatch(key);

        if (receive.length == 1)
        {
            // 初回接続時の送信情報であれば、ユーザのセッション情報をセット
            MatchList.setUserSession(key, session);

            match.sendSessionGameReconnect();

            return;
        }

        // 初回接続時以降であれば、パネルの選択処理を行う
        String selected = receive[1];

        if (receive[1].equals("gameEnd"))
        {
            match.finishMatch();
        }
        else
        {
            match.panelSelect(key, selected);
        }

        // マッチングに参加している各ユーザに対して、更新用HTMLの送信
        match.sendSessionGameUpdate();
    }

    @OnClose
    public void onClose(Session session)
    {
        Match match = MatchList.getMatch(session);
        if (match != null)
        {
            MatchList.setUserSessionNull(session);
            if (!match.isFinish())
            {
                match.sendSessionGameDisconnect(session);
            }
        }
        ses.remove(session);
    }

}

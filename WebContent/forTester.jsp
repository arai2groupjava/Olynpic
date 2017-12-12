<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.*" %>
<%@ page import="java.net.*"%>
<%@ page import="java.util.ArrayList"%>

<%

Cookie[] aryCookies = request.getCookies();
String key = null;
String name = null;

if (aryCookies != null)
{
    for (int i = 0; i < aryCookies.length; i++)
    {
        String cookie = aryCookies[i].getName();
        if (cookie.equals("key"))
        {
            key = aryCookies[i].getValue();            
        }
        else if (cookie.equals("name"))
        {
            name = URLDecoder.decode(aryCookies[i].getValue(), "UTF-8");
        }
    }
}

%>

<!DOCTYPE html>
<html>

<head>
    <META charset="UTF-8">
    <title>�e�X�g�p���</title>
</head>

<body>

<table>
<%for (int i=0;i<aryCookies.length;i++) {%>
    <TR>
        <TD><%=aryCookies[i].getName()%></TD>
        <TD><%=aryCookies[i].getValue()%></TD>
    </TR>
<%}%>
</table>

<h1>��l�ΐ�}�b�`���O�e�[�u��</h1>
<table border="1">
    <tr>
        <th>�}�b�`No</th>
        <th>���[�U�L�[</th>
        <th>���[�U���O</th>
        <th>�J�n����</th>
        <th>�I������</th>
        <th>�p�l������</th>
    </tr>
    <%for (Match m : MatchList.getMatchList()){ 
          if (m.isFinish() || m.getPlayerCount() != 1)
          {
              continue;
          }
    %>
        <tr>
            <td><%=m.getMatchNo()%></td>
            <%for (User u : m.getUserList()) {%>
                <td><%=u.getKey()%></td>
                <td><%=u.getName()%></td>
            <% } %>
            <td><%=m.getStartTime()%></td>
            <td><%=m.getEndTime()%></td>
            <td><%=m.getPanelList().size()%></td>
        </tr>
    <%}%>
</table>

<h1>��l�ΐ�}�b�`���O�e�[�u��</h1>
<table border="1">
    <tr>
        <th>�}�b�`No</th>
        <th>���[�U�L�[</th>
        <th>���[�U���O</th>
        <th>���[�U�L�[</th>
        <th>���[�U���O</th>
        <th>�J�n����</th>
        <th>�I������</th>
        <th>�p�l������</th>
    </tr>
    <%for (Match m : MatchList.getMatchList()){ 
          if (m.isFinish() || m.getPlayerCount() != 2)
          {
              continue;
          }
    %>
    <tr>
        <td><%=m.getMatchNo()%></td>
        <%for (User u : m.getUserList()) {%>
            <td><%=u.getKey()%></td>
            <td><%=u.getName()%></td>
        <% } %>
        <td><%=m.getStartTime()%></td>
        <td><%=m.getEndTime()%></td>
        <td><%=m.getPanelList().size()%></td>
    </tr>
    <%}%>
</table>

<h1>�I����}�b�`���O�e�[�u��</h1>
<table border="1">
    <tr>
        <th>�}�b�`No</th>
        <th>���[�U�L�[</th>
        <th>���[�U���O</th>
        <th>�J�n����</th>
        <th>�I������</th>
        <th>�p�l������</th>
    </tr>
    <%for (Match m : MatchList.getMatchList()){ 
          if (!m.isFinish() || m.getPlayerCount() != 1)
          {
              continue;
          }
    %>
    <tr>
        <td><%=m.getMatchNo()%></td>
        <%for (User u : m.getUserList()) {%>
            <td><%=u.getKey()%></td>
            <td><%=u.getName()%></td>
        <% } %>
        <td><%=m.getStartTime()%></td>
        <td><%=m.getEndTime()%></td>
        <td><%=m.getPanelList().size()%></td>
    </tr>
    <%}%>
</table>
<table border="1">
    <tr>
        <th>�}�b�`No</th>
        <th>���[�U�L�[</th>
        <th>���[�U���O</th>
        <th>���[�U�L�[</th>
        <th>���[�U���O</th>
        <th>�J�n����</th>
        <th>�I������</th>
        <th>�p�l������</th>
    </tr>
    <%for (Match m : MatchList.getMatchList()){ 
          if (!m.isFinish() || m.getPlayerCount() != 2)
          {
              continue;
          }
    %>
        <tr>
            <td><%=m.getMatchNo()%></td>
            <%for (User u : m.getUserList()) {%>
                <td><%=u.getKey()%></td>
                <td><%=u.getName()%></td>
            <% } %>
            <td><%=m.getStartTime()%></td>
            <td><%=m.getEndTime()%></td>
            <td><%=m.getPanelList().size()%></td>
        </tr>
    <%}%>
</table>


<a href="top">�g�b�v�֖߂�</a>

</body>
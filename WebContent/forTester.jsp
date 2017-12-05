<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page import="modelPack.*"%>
<%@ page import="Utility.*" %>
<%@ page import="java.util.ArrayList"%>

<%

Match match = (Match)request.getAttribute("match");

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
            name = aryCookies[i].getValue();            
        }
    }
}

//����A�N�Z�X���̋����ɂ��Ă͗v�C��
if (key == null || key.isEmpty())
{
    Cookie cooKey = new Cookie("key", Utility.getKey());
    response.addCookie(cooKey);
    response.sendRedirect("index.jsp");
    return;
}

if (name != null)
{
    Cookie cooName = new Cookie("name", name);
    response.addCookie(cooName);
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
          if (m.getPlayerCount() != 1)
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
          if (m.getPlayerCount() != 2)
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
    <%for (Match m : FinishedMatchList.getMatchList()){ 
          if (m.getPlayerCount() != 2)
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
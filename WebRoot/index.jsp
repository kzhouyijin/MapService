<%@ page contentType="text/html; charset=gb2312" language="java" import="java.io.*" errorPage="" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Untitled Document</title>
</head>

<body>
��ǰWEBӦ�õ�����·����<%=application.getRealPath("/")%>

��ǰ�������JSP�ļ�������·����<%=application.getRealPath(request.getRequestURI())%>

<%String path=application.getRealPath(request.getRequestURI());
String dir=new File(path).getParent();
out.println("��ǰJSP�ļ�����Ŀ¼������·��:"+dir);
String realPath1 = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);
out.println("web URL ·��:"+realPath1);
%>
</body>
</html>
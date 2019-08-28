<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>freemarker测试</title>
</head>
<body>
${name}:${message}<br>
<br>
<#-- 这是注释；在生成后的文件中是看不见的 -->
assign可以定义变量：<br>
<#assign linkman="黑马程序员"/>
${linkman}<br>
<#assign info={"mobile":"13400000000", "address":"吉山村"}>
手机号为：${info.mobile}---地址为：${info.address}
<br><hr><br>
include可以引入其它模版文件到当前模版文件中。<br>
<#include "header.ftl"/>

<br><hr><br>
<br><hr><br>
<br><hr><br>
<br><hr><br>
<br><hr><br>
<br><hr><br>
<br><hr><br>
</body>
</html>
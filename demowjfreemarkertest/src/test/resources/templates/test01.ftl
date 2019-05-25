<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
    Hello ${name}!<br/>
    <#list stus as item>
    ${item_index}
        ${item.name}<br/>
    </#list>

    ${stuMap.stu1.name}

<#list stuMap?keys as k>
    ${stuMap[k].name}
</#list>
</body>
</html>
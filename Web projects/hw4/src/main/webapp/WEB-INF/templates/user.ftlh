<#-- @ftlvariable name="id" type="java.lang.Long" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#assign u = c.findBy(users, "id", user_id!-1)!/>
    <#if !u?hasContent>
        <#assign u = c.findBy(users, "handle", handle!"")!/>
    </#if>

    <#if u?hasContent>
        <div class="user">
            <div class="textAboutUser">
            <a href="#">${u.name}</a>
                <ul>
                    <li>id: ${u.id}</li>
                    <li>handle: ${u.handle}</li>
                    <li>name: ${u.name}</li>
                </ul>
            </div>
            <img src="../../img/no-title.jpg" title="userImage" alt="userImage"/>
        </div>
        <div class="swapUser">
            <#assign indexOfUser = users?seqIndexOf(u)>
            <#if indexOfUser == 0>
                <a class="emptyLink">← </a>
                <a href="/user?handle=${users[indexOfUser + 1].handle}"> →</a>
            <#elseIf indexOfUser == users?size - 1>
                <a href="/user?handle=${users[indexOfUser - 1].handle}">← </a>
                <a class="emptyLink"> →</a>
            <#else>
                <a href="/user?handle=${users[indexOfUser - 1].handle}">← </a>
                <a href="/user?handle=${users[indexOfUser + 1].handle}"> →</a>
            </#if>
        </div>
    <#else>
        No users
    </#if>
</@c.page>

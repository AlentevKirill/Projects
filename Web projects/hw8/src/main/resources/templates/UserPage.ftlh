<#import "commons.ftlh" as c>

<@c.page>
    <ul>
        <#if userr?has_content>
            <li>User id: ${userr.id}</li>
            <li>User name: ${userr.login}</li>
            <li>Created: ${userr.creationTime}</li>
        <#else>
            <p>No such User</p>
        </#if>
    </ul>
</@c.page>
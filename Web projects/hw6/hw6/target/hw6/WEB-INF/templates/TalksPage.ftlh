<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="talks" type="ru.itmo.wp.model.domain.Talk[]" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Talk</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="talk"/>
                <select class="select" size="1" name="recipientUserLogin">
                    <option disabled>Choose user</option>
                    <#list users as user>
                        <option value="${user.login}">${user.login}</option>
                    </#list>
                </select>
                <textarea class="textarea" rows="10" cols="45" name="text"></textarea>
                <#if error??>
                    <div class="error">
                        ${error!}
                    </div>
                </#if>
                <div class="button-field">
                    <input type="submit" value="Enter">
                </div>
            </form>
        </div>
    </div>
    <div class="datatable">
        <div class="caption">Talks</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Sender</th>
                <th>Recipient</th>
                <th>Text</th>
                <th>Created</th>
            </tr>
            </thead>
            <tbody>
            <#list talks as talk>
                <tr>
                    <td>${talk.id}</td>
                    <td>${c.findBy(users, "id", talk.sourceUserId).login}</td>
                    <td>${c.findBy(users, "id", talk.targetUserId).login}</td>
                    <td>${talk.text}</td>
                    <td>${talk.creationTime}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
<#-- @ftlvariable name="email" type="java.lang.String" -->
<#-- @ftlvariable name="passwordConfirm" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->
<#-- @ftlvariable name="password" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Register</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="register"/>
                <div class="field">
                    <div class="name">
                        <label for="login">Login</label>
                    </div>
                    <div class="value">
                        <input id="login" name="login" value="${login!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="email">Email</label>
                    </div>
                    <div class="value">
                        <input id="email" name="email" value="${email!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" name="password" type="password" value="${password!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="passwordConfirm">Password confirm</label>
                    </div>
                    <div class="value">
                        <input id="passwordConfirm" name="passwordConfirm" type="password" value="${passwordConfirm!}"/>
                    </div>
                </div>
                <#if error??>
                    <div class="error">
                        ${error!}
                    </div>
                </#if>
                <div class="button-field">
                    <input type="submit" value="Login">
                </div>
            </form>
        </div>
    </div>
</@c.page>

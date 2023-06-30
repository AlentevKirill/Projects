<#import "commons.ftlh" as c>

<@c.page>
    <form class="notice-form" action="" method="post">
        <div>
            <p>Notice:</p>
        </div>
        <textarea class="textarea" id="content" rows="10" cols="45" name="content"></textarea>
        <div class="button-field">
            <input type="submit" value="Add">
        </div>
        <@c.error "noticeForm.content"/>
    </form>
</@c.page>
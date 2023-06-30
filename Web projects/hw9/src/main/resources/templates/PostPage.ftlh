<#import "/spring.ftl" as spring/>
<#import "commons.ftlh" as c>

<@c.page>
<#-- @ftlvariable name="postById" type="ru.itmo.wp.domain.Post" -->
    <article>
        <div class="title">${postById.title}</div>
        <div class="information">By ${postById.user.login}, ${postById.creationTime}</div>
        <div class="body">${postById.text}</div>
        <ul class="attachment">
            <li>Announcement of <a href="#">Codeforces Round #510 (Div. 1)</a></li>
            <li>Announcement of <a href="#">Codeforces Round #510 (Div. 2)</a></li>
        </ul>
        <div class="footer">
            <div class="left">
                <img src="<@spring.url '/img/voteup.png'/>" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+173</span>
                <img src="<@spring.url '/img/votedown.png'/>" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="<@spring.url '/img/date_16x16.png'/>" title="Publish Time" alt="Publish Time"/>
                ${postById.creationTime}
                <img src="<@spring.url '/img/comments_16x16.png'/>" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
        <div class="tags">
            <#list postById.tags as tag>
                ${tag.name}<#if tag_has_next>, </#if>
            </#list>
        </div>
    </article>
    <#if user??>
        <form method="post" action="">
            <div class="field">
                <div class="name">
                    <label for="text">Comment</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text">${comment.text!}</textarea>
                </div>
                <@c.error "comment.text"/>
            </div>
            <div class="button-field">
                <input type="submit" value="Write Comment">
            </div>
        </form>
    </#if>
    <#list postById.comments as comment>
        <div class="comment">
            <p>User: ${comment.user.login}</p>
            <p>Text: ${comment.text}</p>
        </div>
    </#list>
</@c.page>
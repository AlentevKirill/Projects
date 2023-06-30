<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <ul class="posts">
        <template>
            <li>
                <article>
                    <div class="title"><#--${article.title}--></div>
                    <#--<#assign u = c.findBy(users, "id", article.userId!1)!/>-->
                    <div class="information"><#--By ${u.login}, 2 days ago, translation--></div>
                    <div class="body">
                        <p><#--${article.text}--></p>
                    </div>
                    <ul class="attachment">
                        <li>Announcement of <a href="#">Codeforces Round #510 (Div. 1)</a></li>
                        <li>Announcement of <a href="#">Codeforces Round #510 (Div. 2)</a></li>
                    </ul>
                    <div class="footer">
                        <div class="left">
                            <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                            <span class="positive-score">+173</span>
                            <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                        </div>
                        <div class="right">
                            <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                            2 days ago
                            <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                            <a href="#">68</a>
                        </div>
                    </div>
                </article>
            </li>
        </template>
        <li class="noData">No Data</li>
    </ul>

    <script>
        $(function () {
            const $noData = $(".posts .noData");
            $noData.text("Loading...");

            var data = {action: "findAllArticle"};
            var ifSuccess = function(response) {
                response["articles"].forEach(function(article) {
                    $noData.hide();
                    const $list = $($(".posts template").prop("content")).clone();
                    $list.find(".title").text(article["title"]);
                    $list.find(".information").text("By User " + article["userId"] + ", 2 days ago, translation");
                    $list.find(".body p").text(article["text"]);
                    $(".posts").append($list);
                });
            }

            ajax(data, $(this), "", ifSuccess);
        })
    </script>
</@c.page>

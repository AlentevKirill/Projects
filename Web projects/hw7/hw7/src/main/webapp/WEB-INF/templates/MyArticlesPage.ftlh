<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="datatable">
        <div class="caption">Articles</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Hidden</th>
            </tr>
            </thead>
            <tbody>
            <#list articles as article>
                <tr>
                    <td>${article.id}</td>
                    <td>${article.title}</td>
                    <td><button class="articleId-${article.id}"></button></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            var data = {action: "action"};
            var ifSuccess = function(response) {
                response["articles"].forEach(function(article) {
                    const $button = $(".datatable tbody .articleId-" + article["id"])
                    $button.text(article["hidden"]?"Show":"Hide");
                    $button.attr("data-articleId", article["id"]);
                });
                $(".datatable tbody button").click(function() {
                    let now = $(this).text();
                    const $that = $(this);
                    //const text = now === "Hide" ? "Show" : "Hide";
                    var data = {
                        action: "changePermission",
                        //now: now,
                        //text: text,
                        articleId: $(this).attr("data-articleId")
                    }
                    var ifSuccess = function () {
                        const text = now === "Hide" ? "Show" : "Hide";
                        //alert($(this).attr("data-articleId"));
                        $that.text(text);
                    }
                    ajax(data, $(this), "", ifSuccess);
                });
            }

            ajax(data, $(this), "", ifSuccess);
            /*$.ajax({
                type: "POST",
                url: "",
                dataType: "json",
                data: {action: "action"},
                success: function(response) {
                    response["articles"].forEach(function (article) {
                        const $button = $(".datatable tbody .articleId-" + article["id"])
                        $button.text(article["hidden"] ? "Show" : "Hide");
                        $button.attr("data-articleId", article["id"]);
                    });
                    $(".datatable tbody button").click(function() {
                        let now = $(this).text();
                        $.ajax({
                            type: "POST",
                            url: "",
                            dataType: "json",
                            data: {
                                action: "changePermission",
                                now: now,
                                articleId: $(this).attr("data-articleId")
                            },
                            success: function (response) {
                                const text = now === "Hide" ? "Show" : "Hide";
                                //alert($(this).attr("data-articleId"));
                                $(this).text(text);
                                $(".datatable tbody .articleId-" + $(this).attr("data-articleId")).text(text);
                            }
                        })
                    });
                }
            });*/
        })
    </script>
</@c.page>
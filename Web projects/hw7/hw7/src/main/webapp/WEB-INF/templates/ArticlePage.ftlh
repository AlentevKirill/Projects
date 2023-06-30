<#import "commons.ftlh" as c>

<@c.page>
  <div class="form-box">
    <div class="header">Article</div>
    <div class="body">
      <form method="post" action="">
        <input type="hidden" name="action" value="article"/>
        <div class="field">
          <div class="name">
            <label for="title">Title</label>
          </div>
          <div class="value">
            <input id="title" name="title"/>
          </div>
        </div>
        <textarea class="textarea" rows="10" cols="45" name="text"></textarea>
        <div class="error"></div>
        <div class="button-field">
          <input type="submit" value="Create">
        </div>
      </form>
    </div>
  </div>

  <script>
    $(function () {
      $(".form-box").submit(function () {
        const title = $(this).find("input[name='title']").val();
        const text = $(this).find("textarea[name='text']").val();

        var data = {
          action: "article",
          title: title,
          text: text
        }
        ajax(data, $(this).find(".error"), response["redirect"]);
        return false;
      });
    })
  </script>
</@c.page>
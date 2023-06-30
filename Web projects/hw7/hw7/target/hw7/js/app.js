window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

/*const defaultSuccessHandler = function (response) {
    if (response["error"]) {
        $(this).find(".error").text(response["error"]);
    } else {
        location.href = response["redirect"];
    }
};*/

window.ajax = function (data, $error, redirect = "/index", ifSuccess = function (response) {
    if (response["error"]) {
        //$(this).find(".error").text(response["error"]);
        $error.text(response["error"]);
    } else {
        location.href = redirect;
    }
}/*defaultSuccessHandler*/) {
    $.ajax({
        type: "POST",
        url: "",
        dataType: "json",
        data: data,
        success: ifSuccess
    });
}

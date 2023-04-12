$('#cover-spin').show();
function validateJwtToken(accessToken) {
    try {
        console.log("verify function is called" + apiUrl);

        $.ajax({
            url: apiUrl + "/verify-token",
            method: "POST",
            async: false,
            data: JSON.stringify({
                "jwt": window.sessionStorage.getItem("token")
            }),
            headers: jsonHeader,
            success: function (responce) {
                console.log("API responce", responce);
                // showToast(responce.message, 'success');
            },
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText);
                console.log(errorMessage.message);
                // showToast(errorMessage.message, 'error');
            }
        });
        return true;
    }
    catch (error) {
        console.log("error" + error)
        return false;
    }
}
$(document).load(function () {
    var accessToken = window.sessionStorage.getItem("token"); // get token and store accessToken
    var sessionCheck = false;
    console.log(accessToken != null);
    if (accessToken != null) { // accessToken find then this block execute
        var tokenvalide = validateJwtToken(accessToken);
        if (tokenvalide) {
            sessionCheck = true;
        }
    }
    var page_url = window.location.href;
    console.log(page_url);
    var extractedPath = page_url.split("/").slice(-1);
    console.log(extractedPath);
    if (sessionCheck != true) {
        // window.sessionStorage.removeItem("token");
        if (extractedPath != "login.html" && //false 
            extractedPath != "register.html" && //true
            extractedPath != "forgot.html") { //true
            window.location.href = "login.html";
        }
    }
    else {
        if (extractedPath == "login.html" ||
            extractedPath == "register.html" ||
            extractedPath == "forgot.html") {
            window.location.href = "home.html";
            console.log("token not valid");
        }
    }
});
$('#cover-spin').hide();


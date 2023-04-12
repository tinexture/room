// const apiUrl = 'https://roommates-782r.onrender.com';
$('#cover-spin').show();

function validateJwtToken(accessToken) {
    try {
        console.log("verify function is called");
        $.ajax({
            url: apiUrl + "/verify-token",
            method: "POST",
            async: false,
            data: JSON.stringify({
                "jwt": window.sessionStorage.getItem("token")
                // "jwt": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGF2ZGFzYW5kaXBAZ21haWwuY29tIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWQiOjgzLCJmdWxsbmFtZSI6IlNhbmRpcCBDaGF2ZGEiLCJsb2NrZWQiOmZhbHNlLCJleHAiOjE2ODEyMzk2NjUsImlhdCI6MTY4MTIzNjA2NSwiZW5hYmxlZCI6dHJ1ZX0.xctjCLOe6LaStBDKj2GsCrdWdhbDIGmhmGd_ET-MdEVeqsdTt7O-Tk67ZTAnY6-2ymqqVPzw71COeywdHQMceQ"
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function (responce) {
                console.log("API responce", responce);
                alert("user tiken is valid");
                showToast(responce.message, 'success');
            },
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText);
                console.log(errorMessage.message);
                alert("user tiken is not valid");
                showToast(errorMessage.message, 'error');
            }
        });
        return true;
    }
    catch (error) {
        return false;
    }
}
var accessToken = window.sessionStorage.getItem("token"); // get token and store accessToken
var sessionCheck = false;
if (accessToken != null) { // accessToken find then this block execute
    if (validateJwtToken(accessToken)) {
        sessionCheck = true;
    }
}
var page_url = window.location.href;
var extractedPath = page_url.split("/").slice(-1);
if (sessionCheck != true) {
    window.localStorage.removeItem("token");
    if (extractedPath != "login.html" && //false 
        extractedPath != "registration.html" && //true
        extractedPath != "forgote-pass.html") { //true
        window.location.href = "login.html";

    }
}
else {
    if (extractedPath == "login.html" ||
        extractedPath == "registration.html" ||
        extractedPath == "forgote-pass.html") {
        window.location.href = "homePage.html";
        console.log("token not valid");
    }
}

$('#cover-spin').hide();


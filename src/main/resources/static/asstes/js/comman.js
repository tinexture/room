const apiUrl = 'https://roommates-782r.onrender.com';

function showToast(message, type) {
    console.log("c -tost call");
    const $newDiv = $('<div>' + message + '</div>');   // create a div element
    $newDiv.addClass('toast-message');    // set class for the div element
    $('#api-responce').append($newDiv);    // append the div element to the body of the page
    $newDiv.css("background-color", type === 'success' ? '#4CAF50' : '#F44336'); // set new div background color 
    setTimeout(() => {
        $newDiv.css("opacity", "0.5");
    }, 3000);
    setTimeout(() => {
        $newDiv.css("display", "none");

    }, 3000);
}

async function validateJwtToken(accessToken){
    try {
        await $.ajax({
            url: apiUrl + "/verify-token",
            method: "POST",
            data: JSON.stringify({
                "jwt": window.sessionStorage.getItem("token")
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function (responce) {
                console.log("API responce", responce);
                showToast(responce.message, 'success');
            },
            error: function (xhr, status, error) {
                var errorMessage = JSON.parse(xhr.responseText);
                console.log(errorMessage.message);
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
if (accessToken != null) { // accessToken find then this block execute
    $('#cover-spin').show();
    (async function () {
        const result = await validateJwtToken(accessToken);
        var pathArray = window.location.pathname.split("/");
        var extractedPath = pathArray[pathArray.length - 1];
        console.log(extractedPath);
        if (!result) {
            // The user makes any request other than the login and registration page then redirect login page.
            if (extractedPath != "login.html" &&
                extractedPath != "registration.html" &&
                extractedPath != "forgote-pass.html") {
                window.location.href = "login.html";
                console.log("token not valid");
            }
            window.localStorage.removeItem("token");
        }
        else {
            if (extractedPath == "login.html" ||
                extractedPath == "registration.html" ||
                extractedPath == "forgote-pass.html") {
                window.location.href = "homePage.html";
                console.log("token not valid");
            }
        }
    })();
    $('#cover-spin').hide();
}
if(accessToken==null) {
    showToast("Token already exist  ", 'success');
}

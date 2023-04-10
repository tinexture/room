function callLoginApi() {
    authenticate($("#email").val(), $("#password").val());
}

function authenticate(email, password) {
    $.ajax({

        url: apiUrl+"/authenticate",
        method: "POST",
        data: JSON.stringify({
            "email": email,
            "password": password
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            console.log("API responce", responce);
            alert("User logged-in successsfully");
            window.localStorage.setItem("token", "Bearer " + responce.jwt);
        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            console.log(errorMessage.message);
            alert(errorMessage.message);
        }

    });
}


function callLRegistrationApi() {
    var emailEle = $("#email").val();

    $.ajax({
        url: apiUrl+"/user",
        method: "POST",
        data: JSON.stringify({
            "email": $("#email").val(),
            "firstName": $("#fname").val(),
            "lastName": $("#lname").val(),
            "profilePhoto": null,
            "password": $("#email").val(),
            "gender": $("#gender").val(),
            "enabled": "true",
            "locked": "false",
            "mobile": $("#mobile-fild").val(),
            "address": {
                "generalAddress": $("#general-address").val(),
                "country": $("#country").val(),
                "state": $("#state").val(),
                "city": $("#city").val(),
                "pincode": $("#pin-code-fild").val()
            }
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (responce) {
            console.log("API responce", responce);
            alert("User created successsfully");

        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            console.log(errorMessage.message);
            alert(errorMessage.message);
        }

    });
}

var reader = new FileReader();
reader.onload = function(event) {
    var base64string = event.target.result;
    console.log(base64string);
};
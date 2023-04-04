function callLoginApi() {
    authenticate($("#email").val(), $("#password").val());
}

function authenticate(email, password) {
    const apiUrl = 'https://roomates.onrender.com/authenticate';
    $.ajax({
        url: apiUrl,
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
            window.location.href = "homePage.html";
        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            console.log(errorMessage.message);
            alert(errorMessage.message);
        }

    });
}


function callLRegistrationApi() {
    const apiUrl = 'https://roomates.onrender.com/user';
    $.ajax({
        url: apiUrl,
        method: "POST",
        data: JSON.stringify({
            "firstName": $("#fname").val(),
            "lastName": $("#lname").val(),
            "profilePhoto": null,
            "password": $("#password-fild").val(),
            "gender": $("#gender").val(),
            "enabled": "true",
            "locked": "false",
            "mobile": $("#mobile-fild").val(),
            "email": $("#email-fild").val(),
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
            window.location.href = "login.html";

        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            console.log(errorMessage.message);
            alert(errorMessage.message);
        }
    });
}

function verifyToken() {
    const jwt = require('jsonwebtoken');
    const token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...';
    const secret = 'room-management-system';

    jwt.verify(token, secret, (err, decoded) => {
        if (err) {
            console.log('Invalid token!');
        } else {
            console.log(decoded);
        }
    });
}

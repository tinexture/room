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
    const apiUrl = 'https://roomates.onrender.com/user';
    var getToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGF2ZGFzYW5kaXBAZ21haWwuY29tIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWQiOjEzLCJmdWxsbmFtZSI6InByYWRpcCBjaGF2ZGEiLCJsb2NrZWQiOmZhbHNlLCJleHAiOjE2ODA1NTU2MDMsImlhdCI6MTY4MDU1MjAwMywiZW5hYmxlZCI6dHJ1ZX0.u4TxWeOHKMtZjCZNk1votGli61xHKqjZa2f4Y9rRsdwi4xGeB6_nSW3YjqDuIRZ9jED7QtgxCaU4YsqpE1L8cw";
    console.log(getToken);

    $.ajax({
        url: apiUrl,
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
            'Content-Type': 'application/json',
            'Authorization': getToken
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
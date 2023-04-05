function callLoginApi() {
    authenticate($("#email").val(), $("#password").val());
}

function authenticate(email, password) {
    $('#cover-spin').show();
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
            $('#cover-spin').hide(); 
            alert("User logged-in successsfully");
            window.sessionStorage.setItem("token", "Bearer " + responce.jwt);
            window.location.href = "homePage.html";
        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            console.log(errorMessage.message);
            $('#cover-spin').hide();
            alert(errorMessage.message);
        }
    });
}


$('#sub').click(function () {
    // get value
    var email = $('#email').val();
    var pass = $('#password').val();
    //error element stor 
    var email_error = $('#email-err');
    var pass_error = $('#password-err');
    // call function
    var email_ret = emailVlid(email, email_error);
    var pass_ret = passVlid(pass, pass_error);
    // give error border 
    if (email_ret != true) {
        $('#email').css("border-color", "red");
    } else {
        $('#email').css("border-color", "#ced4da");
    }
    if (pass_ret != true) {
        $('#password').css("border-color", "red");
    } else {
        $('#password').css("border-color", "#ced4da");
    }
    // form valide then call "callLoginApi()"
    if (email_ret && pass_ret) {
        console.log("form is valid");
        callLoginApi();
    }
});

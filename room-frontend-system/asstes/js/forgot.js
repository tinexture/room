
$("#generate-otp").click(function () {
    var femailren = emailVlid($('#fEmail').val(), $('#femail-err'));
    borderErrorColor(femailren, $('#fEmail'));
    if (femailren) {
        callSendOtp();
    }
});
$("#verified-otp").click(function () {
    var otpren = otpVlid($('#otp-field').val(), $('#otp-err'));
    borderErrorColor(otpren, $('#otp-field'));
    if (otpren) {
        callVerifyOtp();
    }
});
$("#set-new-password").click(function () {
    var npssretu = passVlid($('#new-password').val(), $('#new-password-err'));
    var cnpassretu = conpassVlid($('#re-enter-password').val(), $('#new-password').val(), $('#re-enter-password-err'));
    borderErrorColor(npssretu, $('#new-password'));
    borderErrorColor(cnpassretu, $('#re-enter-password'));
    if (npssretu && cnpassretu) {
        callChangePassword($('#fEmail').val(), $('#new-password').val());
    }
});

function callSendOtp() {
    $('#cover-spin').show();
    $.ajax({
        url: apiUrl + "/user/send-otp/" + $('#fEmail').val(),
        method: "GET",
        headers: jsonHeader,
        success: function (response) {
            showToast(response.message, "success");
            $(".otp-section").css("display", "block");
            $("#verified-otp").css("display", "block");
            $("#generate-otp , #fEmail").prop("disabled", true);
            $('#cover-spin').hide();
        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            showToast(errorMessage.message, "error");
            $('#cover-spin').hide();
        }

    });
}


function callVerifyOtp() {
    $('#cover-spin').show();
    $.ajax({
        url: apiUrl + "/user/verify-otp/" + $('#fEmail').val() + "/" + $('#otp-field').val(),
        method: "GET",
        headers: jsonHeader,
        success: function (response) {
            $(".password-section").css("display", "block");
            $("#set-new-password").css("display", "block");
            $("#verified-otp,#otp-field").prop("disabled", true);
            showToast(response.message, "success");
            // showToast("Otp verify sucessfully", "success");
            $('#cover-spin').hide();
        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            showToast(errorMessage.message, "error");
            $('#cover-spin').hide();
        }

    });
}


function callChangePassword(email, password) {
    $('#cover-spin').show();
    $.ajax({
        url: apiUrl + "/user/change-password",
        method: "POST",
        headers: jsonHeader,
        data: JSON.stringify({
            "email": email,
            "password": password
        }),
        success: function (response) {
            $("#set-new-password, #new-password ,#re-enter-password").prop("disabled", true);
            showToast(response.message, "success");
            $('#cover-spin').hide();
            window.location.replace("login.html");
        },
        error: function (xhr, status, error) {
            var errorMessage = JSON.parse(xhr.responseText);
            showToast(errorMessage.message, "error");
            $('#cover-spin').hide();
        }

    });
}

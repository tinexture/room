let auth_token;
$(document).ready(function () {
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/getaccesstoken',
        success: function (data) {
            auth_token = data.auth_token;
            getCountry(data.auth_token)
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Accept": "application/json",
            "api-token": "6ipX_pWA4MT4iEk7S-1oRAZ7QvVq2PhmwQFpquICe6DLLHauIRUl_kCQNcrNxelcwSc",
            "user-email": "chavdasandipbhai@gmail.com"
        }

    });
});
function getCountry(auth_token) {
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/countries/',
        success: function (data) {
            data.forEach(element => {
                $('#country').append('<option value="' + element.country_name + '">' + element.country_name + '</option>');
            });
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Authorization": "Bearer " + auth_token,
            "Accept": "application/json"
        }
    });
    $('#country').change(function () {
        getState();
    });
    $('#state').change(function () {
        getCity();
    });
}
function getState() {
    let country_name = $('#country').val();
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/states/' + country_name,
        success: function (data) {
            $('#state').empty();
            $('#city').empty();
            $('#city').append('<option value="select">City</option>');
            data.forEach(element => {
                $('#state').append('<option value="' + element.state_name + '">' + element.state_name + '</option>');
            });
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Authorization": "Bearer " + auth_token,
            "Accept": "application/json"
        }
    });
}
function getCity() {
    let state_name = $('#state').val();
    console.log("Done");
    $.ajax({
        type: 'get',
        url: 'https://www.universal-tutorial.com/api/cities/' + state_name,
        success: function (data) {
            $('#city').empty();
            data.forEach(element => {
                $('#city').append('<option value="' + element.city_name + '">' + element.city_name + '</option>');
            });
        },
        error: function (error) {
            console.log(error);
        },
        headers: {
            "Authorization": "Bearer " + auth_token,
            "Accept": "application/json"
        }
    });
}
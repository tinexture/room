// Below function use for read base64String
function encodeImageFileAsURL(element) {
    var input = $('#upload-profile-picture')[0];
    var reader = new FileReader();
    reader.onload = function (event) {
        var base64String = event.target.result;
        console.log(base64String);
    };
    reader.readAsDataURL(input.files[0]);
}

function showToast(message, type) {
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
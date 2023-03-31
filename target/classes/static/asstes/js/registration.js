var currentTab = 0; // Current tab is set to be the first tab (0)
showTab(currentTab); // Display the current tab

$("#submit").css("display", "none");
function showTab(n) {//0
    var allTabs = $(".tab");
    allTabs.eq(n).css("display", "block");
    if (n == (allTabs.length - 1)) {
        HideShow("#submit", "inline");
        HideShow("#nextBtn", "none");

    } else {
        HideShow("#submit", "none")
        HideShow("#nextBtn", "inline");
    }
    if (n == 0) {
        HideShow("#prevBtn", "none");
    } else {
        HideShow("#prevBtn", "inline");
    }
}

function HideShow(element, value) {
    $(element).css("display", value);
}

function nextPrev(n) {
    var allTabs = $(".tab");
    // If n == 1 then user clicked next button otherwise previous
    allTabs.eq(currentTab).css("display", "none");
    if (n == 1) {
        showTab(++currentTab);
    }
    else {
        showTab(--currentTab)
    }
}

function encodeImageFileAsURL(element) {
    var input = $('#upload-profile-picture')[0];
    // Create a new FileReader object
    var reader = new FileReader();

    // Set up the onload function for the FileReader object
    reader.onload = function (event) {
        // Get the Base64 string from the FileReader object
        var base64String = event.target.result;

        // Do something with the Base64 string (e.g., send it to a server via AJAX)
        console.log(base64String);
    };

    // Read the selected file as a Data URL (which will give us a Base64 string)
    reader.readAsDataURL(input.files[0]);
    $("#preview").attr("src ", base64String);
}




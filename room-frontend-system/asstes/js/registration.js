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
        // console.log(base64String);
    };

    // Read the selected file as a Data URL (which will give us a Base64 string)
    reader.readAsDataURL(input.files[0]);
    $("#preview").attr("src ", base64String);
}

// Set Preview Image function
var preview = $('#preview');
var profile_picture = $('#upload-profile-picture');
profile_picture.change(function (event) {
    if (event.target.files.length == 0) {
        return 0;
    }
    var tempURL = URL.createObjectURL(event.target.files[0]);
    preview.attr('src', tempURL);
});


// function reqListener() {
//     console.log(this.responseText);
//   }
  
//   const req = new XMLHttpRequest();
//   req.addEventListener("load", reqListener);
//   req.open("POST", "https://roomates.onrender.com/authenticate");
//   req.send();
function loginapi() {
  fetch('https://roomates.onrender.com/authenticate')
            .then(response => response.json())
            .then(data => console.log(data))
            .catch(error => console.error(error));
}
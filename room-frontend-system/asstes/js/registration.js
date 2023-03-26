var currentTab = 0; // Current tab is set to be the first tab (0)
showTab(currentTab); // Display the current tab

$("#submit").css("display", "none");
function showTab(n) {
    var allTabs=$(".tab");
    allTabs.eq(n).css("display", "block");
    if (n == (allTabs.length - 1)) {
        HideShow("#submit","inline")
        HideShow("#nextBtn","none")
        
    } else {
        HideShow("#submit","none")
        HideShow("#nextBtn","inline")
    }
    if (n == 0) {
        HideShow("#prevBtn","none")
    } else {
        HideShow("#prevBtn","inline")
    }
}

function HideShow(element, value){
    $(element).css("display", value);
}

function nextPrev(n) {
    var allTabs=$(".tab");
    // If n == 1 then user clicked next button otherwise previous
    allTabs.eq(currentTab).css("display", "none");
    if(n == 1){
        showTab(++currentTab);
    }
    else{
        showTab(--currentTab)
    }
}
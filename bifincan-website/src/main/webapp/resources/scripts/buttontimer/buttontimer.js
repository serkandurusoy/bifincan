$.fn.timedDisable = function(seconds) { 
    return $(this).each(function() { 
        var disabledElem = $(this); 
        var originalText = disabledElem.prop('value');  // Remember the original text content 
        disabledElem.prop('disabled','disabled');
        disabledElem.addClass("disabledElem");
          // append the number of seconds to the text 
        disabledElem.prop('value', '(' + seconds + ') ' + originalText);  
 
         // do a set interval, using an interval of 1000 milliseconds 
         //     and clear it after the number of seconds counts down to 0 
        var interval = setInterval(function() { 
                // decrement the seconds and update the text 
            disabledElem.prop('value', '(' + --seconds + ') ' + originalText );   
            if (seconds === 0) {  // once seconds is 0... 
                disabledElem.prop('value',originalText).removeAttr('disabled').removeClass("disabledElem");   //reset to original text 
                clearInterval(interval);  // clear interval
            } 
        }, 1000); 
    }); 
}; 

function preventDoubleClick(data) {
    var disabledElem = $('#surveySaveAndNextButton'); // The HTML DOM element which invoked the ajax event.
    var ajaxStatus = data.status; // Can be "begin", "complete" and "success".

    switch (ajaxStatus) {
        case "begin": // This is called right before ajax request is been sent.
            disabledElem.prop('disabled','disabled');
            disabledElem.addClass("disabledElem");
            disabledElem.prop('value','az bekle');
            $('#pacman').show();
            break;

        case "complete": // This is called right after ajax response is received.
            // We don't want to enable it yet here, right?
            break;

        case "success": // This is called when ajax response is successfully processed.
            $('#pacman').hide();
            disabledElem.removeAttr('disabled').removeClass("disabledElem");
            break;
    }
};        


$(document).ready(function() {
    var characterCount = Math.ceil(($("#surveyContentQuestion").text().split(" ").length
                            + $("#Option1 tbody tr td label").text().split(" ").length
                            + $("#Option2 tbody tr td label").text().split(" ").length
                            + $("#RatOption1 tbody tr td label").text().split(" ").length - 3) / 6 ) + 1;
    $('#surveySaveAndNextButton').timedDisable(characterCount);
});

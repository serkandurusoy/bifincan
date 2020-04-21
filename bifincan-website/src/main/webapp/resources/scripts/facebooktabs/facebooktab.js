/*
 * JS to use in facebook tabs
 * 
 */

$(document).ready(function() {
    $('a[rel*=external]').bind('click', function(e) {
        e.preventDefault();
        window.open($(this).attr('href'));
    });
    
    $('ul#faq li a').click(function(){
        $('ul#faq li').removeClass('active');
        $($(this).parent()).addClass('active');
    });

});


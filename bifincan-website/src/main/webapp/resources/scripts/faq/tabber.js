$(document).ready(function() {
    $('div#content ul#faq li a').click(function(){
        $('div#content ul#faq li').removeClass('active');
        $($(this).parent()).addClass('active');
    });
});

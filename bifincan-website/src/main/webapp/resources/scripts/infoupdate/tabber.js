$(document).ready(function() {
    $('div#content ul.tabs-a li').click(function(){
        $('div#content ul.tabs-a li').removeClass('active');
        $('div#content div.tabcontent').removeClass('active');
        $(this).addClass('active');
        $('div#content div#'+$(this).attr('rel')).addClass('active');
    });
});

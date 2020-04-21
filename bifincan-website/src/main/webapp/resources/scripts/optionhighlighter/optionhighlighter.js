$(document).ready(function() {
    $('[type="checkbox"]').click(function() {
        if ($(this).is(':checked')) {
            $(this).parent().addClass('secili');
        } else {
            $(this).parent().removeClass('secili');
        }
    });
    
    $('[type="radio"]').click(function() {
        $('[type="radio"]').parent().removeClass('secili');
        $(this).parent().addClass('secili');
    });

});


$(document).ready(function() {

    $(document).on('mouseover','#comments [id^=filike]',function() {
        pos = $(this).position();
        $('#filikecontainer').css('top',pos.top+4);
        $('#filikecontainer').css('left',pos.left-66);
        $('#filikecontainer').show();
    });

    $(document).on('mouseout','#comments [id^=filike]',function() {
        $('#filikecontainer').hide();
    });

    $(document).on('mouseover','#comments [id^=fidislike]',function() {
        pos = $(this).position();
        $('#fidislikecontainer').css('top',pos.top+4);
        $('#fidislikecontainer').css('left',pos.left-66);
        $('#fidislikecontainer').show();
    });
    
    $(document).on('mouseout','#comments [id^=fidislike]',function() {
        $('#fidislikecontainer').hide();
    });

});

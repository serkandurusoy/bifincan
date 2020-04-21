$(document).ready(function() {
    $("a#product-image").fancybox({
        'centerOnScroll'        : true,
        'hideOnContentClick'    : true,
        'type'                  : 'image',
	'titlePosition'         : 'over',
	'onComplete'            : function() {
                                    $("#fancybox-wrap").hover(function() {
                                        $("#fancybox-title").show();
                                    },
                                    function() {
                                        $("#fancybox-title").hide();
                                    });
                                  }
    });
});

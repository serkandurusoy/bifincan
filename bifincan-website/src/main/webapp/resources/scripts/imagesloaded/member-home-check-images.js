$(document).ready(function()
{
    reloadBrokenImages();
});



function reloadBrokenImages() {
    var dfd = $('div#content div.module-b div div p.image-b img , div#content div.product-a div.description p.image img').imagesLoaded(); // save a deferred object
    
    d = new Date();

    dfd.progress( function( isBroken, $images, $proper, $broken ){
        if (isBroken) {
            $(this).attr('src', $(this).attr('src')+'?'+d.getTime());
        }
    });
    
}



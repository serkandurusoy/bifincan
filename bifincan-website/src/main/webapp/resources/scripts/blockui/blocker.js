$(document).ready(function() {

    $.blockUI.defaults.fadeIn = 0;

    $.blockUI.defaults.css.width = '80%';
    
    $.blockUI.defaults.css.color = '#fff';
    $.blockUI.defaults.css.border = 'none';
    $.blockUI.defaults.css.backgroundColor = 'transparent';
    $.blockUI.defaults.css.cursor = 'pointer';

    //$.blockUI.defaults.overlayCSS.backgroundColor = '#6cc';
    $.blockUI.defaults.overlayCSS.backgroundColor = '#3d4c4c';
    $.blockUI.defaults.overlayCSS.opacity = '0.95';
    
    $.blockUI.defaults.applyPlatformOpacityRules = false;

    $('div.ALCOHOL-product').block({
        message: '<h3>bu hediye bir <i>&#34;alkollü içecek&#34;</i> markasına ait,<br/>hediyeyi görmek için buraya tıklayabilirsin</h3>'
    });
        $('div.ALCOHOL-product').click(function() {
            $('div.ALCOHOL-product').unblock();
        });

    $('div.TOBACCO-product').block({
        message: '<h3>bu hediye bir <i>&#34;tütün ürünleri&#34;</i> markasına ait,<br/>hediyeyi görmek için buraya tıklayabilirsin</h3>'
    });
        $('div.TOBACCO-product').click(function() {
            $('div.TOBACCO-product').unblock();
        });

    $('div.MEDICAL-product').block({
        message: '<h3>bu hediye bir <i>&#34;medikal ürünler&#34;</i> markasına ait,<br/>hediyeyi görmek için buraya tıklayabilirsin</h3>'
    }); 
        $('div.MEDICAL-product').click(function() {
            $('div.MEDICAL-product').unblock();
        });

});

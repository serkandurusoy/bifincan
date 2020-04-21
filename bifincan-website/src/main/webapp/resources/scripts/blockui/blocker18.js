$(document).ready(function() {

    $.blockUI.defaults.fadeIn = 0;

    $.blockUI.defaults.css.width = '80%';
    
    $.blockUI.defaults.css.color = '#fff';
    $.blockUI.defaults.css.border = 'none';
    $.blockUI.defaults.css.backgroundColor = 'transparent';
    $.blockUI.defaults.css.cursor = 'pointer';

    //$.blockUI.defaults.overlayCSS.backgroundColor = '#6cc';
    $.blockUI.defaults.overlayCSS.backgroundColor = '#3d4c4c';
    $.blockUI.defaults.overlayCSS.opacity = '0.98';
    
    $.blockUI.defaults.applyPlatformOpacityRules = false;

    $('div.ALCOHOL-product').block({
        message: '<h4>bu hediye bir <i>&#34;alkollü içecek&#34;</i> markasına ait, hem sağlığını gözetmek amacıyla hem de kanunlar uygun görmediği için bu içeriği sana gösteremiyorum, bifincan&#39;da yer alan diğer hediyeleri görmek için<br/>buraya tıklayabilirsin.</h4>'
    });
        $('div.ALCOHOL-product').click(function() {
            window.location = '/fi/tum-hediyeler';
        });

    $('div.TOBACCO-product').block({
        message: '<h4>bu hediye bir <i>&#34;tütün ürünleri&#34;</i> markasına ait, hem sağlığını gözetmek amacıyla hem de kanunlar uygun görmediği için bu içeriği sana gösteremiyorum, bifincan&#39;da yer alan diğer hediyeleri görmek için<br/>buraya tıklayabilirsin.</h4>'
    });
        $('div.TOBACCO-product').click(function() {
            window.location = '/fi/tum-hediyeler';
        });

    $('div.MEDICAL-product').block({
        message: '<h4>bu hediye bir <i>&#34;medikal ürünler&#34;</i> markasına ait, hem sağlığını gözetmek amacıyla hem de kanunlar uygun görmediği için bu içeriği sana gösteremiyorum, bifincan&#39;da yer alan diğer hediyeleri görmek için<br/>buraya tıklayabilirsin.</h4>'
    }); 
        $('div.MEDICAL-product').click(function() {
            window.location = '/fi/tum-hediyeler';
        });

});

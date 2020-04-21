$(document).ready(function(){
        if($.browser.msie) {
                //var browserVersion = $.browser.version.slice(0,$.browser.version.indexOf("."));
                //var documentVersion = document.documentMode;
                //if(browserVersion != documentVersion) {
                //        alert(browserVersion);
                //        alert(documentVersion);
                //        //window.location = "/fi/bu-tarayici-biraz-eski-sanki";
                //}
                if(!isIE9Std()) {
                    window.location = "/fi/bu-tarayici-biraz-eski-sanki";
                }
        }
});

function isIE9Std() {
  var a;
  try{var b=arguments.caller.length;a=0;}catch(e){a=1;}
  return((document.all&&a)==1);
}
$(document).ready(function() {
    /*$('[id]').hover(
        function () {
            showTip($(this));
        }, 
        function () {
            $(this).grumble('hide');
        });*/
    $('[id]').focus(
        function () {
            showTip($(this));
        });
    $('[id]').blur(
        function () {
            $(this).grumble('hide');
        });
});

function showTip(component) {
    if (getComponentText(component)!='') {
        component.grumble({
            text: getComponentText(component),
            angle: 45,
            hideOnClick: true,
            distance: 20
        })
    };
    return null;
}

function getComponentText(component) {
    switch(component.attr('id')) {
        case 'username':
            return 'kullanıcı adını<br/>yalnızca küçük harf ve rakamlardan oluşacak şekilde, Türkçe karakter kullanmadan ve 6-20 hane arasında girmelisin';
            break;
        case 'password':
            return 'şifreni 6-20<br/>karakter arasında girmelisin';
            break;
        case 'confirmPassword':
            return 'şifreni hatalı girmediğinden emin olmak için tekrar girmelisin';
            break;
        case 'firstname':
            return 'ilk ve varsa ikinci adını girmelisin';
            break;
        case 'lastname':
            return 'soyadını girmelisin';
            break;
        case 'birthDate':
            return 'doğum tarihini takvimden seçmen gerekiyor';
            break;
        case 'gsmNumber':
            return 'cep telefonunu boşluk bırakmadan ve sadece rakamlar halinde gir lütfen';
            break;
        case 'addressStreetName':
            return 'cadde ve sokak adını gir lütfen';
            break;
        case 'addressBuildingNumber':
            return 'binanın sokak kapı numarasını gir lütfen';
            break;
        case 'addressAppartmentNumber':
            return 'daire numaranı girmelisin, evin müstakil ise müstakil yazabilirsin';
            break;
        default:
            return '';
    }
}




$(document).ready(function() {
    $(document).on('mouseover','p.alaka a.bialaka',
        function () {
            showTipBialaka($(this));
        });
    $(document).on('mouseout','p.alaka a.bialaka',
        function () {
            $(this).grumble('hide');
        });
    $(document).on('mouseover','p.alaka a.kelalaka',
        function () {
            showTipKelalaka($(this));
        });
    $(document).on('mouseout','p.alaka a.kelalaka',
        function () {
            $(this).grumble('hide');
        });
});

function showTipBialaka(component) {
    component.grumble({
        text: 'bu yorumu konuyla ilgili ve faydalı buluyorum',
        angle: 45,
        hideOnClick: true,
        distance: 20
    });
    return null;
}

function showTipKelalaka(component) {
    component.grumble({
        text: 'bu yorum bence konuyla ilgili veya faydalı değil',
        angle: 45,
        hideOnClick: true,
        distance: 20
    });
    return null;
}




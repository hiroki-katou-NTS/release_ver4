__viewContext.ready(function() {
    var screenModel = new nts.uk.pr.view.qpp021.a.viewmodel.ScreenModel();

    $(function() {
        var width = 1320;
        if ($(window).width() > 1366) {
            width = $(window).width() - 56;
        }
        $('#contents-area').width(width);
    });
    this.bind(screenModel);

});
module nts.uk.pr.view.ccg007.b {
    __viewContext.ready(function() {
        if ($('#contents-area').data('loaded')) {
            $('[id=contents-area]:eq(1)').remove();
            return;
        }
        $('#contents-area').data('loaded', true);
        var screenModel = new viewmodel.ScreenModel();
        screenModel.start().done(function() {
            //get url
            let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
            let isSignOn = url.indexOf('signon=on') >= 0 || url.indexOf('signon=oN') >= 0 || url.indexOf('signon=On') >= 0
            || url.indexOf('signon=ON') >= 0;
            if(!isSignOn){
                screenModel.displayLogin(true);
            }
            __viewContext.bind(screenModel);
            nts.uk.characteristics.restore("form1LoginInfo").done(function(loginInfo: any) {
                if (loginInfo) {
                    $('#password-input').focus();
                }
                else {
                    $('#login-id-inp').val();
                    $('#login-id-inp').focus();
                }
            });
            
        });
    });
}
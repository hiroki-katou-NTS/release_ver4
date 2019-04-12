module nts.uk.pr.view.ccg007.c {
    __viewContext.ready(function() {
        if ($('#contents-area').data('loaded')) {
            $('[id=contents-area]:eq(1)').remove();
            return;
        }
        $('#contents-area').data('loaded', true);
        var screenModel = new viewmodel.ScreenModel();
        screenModel.start().done(function() {
            __viewContext.bind(screenModel);
            nts.uk.characteristics.restore("form2LoginInfo").done(function(loginInfo: any) {
                if (!loginInfo || !loginInfo.companyCode) {
                    $('#company-code-inp').focus();
                }
                else {
                    if (!loginInfo.employeeCode) {
                        $('#employee-code-inp').focus();
                    }
                    else {
                        $('#password-input').focus();
                    }
                }
            });
        });
    });
}
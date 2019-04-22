module common.error.system {
    __viewContext.ready(function() {
        var screenModel = new ScreenModel();
        __viewContext.bind(screenModel);
    });
    
    class ScreenModel {
        constructor() {
        }
        
        gotoLogin() {
            nts.uk.characteristics.restore("loginMode").done(mode => {
                let rgc = nts.uk.ui.windows.rgc();
                if (mode) {
                    rgc.nts.uk.request.jump("com", "/view/ccg/007/d/index.xhtml?signon=on");
                } else {
                    rgc.nts.uk.request.login.jumpToUsedLoginPage();
                }
            });
        }
    }
}
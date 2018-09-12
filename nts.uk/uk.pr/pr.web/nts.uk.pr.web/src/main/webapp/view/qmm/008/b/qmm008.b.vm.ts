module nts.uk.com.view.qmm008.b.viewmodel {
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import dialog = nts.uk.ui.dialog;
    import getText = nts.uk.resource.getText;
    import modal = nts.uk.ui.windows.sub.modal;
    import block = nts.uk.ui.block;
    export class ScreenModel {
        constructor() {
            let self = this;
            let params = getShared("QMM008_G_PARAMS");
        } 
        register() {
            console.log('register');
        }
        printPDF() {
            console.log('printPDF');
        }
        registerBusinessEstablishment() {
            console.log('registerBusinessEstablishment');
        }
        standardRemunerationMonthlyAmount() {
            console.log('standardRemunerationMonthlyAmount');
        }
        masterCorrectionLog() {
            console.log('masterCorrectionLog');
        }
    } 
}


module nts.uk.at.view.kmw006.a.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        

        constructor() {
            var self = this;

            

        }

        startPage(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            dfd.resolve();
            return dfd.promise();
        }



        openKMW006fDialog() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            modal("/view/kmf/006/f/index.xhtml").onClosed(() => {
                var output = getShared("outputKAL003d");
                if (!nts.uk.util.isNullOrUndefined(output)) {
                    
                }
            });
        }


    }

}
module cps001.d.vm {
    import text = nts.uk.resource.getText;
    import alert = nts.uk.ui.dialog.alert;
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    let __viewContext: any = window['__viewContext'] || {};

    export class ViewModel {

        constructor() {
            let self = this,
                dto: IModelDto = getShared('CPS001B_PARAM') || {};

        }

        pushData() {
            let self = this;

            setShared('CPS001B_VALUE', {});
            self.close();
        }

        close() {
            close();
        }
    }

    interface IModelDto {
        
    }
}
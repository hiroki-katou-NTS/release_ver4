module cps008.c.viewmodel {

    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import close = nts.uk.ui.windows.close;

    export class ViewModel {
        layout: KnockoutObservable<Layout> = ko.observable(new Layout({ id: '', code: '', name: '' }));

        constructor() {
            let self = this,
                layout: Layout = self.layout();

            let _data = getShared('CPS008_PARAM');

            layout.id.subscribe(id => {
                // call service for get code, name of layout
                service.getDetails(id).done((data: any) => {
                    if (data) {
                        layout.code(data.layoutCode);
                        layout.name(data.layoutName);
                    }
                });
            });
            layout.id(_data.id);

            $("#C_INP_CODE").focus();
        }

        coppyBtn() {
            let self = this,
                layout: ILayout = ko.toJS(self.layout);

            if (layout.newCode == '' || layout.newName == '') {
                if (layout.newCode == '') {
                    $("#C_INP_CODE").focus();
                } else {
                    $("#C_INP_NAME").focus();
                }
                return;
            } else if (layout.newCode == layout.code) {
                nts.uk.ui.dialog.alert("#Msg355");
                return;
            } else if (layout.newCode && layout.newName) {
                setShared('CPS008C_RESPONE', { id: layout.id, code: layout.newCode, name: layout.newName, action: layout.overrideMode });
                close();
            } 
        }

        close() {
            setShared('CPS008C_RESPONE', null);
            close();
        }
    }

    interface ILayout {
        id: string;
        code: string;
        name: string;
        newCode?: string;
        newName?: string;
        overrideMode?: boolean;
    }

    class Layout {
        id: KnockoutObservable<string> = ko.observable('');
        code: KnockoutObservable<string> = ko.observable('');
        name: KnockoutObservable<string> = ko.observable('');

        newCode: KnockoutObservable<string> = ko.observable('');
        newName: KnockoutObservable<string> = ko.observable('');
        overrideMode: KnockoutObservable<boolean> = ko.observable(false);

        constructor(param: ILayout) {
            let self = this;

            if (param) {
                self.id(param.id || '');
                self.code(param.code || '');
                self.name(param.name || '');

                self.newCode(param.newCode || '');
                self.newName(param.newName || '');
                self.overrideMode(param.overrideMode || false);
            }
        }
    }
}
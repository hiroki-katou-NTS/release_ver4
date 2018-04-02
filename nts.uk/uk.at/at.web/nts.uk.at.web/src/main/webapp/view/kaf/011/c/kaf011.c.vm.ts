module nts.uk.at.view.kaf011.c.screenModel {

    import dialog = nts.uk.ui.dialog.info;
    import text = nts.uk.resource.getText;
    import formatDate = nts.uk.time.formatDate;
    import common = nts.uk.at.view.kaf011.shr.common;
    import service = nts.uk.at.view.kaf011.shr.service;
    import block = nts.uk.ui.block;

    export class ViewModel {
        prePostTypes = ko.observableArray([
            { code: 0, text: text('KAF011_14') },
            { code: 1, text: text('KAF011_15') }]);

        prePostSelectedCode: KnockoutObservable<number> = ko.observable(0);

        reason: KnockoutObservable<string> = ko.observable('');

        appReasons = ko.observableArray([]);

        appReasonSelectedID: KnockoutObservable<string> = ko.observable('');

        appDate: KnockoutObservable<String> = ko.observable(formatDate(moment().toDate(), "yyyy/MM/dd").format());

        kaf000_a = new kaf000.a.viewmodel.ScreenModel();

        constructor() {
            let self = this;
            let param = nts.uk.ui.windows.getShared('KAF_011_PARAMS');
            if (param) {
                self.prePostSelectedCode(param.prePostSelectedCode);
                self.reason(param.reason);
                self.appReasons(param.appReasons);
                self.appReasonSelectedID(param.appReasonSelectedID);
                self.appDate(param.appDate);
            }
        }

        start(): JQueryPromise<any> {
            block.invisible();
            var self = this,
                dfd = $.Deferred(),
                startParam = {
                    sID: null,
                    appDate: self.appDate(),
                    uiType: 0
                };

            service.start(startParam).done((data: common.IHolidayShipment) => {
                // self.setDataFromStart(data);

            }).fail((error) => {
                dialog({ messageId: error.messageId });
            }).always(() => {
                block.clear();
                dfd.resolve();

            });
            return dfd.promise();
        }

        closeDialog() {
            nts.uk.ui.windows.close();
        }

    }
}
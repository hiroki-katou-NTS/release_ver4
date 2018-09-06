module nts.uk.at.view.kmf004.j.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<ItemModel> = ko.observableArray([]);;
        columns: KnockoutObservableArray<NtsGridListColumn> = ko.observableArray([
            { headerText: nts.uk.resource.getText('KMF004_148'), key: 'code', width: 100, hidden: true },
            { headerText: nts.uk.resource.getText('KMF004_148'), key: 'name', width: 320 }
        ]);
        currentCodeList: KnockoutObservableArray<any> = ko.observableArray([]);
        selectedCode: KnockoutObservable<number> = ko.observable(null);

        constructor() {
            let self = this;
            self.currentCodeList(nts.uk.ui.windows.getShared("KMF004_A_TARGET_ITEMS"));
            self.selectedCode(nts.uk.ui.windows.getShared("KMF004_A_SELECTED_CD"));
            $("#data-items").focus();
        }

        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();

            service.findForScreenJ(self.selectedCode()).done(function(data) {
                self.items(_.map(data, (item) => {
                    return new ItemModel(item);
                }));
                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.dialog.alertError({ messageId: error.messageId });
            }).always(() => {
                dfd.resolve();
            });

            return dfd.promise();
        }


        submit() {
            let self = this;

            if (self.currentCodeList().length > 0) {
                nts.uk.ui.windows.setShared("KMF004_J_SELECTED_ITEMS", self.currentCodeList());
                nts.uk.ui.windows.close();
            } else {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_1268" });
            }
        }

        cancel() {
            nts.uk.ui.windows.close();
        }
    }

    class ItemModel {
        code: string;
        name: string;
        constructor(data) {
            if (data) {
                this.code = data.itemType+data.specialHdFrameNo;
                this.name = data.specialHdFrameName;
            }
        }
    }
}
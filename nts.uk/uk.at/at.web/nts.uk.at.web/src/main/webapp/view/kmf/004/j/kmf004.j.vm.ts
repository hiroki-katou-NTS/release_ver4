module nts.uk.at.view.kmf004.j.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<ItemModel> = ko.observableArray([]);;
        columns: KnockoutObservableArray<NtsGridListColumn> = ko.observableArray([
            { headerText: nts.uk.resource.getText('KMF004_101'), key: 'code', width: 100, hidden: true },
            { headerText: nts.uk.resource.getText('KMF004_101'), key: 'name', width: 320 }
        ]);
        currentCodeList: KnockoutObservableArray<any> = ko.observableArray([]);
        selectedCode: KnockoutObservable<number> = ko.observable(null);

        constructor() {
            let self = this,
            dataScreenA = nts.uk.ui.windows.getShared("KMF004_A_TARGET_ITEMS");
            self.currentCodeList(dataScreenA.currentCodeList);
            self.selectedCode(dataScreenA.selectedCode)
            $("#data-items").focus();
        }

        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred(),
                param = {
                    currentCode: self.selectedCode()
                };

            service.findForScreenJ(param).done(function(data) {
                self.items(_.sortBy(_.map(data, (item) => {
                    return new ItemModel(item);
                }), ['itemType','frameNo']));
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
            let NoDataCodes = [];
            //remove nodata code from code list
            _.forEach(self.currentCodeList(),(code)=>{
                let item = _.find(self.items(), function(o) { return o.code == code; });
                if(!item){
                    NoDataCodes.push(code);
                }
            });
            _.forEach(NoDataCodes, (code) => {
                let index = self.currentCodeList().indexOf(code);
                self.currentCodeList().splice(index, 1);
            });
            
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
        frameNo: number;
        itemType: string;
        name: string;
        constructor(data) {
            if (data) {
                this.code = data.itemType+data.specialHdFrameNo;
                this.itemType = data.itemType;
                this.frameNo = data.specialHdFrameNo
                this.name = data.specialHdFrameName;
            }
        }
    }
}
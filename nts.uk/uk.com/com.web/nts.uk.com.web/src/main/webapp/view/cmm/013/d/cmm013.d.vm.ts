module cmm013.d.viewmodel {
    export class ScreenModel {
        inp_003: KnockoutObservable<string>;
        inp_003_enable: KnockoutObservable<boolean>;
        endDateUpdate: KnockoutObservable<string>;
        //D_SEL_001
        itemList: KnockoutObservableArray<any>;
        selectedId: KnockoutObservable<number>;
        enable: KnockoutObservable<boolean>;
        lstMessage: KnockoutObservableArray<ItemMessage>;
        histIdUpdate: KnockoutObservable<string>;
        oldStartDate: KnockoutObservable<string>;
        oldEndDate: KnockoutObservable<string>;
        jobCode: KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.inp_003 = ko.observable(null);
            self.inp_003_enable = ko.observable(true);
            self.endDateUpdate = ko.observable('');
            //D_SEL_001
            self.enable = ko.observable(true);
            self.oldStartDate = ko.observable('');
            self.lstMessage = ko.observableArray([]);
            self.histIdUpdate = ko.observable('');
            self.oldEndDate= ko.observable('');
            self.jobCode= ko.observable('');
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.endDateUpdate(nts.uk.ui.windows.getShared('cmm013EndDate'));
            self.oldEndDate(nts.uk.ui.windows.getShared('cmm013OldEndDate'));
            self.oldStartDate(nts.uk.ui.windows.getShared('cmm013StartDate'))
            self.histIdUpdate(nts.uk.ui.windows.getShared('cmm013HistoryId'));
            self.jobCode(nts.uk.ui.windows.getShared('cmm013JobCode'));
            self.listMessage();
            self.setValueForRadio();
            self.selectedId.subscribe(function(newValue) {
                if (newValue == 1) {
                    self.inp_003_enable(false);
                } else {
                    self.inp_003_enable(true);
                }
            })
            self.inp_003(self.oldStartDate());
            //nếu không phải lịch sử mới nhat thi khong duoc xoa va chỉ mặc định được sửa thôi
            if (self.oldEndDate() === "9999/12/31") {
                self.enable(true);
            } else {
                self.enable(false);
            }
            dfd.resolve();
            return dfd.promise();
        }

        setValueForRadio(): any {
            var self = this;
            self.itemList = ko.observableArray([
                new BoxModel(1, '履歴を削除する '),
                new BoxModel(2, '履歴を修正する')
            ]);
            self.selectedId = ko.observable(2);
        }
        //<---list  message--->
        listMessage(): any {
            var self = this;
            self.lstMessage.push(new ItemMessage("ER005", "入力した*は既に存在しています。\r\n*を確認してください。"));
            self.lstMessage.push(new ItemMessage("ER010", "対象データがありません。"));
            self.lstMessage.push(new ItemMessage("AL001", "変更された内容が登録されていません。\r\nよろしいですか。"));
            self.lstMessage.push(new ItemMessage("AL002", "データを削除します。\r\nよろしいですか？"));
            self.lstMessage.push(new ItemMessage("ER026", "更新対象のデータが存在しません。"));
            self.lstMessage.push(new ItemMessage("ER023", "履歴の期間が重複しています。"));
        }
        //<---clear set share--->
        clearShared(): any {
            nts.uk.ui.windows.setShared('cmm013StartDate', '');
            nts.uk.ui.windows.setShared('cmm013EndDate', '');
            nts.uk.ui.windows.setShared('cmm013HistoryId', '');
        }
        //<---close dialog--->       
        closeDialog() {
            nts.uk.ui.windows.setShared('cancelDialog', true);
            nts.uk.ui.windows.close();
        }
        positionHis() {
            let self = this;
            //<---If delete--->
            var historyInfo = new model.ListHistoryDto(self.histIdUpdate(), self.oldStartDate(), self.inp_003(),self.jobCode());
            if (self.selectedId() === 1 ) {
                //<---Delete?--->
                
                var AL002 = _.find(self.lstMessage(), function(mess) {
                    return mess.messCode === "AL002";
                })
                nts.uk.ui.dialog.confirm(AL002.messName).ifCancel(function() {
                    return;
                }).ifYes(function() {
                    service.deleteHistory(historyInfo).fail(function(res: any) {
                        var delMess = _.find(self.lstMessage(), function(mess) {
                            return mess.messCode === "ER005";
                        })
                        nts.uk.ui.dialog.alert(delMess.messName);
                    }).done(function() {
                        //<---Close Screen--->
                        self.clearShared();
                        nts.uk.ui.windows.close();
                    })
                })
                //<---Edit--->
            } else {
                //Check Input New StartDate <= Now StartDate 
                var originallyStartDate = new Date(self.oldStartDate());
                var newStartDate = new Date(self.inp_003());
                if (originallyStartDate >= newStartDate) {
                    var AL023 = _.find(self.lstMessage(), function(mess) {
                        return mess.messCode === "ER023";
                    })
                    nts.uk.ui.dialog.alert(AL023.messName);
                } else {
                    service.updateHistory(historyInfo).fail(function(res: any) {
                        var upMess = _.find(self.lstMessage(), function(mess) {
                            return mess.messCode === res.message;
                        })
                        nts.uk.ui.dialog.alert(upMess.messName);
                    }).done(function() {
                        //<---Close Screen--->
                        self.clearShared();
                        nts.uk.ui.windows.close();
                    })
                }
            }
        }
    }

    class BoxModel {
        id: number;
        name: string;
        constructor(id: number, name: string) {
            this.id = id;
            this.name = name;
        }
    }

    export module model {
        export class ListHistoryDto {
            historyId: string;
            oldStartDate: string;
            newStartDate: string;
            jobCode: string;
            constructor(historyId: string, oldStartDate: string, newStartDate: string, jobCode: string) {
                this.historyId = historyId;
                this.oldStartDate = oldStartDate;
                this.newStartDate = newStartDate;
                this.jobCode = jobCode;
            }
        }
    }
    export class ItemMessage {
        messCode: string;
        messName: string;
        constructor(messCode: string, messName: string) {
            this.messCode = messCode;
            this.messName = messName;
        }
    }
}

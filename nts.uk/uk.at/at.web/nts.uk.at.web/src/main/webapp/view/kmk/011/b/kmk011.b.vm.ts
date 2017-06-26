module kmk011.b.viewmodel {
    export class ScreenModel {
        //A_label_x
        columns: KnockoutObservableArray<any>;
        dataSource: KnockoutObservableArray<model.Item>;
        currentCode: KnockoutObservable<string>;
        switchUSe3: KnockoutObservableArray<any>;
        requiredAtr: KnockoutObservable<any>;
        divReasonCode: KnockoutObservable<string>;
        divReasonContent: KnockoutObservable<string>;
        enableCode: KnockoutObservable<boolean>;
        itemDivReason: KnockoutObservable<model.Item>;
        divTimeId: KnockoutObservable<string>;
        index_of_itemDelete: any;
        objectOld: any;
        enableDel: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.currentCode = ko.observable('');
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMK011_37'), key: 'divReasonCode', width: 100 },
                { headerText: nts.uk.resource.getText('KMK011_38'), key: 'divReasonContent', width: 200 }
            ]);
            self.dataSource = ko.observableArray([]);
            self.switchUSe3 = ko.observableArray([
                { code: '1', name: nts.uk.resource.getText("Enum_DivergenceReasonInputRequiredAtr_Required") },
                { code: '0', name: nts.uk.resource.getText("Enum_DivergenceReasonInputRequiredAtr_Optional") },
            ]);
            self.requiredAtr = ko.observable(0);
            self.divReasonCode = ko.observable('');
            self.divReasonContent = ko.observable('');
            self.enableCode = ko.observable(false);
            self.itemDivReason = ko.observable(null);
            self.divTimeId = ko.observable(null);
            self.enableDel = ko.observable(true);
            //subscribe currentCode
            self.currentCode.subscribe(function(codeChanged) {
                self.clearError();
                self.itemDivReason(self.findItemDivTime(codeChanged));
                if (self.itemDivReason() === undefined || self.itemDivReason() == null) {
                    return;
                }
                self.objectOld = self.itemDivReason().divReasonCode + self.itemDivReason().divReasonContent + self.itemDivReason().requiredAtr;
                self.enableCode(false);
                self.divReasonCode(self.itemDivReason().divReasonCode);
                self.divReasonContent(self.itemDivReason().divReasonContent);
                self.requiredAtr(self.itemDivReason().requiredAtr);
                self.enableDel(true);
            });
        }

        /**
         * start page
         * get all divergence reason
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            self.currentCode('');
            var dfd = $.Deferred();
            self.divTimeId(nts.uk.ui.windows.getShared("KMK011_divTimeId"));
            service.getAllDivReason(self.divTimeId()).done(function(lstDivReason: Array<model.Item>) {
                if (lstDivReason === undefined || lstDivReason.length == 0) {
                    self.dataSource([]);
                    self.enableCode(true);
                } else {
                    self.dataSource(lstDivReason);
                    let reasonFirst = _.first(lstDivReason);
                    self.currentCode(reasonFirst.divReasonCode);
                }
                dfd.resolve();
            })
            return dfd.promise();
        }
        /**
         * find item Divergence Time is selected
         */
        findItemDivTime(value: string): any {
            let self = this;
            var itemModel = null;
            return _.find(self.dataSource(), function(obj: model.Item) {
                return obj.divReasonCode == value;
            })
        }
        refreshData() {
            var self = this;
            self.divReasonCode(null);
            self.divReasonContent("");
            self.requiredAtr(0);
            self.enableCode(true);
            self.clearError();
            self.enableDel(false);
            self.currentCode(null);
            $("#inpCode").focus();
            
        }

        clearError(): void {
            if ($('.nts-editor').ntsError("hasError")) {
                $('.nts-input').ntsError('clear');
            }
        }
        RegistrationDivReason() {
            var self = this;
            $('.nts-input').trigger("validate");
            _.defer(() => {
                if (!$('.nts-editor').ntsError("hasError")) {
                    if (self.enableCode() == false) {
                        let objectNew = self.convertCode(self.divReasonCode()) + self.divReasonContent() + self.requiredAtr();
                        if (self.objectOld == objectNew) {
                            return;
                        }
                        self.updateDivReason();
                    } else
                        if (self.enableCode() == true) {//add divergence
                            self.addDivReason();
                        }
                }
            });
        }

        addDivReason() {
            var self = this;
            var dfd = $.Deferred();
            self.convertCode(self.divReasonCode());
            var divReason = new model.Item(self.divTimeId(), self.divReasonCode(), self.divReasonContent(), self.requiredAtr());
            service.addDivReason(divReason).done(function() {
                nts.uk.ui.dialog.alert(nts.uk.resource.getMessage('Msg_15'));
                self.getAllDivReasonNew();
            }).fail(function(error) {
                $('#inpCode').ntsError('set', error);
            });
        }
        convertCode(value: string) {
            var self = this;
            if (value.length == 1) {
                let code = '0' + value;
                self.divReasonCode(code);
            }
            else return;
        }
        updateDivReason() {
            var self = this;
            var dfd = $.Deferred();
            var divReason = new model.Item(self.divTimeId(), self.divReasonCode(), self.divReasonContent(), self.requiredAtr());
            service.updateDivReason(divReason).done(function() {
                self.getAllDivReasonNew();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject(res);
            });
        }
        //get all divergence reason new
        getAllDivReasonNew() {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivReason(self.divTimeId()).done(function(lstDivReason: Array<model.Item>) {
                self.currentCode('');
                self.dataSource(lstDivReason);
                self.enableCode(false);
                self.currentCode(self.divReasonCode());
                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();
        }
        //delete divergence reason
        deleteDivReason() {
            var self = this;
            nts.uk.ui.dialog.confirm(nts.uk.resource.getMessage('Msg_18')).ifYes(function() {
                let divReason = self.itemDivReason();
                self.index_of_itemDelete = self.dataSource().indexOf(self.itemDivReason());
                service.deleteDivReason(divReason).done(function() {
//                    self.getDivReasonList_afterDelete();
                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage('Msg_16')).then(function(){
//                        $("#inpCode").focus();
                        self.getDivReasonList_afterDelete();
                         $("#inpCode").focus();
//                        self.refreshData();
                    });
                });
            }).ifNo(function() {
                return;
            })
        }
        //get list divergence reason after Delete 1 divergence reason
        getDivReasonList_afterDelete(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivReason(self.divTimeId()).done(function(lstDivReason: Array<model.Item>) {
                self.dataSource(lstDivReason);

                if (self.dataSource().length > 0) {
                    if (self.index_of_itemDelete === self.dataSource().length) {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete - 1].divReasonCode)
                    } else {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete].divReasonCode)
                    }

                } else {
                    self.refreshData();
                }

                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();

        }
        closeDialog() {
            nts.uk.ui.windows.close();
        }
    }
    export module model {
        export class Item {
            divTimeId: number;
            divReasonCode: string;
            divReasonContent: string;
            requiredAtr: number;
            constructor(divTimeId: number, divReasonCode: string, divReasonContent: string, requiredAtr: number) {
                this.divTimeId = divTimeId;
                this.divReasonCode = divReasonCode;
                this.divReasonContent = divReasonContent;
                this.requiredAtr = requiredAtr;
            }
        }
    }
}
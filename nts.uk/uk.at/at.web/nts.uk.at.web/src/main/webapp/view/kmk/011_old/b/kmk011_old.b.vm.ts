module kmk011_old.b.viewmodel {
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
        divTimeId: KnockoutObservable<number>;
        index_of_itemDelete: any;
        objectOld: any;
        enableDel: KnockoutObservable<boolean>;
        checkModel: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.currentCode = ko.observable('');
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMK011_22'), key: 'divReasonCode',formatter: _.escape, width: 100 },
                { headerText: nts.uk.resource.getText('KMK011_23'), key: 'divReasonContent',formatter: _.escape, width: 200 }
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
            self.checkModel = ko.observable(true);
            //subscribe currentCode
            self.currentCode.subscribe(function(codeChanged) {
//                var t0 = performance.now();   
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
                $("#inpReason").focus();
//                var t1 = performance.now();
//                console.log("Selection process " + (t1 - t0) + " milliseconds.");
            });
        }

        /**
         * start page
         * get all divergence reason
         */
        startPage(): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            var self = this;
            self.currentCode('');
            var dfd = $.Deferred();
            var id = nts.uk.ui.windows.getShared("KMK011_divTimeId")
            if(id==null){
                self.divTimeId(0);
            }else{
                self.divTimeId(id);
            }
            service.getAllDivReason(self.divTimeId().toString()).done(function(lstDivReason: Array<model.Item>) {
                nts.uk.ui.block.clear();
                self.currentCode(null);
                if (id==null||lstDivReason === undefined || lstDivReason.length == 0) {
                    self.dataSource([]);
                    self.enableCode(true);
                    self.checkModel(false);
                    self.enableDel(false);
                } else {
                    self.dataSource(lstDivReason);
                    let reasonFirst = _.first(lstDivReason);
                    self.currentCode(reasonFirst.divReasonCode);
                    self.checkModel(true);
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
            nts.uk.ui.block.invisible();
            var self = this;
            $('.nts-input').trigger("validate");
            _.defer(() => {
                if (!$('.nts-editor').ntsError("hasError")) {
                    if (self.enableCode() == false) {
                        self.convertCode(self.divReasonCode());
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
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                self.getAllDivReasonNew();
                $("#inpReason").focus();
                nts.uk.ui.block.clear();
            }).fail(function(error) {
                nts.uk.ui.block.clear();
                $('#inpCode').ntsError('set', error);
            });
        }
        convertCode(value: string) {
            var self = this;
            if (value.length == 1) {
                let code = '0' + value;
                self.divReasonCode(code);
            }
            else self.divReasonCode(value);
        }
        updateDivReason() {
            var self = this;
            var dfd = $.Deferred();
            var divReason = new model.Item(self.divTimeId(), self.divReasonCode(), self.divReasonContent(), self.requiredAtr());
            service.updateDivReason(divReason).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function(){
                    self.getAllDivReasonNew();
                    nts.uk.ui.block.clear();
                    });;
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject(res);
                nts.uk.ui.block.clear();
            });
        }
        //get all divergence reason new
        getAllDivReasonNew() {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivReason(self.divTimeId().toString()).done(function(lstDivReason: Array<model.Item>) {
                self.currentCode('');
                self.dataSource(lstDivReason);
                self.enableCode(false);
                self.currentCode(self.divReasonCode());
                dfd.resolve();
                 $("#inpReason").focus();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();
        }
        //delete divergence reason
        deleteDivReason() {
            nts.uk.ui.block.invisible();
            var self = this;
            nts.uk.ui.dialog.confirm({messageId:'Msg_18'}).ifYes(function() {
                let divReason = self.itemDivReason();
                self.index_of_itemDelete = self.dataSource().indexOf(self.itemDivReason());
                service.deleteDivReason(divReason).done(function() {
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function(){
                        nts.uk.ui.block.clear();
                        self.getDivReasonList_afterDelete();
                         $("#inpCode").focus();
                    });
                });
            }).ifNo(function() {
                nts.uk.ui.block.clear();
                return;
            })
        }
        //get list divergence reason after Delete 1 divergence reason
        getDivReasonList_afterDelete(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivReason(self.divTimeId().toString()).done(function(lstDivReason: Array<model.Item>) {
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
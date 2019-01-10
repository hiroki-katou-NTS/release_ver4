module nts.uk.at.view.kmk011.c.viewmodel {
    import blockUI = nts.uk.ui.block;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {
        //A_label_x
        columns: KnockoutObservableArray<any>; //define C2_1
        dataSource: KnockoutObservableArray<model.DivergenceReason>;// list C2_1
        currentCode: KnockoutObservable<string>; // currentCode
        switchUSe3: KnockoutObservableArray<any>; //list value swithButton c3_5
        requiredAtr: KnockoutObservable<any>; //value C3_5
        divReasonCode: KnockoutObservable<string>; // show value divergenceReasonCode in C3_2
        divReasonContent: KnockoutObservable<string>; //show value C3_3 input tex
        enableCode: KnockoutObservable<boolean>; //define for enable code (=true with NEW_MODE)
        enableContent: KnockoutObservable<boolean>; //define for enable content
        enableMode: KnockoutObservable<boolean>;//define for enable content mode for switchButton
        itemDivReason: KnockoutObservable<model.DivergenceReason>;
        divTimeId: KnockoutObservable<number>;
        index_of_itemDelete: any;
        objectOld: any;
        enableDel: KnockoutObservable<boolean>;
        checkModel: KnockoutObservable<boolean>;
        textNotice01: KnockoutObservable<any>;
        textNotice02: KnockoutObservable<any>;
        mode: boolean;

        constructor() {
            var self = this;
            self.currentCode = ko.observable('');
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMK011_22'), key: 'divergenceReasonCode', formatter: _.escape, width: 100 },
                { headerText: nts.uk.resource.getText('KMK011_23'), key: 'reason', formatter: _.escape, width: 200 }
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
            self.enableContent = ko.observable(true);
            self.enableMode = ko.observable(true);
            //subscribe currentCode
            self.currentCode.subscribe((codeChanged) => {
                if (codeChanged != "") {
                    self.clearError();
                    //get data
                    self.itemDivReason(self.findItemDivTime(codeChanged));

                    //check null data
                    if (self.itemDivReason() === undefined || self.itemDivReason() == null) {
                        return;
                    }

                    //fill data
                    self.enableCode(false);
                    self.enableContent(true);
                    self.divReasonCode(self.itemDivReason().divergenceReasonCode);
                    self.divReasonContent(self.itemDivReason().reason);
                    self.requiredAtr(self.itemDivReason().reasonRequired);

                    self.enableDel(true);
                    if (self.mode) {
                        self.enableMode(true);
                    }
                    else
                        self.enableMode(false);

                    $("#inpReason").focus();
                }
                else {
                    //Disable and blank content
                    self.divReasonCode("");
                    self.divReasonContent("");
                    self.enableCode(false);
                    self.enableContent(false);
                    self.enableDel(false);
                    self.enableMode(false);
                }
            });

            var notice = nts.uk.resource.getText("KMK011_78");

            var index = notice.indexOf("め、");

            var text1 = notice.substr(0, index + 2);

            var text2 = notice.substr(index + 3);

            self.textNotice01 = ko.observable(text1);

            self.textNotice02 = ko.observable(text2);

            // set C3_17
            self.mode = nts.uk.ui.windows.getShared('selectInput');
        }

        /**
        * start page
        * get all divergence reason
        */
        start_page(): JQueryPromise<any> {
            blockUI.invisible();
            var self = this;
            self.currentCode('');
            var dfd = $.Deferred();
            var id = getShared("KMK011_divTimeId")
            if (id == null) {
                self.divTimeId(0);
            } else {
                self.divTimeId(id);
            }
            service.getAllDivReason(self.divTimeId().toString()).then(function(lstDivReason: Array<model.DivergenceReason>) {
                blockUI.clear();
                self.currentCode(null);
                if (id == null || lstDivReason === undefined || lstDivReason.length == 0) {
                    self.dataSource([]);
                    self.enableCode(true);
                    self.checkModel(false);
                    self.enableDel(false);
                } else {
                    self.dataSource(lstDivReason);
                    let reasonFirst = _.first(lstDivReason);
                    self.currentCode(reasonFirst.divergenceReasonCode);
                    self.checkModel(true);
                }
                dfd.resolve();
                if (id == null || lstDivReason === undefined || lstDivReason.length == 0) {

                    setTimeout(function() {
                        $("#inpCode").focus();
                    }, 100);

                }
                else {

                    setTimeout(function() {
                        $("#inpReason").focus();
                    }, 100);

                }
                if (self.mode) {
                    self.enableMode(true);
                }
                else
                    self.enableMode(false);

            })
            $('#inpReason').focus(function() {
                var temp = $(this).val();
                $(this).val('').select();
                $(this).val(temp);
            });
            return dfd.promise();
        }
        /**
        * find item Divergence Time is selected
        */
        findItemDivTime(value: string): any {
            let self = this;
            var itemModel: any = null;
            return _.find(self.dataSource(), function(obj: model.DivergenceReason) {
                return obj.divergenceReasonCode == value;
            })
        }
        refreshData() {
            var self = this;
            self.divReasonCode(null);
            self.divReasonContent("");
            self.requiredAtr(0);
            self.enableCode(true);
            self.enableContent(true);
            self.clearError();
            self.enableDel(false);
            self.currentCode(null);
            if (self.mode) {
                self.enableMode(true);
            }
            else
                self.enableMode(false);
            $("#inpCode").focus();

        }

        clearError(): void {
            if ($('.nts-editor').ntsError("hasError")) {
                $('.nts-input').ntsError('clear');
            }
        }
        RegistrationDivReason() { //C1_2 click function

            blockUI.invisible();
            var self = this;

            $('.nts-input').trigger("validate");
            _.defer(() => {
                if (!$('.nts-editor').ntsError("hasError")) {
                    if (!nts.uk.text.isNullOrEmpty(self.divReasonCode()) && !nts.uk.text.isNullOrEmpty(self.divReasonContent())) {
                        if (self.enableCode() == false) {
                            self.convertCode(self.divReasonCode());
                            self.updateDivReason();
                        } else {
                            if (self.enableCode() == true) {//add divergence
                                self.addDivReason();
                            }
                        }
                    }
                }
            });

            blockUI.clear();
            $("#inpReason").focus();
        }

        addDivReason() {
            var self = this;
            var dfd = $.Deferred();
            self.convertCode(self.divReasonCode());
            var obj = self.findItemDivTime(self.divReasonCode());
            if (obj != null) {
                nts.uk.ui.dialog.info({ messageId: "Msg_3" });
                blockUI.clear();
                $('#inpCode').trigger("validate");
                return;
            }
            var divReason = new model.DivergenceReason(self.divTimeId(), self.divReasonCode(), self.divReasonContent(), self.requiredAtr());
            service.addDivReason(divReason).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    self.getAllDivReasonNew(divReason.divergenceReasonCode).done(function() {
                        $("#inpReason").focus();
                    })
                    blockUI.clear();
                });
            }).fail(function(error) {
                blockUI.clear();
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
            var divReason = new model.DivergenceReason(self.divTimeId(), self.divReasonCode(), self.divReasonContent(), self.requiredAtr());
            service.updateDivReason(divReason).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    self.getAllDivReasonNew(divReason.divergenceReasonCode).done(function() {
                        $("#inpReason").focus();
                    })
                    blockUI.clear();
                });
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject(res);
                blockUI.clear();
            });
        }
        //get all divergence reason new
        getAllDivReasonNew(code: string) {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivReason(self.divTimeId().toString()).done(function(lstDivReason: Array<model.DivergenceReason>) {
                self.dataSource(lstDivReason);
                self.enableCode(false);
                self.currentCode(code);
                $("#inpReason").focus();
                self.enableDel(true);
                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();
        }
        //delete divergence reason
        deleteDivReason() {
            blockUI.invisible();
            var self = this;
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                //                let divReason = self.itemDivReason();
                var divReason = new model.DivergenceReason(self.divTimeId(), self.divReasonCode(), self.divReasonContent(), self.requiredAtr());
                self.index_of_itemDelete = self.dataSource().indexOf(self.itemDivReason());
                service.deleteDivReason(divReason).done(function() {
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                        blockUI.clear();
                        self.getDivReasonList_afterDelete();
                        $("#inpReason").focus();
                    });
                });
            }).ifNo(function() {
                blockUI.clear();
                return;
            })
        }
        //get list divergence reason after Delete 1 divergence reason
        getDivReasonList_afterDelete(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivReason(self.divTimeId().toString()).done(function(lstDivReason: Array<model.DivergenceReason>) {
                self.dataSource(lstDivReason);

                if (self.dataSource().length > 0) {
                    if (self.index_of_itemDelete === self.dataSource().length) {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete - 1].divergenceReasonCode)
                    } else {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete].divergenceReasonCode)
                    }
                } else {
                    self.refreshData();
                }
                if (_.isEmpty(self.dataSource())) {
                    self.enableDel(false);
                }
                else self.enableDel(true);
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
        export class DivergenceReason {
            divergenceTimeNo: number;
            divergenceReasonCode: string;
            reason: string;
            reasonRequired: number;
            constructor(divTimeId: number, divReasonCode: string, divReasonContent: string, requiredAtr: number) {
                this.divergenceTimeNo = divTimeId;
                this.divergenceReasonCode = divReasonCode;
                this.reason = divReasonContent;
                this.reasonRequired = requiredAtr;
            }
        }
    }
}
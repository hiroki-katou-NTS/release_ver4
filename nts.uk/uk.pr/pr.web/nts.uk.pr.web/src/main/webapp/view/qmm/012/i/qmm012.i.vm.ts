module nts.uk.pr.view.qmm012.i.viewmodel {
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import block = nts.uk.ui.block;
    import errors = nts.uk.ui.errors;
    import dialog = nts.uk.ui.dialog;
    import model = nts.uk.pr.view.qmm012.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {

        lstBreakdownItemSet: KnockoutObservableArray<model.IBreakdownItemSet> = ko.observableArray([]);
        currentCode: KnockoutObservable<string> = ko.observable('');
        currentBreakdownItemSet: KnockoutObservable<model.BreakdownItemSet> = ko.observable(null);

        isNewMode: KnockoutObservable<boolean> = ko.observable(true);
        salaryItemId: KnockoutObservable<string> = ko.observable('salary1');
        enableCode: KnockoutObservable<boolean> = ko.observable(true);
        bindAtr: KnockoutObservable<string> = ko.observable('bindAtr');

        breakdownItemCode: KnockoutObservable<string> = ko.observable('');
        breakdownItemName: KnockoutObservable<string> = ko.observable('');
        constructor() {
            let self = this,
                dfd = $.Deferred();

            self.currentCode.subscribe((item) => {
                if (item != '') {
                    let itemModel = _.find(self.lstBreakdownItemSet(), function(x) { return x.breakdownItemCode == item });
                    self.breakdownItemCode(itemModel.breakdownItemCode);
                    self.breakdownItemName(itemModel.breakdownItemName);
                    self.isNewMode(false);
                    self.enableCode(true);
                    $("#breakdownItemName").focus();
                    nts.uk.ui.errors.clearAll();
                }
            });
            block.invisible();
            service.getAllBreakdownItemSetById(self.salaryItemId()).done(function(data: Array<model.IBreakdownItemSet>) {
                if (data && data.length > 0) {
                    let dataSort = _.sortBy(data, ["breakdownItemCode"]);
                    self.lstBreakdownItemSet(dataSort);
                    self.currentCode(self.lstBreakdownItemSet()[0].breakdownItemCode);
                    self.isNewMode(false);
                }
                else {
                    //new mode
                    self.createItemSet();
                }
                dfd.resolve(self);
            }).fail(function(error) {
                alertError(error);
                dfd.reject();
            }).always(() => {
                block.clear();
            });

        }

        createItemSet() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            self.breakdownItemCode('');
            self.breakdownItemName('');
            self.currentCode('');
            self.isNewMode(true);
            self.enableCode(false);
            $("#breakdownItemCode").focus();
        }

        saveItemSet() {
            let self = this;
            let data = {
                salaryItemId: self.salaryItemId(),
                breakdownItemCode: self.breakdownItemCode(),
                breakdownItemName: self.breakdownItemName()
            };
            $("#breakdownItemCode").trigger("validate");
            $("#breakdownItemName").trigger("validate");
            if (errors.hasError() === false) {
                block.invisible();
                if (self.isNewMode()) {
                    // create 
                    service.addBreakdownItemSet(ko.toJS(data)).done(() => {
                        self.getAllData().done(() => {
                            dialog.info({ messageId: "Msg_15" }).then(() => {
                                $("#breakdownItemCode").focus();
                                self.isNewMode(false);
                                self.currentCode(data.breakdownItemCode);
                            });
                        });
                    }).fail(function(error) {
                        alertError(error);
                    }).always(function() {
                        $("#breakdownItemCode").focus();
                        block.clear();
                    });
                } else {
                    // update
                    service.updateBreakdownItemSet(ko.toJS(data)).done(() => {
                        self.getAllData().done(() => {
                            dialog.info({ messageId: "Msg_15" }).then(() => {
                                self.isNewMode(false);
                                self.currentCode(data.breakdownItemCode);
                            });
                        });
                    }).fail(function(error) {
                        alertError(error);
                    }).always(function() {
                        $("#breakdownItemCode").focus();
                        block.clear();
                    });
                }
            }
        }

        deleteItemSet() {
            let self = this;
            let data = {
                salaryItemId: self.salaryItemId(),
                breakdownItemCode: self.breakdownItemCode(),
                breakdownItemName: self.breakdownItemName()
            };
            block.invisible();
            dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                if (data.breakdownItemCode) {
                    let index: number = _.findIndex(self.lstBreakdownItemSet(), function(x)
                    { return x.breakdownItemCode == data.breakdownItemCode });
                    service.removeBreakdownItemSet(ko.toJS(data)).done(function() {
                        self.getAllData().done(() => {
                            dialog.info({ messageId: "Msg_16" }).then(() => {
                                if (self.lstBreakdownItemSet().length == 0) {
                                    self.createItemSet();
                                } else {
                                    if (index == self.lstBreakdownItemSet().length) {
                                        self.currentCode(self.lstBreakdownItemSet()[index - 1].breakdownItemCode);
                                    } else {
                                        self.currentCode(self.lstBreakdownItemSet()[index].breakdownItemCode);
                                    }
                                }
                            });
                        });

                    }).fail(function(error) {
                        dialog.alertError({ messageId: error.messageId });
                        block.clear();
                    }).always(function() {
                        block.clear();
                    });
                } else {
                    block.clear();
                }
            }).then(() => {
                $('.nts-input').ntsError('clear');
                nts.uk.ui.errors.clearAll();
                block.clear();
            });;
        }
        close() {
            nts.uk.ui.windows.close();
        }

        getAllData(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            block.invisible();
            self.lstBreakdownItemSet.removeAll();
            service.getAllBreakdownItemSetById(self.salaryItemId()).done(function(data: Array<model.IBreakdownItemSet>) {
                if (data && data.length > 0) {
                    let dataSort = _.sortBy(data, ["breakdownItemCode"]);
                    self.lstBreakdownItemSet(dataSort);
                    self.currentCode(self.lstBreakdownItemSet()[0].breakdownItemCode);
                }
                else {
                    nts.uk.ui.errors.clearAll();
                    self.breakdownItemCode('');
                    self.breakdownItemName('');
                    self.isNewMode(true);
                }
                block.clear();
                dfd.resolve(self);
            }).fail(function(res) {
                alertError({ messageId: res.messageId });
                block.clear();
                dfd.reject();
            });
            return dfd.promise();
        }
    }
}
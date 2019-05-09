module nts.uk.at.view.kal003.a.tab {
    import windows = nts.uk.ui.windows;
    import dialog = nts.uk.ui.dialog;
    import getText = nts.uk.resource.getText;
    import block = nts.uk.ui.block;
    import model = nts.uk.at.view.kal003.share.model;
    import shareutils = nts.uk.at.view.kal003.share.kal003utils;
    import info = nts.uk.ui.dialog.info;

    export class CheckConditionTab {
        listWorkRecordExtractingConditions: KnockoutObservableArray<model.WorkRecordExtractingCondition> = ko.observableArray([]);
        isAllCheckCondition: KnockoutObservable<boolean> = ko.observable(false);
        currentRowSelected: KnockoutObservable<number> = ko.observable(0);

        itemList: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getSchedule4WeekAlarmCheckCondition());
        // schedule4WeekCheckCondition: KnockoutObservable<number> = ko.observable(model.SCHEDULE_4_WEEK_CHECK_CONDITION.FOR_ACTUAL_RESULTS_ONLY);
        schedule4WeekCheckCondition: KnockoutObservable<number>;
        list4weekClassEnum: KnockoutObservableArray<any>;

        category: KnockoutObservable<number>;
        checkUseAtr: KnockoutObservable<boolean> = ko.observable(false);
        checkUseAtr57: KnockoutObservable<boolean> = ko.observable(false);

        //MinhVV
        listMulMonCheckSet: KnockoutObservableArray<model.MulMonCheckCondSet> = ko.observableArray([]);
        
        //
        userAtrCheck: KnockoutObservableArray<any>; 

        constructor(category: number, listWorkRecordExtractingConditions?: Array<model.WorkRecordExtractingCondition>, listMulMonCheckSet?: Array<model.MulMonCheckCondSet>, schedule4WeekCheckCondition?: number) {
            let self = this;
            
            self.userAtrCheck = ko.observableArray([
                { code: '1', name: getText('KAL003_189'), width: 60 },
                { code: '0', name: getText('KAL003_190'), width: 60 }
            ]);
            
            self.category = ko.observable(category);

            if (listWorkRecordExtractingConditions) {
                self.listWorkRecordExtractingConditions(listWorkRecordExtractingConditions);
                //                for (var i = 0; i < self.listWorkRecordExtractingConditions().length; i++) {
                //                    self.listWorkRecordExtractingConditions()[i].rowId(i + 1);
                //                }
            }

            self.listWorkRecordExtractingConditions.subscribe(() => {
                for (var i = 0; i < self.listWorkRecordExtractingConditions().length; i++) {
                    self.listWorkRecordExtractingConditions()[i].rowId(i + 1);
                }
            });
            // MinhVV add
            if (listMulMonCheckSet) {
                self.listMulMonCheckSet(listMulMonCheckSet);
            }
            self.listMulMonCheckSet.subscribe(() => {
                for (var i = 0; i < self.listMulMonCheckSet().length; i++) {
                    self.listMulMonCheckSet()[i].rowId(i + 1);
                }
            });
            // schedule4WeekCheckCondition
            self.list4weekClassEnum = ko.observableArray(__viewContext.enums.FourW4DCheckCond);
            self.schedule4WeekCheckCondition = ko.observable(0);
            if (schedule4WeekCheckCondition) {
                self.schedule4WeekCheckCondition(schedule4WeekCheckCondition);
            }

//            $("#check-condition-table").ntsFixedTable({ height: 300 });
            // MinhVV add
//            $("#check-condition-table_category9").ntsFixedTable({ height: 300 });

            self.isAllCheckCondition = ko.pureComputed({
                read: function() {
                    if (self.category() == 9) {
                        let l = self.listMulMonCheckSet().length;
                        if (self.listMulMonCheckSet().filter((x) => { return x.deleteAtr() }).length == l && l > 0) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        let l = self.listWorkRecordExtractingConditions().length;
                        if (self.listWorkRecordExtractingConditions().filter((x) => { return x.deleteAtr(); }).length == l && l > 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                },
                write: function(value) {
                    if (self.category() == 9) {
                        for (var i = 0; i < self.listMulMonCheckSet().length; i++) {
                            self.listMulMonCheckSet()[i].deleteAtr(value);
                        }
                    } else {
                        for (var i = 0; i < self.listWorkRecordExtractingConditions().length; i++) {
                            self.listWorkRecordExtractingConditions()[i].deleteAtr(value);
                        }
                    }

                },
                owner: self
            });
            self.currentRowSelected.subscribe((data) => {
                // MinhVV edit check category 5 or 9

                if (self.category() == 9) {
                    $("#check-condition-table_category9 tr").removeClass("ui-state-active");
                    $("#check-condition-table_category9 tr[data-id='" + data + "']").addClass("ui-state-active");
                } else {
                    $("#check-condition-table tr").removeClass("ui-state-active");
                    $("#check-condition-table tr[data-id='" + data + "']").addClass("ui-state-active");
                }
            });
            // check delete 9
            self.checkUseAtr = ko.pureComputed({
                read: function() {
                    if (self.listMulMonCheckSet().filter((x) => { return x.deleteAtr() }).length > 0) {
                        return true;
                    } else {
                        return false;
                    }
                },
                owner: self
            });
            
            self.checkUseAtr57 = ko.pureComputed({
                read: function() {
                    if (self.listWorkRecordExtractingConditions().filter((x) => { return x.deleteAtr() }).length > 0) {
                        return true;
                    } else {
                        return false;
                    }
                },
                owner: self
            });
            
        }
        /**
         *Create new WorkRecordExtractingCondition 
         */
        private createNewCheckCondition_click() {
            let self = this;
            // MinhVV add

            if (self.category() == 9) {
                if (self.listMulMonCheckSet == null || self.listMulMonCheckSet == undefined) {
                    self.listMulMonCheckSet = ko.observableArray([]);
                }
                if (self.listMulMonCheckSet().length == 50) {
                    dialog.alertError({ messageId: "Msg_833" });
                    return;
                }
                let mulMonCheckCondSet = shareutils.getDefaultMulMonCheckCondSet(0);
                mulMonCheckCondSet.rowId(self.listMulMonCheckSet().length + 1);
                self.listMulMonCheckSet.push(mulMonCheckCondSet);
                self.currentRowSelected(self.listMulMonCheckSet().length);
                // MinhVV edit
                $("#check-condition-table_category9 tr")[self.listMulMonCheckSet().length - 1].scrollIntoView();

            } else {
                if (self.listWorkRecordExtractingConditions == null || self.listWorkRecordExtractingConditions == undefined) {
                    self.listWorkRecordExtractingConditions = ko.observableArray([]);
                }
                if (self.listWorkRecordExtractingConditions().length == 50) {
                    dialog.alertError({ messageId: "Msg_833" });
                    return;
                }
                let workRecordExtractingCondition = shareutils.getDefaultWorkRecordExtractingCondition(0);
                workRecordExtractingCondition.rowId(self.listWorkRecordExtractingConditions().length + 1);

                self.listWorkRecordExtractingConditions.push(workRecordExtractingCondition);
                self.currentRowSelected(self.listWorkRecordExtractingConditions().length);
                $("#check-condition-table tr")[self.listWorkRecordExtractingConditions().length - 1].scrollIntoView();
            }

        }

        /**
         * Open dialog Kal003 B for setting the Setting Check Condition.
         * @param rowId
         */
        private btnSettingCheckCondition_click(rowId) {
            let self = this;
            if (self.category() == 9) {
                if (rowId() < 1 || rowId() > self.listMulMonCheckSet().length) {
                    return;
                }

                let mulMonCheckCondSet = self.listMulMonCheckSet()[rowId() - 1];
                if (mulMonCheckCondSet) {
                    if (_.isEmpty(mulMonCheckCondSet.erAlAtdItem())) {
                        mulMonCheckCondSet.erAlAtdItem(shareutils.getDefaultCondition(0));
                    }
                    self.showDialogMulMonKal003B(mulMonCheckCondSet, rowId());
                }
            } else {
                if (rowId() < 1 || rowId() > self.listWorkRecordExtractingConditions().length) {
                    return;
                }
                let workRecordExtractingCondition = self.listWorkRecordExtractingConditions()[rowId() - 1];
                if (workRecordExtractingCondition) {
                    if (_.isEmpty(workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon())) {
                        workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon([shareutils.getDefaultCondition(0), shareutils.getDefaultCondition(1), shareutils.getDefaultCondition(2)]);
                    }
                    self.showDialogKal003B(workRecordExtractingCondition, rowId());
                }
            }
        }

        /**
         * Open dialog Kal003B
         * @param errorAlamCondition
         * @param rowId
         */
        private showDialogKal003B(workRecordExtractingCondition: model.WorkRecordExtractingCondition, rowId: number) {
            let self = this;
            $(".nameAlarmDailyM").ntsError("clear");
            let sendData = ko.toJS(workRecordExtractingCondition);
            //sendData = shareutils.convertArrayOfWorkRecordExtractingConditionToJS(sendData, workRecordExtractingCondition);
            sendData = { data: shareutils.convertArrayOfWorkRecordExtractingConditionToJS(sendData, workRecordExtractingCondition), category: self.category() };
            windows.setShared('inputKal003b', sendData);
            windows.sub.modal('/view/kal/003/b/index.xhtml', { height: 600, width: 1020 }).onClosed(function(): any {
                // get data from share window    
                let data = windows.getShared('outputKal003b');
                if (data != null && data != undefined) {
                    if (rowId > 0 && rowId <= self.listWorkRecordExtractingConditions().length) {
                        self.listWorkRecordExtractingConditions()[rowId - 1] = shareutils.convertTransferDataToWorkRecordExtractingCondition(data);
                        self.listWorkRecordExtractingConditions.valueHasMutated();
                    }
                }
                block.clear();
            });
        }

        /**
         * Open dialog Kal003B
         * @param errorAlamCondition
         * @param rowId
         */
        private showDialogMulMonKal003B(mulMonCheckCondSet: model.MulMonCheckCondSet, rowId: number) {
            let self = this;
            let sendData = ko.toJS(mulMonCheckCondSet);
            $(".nameWKRecordID").ntsError("clear");
            sendData = { data: shareutils.convertArrayOfMulMonCheckCondSetToJS(sendData, mulMonCheckCondSet), category: self.category() };
            windows.setShared('inputKal003b', sendData);
            windows.sub.modal('/view/kal/003/b/index.xhtml', { height: 500, width: 1020 }).onClosed(function(): any {
                // get data from share window    
                let data = windows.getShared('outputKal003b');
                if (data != null && data != undefined) {
                    if (rowId > 0 && rowId <= self.listMulMonCheckSet().length) {
                        let condSet = shareutils.convertTransferDataToMulMonCheckCondSet(data);
                        self.listMulMonCheckSet()[rowId - 1] = condSet;
                        self.listMulMonCheckSet.valueHasMutated();
                    }
                }
//                else{
//                    if (rowId > 0 && rowId <= self.listMulMonCheckSet().length) {
//                        let mulMonCheckCondSet = shareutils.getDefaultMulMonCheckCondSet(0);
//                        self.listMulMonCheckSet()[rowId - 1] = mulMonCheckCondSet;
//                        self.listMulMonCheckSet.valueHasMutated();
//                    }
//                }
                block.clear();
            });
        }
        /**
             * Execute deleting the selected WorkRecordExtractingCondition on screen 
             */
        private deleteCheckCondition_click() {
            let self = this;
            block.invisible();

            if (self.category() == 9) {
                if (self.isAllCheckCondition()) {
                    self.listMulMonCheckSet.removeAll();
                    info({ messageId: "Msg_16" }).then(() => {
                        block.clear();
                        return;
                    });
                }
                if (self.currentRowSelected() < 1 || self.currentRowSelected() > self.listMulMonCheckSet().length || _.filter(self.listMulMonCheckSet(), function(o) { return o.deleteAtr(); }).length == 0) {
                    block.clear();
                    nts.uk.ui.errors.clearAll();
                    return;
                }
                self.listMulMonCheckSet.remove(function(item) {
                    nts.uk.ui.errors.clearAll();
                    return item.deleteAtr();
                })
                nts.uk.ui.errors.clearAll();
                for (var i = 0; i < self.listMulMonCheckSet().length; i++) {
                    self.listMulMonCheckSet()[i].rowId(i + 1);
                }
                if (self.currentRowSelected() >= self.listMulMonCheckSet().length) {
                    self.currentRowSelected(self.listMulMonCheckSet().length);
                }
                self.currentRowSelected.valueHasMutated();
                if (self.currentRowSelected() > 0) {
                    $("#check-condition-table_category9 tr")[self.currentRowSelected() - 1].scrollIntoView();
                }
                info({ messageId: "Msg_16" }).then(() => {
                    block.clear();
                });
            } else{
                if (self.isAllCheckCondition()) {
                    self.listWorkRecordExtractingConditions.removeAll();
                    info({ messageId: "Msg_16" }).then(() => {
                        block.clear();
                        return;
                    });
                }
                if (self.currentRowSelected() < 1 || self.currentRowSelected() > self.listWorkRecordExtractingConditions().length || _.filter(self.listWorkRecordExtractingConditions(), function(o) { return o.deleteAtr(); }).length == 0) {
                    block.clear();
                    nts.uk.ui.errors.clearAll();
                    return;
                }
                //self.listWorkRecordExtractingConditions.remove(function(item) { return item.rowId() == (self.currentRowSelected()); })
                self.listWorkRecordExtractingConditions.remove(function(item) { 
                    nts.uk.ui.errors.clearAll();
                    return item.deleteAtr(); 
                })
                nts.uk.ui.errors.clearAll();
                for (var i = 0; i < self.listWorkRecordExtractingConditions().length; i++) {
                    self.listWorkRecordExtractingConditions()[i].rowId(i + 1);
                }
                if (self.currentRowSelected() >= self.listWorkRecordExtractingConditions().length) {
                    self.currentRowSelected(self.listWorkRecordExtractingConditions().length);
                }
                self.currentRowSelected.valueHasMutated();
                if (self.currentRowSelected() > 0)
                    $("#check-condition-table tr")[self.currentRowSelected() - 1].scrollIntoView();
                info({ messageId: "Msg_16" }).then(() => {
                    block.clear();
                });
            }
        }
    }
}

$(function() {
    // MinhVV edit
    $("#check-condition-table").on("click", "tr", function() {
        var id = $(this).attr("data-id");
        nts.uk.ui._viewModel.content.tabCheckCondition.currentRowSelected(id);
    })
    $("#check-condition-table_category9").on("click", "tr", function() {
        var id = $(this).attr("data-id");
        nts.uk.ui._viewModel.content.tabCheckCondition.currentRowSelected(id);
    })
})


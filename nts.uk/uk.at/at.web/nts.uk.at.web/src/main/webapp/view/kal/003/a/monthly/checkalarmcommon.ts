module nts.uk.at.view.kal003.a.tab {
    import model = kal003.share.model;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import windows = nts.uk.ui.windows;
    import dialog = nts.uk.ui.dialog;
    import getText = nts.uk.resource.getText;
    import block = nts.uk.ui.block;
    import shareutils = nts.uk.at.view.kal003.share.kal003utils;
    import info = nts.uk.ui.dialog.info;
    export class CheckAlarmTab {
        //
        listExtraResultMonthly : KnockoutObservableArray<model.ExtraResultMonthly> = ko.observableArray([]);
        isAllCheckCondition: KnockoutObservable<boolean> = ko.observable(false);
        currentRowSelected: KnockoutObservable<number> = ko.observable(0);
        category: KnockoutObservable<number>;
        checkUseAtr57: KnockoutObservable<boolean> = ko.observable(false);
        
        userAtrCheck: KnockoutObservableArray<any>;
        constructor(category: number, listExtraResultMonthly?: Array<model.ExtraResultMonthly>) {
            let self = this;
            
            self.userAtrCheck = ko.observableArray([
                { code: '1', name: getText('KAL003_189'), width: 60 },
                { code: '0', name: getText('KAL003_190'), width: 60 }
            ]);
            if(listExtraResultMonthly){
                self.listExtraResultMonthly(listExtraResultMonthly);    
            }
            self.listExtraResultMonthly.subscribe(() => {
                 for (var i = 0; i < self.listExtraResultMonthly().length; i++) {
                    self.listExtraResultMonthly()[i].rowId(i + 1);
                }
            });
            //
            self.category = ko.observable(model.CATEGORY.MONTHLY);
            
//            $("#fixed-table2").ntsFixedTable({ height: 200 });
            
            self.isAllCheckCondition = ko.pureComputed({
                read: function () {
                    let l = self.listExtraResultMonthly().length;
                    if (self.listExtraResultMonthly().filter((x) => {return x.deleteAtr()}).length == l && l > 0) {
                        return true;
                    } else {
                        return false;
                    }
                },
                write: function (value) {
                    for (var i = 0; i < self.listExtraResultMonthly().length; i++) {
                        self.listExtraResultMonthly()[i].deleteAtr(value);
                    }
                },
                owner: self
            });
            self.currentRowSelected.subscribe((data) => {
                $("#fixed-table2 tr").removeClass("ui-state-active");
                $("#fixed-table2 tr[data-id='" + data + "']").addClass("ui-state-active");
            });
            //MinhVV add
            self.checkUseAtr57 = ko.pureComputed({
                read: function() {
                    if (self.listExtraResultMonthly().filter((x) => { return x.deleteAtr() }).length > 0) {
                        return true;
                    } else {
                        return false;
                    }
                },
                owner: self
            });
        }
        
        /**
         * Open dialog Kal003 B for setting the Setting Check Condition.
         * @param rowId
         */
        private createNewCheckCondition_click() {
            let self = this;
            if (self.listExtraResultMonthly == null || self.listExtraResultMonthly == undefined) {
                self.listExtraResultMonthly = ko.observableArray([]);
            }
            if (self.listExtraResultMonthly().length == 50) {
                dialog.alertError({ messageId: "Msg_833" });
                return;
            }
            let extraResultMonthly = shareutils.getDefaultExtraResultMonthly({ typeCheckItem : 0 });
            extraResultMonthly.rowId(self.listExtraResultMonthly().length + 1);
            
            // khi xoa roi tao moi No se phai = max + 1
            if(self.listExtraResultMonthly().length != 0 ){
                let max = self.listExtraResultMonthly()[(self.listExtraResultMonthly().length)-1].sortBy();
                extraResultMonthly.sortBy(max+1);    
            }else{
                extraResultMonthly.sortBy(1);
            }
            
            self.listExtraResultMonthly.push(extraResultMonthly);
            self.currentRowSelected(self.listExtraResultMonthly().length);
            $("#fixed-table2 tr")[self.listExtraResultMonthly().length - 1].scrollIntoView();
            
        }
        
         private btnSettingCheckCondition_click( rowId) {
            let self = this;
            if (rowId() < 1 || rowId() > self.listExtraResultMonthly().length) {
                return;
            }
            let extraResultMonthly = self.listExtraResultMonthly()[rowId() - 1];
            if (extraResultMonthly) {
                self.showDialogKal003B(extraResultMonthly, rowId());
            }
        }
        
        /**
         * Open dialog Kal003B
         * @param errorAlamCondition
         * @param rowId
         */
//        private showDialogKal003B(workRecordExtractingCondition: model.WorkRecordExtractingCondition, rowId: number) {
        private showDialogKal003B( extraResultMonthly :model.ExtraResultMonthly ,rowId: number) {
            let self = this;
//            let sendData = ko.toJS(extraResultMonthly);
//            sendData = { data: shareutils.convertArrayOfExtraResultMonthlyToJS(sendData, extraResultMonthly), category: self.category() };
             $(".nameAlarm").ntsError("clear");
            let sendData = { data: ko.toJS( extraResultMonthly), category: self.category() };

            windows.setShared('inputKal003b', sendData);
            windows.sub.modal('/view/kal/003/b/index.xhtml', { height: 600, width: 1020 }).onClosed(function(): any {
                // get data from share window    
                let data = windows.getShared('outputKal003b');
                if (data != null && data != undefined) {
                    if (rowId > 0 && rowId <= self.listExtraResultMonthly().length) {
                        self.listExtraResultMonthly()[rowId - 1] = model.ExtraResultMonthly.clone(data);
                        self.listExtraResultMonthly.valueHasMutated();
                    }
                }
                block.clear();
            });
        }
        
        
        
        /**
         * Execute deleting the selected WorkRecordExtractingCondition on screen 
         */
        private deleteCheckCondition_click() {
            let self = this;
            block.invisible();
            if(self.isAllCheckCondition()){
                self.listExtraResultMonthly.removeAll();
                info({ messageId: "Msg_16" }).then(() => {
                    block.clear();
                    return;
                });
            } 
            if (self.currentRowSelected() < 1 || self.currentRowSelected() > self.listExtraResultMonthly().length || _.filter(self.listExtraResultMonthly(), function(o) { return o.deleteAtr(); }).length==0 ) {
                block.clear();
                nts.uk.ui.errors.clearAll();
                return;
            }
            
            self.listExtraResultMonthly.remove(function(item) { 
                nts.uk.ui.errors.clearAll();
                return item.deleteAtr(); 
            })
            nts.uk.ui.errors.clearAll();
            for (var i = 0; i < self.listExtraResultMonthly().length; i++) {
                self.listExtraResultMonthly()[i].rowId(i + 1);
            }
            if (self.currentRowSelected() >= self.listExtraResultMonthly().length) {
                self.currentRowSelected(self.listExtraResultMonthly().length);
            }
            self.currentRowSelected.valueHasMutated();
            if (self.currentRowSelected() > 0) 
                $("#fixed-table2 tr")[self.currentRowSelected() - 1].scrollIntoView();
            info({ messageId: "Msg_16" }).then(() => {
                block.clear();
            });
        }  
    }
}

$(function() {
    $("#fixed-table2").on("click", "tr", function() {
        var id = $(this).attr("data-id");
        nts.uk.ui._viewModel.content.tabCheckAlarm.currentRowSelected(id);
    })
})
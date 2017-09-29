module nts.uk.com.view.cmm018.j {
    export module viewmodel {
        import servicebase = cmm018.shr.servicebase;
        import close = nts.uk.ui.windows.close;
        import block =  nts.uk.ui.block;
        import vmbase = cmm018.shr.vmbase;
        export class ScreenModel {
            isUpdate: KnockoutObservable<boolean>;
            size: number;
            newStartDate: KnockoutObservable<string>;
            item: KnockoutObservable<string> = ko.observable('');
            dataSource: vmbase.JData_Param;
            itemList:  KnockoutObservableArray<any>;
            selectedId: KnockoutObservable<number> = ko.observable(0);
            constructor() {
                var self = this; 
                self.itemList =  ko.observableArray([
                                    { code: 0, name: nts.uk.resource.getText("CMM018_54") },
                                    { code: 1, name: nts.uk.resource.getText("CMM018_55") }
                                ]);
                let lstItem = [];
                lstItem.push(new vmbase.UpdateHistoryDto('1','1'));
                lstItem.push(new vmbase.UpdateHistoryDto('0','2'));
                var data: vmbase.JData_Param = nts.uk.ui.windows.getShared('CMM018J_PARAM')|| 
                        {name: 'Hatake Kakashi',startDate: '2021-11-02', endDate: '9999-12-31', workplaceId: '123', employeeId: 'abc', check: 1, mode: 1, overlapFlag: true, lstUpdate: lstItem};
                self.dataSource = data;
                self.item(self.dataSource.name);
                self.newStartDate = ko.observable(self.dataSource.startDate);//startDate
                self.isUpdate = ko.observable(true);
                self.selectedId.subscribe(function(codeChange){
                    if(codeChange == 1){//delete
                        self.isUpdate(false);
                    }else{//update
                        self.isUpdate(true);
                    }
                });
            }
            
            /**
             * update/delete data when no error and close dialog
             */
            registration(): void {
                block.invisible();
                var self = this;
                //data
                let dataFix: vmbase.JData = new vmbase.JData(self.newStartDate(),'9999-12-31',self.dataSource.workplaceId,self.dataSource.employeeId,self.dataSource.check,self.selectedId(),self.dataSource.startDate,self.dataSource.lstUpdate);
                if(self.isUpdate()) {//TH: edit
                    //履歴編集を実行する(Update history)
                    servicebase.updateHistory(self.dataSource).done(function(){
                        //情報メッセージ（Msg_15）(Show message Msg_15)
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                            //close dialog
                            close();
                        });
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError(res.message).then(function(){
                            block.clear();
                        });       
                    });
                } else {//TH: delete
                    //削除する期間が最新なのかチェックする (Check history the last)
                    if(self.dataSource.endDate != '9999-12-31'){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_154" }).then(function(res){
                            block.clear();
                        });
                        return;
                     }
                    //TH: まとめて設定モード
                    if(self.dataSource.mode == 0){
                        //編集対象期間履歴が重なっているかチェックする(Check ls co bị chồng chéo k?)
                        if(self.dataSource.overlapFlag){
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_319" }).then(function(res){
                               block.clear();
                            });
                            return;
                        }
                    }
                    //削除前の確認処理(xác nhận trước khi xóa)
                    //確認メッセージ（Msg_18）を表示する(Show Confirm Message Msg_18)
                    nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function(){
                        //履歴の削除を実行する(Delete history) 
                        servicebase.updateHistory(self.dataSource).done(function(){
                            //情報メッセージ（Msg_16）(Show message Msg_16)
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                                close();
                            });
                        }).fail(function(res){
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function(){
                               block.clear();
                            });
                        });
                    }).ifNo(function(){
                        block.clear();        
                    });
                }
            }
            submitAndCloseDialog(): void {
                var self = this;
                let dataFix: vmbase.JData = new vmbase.JData(self.newStartDate(),'9999-12-31',self.dataSource.workplaceId,self.dataSource.employeeId,self.dataSource.check,self.selectedId(),self.dataSource.startDate,self.dataSource.lstUpdate);
                servicebase.updateHistory(self.dataSource).done(function(){
                    alert("done");
                }).fail(function(res){
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                });
            }
            /**
             * close dialog and do nothing
             */
            closeDialog(): void {
                $("#startDateInput").ntsError('clear');
                close();   
            }
        }
    }
}
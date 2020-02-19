module nts.uk.com.view.cmm018.m {
    export module viewmodel {
        export class ScreenModel {
            isCompany: KnockoutObservable<boolean> = ko.observable(true);
            isWorkplace: KnockoutObservable<boolean> = ko.observable(true);
            isPerson: KnockoutObservable<boolean> = ko.observable(true);            
            date: KnockoutObservable<Date> = ko.observable(moment(new Date()).toDate());
            sysAtr: KnockoutObservable<number> = ko.observable(0);
            lstAppName: Array<any>;
            constructor() {
                let self = this; 
                let param = nts.uk.ui.windows.getShared('CMM018_SysAtr');
                self.sysAtr(param.sysAtr || 0);
                self.lstAppName = param.lstName || [];
            }
            //閉じるボタン
            closeDialog(){
                nts.uk.ui.windows.close();
            }
            
            printExcel(){
                if (nts.uk.ui.errors.hasError()) { return; }
                let self = this;
                //会社別、職場別、個人別のチェック状態をチェックする(kiểm tra trạng thái check của 会社別、職場別、個人別)
                //１件もチェック入れていない場合(không check cái nào)
                if(!self.isCompany() && !self.isWorkplace() && !self.isPerson()){
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_199"});
                    return;    
                }
                let master = new service.MasterApproverRootQuery(self.date(), self.isCompany(), 
                        self.isWorkplace(), self.isPerson(), self.sysAtr(), self.lstAppName);
                nts.uk.ui.block.grayout();
                service.saveAsExcel(master).done(function(data: service.MasterApproverRootQuery){
                    nts.uk.ui.block.clear();
                }).fail(function(res: any){
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId});  
                    nts.uk.ui.block.clear();                      
                });
            }
        }
        
        
    }
}

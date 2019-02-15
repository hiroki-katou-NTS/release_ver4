module nts.uk.at.view.kaf002.c {
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import service = nts.uk.at.view.kaf002.shr.service;
    import kaf000 = nts.uk.at.view.kaf000;
    import kaf002 = nts.uk.at.view.kaf002;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase; 
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
            cm: kaf002.cm.viewmodel.ScreenModel;
            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
                super(listAppMetadata, currentApp);
                var self = this;
                self.appID.subscribe(value=>{
                    if(self.appType()==7){
                        self.startPage(value);       
                    }
                });
                self.startPage(self.appID());
            }
            
            startPage(appID: string): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                var dfdCommonSet = service.newScreenFind();
                var dfdAppStamp = service.findByAppID(appID);
                $.when(dfdCommonSet, dfdAppStamp).done((commonSetData, appStampData) => {
                    self.cm = new kaf002.cm.viewmodel.ScreenModel(appStampData.stampRequestMode,0);
                    self.cm.start(commonSetData, appStampData, self.editable(), appStampData.employeeID, appStampData.applicationDate);   
                    dfd.resolve(); 
                })
                .fail(function(res) { 
                     if (res.messageId == 'Msg_426') {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function () {
                            nts.uk.ui.block.clear();
                        });
                    }else {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function () {
                            nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");
                            nts.uk.ui.block.clear();
                        });
                    }
                    dfd.reject(res);  
                });
                return dfd.promise();
            }
            
            update(){
                var self = this;
                self.cm.update(self.approvalList);
            }
            
            getBoxReason(){
                var self = this;
                return cm.getBoxReason();
            }
        
            getAreaReason(){
                var self = this;
                return cm.getAreaReason(); 
            }
            
            resfreshReason(appReason: string){
                var self = this;
                return cm.resfreshReason(appReason);
            }
        }
    }
}
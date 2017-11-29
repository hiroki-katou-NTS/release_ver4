module nts.uk.com.view.cmm007.c {

    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import blockUI = nts.uk.ui.block;
    
    export module viewmodel {
        export class ScreenModel {
            mapModel : KnockoutObservable<Map<number, moduleDto>>;
            
            constructor(){
                let _self = this;
                
                _self.mapModel = new Map<number, moduleDto>();
            }
            
             /**
             * init default data when start page
             */
            public start_page(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var _self = this;
                
                _self.getDataByCId().done(() => {
                    dfd.resolve();
                });
                
                
                return dfd.promise();
            }
            
            public getDataByCId(): JQueryPromise<void> {
                let _self = this;
                let dfd = $.Deferred<void>();
                service.getTempAbsenceFrameByCId().done(function(data){
                    
                    for (let i of data) {
                        let a = new viewmodel.moduleDto(i.companyId, i.tempAbsenceFrNo, i.useClassification, i.tempAbsenceFrName);
                        _self.mapModel.set(i.tempAbsenceFrNo , a);
                    }
                    
                    for (let i=1; i<10; i++) {
                        if (_self.mapModel.get(i) == null || typeof _self.mapModel.get(i) == undefined) {
                            _self.mapModel.set(i, new viewmodel.moduleDto());    
                        }    
                    }
                    dfd.resolve();
                    
                }).fail(function(data) {
                    console.log(data);
                })
                return dfd.promise();
            }
            
            public checkStatusEnable(value): boolean {
                let _self = this;
                return _self.mapModel.get(value).useClassification() == 1 ? true : false;
            }
            
            public myFunction(value): void {
                let _self = this;
                _self.mapModel.get(value).useClassification() == 1 ? _self.mapModel.get(value).useClassification(0) : _self.mapModel.get(value).useClassification(1);
            }
            
            /**
             * Register/update data
             */
            public regsiter(): void {
                let _self = this;
                
                let lstDto: Array<viewmodel.moduleDto> = new Array();
                for (let i=1; i<=_self.mapModel.size; i++) {
                    lstDto.push(_self.mapModel.get(i));
                }
                
                service.updateTempAbsenceFrame(lstDto).done(function(data){
                    nts.uk.ui.dialog.alert({ messageId: "Msg_15" }).then(() => {
                    });
                }).fail(function(data) {
//                    console.log(data);
                })
            }
       }      
        
        export class moduleDto {
            companyId: KnockoutObservable<string>;
            tempAbsenceFrNo: KnockoutObservable<number>; 
            useClassification: KnockoutObservable<number>; 
            tempAbsenceFrName: KnockoutObservable<string>;
            
            constructor(companyId: string, tempAbsenceFrNo: number, useClassification: number, tempAbsenceFrName: string) {
                let _self = this;
                _self.companyId = ko.observable(companyId);
                _self.tempAbsenceFrNo = ko.observable(tempAbsenceFrNo);
                _self.useClassification = ko.observable(useClassification);
                _self.tempAbsenceFrName = ko.observable(tempAbsenceFrName);
            }
        }
    }
}
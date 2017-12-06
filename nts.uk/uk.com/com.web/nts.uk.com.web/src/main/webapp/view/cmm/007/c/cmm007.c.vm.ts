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
            
            /*
            *   find all
            */
            public getDataByCId(): JQueryPromise<void> {
                let _self = this;
                let dfd = $.Deferred<void>();
                service.getTempAbsenceFrameByCId().done(function(data){
                    for (let i of data) {
                        if (typeof _self.mapModel.get(i.tempAbsenceFrNo) === "undefined") {
                            let temp = new viewmodel.moduleDto(i.companyId, i.tempAbsenceFrNo, i.useClassification, i.tempAbsenceFrName);
                            _self.mapModel.set(i.tempAbsenceFrNo , temp);
                        } else {
                            _self.mapModel.get(i.tempAbsenceFrNo).tempAbsenceFrName(i.tempAbsenceFrName);
                        }
                    }
                    
                    dfd.resolve();
                    
                }).fail(function(data) {
//                    console.log(data);
                })
                return dfd.promise();
            }
            
            /*
            *   status check/uncheck checkbox
            */
            public checkStatusEnable(value): boolean {
                let _self = this;
                return _self.mapModel.get(value).useClassification() == 1 ? true : false;
            }
            
            /*
            *   catch event click check box
            */
            public clickCheckbox(value): void {
                let _self = this;
                _self.mapModel.get(value).useClassification() == 1 ? _self.mapModel.get(value).useClassification(0) : _self.mapModel.get(value).useClassification(1);
                if (_self.mapModel.get(value).useClassification() == 1) {
                    $('#tempAbsenceNo' + value).ntsEditor("validate");    
                } else {
                    $('#tempAbsenceNo' + value).ntsEditor("clear");
                }   
            }
            
            /**
             * update data
             */
            public update(): void {
                let _self = this;
                
                if (_self.hasError()) {
                    return true;    
                }
                
                let lstDto: Array<viewmodel.moduleDto> = new Array();
                for (let i=1; i<=_self.mapModel.size; i++) {
                    lstDto.push(_self.mapModel.get(i));
                }
                
                service.updateTempAbsenceFrame(lstDto).done(function(data){
                    _self.getDataByCId().done(() => {
                    });
                    nts.uk.ui.dialog.alert({ messageId: "Msg_15" }).then(() => {
                        $('#tempAbsenceNo7').focus();
                    });
                }).fail(function(data) {
//                    console.log(data);
                })
            }
            
             /**
             * Check Errors all input.
             */
            private hasError(): boolean {
                let _self = this;
                _self.clearErrors();
                for (var i=7; i<=10; i++) {
                    if (_self.mapModel.get(i).useClassification() == 1) {
                        $('#tempAbsenceNo' + i).ntsEditor("validate");    
                    }    
                }
                if ($('.nts-input').ntsError('hasError')) {
                    return true;
                }
                return false;
            }

            /**
             * Clear Errors
             */
            private clearErrors(): void {
                let _self = this;
                // Clear errors
                for (var i=7; i<=10; i++) {
                    if (_self.mapModel.get(i).useClassification() == 1) {
                        $('#tempAbsenceNo' + i).ntsEditor("clear");    
                    }    
                }
                
                // Clear error inputs
                $('.nts-input').ntsError('clear');
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
module nts.uk.com.view.cmm007.c {

    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import blockUI = nts.uk.ui.block;
    
    export module viewmodel {
        export class ScreenModel {
            mapModel : KnockoutObservable<Map<number, moduleDto>>;
            checkStatusSetOptions: KnockoutObservableArray<any>;
            
            constructor(){
                let _self = this;
                
                _self.mapModel = new Map<number, moduleDto>();
                _self.checkStatusSetOptions = ko.observableArray([
                    { code: true, name: nts.uk.resource.getText("CMM007_101") },
                    { code: false, name: nts.uk.resource.getText("CMM007_102") }
                ]);
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
                            //set value 休職 == 使用する
                            if (i.tempAbsenceFrNo == 1) {
                                _self.mapModel.get(i.tempAbsenceFrNo).useClassification(1);
                            } else {
                                _self.mapModel.get(i.tempAbsenceFrNo).tempAbsenceFrName(i.tempAbsenceFrName);
                                _self.mapModel.get(i.tempAbsenceFrNo).useClassification(i.useClassification);
                            }
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
            public checkStatusEnable(value): KnockoutObservable<boolean> {
                let _self = this;
                return ko.observable(_self.mapModel.get(value).useClassification() == USE_CLASSIFICATION.USE ? true : false);
            }
            
            /*
            *   catch event click check box
            */
            public clickCheckbox(value): void {
                let _self = this;
                _self.mapModel.get(value).useClassification() == USE_CLASSIFICATION.USE ? _self.mapModel.get(value).useClassification(0) : _self.mapModel.get(value).useClassification(1);
                if (_self.mapModel.get(value).useClassification() == USE_CLASSIFICATION.USE) {
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
                nts.uk.ui.block.grayout();
                service.updateTempAbsenceFrame(lstDto).done(function(data){
                    _self.getDataByCId().done(() => {
                    });
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                        $('#tempAbsenceNo7').focus();
                    });
                }).fail(function(data) {
//                    console.log(data);
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }
            
             /**
             * Check Errors all input.
             */
            private hasError(): boolean {
                let _self = this;
                _self.clearErrors();
                for (var i=7; i<=10; i++) {
                    if (_self.mapModel.get(i).useClassification() == USE_CLASSIFICATION.USE) {
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
                    if (_self.mapModel.get(i).useClassification() == USE_CLASSIFICATION.USE) {
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
    
    module USE_CLASSIFICATION {
        export const NOT_USE: number = 0;                        
        export const USE: number = 1;                           
    }
}
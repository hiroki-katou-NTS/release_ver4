module nts.uk.at.view.kdl009.a {
    export module viewmodel {
        export class ScreenModel {
            //Grid data
            listComponentOption: any;
            selectedCode: KnockoutObservable<string> = ko.observable('');
            multiSelectedCode: KnockoutObservableArray<string>;
            isShowAlreadySet: KnockoutObservable<boolean>;
            alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
            isDialog: KnockoutObservable<boolean>;
            isShowNoSelectRow: KnockoutObservable<boolean>;
            isMultiSelect: KnockoutObservable<boolean>;
            isShowWorkPlaceName: KnockoutObservable<boolean>;
            isShowSelectAllButton: KnockoutObservable<boolean>;
            employeeList: KnockoutObservableArray<UnitModel>;
            
            legendOptions: any;
            kdl009Data: KnockoutObservable<any>;
            employeeInfo: KnockoutObservable<string>;
            
            dataItems: KnockoutObservableArray<any>;
            
            value01: KnockoutObservable<string> = ko.observable("");
            value02: KnockoutObservable<string> = ko.observable("");
            hint02: KnockoutObservable<string> = ko.observable("");
            value03: KnockoutObservable<string> = ko.observable("");
            hint03: KnockoutObservable<string> = ko.observable("");
            value04: KnockoutObservable<string> = ko.observable("");
            hint04: KnockoutObservable<string> = ko.observable("");
            
            constructor() {
                var self = this;
                
                self.kdl009Data = nts.uk.ui.windows.getShared("KDL009_DATA");

                self.employeeInfo = ko.observable("");
                
                self.dataItems = ko.observableArray([]);
                
                this.legendOptions = {
                    items: [
                        { labelText: nts.uk.resource.getText("KDL009_18") + " : " + nts.uk.resource.getText("KDL009_19") }
                    ]
                };
                
                service.getEmployee(self.kdl009Data).done(function(data: any) {
                    if(data.employeeBasicInfo.length > 1) {
                        self.selectedCode.subscribe(function(value) {
                            let itemSelected = _.find(data.employeeBasicInfo, ['employeeCode', value]);
                            self.employeeInfo(nts.uk.resource.getText("KDL009_25", [value, itemSelected.businessName]));
                            
                            service.getAcquisitionNumberRestDays(itemSelected.employeeId, data.baseDate).done(function(data) {
                                self.bindTimeData(data);
                                self.bindSummaryData(data);
                            }).fail(function(res) {
                                nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                            });
                        });  
                            
                        self.bindEmpList(data.employeeBasicInfo);
                        
                        $("#date-fixed-table").ntsFixedTable({ height: 341 });
                    } else if(data.employeeBasicInfo.length == 1) {
                        self.employeeInfo(nts.uk.resource.getText("KDL009_25", [data.employeeBasicInfo[0].employeeCode, data.employeeBasicInfo[0].businessName]));
                        
                        service.getAcquisitionNumberRestDays(data.employeeBasicInfo[0].employeeId, data.baseDate).done(function(data) {
                            self.bindTimeData(data);
                            self.bindSummaryData(data);
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                        });
                        
                        $("#date-fixed-table").ntsFixedTable({ height: 341 });
                    } else {
                        self.employeeInfo(nts.uk.resource.getText("KDL009_25", ["", ""]));
                        
                        $("#date-fixed-table").ntsFixedTable({ height: 341 });
                        
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_918" });
                    }
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                });

            }
            
            startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                
                dfd.resolve();
    
                return dfd.promise();
            }
            
            bindEmpList(data: any) {
                var self = this;
                
                self.baseDate = ko.observable(new Date());
                self.selectedCode(data[0].employeeCode);
                self.multiSelectedCode = ko.observableArray([]);
                self.isShowAlreadySet = ko.observable(false);
                self.alreadySettingList = ko.observableArray([
                    {code: '1', isAlreadySetting: true},
                    {code: '2', isAlreadySetting: true}
                ]);
                self.isDialog = ko.observable(false);
                self.isShowNoSelectRow = ko.observable(false);
                self.isMultiSelect = ko.observable(false);
                self.isShowWorkPlaceName = ko.observable(false);
                self.isShowSelectAllButton = ko.observable(false);
                this.employeeList = ko.observableArray<UnitModel>(_.map(data,x=>{return {code:x.employeeCode ,name:x.businessName};}));
                self.listComponentOption = {
                    isShowAlreadySet: self.isShowAlreadySet(),
                    isMultiSelect: self.isMultiSelect(),
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    selectedCode: self.selectedCode,
                    isDialog: self.isDialog(),
                    isShowNoSelectRow: self.isShowNoSelectRow(),
                    alreadySettingList: self.alreadySettingList,
                    isShowWorkPlaceName: self.isShowWorkPlaceName(),
                    isShowSelectAllButton: self.isShowSelectAllButton(),
                    maxRows: 12
                };
                
                $('#component-items-list').ntsListComponent(self.listComponentOption);
            }
            
            bindTimeData(data: any) {
                var self = this;
                var issueDate = "";
                var holidayDateTop = "";
                var holidayDateBot = "";
                var expirationDate = "";
                var occurrenceDays1 = "";
                var occurrenceDays2Top = "";
                var occurrenceDays2Bot = "";
                self.dataItems.removeAll();
                                
                if(data.recAbsHistoryOutput.length > 0) {
                    _.each(data.recAbsHistoryOutput, function (item) {
                        var isHalfDay = false;
                        
                        if(item.recHisData != null) {
                            if(item.recHisData.recDate.unknownDate) {
                                if(item.recHisData.dataAtr == 3) {
                                    issueDate = nts.uk.resource.getText("KDL009_13", [nts.uk.time.applyFormat("Short_YMDW", [item.recHisData.recDate.dayoffDate])]);
                                } else {
                                    issueDate = nts.uk.time.applyFormat("Short_YMDW", [item.recHisData.recDate.dayoffDate]);
                                }
                            } else {
                                issueDate = nts.uk.resource.getText("KDL009_11");
                            }
                            
                            if(item.recHisData.occurrenceDays == 0.5) {
                                occurrenceDays1 = nts.uk.resource.getText("KDL009_14", [item.recHisData.occurrenceDays]);
                            }
                            
                            if(item.recHisData.expirationDate != null) {
                                expirationDate = nts.uk.time.applyFormat("Short_YMDW", [item.recHisData.expirationDate]);
                            } else {
                                expirationDate = "";
                            }
                        }
                        
                        if(item.absHisData != null) {
                            if(item.absHisData.absDate.unknownDate) {
                                holidayDateTop = nts.uk.resource.getText("KDL009_11");
                            } else {
                                if(item.absHisData.createAtr == 3) {
                                    holidayDateTop = nts.uk.resource.getText("KDL009_13", [nts.uk.time.applyFormat("Short_YMDW", [item.absHisData.absDate.dayoffDate])]);
                                } else {
                                    holidayDateTop = nts.uk.time.applyFormat("Short_YMDW", [item.absHisData.absDate.dayoffDate]);
                                }
                            }
                            
                            if(item.absHisData.requeiredDays == 0.5) {
                                occurrenceDays2Top = nts.uk.resource.getText("KDL009_14", [item.absHisData.requeiredDays]);
                            }
                            
                            if(holidayDateTop === nts.uk.resource.getText("KDL009_11")) {
                                holidayDateTop = "";
                                occurrenceDays2Top = "";
                            }
                        }
                        
                        if(item.recHisData.dataAtr == 1) {
                            if(holidayDateTop === "" && occurrenceDays2Top === "") {
                                isHalfDay = false;
                            } else {
                                isHalfDay = true;
                            }                            
                        }
                        
                        var temp = new DataItems(issueDate, holidayDateTop, holidayDateBot, expirationDate, occurrenceDays1, occurrenceDays2Top, occurrenceDays2Bot, isHalfDay);
                            
                        self.dataItems.push(temp);
                    });                    
                }
            }
            
            bindSummaryData(data: any) {
                var self = this;
                
                if(data.absRemainInfor != null) {
                    self.value01(nts.uk.resource.getText("KDL009_14", [data.absRemainInfor.carryForwardDays]));
                    self.value02(nts.uk.resource.getText("KDL009_14", [data.absRemainInfor.recordOccurrenceDays]));
                    self.hint02(nts.uk.resource.getText("KDL009_15", [data.absRemainInfor.scheOccurrenceDays]));
                    self.value03(nts.uk.resource.getText("KDL009_14", [data.absRemainInfor.recordUseDays]));
                    self.hint03(nts.uk.resource.getText("KDL009_15", [data.absRemainInfor.scheUseDays]));
                    self.value04(nts.uk.resource.getText("KDL009_14", [data.absRemainInfor.recordOccurrenceDays - data.absRemainInfor.recordUseDays]));
                    self.hint04(nts.uk.resource.getText("KDL009_15", [data.absRemainInfor.scheOccurrenceDays - data.absRemainInfor.scheUseDays]));
                } else {
                    self.value01(nts.uk.resource.getText("KDL009_14", ["0"]));
                    self.value02(nts.uk.resource.getText("KDL009_14", ["0"]));
                    self.hint02(nts.uk.resource.getText("KDL009_15", ["0"]));
                    self.value03(nts.uk.resource.getText("KDL009_14", ["0"]));
                    self.hint03(nts.uk.resource.getText("KDL009_15", ["0"]));
                    self.value04(nts.uk.resource.getText("KDL009_14", ["0"]));
                    self.hint04(nts.uk.resource.getText("KDL009_15", ["0"]));
                }
            }
            
            cancel() {
                nts.uk.ui.windows.close();
            }
        }
        
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        export interface UnitModel {
            code: string;
            name?: string;
            workplaceName?: string;
            isAlreadySetting?: boolean;
        }
        
        export class SelectType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }
        
        export interface UnitAlreadySettingModel {
            code: string;
            isAlreadySetting: boolean;
        }
        
        class DataItems {
            issueDate: string;
            holidayDateTop: string;
            holidayDateBot: string;
            expirationDate: string;
            occurrenceDays1: string;
            occurrenceDays2Top: string;
            occurrenceDays2Bot: string;
            isHalfDay: boolean;
    
            constructor(issueDate: string, holidayDateTop: string, holidayDateBot: string, expirationDate: string, occurrenceDays1: string, 
                    occurrenceDays2Top: string, occurrenceDays2Bot: string, isHalfDay: boolean) {
                this.issueDate = issueDate;
                this.holidayDateTop = holidayDateTop;
                this.holidayDateBot = holidayDateBot;
                this.expirationDate = expirationDate;
                this.occurrenceDays1 = occurrenceDays1;
                this.occurrenceDays2Top = occurrenceDays2Top;
                this.occurrenceDays2Bot = occurrenceDays2Bot;
                this.isHalfDay = isHalfDay;
            }
        }
    }
}
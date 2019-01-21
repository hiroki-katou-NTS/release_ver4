module nts.uk.at.view.kwr001.c {

    import service = nts.uk.at.view.kwr001.c.service;
    import blockUI = nts.uk.ui.block;

    export module viewmodel {

        const DEFAULT_DATA_FIRST = 0;

        export class ScreenModel {
            data: KnockoutObservable<number>;

            // list
            items: KnockoutObservableArray<ItemModel>;
            outputItemList: KnockoutObservableArray<ItemModel>;
            currentCodeList: KnockoutObservable<any>;
            columns: KnockoutObservableArray<any>;

            C3_2_value: KnockoutObservable<string>;
            C3_3_value: KnockoutObservable<string>;

            // switch button
            roundingRules: KnockoutObservableArray<any>;
            selectedRuleCode: any;

            currentCodeListSwap: KnockoutObservableArray<ItemModel>;

            checkedRemarksInput: KnockoutObservable<boolean>;
            checkedMasterUnregistered: KnockoutObservable<boolean>;
            checkedEngraving: KnockoutObservable<boolean>;
            checkedImprintingOrderNotCorrect: KnockoutObservable<boolean>;
            checkedLeavingEarly: KnockoutObservable<boolean>;
            checkedHolidayStamp: KnockoutObservable<boolean>;
            checkedDoubleEngraved: KnockoutObservable<boolean>;
            checkedAcknowledgment: KnockoutObservable<boolean>;
            checkedManualInput: KnockoutObservable<boolean>;
            checkedNotCalculated: KnockoutObservable<boolean>;
            checkedExceedByApplication: KnockoutObservable<boolean>;

            // start: variable global store data from service 
            allMainDom: KnockoutObservable<any>;
            outputItemPossibleLst: KnockoutObservableArray<ItemModel>;
            // end: variable global store data from service

            // store map to convert id and code attendance item
            mapIdCodeAtd: any;
            mapCodeIdAtd: any;

            enableBtnDel: KnockoutObservable<boolean>;
            enableCodeC3_2: KnockoutObservable<boolean>;
            storeCurrentCodeBeforeCopy: KnockoutObservable<string>;

            remarkInputContents: KnockoutObservableArray<ItemModel>;
            currentRemarkInputContent: KnockoutObservable<string>;
            isEnableRemarkInputContents: KnockoutObservable<boolean>;

            constructor() {
                var self = this;
                self.allMainDom = ko.observable();
                self.outputItemPossibleLst = ko.observableArray([]);

                self.items = ko.observableArray([]);
                self.outputItemList = ko.observableArray([]);
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText("KWR001_52"), prop: 'code', width: 70 },
                    { headerText: nts.uk.resource.getText("KWR001_53"), prop: 'name', width: 180, formatter: _.escape }
                ]);
                self.currentCodeList = ko.observable();
                self.C3_2_value = ko.observable("");
                self.C3_3_value = ko.observable("");

                self.roundingRules = ko.observableArray([]);
                self.selectedRuleCode = ko.observable(0);
                self.currentCodeListSwap = ko.observableArray([]);

                self.currentCodeListSwap.subscribe(function(value) {
                })

                self.items.subscribe(function(value) {
                })

                self.enableBtnDel = ko.observable(false);
                self.enableCodeC3_2 = ko.observable(false);

                self.currentCodeList.subscribe(function(value) {
                    let codeChoose = _.find(self.allMainDom(), function(o: any) {
                        return value == o.itemCode;
                    });
                    if (!_.isUndefined(codeChoose) && !_.isNull(codeChoose)) {
                        blockUI.grayout();
                        service.findByCode(self.currentCodeList()).done((outputItemDailyWorkSchedule) => {
                            self.C3_2_value(outputItemDailyWorkSchedule.itemCode);
                            self.C3_3_value(outputItemDailyWorkSchedule.itemName);
                            self.getOutputItemDailyWorkSchedule(outputItemDailyWorkSchedule);
                            self.selectedRuleCode(outputItemDailyWorkSchedule.workTypeNameDisplay);
                            self.enableBtnDel(true);
                            self.enableCodeC3_2(false);
                            self.currentRemarkInputContent(self.convertDBRemarkInputToValue(outputItemDailyWorkSchedule.remarkInputNo));
                            blockUI.clear();
                        })
                    } else {
                        self.C3_3_value('');
                        self.C3_2_value('');
                        self.getOutputItemDailyWorkSchedule([]);
                        self.enableBtnDel(false);
                        self.enableCodeC3_2(true);
                    }
                    _.delay(() => {
                        nts.uk.ui.errors.clearAll();
                    }, 400);
                })

                self.checkedRemarksInput = ko.observable(false);
                self.checkedMasterUnregistered = ko.observable(false);
                self.checkedEngraving = ko.observable(false);
                self.checkedImprintingOrderNotCorrect = ko.observable(false);
                self.checkedLeavingEarly = ko.observable(false);
                self.checkedHolidayStamp = ko.observable(false);
                self.checkedDoubleEngraved = ko.observable(false);
                self.checkedAcknowledgment = ko.observable(false);
                self.checkedManualInput = ko.observable(false);
                self.checkedNotCalculated = ko.observable(false);
                self.checkedExceedByApplication = ko.observable(false);

                self.storeCurrentCodeBeforeCopy = ko.observable('');

                self.remarkInputContents = ko.observableArray([]);

                self.currentRemarkInputContent = ko.observable('1');

                self.currentRemarkInputContent.subscribe(function(value) {
                })

                self.checkedRemarksInput.subscribe(function(isChecked) {
                    if (isChecked) {
                        self.isEnableRemarkInputContents(true);
                    } else {
                        self.isEnableRemarkInputContents(false);
                    }
                })

                self.isEnableRemarkInputContents = ko.observable(false);
                self.mapIdCodeAtd = {};
                self.mapCodeIdAtd = {};
            }

            /*
             * set data to C7_2, C7_8 
            */
            private getOutputItemDailyWorkSchedule(data: any): void {
                let self = this;

                // variable temporary
                let temp2: ItemModel[] = [];
                let temp1: ItemModel[] = [];
                self.items.removeAll();
                self.currentCodeListSwap.removeAll();
                _.forEach(data.lstDisplayedAttendance, function(value, index) {
                    if (value.attendanceName) {
                        temp1.push({ code: self.mapIdCodeAtd[value.attendanceDisplay], name: value.attendanceName, id: value.attendanceDisplay });
                    }
                })
                _.forEach(self.outputItemPossibleLst(), function(value) {
                    temp2.push(value);
                })
                // refresh data for C7_2
                self.items(temp2);
                // refresh data for C7_8
                self.currentCodeListSwap(temp1);

                if (!_.isEmpty(data)) {
                    self.checkedRemarksInput(self.convertNumToBool(data.lstRemarkContent[0].usedClassification));
                    self.checkedMasterUnregistered(self.convertNumToBool(data.lstRemarkContent[1].usedClassification));
                    self.checkedEngraving(self.convertNumToBool(data.lstRemarkContent[2].usedClassification));
                    self.checkedImprintingOrderNotCorrect(self.convertNumToBool(data.lstRemarkContent[3].usedClassification));
                    self.checkedLeavingEarly(self.convertNumToBool(data.lstRemarkContent[4].usedClassification));
                    self.checkedHolidayStamp(self.convertNumToBool(data.lstRemarkContent[5].usedClassification));
                    self.checkedDoubleEngraved(self.convertNumToBool(data.lstRemarkContent[6].usedClassification));
                    self.checkedAcknowledgment(self.convertNumToBool(data.lstRemarkContent[7].usedClassification));
                    self.checkedManualInput(self.convertNumToBool(data.lstRemarkContent[8].usedClassification));
                    self.checkedNotCalculated(self.convertNumToBool(data.lstRemarkContent[9].usedClassification));
                    self.checkedExceedByApplication(self.convertNumToBool(data.lstRemarkContent[10].usedClassification));
                    self.selectedRuleCode(data.workTypeNameDisplay);
                } else {
                    self.setRemarksContentDefault();
                }

            }

            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                let self = this;

                $.when(self.getDataService(), self.getEnumName(), self.getEnumRemarkContentChoice(), self.getEnumRemarkInputContent()).done(function() {
                    if (_.isUndefined(nts.uk.ui.windows.getShared('KWR001_C'))) {
                        self.currentCodeList(null);
                    } else {
                        self.currentCodeList(nts.uk.ui.windows.getShared('KWR001_C'));
                    }

                    dfd.resolve();
                })
                return dfd.promise();
            }

            /*
             *  get data from server
            */
            private getDataService(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                service.getDataStartPage().done(function(data: any) {
                    // variable global store data from service 
                    self.allMainDom(data.outputItemDailyWorkSchedule);

                    // variable temporary 
                    let temp: any[] = [];
                    _.forEach(data.dailyAttendanceItem, function(value) {
                        temp.push(value);
                    })
                    self.outputItemPossibleLst(temp);

                    let arrCodeName: ItemModel[] = [];
                    _.forEach(data.outputItemDailyWorkSchedule, function(value, index) {
                        arrCodeName.push({ code: value.itemCode + "", name: value.itemName, id: "" });
                    });
                    self.outputItemList(arrCodeName);

                    _.forEach(data.dailyAttendanceItem, (value) => {
                        self.mapCodeIdAtd[value.code] = value.id;
                        self.mapIdCodeAtd[value.id] = value.code;
                    })

                    self.items(data.dailyAttendanceItem);

                    dfd.resolve();
                })

                return dfd.promise();
            }

            /*
             * get enum name
            */
            private getEnumName(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                service.getEnumName().done(function(data: any) {
                    let arr: any[] = [];
                    arr.push({ code: '0', name: data[0].localizedName });
                    arr.push({ code: '1', name: data[1].localizedName });
                    self.roundingRules(arr);
                    dfd.resolve();
                })
                return dfd.promise();
            }

            /*
             * get enum RemarksContentChoice
            */
            private getEnumRemarkContentChoice(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                service.getEnumRemarkContentChoice().done(function(data: any) {
                    dfd.resolve();
                })
                return dfd.promise();
            }

            /*
             * get enum EnumRemarkInputContent
            */
            private getEnumRemarkInputContent(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                service.getEnumRemarkInputContent().done(function(data: any) {
                    let arr: ItemModel[] = [];
                    arr.push(new ItemModel('1', nts.uk.resource.getText("KWR001_118"), ''));
                    arr.push(new ItemModel('2', nts.uk.resource.getText("KWR001_119"), ''));
                    arr.push(new ItemModel('3', nts.uk.resource.getText("KWR001_120"), ''));
                    arr.push(new ItemModel('4', nts.uk.resource.getText("KWR001_121"), ''));
                    arr.push(new ItemModel('5', nts.uk.resource.getText("KWR001_122"), ''));
                    self.remarkInputContents(arr);
                    dfd.resolve();
                })
                return dfd.promise();
            }

            /*
             *  open screen D
            */
            openScreenD() {
                var self = this;
                let dataScrD: any;
                //                nts.uk.ui.windows.setShared('KWR001_D', self.outputItemPossibleLst(), true);
                if (!_.isEmpty(self.currentCodeList())) {
                    self.storeCurrentCodeBeforeCopy(self.currentCodeList());
                }
                nts.uk.ui.windows.sub.modal('/view/kwr/001/d/index.xhtml').onClosed(function(): any {
                    nts.uk.ui.errors.clearAll();
                    dataScrD = nts.uk.ui.windows.getShared('KWR001_D');
                    if (!_.isEmpty(dataScrD)) {
                        if (!_.isEmpty(dataScrD.error)) {
                            nts.uk.ui.dialog.alertError(dataScrD.error);
                        } else {
                            self.currentCodeList('');
                            if (!_.isUndefined(dataScrD.lstAtdChoose) && !_.isEmpty(dataScrD.lstAtdChoose)) {
                                $('#C3_3').focus();
                            } else {
                                $('#C3_2').focus();
                            }

                            let arrTemp: any[] = [];
                            _.forEach(self.outputItemPossibleLst(), function(value) {
                                arrTemp.push(value);
                            })
                            _.forEach(dataScrD.lstAtdChoose.dataInforReturnDtos, (value) => {
                                value.code = self.mapIdCodeAtd[value.id];
                            })
                            self.currentCodeListSwap(dataScrD.lstAtdChoose.dataInforReturnDtos);
                            self.items(arrTemp);
                            self.C3_2_value(dataScrD.codeCopy);
                            self.C3_3_value(dataScrD.nameCopy);
                            if (_.size(dataScrD.lstAtdChoose.msgErr)) {
                                nts.uk.ui.dialog.error({ messageId: "Msg_1476" }).then(function() {
                                    self.saveData(dataScrD);
                                });
                            }else {

                                self.saveData(dataScrD);
                            }

                        }
                    } else {
                        self.currentCodeList(self.storeCurrentCodeBeforeCopy());
                    }
                });
            }

            /*
             *  save data to server
            */
            private saveData(dataScrD: any): JQueryPromise<any> {
                let self = this;
                $('.save-error').ntsError('check');
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }
                blockUI.grayout();
                let dfd = $.Deferred();
                let command: any = {};
                command.itemCode = self.C3_2_value();
                command.itemName = self.C3_3_value();
                command.lstDisplayedAttendance = [];
                _.forEach(self.currentCodeListSwap(), function(value, index) {
                    command.lstDisplayedAttendance.push({ sortBy: index, itemToDisplay: self.mapCodeIdAtd[value.code] });
                });
                command.lstRemarkContent = [];
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedRemarksInput()), printItem: 0 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedMasterUnregistered()), printItem: 1 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedEngraving()), printItem: 2 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedImprintingOrderNotCorrect()), printItem: 3 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedLeavingEarly()), printItem: 4 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedHolidayStamp()), printItem: 5 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedDoubleEngraved()), printItem: 6 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedAcknowledgment()), printItem: 7 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedManualInput()), printItem: 8 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedNotCalculated()), printItem: 9 });
                command.lstRemarkContent.push({ usedClassification: self.convertBoolToNum(self.checkedExceedByApplication()), printItem: 10 });
                command.workTypeNameDisplay = self.selectedRuleCode();
                command.newMode = (_.isUndefined(self.currentCodeList()) || _.isNull(self.currentCodeList()) || _.isEmpty(self.currentCodeList())) ? true : false;

                // check to get data old from DB or current interface when it was disable
                if (self.checkedRemarksInput()) {
                    command.remarkInputNo = self.convertValueRemarkInputToDB(self.currentRemarkInputContent());
                } else {
                    let outputItemDailyWorkSchedule: any = _.find(self.allMainDom(), function(o: any) {
                        return self.currentCodeList() == o.itemCode;
                    });
                    command.remarkInputNo = _.isEmpty(outputItemDailyWorkSchedule) ? DEFAULT_DATA_FIRST : outputItemDailyWorkSchedule.remarkInputNo;
                    self.currentRemarkInputContent(self.convertDBRemarkInputToValue(command.remarkInputNo));
                }
                service.save(command).done(function() {
                    self.getDataService().done(function() {
                        self.currentCodeList(self.C3_2_value());
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function () {
                           $('#C3_3').focus(); 
                        });
                        blockUI.clear();
                        dfd.resolve();
                    })

                }).fail(function(err) {
                    blockUI.clear();
                    if (err.messageId == "Msg_3") {
                        $("#C3_2").ntsError('set', { messageId: "Msg_3" });
                    } else {
                        nts.uk.ui.dialog.alertError(err);
                    }
                    dfd.reject();
                })

                return dfd.promise();
            }

            private newMode(): void {
                let self = this;
                self.currentCodeList('');
                self.C3_2_value('');
                self.C3_3_value('');
                $('#C3_2').focus();
                self.getOutputItemDailyWorkSchedule([]);
                self.enableBtnDel(false);
                self.selectedRuleCode(0);
                _.delay(() => {
                    nts.uk.ui.errors.clearAll();
                }, 400);
            }

            private convertBoolToNum(value: boolean): number {
                return value ? 1 : 0;
            }

            private convertNumToBool(value: number): boolean {
                return value == 1 ? true : false;
            }

            // return to screen A
            closeScreenC(): void {
                let self = this;
                nts.uk.ui.windows.setShared('KWR001_C', self.currentCodeList(), true);
                nts.uk.ui.windows.close();
            }

            /*
             *  set default data
            */
            private setRemarksContentDefault(): void {
                let self = this;
                self.checkedRemarksInput(false);
                self.checkedMasterUnregistered(false);
                self.checkedEngraving(false);
                self.checkedImprintingOrderNotCorrect(false);
                self.checkedLeavingEarly(false);
                self.checkedHolidayStamp(false);
                self.checkedDoubleEngraved(false);
                self.checkedAcknowledgment(false);
                self.checkedManualInput(false);
                self.checkedNotCalculated(false);
                self.checkedExceedByApplication(false);
            }

            /*
             *  remove data       
            */
            private removeData(): void {
                let self = this;
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                    blockUI.grayout();
                    service.remove(self.currentCodeList()).done(function() {
                        let indexCurrentCode = _.findIndex(self.outputItemList(), function(value, index) {
                            return self.currentCodeList() == value.code;
                        })

                        // self.currentCodeList only have 1 element in list
                        if (self.outputItemList().length == 1) {
                            self.currentCodeList(null);
                        }
                        // when current code was selected is last element in list self.currentCodeList
                        else if (indexCurrentCode == (self.outputItemList().length - 1)) {
                            self.currentCodeList(self.outputItemList()[indexCurrentCode - 1].code);
                        }
                        // when current code was selected in place middle in list self.currentCodeList
                        else {
                            self.currentCodeList(self.outputItemList()[indexCurrentCode + 1].code);
                        }
                        self.getDataService().done(function() {
                            blockUI.clear();
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                                if (_.isEmpty(self.currentCodeList())) {
                                    $('#C3_2').focus();
                                } else {
                                    $('#C3_3').focus();
                                }
                            });
                        })
                    })
                })
            }

            /*
             *  convert value remark input to DB       
             */
            private convertValueRemarkInputToDB(args: string): number {
                return _.parseInt(args) - 1;
            }

            /*
              *  convert from DB remark input to value client       
              */
            private convertDBRemarkInputToValue(args: number): string {
                return _.toString(args + 1);
            }
        }
        class ItemModel {
            code: string;
            name: string;
            id: string;
            constructor(code: string, name: string, id: string) {
                this.code = code;
                this.name = name;
                this.id = id;
            }
        }
    }
}
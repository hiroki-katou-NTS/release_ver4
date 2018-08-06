module nts.uk.at.view.kwr006.c {

    import service = nts.uk.at.view.kwr006.c.service;
    export module viewmodel {
        const DEFAULT_DATA_FIRST = 0;
        export class ScreenModel {
            items: KnockoutObservableArray<ItemModel>;
            outputItemList: KnockoutObservableArray<ItemModel>;
            currentCodeList: KnockoutObservable<any>;
            columns: KnockoutObservableArray<any>;


            allMainDom: KnockoutObservable<any>;
            outputItemPossibleLst: KnockoutObservableArray<ItemModel>;

            currentCodeListSwap: KnockoutObservableArray<ItemModel>;
            C3_2_value: KnockoutObservable<string>;
            C3_3_value: KnockoutObservable<string>;

            enableBtnDel: KnockoutObservable<boolean>;
            enableCodeC3_2: KnockoutObservable<boolean>;
            //a8-2
            itemListConditionSet: KnockoutObservableArray<any>;
            selectedCodeA8_2: KnockoutObservable<number>;

            storeCurrentCodeBeforeCopy: KnockoutObservable<string>;

            //c8-5
            remarkInputContents: KnockoutObservableArray<ItemModel>;
            currentRemarkInputContent: KnockoutObservable<number>;
            isEnableRemarkInputContents: KnockoutComputed<boolean>;

            constructor() {
                var self = this;
                self.C3_2_value = ko.observable("");
                self.C3_3_value = ko.observable("");

                self.allMainDom = ko.observable();
                self.outputItemPossibleLst = ko.observableArray([]);

                self.currentCodeListSwap = ko.observableArray([]);
                self.outputItemList = ko.observableArray([]);
                self.currentCodeList = ko.observable();

                self.enableBtnDel = ko.observable(false);
                self.enableCodeC3_2 = ko.observable(false);
                self.currentCodeList.subscribe(function(value) {
                    let codeChoose = _.find(self.allMainDom(), function(o: any) {
                        return value == o.itemCode;
                    });
                    if (!_.isNil(codeChoose)) {
                        nts.uk.ui.errors.clearAll();
                        self.C3_2_value(codeChoose.itemCode);
                        self.C3_3_value(codeChoose.itemName);
                        let outputItemMonthlyWorkSchedule: any = _.find(self.allMainDom(), function(o: any) {
                            return codeChoose.itemCode == o.itemCode;
                        });
                        self.getOutputItemMonthlyWorkSchedule(outputItemMonthlyWorkSchedule);
                        self.enableBtnDel(true);
                        self.enableCodeC3_2(false);
                        self.selectedCodeA8_2(outputItemMonthlyWorkSchedule.printSettingRemarksColumn);
                        self.currentRemarkInputContent(outputItemMonthlyWorkSchedule.remarkInputContent);
                        $('#C3_3').focus();
                    } else {
                        self.newMode();
                    }
                });
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText("KWR006_40"), prop: 'code', width: 70 },
                    { headerText: nts.uk.resource.getText("KWR006_41"), prop: 'name', width: 180 }
                ]);
                self.itemListConditionSet = ko.observableArray([
                    new BoxModel(0, nts.uk.resource.getText("KWR006_56")),
                    new BoxModel(1, nts.uk.resource.getText("KWR006_57"))
                ]);
                self.items = ko.observableArray([]);
                self.selectedCodeA8_2 = ko.observable(0);
                self.isEnableRemarkInputContents = ko.computed(function() {
                    return self.selectedCodeA8_2() == 1;
                });
                self.remarkInputContents = ko.observableArray([]);
                self.storeCurrentCodeBeforeCopy = ko.observable('');
                self.currentRemarkInputContent = ko.observable(0);

            }

            /*
             * set data to C7_2, C7_8 
            */
            private getOutputItemMonthlyWorkSchedule(data?: any): void {
                let self = this;

                const lstSwapLeft = self.outputItemPossibleLst().sort((a, b) => parseInt(a.code) - parseInt(b.code));
                let lstSwapRight = [];
                if (data) {
                    lstSwapRight = data.lstDisplayedAttendance.map(item => {
                        return { code: item.attendanceDisplay + "", name: item.attendanceName };
                    }).sort((a, b) => parseInt(a.code) - parseInt(b.code));
                }

                // refresh data for C7_2
                self.items(lstSwapLeft);
                // refresh data for C7_8
                self.currentCodeListSwap(lstSwapRight);
            }

            /*
            Open D screen
            */
            openScreenD() {
                var self = this;
                nts.uk.ui.windows.setShared('KWR006_D', self.outputItemPossibleLst(), true);
                if (!_.isEmpty(self.currentCodeList())) {
                    self.storeCurrentCodeBeforeCopy(self.currentCodeList());
                }
                nts.uk.ui.windows.sub.modal('/view/kwr/006/d/index.xhtml').onClosed(function(): any {
                    nts.uk.ui.errors.clearAll();
                    const KWR006DOutput = nts.uk.ui.windows.getShared('KWR006_D');
                    if (!_.isNil(KWR006DOutput)) {
                        self.currentCodeList('');
                        if (!_.isEmpty(KWR006DOutput.lstAtdChoose)) {
                            const chosen = self.outputItemPossibleLst().filter(item => _.some(KWR006DOutput.lstAtdChoose, atd => atd.itemDaily == item.code));
                            self.items(self.outputItemPossibleLst());
                            self.currentCodeListSwap(chosen);
                            $('#C3_3').focus();
                        } else {
                            $('#C3_2').focus();
                        }

                        self.C3_2_value(nts.uk.ui.windows.getShared('KWR006_D').codeCopy);
                        self.C3_3_value(nts.uk.ui.windows.getShared('KWR006_D').nameCopy);
                    } else {
                        self.currentCodeList(self.storeCurrentCodeBeforeCopy());
                    }
                });
            }

            /*
                New mode
            */
            private newMode(): void {
                let self = this;
                self.currentCodeList('');
                self.C3_2_value('');
                self.C3_3_value('');
                nts.uk.ui.errors.clearAll();
                $('#C3_2').focus();
                self.getOutputItemMonthlyWorkSchedule();
                self.enableBtnDel(false);
                self.enableCodeC3_2(true);
                self.selectedCodeA8_2(0);
                self.currentRemarkInputContent(0);
            }

            /*
                Save data
            */
            private saveData(): JQueryPromise<any> {
                let self = this;

                $('.save-error').ntsError('check');
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }

                let dfd = $.Deferred();
                let command: any = {};
                command.itemCode = self.C3_2_value();
                command.itemName = self.C3_3_value();
                command.lstDisplayedAttendance = [];
                command.printSettingRemarksColumn = self.selectedCodeA8_2();
                _.forEach(self.currentCodeListSwap(), function(value, index) {
                    command.lstDisplayedAttendance.push({ sortBy: index, itemToDisplay: value.code });
                });

                if (self.selectedCodeA8_2() == 1) {
                    command.remarkInputNo = self.currentRemarkInputContent();
                } else {
                    let outputItemMonthlyWorkSchedule: any = _.find(self.allMainDom(), function(o: any) {
                        return self.currentCodeList() == o.itemCode;
                    });
                    command.remarkInputNo = _.isEmpty(outputItemMonthlyWorkSchedule) ? DEFAULT_DATA_FIRST : outputItemMonthlyWorkSchedule.remarkInputNo;
                    self.currentRemarkInputContent(command.remarkInputNo);
                }

                command.newMode = (_.isUndefined(self.currentCodeList()) || _.isNull(self.currentCodeList()) || _.isEmpty(self.currentCodeList())) ? true : false;
                service.save(command).done(function() {
                    self.getDataService().done(function() {
                        self.currentCodeList(self.C3_2_value());
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                            $('#C3_3').focus();
                        });
                        dfd.resolve();
                    })

                }).fail(function(err) {
                    nts.uk.ui.dialog.alertError(err);
                    dfd.reject();
                })

                return dfd.promise();
            }

            /*
                Remove data
            */
            private removeData(): void {
                let self = this;
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
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
                Close C screen
            */
            public closeScreenC(): void {
                nts.uk.ui.windows.close();
            }

            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                let self = this;

                $.when(self.getDataService(), self.getEnumSettingPrint(), self.getEnumRemarkInputContent()).done(function() {
                    self.currentCodeList(nts.uk.ui.windows.getShared('selectedCode'));
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
                    self.allMainDom(data.outputItemMonthlyWorkSchedule);

                    // variable temporary 
                    let temp: any[] = [];
                    _.forEach(data.monthlyAttendanceItem, function(value) {
                        temp.push(value);
                    })
                    self.outputItemPossibleLst(temp);

                    let arrCodeName: ItemModel[] = [];
                    _.forEach(data.outputItemMonthlyWorkSchedule, function(value, index) {
                        arrCodeName.push({ code: value.itemCode + "", name: value.itemName });
                    });
                    self.outputItemList(arrCodeName);
                    self.items(_.isEmpty(data.monthlyAttendanceItem) ? [] : data.monthlyAttendanceItem);
                    dfd.resolve();
                })

                return dfd.promise();
            }

            /*
                get Enum Setting Print
            */
            private getEnumSettingPrint(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                service.getEnumSettingPrint().done(function(data: any) {
                    console.log(data);
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
                    self.remarkInputContents(data);
                    dfd.resolve();
                })
                return dfd.promise();
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

            private convertBoolToNum(value: boolean): number {
                return value ? 1 : 0;
            }

            private convertNumToBool(value: number): boolean {
                return value == 1 ? true : false;
            }
        }

        class ItemModel {
            code: string;
            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }

        class BoxModel {
            code: number;
            name: string;
            constructor(code: number, name: string) {
                this.code = code;
                this.name = name;
            }
        }

    }
}
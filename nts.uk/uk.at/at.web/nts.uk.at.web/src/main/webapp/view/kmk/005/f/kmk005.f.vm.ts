module nts.uk.at.view.kmk005.f {
    export module viewmodel {
        import href = nts.uk.request.jump;
        import fService = nts.uk.at.view.kmk005.f.service;
        export class ScreenModel {
            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;
            time: KnockoutObservable<string>;
            bonusPaySettingList: KnockoutObservableArray<any>;
            bonusPayTimeItemList: KnockoutObservableArray<BonusPayTimeItem>;
            specBonusPayTimeItemList: KnockoutObservableArray<BonusPayTimeItem>;
            specDateItem: KnockoutObservableArray<SpecDateItem>;
            currentBonusPaySetting: KnockoutObservable<BonusPaySetting>;
            currentBPSetCode: KnockoutObservable<string>;
            currentBonusPayTimesheets: KnockoutObservableArray<BonusPayTimesheet>;
            currentSpecBonusPayTimesheets: KnockoutObservableArray<SpecBonusPayTimesheet>;
            isUpdate: KnockoutObservable<boolean>;
            constructor() {
                var self = this;
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: nts.uk.resource.getText("KMK005_17"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: nts.uk.resource.getText("KMK005_18"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
                ]);
                self.selectedTab = ko.observable('tab-1');
                self.time = ko.observable('');
                self.bonusPaySettingList = ko.observableArray([]);
                self.bonusPayTimeItemList = ko.observableArray([]);
                self.specBonusPayTimeItemList = ko.observableArray([]);
                self.currentBonusPaySetting = ko.observable(new BonusPaySetting('', '', ''));
                self.currentBPSetCode = ko.observable('');
                self.currentBonusPayTimesheets = ko.observableArray([]);
                self.currentSpecBonusPayTimesheets = ko.observableArray([]);
                self.specDateItem = ko.observableArray([]);
                self.isUpdate = ko.observable(true);
                self.currentBPSetCode.subscribe(function(value) {
                    if (value != '') {
                        self.isUpdate(true);
                        self.currentBonusPaySetting(ko.mapping.fromJS(_.find(self.bonusPaySettingList(), (o) => { 
                            let codes = o.code;
                            return String(codes) == value; 
                        })));
                        self.getBonusPayTimesheets(value);
                    } else {
                        self.isUpdate(false);
                        self.currentBonusPaySetting(new BonusPaySetting('', '', ''));
                    }
                });
            }

            startPage(): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                let dfdBonusPaySetting = fService.getBonusPaySetting();
                let dfdBonusPayTimeItem = fService.getBonusPayTimeItem();
                let dfdSpecBonusPayTimeItem = fService.getSpecBonusPayTimeItem();
                let dfdSpecDateItem = fService.getSpecDateItem();
                $.when(dfdBonusPaySetting, dfdBonusPayTimeItem, dfdSpecBonusPayTimeItem, dfdSpecDateItem)
                    .done((dfdBonusPaySettingData, dfdBonusPayTimeItemData, dfdSpecBonusPayTimeItemData, dfdSpecDateItemData) => {
                        self.bonusPayTimeItemList.removeAll();
                        _.forEach(dfdBonusPayTimeItemData, (item) => {
                            self.bonusPayTimeItemList.push(item);
                        });
                        _.forEach(dfdSpecBonusPayTimeItemData, (item) => {
                            self.specBonusPayTimeItemList.push(item);
                        });
                        _.forEach(dfdSpecDateItemData, (item) => {
                            self.specDateItem.push(item);
                        });
                        if (dfdBonusPaySettingData.length != 0) {
                            self.bonusPaySettingList(dfdBonusPaySettingData);
                            self.currentBonusPaySetting(ko.mapping.fromJS(_.first(self.bonusPaySettingList())));
                            self.currentBPSetCode(self.currentBonusPaySetting().code());
                            self.isUpdate(true);
                        } else {
                            self.isUpdate(false);
                            self.createData();
                            self.currentBPSetCode('');
                        }
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                    }).fail((res1, res2, res3, res4) => {
                        nts.uk.ui.dialog.alertError(res1.message + '\n' + res2.message + '\n' + res3.message + '\n' + res4.message).then(function() { nts.uk.ui.block.clear(); });
                        dfd.reject();
                    })
                return dfd.promise();
            }

            getBonusPaySetting(): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                fService.getBonusPaySetting()
                    .done((data) => {
                        if (data.length != 0) {
                            self.isUpdate(true);
                            self.bonusPaySettingList(data);
                            self.currentBonusPaySetting(ko.mapping.fromJS(_.first(self.bonusPaySettingList())));
                            self.currentBPSetCode(self.currentBonusPaySetting().code());
                        } else {
                            self.isUpdate(false);
                            self.createData();
                        }
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                    }).fail((res) => {
                        nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                        dfd.reject();
                    })
                return dfd.promise();
            }

            getBonusPayTimesheets(code: string): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                let dfdTimesheetList = fService.getBonusPayTimesheet(code);
                let dfdGetSpecTimesheetList = fService.getSpecBonusPayTimesheet(code);
                $.when(dfdTimesheetList, dfdGetSpecTimesheetList).done((dfdTimesheetListData, dfdGetSpecTimesheetListData) => {
                    self.currentBonusPayTimesheets.removeAll();
                    self.currentSpecBonusPayTimesheets.removeAll();
                    if(nts.uk.util.isNullOrEmpty(dfdTimesheetListData)||nts.uk.util.isNullOrEmpty(dfdGetSpecTimesheetListData)){
                        for (let i = 0; i < 10; i++) {
                            self.currentBonusPayTimesheets.push(new BonusPayTimesheet('', 1, 1, '', i.toString(), 0, 0, 0, 0));
                            self.currentSpecBonusPayTimesheets.push(new SpecBonusPayTimesheet('', 1, 1, '', i.toString(), 0, 0, 0, 0, 1));
                        }    
                    } else {
                        dfdTimesheetListData.forEach(function(item) {
                            self.currentBonusPayTimesheets.push(new BonusPayTimesheet(
                                item.companyId,
                                item.timeSheetNO,
                                item.useAtr,
                                item.bonusPaySettingCode,
                                item.timeItemId,
                                item.startTime,
                                item.endTime,
                                item.roundingTimeAtr,
                                item.roundingAtr));
                        });
                        
                        dfdGetSpecTimesheetListData.forEach(function(item) {
                            self.currentSpecBonusPayTimesheets.push(new SpecBonusPayTimesheet(
                                item.companyId,
                                item.timeSheetNO,
                                item.useAtr,
                                item.bonusPaySettingCode,
                                item.timeItemId,
                                item.startTime,
                                item.endTime,
                                item.roundingTimeAtr,
                                item.roundingAtr,
                                item.specialDateItemNO));
                        });
                    }
                    nts.uk.ui.block.clear();
                    dfd.resolve();
                }).fail((res1, res2) => {
                    nts.uk.ui.dialog.alertError(res1.message + '\n' + res2.message).then(function() { nts.uk.ui.block.clear(); });
                    dfd.reject();
                });
                return dfd.promise();
            }

            createData(): void {
                var self = this;
                self.isUpdate(false);
                self.currentBonusPaySetting(new BonusPaySetting('', '', ''));
                self.currentBPSetCode(self.currentBonusPaySetting().code());
                self.currentBonusPayTimesheets.removeAll();
                self.currentSpecBonusPayTimesheets.removeAll();
                for (let i = 0; i < 10; i++) {
                    self.currentBonusPayTimesheets.push(new BonusPayTimesheet('', i+1, 0, (i+1).toString(), (i+1).toString(), 0, 0, 0, 0));
                    self.currentSpecBonusPayTimesheets.push(new SpecBonusPayTimesheet('', i+1, 0, (i+1).toString(), (i+1).toString(), 0, 0, 0, 0, 0));
                }
            }

            submitData(): void {
                nts.uk.ui.block.invisible();
                var self = this;
                $(".inputRequired").trigger("validate");
                if (!nts.uk.ui.errors.hasError()) {
                    if (self.isUpdate()) {
                        fService.updateBonusPaySetting(
                            self.createCommand(self.currentBonusPaySetting(), self.currentBonusPayTimesheets(), self.currentSpecBonusPayTimesheets())
                        ).done((data) => {
                            self.getBonusPaySetting();
                            nts.uk.ui.block.clear();
                        }).fail((res) => {
                            nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                        });
                    } else {
                        fService.insertBonusPaySetting(
                            self.createCommand(self.currentBonusPaySetting(), self.currentBonusPayTimesheets(), self.currentSpecBonusPayTimesheets())
                        ).done((data) => {
                            self.getBonusPaySetting();
                            nts.uk.ui.block.clear();
                        }).fail((res) => {
                            nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                        });
                    }
                } else {
                    nts.uk.ui.block.clear();
                }
            }

            deleteData(): void {
                nts.uk.ui.block.invisible();
                var self = this;
                fService.deleteBonusPaySetting(
                    self.createCommand(self.currentBonusPaySetting(), self.currentBonusPayTimesheets(), self.currentSpecBonusPayTimesheets())
                ).done((data) => {
                    self.getBonusPaySetting();
                    nts.uk.ui.block.clear();
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                });
            }

            createCommand(bonusPaySetting: BonusPaySetting, bonusPayTimesheets: Array<BonusPayTimesheet>, specBonusPayTimesheets: Array<SpecBonusPayTimesheet>): any {
                var self = this;
                return {
                    companyId: '',
                    code: bonusPaySetting.code(),
                    name: bonusPaySetting.name(),
                    lstBonusPayTimesheet: ko.mapping.toJS(bonusPayTimesheets),
                    lstSpecBonusPayTimesheet: ko.mapping.toJS(specBonusPayTimesheets)
                }
            }
            
            navigateView() {
                let self = this;           
                href("../a/index.xhtml");
            }
            
            navigateViewToK() {
                let self = this;           
                href("../g/index.xhtml");
            }
        }

       export class BonusPaySetting {
            companyId: KnockoutObservable<string>;
            name: KnockoutObservable<string>;
            code: KnockoutObservable<string>;
            constructor(companyId: string, name: string, code: string) {
                this.companyId = ko.observable(companyId);
                this.name = ko.observable(name);
                this.code = ko.observable(code);
            }
        }

        export class BonusPayTimeItem {
            companyId: string;
            timeItemId: string;
            useAtr: number;
            timeItemName: string;
            timeItemNo: number;
            timeItemTypeAtr: number;
            constructor(companyId: string, timeItemId: string, useAtr: number, timeItemName: string, timeItemNo: number, timeItemTypeAtr: number) {
                this.companyId = companyId;
                this.timeItemId = timeItemId;
                this.useAtr = useAtr;
                this.timeItemName = timeItemName;
                this.timeItemNo = timeItemNo;
                this.timeItemTypeAtr = timeItemTypeAtr;
            }
        }

        export class SpecDateItem {
            useAtr: number;
            timeItemId: string;
            specificName: string;
            specificDateItemNo: string;
            constructor(useAtr: number, timeItemId: string, specificName: string, specificDateItemNo: string) {
                this.useAtr = useAtr;
                this.timeItemId = timeItemId;
                this.specificName = specificName;
                this.specificDateItemNo = specificDateItemNo;
            }
        }

        export class BonusPayTimesheet {
            companyId: KnockoutObservable<string>;
            timeSheetNO: KnockoutObservable<number>;
            useAtr: KnockoutObservable<number>;
            bonusPaySettingCode: KnockoutObservable<string>;
            timeItemId: KnockoutObservable<string>;
            startTime: KnockoutObservable<number>;
            endTime: KnockoutObservable<number>;
            roundingTimeAtr: KnockoutObservable<number>;
            roundingAtr: KnockoutObservable<number>;
            constructor(companyId: string, timeSheetNO: number, useAtr: number, bonusPaySettingCode: string, timeItemId: string,
                startTime: number, endTime: number, roundingTimeAtr: number, roundingAtr: number) {
                this.companyId = ko.observable(companyId);
                this.timeSheetNO = ko.observable(timeSheetNO);
                this.useAtr = ko.observable(useAtr);
                this.bonusPaySettingCode = ko.observable(bonusPaySettingCode);
                this.timeItemId = ko.observable(timeItemId);
                this.startTime = ko.observable(startTime);
                this.endTime = ko.observable(endTime);
                this.roundingTimeAtr = ko.observable(roundingTimeAtr);
                this.roundingAtr = ko.observable(roundingAtr);
            }
        }

        export class SpecBonusPayTimesheet {
            companyId: KnockoutObservable<string>;
            timeSheetNO: KnockoutObservable<number>;
            useAtr: KnockoutObservable<number>;
            bonusPaySettingCode: KnockoutObservable<string>;
            timeItemId: KnockoutObservable<string>;
            startTime: KnockoutObservable<number>;
            endTime: KnockoutObservable<number>;
            roundingTimeAtr: KnockoutObservable<number>;
            roundingAtr: KnockoutObservable<number>;
            specialDateItemNO: KnockoutObservable<number>;
            constructor(companyId: string, timeSheetNO: number, useAtr: number, bonusPaySettingCode: string, timeItemId: string,
                startTime: number, endTime: number, roundingTimeAtr: number, roundingAtr: number, specialDateItemNO: number) {
                this.companyId = ko.observable(companyId);
                this.timeSheetNO = ko.observable(timeSheetNO);
                this.useAtr = ko.observable(useAtr);
                this.bonusPaySettingCode = ko.observable(bonusPaySettingCode);
                this.timeItemId = ko.observable(timeItemId);
                this.startTime = ko.observable(startTime);
                this.endTime = ko.observable(endTime);
                this.roundingTimeAtr = ko.observable(roundingTimeAtr);
                this.roundingAtr = ko.observable(roundingAtr);
                this.specialDateItemNO = ko.observable(specialDateItemNO);
            }
        }
    }
}
module nts.uk.pr.view.qmm008.b.viewmodel {
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import dialog = nts.uk.ui.dialog;
    import getText = nts.uk.resource.getText;
    import modal = nts.uk.ui.windows.sub.modal;
    import block = nts.uk.ui.block;
    import model = nts.uk.pr.view.qmm008.share.model;
    import service = nts.uk.pr.view.qmm008.b.service;
    export class ScreenModel {
        isUpdateMode: KnockoutObservable<boolean> = ko.observable(true);
        isOnStartUp: boolean = true;
        // History Tree Grid C1_1 -> C1_12 
        socialInsuranceOfficeList: KnockoutObservableArray<model.SocialInsuranceOffice> = ko.observableArray([]);
        isSelectedHistory: KnockoutObservable<boolean> = ko.observable(false);
        healthInsuranceRateTreeList: KnockoutObservableArray<model.TreeGridNode> = ko.observableArray([]);
        // Office and history info
        selectedOffice: any = null;
        selectedHistoryId: string = "";
        selectedHealthInsurance: KnockoutObservable<number> = ko.observable(null);

        // Health insurance item
        selectedHistoryPeriod: KnockoutObservable<model.GenericHistoryYearMonthPeiod> = ko.observable({ displayStart: '', displayJapanYearMonth: '', displayEnd: '' });
        bonusHealthInsuranceRate: KnockoutObservable<model.BonusHealthInsuranceRate> = ko.observable(null);
        healthInsuranceMonthlyFee: KnockoutObservable<model.HealthInsuranceMonthlyFee> = ko.observable(null);
        constructor() {
            let self = this;
            $("#B3").ntsFixedTable({});
            $("#B4").ntsFixedTable({});
            self.watchDataChanged();
            self.initBlankData();
        }

        initBlankData() {
            let self = this;
            self.bonusHealthInsuranceRate(new model.BonusHealthInsuranceRate(null));
            self.healthInsuranceMonthlyFee(new model.HealthInsuranceMonthlyFee(null));
        }

        register() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            $('.tab-b .nts-input').trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            block.invisible();
            // Register data
            let command = {
                officeCode: self.selectedOffice.socialInsuranceCode,
                bonusHealthInsuranceRate: ko.toJS(self.bonusHealthInsuranceRate),
                healthInsuranceMonthlyFee: ko.toJS(self.healthInsuranceMonthlyFee),
                yearMonthHistoryItem: ko.toJS(self.selectedHistoryPeriod)
            }
            // Update historyId for case clone previous data
            command.bonusHealthInsuranceRate.historyId = command.yearMonthHistoryItem.historyId;
            command.healthInsuranceMonthlyFee.historyId = command.yearMonthHistoryItem.historyId;
            service.registerEmployeeHealthInsurance(command).done(function(data) {
                block.clear();
                dialog.info({ messageId: 'Msg_15' });
                self.isUpdateMode(true);
            }).fail(function(err) {
                block.clear();
                dialog.alertError(err.message);
            });
        }
        printPDF() {
            // TODO
            console.log('TODO');
        }

        registerBusinessEstablishment() {
            let self = this;
            modal("/view/qmm/008/d/index.xhtml").onClosed(() => {
                // Reload tree grid if data change
                if (getShared("QMM008_D_RES_PARAMS")) self.showAllOfficeAndHistory();
            });
        }

        standardRemunerationMonthlyAmount() {
            let self = this;
            let selectedOffice = self.selectedOffice, selectedHistoryId = self.selectedHistoryId;
            let selectedHistory = ko.toJS(self.selectedHistoryPeriod);
            setShared("QMM008_E_PARAMS", { selectedOffice: self.selectedOffice, selectedHistory: selectedHistory, history: selectedOffice.welfareInsuranceRateHistory.history });
            modal("/view/qmm/008/e/index.xhtml").onClosed(() => {
                // Reload tree grid if data change
                if (getShared("QMM008_E_RES_PARAMS")) self.showAllOfficeAndHistory();
            });
        }

        masterCorrectionLog() {
            // TODO
            console.log('TODO');
        }
        startPage(): JQueryPromise<any> {
            let self = this;
            return self.showAllOfficeAndHistory();
        }

        showAllOfficeAndHistory(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            block.invisible();
            service.findAllOffice().done(function(data) {
                if (data) {
                    // show add office screen if there are no office
                    if (data.length == 0) {
                        self.registerBusinessEstablishment();
                        return;
                    }
                    let socailInsuranceOfficeList: Array<model.SocialInsuranceOffice> = [];
                    data.forEach(office => {
                        socailInsuranceOfficeList.push(new model.SocialInsuranceOffice(office));
                    });
                    self.socialInsuranceOfficeList(socailInsuranceOfficeList);
                    self.convertToTreeGridList();
                    // select first office and last history
                    if (self.isOnStartUp) {
                        self.isOnStartUp = false;
                        let firstOffice = data[0].healthInsuranceFeeRateHistory
                        if (firstOffice.history.length > 0) self.selectedHealthInsurance(firstOffice.socialInsuranceCode + "___" + firstOffice.history[0].historyId);
                        else self.selectedHealthInsurance(firstOffice.socialInsuranceCode);
                    } else self.changeBySelectedValue();
                }
                block.clear();
                dfd.resolve();
            }).fail(function(err) {
                dfd.reject();
                block.clear();
                dialog.alertError(err.message);
            });
            return dfd.promise();
        }

        watchDataChanged() {
            let self = this;
            self.selectedHealthInsurance.subscribe(function(selectedValue: any) {
                nts.uk.ui.errors.clearAll();
                if (selectedValue) {
                    self.showByHistory();
                    // if select history
                    if (selectedValue.length >= 36) {
                        self.isSelectedHistory(true);
                    } else { // if select office
                        self.isSelectedHistory(false);
                        self.initBlankData();
                    }
                }
            });
        }

        showByHistory() {
            let self = this;
            if (!self.isUpdateMode()) {
                // reload to remove not register history
                self.isUpdateMode(true);
                self.showAllOfficeAndHistory();
            } else {
                self.changeBySelectedValue();
            }
        }

        changeBySelectedValue() {
            let self = this;
            if (self.selectedHealthInsurance()) {
                // ___ is char between office code and history id
                let selectedInsuranceCode = self.selectedHealthInsurance().split('___')[0],
                    selectedHistoryId = self.selectedHealthInsurance().split('___')[1],
                    selectedHistoryPeriod = null,
                    listInsuranceOffice = ko.toJS(self.socialInsuranceOfficeList);
                self.selectedHistoryId = selectedHistoryId;
                // find selected office
                self.selectedOffice = _.find(listInsuranceOffice, { socialInsuranceCode: selectedInsuranceCode });
                if (selectedHistoryId) {
                    let selectedHistoryPeriod;
                    // find selected history
                    if (self.selectedOffice) {
                        selectedHistoryPeriod = _.find(self.selectedOffice.healthInsuranceFeeRateHistory.history, { historyId: selectedHistoryId });
                    }
                    if (selectedHistoryPeriod) {
                        selectedHistoryPeriod.displayJapanYearMonth = "(" + self.convertYearMonthToDisplayJpanYearMonth(selectedHistoryPeriod.startMonth) + ")";
                        selectedHistoryPeriod.displayEnd = self.convertYearMonthToDisplayYearMonth(selectedHistoryPeriod.endMonth);
                        selectedHistoryPeriod.displayStart = self.convertYearMonthToDisplayYearMonth(selectedHistoryPeriod.startMonth);
                        self.selectedHistoryPeriod(selectedHistoryPeriod);
                        self.showEmployeeHealthInsuranceByHistoryId(self.selectedHistoryId);
                    } else {
                        // display none if not found
                        self.selectedHistoryPeriod({ displayStart: '', displayJapanYearMonth: '', displayEnd: '' });
                    }
                }
            }
        }

        showEmployeeHealthInsuranceByHistoryId(historyId) {
            let self = this;
            block.invisible();
            service.findEmployeeHealthInsuranceByHistoryId(historyId).done(function(data) {
                if (data) {
                    self.healthInsuranceMonthlyFee(new model.HealthInsuranceMonthlyFee(data.healthInsuranceMonthlyFeeDto));
                    self.bonusHealthInsuranceRate(new model.BonusHealthInsuranceRate(data.bonusHealthInsuranceRateDto));
                }
                if (!self.isUpdateMode()) $('#B2_7').focus();
                block.clear();
            }).fail(function(err) {
                block.clear();
                dialog.alertError(err.message);
            });
        }

        convertToTreeGridList() {
            let self = this,
                healthInsuranceList = ko.toJS(self.socialInsuranceOfficeList),
                displayHealthInsuranceList: Array<model.TreeGridNode> = [],
                pensionItem = {};
            healthInsuranceList.forEach(function(office) {
                let healthInsuranceItem = new model.TreeGridNode(office.socialInsuranceCode, office.socialInsuranceCode + ' ' + office.socialInsuranceName, []);
                if (office.healthInsuranceFeeRateHistory) {
                    let displayStart, displayEnd = "";
                    office.healthInsuranceFeeRateHistory.history.forEach(function(history) {
                        displayStart = self.convertYearMonthToDisplayYearMonth(history.startMonth);
                        displayEnd = self.convertYearMonthToDisplayYearMonth(history.endMonth);
                        // ___ is for child contain office code and history id
                        healthInsuranceItem.child.push(new model.TreeGridNode(office.socialInsuranceCode + '___' + history.historyId, displayStart + ' ~ ' + displayEnd, []));
                    });
                }
                displayHealthInsuranceList.push(healthInsuranceItem);
            });
            self.healthInsuranceRateTreeList(displayHealthInsuranceList);
        }

        convertYearMonthToDisplayYearMonth(yearMonth) {
            return nts.uk.time.formatYearMonth(yearMonth);
        }
        
        convertYearMonthToDisplayJpanYearMonth (yearMonth) {
            return nts.uk.time.yearmonthInJapanEmpire(yearMonth).toString().split(' ').join('');    
        }

        createNewHistory() {
            let self = this;
            let selectedOffice = self.selectedOffice;
            let history = selectedOffice.healthInsuranceFeeRateHistory.history;
            setShared("QMM008_G_PARAMS", { selectedOffice: selectedOffice, history: history });
            modal("/view/qmm/008/g/index.xhtml").onClosed(() => {
                let params = getShared("QMM008_G_RES_PARAMS");
                if (params) {
                    let socialInsuranceOfficeList = ko.toJS(self.socialInsuranceOfficeList);
                    let historyId = nts.uk.util.randomId();
                    // update previous history
                    if (history.length > 0) {
                        let beforeLastestMonth = moment(params.startMonth, 'YYYYMM').subtract(1, 'month');
                        history[0].endMonth = beforeLastestMonth.format('YYYYMM');
                    }
                    // add new history
                    history.unshift({ historyId: historyId, startMonth: params.startMonth, endMonth: '999912' });
                    socialInsuranceOfficeList.forEach(office => {
                        if (office.socialInsuranceCode == selectedOffice.socialInsuranceCode) {
                            office.healthInsuranceFeeRateHistory.history = history;
                            office = new model.SocialInsuranceOffice(office);
                        }
                    });
                    // update office and tree grid
                    self.socialInsuranceOfficeList(socialInsuranceOfficeList);
                    self.convertToTreeGridList();
                    self.selectedHealthInsurance(selectedOffice.socialInsuranceCode + "___" + historyId);
                    // clone data 
                    if (params.takeoverMethod == model.TAKEOVER_METHOD.FROM_LASTEST_HISTORY && history.length > 1) {
                        self.showEmployeeHealthInsuranceByHistoryId(history[1].historyId);
                    } else {
                        self.initBlankData();    
                    }
                    self.isUpdateMode(false);
                }
                $("#B2_7").focus();
            });
        }

        editHistory() {
            let self = this;
            let selectedOffice = self.selectedOffice, selectedHistoryId = self.selectedHistoryId;
            let selectedHistory = ko.toJS(self.selectedHistoryPeriod);
            let history = selectedOffice.healthInsuranceFeeRateHistory.history;
            setShared("QMM008_H_PARAMS", { screen: "B", selectedOffice: self.selectedOffice, selectedHistory: selectedHistory, history: history });
            modal("/view/qmm/008/h/index.xhtml").onClosed(() => {
                $("#B1_5").focus();
                let params = getShared("QMM008_H_RES_PARAMS");
                if (params) {
                    // update office and tree grid
                    self.showAllOfficeAndHistory();
                    self.convertToTreeGridList();
                    // change selected value
                    if (params.modifyMethod == model.MOFIDY_METHOD.DELETE) {
                        if (history.length <= 1){
                            self.selectedHealthInsurance(selectedOffice.socialInsuranceCode);
                        } else {
                            self.selectedHealthInsurance(selectedOffice.socialInsuranceCode + "___" + history[1].historyId)    
                        }
                    } else {
                        let selectedHealthInsurance = self.selectedHealthInsurance();
                        setTimeout(function(){
                            self.selectedHealthInsurance(selectedOffice.socialInsuranceCode);
                            self.selectedHealthInsurance(selectedHealthInsurance);
                        }, 50);
                        
                    }
                }
            });
        }
    }
}


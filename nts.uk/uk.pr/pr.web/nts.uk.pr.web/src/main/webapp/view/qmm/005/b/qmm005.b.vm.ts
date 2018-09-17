module nts.uk.pr.view.qmm005.b.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import model = nts.uk.pr.view.qmm005.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import modal = nts.uk.ui.windows.sub.modal;

    export class ScreenModel {
        processCateNo: any;
        processInfomationDto: any;
        setDaySupportDtoList: any;
        targetMonth: KnockoutObservable<string>;
        processingYear: KnockoutObservable<number>;
        processingDivisionName: KnockoutObservable<string>;
        settingPaymentList: KnockoutObservableArray<any>;
        processingYearList: KnockoutObservableArray<model.ItemModel>;
        btnText: any;
        isNewMode: KnockoutObservable<boolean>;
        processingYearListSelectedCode: KnockoutObservable<number>;
        show: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.targetMonth = ko.observable();
            self.processingDivisionName = ko.observable();
            self.settingPaymentList = ko.observableArray([]);
            self.processingYearList = ko.observableArray([]);
            self.show = ko.observable(false);
            self.btnText = ko.computed(function () {
                let windowSize = nts.uk.ui.windows.getSelf();
                if (self.show()) {
                    windowSize.$dialog.dialog('option', {
                        position: {
                            my: "top-100",
                            at: "left+200",
                            of: $("#content_dialog")
                        },
                        width: 1700,
                        height: 500
                    });
                    windowSize.$dialog.resize();
                    return "-";
                } else {
                    windowSize.$dialog.dialog('option', {
                        position: {
                            my: "top-100",
                            at: "left+200)",
                            of: $("#content_dialog")
                        },
                        width: 1100,
                        height: 500
                    });
                    windowSize.$dialog.resize();
                    return "+";
                }

            });
            self.isNewMode = ko.observable(false);

            self.processingYearListSelectedCode = ko.observable(-1);
            self.processingYear = ko.observable(2018);
            self.processingYear.subscribe(function (newValue) {
                self.processingYear(newValue);
                self.selectProcessingYear(newValue);

            });
        }

        blankData() {
            var self = this;
            for (var i = 0; i < self.settingPaymentList().length; i++) {
                for (k in self.settingPaymentList()[i]) {
                    var key = k.toString();
                    if (key != 'targetMonth') {
                        self.settingPaymentList()[i][key](null);
                    }
                }
            }
        }

        toggle(): void {
            this.show(!this.show());
        }

        cancel() {
            nts.uk.ui.windows.close();
        }

        startPage(params): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.startupScreen();
            dfd.resolve();
            return dfd.promise();
        }

        startupScreen() {
            // get domain 処理区分基本情報 (ProcessInfomation), 給与支払日設定 (SetDaySupport)
            var self = this;
            self.processCateNo = getShared("QMM005_output_B");
            service.getProcessInfomation(self.processCateNo).done(function (data) {
                if (data) {
                    self.processInfomationDto = data;
                    // B3_2
                    self.processingDivisionName = ko.observable(nts.uk.text.format(nts.uk.resource.getText("#QMM005_97"), self.processCateNo, data.processDivisionName));
                }
            });
            service.getSetDaySupport(self.processCateNo).done(function (data) {
                // B2_2
                // 詳細設定対象_処理年 processingYearList
                var array = [];
                _.forEach(data, function (setDaySupport) {
                    _.forEach(setDaySupport, function (value, key) {
                        if (key == "processDate") {
                            let year = value.toString().substr(0, 4);
                            array.push(new model.ItemModel(year, year + '(' + nts.uk.time.yearInJapanEmpire(year).toString().split(' ').join('') + ')'));
                        }
                    });
                });
                self.processingYearList(_.orderBy(_.uniqBy(array, 'code'), ['code'], ['desc']));
                if (array.length > 0) {
                    self.processingYear(self.processingYearList()[0].code);
                }
            });

        }

        selectProcessingYear(year) {
            /* phần lớn dữ liệu lấy từ setDaySupport*/
            // B3_4				処理年
            var self = this;
            service.getSelectProcessingYear(self.processCateNo, year).done(function (data) {
                if (data.setDaySupportDtoList.length > 0) {
                    var firstArray = [];
                    var index = 0;
                    _.forEach(data.setDaySupportDtoList, function (setDaySupport) {
                        var obj = {};
                        obj["targetMonth"] = ko.observable(++index);
                        // B4_10 支払年月日
                        obj["paymentDate"] = ko.observable(setDaySupport.paymentDate);
                        // B4_11 支払曜日
                        // B4_12				社員抽出基準日
                        obj["employeeExtractionReferenceDate"] = ko.observable(setDaySupport.empExtraRefeDate);
                        // B4_13 社会保険徴収月
                        obj["socialInsuranceCollectionMonth"] = ko.observable(setDaySupport.socialInsurdCollecMonth);
                        // B4_16 要勤務日数
                        obj["numberOfWorkingDays"] = ko.observable(setDaySupport.numberWorkDay);
                        // B6_7	社会保険基準日
                        obj["socialInsuranceStandardDate"] = ko.observable(setDaySupport.socialInsurdStanDate);
                        // B6_8	雇用保険基準日
                        obj["employmentInsuranceStandardDate"] = ko.observable(setDaySupport.empInsurdStanDate);
                        // B6_9	勤怠締め日
                        obj["timeClosingDate"] = ko.observable(setDaySupport.closeDateTime);
                        // B6_10 所得税基準日
                        obj["incomeTaxReferenceDate"] = ko.observable(setDaySupport.incomeTaxDate);
                        // B6_11
                        obj["accountingClosureDate"] = ko.observable(setDaySupport.closureDateAccounting)
                        firstArray.push(obj);
                    });
                    var secondArray = [];
                    _.forEach(data.specPrintYmSetDtoList, function (specPrintYmSet) {
                        secondArray.push({specificationPrintDate: ko.observable(specPrintYmSet.printDate)});
                    });
                    var i;
                    for (i = 0; i < firstArray.length; i++) {
                        firstArray[i]["specificationPrintDate"] = secondArray[i]["specificationPrintDate"];
                    }
                    self.settingPaymentList(firstArray);
                } else {
                    self.blankData();
                }
            });

        }

        creatNewProcessYear() {
            var self = this;
            self.isNewMode(true);
            self.processingYear();
            self.blankData();
        }

        //反映ボタン押下時処理
        //screen E in insert reflect
        reflectionPressingProcess() {
            var self = this;
            // check processingYear valid
            if (self.processingYear()) {
                var array = [];
                service.getValPayDateSet(self.processCateNo).done(function (data) {
                        for (index = 1; index < 13; index++) {
                            let objItem = {
                                targetMonth: ko.observable(index),
                                paymentDate: ko.observable(self.processingYear() + '/' + index + '/' + data.basicSetting.monthlyPaymentDate.datePayMent),
                                employeeExtractionReferenceDate: ko.observable(self.processingYear() + '/' + index + '/' + data.basicSetting.employeeExtractionReferenceDate.refeDate),
                                socialInsuranceCollectionMonth: ko.observable(Number.parseInt(self.processingYear() + self.fullMonth(index))),
                                specificationPrintDate: ko.observable(Number.parseInt(self.processingYear() + '' + index)),
                                numberOfWorkingDays: ko.observable(data.basicSetting.workDay),
                                socialInsuranceStandardDate: ko.observable(self.processingYear() + '/' + index + '/' + data.advancedSetting.sociInsuStanDate.refeDate),
                                employmentInsuranceStandardDate: ko.observable((self.processingYear() - 1) + '/' + index + '/' + data.advancedSetting.empInsurStanDate.refeDate),
                                timeClosingDate: ko.observable(self.processingYear() + '/' + index + '/' + data.advancedSetting.closeDate.refeDate),
                                incomeTaxReferenceDate: ko.observable(self.processingYear() + '/' + data.advancedSetting.incomTaxBaseYear.baseMonth + '/' + data.advancedSetting.incomTaxBaseYear.refeDate),
                                accountingClosureDate: ko.observable(self.processingYear() + '/' + index + '/' + data.basicSetting.accountingClosureDate.disposalDay)
                            }
                            array.push(objItem);
                        }
                        self.settingPaymentList(array);
                    }
                );
                self.reflectSystemReference();
            }
        }

        //screen E mode update
        reflectSystemReference() {
            var self = this;
            setShared("QMM005bParams", {
                processCateNo: self.processCateNo,
                processingYear: self.processingYear(),
                processInfomation: self.processInfomationDto
            });
            modal("/view/qmm/005/e/index.xhtml").onClosed(() => {
                self.eScreenReflect();
            });
        }

        eScreenReflect() {
            var self = this;
            var params = getShared("QMM005eParams");
            if (params.reflect) {
                for (var index = 0; index < self.settingPaymentList().length;) {
                    var settingPayment = self.settingPaymentList()[index];
                    ++index;
                    var basicSetting = params.valPayDateSet.basicSetting;
                    var advancedSetting = params.valPayDateSet.advancedSetting;
                    if (params.startMonth <= index) {
                        /* B4_10	支払年月日
                         ※1　支払日チェックが入っている場合のみ更新する
                         ※10 勤怠締め日チェックが入っている場合のみ更新する
                         */
                        if (params.checkbox.dailyPaymentDateCheck && params.checkbox.timeClosingDateCheck) {
                            // not exist
                            settingPayment.paymentDate(self.processingYear() + '/' + index + '/' + basicSetting.monthlyPaymentDate.datePayMent);
                        }
                        // B4_11    支払曜日
                        // ※1　支払日チェックが入っている場合のみ更新する
                        if (params.checkbox.dailyPaymentDateCheck) {

                        }
                        // B4_12	社員抽出基準日
                        // ※2　対象社員抽出基準日チェックが入っている場合のみ更新する
                        if (params.checkbox.empExtractionRefDateCheck) {
                            settingPayment.employeeExtractionReferenceDate(self.processingYear() + '/' + basicSetting.employeeExtractionReferenceDate.refeMonth + '/' + basicSetting.employeeExtractionReferenceDate.refeDate);
                        }

                        // B4_13	社会保険徴収月
                        // ※3　社会保険徴収月チェックが入っている場合のみ更新する
                        if (params.checkbox.socialInsuranceMonthCheck) {
                            settingPayment.socialInsuranceCollectionMonth(self.processingYear() + '/' + advancedSetting.salaryInsuColMon.monthCollected);
                        }
                        // B4_15	明細書印字年月
                        // ※4　要勤務日数チェックが入っている場合のみ更新する
                        if (params.checkbox.specPrintDateCheck) {
                            settingPayment.specificationPrintDate(self.processingYear() + '/' + advancedSetting.detailPrintingMon.printingMonth);
                        }
                        // B4_16	要勤務日数
                        // ※5　明細書印字年月チェックが入っている場合のみ更新する
                        if (params.checkbox.numWorkingDaysCheck) {
                            settingPayment.numberOfWorkingDays(basicSetting.workDay);
                        }
                        // B6_7		社会保険基準日
                        // ※6　社会保険基準日チェックが入っている場合のみ更新する
                        if (params.checkbox.socialInsuranceDateCheck) {
                            settingPayment.socialInsuranceStandardDate(advancedSetting.sociInsuStanDate.baseYear + '/' + advancedSetting.sociInsuStanDate.baseMonth + '/' + advancedSetting.sociInsuStanDate.baseYear);
                        }
                        // B6_8		雇用保険基準日
                        // ※7　雇用保険基準日チェックが入っている場合のみ更新する
                        if (params.checkbox.empInsuranceStandardDateCheck) {
                            settingPayment.employmentInsuranceStandardDate((self.processingYear() - 1) + '/' + advancedSetting.empInsurStanDate.baseMonth + advancedSetting.empInsurStanDate.refeDate);
                        }
                        // B6_9		勤怠締め日
                        // ※10 勤怠締め日チェックが入っている場合のみ更新する
                        if (params.checkbox.timeClosingDateCheck) {
                            settingPayment.timeClosingDate(self.processingYear() + '/' + advancedSetting.closeDate.baseMonth + '/' + advancedSetting.closeDate.refeDate);
                        }

                        /*B6_10	所得税基準日
                         ※8　所得税基準日チェックが入っている場合のみ更新する*/
                        if (params.checkbox.incomeTaxReferenceCheck) {
                            settingPayment.incomeTaxReferenceDate(self.processingYear() + '/' + advancedSetting.incomTaxBaseYear.baseMonth + '/' + advancedSetting.incomTaxBaseYear.refeDate);
                        }
                        /*B6_11	経理締め日
                         ※9　経理締め日チェックが入っている場合のみ更新する*/
                        if (params.checkbox.accountingClosureDateCheck) {
                            settingPayment.accountingClosureDate(self.processingYear() + '/' + basicSetting.accountingClosureDate.processMonth + basicSetting.accountingClosureDate.disposalDay);
                        }
                    }
                }
            }
        }

        registration() {
            let self = this;
            //    check input year valid
            let arrayItem = [];
            let index = 0;
            _.forEach(self.settingPaymentList(), function (setting) {
                index++;
                arrayItem.push({
                    setDaySupportCommand: {
                        processCateNo: self.processCateNo,
                        paymentDate: setting.paymentDate(),
                        processDate: self.processingYear() + self.fullMonth(index),
                        closeDateTime: setting.timeClosingDate(),
                        empInsurdStanDate: setting.employmentInsuranceStandardDate(),
                        closureDateAccounting: setting.accountingClosureDate(),
                        empExtraRefeDate: setting.employeeExtractionReferenceDate(),
                        socialInsurdStanDate: setting.socialInsuranceStandardDate(),
                        socialInsurdCollecMonth: setting.socialInsuranceCollectionMonth(),
                        incomeTaxDate: setting.incomeTaxReferenceDate(),
                        numberWorkDay: setting.numberOfWorkingDays()
                    },
                    specPrintYmSetCommand: {
                        printDate: setting.specificationPrintDate(),
                        processCateNo: self.processCateNo,
                        processDate: self.processingYear() + self.fullMonth(index),
                    }
                })
            });
            let commandData = {paymentDateSettingCommands: arrayItem}
            if (self.isNewMode()) {
                service.addDomainModel(commandData).done(function (data) {
                    self.transactionSuccess();
                }).fail(function (error) {
                    nts.uk.ui.dialog.alertError({messageId: error.messageId});
                })
            } else {
                service.updateDomainModel(commandData).done(function (data) {
                    self.transactionSuccess();
                }).fail(function (error) {
                    nts.uk.ui.dialog.alertError({messageId: error.messageId});
                })
            }
        }

        fullMonth(month) {
            return (month < 10 ? '0' + month : month).toString();
        }


        transactionSuccess() {
            let self = this;
            nts.uk.ui.dialog.info({messageId: "Msg_15"});
            self.startupScreen();
            self.isNewMode(false);
        }
    }
}

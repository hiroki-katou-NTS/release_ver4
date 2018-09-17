module nts.uk.pr.view.qmm005.d.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import model = nts.uk.pr.view.qmm005.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import close=nts.uk.ui.windows.close;


    export class ScreenModel {


        enableCloseDate: KnockoutObservable<boolean>;
        modeUpdate: number;

        valPayDateSet: model.ValPayDateSet;
        basicSetting: model.BasicSetting;
        advancedSetting: model.AdvancedSetting;
        processInfomation: model.ProcessInfomation;





        processCategoryNo: number;

        //basicSetting
        monthlyPaymentDate: model.MonthlyPaymentDate;
        employeeExtractionReferenceDate: model.EmployeeExtractionReferenceDate;
        accountingClosureDate: model.AccountingClosureDate;


        //advancedSetting
        detailPrintingMonth: model.DetailPrintingMonth;
        salaryInsuranceCollecMonth: model.SalaryInsuranceCollecMonth;
        employmentInsuranceStanDate: model.EmploymentInsuranceStanDate;
        incomeTaxBaseYear: model.IncomeTaxBaseYear;
        socialInsuranceStanDate: model.SocialInsuranceStanDate;
        closeDate: model.CloseDate;




        //D2_3
        DiscontinueThisProcessClassification: KnockoutObservableArray<model.Abolition>;
        DiscontinueThisProcessClassificationSelectedCode: KnockoutObservable<number>;

        //D4_19
        guaranteedBaseDate: KnockoutObservableArray<model.ItemModel>;
        guaranteedBaseDateSelectedCode: KnockoutObservable<number>;
        //D3_8
        refeDate: KnockoutObservableArray<model.ItemModel>;
        refeDateSelectedCode: KnockoutObservable<number>;

        //D3_11
        refeMonth: KnockoutObservableArray<model.ItemModel>;
        refeMonthSelectedCode: KnockoutObservable<number>;
        //D4_30
        timeCloseDate: KnockoutObservableArray<model.ItemModel>;
        timeCloseDateSelectedCode: KnockoutObservable<number>;

        //D3_20
        disposalDay: KnockoutObservableArray<model.ItemModel>;
        disposalDaySelectedCode: KnockoutObservable<number>;


        processName: KnockoutObservable<string>;
        //D3_3
        datePayment: KnockoutObservableArray<model.ItemModel>;
        datePaymentSelectedCode: KnockoutObservable<number>;

        isEnable: KnockoutObservable<boolean>;


        numberOfWorkingDays: KnockoutObservable<number>;
        selectedId: KnockoutObservable<number>;


        enableChecckBox: KnockoutObservable<boolean>;


        //d3_17
        processingMonth: KnockoutObservableArray<model.ItemModel>;
        processingMonthSelectedCode: KnockoutObservable<number>;

        labelRequired: KnockoutObservable<boolean>;

        //D4_3
        printingMonth: KnockoutObservableArray<model.ItemModel>;
        printingMonthSelectedCode: KnockoutObservable<number>;

        //D4_9
        monthsCollected: KnockoutObservableArray<model.ItemModel>;
        monthsCollectedSelectedCode: KnockoutObservable<number>;

        //D4_15
        guaranteedBaseYear: KnockoutObservableArray<model.ItemModel>;
        guaranteedBaseYearSelectedCode: KnockoutObservable<number>;

        //D4_17
        guaranteedBaseMonth: KnockoutObservableArray<model.ItemModel>;
        guaranteedBaseMonthSelectedCode: KnockoutObservable<number>;


        //D4_23
        employmentInsuranceStandardMonth: KnockoutObservableArray<model.ItemModel>;
        employmentInsuranceStandardMonthSelectedCode: KnockoutObservable<number>;


        //D4_25
        employmentInsuranceStandardDate: KnockoutObservableArray<model.ItemModel>;
        employmentInsuranceStandardDateSelectedCode: KnockoutObservable<number>;

        //D4_34
        timeBaseYear: KnockoutObservableArray<model.ItemModel>;
        timeBaseYearSelectedCode: KnockoutObservable<number>;

        //D4_36
        timeClosingStandardMonth: KnockoutObservableArray<model.ItemModel>;
        timeClosingStandardMonthSelectedCode: KnockoutObservable<number>;

        //D4_38
        timeReferenceStandardDay: KnockoutObservableArray<model.ItemModel>;
        timeReferenceStandardDaySelectedCode: KnockoutObservable<number>;

        //D4_42
        yearSelectClassification: KnockoutObservableArray<model.ItemModel>;
        yearSelectClassificationSelectedCode: KnockoutObservable<number>;

        //D4_44
        incomeTaxBaseMonth: KnockoutObservableArray<model.ItemModel>;
        incomeTaxBaseMonthSelectedCode: KnockoutObservable<number>;

        //D4_46
        incomeTaxBaseDate: KnockoutObservableArray<model.ItemModel>;
        incomeTaxBaseDateSelectedCode: KnockoutObservable<number>;


        constructor() {

            var self = this;


            let init=getShared('QMM005_output_D');
            self.modeUpdate=init.modeUpdate;
            self.processCategoryNo=init.processCateNo;






            self.numberOfWorkingDays = ko.observable(20);
            self.enableCloseDate = ko.observable(false);
            //d2_2
            self.processName = ko.observable('');

            self.DiscontinueThisProcessClassification = ko.observableArray([
                new model.ItemModel(model.Abolition.Abolition, 'Abolition'),
                new model.ItemModel(model.Abolition.Not_Abolition, 'Not_Abolition')
            ]);
            self.DiscontinueThisProcessClassificationSelectedCode = ko.observable(0);

            //D4_42
            self.yearSelectClassification = ko.observableArray([
                new model.ItemModel(model.YearSelectClassification.LAST_YEAR, 'LAST_YEAR'),
                new model.ItemModel(model.YearSelectClassification.THIS_YEAR, 'THIS_YEAR'),
                new model.ItemModel(model.YearSelectClassification.AFTER_YEAR, 'AFTER_YEAR'),
                new model.ItemModel(model.YearSelectClassification.LEAP_YEAR, 'LEAP_YEAR')
            ]);
            self.yearSelectClassificationSelectedCode = ko.observable(0);

            //D4_44
            self.incomeTaxBaseMonth = ko.observableArray([
                new model.ItemModel(model.MonthSelectionSegment.JANUARY, 'JANUARY'),
                new model.ItemModel(model.MonthSelectionSegment.FEBRUARY, 'FEBRUARY'),
                new model.ItemModel(model.MonthSelectionSegment.MARCH, 'MARCH'),
                new model.ItemModel(model.MonthSelectionSegment.APRIL, 'APRIL'),
                new model.ItemModel(model.MonthSelectionSegment.MAY, 'MAY'),
                new model.ItemModel(model.MonthSelectionSegment.JUNE, 'JUNE'),
                new model.ItemModel(model.MonthSelectionSegment.JULY, 'JULY'),
                new model.ItemModel(model.MonthSelectionSegment.AUGUST, 'AUGUST'),
                new model.ItemModel(model.MonthSelectionSegment.SEPTEMBER, 'SEPTEMBER'),
                new model.ItemModel(model.MonthSelectionSegment.OCTOBER, 'OCTOBER'),
                new model.ItemModel(model.MonthSelectionSegment.NOVEMBER, 'NOVEMBER'),
                new model.ItemModel(model.MonthSelectionSegment.DECEMBER, 'DECEMBER')
            ]);
            self.incomeTaxBaseMonthSelectedCode = ko.observable(1);

            //D4_46
            self.incomeTaxBaseDate = ko.observableArray([]);
            self.incomeTaxBaseDateSelectedCode = ko.observable(0);


            //D4_34
            self.timeBaseYear = ko.observableArray([
                new model.ItemModel(model.YearSelectClassification.LAST_YEAR, 'LAST_YEAR'),
                new model.ItemModel(model.YearSelectClassification.THIS_YEAR, 'THIS_YEAR'),
                new model.ItemModel(model.YearSelectClassification.AFTER_YEAR, 'AFTER_YEAR'),
                new model.ItemModel(model.YearSelectClassification.LEAP_YEAR, 'LEAP_YEAR')
            ]);
            self.timeBaseYearSelectedCode = ko.observable(0);


            //D4_36
            self.timeClosingStandardMonth = ko.observableArray([
                new model.ItemModel(model.SocialInsuColleMonth.BEFORE_MONTH, 'BEFORE_MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.LAST_MONTH, 'LAST_MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.MONTH, 'MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.NEXT_MONTH, 'NEXT_MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.SECOND_FOLLOWING_MONTH, 'SECOND_FOLLOWING_MONTH'),
            ]);
            self.timeClosingStandardMonthSelectedCode = ko.observable(1);

            //D4_38
            self.timeReferenceStandardDay = ko.observableArray([]);
            self.timeReferenceStandardDaySelectedCode = ko.observable(1);

            //D4_19
            self.guaranteedBaseDate = ko.observableArray([]);
            self.guaranteedBaseDateSelectedCode = ko.observable(1);


            //D3_3
            self.disposalDay = ko.observableArray([]);
            self.disposalDaySelectedCode = ko.observable(1);

            //D4_23
            self.employmentInsuranceStandardDate = ko.observableArray([]);
            self.employmentInsuranceStandardDateSelectedCode = ko.observable(1);


            //D4_25
            self.employmentInsuranceStandardMonth = ko.observableArray([
                new model.ItemModel(model.MonthSelectionSegment.JANUARY, 'JANUARY'),
                new model.ItemModel(model.MonthSelectionSegment.FEBRUARY, 'FEBRUARY'),
                new model.ItemModel(model.MonthSelectionSegment.MARCH, 'MARCH'),
                new model.ItemModel(model.MonthSelectionSegment.APRIL, 'APRIL'),
                new model.ItemModel(model.MonthSelectionSegment.MAY, 'MAY'),
                new model.ItemModel(model.MonthSelectionSegment.JUNE, 'JUNE'),
                new model.ItemModel(model.MonthSelectionSegment.JULY, 'JULY'),
                new model.ItemModel(model.MonthSelectionSegment.AUGUST, 'AUGUST'),
                new model.ItemModel(model.MonthSelectionSegment.SEPTEMBER, 'SEPTEMBER'),
                new model.ItemModel(model.MonthSelectionSegment.OCTOBER, 'OCTOBER'),
                new model.ItemModel(model.MonthSelectionSegment.NOVEMBER, 'NOVEMBER'),
                new model.ItemModel(model.MonthSelectionSegment.DECEMBER, 'DECEMBER')
            ]);
            self.employmentInsuranceStandardMonthSelectedCode = ko.observable(1);

            //D3_20
            self.datePayment = ko.observableArray([]);
            self.datePaymentSelectedCode = ko.observable(0);


            //D3_17
            self.processingMonth = ko.observableArray([
                new model.ItemModel(model.PreviousMonthClassification.THIS_MONTH, 'THIS_MONTH'),
                new model.ItemModel(model.PreviousMonthClassification.LAST_MONTH, 'LAST_MONTH')
            ]);
            self.processingMonthSelectedCode = ko.observable(0);


            //D4_3
            self.printingMonth = ko.observableArray([
                new model.ItemModel(model.PreviousMonthClassification.THIS_MONTH, 'THIS_MONTH'),
                new model.ItemModel(model.PreviousMonthClassification.LAST_MONTH, 'LAST_MONTH')
            ]);
            self.printingMonthSelectedCode = ko.observable(0);

            //D3_20
            self.disposalDay = ko.observableArray([]);
            self.disposalDaySelectedCode = ko.observable(0);


            //D4_30
            self.timeCloseDate = ko.observableArray([
                new model.ItemModel(model.TimeCloseDateClassification.SAME_DATE, 'SAME_DATE'),
                new model.ItemModel(model.TimeCloseDateClassification.APART_FROM_DATE, 'APART_FROM_DATE')
            ]);
            self.timeCloseDateSelectedCode = ko.observable(0);
            self.timeCloseDateSelectedCode.subscribe(function () {
                if (self.timeCloseDateSelectedCode() == 0)
                    self.enableCloseDate(false);
                else
                    self.enableCloseDate(true);
            });

            //D4_9
            self.monthsCollected = ko.observableArray([
                new model.ItemModel(model.SocialInsuColleMonth.BEFORE_MONTH, 'BEFORE_MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.LAST_MONTH, 'LAST_MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.MONTH, 'MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.NEXT_MONTH, 'NEXT_MONTH'),
                new model.ItemModel(model.SocialInsuColleMonth.SECOND_FOLLOWING_MONTH, 'SECOND_FOLLOWING_MONTH'),

            ]);
            self.monthsCollectedSelectedCode = ko.observable(0);

            //D4_15
            self.guaranteedBaseYear = ko.observableArray([
                new model.ItemModel(model.YearSelectClassification.LAST_YEAR, 'LAST_YEAR'),
                new model.ItemModel(model.YearSelectClassification.THIS_YEAR, 'THIS_YEAR'),
                new model.ItemModel(model.YearSelectClassification.AFTER_YEAR, 'AFTER_YEAR'),
                new model.ItemModel(model.YearSelectClassification.LEAP_YEAR, 'LEAP_YEAR')


            ]);
            self.guaranteedBaseYearSelectedCode = ko.observable(1);


            //D3_11
            self.refeDate = ko.observableArray([]);
            self.refeDateSelectedCode = ko.observable(0);

            //D3_8

            self.refeMonth = ko.observableArray([
                new model.ItemModel(model.PreviousMonthClassification.THIS_MONTH, 'THIS_MONTH'),
                new model.ItemModel(model.PreviousMonthClassification.LAST_MONTH, 'LAST_MONTH')
            ]);
            self.refeMonthSelectedCode = ko.observable(0);

            //D4_17
            self.guaranteedBaseMonth = ko.observableArray([
                new model.ItemModel(model.InsuranceStanMonthClassSification.LAST_MONTH, 'LAST_MONTH'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.MONTH, 'MONTH'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.JANUARY, 'JANUARY'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.FEBRUARY, 'FEBRUARY'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.MARCH, 'MARCH'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.APRIL, 'APRIL'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.MAY, 'MAY'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.JUNE, 'JUNE'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.JULY, 'JULY'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.AUGUST, 'AUGUST'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.SEPTEMBER, 'SEPTEMBER'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.OCTOBER, 'OCTOBER'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.NOVEMBER, 'NOVEMBER'),
                new model.ItemModel(model.InsuranceStanMonthClassSification.DECEMBER, 'DECEMBER')

            ]);
            self.guaranteedBaseMonthSelectedCode = ko.observable(1);


            self.pushDaytoList(self.disposalDay, model.DateSelectClassification);
            self.pushDaytoList(self.datePayment, model.DateSelectClassification);
            self.pushDaytoList(self.refeDate, model.DateSelectClassification);
            self.pushDaytoList(self.employmentInsuranceStandardDate, model.DateSelectClassification);
            self.pushDaytoList(self.timeReferenceStandardDay, model.DateSelectClassification);
            self.pushDaytoList(self.guaranteedBaseDate, model.DateSelectClassification);
            self.pushDaytoList(self.incomeTaxBaseDate, model.DateSelectClassification);

            self.selectedId = ko.observable(0);
            self.labelRequired = ko.observable(true);
            self.isEnable = ko.observable(true);
            self.enableChecckBox = ko.observable(true);



        }

        saveCharacterSetting(): void {
            let self = this;

            if(self.modeUpdate==0){
                service.deleteDisplayRegister(self.processCategoryNo).done(function () {
                    self.saveToDB();
                })
            }
            if (self.modeUpdate==1){
                self.saveToDB();
            }





        }

        cancelCharacterSetting(): void {
            nts.uk.ui.windows.close();
        }


        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            dfd.resolve();
            if(self.modeUpdate==0){
                service.findDisplayRegister(this.processCategoryNo).done(function (data) {
                    console.log(data);
                    self.valPayDateSet=new model.ValPayDateSet(data);
                    console.log(self.valPayDateSet)

                    //basic
                    self.datePaymentSelectedCode(self.valPayDateSet.basicSetting.monthlyPaymentDate.datePayMent);
                    self.refeDateSelectedCode(self.valPayDateSet.basicSetting.employeeExtractionReferenceDate.refeDate);
                    self.refeMonthSelectedCode(self.valPayDateSet.basicSetting.employeeExtractionReferenceDate.refeMonth);
                    self.disposalDaySelectedCode(self.valPayDateSet.basicSetting.accountingClosureDate.disposalDay);
                    self.processingMonthSelectedCode(self.valPayDateSet.basicSetting.accountingClosureDate.processMonth);
                    self.numberOfWorkingDays(self.valPayDateSet.basicSetting.workDay);
                    //advan

                        self.timeCloseDateSelectedCode(self.valPayDateSet.advancedSetting.closeDate.timeCloseDate);
                        self.timeBaseYearSelectedCode(self.valPayDateSet.advancedSetting.closeDate.baseYear);
                        self.timeReferenceStandardDaySelectedCode(self.valPayDateSet.advancedSetting.closeDate.refeDate);
                        self.timeClosingStandardMonthSelectedCode(self.valPayDateSet.advancedSetting.closeDate.baseMonth);

                        self.yearSelectClassificationSelectedCode(self.valPayDateSet.advancedSetting.incomTaxBaseYear.baseYear);
                        self.incomeTaxBaseMonthSelectedCode(self.valPayDateSet.advancedSetting.incomTaxBaseYear.baseMonth);
                        self.incomeTaxBaseDateSelectedCode(self.valPayDateSet.advancedSetting.incomTaxBaseYear.refeDate);
                        self.printingMonthSelectedCode(self.valPayDateSet.advancedSetting.detailPrintingMon.printingMonth);
                        self.monthsCollectedSelectedCode(self.valPayDateSet.advancedSetting.salaryInsuColMon.monthCollected);
                        self.guaranteedBaseYearSelectedCode(self.valPayDateSet.advancedSetting.sociInsuStanDate.baseYear);
                        self.guaranteedBaseMonthSelectedCode(self.valPayDateSet.advancedSetting.sociInsuStanDate.baseMonth);
                        self.guaranteedBaseDateSelectedCode(self.valPayDateSet.advancedSetting.sociInsuStanDate.refeDate);
                        self.employmentInsuranceStandardDateSelectedCode(self.valPayDateSet.advancedSetting.empInsurStanDate.refeDate);
                        self.employmentInsuranceStandardMonthSelectedCode(self.valPayDateSet.advancedSetting.empInsurStanDate.baseMonth);





                })

            }
            return dfd.promise();
        }


        //初期表示処理
        onInitScreen(): void {

        }




        saveToDB():void{
            let self = this;


            //basic
            self.monthlyPaymentDate = new model.MonthlyPaymentDate(self.datePaymentSelectedCode());
            self.employeeExtractionReferenceDate = new model.EmployeeExtractionReferenceDate(self.refeDateSelectedCode(), self.refeMonthSelectedCode());
            self.accountingClosureDate = new model.AccountingClosureDate(self.disposalDaySelectedCode(), self.processingMonthSelectedCode());
            //advan
            self.closeDate = new model.CloseDate(self.timeCloseDateSelectedCode(), self.timeBaseYearSelectedCode(), self.timeReferenceStandardDaySelectedCode(), self.timeClosingStandardMonthSelectedCode());
            self.incomeTaxBaseYear = new model.IncomeTaxBaseYear(self.yearSelectClassificationSelectedCode(), self.incomeTaxBaseMonthSelectedCode(), self.incomeTaxBaseDateSelectedCode());
            self.detailPrintingMonth = new model.DetailPrintingMonth(self.printingMonthSelectedCode());
            self.salaryInsuranceCollecMonth = new model.SalaryInsuranceCollecMonth(self.monthsCollectedSelectedCode());
            self.socialInsuranceStanDate = new model.SocialInsuranceStanDate(self.guaranteedBaseYearSelectedCode(), self.guaranteedBaseMonthSelectedCode(), self.guaranteedBaseDateSelectedCode());
            self.employmentInsuranceStanDate = new model.EmploymentInsuranceStanDate(self.employmentInsuranceStandardDateSelectedCode(), self.employmentInsuranceStandardMonthSelectedCode());


            let advancedSettingParam = {
                closeDate: self.closeDate,
                incomTaxBaseYear: self.incomeTaxBaseYear,
                detailPrintingMon: self.detailPrintingMonth,
                sociInsuStanDate: self.socialInsuranceStanDate,
                salaryInsuColMon: self.salaryInsuranceCollecMonth,
                empInsurStanDate: self.employmentInsuranceStanDate,

            }

            let bassicSettingParam = {
                monthlyPaymentDate: self.monthlyPaymentDate,
                employeeExtractionReferenceDate: self.employeeExtractionReferenceDate,
                accountingClosureDate: self.accountingClosureDate,
                workDay: self.numberOfWorkingDays()


            }

            self.basicSetting = new model.BasicSetting(bassicSettingParam);
            self.advancedSetting = new model.AdvancedSetting(advancedSettingParam);

            let ValPayDateSetParam = {

                processCateNo: self.processCategoryNo,
                basicSetting: self.basicSetting,
                advancedSetting: self.advancedSetting
            }

            self.valPayDateSet = new model.ValPayDateSet(ValPayDateSetParam);

            let processInfomationParam = {

                processCateNo: self.processCategoryNo,
                processDivisionName: self.processName(),
                deprecatCate: self.DiscontinueThisProcessClassificationSelectedCode()

            }

            self.processInfomation = new model.ProcessInfomation(processInfomationParam);
            console.log(self.processInfomation);
            console.log(self.valPayDateSet);
            service.registerprocessingsegment({
                processInformation: ko.toJS(self.processInfomation),
                valPayDateSet: ko.toJS(self.valPayDateSet)
            });
        }




        pushDaytoList(itemList: KnockoutObservableArray<model.ItemModel>, codeEnum: any): void {
            let items = itemList;
            let code = codeEnum;
            for(let i=1;i<32;i++){
                itemList.push(new model.ItemModel(i,i+'日'));
            }

//            let i = 1;
//            for (let data in codeEnum) {
//                if (isNaN(data)) continue;
//                itemList.push(new model.ItemModel(data, i + '日'));
//                i++;
//            }
        }


    }

}


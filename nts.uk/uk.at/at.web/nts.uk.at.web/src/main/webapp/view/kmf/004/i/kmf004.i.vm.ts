module nts.uk.at.view.kmf004.i.viewmodel {

    import getText = nts.uk.resource.getText;
    import alError = nts.uk.ui.dialog.alertError;
    import block = nts.uk.ui.block;
    import dialog = nts.uk.ui.dialog.info;
    import formatDate = nts.uk.time.formatDate;
    import parseYearMonthDate = nts.uk.time.parseYearMonthDate;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
    export class ScreenModel {

        frameItems: KnockoutObservableArray<FrameItem> = ko.observableArray([]);
        frameColumns: KnockoutObservableArray<any> =
        ko.observableArray([
            { headerText: getText('KMF004_6'), key: 'specialHdFrameNo', width: 150, hidden: true },
            //I2_3
            { headerText: getText('KMF004_6'), key: 'specialHdFrameName', width: 150 },
            //I2_4
            {
                headerText: getText('KMF004_73'), key: 'setting', width: 60,
                //I2_6
                template: '{{if ${setting} == "true"}}<div style=\' height:100%;\'> <i  data-bind=\'ntsIcon: { no: 78 }\'></i> </div>{{else }}  {{/if}}',

                columnCssClass:'nowrap'
            }
        ]);;
        currentFrameCd: KnockoutObservable<number> = ko.observable(null);
        tabs = ko.observableArray([
            //I6_2
            { id: 'tab-1', title: getText('KMF004_74'), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) }
//            ,
//            //I6_3
//            { id: 'tab-2', title: getText('KMF004_13'), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
        ]);
        selectedTab = ko.observable('tab-1');
        limits = ko.observableArray([
            //I7_3
            new BoxModel(1, getText('KMF004_76')),
            //I7_4
            new BoxModel(2, getText('KMF004_77'))
        ]);
        genderLst = ko.observableArray([
            //I10_3
            { code: 1, name: getText('KMF004_55') },
            //I10_4
            { code: 2, name: getText('KMF004_56') }
        ]);

        ageStandardlst = ko.observableArray([
            { code: 1, name: '当年' },
            { code: 2, name: '翌年' }
        ]);
        currentSHEvent: KnockoutObservable<SpecialHolidayEvent> = ko.observable(new SpecialHolidayEvent());
        constructor() {
            let self = this;
            self.currentFrameCd.subscribe((newCd) => {
                service.changeSpecialEvent(newCd).done((data) => {
                    $("input").ntsError('clear');
                    self.currentSHEvent().setData(new SpecialHolidayEvent(data));
                });
            });
        }

        /** get data to list **/
        getData(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();


            dfd.resolve();

            return dfd.promise();
        }


        /** get data when start dialog **/
        startPage(isReload?): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            block.invisible();
            service.getFrames().done((datas: Array<any>) => {
                self.frameItems(_.map(datas, (item) => { return new FrameItem(item) }));
                if (datas && datas.length) {
                    if (isReload) {
                        self.currentFrameCd.valueHasMutated();
                        
                    } else {
                        self.currentFrameCd(datas[0].specialHdFrameNo);
                    }
                    _.each($('td i'), icon => ko.bindingHandlers.ntsIcon.init(icon, () => ({ no: 78 })));
                }
            }).fail((error) => { alError({ messageId: error.messageId, messageParams: error.parameterIds }); })
                .always(() => {
                    block.clear();
                    dfd.resolve();
                });


            return dfd.promise();
        }

        /** update or insert data when click button register **/
        saveData() {
            let self = this,
                cmd = ko.toJS(self.currentSHEvent);
            $(".nts-input").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            block.invisible();
            service.save(cmd).done(() => {
                dialog({ messageId: 'Msg_15' }).then(function() {
                    self.startPage(true);
                });
            }).fail((error) => { alError({ messageId: error.messageId, messageParams: error.parameterIds }); })
                .always(() => {
                    block.clear();
                });

        }
        deleteItem() {
            let self = this;
            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(function() {
                block.invisible();
                service.remove(self.currentFrameCd())
                    .done(() => {
                        dialog({ messageId: "Msg_16" }).then(function() {
                            self.startPage(false);
                        });;
                    })
                    .fail((error) => { alError({ messageId: error.messageId, messageParams: error.parameterIds }); })
                    .always(() => {
                        block.clear();
                    });
            });
        }

        openGDialog() {
            let self = this;
            setShared("KMF004Data", { makeInvitation: self.makeInvitation(), sHENo: __viewContext['viewModel'].currentFrameCd() });
            modal("/view/kmf/004/g/index.xhtml").onClosed(() => {
            });
        }
        openHDialog() {
            modal("/view/kmf/004/h/index.xhtml").onClosed(() => {
            });
        }


    }

    export class FrameItem {
        companyId: string = '';
        specialHdFrameNo: number = 0;
        specialHdFrameName: string = "";
        deprecateSpecialHd: number = 0;
        setting: boolean = false;
        constructor(data?) {
            if (data) {
                this.companyId = data.companyId;
                this.specialHdFrameNo = data.specialHdFrameNo;
                this.specialHdFrameName = data.specialHdFrameName;
                this.deprecateSpecialHd = data.deprecateSpecialHd;
                this.setting = data.setting;
            }

        }
    }
    export class SpecialHolidayEvent {
        companyId: KnockoutObservable<string> = ko.observable("");
        specialHolidayEventNo: KnockoutObservable<number> = ko.observable();
        maxNumberDay: KnockoutObservable<number> = ko.observable(1);
        fixedDayGrant: KnockoutObservable<number> = ko.observable("");
        makeInvitation: KnockoutObservable<boolean> = ko.observable(false);
        includeHolidays: KnockoutObservable<boolean> = ko.observable(false);
        ageLimit: KnockoutObservable<boolean> = ko.observable(false);
        genderRestrict: KnockoutObservable<boolean> = ko.observable(false);
        restrictEmployment: KnockoutObservable<boolean> = ko.observable(false);
        restrictClassification: KnockoutObservable<boolean> = ko.observable(false);
        gender: KnockoutObservable<number> = ko.observable(1);
        ageRange: KnockoutObservable<AgeRange> = ko.observable(new AgeRange());
        ageStandard: KnockoutObservable<number> = ko.observable(1);
        ageStandardBaseDate: KnockoutObservable<string> = ko.observable("");
        memo: KnockoutObservable<string> = ko.observable("");
        clsList: Array<ClassificationList> = ko.observableArray([]);
        empList: Array<EmploymentList> = ko.observableArray([]);
        employmentTxt: KnockoutObservable<string> = ko.observable("");
        classificationTxt: KnockoutObservable<string> = ko.observable("");
        createNew: KnockoutObservable<boolean> = ko.observable(true);
        dayGrantUse: KnockoutObservable<boolean> = ko.observable(true);
        constructor(data?) {
            let self = this;
            self.empList.subscribe((newData) => {
                if (!_.size(newData)) {
                    self.employmentTxt("");
                    return;
                }
                let codes = _.map(newData, item => { return item.employmentCd });
                service.findEmpByCodes(codes).done((datas) => {
                    self.employmentTxt(_.map(datas, item => { return item }).join(' + '));
                });
            });
            self.clsList.subscribe((newData) => {
                if (!_.size(newData)) {
                    self.classificationTxt("");
                    return;
                }
                let codes = _.map(newData, item => { return item.classificationCd });
                service.findClsByCodes(codes).done((datas) => {
                    self.classificationTxt(_.map(datas, item => { return item }).join(' + '));
                });
            });
            
            self.ageLimit.subscribe((value) => {
                if (!value) {
                    $("#lowerLimit").ntsError('clear');
                    $("#higherLimit").ntsError('clear');
                }
            });
            
            self.maxNumberDay.subscribe((value) => {
                if (value == 1) {
                    self.dayGrantUse(true);
                } else {
                    $("#max_date").ntsError('clear');
                    self.dayGrantUse(false);
                }
            });

            self.createNew(data ? false : true);
            if (__viewContext['viewModel']) {
                self.specialHolidayEventNo(__viewContext['viewModel'].currentFrameCd());
            }
            if (data) {
                self.newData(data);
            }
        }
        
        
        newData(data) {
            let self = this;
            self.companyId(data.companyId);
            self.maxNumberDay(data.maxNumberDay);
            self.fixedDayGrant(data.fixedDayGrant == null ? "" : data.fixedDayGrant);
            self.makeInvitation(data.makeInvitation == 0 ? false : true);
            self.includeHolidays(data.includeHolidays == 0 ? false : true);
            self.ageLimit(data.ageLimit == 0 ? false : true);
            self.genderRestrict(data.genderRestrict == 0 ? false : true);
            self.restrictEmployment(data.restrictEmployment == 0 ? false : true);
            self.restrictClassification(data.restrictClassification == 0 ? false : true);
            self.gender(data.gender);
            self.ageRange().setData(data.ageRange);
            self.ageStandard(data.ageStandard);
            self.ageStandardBaseDate(data.ageStandardBaseDate);
            self.memo(data.memo);
            self.clsList(_.map(data.clsList, item => { return new ClassificationList(item) }));
            self.empList(_.map(data.empList, item => { return new EmploymentList(item) }));
        }
        setData(data) {
            let self = this;
            data = ko.toJS(data);
            self.newData(data);
            self.specialHolidayEventNo(data.specialHolidayEventNo);
            self.createNew(data.createNew);
        }




        openCDL002() {
            let self = this,
                selectedCodes = _.map(self.empList(), item => { return item.employmentCd });

            setShared("CDL002Params", { isMultiple: true, showNoSelection: false, selectedCodes: selectedCodes });

            modal("com", "/view/cdl/002/a/index.xhtml").onClosed(() => {
                let SHENo = self.specialHolidayEventNo,
                    companyId = self.companyId,
                    data = getShared('CDL002Output');
                if (data) {
                    self.empList(_.map(data, item => { return new EmploymentList({ companyId: companyId, specialHolidayEventNo: SHENo, employmentCd: item }) }));
                }
            });
        }
        openCDL003() {
            let self = this,
                selectedCodes = _.map(self.clsList(), item => { return item.employmentCd });

            setShared("inputCDL003", { isMultiple: true, showNoSelection: false, selectedCodes: selectedCodes });

            modal("com", "/view/cdl/003/a/index.xhtml").onClosed(() => {
                let SHENo = self.specialHolidayEventNo,
                    companyId = self.companyId,
                    data = getShared('outputCDL003');
                if (data) {
                    self.clsList(_.map(data, item => { return new ClassificationList({ companyId: companyId, specialHolidayEventNo: SHENo, classificationCd: item }) }));
                }
            });

        }



    }
    export class AgeRange {
        ageLowerLimit: KnockoutObservable<number> = ko.observable();
        ageHigherLimit: KnockoutObservable<number> = ko.observable();
        constructor(data?) {
            if (data) {
                this.ageLowerLimit(data.ageLowerLimit);
                this.ageHigherLimit(data.ageHigherLimit);
            }
        }
        setData(data) {
            if (data) {
                this.ageLowerLimit(data.ageLowerLimit);
                this.ageHigherLimit(data.ageHigherLimit);
            }
        }
    }
    export class ClassificationList {
        companyId: string;
        specialHolidayEventNo: number;
        classificationCd: string;
        constructor(data?) {
            if (data) {
                this.companyId = data.companyId;
                this.specialHolidayEventNo = data.specialHolidayEventNo;
                this.classificationCd = data.classificationCd;
            }
        }

    }

    export class EmploymentList {
        companyId: string;
        specialHolidayEventNo: number;
        employmentCd: string;
        constructor(data?) {
            if (data) {
                this.companyId = data.companyId;
                this.specialHolidayEventNo = data.specialHolidayEventNo;
                this.employmentCd = data.employmentCd;
            }
        }
    }

    class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
}






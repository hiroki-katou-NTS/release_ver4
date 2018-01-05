module nts.uk.at.view.kmf004.a.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<model.SpecialHolidayDto>;
        sphdList: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;

        currentItem: KnockoutObservable<model.SpecialHolidayDto>
        tabs: KnockoutObservableArray<any>;
        selectedTab: KnockoutObservable<string>;
        selectedValue: KnockoutObservable<any>;
        enable: KnockoutObservable<boolean>;

        //Tab Regular
        option1: KnockoutObservable<any>;
        option2: KnockoutObservable<any>;

        //
        date: KnockoutObservable<string>;
        roundingSplitAcquisition: KnockoutObservableArray<any>;
        roundingCarryForward: KnockoutObservableArray<any>;
        roundingGenderAtr: KnockoutObservableArray<any>;
        roundingMakeInvitation: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;

        //Input Screen
        roundingPeriodic: KnockoutObservableArray<any>;
        workTypeList: KnockoutObservableArray<any>;
        employmentList: KnockoutObservableArray<any>;
        classificationList: KnockoutObservableArray<any>;
        List: KnockoutObservableArray<any>;
        workTypeNames: KnockoutObservable<string>;
        employmentNames: KnockoutObservable<string>;
        classficationNames: KnockoutObservable<string>;
        isEnableCode: KnockoutObservable<boolean>;
        inp_grantMethod: KnockoutObservable<number>;

        //Combobox
        itemAgeBaseYearAtr: KnockoutObservableArray<any>;
        visibleGrantSingle: KnockoutObservable<boolean>;
        visibleGrant: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.visibleGrantSingle = ko.observable(false);
            self.visibleGrant = ko.observable(true);
            self.inp_grantMethod = ko.observable(1);

            self.items = ko.observableArray([]);
            self.sphdList = ko.observableArray([]);
            self.enable = ko.observable(true);

            self.date = ko.observable('');
            self.selectedCode = ko.observable('0002')
            self.isEnable = ko.observable(true);
            self.isEnableCode = ko.observable(false);
            self.isEditable = ko.observable(true);

            self.currentItem = ko.observable(new model.SpecialHolidayDto({}));
            self.workTypeList = ko.observableArray([]);
            self.employmentList = ko.observableArray([]);
            self.classificationList = ko.observableArray([]);
            self.workTypeNames = ko.observable("");
            self.employmentNames = ko.observable("");
            self.classficationNames = ko.observable("");

            self.itemAgeBaseYearAtr = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('Enum_AgeBaseYearAtr_NEXT_MONTH') },
                { code: 1, name: nts.uk.resource.getText('Enum_AgeBaseYearAtr_THIS_MONTH') }
            ]);
            self.roundingPeriodic = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_15') },
                { code: 1, name: nts.uk.resource.getText('KMF004_14') },
            ]);

            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMF004_7'), key: 'specialHolidayCode', width: 100 },
                { headerText: nts.uk.resource.getText('KMF004_8'), key: 'specialHolidayName', width: 150 }
            ]);

            self.roundingSplitAcquisition = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_38') },
                { code: 1, name: nts.uk.resource.getText('KMF004_39') }
            ]);

            self.roundingCarryForward = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_51') },
                { code: 1, name: nts.uk.resource.getText('KMF004_52') }
            ]);

            self.roundingGenderAtr = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_61') },
                { code: 1, name: nts.uk.resource.getText('KMF004_62') }
            ]);

            self.roundingMakeInvitation = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_61') },
                { code: 1, name: nts.uk.resource.getText('KMF004_62') }
            ]);

            //Tab1
            self.selectedValue = ko.observable(false);
            self.option1 = ko.observable({ value: 0, text: nts.uk.resource.getText('KMF004_23') });

            //Tab2
            self.option2 = ko.observable({ value: 1, text: nts.uk.resource.getText('KMF004_24') });

            self.currentCode = ko.observable();
            self.currentCode.subscribe(function(codeChanged) {
                if (codeChanged !== null && codeChanged !== undefined) {
                    self.changedCode(codeChanged);
                    self.isEnableCode(false);
                    $("#code-text2").focus();
                    nts.uk.ui.errors.clearAll();
                } else {
                    $("#code-text").focus();
                }
            });

            self.inp_grantMethod.subscribe(function(value) {
                if (value == 0) {
                    self.visibleGrantSingle(true);
                    self.visibleGrant(false);
                    self.selectedTab('tab-5');
                    nts.uk.ui.errors.clearAll();
                    $("#code-text").focus();
                    self.hiddenRegular();
                    self.hiddenPeriodic();
                } else {
                    self.visibleGrantSingle(false);
                    self.visibleGrant(true);
                    self.selectedTab('tab-1');
                    nts.uk.ui.errors.clearAll();
                    $("#code-text").focus();
                    self.hiddenSingle();
                    
                }
            })

            self.tabs = ko.observableArray([
                { id: 'tab-1', title: nts.uk.resource.getText('KMF004_17'), content: '.tab-content-1', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-2', title: nts.uk.resource.getText('KMF004_18'), content: '.tab-content-2', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-3', title: nts.uk.resource.getText('KMF004_19'), content: '.tab-content-3', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-4', title: nts.uk.resource.getText('KMF004_20'), content: '.tab-content-4', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-5', title: nts.uk.resource.getText('KMF004_21'), content: '.tab-content-5', enable: ko.observable(true), visible: self.visibleGrant }
            ]);
            self.selectedTab = ko.observable('tab-5');
            self.selectedTab.subscribe(function(value) {
                nts.uk.ui.errors.clearAll();
            });
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            $.when(self.getAllSpecialHoliday(), self.getWorkTypeList(), self.getEmploymentList(), self.getClassList()).done(function() {
                if (self.items().length > 0) {
                    self.currentCode(self.items()[0].specialHolidayCode());
                    $("#code-text2").focus();
                } else {
                    self.initSpecialHoliday();
                }
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            self.hiddenSingle();
            return dfd.promise();
        }

        getAllSpecialHoliday(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.items.removeAll();
            //self.sphdList.removeAll();
            service.findAllSpecialHoliday().done(function(specialHoliday_arr: Array<model.ISpecialHolidayDto>) {
                self.sphdList(specialHoliday_arr);
                _.forEach(specialHoliday_arr, function(specialHolidayRes: model.ISpecialHolidayDto) {
                    var specialHoliday: model.ISpecialHolidayDto = {
                        specialHolidayCode: specialHolidayRes.specialHolidayCode,
                        specialHolidayName: specialHolidayRes.specialHolidayName,
                        grantMethod: specialHolidayRes.grantMethod,
                        memo: specialHolidayRes.memo,
                        workTypeList: specialHolidayRes.workTypeList,
                        grantRegular: specialHolidayRes.grantRegular,
                        grantPeriodic: specialHolidayRes.grantPeriodic,
                        sphdLimit: specialHolidayRes.sphdLimit,
                        subCondition: self.toSubConditionDto(specialHolidayRes.subCondition),
                        grantSingle: specialHolidayRes.grantSingle
                    };
                    self.items.push(new model.SpecialHolidayDto(specialHoliday));

                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            return dfd.promise();
        }

        toGrantRegularDto(grantRegular: model.IGrantRegularDto): model.GrantRegularDto {
            if (!grantRegular) {
                return new model.GrantRegularDto({});
            }
            return new model.GrantRegularDto(grantRegular);
        }

        toGrantPeriodicDto(grantPeriodic: model.IGrantPeriodic): model.GrantPeriodicDto {
            if (!grantPeriodic) {
                return new model.GrantPeriodicDto({});
            }
            return new model.GrantPeriodicDto(grantPeriodic);
        }

        toSphdLimitDto(sphdLimit: model.ISphdLimitDto): model.SphdLimitDto {
            if (!sphdLimit) {
                return new model.SphdLimitDto({});
            }
            return new model.SphdLimitDto(sphdLimit);
        }

        toSubConditionDto(subCondition: model.ISubConditionDto): model.ISubConditionDto {
            if (!subCondition) {
                return subCondition;
            }

            subCondition.useGender = Number(subCondition.useGender) == 1;
            subCondition.useEmployee = Number(subCondition.useEmployee) == 1;
            subCondition.useCls = Number(subCondition.useCls) == 1;
            subCondition.useAge = Number(subCondition.useAge) == 1;
            subCondition.employmentList = subCondition.employmentList;
            subCondition.classificationList = subCondition.classificationList;

            return subCondition;
        }

        toGrantSingleDto(grantSingle: model.IGrantSingleDto): model.GrantSingleDto {
            if (!grantSingle) {
                return new model.GrantSingleDto({});
            }
            return new model.GrantSingleDto(grantSingle);
        }

        /**
         * Trigger by grant method
         */
        triggerByGrantMethod(): void {
            var self = this;
            if (model.GrantRegularMethod.GrantStartDateSpecify == self.currentItem().grantRegular().grantRegularMethod()) {
                $("#grant-start-date").trigger("validate");
                $("#months").trigger("validate");
                $("#years").trigger("validate");
            }
            if (model.GrantPeriodicMethod.Allow == self.currentItem().grantPeriodic().grantPeriodicMethod()) {
                $("#grant-day").trigger("validate");
            }
            if (model.SpecialVacationMethod.AvailableGrantDateDesignate == self.currentItem().sphdLimit().specialVacationMethod()) {
                $("#specialVacationMonths").trigger("validate");
                $("#specialVacationYears").trigger("validate");
            }
            var useAge = self.currentItem().subCondition().useAge() ? 1 : 0;
            if (model.UseAtr.Use == useAge) {
                $("#limitAgeFrom").trigger("validate");
                $("#limitAgeTo").trigger("validate");
            }
        }

        addSpecialHoliday(): JQueryPromise<any> {
            var self = this;
            $("#code-text").trigger("validate");
            $("#code-text2").trigger("validate");
            $('#text-target-item').trigger("validate");
            $('#grant-start-date').trigger("validate");
            var specialHoliday = ko.toJS(self.currentItem());
            if (self.inp_grantMethod() == 1) {
                $("#fixNumberDays").trigger("validate");
                specialHoliday.grantRegular = null;
                specialHoliday.grantPeriodic = null;
                specialHoliday.sphdLimit = null;
                specialHoliday.subCondition = null;
            } else {
                self.triggerByGrantMethod();

                var useGender = specialHoliday.subCondition.useGender;
                var useEmployee = specialHoliday.subCondition.useEmployee;
                var useCls = specialHoliday.subCondition.useCls;
                var useAge = specialHoliday.subCondition.useAge;

                specialHoliday.subCondition.useGender = useGender ? 1 : 0;
                specialHoliday.subCondition.useEmployee = useEmployee ? 1 : 0;
                specialHoliday.subCondition.useCls = useCls ? 1 : 0;
                specialHoliday.subCondition.useAge = useAge ? 1 : 0;

                specialHoliday.grantSingle = null;
                if (specialHoliday.grantRegular.grantStartDate) {
                    specialHoliday.grantRegular.grantStartDate = new Date(specialHoliday.grantRegular.grantStartDate);
                } else {
                    specialHoliday.grantRegular.grantStartDate = null;
                }
            }
            specialHoliday.grantMethod = self.inp_grantMethod();

            // check error form
            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            nts.uk.ui.block.invisible();

            if (self.isEnableCode()) {
                var emptyObjectRegular: model.IGrantRegularDto = {};
                var emptyObjectPeriodic: model.IGrantPeriodic = {};
                var emptyObjectSphdLimit: model.ISphdLimitDto = {};
                var emptyObjectSubCondition: model.ISubConditionDto = {};
                var emptyObjectGrantSingle: model.IGrantSingleDto = {};

                specialHoliday["grantMethod"] = self.inp_grantMethod();

                service.addSpecialHoliday(specialHoliday).done(function(errors) {
                    if (errors && errors.length > 0) {
                        self.addListError(errors);
                    } else {
                        if (self.currentCode) {
                            nts.uk.ui.dialog.info(nts.uk.resource.getMessage("Msg_15"));
                            self.getAllSpecialHoliday().done(function() {
                                self.currentCode(self.currentItem().specialHolidayCode());
                                self.isEnableCode(false);
                            });
                        }
                    }
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
                nts.uk.ui.block.clear();
            }
            else {
                service.updateSpecialHoliday(specialHoliday).done(function(errors) {
                    if (errors && errors.length > 0) {
                        self.addListError(errors);
                    } else {
                        nts.uk.ui.dialog.info(nts.uk.resource.getMessage("Msg_15"));
                        self.getAllSpecialHoliday().done(function() {
                            self.currentCode(self.currentItem().specialHolidayCode());
                        });
                    }
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
            }
        }

        getWorkTypeList() {
            var self = this;
            var dfd = $.Deferred();
            service.findWorkType().done(function(res) {
                _.forEach(res, function(item) {
                    self.workTypeList.push({
                        workTypeCode: item.workTypeCode,
                        name: item.name,
                        memo: item.memo
                    });
                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            return dfd.promise();
        }

        getEmploymentList() {
            var self = this;
            var dfd = $.Deferred();
            if (self.employmentList().length > 0) {
                dfd.resolve();
                return dfd.promise();
            }
            service.findEmployment().done(function(res) {
                _.forEach(res, function(item) {
                    self.employmentList.push({
                        employmentCode: item.code,
                        name: item.name,
                        memo: item.memo
                    });
                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            return dfd.promise();
        }

        getClassList() {
            var self = this;
            var dfd = $.Deferred();
            if (self.classificationList().length > 0) {
                dfd.resolve();
                return dfd.promise();
            }
            service.findClass().done(function(res) {
                _.forEach(res, function(item) {
                    self.classificationList.push({
                        classficationCode: item.code,
                        name: item.name,
                        memo: item.memo
                    });
                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            return dfd.promise();
        }
        deleteSpecialHoliday() {
            let self = this;
            var index_of_itemDelete = _.findIndex(self.items(), function(item) {
                return item.specialHolidayCode() == self.currentCode();
            });
            nts.uk.ui.block.invisible();
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                var specialholiday = {
                    specialHolidayCode: self.currentItem().specialHolidayCode()
                };
                service.deleteSpecialHoliday(specialholiday).done(function() {
                    $.when(self.getAllSpecialHoliday()).done(function() {
                        var holidayId: number = null;
                        if (self.items().length == 0) {
                            self.initSpecialHoliday();
                        } else if (self.items().length == 1) {
                            holidayId = self.items()[0].specialHolidayCode();
                        } else if (index_of_itemDelete == self.items().length) {
                            holidayId = self.items()[index_of_itemDelete - 1].specialHolidayCode();
                        } else {
                            holidayId = self.items()[index_of_itemDelete].specialHolidayCode();
                        }
                        self.currentCode(holidayId);
                        nts.uk.ui.dialog.info(nts.uk.resource.getMessage("Msg_16"));
                    });
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
            }).ifNo(function() {
                nts.uk.ui.block.clear();
            });
        }

        initSpecialHoliday(): void {
            let self = this;
            var emptyObject: model.ISpecialHolidayDto = {};
            self.currentItem(new model.SpecialHolidayDto(emptyObject))
            self.currentCode("");
            self.inp_grantMethod(1);
            self.visibleGrantSingle(false);
            self.visibleGrant(true);
            self.workTypeNames("");
            self.isEnableCode(true);
            $("#code-text").focus();
            nts.uk.ui.errors.clearAll();
            self.hiddenSingle();
            self.hiddenRegular();
            self.hiddenPeriodic();
        }

        focus(): void {
            var self = this;
            if (nts.uk.text.isNullOrEmpty(self.currentCode())) {
                $("#code-text").focus();
            } else {
                $("#code-text2").focus();
            }
        }

        changedCode(value) {
            var self = this;
            self.currentItem(self.findSpecialHoliday(value));
            if (self.currentItem() != null) {
                var names = self.getNames(self.workTypeList(), self.currentItem().workTypeList());
                var nameClass = self.getNamesClass(self.classificationList(), self.currentItem().subCondition().classificationList());
                var nameEmp = self.getNamesEmploy(self.employmentList(), self.currentItem().subCondition().employmentList());
                self.workTypeNames(names);
                self.classficationNames(nameClass);
                self.employmentNames(nameEmp);
                self.inp_grantMethod(self.currentItem().grantMethod());
            }
        }

        findSpecialHoliday(value: number): any {
            let self = this;
            var result = _.find(self.items(), function(obj: model.SpecialHolidayDto) {
                return obj.specialHolidayCode() == value;
            });
            if (result) {
                self.inp_grantMethod(result.grantMethod());
                return result;
            }
            return new model.SpecialHolidayDto({});
        }

        openKDL002Dialog() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            nts.uk.ui.block.invisible();
            var workTypeCodes = _.map(self.workTypeList(), function(item: IWorkTypeModal) { return item.workTypeCode });
            nts.uk.ui.windows.setShared('KDL002_Multiple', true);
            nts.uk.ui.windows.setShared('KDL002_AllItemObj', workTypeCodes);
            nts.uk.ui.windows.setShared('KDL002_SelectedItemId', self.currentItem().workTypeList());

            nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: '' }).onClosed(function(): any {
                nts.uk.ui.block.clear();
                var data = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                var name = [];
                _.forEach(data, function(item: IWorkTypeModal) {
                    name.push(item.name);
                });
                self.workTypeNames(name.join(" + "));

                var workTypeCodes = _.map(data, function(item: any) { return item.code; });
                self.currentItem().workTypeList(workTypeCodes);
            });
            nts.uk.ui.block.clear();
        }

        openBDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('KMF004B_SPHD_CD', self.currentItem().specialHolidayCode());
            nts.uk.ui.windows.sub.modal('/view/kmf/004/b/index.xhtml').onClosed(function(): any {
            });

        }

        openDDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('KMF004D_SPHD_CD', self.currentItem().specialHolidayCode());
            nts.uk.ui.windows.sub.modal('/view/kmf/004/d/index.xhtml').onClosed(function(): any {
            });

        }

        openGDialog() {
            let self = this;
            service.findAllGrantRelationship(self.currentItem().specialHolidayCode()).done((lstData: Array<viewmodel.GrantRelationship>) => {
                if (lstData.length == 0) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_375" });
                    return;
                } else {
                    nts.uk.ui.windows.setShared('KMF004G_SPHD_CD', self.currentItem().specialHolidayCode());
                    nts.uk.ui.windows.sub.modal('/view/kmf/004/g/index.xhtml').onClosed(function(): any {
                    });
                }
            })
        }

        openHDialog() {
            let self = this;
            nts.uk.ui.windows.sub.modal('/view/kmf/004/h/index.xhtml').onClosed(function(): any {
            });

        }

        /**
         * CDL002：雇用選択ダイアログ
         */
        openCDL002(): void {
            let self = this;
            nts.uk.ui.errors.clearAll();
            nts.uk.ui.windows.setShared('CDL002Params', {
                isMultiple: true,
                selectedCodes: self.currentItem().subCondition().employmentList(),
                showNoSelection: false,
            }, true);
            nts.uk.ui.windows.sub.modal("com", "/view/cdl/002/a/index.xhtml").onClosed(() => {
                var data = nts.uk.ui.windows.getShared('CDL002Output');
                var codes = [];
                var name = [];
                _.forEach(data, function(item: IEmploymentModal) {
                    var emp = _.find(self.employmentList(), function(itemEmp) { return itemEmp.employmentCode == item; });
                    name.push(emp.name);
                    codes.push(item);
                });
                self.employmentNames(name.join(" + "));
                self.currentItem().subCondition().employmentList(codes);
            });
        }

        /**
         * CDL003：分類選択ダイアログ
         */
        openCDL003(): void {
            nts.uk.ui.errors.clearAll();
            let self = this;
            var classficationCodes = _.map(self.classificationList(), function(item: IClassficationsModal) { return item.classficationCode });
            nts.uk.ui.windows.setShared('inputCDL003', {
                selectedCodes: self.currentItem().subCondition().classificationList(),
                showNoSelection: true,
                isMultiple: true
            }, true);
            nts.uk.ui.windows.sub.modal("com", "/view/cdl/003/a/index.xhtml").onClosed(() => {

                var data = nts.uk.ui.windows.getShared('outputCDL003');
                var codes = [];
                var name = [];
                _.forEach(data, function(item: IClassficationsModal) {
                    var cls = _.find(self.classificationList(), function(itemCls) { return itemCls.classficationCode == item; });
                    name.push(cls.name);
                    codes.push(item);
                });
                self.classficationNames(name.join(" + "));
                self.currentItem().subCondition().classificationList(codes);
            });
        }
        getNamesClass(data: Array<IClassficationsModal>, classCodesSelected: Array<string>) {
            var name = [];
            if (classCodesSelected && classCodesSelected.length > 0) {
                _.forEach(data, function(item: IClassficationsModal) {
                    if (_.includes(classCodesSelected, item.classficationCode)) {
                        name.push(item.name);
                    }
                });
            }
            return name.join(" + ");
        }

        getNamesEmploy(data: Array<IEmploymentModal>, employCodesSelected: Array<string>) {
            var name = [];
            if (employCodesSelected && employCodesSelected.length > 0) {
                _.forEach(data, function(item: IEmploymentModal) {
                    if (_.includes(employCodesSelected, item.employmentCode)) {
                        name.push(item.name);
                    }
                });
            }
            return name.join(" + ");
        }

        getNames(data: Array<IWorkTypeModal>, workTypeCodesSelected: Array<string>) {
            var name = [];
            if (workTypeCodesSelected && workTypeCodesSelected.length > 0) {
                _.forEach(data, function(item: IWorkTypeModal) {
                    if (_.includes(workTypeCodesSelected, item.workTypeCode)) {
                        name.push(item.name);
                    }
                });
            }
            return name.join(" + ");
        }

        /**
         * Set error
         */
        addListError(errorsRequest: Array<string>) {
            var self = this;
            var errors = [];
            _.forEach(errorsRequest, function(err) {
                errors.push({ message: nts.uk.resource.getMessage(err), messageId: err, suppliments: {} });
            });

            nts.uk.ui.dialog.bundledErrors({ errors: errors });
        }

        hiddenSingle() {
            var self = this;
            var grantDaySingle = self.currentItem().grantSingle().grantDaySingleType;
            if (grantDaySingle() == 0) {
                $('#hidden-lbl01').addClass('disabled');
            } else {
                $('#hidden-lbl01').removeClass('disabled');
            }

            grantDaySingle.subscribe(function(value) {
                if (value == 0) {
                    $('#hidden-lbl01').addClass('disabled');
                } else {
                    $('#hidden-lbl01').removeClass('disabled');
                }
            });
        }
        hiddenRegular() {
            var self = this;
            var grantRegular = self.currentItem().grantRegular().grantRegularMethod;
            if (grantRegular() == 0) {
                $('#hidden-lbl02').addClass('disabled');
            } else {
                $('#hidden-lbl02').removeClass('disabled');
            }

            grantRegular.subscribe(function(value) {
                if (value == 0) {
                    $('#hidden-lbl02').addClass('disabled');
                } else {
                    $('#hidden-lbl02').removeClass('disabled');
                }
            });
        }

        hiddenPeriodic() {
            var self = this;
            var grantPeriodic = self.currentItem().grantPeriodic().grantPeriodicMethod;
            if (grantPeriodic() == 0) {
                $('#hidden-lbl03').addClass('disabled');
            } else {
                $('#hidden-lbl03').removeClass('disabled');
            }

            grantPeriodic.subscribe(function(value) {
                if (value == 0) {
                    $('#hidden-lbl03').addClass('disabled');
                } else {
                    $('#hidden-lbl03').removeClass('disabled');
                }
            });
        }

    }

    class ItemModelSpecialHoliday {
        specialHolidayCode: string;
        specialHolidayName: string;
        constructor(specialHolidayCode: string, specialHolidayName: string) {
            this.specialHolidayCode = specialHolidayCode;
            this.specialHolidayName = specialHolidayName;
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

    export class WorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
        constructor(param: IWorkTypeModal) {
            this.workTypeCode = param.workTypeCode;
            this.name = param.name;
            this.memo = param.memo;
        }
    }

    export interface IWorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
    }

    export class EmploymentModal {
        employmentCode: string;
        name: string;
        memo: string;
        constructor(param: IEmploymentModal) {
            this.employmentCode = param.employmentCode;
            this.name = param.name;
            this.memo = param.memo;
        }
    }

    export interface IEmploymentModal {
        employmentCode: string;
        name: string;
        memo: string;
    }

    export class ClassficationsModal {
        classficationCode: string;
        name: string;
        memo: string;
        constructor(param: IClassficationsModal) {
            this.classficationCode = param.classficationCode;
            this.name = param.name;
            this.memo = param.memo;
        }
    }

    export interface IClassficationsModal {
        classficationCode: string;
        name: string;
        memo: string;
    }

    export module model {
        export interface ISpecialHolidayDto {
            specialHolidayCode?: string;
            specialHolidayName?: string;
            grantMethod?: number;
            memo?: string;
            workTypeList?: Array<string>;
            grantRegular?: IGrantRegularDto;
            grantPeriodic?: IGrantPeriodic;
            sphdLimit?: ISphdLimitDto;
            subCondition?: ISubConditionDto;
            grantSingle?: IGrantSingleDto;
        }
        export class SpecialHolidayDto {
            specialHolidayCode: KnockoutObservable<any>;
            specialHolidayName: KnockoutObservable<string>;
            grantMethod: KnockoutObservable<number>;
            memo: KnockoutObservable<string>;
            workTypeList: KnockoutObservableArray<any>;
            grantRegular: KnockoutObservable<GrantRegularDto>;
            grantPeriodic: KnockoutObservable<GrantPeriodicDto>;
            sphdLimit: KnockoutObservable<SphdLimitDto>;
            subCondition: KnockoutObservable<SubConditionDto>;
            grantSingle: KnockoutObservable<GrantSingleDto>;

            constructor(param: ISpecialHolidayDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.specialHolidayName = ko.observable(param.specialHolidayName || '');
                this.grantMethod = ko.observable(param.grantMethod || 0);
                this.memo = ko.observable(param.memo || '');
                this.workTypeList = ko.observableArray(param.workTypeList || null);
                this.grantRegular = ko.observable(param.grantRegular ? new GrantRegularDto(param.grantRegular) : new GrantRegularDto({}));
                this.grantPeriodic = ko.observable(param.grantPeriodic ? new GrantPeriodicDto(param.grantPeriodic) : new GrantPeriodicDto({}));
                this.sphdLimit = ko.observable(param.sphdLimit ? new SphdLimitDto(param.sphdLimit) : new SphdLimitDto({}));
                this.subCondition = ko.observable(param.subCondition ? new SubConditionDto(param.subCondition) : new SubConditionDto({}));
                this.grantSingle = ko.observable(param.grantSingle ? new GrantSingleDto(param.grantSingle) : new GrantSingleDto({}));
            }
        }

        export interface IGrantRegularDto {
            specialHolidayCode?: string;
            grantStartDate?: string;
            months?: number;
            years?: number;
            grantRegularMethod?: number;
        }
        export class GrantRegularDto {
            specialHolidayCode: KnockoutObservable<any>;
            grantStartDate: KnockoutObservable<string>;
            months: KnockoutObservable<number>;
            years: KnockoutObservable<number>;
            grantRegularMethod: KnockoutObservable<number>;
            constructor(param: IGrantRegularDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.grantStartDate = ko.observable(param.grantStartDate || null); //TODO PENDING KIBAN FIX
                this.months = ko.observable(param.months || 0);
                this.years = ko.observable(param.years || 0);
                this.grantRegularMethod = ko.observable(param.grantRegularMethod || 0);
                
                if (this.grantRegularMethod() == 0) {
                    $('#hidden-lbl02').addClass('disabled');
                } else {
                    $('#hidden-lbl02').removeClass('disabled');
                }
                
                this.grantRegularMethod.subscribe(function(value) {
                    if (value == 0) {
                        $('#hidden-lbl02').addClass('disabled');
                    } else {
                        $('#hidden-lbl02').removeClass('disabled');
                    }
                });

            }
        }

        export interface IGrantPeriodic {
            specialHolidayCode?: string;
            grantDay?: number;
            splitAcquisition?: number;
            grantPeriodicMethod?: number;
        }
        export class GrantPeriodicDto {
            specialHolidayCode: KnockoutObservable<any>;
            grantDay: KnockoutObservable<number>;
            splitAcquisition: KnockoutObservable<number>;
            grantPeriodicMethod: KnockoutObservable<number>;
            constructor(param: IGrantPeriodic) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.grantDay = ko.observable(param.grantDay || null);
                this.splitAcquisition = ko.observable(param.splitAcquisition || 0);
                this.grantPeriodicMethod = ko.observable(param.grantPeriodicMethod || 0);
                
                
                if (this.grantPeriodicMethod() == 0) {
                    $('#hidden-lbl03').addClass('disabled');
                } else {
                    $('#hidden-lbl03').removeClass('disabled');
                }
                
                this.grantPeriodicMethod.subscribe(function(value) {
                    if (value == 0) {
                        $('#hidden-lbl03').addClass('disabled');
                    } else {
                        $('#hidden-lbl03').removeClass('disabled');
                    }
                });
            }
        }

        export interface ISphdLimitDto {
            specialHolidayCode?: string;
            specialVacationMonths?: number;
            specialVacationYears?: number;
            grantCarryForward?: number;
            limitCarryoverDays?: number;
            specialVacationMethod?: number;
        }
        export class SphdLimitDto {
            specialHolidayCode: KnockoutObservable<any>;
            specialVacationMonths: KnockoutObservable<number>;
            specialVacationYears: KnockoutObservable<number>;
            grantCarryForward: KnockoutObservable<number>;
            limitCarryoverDays: KnockoutObservable<number>;
            specialVacationMethod: KnockoutObservable<number>;
            constructor(param: ISphdLimitDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.specialVacationMonths = ko.observable(param.specialVacationMonths || 0);
                this.specialVacationYears = ko.observable(param.specialVacationYears || 0);
                this.grantCarryForward = ko.observable(param.grantCarryForward || 0);
                this.limitCarryoverDays = ko.observable(param.limitCarryoverDays || null);
                this.specialVacationMethod = ko.observable(param.specialVacationMethod || 0);
            }
        }

        export interface ISubConditionDto {
            specialHolidayCode?: string;
            useGender?: boolean;
            useEmployee?: boolean;
            useCls?: boolean;
            useAge?: boolean;
            genderAtr?: number;
            limitAgeFrom?: number;
            limitAgeTo?: number;
            ageCriteriaAtr?: number;
            ageBaseYearAtr?: number;
            ageBaseDates?: number;
            clsCodes?: Array<string>;
            empCodes?: Array<string>;
            employmentList?: Array<string>;
            classificationList?: Array<string>;
        }
        export class SubConditionDto {
            specialHolidayCode: KnockoutObservable<string>;
            useGender: KnockoutObservable<boolean>;
            useEmployee: KnockoutObservable<boolean>;
            useCls: KnockoutObservable<boolean>;
            useAge: KnockoutObservable<boolean>;
            genderAtr: KnockoutObservable<number>;
            limitAgeFrom: KnockoutObservable<number>;
            limitAgeTo: KnockoutObservable<number>;
            ageCriteriaAtr: KnockoutObservable<number>;
            ageBaseYearAtr: KnockoutObservable<number>;
            ageBaseDates: KnockoutObservable<number>;
            clsCodes: KnockoutObservableArray<string>;
            empCodes: KnockoutObservableArray<string>;
            employmentList: KnockoutObservableArray<any>;
            classificationList: KnockoutObservableArray<any>;

            constructor(param: ISubConditionDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.useGender = ko.observable(param.useGender || false);
                this.useEmployee = ko.observable(param.useEmployee || false);
                this.useCls = ko.observable(param.useCls || false);
                this.useAge = ko.observable(param.useAge || false);
                this.genderAtr = ko.observable(param.genderAtr || 0);
                this.limitAgeFrom = ko.observable(param.limitAgeFrom || null);
                this.limitAgeTo = ko.observable(param.limitAgeTo || null);
                this.ageCriteriaAtr = ko.observable(param.ageCriteriaAtr || 0);
                this.ageBaseYearAtr = ko.observable(param.ageBaseYearAtr || 0);
                this.ageBaseDates = ko.observable(param.ageBaseDates || null);
                this.clsCodes = ko.observableArray(param.clsCodes || null);
                this.empCodes = ko.observableArray(param.empCodes || null);
                this.employmentList = ko.observableArray(param.employmentList || null);
                this.classificationList = ko.observableArray(param.classificationList || null);
            }
        }

        export interface IGrantSingleDto {
            specialHolidayCode?: string;
            grantDaySingleType?: number;
            fixNumberDays?: number;
            makeInvitation?: number;
            holidayExclusionAtr?: number;
        }
        export class GrantSingleDto {
            specialHolidayCode: KnockoutObservable<string>;
            grantDaySingleType: KnockoutObservable<number>;
            fixNumberDays: KnockoutObservable<number>;
            makeInvitation: KnockoutObservable<number>;
            holidayExclusionAtr: KnockoutObservable<number>;
            constructor(param: IGrantSingleDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.grantDaySingleType = ko.observable(param.grantDaySingleType || 0);
                this.fixNumberDays = ko.observable(param.fixNumberDays || 0);
                this.makeInvitation = ko.observable(param.makeInvitation || 0);
                this.holidayExclusionAtr = ko.observable(param.holidayExclusionAtr || 0);
                
                if (this.grantDaySingleType() == 0) {
                    $('#hidden-lbl01').addClass('disabled');
                } else {
                    $('#hidden-lbl01').removeClass('disabled');
                }
                
                this.grantDaySingleType.subscribe(function(value) {
                    if (value == 0) {
                        $('#hidden-lbl01').addClass('disabled');
                    } else {
                        $('#hidden-lbl01').removeClass('disabled');
                    }
                });

            };
        }

        export enum GrantRegularMethod {
            /* 付与開始日を指定して付与する */
            GrantStartDateSpecify = 0,
            /* 付与日テーブルを参照して付与する */
            ReferGrantDateTable
        }

        export enum GrantPeriodicMethod {
            /* 許可する */
            Allow = 0,
            /* 許可しない */
            DoNotAllow
        }

        export enum SpecialVacationMethod {
            /** 0- 無期限 **/
            IndefinitePeriod = 0,
            /** 1- 次回付与日まで使用可能 **/
            AvailableUntilNextGrantDate,
            /** 2- 付与日から指定期間まで使用可能 **/
            AvailableGrantDateDesignate
        }

        export enum UseAtr {
            NotUse = 0,
            Use
        }
    }
}


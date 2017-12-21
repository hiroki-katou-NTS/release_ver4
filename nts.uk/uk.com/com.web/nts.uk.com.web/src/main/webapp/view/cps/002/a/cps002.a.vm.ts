module cps002.a.vm {
    import alert = nts.uk.ui.dialog.alert;
    import text = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import dialog = nts.uk.ui.dialog.info;
    import subModal = nts.uk.ui.windows.sub.modal;
    import jump = nts.uk.request.jump;
    import liveView = nts.uk.request.liveView;
    import character = nts.uk.characteristics;
    import block = nts.uk.ui.block;

    export class ViewModel {

        date: KnockoutObservable<Date> = ko.observable(moment().toDate());

        createTypeList: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, text('CPS002_26')),
            new BoxModel(2, text('CPS002_27')),
            new BoxModel(3, text('CPS002_28'))
        ]);

        categoryList: KnockoutObservableArray<CategoryItem> = ko.observableArray([]);

        initValueList: KnockoutObservableArray<InitSetting> = ko.observableArray([]);

        itemSettingList: KnockoutObservableArray<SettingItem> = ko.observableArray([]);

        createTypeId: KnockoutObservable<number> = ko.observable(1);

        currentEmployee: KnockoutObservable<Employee> = ko.observable(new Employee());

        categorySelectedCode: KnockoutObservable<string> = ko.observable('');

        empRegHistory: KnockoutObservable<EmpRegHistory> = ko.observable(null);

        currentStep: KnockoutObservable<number> = ko.observable(0);

        initSettingSelectedCode: KnockoutObservable<string> = ko.observable('');

        currentInitSetting: KnockoutObservable<InitSetting> = ko.observable(new InitSetting(null));

        copyEmployee: KnockoutObservable<EmployeeCopy> = ko.observable(new EmployeeCopy(null));

        layout: KnockoutObservable<Layout> = ko.observable(new Layout());

        isAllowAvatarUpload: KnockoutObservable<boolean> = ko.observable(false);

        currentUseSetting: KnockoutObservable<UserSetting> = ko.observable(null);

        employeeBasicInfo: KnockoutObservable<IEmployeeBasicInfo> = ko.observable(null);

        layoutData: KnockoutObservableArray<any> = ko.observableArray([]);

        ccgcomponent: any = {
            baseDate: ko.observable(moment().toDate()),
            isQuickSearchTab: true,
            isAdvancedSearchTab: true,
            isAllReferableEmployee: true,
            isOnlyMe: true,
            isEmployeeOfWorkplace: true,
            isEmployeeWorkplaceFollow: true,
            isMutipleCheck: false,
            isSelectAllEmployee: false,
            onApplyEmployee: (dataEmployee: Array<any>) => {
                let self = this;
                self.copyEmployee(new EmployeeCopy(dataEmployee[0]));
            }, onSearchOnlyClicked: function(data: any) {
                let self = this;
                self.copyEmployee(data);
            }
        };

        constructor() {
            let self = this;
            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent);

            self.createTypeId.subscribe((newValue) => {
                self.initValueList([]);
                self.categoryList([]);
                self.itemSettingList([]);
                self.categorySelectedCode('');
                self.initSettingSelectedCode('');
                self.currentInitSetting(new InitSetting(null));
            });

            self.employeeBasicInfo.subscribe((data) => {
                if (data) {
                    self.currentEmployee().hireDate(data.jobEntryDate);

                    self.createTypeId(data.employeeCreationMethod);


                    if (self.employeeBasicInfo().copyEmployeeId != "") {
                        let command = {
                            baseDate: moment().toDate(),
                            employeeIds: [data.copyEmployeeId]

                        }


                        service.getEmployeeInfo(command).done((result) => {
                            self.copyEmployee(new EmployeeCopy(result[0]));

                        });
                    }
                }
            });

            self.initSettingSelectedCode.subscribe((initCode) => {
                if (initCode === '') {
                    return;
                }

                let InitSetting = _.find(self.initValueList(), item => {
                    return item.itemCode == initCode;
                });
                if (InitSetting) {
                    service.getAllInitValueCtgSetting(InitSetting.itemId).done((result: Array<IInitValueCtgSetting>) => {
                        self.categorySelectedCode("");
                        if (result.length) {
                            self.categoryList(_.map(result, item => {
                                return new CategoryItem(item);
                            }));

                            self.categorySelectedCode(result[0].categoryCd);
                        } else {
                            self.categoryList.removeAll();
                        }
                    });

                    self.currentInitSetting(InitSetting);
                }

            });

            self.copyEmployee.subscribe((CopyEmployee) => {

                self.loadCopySettingItemData();

            });



            self.categorySelectedCode.subscribe((categoryCode) => {

                if (categoryCode == '') {
                    return;
                }
                self.itemSettingList.removeAll();
                if (self.isUseInitValue()) {
                    let command = ko.toJS(self.currentEmployee());
                    command.initSettingId = self.currentInitSetting().itemId;
                    command.baseDate = self.currentEmployee().hireDate();
                    command.categoryCd = categoryCode;
                    service.getAllInitValueItemSetting(command).done((result: Array<SettingItem>) => {
                        if (result.length) {
                            self.itemSettingList(_.map(result, item => {
                                return new SettingItem(item);
                            }));
                        }
                    });
                } else {

                    self.loadCopySettingItemData();


                }
            });

            self.currentEmployee().avatarId.subscribe((avartarId) => {


                var self = this,
                    avartarContent = $("#employeeAvatar");

                if (avartarId != "") {

                    avartarContent.html("");
                    avartarContent.append($("<img/>").attr("src", liveView(avartarId)).attr("id", "employeeAvatar"));
                } else {
                    avartarContent.html("");
                }

            });

            self.start();

        }

        loadCopySettingItemData() {

            let self = this,
                currentCopyEmployeeId = self.copyEmployee().employeeId,
                categorySelectedCode = self.categorySelectedCode(),
                baseDate = nts.uk.time.formatDate(self.currentEmployee().hireDate(), 'yyyyMMdd');

            if (currentCopyEmployeeId != "" && categorySelectedCode) {


                service.getAllCopySettingItem(currentCopyEmployeeId, categorySelectedCode, baseDate).done((result: Array<SettingItem>) => {
                    if (result.length) {
                        self.itemSettingList(_.map(result, item => {
                            return new SettingItem(item);
                        }));
                    }
                }).fail(error => {

                    dialog({ messageId: error.message });

                });
            }
        }

        start() {

            let self = this;


            nts.uk.characteristics.restore("NewEmployeeBasicInfo").done((data: IEmployeeBasicInfo) => {
                self.employeeBasicInfo(data);
            });
            service.getLayout().done((layout) => {
                if (layout) {
                    service.getUserSetting().done((result: IUserSetting) => {
                        if (result) {
                            self.getEmployeeCode(result).done((empCode) => {
                                self.currentEmployee().employeeCode(empCode);
                                self.getCardNumber(result);

                            });
                        }

                        self.currentUseSetting(new UserSetting(result));

                        self.getLastRegHistory(result);
                        $("#hireDate").focus();
                    });
                } else {
                    dialog({ messageId: "Msg_344" }).then(() => {
                        //move to toppage
                        jump('/view/cps/007/a/index.xhtml');
                    });
                }
            });

        }

        getLastRegHistory(userSetting: IUserSetting) {
            let self = this,
                showHistory = !userSetting ? true : userSetting.recentRegistrationType === 1 ? true : false;

            if (showHistory) {
                service.getLastRegHistory().done((result: IEmpRegHistory) => {
                    if (result) {

                        self.empRegHistory(new EmpRegHistory(result));
                    }
                });
            } else {
                self.empRegHistory(null);

            }
        }

        getEmployeeCode(userSetting: IUserSetting): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                genType = userSetting.employeeCodeType;

            if (genType === 3 || genType === 1) {
                service.getEmployeeCode(genType === 1 ? userSetting.employeeCodeLetter : '').done((result) => {

                    dfd.resolve(result);
                });
            }

            return dfd.promise();
        }

        getCardNumber(userSetting: IUserSetting) {
            //            let self = this,
            //                genType = userSetting.cardNumberType,
            //                eployee = self.currentEmployee();
            //
            //            if (genType === 1 || genType === 4) {
            //
            //                //                service.getCardNumber(genType === 1 ? userSetting.cardNumberLetter : '').done((result) => {
            //                //
            //                //                    eployee.cardNo(result);
            //                //
            //                //                });
            //            } else {
            //
            //                if (genType === 3) {
            //
            //                    eployee.cardNo(eployee.employeeCode());
            //                }
            //
            //                if (genType === 5) {
            //
            //                    service.getEmployeeCodeAndComId(userSetting.employeeCodeLetter).done((result) => {
            //
            //                        eployee.cardNo(result);
            //                    });
            //                }
            //            }

        }

        isError() {
            $(".form_step1").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return true;
            }
            return false;
        }

        completeStep1() {
            let self = this,
                employee = self.currentEmployee(),
                command = {
                    EmployeeCode: employee.employeeCode(),
                    cardNo: employee.cardNo(),
                    LoginId: employee.loginId()
                };
            if (!self.isError()) {
                service.validateEmpInfo(command).done(() => {

                    if (self.createTypeId() === 3) {

                        self.gotoStep3();
                        return;
                    }

                    self.gotoStep2();

                }).fail((error) => {

                    let message = error.message;
                    switch (message) {
                        case "Msg_345":
                            $('#employeeCode').ntsError('set', { messageId: message });
                            break;
                        case "Msg_757":
                            $('#loginId').ntsError('set', { messageId: message });
                            break;
                    }

                });
            }



        }


        backtoStep1() {

            let self = this;

            self.currentStep(0);

            self.start();
        }

        gotoStep3() {
            let self = this,
                command = ko.toJS(self.currentEmployee()),
                layout = self.layout();
            self.currentEmployee().avatarId("");
            self.currentStep(2);

            //add atr
            command.employeeCopyId = self.copyEmployee().employeeId;
            command.initSettingId = self.currentInitSetting().itemId;
            command.createType = self.createTypeId();
            service.getLayoutByCreateType(command).done((data: ILayout) => {
                layout.layoutCode(data.layoutCode || '');
                layout.layoutName(data.layoutName || '');

                if (data.standardDate) {
                    layout.standardDate(data.standardDate);
                }

                layout.listItemClsDto(data.itemsClassification || []);

            });

            $("#employeeAvatar").focus();



            service.getSelfRoleAuth().done((result: IRoleAuth) => {

                if (result) {
                    self.isAllowAvatarUpload(result ? result.allowAvatarUpload == 0 ? false : true : false);
                }

            });


        }


        completeStep2() {
            let self = this;
            if (self.copyEmployee().employeeId === '' && !self.isUseInitValue()) {

                dialog({ messageId: "Msg_349" });
                return;
            }

            if (nts.uk.text.isNullOrEmpty(self.initSettingSelectedCode()) && self.isUseInitValue()) {

                dialog({ messageId: "Msg_356" });
                return;
            }

            self.gotoStep3();


        }

        isUseInitValue() {
            let self = this;

            return self.createTypeId() === 2;
        }

        gotoStep2() {
            let self = this;

            self.currentStep(1);


            if (self.isUseInitValue()) {

                //start Screen C

                $('#initSettingPanel').show();
                self.loadInitSettingData();


            } else {

                //start Screen B

                $('#initSettingPanel').hide();

                self.loadCopySettingCtgData();
                if (self.copyEmployee().employeeId == '') {
                    $('#hor-scroll-button-show').trigger('click');
                }
                $('#inp_baseDate').focus();

            }


        }

        loadCopySettingCtgData() {

            let self = this;
            self.categoryList.removeAll();

            service.getCopySetting().done((result: Array<ICopySetting>) => {
                if (result.length) {
                    self.categoryList(_.map(result, item => {
                        return new CategoryItem(item);
                    }));

                    self.categorySelectedCode(result[0].code);
                }

            }).fail((error) => {

                dialog({ messageId: error.message }).then(() => {

                    self.currentStep(0);

                });

            });


        }

        loadInitSettingData() {

            let self = this;
            self.initValueList.removeAll();
            service.getAllInitValueSetting().done((result: Array<IInitValueSetting>) => {
                if (result.length) {
                    self.initValueList(_.map(result, item => {
                        return new InitSetting(item);
                    }));

                    if (self.initSettingSelectedCode() == '') {
                        if (self.employeeBasicInfo() && _.find(result, ['settingCode', self.employeeBasicInfo().initialValueCode])) {
                            self.initSettingSelectedCode(self.employeeBasicInfo().initialValueCode);
                        } else {
                            self.initSettingSelectedCode(result[0].settingCode);
                        }
                    }
                    $("#initSearchBox input").focus();
                }
            }).fail((error) => {
                dialog({ messageId: error.message }).then(() => {
                    self.currentStep(0);
                });

            });

        }

        prev() {
            let self = this;
            if (self.currentStep() === 2) {
                //self.layout(new Layout({ id: '', code: '', name: '' }));
                nts.uk.ui.errors.clearAll();
            }
            if (self.createTypeId() === 3) {
                $('#emp_reg_info_wizard').ntsWizard("goto", 0);
                return;
            }
            $('#emp_reg_info_wizard').ntsWizard("prev");


        }

        saveBasicInfo(command, employeeId) {
            let self = this,
                isInit = self.isUseInitValue(),
                currentEmpInfo = self.employeeBasicInfo(),
                newEmpInfo = {
                    copyEmployeeId: command.employeeCopyId,
                    jobEntryDate: command.hireDate,
                    initialValueCode: self.initSettingSelectedCode(),
                    employeeID: employeeId,
                    employeeCreationMethod: self.createTypeId()
                };

            if (currentEmpInfo) {
                if (isInit) {
                    newEmpInfo.copyEmployeeId = newEmpInfo.copyEmployeeId == '' ? currentEmpInfo.copyEmployeeId : newEmpInfo.copyEmployeeId;
                } else {
                    newEmpInfo.initialValueCode = newEmpInfo.initialValueCode == '' ? currentEmpInfo.initialValueCode : newEmpInfo.initialValueCode;

                }

            }




            character.save('NewEmployeeBasicInfo', newEmpInfo);
        }


        finish() {

            let self = this,
                command = ko.toJS(self.currentEmployee());
            //add atr
            command.employeeCopyId = self.copyEmployee().employeeId;
            command.initSettingId = self.currentInitSetting().itemId;
            command.inputs = self.layoutData();
            command.createType = self.createTypeId();

            if (!self.isError()) {

                service.addNewEmployee(command).done((employeeId) => {
                    self.saveBasicInfo(command, employeeId);

                    nts.uk.ui.windows.sub.modal('/view/cps/002/h/index.xhtml', { dialogClass: "no-close", title: '' }).onClosed(() => {
                        if (getShared('isContinue')) {

                            self.backtoStep1();

                        } else {
                            jump('/view/cps/001/a/index.xhtml');
                        }
                    });

                }).fail(error => {

                    dialog({ messageId: error.message });

                })
            }
        }

        openEModal(param, data) {

            let self: ViewModel = __viewContext['viewModel'],
                isCardNoMode = param === 'true' ? true : false,
                useSetting = self.currentUseSetting(),
                employee = self.currentEmployee();

            setShared("cardNoMode", isCardNoMode);
            if (useSetting) {

                if (!isCardNoMode) {
                    self.getEmployeeCode(useSetting).done((employeeCode) => {

                        setShared("textValue", employeeCode);
                    });
                } else {


                }
            }
            subModal('/view/cps/002/e/index.xhtml', { title: '' }).onClosed(() => {

                let result = getShared("CPS002_PARAM"),
                    currentEmp = self.currentEmployee();
                if (result) {

                    param === isCardNoMode ? currentEmp.cardNo(result) : currentEmp.employeeCode(result);
                }
            });
        }

        openFModal() {

            let self = this;

            subModal('/view/cps/002/f/index.xhtml', { title: '' }).onClosed(() => {

            });
        }

        openGModal() {

            let self = this;

            subModal('/view/cps/002/g/index.xhtml', { title: '' }).onClosed(() => {

                if (getShared("userSettingStatus")) {
                    service.getUserSetting().done((result: IUserSetting) => {
                        self.currentUseSetting(new UserSetting(result));
                        self.getLastRegHistory(result);

                    });

                }

            });
        }

        openIModal() {
            let self = this,
                avatarId = self.currentEmployee().avatarId();
            if (avatarId != "") {
                setShared("imageId", avatarId);
            }
            if (self.isAllowAvatarUpload()) {

                subModal('/view/cps/002/i/index.xhtml', { title: '' }).onClosed(() => {

                    let imageResult = getShared("imageId");

                    if (imageResult) {
                        self.currentEmployee().avatarId(imageResult);
                    }

                });

            }
        }


        openInitModal() {

            jump('/view/cps/009/a/index.xhtml');
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

    class Employee {

        employeeName: KnockoutObservable<string> = ko.observable("");
        employeeCode: KnockoutObservable<string> = ko.observable("");
        hireDate: KnockoutObservable<Date> = ko.observable(moment().toDate());
        cardNo: KnockoutObservable<string> = ko.observable("");
        avatarId: KnockoutObservable<string> = ko.observable("");
        loginId: KnockoutObservable<string> = ko.observable("");
        password: KnockoutObservable<string> = ko.observable("");
    }


    class EmployeeCopy {
        employeeId: string;
        employeeName: string;
        employeeCode: string;
        workplaceCode: string;
        workplaceId: string;
        workplaceName: string;

        constructor(param?: any) {
            this.employeeId = param ? param.employeeId : '';
            this.employeeName = param ? param.employeeName : '';
            this.employeeCode = param ? param.employeeCode : '';
            this.workplaceCode = param ? param.workplaceCode : '';
            this.workplaceId = param ? param.workplaceId : '';
            this.workplaceName = param ? param.workplaceName : '';
        }
    }

    interface IUserSetting {
        employeeCodeType: number;
        recentRegistrationType: number;
        cardNumberType: number;
        employeeCodeLetter: string;
        cardNumberLetter: string;
    }

    interface IEmpRegHistory {

        lastRegEmployee: IRegEmployee;

        lastRegEmployeeOfCompany: IRegEmployee;

    }

    interface IRegEmployee {

        employeeCd: string;

        employeeName: string;

    }


    interface IInitValueSetting {

        settingId: string;
        settingCode: string;
        settingName: string;

    }

    class InitSetting {

        itemId: string = '';
        itemCode: string = '';
        itemName: string = '';

        constructor(param?: any) {

            this.itemId = param ? param.settingId ? param.settingId : param.employeeId : '';
            this.itemCode = param ? param.settingCode ? param.settingCode : param.employeeCode : '';
            this.itemName = param ? param.settingName ? param.settingName : param.employeeName : '';
        }

    }




    interface IInitValueCtgSetting {

        categoryCd: string;
        categoryName: string;

    }


    interface ICopySetting {
        code: string;
        name: string;

    }

    class CategoryItem {
        code: string;
        name: string;
        constructor(param?: any) {
            this.code = param ? param.categoryCd ? param.categoryCd : param.code : '';
            this.name = param ? param.categoryName ? param.categoryName : param.name : '';

        }
    }


    class UserSetting {
        employeeCodeType: number;
        recentRegistrationType: number;
        cardNumberType: number;
        employeeCodeLetter: string;
        cardNumberLetter: string;
        constructor(param?: IUserSetting) {

            this.employeeCodeType = param ? param.employeeCodeType : 0;
            this.recentRegistrationType = param ? param.recentRegistrationType : 0;
            this.cardNumberType = param ? param.cardNumberType : 0;
            this.employeeCodeLetter = param ? param.employeeCodeLetter : "";
            this.cardNumberLetter = param ? param.cardNumberLetter : "";
        }
    }

    class SettingItem {
        itemCode: string
        itemName: string;
        isRequired: number;
        saveData: any;
        constructor(param?: any) {
            this.itemCode = param ? param.itemCode : '';
            this.itemName = param ? param.itemName : '';
            this.isRequired = param ? param.isRequired : 0;
            this.saveData = param ? param.saveData : null;
        }
    }

    interface ILayout {
        layoutCode?: string;
        layoutName?: string;
        maintenanceLayoutID: string;
        itemsClassification?: Array<any>;
        classificationItems?: Array<any>;
        standardDate?: string;
    }

    class Layout {
        layoutCode: KnockoutObservable<string> = ko.observable('');
        layoutName: KnockoutObservable<string> = ko.observable('');
        maintenanceLayoutID: KnockoutObservable<string> = ko.observable('');
        listItemClsDto: KnockoutObservableArray<any> = ko.observableArray([]);
        standardDate: KnockoutObservable<string> = ko.observable(undefined);

        constructor(param?: ILayout) {
            let self = this;
            if (param) {
                self.layoutCode(param.layoutCode || '');
                self.layoutName(param.layoutName || '');
                self.maintenanceLayoutID(param.maintenanceLayoutID || '');
                self.standardDate(param.standardDate)

                self.listItemClsDto(param.itemsClassification || []);
            }
        }

        // recall selected layout event
        filterData() {
            let self = this;
            self.maintenanceLayoutID.valueHasMutated();
        }
    }

    class EmpRegHistory {

        lastRegEmployee: KnockoutObservable<RegEmployee> = ko.observable(null);

        lastRegEmployeeOfCompany: KnockoutObservable<RegEmployee> = ko.observable(null);


        constructor(param: IEmpRegHistory) {
            this.lastRegEmployee(param ? param.lastRegEmployee : null);

            this.lastRegEmployeeOfCompany(param ? param.lastRegEmployeeOfCompany : null);

        }
    }

    class IEmployeeBasicInfo {
        copyEmployeeId: string;
        jobEntryDate: Date;
        initialValueCode: string;
        employeeID: string;
        employeeCreationMethod: number;

    }

    class RegEmployee {

        employeeCd: string;

        employeeName: string;

        constructor(employeeCd: string, employeeName: string) {
            this.employeeCd = employeeCd;

            this.employeeName = employeeName;
        }
    }

    interface IRoleAuth {
        allowMapUpload: number;
        allowMapBrowse: number;
        allowDocRef: number;
        allowDocUpload: number;
        allowAvatarUpload: number;
        allowAvatarRef: number;

    }



}
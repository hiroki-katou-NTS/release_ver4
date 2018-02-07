module nts.uk.com.view.cmm021.a {

    export module viewModel {

        import UserDto = service.model.UserDto;
        import WindowAccountFinderDto = service.model.WindownAccountFinderDto;
        import SaveWindowAccountCommand = service.model.SaveWindowAccountCommand;
        import WindowAccountDto = service.model.WindowAccountDto;
        import SaveOtherSysAccountCommand = service.model.SaveOtherSysAccountCommand;

        //screen C
        import OtherSysAccFinderDto = service.model.OtherSysAccFinderDto;

        export class ScreenModel {
            baseDate: Date;
            inputDate: KnockoutObservable<Date>;
            
            listUserDto: UserDto[];
            listUserDtoScreenAC: UserDto[];
            checked: KnockoutObservable<boolean>;
            columns: KnockoutObservableArray<any>;
            currentCode: KnockoutObservable<any>;

            listUserInfos: KnockoutObservableArray<ItemModel>;

            useSet: KnockoutObservableArray<any>;
            selectUse: KnockoutObservable<number>;

            enableSave: KnockoutObservable<boolean>;

            listWindowAccCommand: WindowAccountDto[];
            selectedEmployeeId: KnockoutObservable<string>;

            personName: KnockoutObservable<string>;
            employeeCode: KnockoutObservable<string>;
            loginId: KnockoutObservable<string>;
            windowAcc1: WindowAccountDto;
            windowAcc2: WindowAccountDto;
            windowAcc3: WindowAccountDto;
            windowAcc4: WindowAccountDto;
            windowAcc5: WindowAccountDto;

            // declare properties of window account
            hostName1: KnockoutObservable<string>;
            userName1: KnockoutObservable<string>;

            hostName2: KnockoutObservable<string>;
            userName2: KnockoutObservable<string>;

            hostName3: KnockoutObservable<string>;
            userName3: KnockoutObservable<string>;

            hostName4: KnockoutObservable<string>;
            userName4: KnockoutObservable<string>;

            hostName5: KnockoutObservable<string>;
            userName5: KnockoutObservable<string>;

            isLoading: KnockoutObservable<boolean>;

            enable_Save: KnockoutObservable<boolean>;
            enable_Delete: KnockoutObservable<boolean>;

            enable_WinAcc1: KnockoutObservable<boolean>;
            enable_WinAcc2: KnockoutObservable<boolean>;
            enable_WinAcc3: KnockoutObservable<boolean>;
            enable_WinAcc4: KnockoutObservable<boolean>;
            enable_WinAcc5: KnockoutObservable<boolean>;

            userId: KnockoutObservable<string>;
            listUserUnsetting: ItemModel[];
            listUserUnsettingScreenAC: ItemModel[];

            userIdBeChoosen: KnockoutObservable<string>;

            isScreenBSelected: KnockoutObservable<boolean>;
            isScreenCSelected: KnockoutObservable<boolean>;

            //Screen C
            companyCode6: KnockoutObservable<string>;
            userName6: KnockoutObservable<string>;

            otherSysAcc: KnockoutObservable<OtherSysAccFinderDto>;

            enable_otherAcc: KnockoutObservable<boolean>;

            isSaveAction: boolean;   

            constructor() {
                let _self = this;
                _self.listUserDto = [];
                _self.listUserDtoScreenAC = [];
                _self.checked = ko.observable(true);
                _self.listUserInfos = ko.observableArray([]);
                _self.currentCode = ko.observable();
                _self.baseDate = moment(new Date()).toDate();
                _self.inputDate = ko.observable(moment(new Date()).toDate());
                
                _self.inputDate.subscribe((newValue) => {
                    if (_self.validateInputDate(newValue) == false) {
                        return false;
                    } else {
                        _self.baseDate = _self.inputDate();
                    }
                });
                
                _self.useSet = ko.observableArray([
                    { code: '1', name: nts.uk.resource.getText("CMM021_11") },
                    { code: '0', name: nts.uk.resource.getText("CMM021_10") },
                ]);
                _self.selectUse = ko.observable(1);

                _self.enableSave = ko.observable(true);
                _self.windowAcc1 = new WindowAccountDto("", "", "", 0, 0);
                _self.windowAcc2 = new WindowAccountDto("", "", "", 0, 0);
                _self.windowAcc3 = new WindowAccountDto("", "", "", 0, 0);
                _self.windowAcc4 = new WindowAccountDto("", "", "", 0, 0);
                _self.windowAcc5 = new WindowAccountDto("", "", "", 0, 0);

                _self.selectedEmployeeId = ko.observable(null);
                _self.selectedEmployeeId.subscribe((newValue) => {
                    $('.nts-input').ntsError('clear');

                    //check if selected employee id empty
                    if (nts.uk.util.isNullOrEmpty(newValue)) {
                        _self.unSelectedUserId();
                        $('.nts-input').ntsError('clear');
                    } else {
                        
                        _self.userId("");
                        if (newValue) {
                            _self.findUserDtoByEmployeeId(newValue);
                            $('.nts-input').ntsError('clear');
                        }
                    }
                    $('.nts-input').ntsError('clear');
                });

                _self.personName = ko.observable("");
                _self.employeeCode = ko.observable("");
                _self.loginId = ko.observable("");

                // B screen model
                // construct properties in window account
                _self.hostName1 = ko.observable("");
                _self.userName1 = ko.observable("");

                _self.hostName2 = ko.observable("");
                _self.userName2 = ko.observable("");

                _self.hostName3 = ko.observable("");
                _self.userName3 = ko.observable("");

                _self.hostName4 = ko.observable("");
                _self.userName4 = ko.observable("");

                _self.hostName5 = ko.observable("");
                _self.userName5 = ko.observable("");

                _self.enable_Save = ko.observable(true);
                _self.enable_Delete = ko.observable(true);

                // UI
                _self.enable_WinAcc1 = ko.observable(false);
                _self.enable_WinAcc2 = ko.observable(false);
                _self.enable_WinAcc3 = ko.observable(false);
                _self.enable_WinAcc4 = ko.observable(false);
                _self.enable_WinAcc5 = ko.observable(false);

                //SUBSCRIBLE 
                _self.hostName1.subscribe(() => {
                    _self.windowAcc1.isChange = true;
                });
                _self.userName1.subscribe(() => {
                    _self.windowAcc1.isChange = true;
                });
                _self.enable_WinAcc1.subscribe((value) => {
                    if (value == false) {
                        _self.windowAcc1.isChange = true;
                    }
                });

                _self.hostName2.subscribe(() => {
                    _self.windowAcc2.isChange = true;
                });
                _self.userName2.subscribe(() => {
                    _self.windowAcc2.isChange = true;
                });
                _self.enable_WinAcc2.subscribe((value) => {
                    if (value == false) {
                        _self.windowAcc2.isChange = true;
                    }
                });

                _self.hostName3.subscribe(() => {
                    _self.windowAcc3.isChange = true;
                });
                _self.userName3.subscribe(() => {
                    _self.windowAcc3.isChange = true;
                });
                _self.enable_WinAcc3.subscribe((value) => {
                    if (value == false) {
                        _self.windowAcc3.isChange = true;
                    }
                });

                _self.hostName4.subscribe(() => {
                    _self.windowAcc4.isChange = true;
                });
                _self.userName4.subscribe(() => {
                    _self.windowAcc4.isChange = true;
                });
                _self.enable_WinAcc4.subscribe((value) => {
                    if (value == false) {
                        _self.windowAcc4.isChange = true;
                    }
                });

                _self.hostName5.subscribe(() => {
                    _self.windowAcc5.isChange = true;
                });
                _self.userName5.subscribe(() => {
                    _self.windowAcc5.isChange = true;
                });
                _self.enable_WinAcc5.subscribe((value) => {
                    if (value == false) {
                        _self.windowAcc5.isChange = true;
                    }
                });

                _self.userId = ko.observable("");
                _self.userIdBeChoosen = ko.observable("");
                _self.userId.subscribe((newValue) => {
                    $('.nts-input').ntsError('clear');

                    if (!newValue) {
                        return;
                    }

                    _self.userIdBeChoosen(newValue);

                    if (_self.isScreenBSelected()) {
                        _self.findListWindowAccByUserId(newValue);
                    }
                    if (_self.isScreenCSelected()) {
                        _self.findFirstOtherAcc(newValue);
                    }
                });

                _self.selectUse.subscribe((newValue) => {
                    $('.nts-input').ntsError('clear');
                    if (newValue == 1 && _self.isScreenBSelected() == true) {
                        _self.loadUserSetting();
                    } else if (newValue == 0 && _self.isScreenBSelected() == true) {
                        _self.loadUserUnsetting();
                    } else if (newValue == 1 && _self.isScreenCSelected() == true) {
                        _self.loadUserSettingScreenAC();
                    } else if (newValue == 0 && _self.isScreenCSelected() == true) {
                        _self.loadUserUnsettingScreenAC();
                    }
                });

                _self.listUserUnsetting = [];
                _self.listUserUnsettingScreenAC = [];

                _self.isScreenBSelected = ko.observable(false);
                _self.isScreenCSelected = ko.observable(false);
                _self.isScreenBSelected.subscribe((newValue) => {
                    $('.nts-input').ntsError('clear');
                    if (newValue) {
                        if (!_.isEmpty(_self.listUserDto)) {
                            _self.userId("");
                            _self.userId(_self.listUserDto[0].userId);

                            // show first item in list item
                            _self.selectedEmployeeId("");
                            _self.selectedEmployeeId(_self.listUserDto[0].employeeId);

                        }
                    }
                });
                _self.isScreenCSelected.subscribe((newValue) => {
                    if (newValue) {
                        if (!_.isEmpty(_self.listUserDtoScreenAC)) {
                            _self.userId("");
                            _self.userId(_self.listUserDtoScreenAC[0].userId);

                            // show first item in list item
                            _self.selectedEmployeeId("");
                            _self.selectedEmployeeId(_self.listUserDtoScreenAC[0].employeeId);

                        }
                    }
                });

                // Screen C
                _self.companyCode6 = ko.observable("");
                _self.userName6 = ko.observable("");

                _self.otherSysAcc = ko.observable(null);
                _self.enable_otherAcc = ko.observable(false);

                this.columns = ko.observableArray([
                    { headerText: '', key: 'employeeId', width: 150, hidden: true },
                    { headerText: nts.uk.resource.getText('CMM021_13'), key: 'loginId', width: 135 },
                    { headerText: nts.uk.resource.getText('CMM021_14'), key: 'employeeCode', width: 135 },
                    { headerText: nts.uk.resource.getText('CMM021_15'), key: 'personName', width: 135 },
                    { headerText: nts.uk.resource.getText('CMM021_17'), key: 'other', width: 60, formatter: lockIcon }
                ]);
            }
            
            // validate input date 
            private validateInputDate(inputDate: Date) {
                let _self = this;

                if (!moment.isDate(inputDate)) {
                    return false;
                }
                return true
            }
            

            /**
            * Start page
            */
            public startPage(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();
             
                _self.onSelectScreenB();
                dfd.resolve();
                return dfd.promise();
            }

            //load user id empty
            private unSelectedUserId() {
                let _self = this;

                if (_self.isScreenBSelected()) {
                    _self.unLoadListWinAcc();
                    _self.unselectedMode();
                    _self.personName("");
                    _self.employeeCode("");
                    _self.loginId("");


                } else if (_self.isScreenCSelected()) {
                    _self.unLoadOtherAcc();
                    _self.unselectedMode();
                    _self.personName("");
                    _self.employeeCode("");
                    _self.loginId("");
                }
            }

            /**
            * Show Error Message
            */
            private showMessageError(res: any) {
                // check error business exception
                if (!res.businessException) {
                    return;
                }

                // show error message
                if (Array.isArray(res.errors)) {
                    nts.uk.ui.dialog.bundledErrors(res);
                } else {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                }
            }

            //find list Window Acc
            private findListWindowAccByUserId(userId: string): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();
                service.findListWindowAccByUserId(userId).done((data: any) => {
                    // check data null or empty
                    if (data && data.length > 0) {
                        let winAcc1: any = data.filter(function(o: any) { return o.no == 1; })[0];
                        let winAcc2: any = data.filter(function(o: any) { return o.no == 2; })[0];
                        let winAcc3: any = data.filter(function(o: any) { return o.no == 3; })[0];
                        let winAcc4: any = data.filter(function(o: any) { return o.no == 4; })[0];
                        let winAcc5: any = data.filter(function(o: any) { return o.no == 5; })[0];

                        // set no#1
                        _self.hostName1(winAcc1.hostName);
                        _self.userName1(winAcc1.userName);
                        _self.enable_WinAcc1(winAcc1.useAtr == 1);
                        _self.windowAcc1.isChange = false;

                        // set no#2
                        _self.hostName2(winAcc2.hostName);
                        _self.userName2(winAcc2.userName);
                        _self.enable_WinAcc2(winAcc2.useAtr == 1);
                        _self.windowAcc2.isChange = false;

                        // set no#3
                        _self.hostName3(winAcc3.hostName);
                        _self.userName3(winAcc3.userName);
                        _self.enable_WinAcc3(winAcc3.useAtr == 1);
                        _self.windowAcc3.isChange = false;

                        // set no#4
                        _self.hostName4(winAcc4.hostName);
                        _self.userName4(winAcc4.userName);
                        _self.enable_WinAcc4(winAcc4.useAtr == 1);
                        _self.windowAcc4.isChange = false;

                        // set no#5
                        _self.hostName5(winAcc5.hostName);
                        _self.userName5(winAcc5.userName);
                        _self.enable_WinAcc5(winAcc5.useAtr == 1);
                        _self.windowAcc5.isChange = false;

                        _self.updateMode();
                    } else {
                        _self.newMode();
                        _self.loadNewWindowAccount();
                    }
                    $('#focus-hostName1').focus();
                    if (_self.isSaveAction) {
                        $('.nts-input').ntsError('clear');
                        _self.isSaveAction = false;                      
                    }
                    dfd.resolve();
                }).fail((res: any) => {
                    dfd.reject(res);
                });
                return dfd.promise();
            }

            // find user dto                      
            private findUserDtoByEmployeeId(selectedEmployeeId: string) {
                let _self = this;
                if (_self.isScreenBSelected()) {
                    if (selectedEmployeeId != "") {
                        let user = _self.listUserDto.filter(item => selectedEmployeeId == item.employeeId)[0];

                        _self.personName(user.personName);
                        _self.employeeCode(user.employeeCode);
                        _self.loginId(user.loginId);
                        _self.userId(user.userId);

                    } else {
                        _self.personName("");
                        _self.employeeCode("");
                        _self.loginId("");
                    }
                } else {
                    if (selectedEmployeeId != "") {
                        let user = _self.listUserDtoScreenAC.filter(item => selectedEmployeeId == item.employeeId)[0];

                        _self.personName(user.personName);
                        _self.employeeCode(user.employeeCode);
                        _self.loginId(user.loginId);
                        _self.userId(user.userId);

                    } else {
                        _self.personName("");
                        _self.employeeCode("");
                        _self.loginId("");
                    }
                }
            }

            private saveWindowAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                let user = _self.listUserDto.filter(item => _self.selectedEmployeeId() == item.employeeId)[0];

                let win1 = _self.windowAcc1;
                let win2 = _self.windowAcc2;
                let win3 = _self.windowAcc3;
                let win4 = _self.windowAcc4;
                let win5 = _self.windowAcc5;

                // validate
                if (!_self.validate()) {
                    return;
                }

                let saveCommand = new SaveWindowAccountCommand();

                if (_self.enable_WinAcc1() == true && _self.hostName1() != "" && _self.userName1() != "") {
                    win1.userId = _self.userId();
                    win1.hostName = _self.hostName1();
                    win1.userName = _self.userName1();
                    win1.no = 1;
                    win1.useAtr = 1;
                    saveCommand.winAcc1 = win1;
                }
                else if (_self.enable_WinAcc1() == false) {
                    win1.userId = _self.userId();
                    win1.hostName = "";
                    win1.userName = "";
                    win1.no = 1;
                    win1.useAtr = 0;
                    saveCommand.winAcc1 = win1;
                }

                if (_self.enable_WinAcc2() == true && _self.hostName2() != "" && _self.userName2() != "") {
                    win2.userId = _self.userId();
                    win2.hostName = _self.hostName2();
                    win2.userName = _self.userName2();
                    win2.no = 2;
                    win2.useAtr = 1;
                    saveCommand.winAcc2 = win2;
                }
                else if (_self.enable_WinAcc2() == false) {
                    win2.userId = _self.userId();
                    win2.hostName = "";
                    win2.userName = "";
                    win2.no = 2;
                    win2.useAtr = 0;
                    saveCommand.winAcc2 = win2;
                }


                if (_self.enable_WinAcc3() == true && _self.hostName3() != "" && _self.userName3() != "") {
                    win3.userId = _self.userId();
                    win3.hostName = _self.hostName3();
                    win3.userName = _self.userName3();
                    win3.no = 3;
                    win3.useAtr = 1;
                    saveCommand.winAcc3 = win3;
                }
                else if (_self.enable_WinAcc3() == false) {
                    win3.userId = _self.userId();
                    win3.hostName = "";
                    win3.userName = "";
                    win3.no = 3;
                    win3.useAtr = 0;
                    saveCommand.winAcc3 = win3;
                }

                if (_self.enable_WinAcc4() == true && _self.hostName4() != "" && _self.userName4() != "") {
                    win4.userId = _self.userId();
                    win4.hostName = _self.hostName4();
                    win4.userName = _self.userName4();
                    win4.no = 4;
                    win4.useAtr = 1;
                    saveCommand.winAcc4 = win4;
                }
                else if (_self.enable_WinAcc4() == false) {
                    win4.userId = _self.userId();
                    win4.hostName = "";
                    win4.userName = "";
                    win4.no = 4;
                    win4.useAtr = 0;
                    saveCommand.winAcc4 = win4;
                }

                if (_self.enable_WinAcc5() == true && _self.hostName5() != "" && _self.userName5() != "") {
                    win5.userId = _self.userId();
                    win5.hostName = _self.hostName5();
                    win5.userName = _self.userName5();
                    win5.no = 5;
                    win5.useAtr = 1;
                    saveCommand.winAcc5 = win5;
                }
                else if (_self.enable_WinAcc5() == false) {
                    win5.userId = _self.userId();
                    win5.hostName = "";
                    win5.userName = "";
                    win5.no = 5;
                    win5.useAtr = 0;
                    saveCommand.winAcc5 = win5;
                }

                saveCommand.userId = _self.userId();
                let isCheck: boolean = false;
                let saveArray: Array<WindowAccountDto> = [];
                saveArray.push(saveCommand.winAcc1);
                saveArray.push(saveCommand.winAcc2);
                saveArray.push(saveCommand.winAcc3);
                saveArray.push(saveCommand.winAcc4);
                saveArray.push(saveCommand.winAcc5);

                let saveArrayChecks: Array<WindowAccountDto> = _.filter(saveArray, function(o) { return o.useAtr != 0; });
                if (!_.isEmpty(saveArrayChecks)) {
                    isCheck = true;
                }
                nts.uk.ui.block.invisible();
                service.saveWindowAccount(saveCommand)
                    .done((data: any) => {
                        _self.isSaveAction = true;
                        if (_self.selectUse() == 0 && isCheck == true) {
                            _self.listUserInfos([]);
                            _self.listUserUnsetting = _self.listUserUnsetting.filter(item => item.userId != _self.userIdBeChoosen());
                            _self.listUserInfos(_self.listUserUnsetting);                                                    
                            _self.loadUserInfoAfterSaveWinAcc();                         
                            _self.selectedEmployeeId("");
                            _self.selectedEmployeeId(_self.listUserUnsetting.filter(item => item.userId != _self.userIdBeChoosen())[0].employeeId);
                            _self.updateMode();
//                            $('#focus-hostName1').focus();
//                            if (_self.isSaveAction) {
//                                $('.nts-input').ntsError('clear');
//                                _self.isSaveAction = false;
//                            }
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => $('.nts-input').ntsError('clear'));
                            dfd.resolve();
                        } else if (_self.selectUse() == 0 && isCheck == false) {
                            _self.listUserInfos([]);
                            _self.listUserInfos(_self.listUserUnsetting);
                            _self.loadUserInfoAfterSaveWinAcc();
                            _self.findListWindowAccByUserId("");
                            _self.findListWindowAccByUserId(_self.userIdBeChoosen());
                            _self.updateMode();
//                            $('#focus-hostName1').focus();
//                            if (_self.isSaveAction) {
//                                $('.nts-input').ntsError('clear');
//                                _self.isSaveAction = false;
//                            }
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => $('.nts-input').ntsError('clear'));
                            dfd.resolve();

                        } else {
                            _self.loadUserInfoAfterSaveAndDelWinAcc();
                            _self.reload();
                            _self.updateMode();
//                            $('#focus-hostName1').focus();
//                            if (_self.isSaveAction) {
//                                $('.nts-input').ntsError('clear');
//                                _self.isSaveAction = false;
//                            }
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => $('.nts-input').ntsError('clear'));
                            dfd.resolve();
                        }
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.bundledErrors(res);
                        dfd.reject();
                    }).always(() => nts.uk.ui.block.clear());
                return dfd.promise();
            }

            private reload() {
                let _self = this;

                _self.userId("");
                _self.userId(_self.userIdBeChoosen());
                _self.clearError();

            }

            /**
             * validate
             */
            private validate() {
                let _self = this;

                // clear error
                _self.clearError();

                // validate
                if (_self.enable_WinAcc1()) {
                    $('#focus-hostName1').ntsEditor('validate');
                    $('#userName1').ntsEditor('validate');
                }
                if (_self.enable_WinAcc2()) {
                    $('#hostName2').ntsEditor('validate');
                    $('#userName2').ntsEditor('validate');
                }
                if (_self.enable_WinAcc3()) {
                    $('#hostName3').ntsEditor('validate');
                    $('#userName3').ntsEditor('validate');
                }
                if (_self.enable_WinAcc4()) {
                    $('#hostName4').ntsEditor('validate');
                    $('#userName4').ntsEditor('validate');
                }
                if (_self.enable_WinAcc5()) {
                    $('#hostName5').ntsEditor('validate');
                    $('#userName5').ntsEditor('validate');
                }
                if (_self.enable_WinAcc5()) {
                    $('#hostName5').ntsEditor('validate');
                    $('#userName5').ntsEditor('validate');
                }
                if (_self.enable_otherAcc()) {
                    $('#focus-CompanyCode').ntsEditor('validate');
                    $('#userName6').ntsEditor('validate');
                }

                return !$('.nts-input').ntsError('hasError');
            }

            /**
             * clearError
             */
            private clearError() {
                $('#focus-hostName1').ntsError('clear');
                $('#userName1').ntsError('clear');
                $('#hostName2').ntsError('clear');
                $('#userName2').ntsError('clear');
                $('#hostName3').ntsError('clear');
                $('#userName3').ntsError('clear');
                $('#hostName4').ntsError('clear');
                $('#userName4').ntsError('clear');
                $('#hostName5').ntsError('clear');
                $('#userName5').ntsError('clear');
                $('#focus-CompanyCode').ntsError('clear');
                $('#userName6').ntsError('clear');
            }

            // common function load list user info
            private loadUserInfoScreenAB(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    dfd.reject();
                    return dfd.promise();
                }
                nts.uk.ui.block.invisible();
                service.findListUserInfo(_self.baseDate, false)
                    .done((data: UserDto[]) => {
                        _self.listUserDto = [];
                        _self.listUserDto = data;
                        if (_.isEmpty(_self.listUserDto)) {
                            _self.findUserDtoByEmployeeId("");
                            _self.unLoadListWinAcc();
                        }
                        if (!_.isEmpty(_self.listUserDto)) {
                            _self.selectedEmployeeId("");
                            _self.selectedEmployeeId(_self.listUserDto[0].employeeId);
                        }
                        _self.loadUserDto();
                        dfd.resolve();
                    })
                    .fail((res: any) => {
                        _self.showMessageError(res);
                        dfd.reject(res);
                    }).always(() => nts.uk.ui.block.clear());
                return dfd.promise();

            }

            private loadUserInfoAfterDeleteWinAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    dfd.reject();
                    return dfd.promise();
                }

                service.findListUserInfo(_self.baseDate, false)
                    .done((data: UserDto[]) => {
                        _self.listUserDto = [];
                        _self.listUserDto = data;
                        if (_.isEmpty(_self.listUserDto)) {
                            _self.findUserDtoByEmployeeId("");
                            _self.unLoadListWinAcc();
                        }
                        _self.loadUserDto();
                        dfd.resolve();
                    })
                    .fail((res: any) => {
                        dfd.reject(res);
                    });
                return dfd.promise();

            }


            private loadUserInfoAfterSaveAndDelWinAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    dfd.reject();
                    return dfd.promise();
                }

                service.findListUserInfo(_self.baseDate, false)
                    .done((data: UserDto[]) => {
                        _self.listUserDto = [];
                        _self.listUserDto = data;
                        if (_.isEmpty(_self.listUserDto)) {
                            _self.findUserDtoByEmployeeId("");
                            _self.unLoadListWinAcc();
                        }
                        _self.loadUserDto();
                        dfd.resolve();
                    })
                    .fail((res: any) => {
                        dfd.reject(res);
                    });
                return dfd.promise();

            }

            private loadUserInfoAfterSaveWinAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    dfd.reject();
                    return dfd.promise();
                }

                service.findListUserInfo(_self.baseDate, false)
                    .done((data: UserDto[]) => {
                        _self.listUserDto = [];
                        _self.listUserDto = data;
                       
                        dfd.resolve();
                    })
                    .fail((res: any) => {
                        dfd.reject(res);
                    });
                return dfd.promise();
            }

            private loadUserInfoForOtherAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    _self.loadUserDtoForScreenC();
                    dfd.reject();
                    return dfd.promise();
                } else {
                    nts.uk.ui.block.invisible();
                    service.findListUserInfo(_self.baseDate, true)
                        .done((data: UserDto[]) => {
                            _self.listUserDtoScreenAC = [];
                            _self.listUserDtoScreenAC = data;
                            if (_.isEmpty(_self.listUserDtoScreenAC)) {
                                _self.findUserDtoByEmployeeId("");
                                _self.unLoadOtherAcc();
                            } else {
                                _self.selectedEmployeeId("");
                                _self.selectedEmployeeId(_self.listUserDtoScreenAC[0].employeeId);
                            }

                            _self.loadUserDtoForScreenC();
                            dfd.resolve();
                        })
                        .fail((res: any) => {
                            dfd.reject(res);
                        }).always(() => nts.uk.ui.block.clear());
                    return dfd.promise();
                }
            }

            private loadUserInfoAfterSaveAndDelOtherAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    _self.loadUserDtoForScreenC();
                    dfd.reject();
                    return dfd.promise();
                } else {

                    service.findListUserInfo(_self.baseDate, true)
                        .done((data: UserDto[]) => {
                            _self.listUserDtoScreenAC = [];
                            _self.listUserDtoScreenAC = data;
                            if (_.isEmpty(_self.listUserDtoScreenAC)) {
                                _self.findUserDtoByEmployeeId("");
                                _self.unLoadOtherAcc();
                            }
                            _self.loadUserDtoForScreenC();
                            dfd.resolve();
                        })
                        .fail((res: any) => {
                            dfd.reject(res);
                        });
                    return dfd.promise();
                }
            }


            private loadUserInfoAfterSaveOtherAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    _self.loadUserDtoForScreenC();
                    dfd.reject();
                    return dfd.promise();
                } else {

                    service.findListUserInfo(_self.baseDate, true)
                        .done((data: UserDto[]) => {
                            _self.listUserDtoScreenAC = [];
                            _self.listUserDtoScreenAC = data;
                            dfd.resolve();
                        })
                        .fail((res: any) => {
                            dfd.reject(res);
                        });
                    return dfd.promise();
                }
            }


            // load new mode
            private loadNewWindowAccount() {
                let _self = this;

                _self.hostName1("");
                _self.userName1("");
                _self.enable_WinAcc1(true);

                _self.hostName2("");
                _self.userName2("");
                _self.enable_WinAcc2(false);

                _self.hostName3("");
                _self.userName3("");
                _self.enable_WinAcc3(false);

                _self.hostName4("");
                _self.userName4("");
                _self.enable_WinAcc4(false);

                _self.hostName5("");
                _self.userName5("");
                _self.enable_WinAcc5(false);

            }
            private unLoadListWinAcc() {
                let _self = this;

                _self.hostName1("");
                _self.userName1("");
                _self.enable_WinAcc1(false);

                _self.hostName2("");
                _self.userName2("");
                _self.enable_WinAcc2(false);

                _self.hostName3("");
                _self.userName3("");
                _self.enable_WinAcc3(false);

                _self.hostName4("");
                _self.userName4("");
                _self.enable_WinAcc4(false);

                _self.hostName5("");
                _self.userName5("");
                _self.enable_WinAcc5(false);
            }

            private unLoadOtherAcc() {
                let _self = this;

                _self.companyCode6("");
                _self.userName6("");
                _self.enable_otherAcc(false);
            }

            private loadNewOtherAcc() {
                let _self = this;

                _self.companyCode6("");
                _self.userName6("");
                _self.enable_otherAcc(true);
            }

            private loadUserDto() {
                let _self = this;
                _self.listUserInfos([]);
                // check user info loaded is not empty
                if (!_.isEmpty(_self.listUserDto)) {
                    for (let userDto of _self.listUserDto) {
                        if (userDto.isSetting) {
                            _self.listUserInfos.push(new ItemModel(userDto.personName, userDto.employeeCode, userDto.loginId, userDto.employeeId, userDto.userId, userDto.isSetting, 1));

                        } else {
                            _self.listUserInfos.push(new ItemModel(userDto.personName, userDto.employeeCode, userDto.loginId, userDto.employeeId, userDto.userId, userDto.isSetting, 0));
                        }
                    }
                    // if user info loaded is empty, set unselected mode    
                } else {
                    _self.unselectedMode();
                }

            }

            private loadUserDtoForScreenC() {
                let _self = this;
                _self.listUserInfos([]);
                // check user info loaded is not empty
                if (!_.isEmpty(_self.listUserDtoScreenAC)) {
                    for (let userDto of _self.listUserDtoScreenAC) {
                        if (userDto.isSetting) {
                            _self.listUserInfos.push(new ItemModel(userDto.personName, userDto.employeeCode, userDto.loginId, userDto.employeeId, userDto.userId, userDto.isSetting, 1));

                        } else {
                            _self.listUserInfos.push(new ItemModel(userDto.personName, userDto.employeeCode, userDto.loginId, userDto.employeeId, userDto.userId, userDto.isSetting, 0));
                        }
                    }
                    // if user info loaded is empty, set unselected mode 
                } else {
                    _self.unselectedMode();
                }

            }


            private loadUserUnsetting() {
                let _self = this;
                _self.listUserInfos([]);
                _self.listUserUnsetting = [];
                for (let userDto of _self.listUserDto) {
                    if (!userDto.isSetting) {
                        _self.listUserUnsetting.push(new ItemModel(userDto.personName, userDto.employeeCode, userDto.loginId, userDto.employeeId, userDto.userId, userDto.isSetting, userDto.other));
                    }
                }

                // select first item in list unsetting
                if (!_.isEmpty(_self.listUserUnsetting)) {
                    _self.selectedEmployeeId(_self.listUserUnsetting[0].employeeId);
                    _self.newMode();
                }

                _self.listUserInfos(_self.listUserUnsetting);
                if (_self.listUserUnsetting.length == 0) {
                    _self.unselectedMode();
                }
            }

            private loadUserUnsettingScreenAC() {
                let _self = this;
                _self.listUserInfos([]);
                _self.listUserUnsettingScreenAC = [];

                for (let userDto of _self.listUserDtoScreenAC) {
                    if (!userDto.isSetting) {
                        _self.listUserUnsettingScreenAC.push(new ItemModel(userDto.personName, userDto.employeeCode, userDto.loginId, userDto.employeeId, userDto.userId, userDto.isSetting, 0));
                    }
                }
                // select first item in list unsetting
                if (!_.isEmpty(_self.listUserUnsettingScreenAC)) {
                    _self.selectedEmployeeId("");
                    _self.selectedEmployeeId(_self.listUserUnsettingScreenAC[0].employeeId);
                    _self.newMode();
                }
                _self.listUserInfos(_self.listUserUnsettingScreenAC);
                if (_self.listUserUnsettingScreenAC.length == 0) {
                    _self.unselectedMode();
                }
                $('.nts-input').ntsError('clear');
            }

            //load user info  
            private loadUserInfo() {
                let _self = this;

                if (nts.uk.util.isNullOrEmpty(_self.baseDate)) {
                    return false;
                }

                if ($('.base-date-editor').ntsError("hasError")) {
                    return false;
                }

                if (_self.isScreenBSelected()) {
                    _self.loadUserInfoScreenAB().done(() => {
                        if (_self.selectUse() == 1) {
                            _self.loadUserInfoScreenAB();
                        } else if (_self.selectUse() == 0) {
                            _self.loadUserUnsetting();
                        }
                    });

                } else if (_self.isScreenCSelected()) {
                    _self.loadUserInfoForOtherAcc().done(() => {
                        if (_self.selectUse() == 1) {
                            _self.loadUserInfoForOtherAcc();
                        } else if (_self.selectUse() == 0) {
                            _self.loadUserUnsettingScreenAC();
                        }
                    });
                }
            }

            private loadUserSetting() {
                let _self = this;

                _self.loadUserDto();
                if (_.isEmpty(_self.listUserDto)) {
                    _self.findUserDtoByEmployeeId("");
                    _self.unLoadListWinAcc();
                } else {
                    _self.selectedEmployeeId("");
                    _self.selectedEmployeeId(_self.listUserDto[0].employeeId);
                }

            }
            private loadUserSettingScreenAC() {
                let _self = this;

                _self.loadUserDtoForScreenC();
                if (_.isEmpty(_self.listUserDtoScreenAC)) {
                    _self.findUserDtoByEmployeeId("");
                    _self.unLoadOtherAcc();
                } else {
                    _self.selectedEmployeeId("");
                    _self.selectedEmployeeId(_self.listUserDtoScreenAC[0].employeeId);
                }


            }

            // new mode
            private newMode() {
                let _self = this;
                _self.enable_Save(true);
                _self.enable_Delete(false);
            }

            // update mode
            private updateMode() {
                let _self = this;
                _self.enable_Save(true);
                _self.enable_Delete(true);
            }

            //unselected mode
            private unselectedMode() {
                let _self = this;

                _self.enable_Save(false);
                _self.enable_Delete(false);
            }

            private deleteAccount() {
                let _self = this;

                if (_self.isScreenBSelected()) {
                    _self.deleteWindowAccount();
                } else if (_self.isScreenCSelected()) {
                    _self.deleteOtherAcc();
                }
            }

            private saveAccount() {
                let _self = this;

                if (_self.isScreenBSelected()) {
                    _self.saveWindowAcc();
                } else if (_self.isScreenCSelected()) {
                    _self.SaveOtherAcc();
                }

            }

            private deleteWindowAccount(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {

                    service.removeWindowAccount(_self.userIdBeChoosen()).done((data: any) => {
                        if (_self.selectUse() == 0) {
                            _self.listUserInfos(_self.listUserUnsetting);
                            _self.newMode();
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                                $('.nts-input').ntsError('clear');
                                $('#focus-hostName1').focus();
                            });

                            dfd.resolve();
                        } else {
                            _self.loadUserInfoAfterSaveAndDelWinAcc();
                            _self.newMode();
                            _self.loadNewWindowAccount();
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                                $('.nts-input').ntsError('clear');
                                $('#focus-hostName1').focus();
                            });
                            dfd.resolve();
                        }
                    });
                }).ifNo(() => {
                    // cancel button delete
                    _self.loadUserInfoScreenAB();
                    $('#focus-hostName1').focus();
                    return;
                });
                return dfd.promise();
            }


            public onSelectScreenB() {
                let _self = this;
                _self.listUserInfos([]);
                _self.isScreenBSelected(true);
                _self.isScreenCSelected(false);
                _self.selectUse(1);
                _self.listUserInfos([]);
                _self.loadUserInfoScreenAB();
            }

            public onSelectScreenC() {
                let _self = this;
                _self.isScreenBSelected(false);
                _self.isScreenCSelected(true);
                _self.selectUse(1);
                _self.listUserInfos([]);
                _self.loadUserInfoForOtherAcc();
            }

            private findFirstOtherAcc(userId: string): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();

                service.findOtherSysAccByUserId(userId).done((data: any) => {
                    // check data null
                    if (data.userId != null && data.useAtr == 1) {
                        _self.companyCode6(data.companyCode);
                        _self.userName6(data.userName);
                        _self.enable_otherAcc(true);
                        _self.updateMode();
                    } else if (data.userId != null && data.useAtr == 0) {
                        _self.companyCode6(data.companyCode);
                        _self.userName6(data.userName);
                        _self.enable_otherAcc(false);
                        _self.updateMode();
                    } else {
                        _self.companyCode6("");
                        _self.userName6("");
                        _self.enable_otherAcc(true);
                        _self.newMode();
                    }
                    $('#focus-CompanyCode').focus();
                    if (_self.isSaveAction) {
                        $('.nts-input').ntsError('clear');
                        _self.isSaveAction = false;
                    }
                    $('.nts-input').ntsError('clear');

                    dfd.resolve();
                }).fail((res: any) => {
                    dfd.reject(res);
                });
                return dfd.promise();
            }

            private SaveOtherAcc(): JQueryPromise<any> {
                let _self = this;
                let otherAcc = new SaveOtherSysAccountCommand();

                // validate
                if (!_self.validate()) {
                    return;
                }

                if (_self.enable_otherAcc() == true && _self.companyCode6() != "" && _self.userName6() != "") {
                    otherAcc.userId = _self.userIdBeChoosen();
                    otherAcc.companyCode = _self.companyCode6();
                    otherAcc.userName = _self.userName6();
                    otherAcc.useAtr = 1;
                } else if (_self.enable_otherAcc() == false) {
                    otherAcc.userId = _self.userIdBeChoosen();
                    otherAcc.companyCode = "";
                    otherAcc.userName = "";
                    otherAcc.useAtr = 0;
                }
                if (otherAcc.userId != undefined) {
                    let dfd = $.Deferred<any>();
                    nts.uk.ui.block.invisible();
                    service.saveOtherSysAccount(otherAcc)
                        .done((data: any) => {
                            _self.isSaveAction = true;
                            if (_self.selectUse() == 0 && otherAcc.useAtr == 1) {
                                _self.listUserInfos([]);
                                _self.listUserUnsettingScreenAC = _self.listUserUnsettingScreenAC.filter(item => item.userId != _self.userIdBeChoosen());
                                _self.listUserInfos(_self.listUserUnsettingScreenAC);
                                _self.selectedEmployeeId("");
                                _self.selectedEmployeeId(_self.listUserUnsettingScreenAC.filter(item => item.userId != _self.userIdBeChoosen())[0].employeeId);
                                _self.loadUserInfoAfterSaveOtherAcc();
                                _self.findFirstOtherAcc("");
                                _self.findFirstOtherAcc(_self.userIdBeChoosen());
                                //_self.loadNewOtherAcc();
                                _self.updateMode();
//                                $('#focus-CompanyCode').focus();
//                                if (_self.isSaveAction) {
//                                    $('.nts-input').ntsError('clear');
//                                    _self.isSaveAction = false;
//                                }
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => $('.nts-input').ntsError('clear'));
                                dfd.resolve();
                            } else if (_self.selectUse() == 0 && otherAcc.useAtr == 0) {
                                _self.listUserInfos([]);
                                _self.listUserInfos(_self.listUserUnsettingScreenAC);
                                _self.loadUserInfoAfterSaveOtherAcc();
                                _self.findFirstOtherAcc("");
                                _self.findFirstOtherAcc(_self.userIdBeChoosen());
                                //_self.loadNewOtherAcc();
                                _self.updateMode();
//                                $('#focus-CompanyCode').focus();
//                                if (_self.isSaveAction) {
//                                    $('.nts-input').ntsError('clear');
//                                    _self.isSaveAction = false;
//                                }
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => $('.nts-input').ntsError('clear'));
                                dfd.resolve();
                            }
                            else {
                                _self.loadUserInfoAfterSaveAndDelOtherAcc();
                                _self.reload();
                                _self.updateMode();
//                                $('#focus-CompanyCode').focus();
//                                if (_self.isSaveAction) {
//                                    $('.nts-input').ntsError('clear');
//                                    _self.isSaveAction = false;
//                                }
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => $('.nts-input').ntsError('clear'));
                                dfd.resolve();
                            }
                        })
                        .fail((res: any) => {
                            nts.uk.ui.dialog.bundledErrors(res);
                            dfd.reject();
                        }).always(() => nts.uk.ui.block.clear());
                    return dfd.promise();
                }
            }

            private deleteOtherAcc(): JQueryPromise<any> {
                let _self = this;
                let dfd = $.Deferred<any>();
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.removeOtherSysAccount(_self.userIdBeChoosen()).done((data: any) => {
                        if (_self.selectUse() == 0) {
                            _self.listUserInfos(_self.listUserUnsettingScreenAC);
                            _self.selectedEmployeeId(_self.listUserUnsettingScreenAC[0].employeeId);
                            _self.newMode();
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                                $('.nts-input').ntsError('clear');
                                $('#focus-CompanyCode').focus();
                            });
                            dfd.resolve();
                        } else {
                            _self.loadUserInfoAfterSaveAndDelOtherAcc();
                            _self.newMode();
                            _self.companyCode6("");
                            _self.userName6("");
                            _self.enable_otherAcc(true);
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                                $('.nts-input').ntsError('clear');
                                $('#focus-CompanyCode').focus();
                            });
                            dfd.resolve();
                        }
                    });
                }).ifNo(() => {
                    _self.loadUserInfoForOtherAcc();
                    $('#focus-CompanyCode').focus();
                    return;
                });
                return dfd.promise();
            }
        }
    }

    function lockIcon(value: any) {
        let _self = this;
        if (value == 1)
            return "<i class='icon-78 icon'></i>";
        return '';
    }


    class ItemModel {
        personName: string;
        employeeCode: string;
        loginId: string;
        other: number;
        employeeId: string;
        userId: string;
        isSetting: boolean;

        constructor(personName: string, employeeCode: string, loginId: string, employeeId: string, userId: string, isSetting: boolean, other?: number) {
            this.personName = personName;
            this.employeeCode = employeeCode;
            this.loginId = loginId;
            this.employeeId = employeeId;
            this.userId = userId;
            this.isSetting = isSetting;
            this.other = other;

        }
    }
}



































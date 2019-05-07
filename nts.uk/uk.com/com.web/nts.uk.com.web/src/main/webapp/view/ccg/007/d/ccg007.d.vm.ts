module nts.uk.pr.view.ccg007.d {
    export module viewmodel {
        import SystemConfigDto = service.SystemConfigDto;
        import ContractDto = service.ContractDto;
        import blockUI = nts.uk.ui.block;
        import CheckChangePassDto = service.CheckChangePassDto;
        import character = nts.uk.characteristics;
        export class ScreenModel {
            employeeCode: KnockoutObservable<string>;
            password: KnockoutObservable<string>;
            companyList: KnockoutObservableArray<CompanyItemModel>;
            selectedCompanyCode: KnockoutObservable<string>;
            companyName: KnockoutObservable<string>;
            isSaveLoginInfo: KnockoutObservable<boolean>;
            contractCode: KnockoutObservable<string>;
            contractPassword: KnockoutObservable<string>;
            isSignOn: KnockoutObservable<boolean> = ko.observable(false);
            displayLogin: KnockoutObservable<boolean> = ko.observable(false);
            constructor() {
                var self = this;
                self.employeeCode = ko.observable('');
                self.password = ko.observable('');
                self.companyList = ko.observableArray([]);
                self.selectedCompanyCode = ko.observable('');
                self.companyName = ko.observable('');
                self.isSaveLoginInfo = ko.observable(true);
                self.contractCode = ko.observable('');
                self.contractPassword = ko.observable('');
                
                self.selectedCompanyCode.subscribe(function(code) {
                    _.each(self.companyList(), function (item, index) {
                        if ((item.companyCode == code)) {
                            self.companyName(item.companyName);
                        }
                    });
                });
            }
            start(): JQueryPromise<void> {
                var self = this;
                //get url
                let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
                let isSignOn = url.indexOf('signon=on') >= 0 || url.indexOf('signon=oN') >= 0 || url.indexOf('signon=On') >= 0
                || url.indexOf('signon=ON') >= 0;
                self.isSignOn(isSignOn);
                if(!isSignOn){
                    self.displayLogin(true);
                }
                var dfd = $.Deferred<void>();
                let defaultContractCode:string = "000000000000";
                //get system config
                blockUI.invisible();
                nts.uk.characteristics.restore("contractInfo").done(function(data:any) {
                    self.contractCode(data ? data.contractCode : "");
                    self.contractPassword(data ? data.contractPassword : "");
                    service.checkContract({ contractCode: data ? data.contractCode : "", contractPassword: data ? data.contractPassword : "" })
                        .done(function(showContractData: any) {
                            //check ShowContract
                            if (showContractData.onpre) {
                                nts.uk.characteristics.remove("contractInfo");
                                nts.uk.characteristics.save("contractInfo", { contractCode:defaultContractCode, contractPassword: self.contractPassword() });
                                self.contractCode(defaultContractCode);
                                self.contractPassword(null);
                                self.getEmployeeLoginSetting(defaultContractCode);
                            }
                            else {
                                if (showContractData.showContract && !showContractData.onpre) {
                                    self.openContractAuthDialog();
                                }
                                else {
                                    self.getEmployeeLoginSetting(data ? data.contractCode : null);
                                }
                            }
                            dfd.resolve();
                            blockUI.clear();
                        }).fail(function() {
                            dfd.resolve();
                            blockUI.clear();
                        });
                });
                
                service.ver().done(data => {
                    $("#ver").html(data.ver);
                });
                
                dfd.resolve();
                return dfd.promise();
            }

            //when invalid contract 
            private openContractAuthDialog() {
                var self = this;
                nts.uk.ui.windows.sub.modal("/view/ccg/007/a/index.xhtml", {
                    height: 300,
                    width: 400,
                    title: nts.uk.resource.getText("CCG007_9"),
                    dialogClass: 'no-close'
                }).onClosed(() => {
                    var contractCode = nts.uk.ui.windows.getShared('contractCode');
                    var contractPassword = nts.uk.ui.windows.getShared('contractPassword');
                    var isSubmit = nts.uk.ui.windows.getShared('isSubmit');
                    self.contractCode(contractCode);
                    self.contractPassword(contractPassword);
                    
                    //get url
                    let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
                    let isSignOn = url.indexOf('signon=on') >= 0;
                    
                    //Check signon
                    if (isSubmit && isSignOn){
                        self.submitLogin(isSignOn);
                    } else {
                        if (isSubmit) {
                            self.getEmployeeLoginSetting(self.contractCode());
                        }
                        service.getAllCompany().done(function(data: Array<CompanyItemModel>) {
                            //get list company from server 
                            self.companyList(data);
                            if (data.length > 0) {
                                self.selectedCompanyCode(self.companyList()[0].companyCode);
                            }
                        });
                    }
                });
            }

            private getEmployeeLoginSetting(contractCode: string): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                //get check signon
                let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
                let isSignOn = url.indexOf('signon=on') >= 0;
                service.getEmployeeLoginSetting(contractCode).done(function(data:any) {
                    if (data.gotoForm1) {
                        nts.uk.request.jump("/view/ccg/007/b/index.xhtml");
                    }
                    else {
                        //シングルサインオン（Active DirectorySSO）かをチェックする
                        if (isSignOn) {
                            self.submitLogin(isSignOn);
                        }
                        else {
                            service.getAllCompany().done(function(data: Array<CompanyItemModel>) {
                                //get list company from server 
                                self.companyList(data);
                                if (data.length > 0) {
                                    self.selectedCompanyCode(self.companyList()[0].companyCode);
                                    self.companyName(self.companyList()[0].companyName);
                                }
                                //get local storage info and set here
                                nts.uk.characteristics.restore("form3LoginInfo").done(function(loginInfo:any) {
                                    if (loginInfo) {
                                        self.selectedCompanyCode(loginInfo.companyCode);
                                        self.employeeCode(loginInfo.employeeCode);
                                    }
                                    dfd.resolve();
                                });
                            });
                        }
                    }
                });
                return dfd.promise();
            }

            private submitLogin(isSignOn: boolean) {
                var self = this;
                var submitData: any = {};
                submitData.companyCode = _.escape(self.selectedCompanyCode());
                submitData.employeeCode = _.escape(self.employeeCode());
                submitData.password = _.escape(self.password());
                submitData.contractCode = _.escape(self.contractCode());
                submitData.contractPassword = _.escape(self.contractPassword());
                blockUI.invisible();
                service.submitLogin(submitData).done(function(messError: CheckChangePassDto) {
                    if(!nts.uk.util.isNullOrUndefined(messError.successMsg)&&!nts.uk.util.isNullOrEmpty(messError.successMsg)){
                        nts.uk.ui.dialog.info({ messageId: messError.successMsg }).then(()=>{
                            self.doSuccessLogin(messError);      
                        });        
                    } else {
                        self.doSuccessLogin(messError); 
                    }
                    blockUI.clear();
                }).fail(function(res:any) {
                     blockUI.clear();
                    if (self.isSignOn()) {//SIGNON
                        if (!nts.uk.util.isNullOrEmpty(res.messageId)) {
                            if (res.messageId == 'Msg_876') {//ActiveDirectory変換マスタが見つかりません
                                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(() => {
                                    nts.uk.request.jump("/view/ccg/007/sso/adsso.xhtml", { 'errAcc': true, 'errMsg': res.message });
                                });
                            }else{
                                nts.uk.request.jump("/view/ccg/007/sso/adsso.xhtml", { 'errAcc': false, 'errMsg': res.message });
                            }
                        } else {
                            nts.uk.request.jump("/view/ccg/007/sso/adsso.xhtml", { 'errAcc': false, 'errMsg': res.message });
                        }
                    } else {//NORMAL
                        if (nts.uk.util.isNullOrEmpty(res.messageId)) {
                            nts.uk.ui.dialog.alertError(res.message);
                        } else {
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                        }
                    }
                });
            }
            
            private doSuccessLogin(messError){
                var self = this;
                if (messError.showContract) {
                    self.openContractAuthDialog();
                }
                else {
                    if(!nts.uk.util.isNullOrEmpty(messError.msgErrorId) && messError.msgErrorId == 'Msg_1517'){
                        //確認メッセージ（Msg_1517）を表示する{0}【残り何日】
                        nts.uk.ui.dialog.confirm({ messageId: messError.msgErrorId, messageParams: [messError.spanDays]})
                        .ifYes(()=>{
                            messError.changePassReason = 'Msg_1523';
                            self.OpenDialogE(messError);
                        })
                        .ifNo(()=>{
                            nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/d/index.xhtml");
                            //Save LoginInfo
                            nts.uk.characteristics.save("form3LoginInfo", {companyCode: _.escape(self.selectedCompanyCode()), employeeCode: _.escape(self.employeeCode())}).done(function() {
                                nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                            });
                        });
                    }else
                    //check MsgError
                    if (!nts.uk.util.isNullOrEmpty(messError.msgErrorId) || messError.showChangePass) {
                        if (messError.showChangePass) {
                            self.OpenDialogE(messError);
                        } else {
                            nts.uk.ui.dialog.alertError({ messageId: messError.msgErrorId });
                            self.password("");
                        }
                    } else {
                        if (self.isSignOn()) {
                            nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/d/index.xhtml?signon=on");
                        } else {
                            nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/d/index.xhtml");
                        }
                        //set mode login
                        character.remove("loginMode").done(function() {
//                                loginMode: true - sign on
//                                loginMode: false - normal
                            character.save("loginMode", self.isSignOn());
                        })
                        //Remove LoginInfo
                        nts.uk.characteristics.remove("form3LoginInfo").done(function() {
                            //check SaveLoginInfo
                            if (self.isSaveLoginInfo()) {
                                //Save LoginInfo
                                nts.uk.characteristics.save("form3LoginInfo", { companyCode: _.escape(self.selectedCompanyCode()), employeeCode: _.escape(self.employeeCode()) })
                                    .done(function() {
                                        nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                                    });
                            } else {
                                nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                            }
                        });
                    }
                }           
            }
            
            //open dialog E 
            private OpenDialogE(messError) {
                let self = this;
                
                //set LoginId to dialog
                nts.uk.ui.windows.setShared('parentCodes', {
                    form1: false,
                    contractCode : self.contractCode(),
                    employeeCode: self.employeeCode(),
                    companyCode: self.selectedCompanyCode()
                }, true);
                nts.uk.ui.windows.setShared("changePw", {
                    reasonUpdatePw: messError.changePassReason,
                    spanDays: messError.spanDays});
                nts.uk.ui.windows.sub.modal('/view/ccg/007/e/index.xhtml',{
                    width : 520,
                    height : 450
                }).onClosed(function(): any {
                    var changePwDone = nts.uk.ui.windows.getShared('changePwDone');
                    if (changePwDone) {
                        nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/d/index.xhtml");
                        //Save LoginInfo
                        nts.uk.characteristics.save("form3LoginInfo", {companyCode: _.escape(self.selectedCompanyCode()), employeeCode: _.escape(self.employeeCode())}).done(function() {
                            nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                        });
                    }
                })
            }
            
            //open dialog G
            OpenDialogG() {
                let self = this;
                
                //set LoginId to dialog
                nts.uk.ui.windows.setShared('parentCodes', {
                    form1: false,
                    companyCode: self.selectedCompanyCode(),
                    companyName: self.companyName(),
                    contractCode: self.contractCode(),
                    employeeCode : self.employeeCode()
                }, true);

                nts.uk.ui.windows.sub.modal('/view/ccg/007/g/index.xhtml',{
                    width : 520,
                    height : 350
                }).onClosed(function(): any {})
            }
            
            private account(){
                service.account().done(data => {
                    alert('domain: ' + data.domain + '\n' + 'user name: ' + data.userName)
                });
            }
        }
        export class CompanyItemModel {
            companyId: string;
            companyCode: string;
            companyName: string;
        }
    }
}
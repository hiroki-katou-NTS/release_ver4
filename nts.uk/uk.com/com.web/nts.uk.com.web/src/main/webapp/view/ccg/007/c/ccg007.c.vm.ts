module nts.uk.pr.view.ccg007.c {
    export module viewmodel {
        import SystemConfigDto = service.SystemConfigDto;
        import ContractDto = service.ContractDto;
        import blockUI = nts.uk.ui.block;
        import CheckChangePassDto = service.CheckChangePassDto;
        export class ScreenModel {
            companyCode: KnockoutObservable<string>;
            companyName: KnockoutObservable<string>;
            employeeCode: KnockoutObservable<string>;
            password: KnockoutObservable<string>;
            isSaveLoginInfo: KnockoutObservable<boolean>;
            contractCode: KnockoutObservable<string>;
            contractPassword: KnockoutObservable<string>;
            isSignOn: KnockoutObservable<boolean> = ko.observable(false);
            displayLogin: KnockoutObservable<boolean> = ko.observable(false);
            constructor() {
                var self = this;
                self.companyCode = ko.observable('');
                self.companyName = ko.observable('');
                self.employeeCode = ko.observable('');
                self.password = ko.observable('');
                self.isSaveLoginInfo = ko.observable(true);
                self.contractCode = ko.observable('');
                self.contractPassword = ko.observable('');
            }

            start(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                //get url
                let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
                let isSignOn = url.indexOf('signon=on') >= 0 || url.indexOf('signon=oN') >= 0 || url.indexOf('signon=On') >= 0
                || url.indexOf('signon=ON') >= 0;
                self.isSignOn(isSignOn);
                if(!isSignOn){
                    self.displayLogin(true);
                }
                let defaultContractCode:string = "000000000000";
                blockUI.invisible();
                
                //get system config
                //get local contract info
                nts.uk.characteristics.restore("contractInfo").done(function(data:any) {
                    //Set ContractInfo
                    self.contractCode(data ? data.contractCode : "");
                    self.contractPassword(data ? data.contractPassword : "");
                    
                    //Check Contract
                    service.checkContract({ contractCode: data ? data.contractCode : "", contractPassword: data ? data.contractPassword : "" })
                        .done(function(showContractData: any) {
                            if (showContractData.onpre) {
                                nts.uk.characteristics.remove("contractInfo");
                                nts.uk.characteristics.save("contractInfo", { contractCode: defaultContractCode, contractPassword: self.contractPassword() });
                                self.contractCode(defaultContractCode);
                                self.contractPassword(null);
                                self.getEmployeeLoginSetting(defaultContractCode);
                            }
                            else {
                                //if show contract
                                if (showContractData.showContract && !showContractData.onpre) {
                                    self.openContractAuthDialog();
                                }
                                else {
                                    //get employ login setting and check permit view form
                                    self.getEmployeeLoginSetting(data ? data.contractCode : null);
                                }
                            }
                        //clear blockUI
                        blockUI.clear();
                        dfd.resolve();
                    }).fail(function() {
                        //clear blockUI
                        dfd.resolve();
                        blockUI.clear();
                    });
                }).fail(function() {
                    //clear blockUI
                    dfd.resolve();
                    blockUI.clear();
                });
                
                service.ver().done(data => {
                    $("#ver").html(data.ver);
                });
                
                return dfd.promise();
            }

            //get employ login setting and check permit view form 
            private getEmployeeLoginSetting(contractCode: string): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
//                let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
//                let isSignOn = url.indexOf('signon=on') >= 0;
                service.getEmployeeLoginSetting(contractCode).done(function(data:any) {
                    if (data.gotoForm1) {
                        nts.uk.request.jump("/view/ccg/007/b/index.xhtml");
                    }
                    else {
                        //シングルサインオン（Active DirectorySSO）かをチェックする
                        if (self.isSignOn()) {
                            self.submitLogin(self.isSignOn());
                        }
                        else {
                            //get login infor from local storeage 
                            nts.uk.characteristics.restore("form2LoginInfo").done(function(loginInfo:any) {
                                if (loginInfo) {
                                    self.companyCode(loginInfo.companyCode);
                                    self.employeeCode(loginInfo.employeeCode);
                                }
                                dfd.resolve();
                            });
                        }
                    }
                });
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
                    if (isSubmit && isSignOn) {
                        self.submitLogin(isSignOn);
                    }
                    else {
                        if (isSubmit) {
                            self.getEmployeeLoginSetting(self.contractCode());
                        }
                    }
                });
            }

            //submit login
            private submitLogin(isSignOn : boolean) {
                var self = this;
                var submitData: any = {};
                submitData.companyCode = _.escape(self.companyCode());
                submitData.employeeCode = _.escape(self.employeeCode());
                submitData.password = _.escape(self.password());
                submitData.contractCode = _.escape(self.contractCode());
                submitData.contractPassword = _.escape(self.contractPassword());

                blockUI.invisible();
                service.submitLogin(submitData, isSignOn).done(function(messError: CheckChangePassDto) {
                    if (messError.showContract) {
                        self.openContractAuthDialog();
                    }
                    else {
                        //check MsgError
                        if (!nts.uk.util.isNullOrEmpty(messError.msgErrorId) || messError.showChangePass) {
                            if (messError.showChangePass) {
                                self.OpenDialogE();
                            } else {
                                nts.uk.ui.dialog.alertError({ messageId: messError.msgErrorId });
                                self.password("");
                            }
                        } else {
                            nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/c/index.xhtml");
                            //Remove LoginInfo
                            nts.uk.characteristics.remove("form2LoginInfo").done(function() {
                                //check SaveLoginInfo
                                if (self.isSaveLoginInfo()) {
                                    //Save LoginInfo
                                    nts.uk.characteristics.save("form2LoginInfo", { companyCode: _.escape(self.companyCode()), employeeCode: _.escape(self.employeeCode()) }).done(function() {
                                        nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                                    });
                                } else {
                                    nts.uk.characteristics.save("form2LoginInfo", { companyCode: _.escape(self.companyCode())}).done(function() {
                                        nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                                    });
                                }
                            });
                        }
                    }
                    blockUI.clear();
                }).fail(function(res:any) {
                    if(self.isSignOn()){
                            self.displayLogin(true);
                        }
                    //Return Dialog Error
                    if (!_.isEqual(res.message, "can not found message id")){
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                    } else {
                       nts.uk.ui.dialog.alertError(res.messageId);
                    }
                    blockUI.clear();
                });
            }
            
            //open dialog E 
            private OpenDialogE() {
                let self = this;
                
                //set LoginId to dialog
                nts.uk.ui.windows.setShared('parentCodes', {
                    form1: false,
                    employeeCode: self.employeeCode(),
                    contractCode : self.contractCode(),
                    companyCode: self.companyCode()
                }, true);

                nts.uk.ui.windows.sub.modal('/view/ccg/007/e/index.xhtml',{
                    width : 520,
                    height : 450
                }).onClosed(function(): any {
                    var childData = nts.uk.ui.windows.getShared('childData');
                    if (childData.submit) {
                        nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                    }else{
                        if(self.isSignOn()){
                            self.displayLogin(true);
                        }    
                    }   
                })
            }
            
            //open dialog G
            OpenDialogG() {
                let self = this;
                
                if(!nts.uk.util.isNullOrEmpty(self.companyCode())){
                    let companyId = self.contractCode() + "-" + self.companyCode();
                    service.getCompanyInfo(companyId).done(function(data: CompanyItemModel) {
                        //get list company from server 
                        self.companyName(data.companyName);
                        
                         //set LoginId to dialog
                        nts.uk.ui.windows.setShared('parentCodes', {
                            companyCode: self.companyCode(),
                            companyName: self.companyName(),
                            contractCode: self.contractCode(),
                            employeeCode : self.employeeCode(),
                            contractPassword: self.contractPassword()
                        }, true);
                        
                        nts.uk.ui.windows.sub.modal('/view/ccg/007/g/index.xhtml',{
                            width : 520,
                            height : 350
                        }).onClosed(function(): any {})
                    });
                }
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
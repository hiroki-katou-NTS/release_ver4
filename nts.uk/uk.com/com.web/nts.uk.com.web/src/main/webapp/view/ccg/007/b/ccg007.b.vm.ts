module nts.uk.pr.view.ccg007.b {
    export module viewmodel {
        import SystemConfigDto = service.SystemConfigDto;
        import ContractDto = service.ContractDto;
        import blockUI = nts.uk.ui.block;
        import SubmitData = service.SubmitData;
        import CheckChangePassDto = service.CheckChangePassDto;
        import character = nts.uk.characteristics;
        export class ScreenModel {
            loginId: KnockoutObservable<string>;
            password: KnockoutObservable<string>;
            contractCode: KnockoutObservable<string>;
            contractPassword: KnockoutObservable<string>;
            isSaveLoginInfo: KnockoutObservable<boolean>;
            isSignOn: KnockoutObservable<boolean> = ko.observable(false);
            displayLogin: KnockoutObservable<boolean> = ko.observable(false);
            constructor() {
                var self = this;
                self.loginId = ko.observable('');
                self.password = ko.observable('');
                self.contractCode = ko.observable('');
                self.contractPassword = ko.observable('');
                self.isSaveLoginInfo = ko.observable(true);
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
                let defaultContractCode: string = "000000000000";
                blockUI.invisible();
                //get system config
                //get local contract info
                nts.uk.characteristics.restore("contractInfo").done(function(data:any) {
                    //Set ContractInfo
                    self.contractCode(data ? data.contractCode : "");
                    self.contractPassword(data ? data.contractPassword : "");

                    //check Contract
                    service.checkContract({ contractCode: data ? data.contractCode : "", contractPassword: data ? data.contractPassword : "" })
                        .done(function(data: any) {
                            if (data.onpre) {
                                nts.uk.characteristics.remove("contractInfo");
                                nts.uk.characteristics.save("contractInfo", { contractCode: defaultContractCode, contractPassword: self.contractPassword() });
                                self.contractCode(defaultContractCode);
                                self.contractPassword(null);
                                //シングルサインオン（Active DirectorySSO）かをチェックする
                                if (isSignOn) {
                                    self.submitLogin(isSignOn);
                                }
                                else {
                                    //Get login ID and set here
                                    nts.uk.characteristics.restore("form1LoginInfo").done(function(loginInfo:any) {
                                        if (loginInfo) {
                                            self.loginId(loginInfo.loginId);
                                        }
                                    });
                                }
                            }
                            else {
                                //check ShowContract
                                if (data.showContract && !data.onpre) {
                                    self.openContractAuthDialog();
                                }
                                else {
                                    //シングルサインオン（Active DirectorySSO）かをチェックする
                                    if (isSignOn) {
                                        self.submitLogin(isSignOn);
                                    }
                                    else {
                                        //Get login ID and set here
                                        nts.uk.characteristics.restore("form1LoginInfo").done(function(loginInfo:any) {
                                            if (loginInfo) {
                                                self.loginId(loginInfo.loginId);
                                            }
                                        });
                                    }
                                }
                            }
                            //clear block
                            blockUI.clear();
                            dfd.resolve();
                        }).fail(function() {
                            //clear block
                            dfd.resolve();
                            blockUI.clear();
                        });
                }).fail(function() {
                    //clear block
                    dfd.resolve();
                    blockUI.clear();
                });
                
                service.ver().done(data => {
                    $("#ver").html(data.ver);
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
                    //Check signon
                    if (isSubmit && self.isSignOn()){
                        self.submitLogin(self.isSignOn());
                    }
                });
            }
            
            /**
             * SubmitLogin
             */
            private submitLogin(isSignOn : boolean) {
                var self = this;
                var submitData = <SubmitData>{};

                //Set SubmitData
                submitData.loginId = nts.uk.text.padRight(_.escape(self.loginId()), " ", 12);
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
                            nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/b/index.xhtml");
                            //set mode login
                            character.remove("loginMode").done(function(){
//                                loginMode: true - sign on
//                                loginMode: false - normal
                                character.save("loginMode", self.isSignOn());
                            })
                            //Remove LoginInfo
                            nts.uk.characteristics.remove("form1LoginInfo").done(function() {
                                //check SaveLoginInfo
                                if (self.isSaveLoginInfo()) {
                                    //Save LoginInfo
                                    nts.uk.characteristics.save("form1LoginInfo", { loginId: _.escape(self.loginId()) }).done(function() {
                                        nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
                                    });
                                } else {
                                    nts.uk.request.jump("/view/ccg/008/a/index.xhtml", { screen: 'login' });
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
                    form1: true,
                    loginId: self.loginId(),
                    contractCode : self.contractCode(),
                    contractPassword: self.contractPassword()
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
            
            //open dialog F 
            OpenDialogF() {
                let self = this;
                
                //set LoginId to dialog
                nts.uk.ui.windows.setShared('parentCodes', {
                    loginId: self.loginId(),
                    contractCode : self.contractCode()
                }, true);

                nts.uk.ui.windows.sub.modal('/view/ccg/007/f/index.xhtml',{
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
    }
}
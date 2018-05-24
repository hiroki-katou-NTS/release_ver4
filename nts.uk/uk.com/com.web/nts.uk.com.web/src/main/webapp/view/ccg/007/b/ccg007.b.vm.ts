module nts.uk.pr.view.ccg007.b {
    export module viewmodel {
        import SystemConfigDto = service.SystemConfigDto;
        import ContractDto = service.ContractDto;
        import blockUI = nts.uk.ui.block;
        import SubmitData = service.SubmitData;
        export class ScreenModel {
            loginId: KnockoutObservable<string>;
            password: KnockoutObservable<string>;
            contractCode: KnockoutObservable<string>;
            contractPassword: KnockoutObservable<string>;
            isSaveLoginInfo: KnockoutObservable<boolean>;
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
                var dfd = $.Deferred<void>();
                let url = _.toLower(_.trim(_.trim($(location).attr('href')), '%20'));
                let isSignOn = url.indexOf('signon=on') >= 0;

                blockUI.invisible();
                //get system config
                //get local contract info
                nts.uk.characteristics.restore("contractInfo").done(function(data) {
                    //Set ContractInfo
                    self.contractCode(data ? data.contractCode : "");
                    self.contractPassword(data ? data.contractPassword : "");

                    //check Contract
                    service.checkContract({ contractCode: data ? data.contractCode : "", contractPassword: data ? data.contractPassword : "" })
                        .done(function(data: any) {
                            //check ShowContract
                            if (data.showContract) {
                                self.openContractAuthDialog();
                            }
                            else {
                                //シングルサインオン（Active DirectorySSO）かをチェックする
                                if (isSignOn) {
                                    self.submitLogin(isSignOn);
                                }
                                else {
                                    //Get login ID and set here
                                    nts.uk.characteristics.restore("form1LoginInfo").done(function(loginInfo) {
                                        if (loginInfo) {
                                            self.loginId(loginInfo.loginId);
                                        }
                                    });
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
                    }
                });
            }

            private submitLogin(isSignOn : boolean) {
                var self = this;
                var submitData = <SubmitData>{};

                //Set SubmitData
                submitData.loginId = nts.uk.text.padRight(_.escape(self.loginId()), " ", 12);
                submitData.password = _.escape(self.password());
                submitData.contractCode = _.escape(self.contractCode());
                submitData.contractPassword = _.escape(self.contractPassword());

                blockUI.invisible();
                service.submitLogin(submitData).done(function(isError) {
                    //check MsgError
                    if (!nts.uk.util.isNullOrEmpty(isError)) {
                        nts.uk.ui.dialog.alertError({ messageId: isError });
                    } else {
                        nts.uk.request.login.keepUsedLoginPage("/nts.uk.com.web/view/ccg/007/b/index.xhtml");
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
                    blockUI.clear();
                }).fail(function(res) {
                    //Return Dialog Error
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                    blockUI.clear();
                });
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
                }).onClosed(function(): any {
                    //view all code of selected item 
                    var childData = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
//                        self.timeHistory(childData.timeHistory);
//                        self.startTime(childData.start);
//                        self.endTime(childData.end);
                    }
                })
            }
        }
    }
}
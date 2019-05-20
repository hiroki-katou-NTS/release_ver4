module nts.uk.pr.view.ccg007.g {
    export module viewmodel {
        import blockUI = nts.uk.ui.block;
        import CallerParameter = service.CallerParameter;
        import SendMailInfoFormGCommand = service.SendMailInfoFormGCommand;
        

        export class ScreenModel {
            companyName: KnockoutObservable<string>;
            employeeCode: KnockoutObservable<string>;
            
            // Parameter from caller screen.
            callerParameter: CallerParameter;
            
            constructor(parentData: CallerParameter) {
                var self = this;
                
                self.companyName = ko.observable(null);
                self.employeeCode = ko.observable(null);
                
                //parent data
                self.callerParameter = parentData;
            }
            
            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // block ui
                nts.uk.ui.block.invisible();
                
                //set infor sendMail
                self.companyName(self.callerParameter.companyName);
                self.employeeCode(self.callerParameter.employeeCode);
                
                dfd.resolve();
                
                //clear block
                nts.uk.ui.block.clear();

                return dfd.promise();
            }
            
            /**
             * Submit
             */
            public submit(): void{
                let self = this;
                
                blockUI.invisible();
                
                //add command
                let command: SendMailInfoFormGCommand = new SendMailInfoFormGCommand(self.callerParameter.companyCode, 
                    self.callerParameter.employeeCode, self.callerParameter.contractCode);
                
                //sendMail
                service.submitSendMail(command).done(function (data) {
                    if (data.length > 0 && !nts.uk.util.isNullOrEmpty(data[0].url)){
                        nts.uk.ui.dialog.info({ messageId: "Msg_207" }).then(() =>{
                            nts.uk.ui.windows.close();
                        });
                    }else{
                        nts.uk.ui.windows.close();
                    }
                    blockUI.clear();
                }).fail(function(res) {
                    //Return Dialog Error
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                    blockUI.clear();
                });
            }
            
            /**
             * close dialog
             */
            public closeDialog(): void {
                nts.uk.ui.windows.close();
            }
        }
    }
}
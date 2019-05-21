module nts.uk.pr.view.ccg007.f {
    export module viewmodel {
        import blockUI = nts.uk.ui.block;
        import CallerParameter = service.CallerParameter;
        import SendMailReturnDto = service.SendMailReturnDto;

        export class ScreenModel {
            
            loginId: KnockoutObservable<string>;
            
            // Parameter from caller screen.
            callerParameter: CallerParameter;
            
            constructor(parentData: CallerParameter) {
                var self = this;
                
                self.loginId = ko.observable(null);
                
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
                
                self.loginId(self.callerParameter.loginId);
                
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
                
                service.submitSendMail(self.callerParameter).done(function (data: SendMailReturnDto) {
                    if (!nts.uk.util.isNullOrEmpty(data.url)){
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
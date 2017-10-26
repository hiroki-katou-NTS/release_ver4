module nts.uk.com.view.cmm050.a {
    import MailServerFindDto = model.MailServerDto;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import blockUI = nts.uk.ui.block;
    
    export module viewmodel {
        export class ScreenModel {
            useServerArray: KnockoutObservableArray<any>;
            authMethodArray: KnockoutObservable<Array<any>>;
            encryptionMethodArray: KnockoutObservableArray<any>;
            
            authMethodEnable: KnockoutObservable<boolean>;
            
            havePopSetting: KnockoutObservable<boolean>;
            haveImapSetting: KnockoutObservable<boolean>;
            haveEncryptMethod: KnockoutObservable<boolean>;
            
            //common info
            emailAuth: KnockoutObservable<string>;
            useAuth: KnockoutObservable<number>;
            authMethod: KnockoutObservable<number>;
            password: KnockoutObservable<string>;
            encryptionMethod: KnockoutObservable<number>;
            
            //smtp info
            smtpPort: KnockoutObservable<number>;
            smtpServer: KnockoutObservable<string>;
                        
            //imap info
            imapPort: KnockoutObservable<number>;
            imapUseServer: KnockoutObservable<number>;
            imapServer: KnockoutObservable<string>;
            
            imapServerEnable: KnockoutObservable<boolean>;
            
            //pop info
            popPort: KnockoutObservable<number>;
            popUseServer: KnockoutObservable<number>;
            popServer: KnockoutObservable<string>;
            
            popServerEnable: KnockoutObservable<boolean>;
            
            constructor(){
                let _self = this;
                
                _self.useServerArray = ko.observableArray([
                    { value: 1, name: nts.uk.resource.getText("CMM050_7") },
                    { value: 0, name: nts.uk.resource.getText("CMM050_8") }
                ]);
                _self.authMethodArray = ko.observable([
                    { value: 0, text: 'POP BEFORE SMTP' },
                    { value: 1, text: 'IMAP BEFORE SMTP' },
                    { value: 2, text: 'SMTP AUTH LOGIN' },
                    { value: 3, text: 'SMTP AUTH PLAIN' },
                    { value: 4, text: 'SMTP AUTH CRAM MD5'}
                ]);
                 _self.encryptionMethodArray = ko.observableArray([
                    { value: 0, text: 'なし' },
                    { value: 1, text: 'SSL' },
                    { value: 2, text: 'TSL' }
                ]);
                
                _self.authMethodEnable = ko.observable(false);
                
                _self.havePopSetting = ko.observable(false);
                _self.haveImapSetting = ko.observable(false);
                _self.haveEncryptMethod = ko.observable(false);
                
                _self.emailAuth = ko.observable("");
                _self.useAuth = ko.observable(0);
                _self.authMethod = ko.observable(0);
                _self.password = ko.observable("");
                _self.encryptionMethod = ko.observable(0);
                
                _self.smtpPort = ko.observable(0);
                _self.smtpServer = ko.observable("");
                
                _self.imapPort = ko.observable(0);
                _self.imapUseServer = ko.observable(0);
                _self.imapServer = ko.observable("");
                
                _self.popPort = ko.observable(0);
                _self.popUseServer = ko.observable(0);
                _self.popServer = ko.observable("");
                
                _self.imapServerEnable = ko.observable(false);
                _self.popServerEnable = ko.observable(false);
                
                _self.useAuth.subscribe(function(useAuthChanged){
                    if(useAuthChanged == UseServer.USE){
                       _self.authMethodEnable(true);
                        _self.fillUI(_self.authMethod());
                    }else{
                       _self.authMethodEnable(false);
                       _self.havePopSetting(false);
                       _self.haveImapSetting(false);
                       _self.haveEncryptMethod(false);
                    }
                });
                
                _self.authMethod.subscribe(function(authMethodChanged){
                    _self.fillUI(authMethodChanged);
                });
                
                _self.imapUseServer.subscribe(function(imapUseServerChanged){
                    if(imapUseServerChanged == ImapUseServer.USE){
                       _self.imapServerEnable(true);
                    }else{
                       _self.imapServerEnable(false);
                    }
                });
                
                _self.popUseServer.subscribe(function(popUseServerChanged){
                    if(popUseServerChanged == PopUseServer.USE){
                       _self.popServerEnable(true);
                    }else{
                       _self.popServerEnable(false);
                    }
                });
                
                _self.emailAuth.subscribe(function(emailString){
                   if(emailString.trim().length <= 0){
                        $('#email_auth').ntsError('set', {messageId:"Msg_533"});
                        return;
                    }else{
                        _self.clearErrors();
                   }
                });
                
                _self.popPort.subscribe(function(popPort){
                    if(Number(popPort) <= 0)
                    {
                        _self.popPort(0);
                    }
                });
                _self.imapPort.subscribe(function(imapPort){
                    if(Number(imapPort) <= 0)
                    {
                        _self.imapPort(0);
                    }
                });
                _self.smtpPort.subscribe(function(smtpPort){
                    if(Number(smtpPort) <= 0)
                    {
                        _self.smtpPort(0);
                    }
                });
            }
            
            /**
             * Register Mail server setting
             */
            public registerMailSetting() {
                let _self = this;
                 
                if(_self.emailAuth().trim().length <= 0)
                {
                    $('#email_auth').ntsError('set', {messageId:"Msg_533"});
                    return;
                }
                
                if(_self.password().trim().length <= 0)
                {
                    $('#email_auth').ntsError('set', {messageId:"Msg_533"});
                    return;
                }
                
                // Validate
                if (_self.hasError()) {
                    // validate input pop info
                    if(_self.useAuth() == UseServer.USE && _self.authMethod() == AuthenticationMethod.POP_BEFORE_SMTP){
                        if(nts.uk.text.isNullOrEmpty(_self.popPort())){
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_542" });
                        }
                         if(_self.popUseServer() == UseServer.USE && nts.uk.text.isNullOrEmpty(_self.popServer())){
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_543" });
                        }
                    }
                    
                    // validate input imap info
                    if(_self.useAuth() == UseServer.USE && _self.authMethod() == AuthenticationMethod.IMAP_BEFORE_SMTP){
                        if(nts.uk.text.isNullOrEmpty(_self.imapPort())){
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_544" });
                        }
                        if(_self.imapUseServer() == UseServer.USE && nts.uk.text.isNullOrEmpty(_self.imapServer())){
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_545" });
                        }
                    }
                    return;
                }
                
                var dfd = $.Deferred<void>();
                
                var params = new model.MailServerDto(
                        _self.useAuth(),
                        _self.encryptionMethod(),
                        _self.authMethod(),
                        _self.emailAuth(),
                        _self.password(),
                        new model.SmtpInfoDto(_self.smtpServer(), _self.smtpPort()),
                        new model.PopInfoDto(_self.popServer(), _self.popUseServer(), _self.popPort()),
                        new model.ImapInfoDto(_self.imapServer(), _self.imapUseServer(), _self.imapPort())
                    );
                
                _self.saveMailServerSetting(params).done(function(){
                    dfd.resolve();
                    nts.uk.ui.dialog.alert({ messageId: "Msg_15" });
                }).fail(function(){
                    alert('error');    
                });
                
                return dfd.promise();
            }
            
            /**
             * Show dialog test
             */
            public showDialogTest() {
                let _self = this;
                // Validate
                if (_self.hasError()) {
                    nts.uk.ui.dialog.alert({ messageId: "Msg_533" });
                    return;
                }
                setShared('CMM050Params', {
                    emailAuth: _self.emailAuth(),
                }, true);
                nts.uk.ui.windows.sub.modal("/view/cmm/050/b/index.xhtml").onClosed(function() {
                  
                });
            }
            
            /**
             * Start Page
             */
            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                let _self = this;
               
                _self.loadMailServerSetting().done(function(data: MailServerFindDto){
      
                    //check visible
                    if (data.useAuth == UseServer.USE && (data.authenticationMethod == AuthenticationMethod.SMTP_AUTH_LOGIN || data.authenticationMethod == AuthenticationMethod.SMTP_AUTH_PLAIN)){
                        _self.fillUI(data.authenticationMethod);
                    }
                    
                    if(data.useAuth == UseServer.USE && data.authenticationMethod == AuthenticationMethod.POP_BEFORE_SMTP){
                        _self.fillUI(data.authenticationMethod);
                        if(data.popDto.popUseServer == PopUseServer.USE){
                          _self.popServerEnable(true);
                        }
                    }
                    
                    if(data.useAuth == UseServer.USE && data.authenticationMethod == AuthenticationMethod.IMAP_BEFORE_SMTP){
                        _self.fillUI(data.authenticationMethod);
                        if(data.imapDto.imapUseServer == ImapUseServer.USE){
                           _self.imapServerEnable(true);
                        }  
                    }
                    
                    dfd.resolve();
                });
                
                return dfd.promise();
            }
            
            private fillUI(authMethodChanged): void {
                let _self = this;
                switch(authMethodChanged){
                    case AuthenticationMethod.POP_BEFORE_SMTP:
                        _self.haveEncryptMethod(false);
                        _self.havePopSetting(true);
                        _self.haveImapSetting(false);
                        
                        _self.encryptionMethod(EncryptMethod.None);
                        break;
                    case AuthenticationMethod.IMAP_BEFORE_SMTP:
                        _self.haveEncryptMethod(false);
                        _self.havePopSetting(false);
                        _self.haveImapSetting(true);
                        
                        _self.encryptionMethod(EncryptMethod.None);
                        break;
                    case AuthenticationMethod.SMTP_AUTH_LOGIN:
                        _self.haveEncryptMethod(true);
                        _self.havePopSetting(false);
                        _self.haveImapSetting(false);
                        break;
                    case AuthenticationMethod.SMTP_AUTH_PLAIN:
                        _self.haveEncryptMethod(true);
                        _self.havePopSetting(false);
                        _self.haveImapSetting(false);
                        break;
                    case AuthenticationMethod.SMTP_AUTH_CRAM_MD5:
                        _self.haveEncryptMethod(false);
                        _self.havePopSetting(false);
                        _self.haveImapSetting(false);
                        
                        _self.encryptionMethod(EncryptMethod.None);
                        break;
                }    
            } 
            
            /**
             * Check Errors all input.
             */
            private hasError(): boolean {
                let _self = this;
                _self.clearErrors();
                $('#email_auth').ntsEditor("validate");
                if (_self.useAuth() == UseServer.USE){
                    $('#password').ntsEditor("validate");
                }
                $('#smtp_port').ntsEditor("validate");
                $('#smtp_server').ntsEditor("validate");
                 // validate input pop info
                if(_self.useAuth() == UseServer.USE && _self.authMethod() == AuthenticationMethod.POP_BEFORE_SMTP){
                     $('#pop_port').ntsEditor("validate");
                   
                    if(_self.popUseServer() == UseServer.USE && nts.uk.text.isNullOrEmpty(_self.popServer())){
                         $('#pop_server').ntsEditor("validate");
                    }
                }
                
                // validate input imap info
                if(_self.useAuth() == UseServer.USE && _self.authMethod() == AuthenticationMethod.IMAP_BEFORE_SMTP){
                    $('#imap_port').ntsEditor("validate");
                    if(_self.imapUseServer() == UseServer.USE && nts.uk.text.isNullOrEmpty(_self.imapServer())){
                        $('#imap_server').ntsEditor("validate");
                    }
                }
                if ($('.nts-input').ntsError('hasError')) {
                    return true;
                }
                return false;
            }

            /**
             * Clear Errors
             */
            private clearErrors(): void {
    
                 // Clear errors
                $('#email_auth').ntsEditor("clear");
                $('#password').ntsEditor("clear");
                $('#smtp_port').ntsEditor("clear");
                $('#smtp_server').ntsEditor("clear");
                $('#imap_port').ntsEditor("clear");
                $('#imap_server').ntsEditor("clear");
                $('#pop_port').ntsEditor("clear");
                $('#pop_server').ntsEditor("clear");
                // Clear error inputs
                $('.nts-input').ntsError('clear');
            }
            
            /**
             * Get mail server setting
             */
            private loadMailServerSetting(): JQueryPromise<any>{
                var dfd = $.Deferred<MailServerFindDto>();
                var _self = this;
                
                service.findMailServerSetting().done(function(data: MailServerFindDto){
                    if (data === undefined){
                         let data = new model.MailServerDto(
                                        _self.useAuth(),
                                        _self.encryptionMethod(),
                                        _self.authMethod(),
                                        _self.emailAuth(),
                                        _self.password(),
                                        new model.SmtpInfoDto(_self.smtpServer(), _self.smtpPort()),
                                        new model.PopInfoDto(_self.popServer(), _self.popUseServer(), _self.popPort()),
                                        new model.ImapInfoDto(_self.imapServer(), _self.imapUseServer(), _self.imapPort())
                                        );
                        dfd.resolve(data);
                    }else {
                        //set common mail server setting data
                        _self.emailAuth(data.emailAuthencation);
                        _self.useAuth(data.useAuth);
                        _self.authMethod(data.authenticationMethod);
                        _self.password(data.password);
                        _self.encryptionMethod(data.encryptionMethod);
                        
                        // set smtp info data
                        _self.smtpPort(data.smtpDto.smtpPort);
                        _self.smtpServer(data.smtpDto.smtpServer);
                        
                        // set pop info data
                        _self.popPort(data.popDto.popPort);
                        _self.popUseServer(data.popDto.popUseServer);
                        _self.popServer(data.popDto.popServer);
                        
                        // set imap info data
                        _self.imapPort(data.imapDto.imapPort);
                        _self.imapUseServer(data.imapDto.imapUseServer);
                        _self.imapServer(data.imapDto.imapServer);
                        
                        dfd.resolve(data);    
                    }
                     
                }).fail(function(){
                    alert("error")
                });
                
                return dfd.promise();
            }
            
            private saveMailServerSetting(params: MailServerFindDto): JQueryPromise<any>{
                var dfd = $.Deferred<MailServerFindDto>();
                let _self = this;
                
                service.registerMailServerSetting(params).done(function(){
                    dfd.resolve();
                }).fail(function(){
                    alert("error")
                });
                
                return dfd.promise();
            }
        }
        
        /**
         * Define constant Authentication Method
         */
        enum AuthenticationMethod {
            POP_BEFORE_SMTP = 0,
            IMAP_BEFORE_SMTP = 1,
            SMTP_AUTH_LOGIN = 2,
            SMTP_AUTH_PLAIN = 3,
            SMTP_AUTH_CRAM_MD5 = 4
        }
        
        /**
         * Define constant Use Server
         */
        enum UseServer {
            NOT_USE = 0,
            USE = 1
        }
        
         /**
         * Define constant Pop Use Server
         */
        enum PopUseServer {
            NOT_USE = 0,
            USE = 1
        }
        
         /**
         * Define constant Imap Use Server
         */
        enum ImapUseServer {
            NOT_USE = 0,
            USE = 1
        }
        
         /**
         * Define constant Encrypt Method
         */
        enum EncryptMethod {
            None = 0,
            SSL = 1,
            TSL = 2
        }
    }
}
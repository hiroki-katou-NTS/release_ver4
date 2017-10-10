module nts.uk.com.view.cmm050.a {
    import MailServerFindDto = model.MailServerDto;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    
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
            smtpTimeOut: KnockoutObservable<number>;
            smtpServer: KnockoutObservable<string>;
            smtpIpVersion: KnockoutObservable<number>;
            
            //imap info
            imapPort: KnockoutObservable<number>;
            imapTimeOut: KnockoutObservable<number>;
            imapUseServer: KnockoutObservable<number>;
            imapServer: KnockoutObservable<string>;
            imapIpVersion: KnockoutObservable<number>;
            
            imapServerEnable: KnockoutObservable<boolean>;
            
            //pop info
            popPort: KnockoutObservable<number>;
            popTimeOut: KnockoutObservable<number>;
            popUseServer: KnockoutObservable<number>;
            popServer: KnockoutObservable<string>;
            popIpVersion: KnockoutObservable<number>;
            
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
                    { value: 0, text: 'None' },
                    { value: 1, text: 'SSL' },
                    { value: 2, text: 'TSL' }
                ]);
                
                _self.authMethodEnable = ko.observable(false);
                
                _self.havePopSetting = ko.observable(false);
                _self.haveImapSetting = ko.observable(false);
                _self.haveEncryptMethod = ko.observable(false);
                
                _self.emailAuth = ko.observable(null);
                _self.useAuth = ko.observable(0);
                _self.authMethod = ko.observable(0);
                _self.password = ko.observable(null);
                _self.encryptionMethod = ko.observable(0);
                
                _self.smtpPort = ko.observable(0);
                _self.smtpTimeOut = ko.observable(0);
                _self.smtpServer = ko.observable(null);
                _self.smtpIpVersion = ko.observable(0);
                
                _self.imapPort = ko.observable(0);
                _self.imapTimeOut = ko.observable(0);
                _self.imapUseServer = ko.observable(0);
                _self.imapServer = ko.observable(null);
                _self.imapIpVersion = ko.observable(0);
                
                _self.popPort = ko.observable(0);
                _self.popTimeOut = ko.observable(0);
                _self.popUseServer = ko.observable(0);
                _self.popServer = ko.observable(null);
                _self.popIpVersion = ko.observable(0);
                
                _self.imapServerEnable = ko.observable(false);
                _self.popServerEnable = ko.observable(false);
                
                _self.useAuth.subscribe(function(useAuthChanged){
                    if(useAuthChanged == UseServer.USE){
                       _self.authMethodEnable(true);
                    }else{
                       _self.authMethodEnable(false);
                       _self.havePopSetting(false);
                       _self.haveImapSetting(false);
                       _self.haveEncryptMethod(false);
                    }
                });
                
                _self.authMethod.subscribe(function(authMethodChanged){
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
            }
            
            /**
             * Register Mail server setting
             */
            public registerMailSetting() {
                let _self = this;
                var dfd = $.Deferred<void>();
                
                // validate input pop info
                if(_self.useAuth() == UseServer.USE && _self.authMethod() == AuthenticationMethod.POP_BEFORE_SMTP){
                    if(nts.uk.text.isNullOrEmpty(_self.popPort())){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_542" });
                        return;
                    }
                     if(nts.uk.text.isNullOrEmpty(_self.popServer())){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_543" });
                        return;
                    }
                }
                
                // validate input imap info
                if(_self.useAuth() == UseServer.USE && _self.authMethod() == AuthenticationMethod.IMAP_BEFORE_SMTP){
                    if(nts.uk.text.isNullOrEmpty(_self.imapPort())){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_544" });
                        return;
                    }
                    if(nts.uk.text.isNullOrEmpty(_self.imapPort())){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_543" });
                        return;
                    }
                }
                
                var params = new model.MailServerDto(
                        _self.useAuth(),
                        _self.encryptionMethod(),
                        _self.authMethod(),
                        _self.emailAuth(),
                        _self.password(),
                        new model.SmtpInfoDto(_self.smtpIpVersion(), _self.smtpServer(), _self.smtpTimeOut(), _self.smtpPort()),
                        new model.PopInfoDto(_self.popIpVersion(), _self.popServer(), _self.popUseServer(), _self.popTimeOut(), _self.popPort()),
                        new model.ImapInfoDto(_self.imapIpVersion(), _self.imapServer(), _self.imapUseServer(), _self.imapTimeOut(), _self.imapPort())
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
                        _self.haveEncryptMethod(true);
                    }
                    
                    if(data.useAuth == UseServer.USE && data.authenticationMethod == AuthenticationMethod.POP_BEFORE_SMTP){
                        _self.havePopSetting(true);
                        if(data.popDto.popUseServer == PopUseServer.USE){
                          _self.popServerEnable(true);
                        }
                    }
                    
                    if(data.useAuth == UseServer.USE && data.authenticationMethod == AuthenticationMethod.IMAP_BEFORE_SMTP){
                        _self.haveImapSetting(true);
                         if(data.imapDto.imapUseServer == ImapUseServer.USE){
                           _self.imapServerEnable(true);
                        }  
                    }
                    
                    dfd.resolve();
                });
                
                return dfd.promise();
            }
            
            /**
             * Get mail server setting
             */
            private loadMailServerSetting(): JQueryPromise<any>{
                var dfd = $.Deferred<MailServerFindDto>();
                var _self = this;
                
                service.findMailServerSetting().done(function(data: MailServerFindDto){
                     //set common mail server setting data
                    _self.emailAuth(data.emailAuthencation);
                    _self.useAuth(data.useAuth);
                    _self.authMethod(data.authenticationMethod);
                    _self.password(data.password);
                    _self.encryptionMethod(data.encryptionMethod);
                    
                    // set smtp info data
                    _self.smtpPort(data.smtpDto.smtpPort);
                    _self.smtpTimeOut(data.smtpDto.smtpTimeOut);
                    _self.smtpServer(data.smtpDto.smtpServer);
                    _self.smtpIpVersion(data.smtpDto.smtpIpVersion);
                    
                    // set pop info data
                    _self.popPort(data.popDto.popPort);
                    _self.popTimeOut(data.popDto.popTimeOut);
                    _self.popUseServer(data.popDto.popUseServer);
                    _self.popServer(data.popDto.popServer);
                    _self.popIpVersion(data.popDto.popIpVersion);
                    
                    // set imap info data
                    _self.imapPort(data.imapDto.imapPort);
                    _self.imapTimeOut(data.imapDto.imapTimeOut);
                    _self.imapUseServer(data.imapDto.imapUseServer);
                    _self.imapServer(data.imapDto.imapServer);
                    _self.imapIpVersion(data.imapDto.imapIpVersion);
                    
                    dfd.resolve(data);
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
import { Vue, _ } from '@app/provider';
import { component, Prop } from '@app/core/component';
import { NavMenu, SideMenu } from '@app/services';
import { ccg007 } from '../common/common';
import { characteristics } from '@app/utils/storage';

@component({
    route: '/ccg/007/c',
    style: require('./style.scss'),
    template: require('./index.html'),
    validations: {
        model: {
            currentPassword: {
                required: true
            },
            newPassword: {
                required: true,
                checkSame: {
                    test(value) {
                        if(_.isEmpty(this.model.newPasswordConfirm)){
                            return false;
                        }
                        return this.model.newPassword === this.model.newPasswordConfirm;
                    }, message: 'Msg_961'
                }
            },
            newPasswordConfirm: {
                required: true,
                checkSame: {
                    test(value) {
                        if(_.isEmpty(this.model.newPassword)){
                            return false;
                        }
                        return this.model.newPassword === this.model.newPasswordConfirm;
                    }, message: 'Msg_961'
                }
            }
        }
    }, 
    name: 'changepass'
})
export class ChangePassComponent extends Vue {

    @Prop({ default: () => ({}) })
    public params!: any;

    public mesId: string;
    public userId: string;
    public changeReason: string;

    public policy = {
        lowestDigits: "0",
        alphabetDigit: "0",
        numberOfDigits: "0",
        symbolCharacters: "0",
        historyCount: "0",
        validPeriod: "0",
        isUse: false
    };

    public model = {
        currentPassword: '',
        newPassword: '',
        newPasswordConfirm: '',
        userName: ''
    };

    public created() {
        let self = this;
        self.mesId = !_.isNil(self.params.spanDays) ? self.$i18n(self.params.changePassReason, self.params.spanDays.toString()) : 
                        _.isEmpty(self.params.changePassReason) ? self.$i18n(self.params.mesId) 
                                                                : self.$i18n(self.params.changePassReason);
        Promise.all([this.$http.post(servicePath.getPasswordPolicy + self.params.contractCode), 
                    this.$http.post(servicePath.getUserName, {  contractCode: self.params.contractCode, 
                                                                employeeCode: self.params.employeeCode, 
                                                                companyCode: self.params.companyCode })])
                .then((values: Array<any>) => {
            let policy: PassWordPolicy = values[0].data, user: LoginInfor = values[1].data;
            self.model.userName = user.userName;
            self.policy.lowestDigits = policy.lowestDigits.toString();
            self.policy.alphabetDigit = policy.alphabetDigit.toString();
            self.policy.numberOfDigits = policy.numberOfDigits.toString();
            self.policy.symbolCharacters = policy.symbolCharacters.toString();
            self.policy.historyCount = policy.historyCount.toString();
            self.policy.validPeriod = policy.validityPeriod.toString();
            self.policy.isUse  = policy.isUse;
            self.userId = user.userId;
        });
    }

    public mounted() {
        // Hide top & side menu
        NavMenu.visible = false;
        SideMenu.visible = false;
    }

    public destroyed() {
        // Show menu
        NavMenu.visible = true;
        SideMenu.visible = true;
    }

    public changePass() {
        this.$validate();
        if (!this.$valid) {
            return;                   
        }

        let self = this, 
            command: ChangePasswordCommand = new ChangePasswordCommand(self.model.currentPassword, 
                                                                        self.model.newPassword, 
                                                                        self.model.newPasswordConfirm,
                                                                        self.userId);

        self.$mask('show');
        
        // submitChangePass
        self.$http.post(servicePath.changePass, command).then((res) => {
            ccg007.login(ccg007.submitLogin , this, {    companyCode : self.params.companyCode,
                                    employeeCode: self.params.employeeCode,
                                    password: command.newPassword,
                                    contractCode : self.params.contractCode,
                                    contractPassword : self.params.contractPassword
            }, () => {
                self.model.currentPassword = '';
                self.model.newPassword = '';
                self.model.newPasswordConfirm = '';
            }, self.params.saveInfo);
        }).catch((res) => {
            // Return Dialog Error
            self.$mask('hide');
            self.showMessageError(res);
        });
    }

    public loginOnly() {
        let self = this;
        characteristics.remove('companyCode')
                        .then(() => characteristics.save('companyCode', self.params.companyCode))
                        .then(() => characteristics.remove('employeeCode'))
                        .then(() => {
                            if (self.params.saveInfo) {
                                characteristics.save('employeeCode', self.params.employeeCode);
                            }
                        }).then(() => ccg007.toHomePage(self));
        // ccg007.login(servicePath.loginWithNoChangePass , this, {    companyCode : self.params.companyCode,
        //                                             employeeCode: self.params.employeeCode,
        //                                             password: self.params.oldPassword,
        //                                             contractCode : self.params.contractCode,
        //                                             contractPassword : self.params.contractPassword
        //                                 }, () => {
        //                                     self.model.currentPassword = '';
        //                                     self.model.newPassword = '';
        //                                     self.model.newPasswordConfirm = '';
        //                                 }, self.params.saveInfo);
    }

    public showMessageError(res: any) {
        // check error business exception
        if (!res.businessException) {
            return;
        }
        /** TODO: show error message */
        if (_.isArray(res.errors) && !_.isEmpty(res.errors)) {
            // nts.uk.ui.dialog.bundledErrors(res);
            /** TODO: show multi line message */
            this.$modal.error({ messageId: res.errors[0].messageId, messageParams: res.errors[0].parameterIds });
        } else {
            this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
        }
    }
}

const servicePath = {
    getPasswordPolicy: 'ctx/sys/gateway/changepassword/getPasswordPolicy/',
    changePass: 'ctx/sys/gateway/changepassword/submitchangepass/mobile',
    loginWithNoChangePass: 'ctx/sys/gateway/login/submit/mobile/nochangepass',
    getUserName: 'ctx/sys/gateway/changepassword/username/mobile'
};

class ChangePasswordCommand {
    public oldPassword: string;
    public newPassword: string;
    public confirmNewPassword: string;
    public userId: string;
    
    constructor(oldPassword: string, newPassword: string, confirmNewPassword: string, userId: string) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
        this.userId = userId;
    }
}

interface LoginInfor {
    loginId: string;
    userName: string;
    userId: string;
    contractCode: string;
}

interface PassWordPolicy {
    notificationPasswordChange: number;
    loginCheck: boolean;
    initialPasswordChange: boolean;
    isUse: boolean;
    historyCount: number;
    lowestDigits: number;
    validityPeriod: number;
    numberOfDigits: number;
    symbolCharacters: number;
    alphabetDigit: number; 
}
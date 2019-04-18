module nts.uk.pr.view.ccg007.c {

    export module service {

        // Service paths.
        var servicePath = {
            checkContract: "ctx/sys/gateway/login/checkcontract",
            submitLogin: "ctx/sys/gateway/login/submit/form2",
            getEmployeeLoginSetting: "ctx/sys/gateway/login/emlogsettingform2",
            getCompanyInfo: "ctx/sys/gateway/login/getcompanybycode",
            account: "ctx/sys/gateway/login/account",
            ver: "ctx/sys/gateway/login/build_info_time"
        }

        /**
          * Function is used to valid contract .
          */
        export function checkContract(data: any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.checkContract, data);
        }
        
        /**
          * Function is used to get employee login setting
          */
        export function getEmployeeLoginSetting(contractCode: string): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.getEmployeeLoginSetting +"/"+ contractCode);
        }
        
        export function getCompanyInfo(companyId: string): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.getCompanyInfo +"/"+ companyId);
        }
        
        export function account(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.account);
        }

        /**
          * Function is used to submit login.
          * loginMethod: true - sign on
          * loginMethod: false - normal
          */
        export function submitLogin(data: any, loginMethod: boolean): JQueryPromise<string> {
            if(!loginMethod){
                return nts.uk.request.ajax(servicePath.submitLogin, data);
            }else{
                return nts.uk.request.ajax(servicePath.submitLogin + location.search, data);
            }
        }
        
        export function ver(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.ver);
        }

        export interface SystemConfigDto {
            installForm: number;
        }
        export interface ContractDto {
            password: string;
            contractCode: string;
            startDate: string;
            endDate: string;
        }
        
        export interface CheckChangePassDto{
            showChangePass: boolean;
            msgErrorId: string;
            showContract: boolean;
        }
    }
}
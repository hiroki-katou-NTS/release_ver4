module nts.uk.pr.view.ccg007.d {

    export module service {

        // Service paths.
        var servicePath = {
            checkContract: "ctx/sys/gateway/login/checkcontract",
            submitLogin: "ctx/sys/gateway/login/submit/form3",
            getAllCompany: "ctx/sys/gateway/login/getcompany",
            getEmployeeLoginSetting: "ctx/sys/gateway/login/emlogsettingform3",
            account: "ctx/sys/gateway/login/account",
            ver: "ctx/sys/gateway/login/build_info_time"
        }

        /**
         * Function is used to copy new Top Page.
         */
        export function checkContract(data: any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.checkContract, data);
        }

        export function getEmployeeLoginSetting(contractCode: string): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.getEmployeeLoginSetting + "/" + contractCode);
        }
        
        /**
          * Function is used to copy new Top Page.
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
        
//        export function account(): JQueryPromise<any> {
//            return nts.uk.request.ajax(servicePath.account);
//        }
        
        export function ver(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.ver);
        }

        /**
          * Function is used to copy new Top Page.
          */
        export function getAllCompany(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.getAllCompany);
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
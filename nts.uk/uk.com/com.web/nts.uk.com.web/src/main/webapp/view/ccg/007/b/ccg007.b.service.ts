module nts.uk.pr.view.ccg007.b {

    export module service {

        // Service paths.
        var servicePath = {
            getContractAuth: "ctx/sys/gateway/login/checkcontract",
            submitLogin: "ctx/sys/gateway/login/submit/form1",
            account: "ctx/sys/gateway/login/account",
            ver: "ctx/sys/gateway/login/build_info_time"
        }

        /**
          * Function is used to check contract.
          */
        export function checkContract(data : any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.getContractAuth,data);
        }
        
        export function account(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.account);
        }

        /**
          * Function is used to copy new Top Page.
          * loginMethod: true - sign on
          * loginMethod: false - normal
          */
        export function submitLogin(data: SubmitData, loginMethod: boolean): JQueryPromise<CheckChangePassDto> {
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
        
        export interface SubmitData {
            loginId: string;
            password: string;
            contractCode: string;
            contractPassword: string;
        }
        
        export interface CheckChangePassDto{
            showChangePass: boolean;
            msgErrorId: string;
            showContract: boolean;
        }
            
    }
}
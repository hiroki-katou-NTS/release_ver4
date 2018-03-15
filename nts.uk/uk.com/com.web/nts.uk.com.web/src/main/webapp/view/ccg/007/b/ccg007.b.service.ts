module nts.uk.pr.view.ccg007.b {

    export module service {

        // Service paths.
        var servicePath = {
            getContractAuth: "ctx/sys/gateway/login/checkcontract",
            submitLogin: "ctx/sys/gateway/login/submit/form1"
        }

        /**
          * Function is used to check contract.
          */
        export function checkContract(data : any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.getContractAuth,data);
        }

        /**
          * Function is used to copy new Top Page.
          */
        export function submitLogin(data: any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.submitLogin + location.search, data);
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
    }
}
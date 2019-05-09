module view.ccg007.sso.service {
    var servicePath = {
        account: "ctx/sys/gateway/login/account"
    }

    export function account(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.account);
    }
}
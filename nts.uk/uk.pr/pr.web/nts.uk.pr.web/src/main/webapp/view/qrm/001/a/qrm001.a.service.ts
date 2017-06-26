module qrm001.a.service {
    var paths: any = {
        getRetirementPaymentList: "pr/core/retirement/payment/findByCompanyCodeandPersonId/{0}",
        register: "pr/core/retirement/payment/register",
        update: "pr/core/retirement/payment/update",
        remove: "pr/core/retirement/payment/remove"
    }
    
    export function getRetirementPaymentList(personId) {
        return nts.uk.request.ajax(nts.uk.text.format(paths.getRetirementPaymentList, personId));
    }
    
    export function registerRetirementPaymentInfo(command) {
        return nts.uk.request.ajax(paths.register, command);
    }
    
    export function updateRetirementPaymentInfo(command) {
        return nts.uk.request.ajax(paths.update, command);
    }
    
    export function removeRetirementPaymentInfo(command) {
        return nts.uk.request.ajax(paths.remove, command);
    }
    
}

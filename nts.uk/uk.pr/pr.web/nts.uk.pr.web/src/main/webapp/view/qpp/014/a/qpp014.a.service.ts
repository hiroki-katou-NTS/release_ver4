module qpp014.a.service {
    var path = {
        findPayDayProcessing: "pr/proto/payment/banktransfer/findPayDayProcessing",
    };

    export function findAll(): JQueryPromise<any> {
        var _path = nts.uk.text.format(path.findPayDayProcessing);
        return nts.uk.request.ajax(_path);
    }
}




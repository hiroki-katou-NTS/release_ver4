module qmm013.a.service {
    var paths: any = {
        getPersonalUnitPriceList: "pr/core/rule/employment/unitprice/personal/find/all",
        getPersonalUnitPriceListNoneDisplay: "pr/core/rule/employment/unitprice/personal/find/all/nonedisplay",
        getPersonalUnitPrice: "pr/core/rule/employment/unitprice/personal/find/",
        addPersonalUnitPrice: "pr/core/rule/employment/unitprice/personal/add",
        updatePersonalUnitPrice: "pr/core/rule/employment/unitprice/personal/update",
        removePersonalUnitPrice: "pr/core/rule/employment/unitprice/personal/remove"
    }

    export function getPersonalUnitPriceList(display: boolean): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        var path = display ? paths.getPersonalUnitPriceList : paths.getPersonalUnitPriceListNoneDisplay;
        nts.uk.request.ajax(path)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            });
        return dfd.promise();
    }

    export function getPersonalUnitPrice(code): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.getPersonalUnitPrice + "" + code)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            });
        return dfd.promise();
    }

    export function addPersonalUnitPrice(isCreated, data): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        var path = isCreated ? paths.addPersonalUnitPrice : paths.updatePersonalUnitPrice;
        nts.uk.request.ajax(path, data)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            });
        return dfd.promise();
    }

    export function removePersonalUnitPrice(data): JQueryPromise<any> {
        var dfd = $.Deferred<any>();

        nts.uk.request.ajax(paths.removePersonalUnitPrice, data)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            });
        return dfd.promise();
    }
}
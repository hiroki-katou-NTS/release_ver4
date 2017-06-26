module qmm018.b.service {
    var paths: any = {
        itemSelect: "pr/core/item/findall/category/{0}"
    }
    
    /**
     * select items master by category
     */
    export function itemSelect(categoryAtr): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(nts.uk.text.format(paths.itemSelect, categoryAtr))
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
}
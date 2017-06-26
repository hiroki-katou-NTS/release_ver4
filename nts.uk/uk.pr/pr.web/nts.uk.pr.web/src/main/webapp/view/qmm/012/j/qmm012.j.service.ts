module qmm012.j {
    export module service {
        var paths: any = {
            findAllItemMasterByCategory: "pr/core/item/findall/category",
            updateNameItemMaster: "pr/core/item/updateName"

        }

        export function findAllItemMasterByCategory(ctgAtr): JQueryPromise<Array<qmm012.b.service.model.ItemMaster>> {
            var dfd = $.Deferred<Array<qmm012.b.service.model.ItemMaster>>();
            nts.uk.request.ajax(paths.findAllItemMasterByCategory + "/" + ctgAtr)
                .done(function(res: Array<qmm012.b.service.model.ItemMaster>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }
        export function updateNameItemMaster(itemList: Array<qmm012.b.service.model.ItemMaster>): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateNameItemMaster, itemList)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }

    }

}
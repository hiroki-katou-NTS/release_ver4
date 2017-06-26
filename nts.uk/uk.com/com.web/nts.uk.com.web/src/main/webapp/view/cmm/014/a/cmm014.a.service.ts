module cmm014.a.service {
    var paths = {
        getAllClassification: "basic/organization/classification/findAllClassification",
        addClassification: "basic/organization/classification/add",
        updateClassification: "basic/organization/classification/update",
        removeClassification: "basic/organization/classification/remove"
    }

    /**
     * Get list classification
     */

    export function getAllClassification(): JQueryPromise<Array<viewmodel.model.ClassificationDto>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.getAllClassification)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
    * update Classification
    */

    export function addClassification(classification: viewmodel.model.ClassificationDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.addClassification, classification).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * update Classification
     */

    export function updateClassification(classification: viewmodel.model.ClassificationDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updateClassification, classification).done(
            function(res: any) {
                dfd.resolve(res);
            }
        )
            .fail(function(res) {
                dfd.reject(res);
            })

        return dfd.promise();
    }

    /**
    * remove Classification
    */

    export function removeClassification(classification: viewmodel.model.RemoveClassificationCommand) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.removeClassification, classification).done(
            function(res: any) {
                dfd.resolve(res);
            }
        )
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


}

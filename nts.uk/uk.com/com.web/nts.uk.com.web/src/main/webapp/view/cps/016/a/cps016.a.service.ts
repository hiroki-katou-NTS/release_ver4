module nts.uk.com.view.cps016.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    var paths = {
        getAllSelectionItems: "ctx/pereg/person/info/setting/selection/findAll",
        getPerInfoSelectionItem: "ctx/pereg/person/info/setting/selection/findItem/{0}",
        saveDataSelectionItem: "ctx/pereg/person/info/setting/selection/addSelectionItem",
        updateDataSelectionItem: "ctx/pereg/person/info/setting/selection/updateSelectionItem",
        removeDataSelectionItem: "ctx/pereg/person/info/setting/selection/removeSelectionItem",
        checkExistedSelectionItemId: "ctx/pereg/person/info/ctgItem/checkExistItem/{0}"


    }

    export function getAllSelectionItems() {
        return ajax(paths.getAllSelectionItems);
    }

    export function getPerInfoSelectionItem(selectionItemId: string) {
        let _path = format(paths.getPerInfoSelectionItem, selectionItemId);
        return nts.uk.request.ajax("com", _path);
    }

    export function saveDataSelectionItem(command) {
        return ajax(paths.saveDataSelectionItem, command);
    }

    export function updateDataSelectionItem(command) {
        return ajax(paths.updateDataSelectionItem, command);
    }

    export function removeDataSelectionItem(command) {
        return ajax(paths.removeDataSelectionItem, command);
    }
    export function checkExistedSelectionItemId(selectionItemId: string) {
        let _path = format(paths.checkExistedSelectionItemId, selectionItemId);
        return nts.uk.request.ajax("com", _path);
    }
}
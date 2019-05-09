module kdl002.a.service {
    var paths = {
        getPossibleWorkType: "at/share/worktype/getpossiblewktypeKDL002"
    }
    /**
    * get all item (item co the duoc chon)
    */
    export function getItemSelected(lstPossible: Array<string>): JQueryPromise<Array<viewmodel.model.WorkTypeInfor>> {
        return nts.uk.request.ajax("at", paths.getPossibleWorkType, lstPossible);
    }
}
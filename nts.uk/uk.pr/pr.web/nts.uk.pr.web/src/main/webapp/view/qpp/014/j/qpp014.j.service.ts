module qpp014.j.service {
    var paths = {
        saveAsPdfA: "screen/pr/QPP014/saveAsPdfA",
        saveAsPdfB: "screen/pr/QPP014/saveAsPdfB"
    }

    export function saveAsPdfA(command: any): JQueryPromise<any> {
        return nts.uk.request.exportFile(paths.saveAsPdfA, command);
    }
    
    export function saveAsPdfB(command: any): JQueryPromise<any> {
        return nts.uk.request.exportFile(paths.saveAsPdfB, command);
    }
}
module nts.uk.at.view.kdw006.d.service {
    let servicePath = {
        getFuncRestric: 'at/record/workrecord/operationsetting/func-rest',
        update: 'at/record/workrecord/operationsetting/register-func-rest'
    };

    export function update(dispRestric: any) {
        nts.uk.request.ajax(servicePath.update, dispRestric);
    }

    export function getFuncRestric() : JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getFuncRestric);
    }
}

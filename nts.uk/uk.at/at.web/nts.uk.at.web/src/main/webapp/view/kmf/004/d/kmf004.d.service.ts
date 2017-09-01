module nts.uk.at.view.kmf004.d.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    let paths: any = {
        find: "at/shared/yearserviceset/findAll",
        add: 'at/share/yearservicecom/add',
        update: 'at/share/yearservicecom/update',
    }

    export function findAll() {
        return nts.uk.request.ajax(paths.find); 
    }

    export function add(command: d.viewmodel.Item): JQueryPromise<void>{
        return ajax(paths.add, command);
    }

    export function update(command: d.viewmodel.Item): JQueryPromise<void>{
        return ajax(paths.update, command);
    }    
}
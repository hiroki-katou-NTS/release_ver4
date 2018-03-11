module nts.uk.at.view.kmf022.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    let paths: any = {
        find: "at/request/application/getalldatabyclosureId",
        //jobId. Muốn lấy được jobid thì phải truyền companyId và baseDate (com)
        findJobId: "bs/employee/jobtitle/findAll",
        // lấy xong list job Id thì có thể truyền cả list jobId để lấy list A15_4 (com)
        findJobTitleSearchList: "workflow/jobtitlesearchset/job/getbyCode",
        // update kaf022
        update: 'screen/at/kaf022/update',
        finAllData : "screen/at/kaf022/findAll"
    }
    export function update(command): JQueryPromise<Array<string>>{
        return ajax("at", paths.update, command);
    }
    export function findAllData():JQueryPromise<Array<string>>{
        return nts.uk.request.ajax("at", paths.finAllData); 
    }  
    export function findApp(ids: any): JQueryPromise<void> {
        return nts.uk.request.ajax("at", paths.find,ids);
    }
        // view model cho thêm đối tượng Kcp003Dto chỉ có 1 trường baseDate: truyền ngày hiện tại
    export function findJobId(date: any): JQueryPromise<void>{
        return nts.uk.request.ajax("com", paths.findJobId, date);     
    }
    export function findJobTitleSearchList(param: any): JQueryPromise<void> {
        return nts.uk.request.ajax("com", paths.findJobTitleSearchList, param);
    }
    
     
}
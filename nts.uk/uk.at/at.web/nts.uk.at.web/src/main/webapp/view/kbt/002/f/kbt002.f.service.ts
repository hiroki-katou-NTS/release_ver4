module nts.uk.at.view.kbt002.f {
    export module service {
        var paths: any = {
            getProcExecList: 'at/function/processexec/getProcExecLogList',
            execute: 'at/function/processexec/execute',
            terminate: 'at/function/processexec/terminate',
        }
        
        export function getProcExecLogList(): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.getProcExecList);
        }
        
        export function execute(command: any): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.execute, command);
        }
        
        export function terminate(command: any) {
            //return nts.uk.request.ajax("at", paths.terminate, command);
            nts.uk.request.ajax("at", paths.terminate, command);
        }
    }
}
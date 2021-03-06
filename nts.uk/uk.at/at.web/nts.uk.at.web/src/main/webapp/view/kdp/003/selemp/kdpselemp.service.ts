module nts.uk.at.view.kdpselemp {
    
    import ajax = nts.uk.request.ajax;
    
    export module service {
        let url = {
            startPage: 'at/record/stamp/management/personal/startPage',
            getStampData: 'at/record/stamp/management/personal/stamp/getStampData',
            getTimeCard: 'at/record/stamp/management/personal/stamp/getTimeCard',
            stampInput: 'at/record/stamp/management/personal/stamp/input',
            getError: 'at/record/stamp/management/personal/getDailyError' 
        }

        export function startPage(): JQueryPromise<any> {
            return ajax("at", url.startPage);
        }

        export function getStampData(data): JQueryPromise<any> {
            return ajax("at", url.getStampData, data);
        }

        export function getTimeCardData(data): JQueryPromise<any> {
            return ajax("at", url.getTimeCard, data);
        }


        export function stampInput(data): JQueryPromise<any> {
            return ajax("at", url.stampInput, data);
        }

        export function getError(data): JQueryPromise<any> {
            return ajax("at", url.getError + "/" + data.pageNo + "/" + data.buttonDisNo);
        }
    }

}


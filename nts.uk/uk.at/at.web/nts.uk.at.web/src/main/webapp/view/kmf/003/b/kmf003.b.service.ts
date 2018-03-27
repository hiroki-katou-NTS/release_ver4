module nts.uk.at.view.kmf003.b.service {
    /**
     *  Service paths
     */
    var servicePath = {
        findByCode: "at/share/lengthofservice/findByCode/{0}",
        findGrantByCode: "at/share/grantholidaytbl/findByCode/{0}/{1}",
        addGrantHdTbl: "at/share/lengthofservice/add",
        calculateGrantDate: "at/share/grantholidaytbl/calculateGrantDate",
        checkData: "ctx/at/share/vacation/setting/annualpaidleave/find/checkkmf003"
    }  
    
    /**
     *  Find data by codes
     */
    export function findByCode(yearHolidayCode: string): JQueryPromise<GrantHolidayTblDto> {
        var path = nts.uk.text.format(servicePath.findByCode, yearHolidayCode);
        return nts.uk.request.ajax(path);
    }  
    
    /**
     *  Find Grant data by codes
     */
    export function findGrantByCode(conditionNo: number, yearHolidayCode: string): JQueryPromise<GrantHolidayTblDto> {
        var path = nts.uk.text.format(servicePath.findGrantByCode, conditionNo, yearHolidayCode);
        return nts.uk.request.ajax(path);
    }  
    
    /**
     *  Add data
     */
    export function addYearHolidayGrant(data: Array<GrantHolidayTblDto>): JQueryPromise<any> {
        var path = nts.uk.text.format(servicePath.addGrantHdTbl);
        return nts.uk.request.ajax("at", path, data);
    }
    
    /**
     *  Calculate grant dates
     */
    export function calculateGrantDate(param: DataTranfer): JQueryPromise<any> {
        var path = nts.uk.text.format(servicePath.calculateGrantDate);
        return nts.uk.request.ajax("at", path, param);
    } 
    
    /**
     *  Check data before load
     */
    export function checkData(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.checkData);
    }
    
    export interface GrantHolidayTblDto {
        grantYearHolidayNo: number,
        conditionNo: number,
        yearHolidayCode: string,
        grantDays: number,
        limitedTimeHdDays: number,
        limitedHalfHdCnt: number,
        lengthOfServiceMonths: number,
        lengthOfServiceYears: number,
        grantReferenceDate: number,
        grantSimultaneity: number
    }
    
    export interface Codes {
        grantYearHolidayNo: number,
        conditionNo: number,
        yearHolidayCode: string,
    }
    
    export interface DataTranfer {
        grantHolidayTblList: Array<GrantHolidayTblDto>,
        useSimultaneousGrant: number, 
        referDate: number, 
        simultaneousGrantDate: number
    }
}

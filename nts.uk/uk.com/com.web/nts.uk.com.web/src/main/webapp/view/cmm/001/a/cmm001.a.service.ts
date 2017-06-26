module cmm001.a.service {
    var paths = {
        getAllCompanys: "ctx/proto/company/findallcompany",
        getCompanyDetail: "ctx/proto/company/findCompanyDetail",
        addCompany: "ctx/proto/company/adddata",
        updateCompany: "ctx/proto/company/updatedata"
    }
    
    /**
     * get list company
     */
    export function getAllCompanys(): JQueryPromise<Array<model.CompanyDto>> {
        let dfd = $.Deferred<Array<model.CompanyDto>>();
        nts.uk.request.ajax(paths.getAllCompanys)
            .done(function(res: Array<model.CompanyDto>) {   
                dfd.resolve(res);
            })
            .fail(function(res: model.CompanyDto) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
    export function getCompanyDetail(companyCd: string): JQueryPromise<model.CompanyDto> {
        let dfd = $.Deferred<model.CompanyDto>();
        nts.uk.request.ajax(paths.getCompanyDetail + "/" + companyCd)
            .done(function(res: model.CompanyDto) {
                dfd.resolve(res);
            })
            .fail(function(res: model.CompanyDto) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
    export function addData(company: model.CompanyDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.addCompany, company).done(function(res: Array<any>) {
            dfd.resolve(res);
        }).fail(function(res: any) {
            nts.uk.ui.dialog.alert(res.messName);
            dfd.reject(res);
        })
        return dfd.promise();
    }

    export function updateData(company: model.CompanyDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.updateCompany, company)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export module model {
        // company
        export class CompanyDto {
            companyCode: string;
            companyName: string;
            companyNameGlobal: string;
            address1: string;
            address2: string;
            addressKana1: string;
            addressKana2: string;
            companyNameAbb: string;
            companyNameKana: string;
            corporateMyNumber: string;
            depWorkPlaceSet: number;
            displayAttribute: number; // cot thu 3
            faxNo: string;
            postal: string;
            presidentName: string;
            presidentJobTitle: string;
            telephoneNo: string;
            termBeginMon: number =0;
            use_Gr_Set: number =0;
            use_Kt_Set: number =0;
            use_Qy_Set: number =0;
            use_Jj_Set: number =0;
            use_Ac_Set: number =0;
            use_Gw_Set: number =0;
            use_Hc_Set: number =0 ;
            use_Lc_Set: number =0;
            use_Bi_Set: number =0;
            use_Rs01_Set: number =0;
            use_Rs02_Set: number =0;
            use_Rs03_Set: number =0;
            use_Rs04_Set: number =0;
            use_Rs05_Set: number =0;
            use_Rs06_Set: number =0;
            use_Rs07_Set: number =0;
            use_Rs08_Set: number =0;
            use_Rs09_Set: number =0;
            use_Rs10_Set: number =0;
            
            constructor(companyCode: string, companyName: string,
                    address1: string, address2: string, addressKana1: string, 
                    addressKana2: string,companyNameAbb: string,
                    companyNameKana: string, corporateMyNumber: string, 
                    depWorkPlaceSet: number, displayAttribute: number, // cot thu 3
                    faxNo: string, postal: string, presidentName: string, 
                    presidentJobTitle: string, telephoneNo: string,
                    termBeginMon: number,useKtSet: number, 
                    useQySet: number, useJjSet: number) {
                
                this.companyCode= companyCode;
                this.companyName = companyName;
                this.companyNameGlobal = "";
                this.companyNameAbb = companyNameAbb;
                this.companyNameKana = companyNameKana;
                this.address1 = (address1);
                this.address2 = (address2);
                this.addressKana1 = (addressKana1);
                this.addressKana2 = (addressKana2);
                this.use_Gr_Set = 0;
                this.use_Kt_Set = useKtSet;
                this.use_Qy_Set = useQySet;
                this.use_Jj_Set = useJjSet;
                this.use_Ac_Set = 0;
                this.use_Gw_Set = 0;
                this.use_Hc_Set = 0;
                this.use_Lc_Set = 0;
                this.use_Bi_Set = 0;
                this.use_Rs01_Set = 0;
                this.use_Rs02_Set = 0;
                this.use_Rs03_Set = 0;
                this.use_Rs04_Set = 0;
                this.use_Rs05_Set = 0;
                this.use_Rs06_Set = 0;
                this.use_Rs07_Set = 0;
                this.use_Rs08_Set = 0;
                this.use_Rs09_Set = 0;
                this.use_Rs10_Set = 0;

            }
        }

    }
}
module nts.uk.pr.view.qmm008.e {

    export module service {

        // Service paths.
        var servicePath = {
            getOfficeDetailData: "pr/insurance/social/find",
            updateOffice: "pr/insurance/social/update",
            regiterOffice: "pr/insurance/social/create",
            removeOffice: "pr/insurance/social/remove"
        };

        /**
         * Function is used to load office detail by office code
         */
        export function getOfficeItemDetail(code: string): JQueryPromise<model.finder.OfficeItemDto> {
            var findPath = servicePath.getOfficeDetailData + "/" + code;
            return nts.uk.request.ajax(findPath);
        }

        /**
         * Function is used to register office data
         */
        export function register(data: model.finder.OfficeItemDto): JQueryPromise<model.finder.OfficeItemDto> {
            return nts.uk.request.ajax(servicePath.regiterOffice, data);
        }

        /**
         * Function is used to update office data
         */
        export function update(data: model.finder.OfficeItemDto): JQueryPromise<model.finder.OfficeItemDto> {
            return nts.uk.request.ajax(servicePath.updateOffice, data);
        }
        
         /**
         * Function is used to remove office by office code
         */
        export function remove(officeCode:string): JQueryPromise<model.finder.OfficeItemDto> {
            var data = {insuranceOfficeCode:officeCode}
            return nts.uk.request.ajax(servicePath.removeOffice,data);
        }
        
         /**
         * Function is used to remove office by office code
         */
        export function saveHistory(officeCode:string): JQueryPromise<model.finder.OfficeItemDto> {
            var data = {insuranceOfficeCode:officeCode}
            return nts.uk.request.ajax(servicePath.removeOffice,data);
        }
        
        /**
        * Model namespace.
        */
        export module model {
            export module finder {
                export class ChooseOption {
                    code: string;
                    name: string;
                }
                //TODO change office item field
                export class OfficeItemDto {
                    companyCode: string;
                    code: string;
                    name: string;
                    shortName: string;
                    picName: string;
                    picPosition: string;
                    potalCode: string;
                    address1st: string;
                    address2nd: string;
                    kanaAddress1st: string;
                    kanaAddress2nd: string;
                    phoneNumber: string;
                    healthInsuOfficeRefCode1st: string;
                    healthInsuOfficeRefCode2nd: string;
                    pensionOfficeRefCode1st: string;
                    pensionOfficeRefCode2nd: string;
                    welfarePensionFundCode: string;
                    officePensionFundCode: string;
                    healthInsuCityCode: string;
                    healthInsuOfficeSign: string;
                    pensionCityCode: string;
                    pensionOfficeSign: string;
                    healthInsuOfficeCode: string;
                    healthInsuAssoCode: string;
                    memo: string;
                    constructor(companyCode: string, code: string, name: string,
                        shortName: string,
                        picName: string,
                        picPosition: string,
                        potalCode: string,
                        address1st: string,
                        address2nd: string,
                        kanaAddress1st: string,
                        kanaAddress2nd: string,
                        phoneNumber: string,
                        healthInsuOfficeRefCode1st: string,
                        healthInsuOfficeRefCode2nd: string,
                        pensionOfficeRefCode1st: string,
                        pensionOfficeRefCode2nd: string,
                        welfarePensionFundCode: string,
                        officePensionFundCode: string,
                        healthInsuCityCode: string,
                        healthInsuOfficeSign: string,
                        pensionCityCode: string,
                        pensionOfficeSign: string,
                        healthInsuOfficeCode: string,
                        healthInsuAssoCode: string,
                        memo: string) {
                        this.companyCode = companyCode; this.code = code; this.name = name;
                        this.shortName = shortName; this.picName = picName; this.picPosition = picPosition;
                        this.potalCode = potalCode; this.address1st = address1st;
                        this.address2nd = address2nd; this.kanaAddress1st = kanaAddress1st; this.kanaAddress2nd = kanaAddress2nd;
                        this.phoneNumber = phoneNumber; this.healthInsuOfficeRefCode1st = healthInsuOfficeRefCode1st; this.healthInsuOfficeRefCode2nd = healthInsuOfficeRefCode2nd;
                        this.pensionOfficeRefCode1st = pensionOfficeRefCode1st; this.pensionOfficeRefCode2nd = pensionOfficeRefCode2nd;
                        this.welfarePensionFundCode = welfarePensionFundCode; this.officePensionFundCode = officePensionFundCode;
                        this.healthInsuCityCode = healthInsuCityCode; this.healthInsuOfficeSign = healthInsuOfficeSign;
                        this.pensionCityCode = pensionCityCode; this.pensionOfficeSign = pensionOfficeSign;
                        this.healthInsuOfficeCode = healthInsuOfficeCode; this.healthInsuAssoCode = healthInsuAssoCode;
                        this.memo = memo;
                    }
                }
            }
        }

    }
}

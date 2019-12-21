class EmployeeModel {
    flag: boolean
    retirementStatus: number;
    registrationStatus: string;
    desiredWorkingCourse: string;
    employeeCode: string;
    employeeName: string;
    employeeKanaName: string;
    employeeDob: string;
    employeeAge: string;
    department: string;
    employment: string;
    hireDate: string;
    retirementDate: string;

    constructor() {

    }
}

interface IEmployee {
    image: string;
    code: string;
    name: string;
    kanaName: string;
    sex: string;
    dob: string;
    age: string;
    department: string;
    position: string;
    employment: string;
}

class ScreenSetting {
    enableRetirementAge: Boolean;
    constructor() {

    }
}

class RetirementAgeSetting {
    code: string;
    name: string;

    constructor(code: string, name: string) {
        this.code = code;
        this.name = name;
    }
}

interface IStartPageDto {
    dateDisplaySettingPeriod: IDateDisplaySettingPeriod;
    retirementCourses: Array<IRetirementCourses>;
    referEvaluationItems: Array<any>;
}

interface IDateDisplaySettingPeriod {
    periodStartdate: String;
    periodEnddate: String;
}

interface IRetirementCourses {
    employmentCode: string;
    employmentName: string;
    retirePlanCourseClass: 0;
    retirementAge: string;
    retireDateBase: string;
    retireDateTerm: IRetireDateTerm;
    retirePlanCourseId: number;
    retirePlanCourseCode: string;
    retirePlanCourseName: string;
    durationFlg: boolean;
}

interface IRetireDateTerm {
    retireDateTerm: string;
    retireDateSettingDate: string;
}

class ItemModel {
    code: string;
    name: string;

    constructor(code: string, name: string) {
        this.code = code;
        this.name = name;
    }
}

interface IDateRange {
    startDate: string;
    endDate: string;
}

interface IDepartment {
    workplaceId: string;
    hierarchyCode: string;
    name: string;
    code: string;
    lever: number;
    nodeText: string;
}

interface IEmployment {
    code: string;
    name: string;
}

class SearchFilterModel {
    includingReflected: KnockoutObservable<boolean> = ko.observable(false);
    retirementAgeDesignation: KnockoutObservable<boolean> = ko.observable(false);
    retirementPeriod: KnockoutObservable<IDateRange> = ko.observable({startDate:'', endDate: ''});
    department: KnockoutObservable<IDepartment> = ko.observable([]);
    departmentDisplay: KnockoutObservable<string> = ko.computed(() => {
        return this.department().map(function(elem){
            return elem.name;
        }).join(', ');
    });
    selectAllDepartment: KnockoutObservable<boolean> = ko.observable(false);
    employment: KnockoutObservable<Array[IEmployment]> = ko.observable([]);
    employmentDisplay: KnockoutObservable<string> = ko.computed(() => {
        return this.employment().map(function(elem){
            return elem.name;
        }).join(', ');
    })
    selectAllEmployment: KnockoutObservable<boolean> = ko.observable(false);
    confirmCheckRetirementPeriod: KnockoutObservable<boolean> = ko.observable(false);
    retirementCourses: KnockoutObservable<IRetirementCourses>;
    retirementAges: KnockoutObservable<Array<RetirementAgeSetting>> = ko.observable([]);
    selectedRetirementAge: KnockoutObservable<string> = ko.observable({});
    constructor() {

    }
}

class ISearchParams {
    confirmCheckRetirementPeriod: boolean;
    startDate: Date;
    endDate: Date;
    retirementAgeSetting: boolean;
    retirementAge: number;
    allSelectDepartment: boolean;
    selectDepartment: Array<string>;
    allSelectEmployment: boolean;
    selectEmployment: Array<string>;
    includingReflected: boolean;
    constructor(param: SearchFilterModel) {
        this.includingReflected = param.includingReflected();
        this.retirementAgeSetting = param.retirementAgeDesignation();
        if(this.retirementAgeSetting) {
            this.retirementAge = parseInt(param.selectedRetirementAge());
        }
        this.startDate = param.retirementPeriod().startDate;
        this.endDate = param.retirementPeriod().endDate;
        this.allSelectDepartment = param.selectAllDepartment();
        this.selectDepartment = _.map(param.department(), (d) => {return d.name;});
        this.allSelectEmployment = param.selectAllEmployment();
        this.selectEmployment = _.map(param.employment(), (d) => {return d.name;});
        this.confirmCheckRetirementPeriod = param.confirmCheckRetirementPeriod();
    }
}

interface ISearchResult {
    interView: IInterviewSummary;
    retiredEmployees: Array<PlannedRetirementDto>;
    employeeImports: Array<IEmployeeInformationImport>;
}

interface IEmployeeInformationImport {
    employeeId : string;
    employeeCode : string;
    businessName : string;
    businessNameKana : string;
    workplace: WorkPlace;
    classification: Classification;
    department: Department;
    position: Position;
    employment: Employment;
    employmentCls: number;
    personID: string;
    employeeName: string;
    avatarFile: FacePhotoFile;
    birthday: string;
    age: number;
}

interface FacePhotoFile {
    thumbnailFileID: string;
    facePhotoFileID: string;
}

interface Employment {
    employmentCode: string;
    employmentName: string;
}

interface Position {
    positionId: string;
    positionCode: string;
    positionName: string;
}

interface Department {
    departmentCode: string;
    departmentName: string;
    departmentDisplayName: string;
}

interface WorkPlace {
    workplaceId: string;
    workplaceCode: string;
    workplaceGenericName: string;
    workplaceName: string;
}



interface Classification {
    classificationCode: string;
    classificationName: string;
}

class PlannedRetirementDto {
    pId : string;
    sId : string;
    scd : string;
    businessName : string;
    businessnameKana : string;
    birthday : string;
    departmentId : string;
    departmentCode : string;
    departmentName : string;
    jobTitleId : string;
    jobTitleCd : string;
    jobTitleName : string;
    employmentCode : string;
    employmentName : string;
    age : number;
    dateJoinComp : string;
    retirementDate : string;
    releaseDate : string;
    inputDate: string;
    pendingFlag: number;
    status:number;
    dst_HistId: string;
    desiredWorkingCourseCd: string;
    extendEmploymentFlg: string;
    companyId: string;
    companyCode: string;
    contractCode: string;
    workId: string;
    workName: string;
    desiredWorkingCourseId: string;
    desiredWorkingCourseName: string;
    retirementCategory: string;
    notificationCategory: number;
    retirementReasonCtgID1: number;
    retirementReasonCtgCd1: string;
    retirementReasonCtgName1: string;
    hrEvaluation1 : string;
    hrEvaluation2 : string;
    hrEvaluation3 : string;
    healthStatus1 : string;
    healthStatus2 : string;
    healthStatus3 : string;
    stressStatus1 : string;
    stressStatus2 : string;
    stressStatus3 : string;

    constructor() {
        
    }
}

interface IInterviewSummary {
    listInterviewRecordAvailability: Array<IInterviewRecordAvailability>;
}

interface IInterviewRecordAvailability {
    employeeID: string;
    isPresence: boolean;
}
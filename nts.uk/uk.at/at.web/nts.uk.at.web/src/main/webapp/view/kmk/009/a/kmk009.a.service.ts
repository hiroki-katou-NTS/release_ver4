module nts.uk.at.view.kmk009.a {
    export module service {

        var paths = {
            getAllTotalTimes: "at/shared/scherec/totaltimes/getallitem",
            getAllTotalTimesDetail: "at/shared/scherec/totaltimes/getdetail/",
            totalClassification: "at/shared/scherec/totaltimes/find/totalclassification",
            totalUseEnum: "at/shared/scherec/totaltimes/find/totalUseEnum",
            saveAllTotalTimes: "at/shared/scherec/totaltimes/save",
            findByIdWorkType: "at/share/worktype/findById",
            findByIdWorkTime: "at/shared/worktimesetting/findById",
            findByIdListWorkTypes: "at/share/worktype/get_possible_work_type_with_no_master",
            findByIdlistWorkTimes: "at/shared/worktimesetting/find_by_code_with_no_master",
            findAlldWorkType: "at/share/worktype/findAll",
            findAllWorkTime: "at/shared/worktimesetting/findAll",
            findAllDailyAttendanceItem: "at/record/businesstype/attendanceItem/getAttendanceItems",
            findAllAttendanceItem: "at/function/employmentfunction/findAll"
        }

        /**
        * get all total times
        */
        export function getAllTotalTimes(): JQueryPromise<Array<model.TotalTimesDto>> {
            return nts.uk.request.ajax("at", paths.getAllTotalTimes);
        }
        export function getTotalClsEnum(): JQueryPromise<Array<model.Enum>> {
            return nts.uk.request.ajax(paths.totalClassification);
        }
        
        export function getTotalUseEnum(): JQueryPromise<Array<model.EnumUse>> {
            return nts.uk.request.ajax(paths.totalUseEnum);
        }

        export function getAllTotalTimesDetail(totalTimesNo: number): JQueryPromise<model.TotalTimesDetailDto> {
            return nts.uk.request.ajax("at", paths.getAllTotalTimesDetail + totalTimesNo);
        }
        /**
       * save
       */
        export function saveAllTotalTimes(command: model.TotalTimesDetailDto): JQueryPromise<void> {
            return nts.uk.request.ajax("at", paths.saveAllTotalTimes, command);
        }

        /**
            * findByIdWorkTime
           */
        export function findByIdWorkTime(workTimeCode: string): JQueryPromise<model.WorkTimeDto> {
            return nts.uk.request.ajax("at", paths.findByIdWorkTime + '/' + workTimeCode);
        }


        /**
         * findByIdWorkType
        */
        export function findByIdWorkType(workTypeCode: string): JQueryPromise<model.WorkTypeDto> {
            return nts.uk.request.ajax("at", paths.findByIdWorkType + '/' + workTypeCode);
        }

        /**
          * findListByIdWorkTime
           */
        export function findListByIdWorkTimes(workTimeCodes: Array<string>): JQueryPromise<Array<model.WorkTimeDto>> {
            return nts.uk.request.ajax(paths.findByIdlistWorkTimes, workTimeCodes);
        }


        /**
         * findListByIdWorkType
        */
        export function findListByIdWorkTypes(workTypeCodes: string[]): JQueryPromise<Array<model.WorkTypeDto>> {
            return nts.uk.request.ajax("at", paths.findByIdListWorkTypes, workTypeCodes);
        }

        /**
         * findListByIdWorkTime
          */
        export function findAllWorkTimes(): JQueryPromise<Array<model.WorkTimeDto>> {
            return nts.uk.request.ajax(paths.findAllWorkTime);
        }


        /**
         * findListByIdWorkType
        */
        export function findAllWorkTypes(): JQueryPromise<Array<model.WorkTypeDto>> {
            return nts.uk.request.ajax("at", paths.findAlldWorkType);
        }

        /**
         * call service find all daily attendance item
         */
        export function findAllDailyAttendanceItem(): JQueryPromise<model.DailyAttendanceItemDto[]> {
            return nts.uk.request.ajax('at', paths.findAllDailyAttendanceItem);
        }
        
        /**
         * call service find all daily attendance item
         */
        export function findAllAttendanceItem(): JQueryPromise<model.DailyAttendanceItemDto[]> {
            return nts.uk.request.ajax('at', paths.findAllAttendanceItem);
        }


        export module model {
            
            export interface TotalTimesDto {
                totalCountNo: number;
                summaryAtr: number;
                useAtr: number;
                totalTimesName: string;
            }
            export interface TotalSubjectsDto{
                workTypeCode: string;
                workTypeAtr: number;    
            }
            
            export interface TotalTimesDetailDto {
                totalCountNo: number;
                countAtr: number;
                useAtr: number;
                totalTimesName: string;
                totalTimesABName: string;
                summaryAtr: number;
                totalCondition: TotalConditionDto;
                listTotalSubjects: TotalSubjectsDto[];
            }
            
            export interface TotalConditionDto {
                upperLimitSettingAtr: number;
                lowerLimitSettingAtr: number;
                thresoldUpperLimit: number;
                thresoldLowerLimit: number;
            }

            export class Enum {
                value: number;
                fieldName: string;
                localizedName: string;

                constructor(value: number, fieldName: string, localizedName: string) {
                    this.value = value;
                    this.fieldName = fieldName;
                    this.localizedName = localizedName;
                }
            }
            
            export class EnumUse {
                value: number;
                fieldName: string;
                localizedName: string;

                constructor(value: number, fieldName: string, localizedName: string) {
                    this.value = value;
                    this.fieldName = fieldName;
                    this.localizedName = localizedName;
                }
            }

            export interface WorkTypeDto {
                workTypeCode: string;
                name: string;
            }

            export interface WorkTimeDto {
                code: string;
                name: string;

            }
            
            export interface DailyAttendanceItemDto {
                attendanceItemId: number;
                attendanceItemName: string;
            }
        }
    }
}
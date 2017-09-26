module nts.uk.at.view.kmk006.a {
    export module service {
        var paths = {

            findEnumUnitAutoCal: "ctx/at/schedule/shift/autocalunit/find/autocalunit",
            findEnumAutoCalAtrOvertime: "ctx/at/schedule/shift/autocal/find/autocalatrovertime",
            findEnumUseClassification: "ctx/at/schedule/shift/autocal/find/autocaluseclassification",
            findEnumTimeLimitUpperLimitSetting: "ctx/at/schedule/shift/autocal/find/autocaltimelimitsetting",
            findEnumUseUnitOvertimeSetting: "ctx/at/schedule/shift/autocal/find/autocaluseunitovertimesetting",
            getComAutoCal: "ctx/at/schedule/shift/autocalcom/getautocalcom",
            saveComAutoCal: "ctx/at/schedule/shift/autocalcom/save",
            saveJobAutoCal: "ctx/at/schedule/shift/autocaljob/save",
            saveWkpAutoCal: "ctx/at/schedule/shift/autocalwkp/save",
            saveWkpJobAutoCal: "ctx/at/schedule/shift/autocalwkpjob/save",
            getJobAutoCal: "ctx/at/schedule/shift/autocaljob/getautocaljob",
            getWkpAutoCal: "ctx/at/schedule/shift/autocalwkp/getautocalwkp",
            getWkpJobAutoCal: "ctx/at/schedule/shift/autocalwkpjob/getautocalwkpjob",
            deleteJobAutoCal: "ctx/at/schedule/shift/autocaljob/delete",
            deleteWkpAutoCal: "ctx/at/schedule/shift/autocalwkp/delete",
            deleteWkpJobAutoCal: "ctx/at/schedule/shift/autocalwkpjob/delete"
        }

        /**
        * delete divergence reason
       */
        export function deleteJobAutoCal(jobId: string): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.deleteJobAutoCal + '/' + jobId);
        }


        /**
        * delete divergence reason
       */
        export function deleteWkpAutoCal(wkpId: string): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.deleteWkpAutoCal + '/' + wkpId);
        }


        /**
        * delete divergence reason
       */
        export function deleteWkpJobAutoCal(wkpId: string, jobId: string): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.deleteWkpJobAutoCal + '/' + wkpId + '/' + jobId);
        }


        export function findEnumAutoCalAtrOvertime(): JQueryPromise<Array<model.Enum>> {
            return nts.uk.request.ajax(paths.findEnumAutoCalAtrOvertime);
        }

        export function findEnumUseClassification(): JQueryPromise<Array<model.Enum>> {
            return nts.uk.request.ajax(paths.findEnumUseClassification);
        }

        export function findEnumTimeLimitUpperLimitSetting(): JQueryPromise<Array<model.Enum>> {
            return nts.uk.request.ajax(paths.findEnumTimeLimitUpperLimitSetting);
        }

        export function findEnumUseUnitOvertimeSetting(): JQueryPromise<Array<model.Enum>> {
            return nts.uk.request.ajax(paths.findEnumUseUnitOvertimeSetting);
        }

        /**
        * save
        */
        export function saveComAutoCal(command: model.ComAutoCalSettingDto): JQueryPromise<void> {
            return nts.uk.request.ajax("at", paths.saveComAutoCal, command);
        }
        /**
      * save
      */
        export function saveJobAutoCal(command: model.JobAutoCalSettingDto): JQueryPromise<void> {
            return nts.uk.request.ajax("at", paths.saveJobAutoCal, command);
        }
        /**
      * save
      */
        export function saveWkpAutoCal(command: model.WkpAutoCalSettingDto): JQueryPromise<void> {
            return nts.uk.request.ajax("at", paths.saveWkpAutoCal, command);
        }
        /**
      * save
      */
        export function saveWkpJobAutoCal(command: model.WkpJobAutoCalSettingDto): JQueryPromise<void> {
            return nts.uk.request.ajax("at", paths.saveWkpJobAutoCal, command);
        }

        export function getComAutoCal(): JQueryPromise<model.ComAutoCalSettingDto> {
            return nts.uk.request.ajax("at", paths.getComAutoCal);
        }

        export function getJobAutoCal(jobId: string): JQueryPromise<model.JobAutoCalSettingDto> {
            return nts.uk.request.ajax("at", paths.getJobAutoCal + '/' + jobId);
        }

        export function getWkpAutoCal(wkpId: string): JQueryPromise<model.WkpAutoCalSettingDto> {
            return nts.uk.request.ajax("at", paths.getWkpAutoCal + '/' + wkpId);
        }
        export function getWkpJobAutoCal(wkpId: string, jobId: string): JQueryPromise<model.WkpJobAutoCalSettingDto> {
            return nts.uk.request.ajax("at", paths.getWkpJobAutoCal + '/' + wkpId + '/' + jobId);
        }
        export function getEnumUnitAutoCal(): JQueryPromise<model.UnitAutoCalSettingDto> {
            return nts.uk.request.ajax("at", paths.findEnumUnitAutoCal);
        }




        export module model {
            //modelauto

            export interface WkpJobAutoCalSettingDto {
                wkpId: string;
                jobId: string;
                normalOTTime: AutoCalOvertimeSettingDto;
                flexOTTime: AutoCalFlexOvertimeSettingDto;
                restTime: AutoCalRestTimeSettingDto;
            }

            export interface WkpAutoCalSettingDto {
                wkpId: string;
                normalOTTime: AutoCalOvertimeSettingDto;
                flexOTTime: AutoCalFlexOvertimeSettingDto;
                restTime: AutoCalRestTimeSettingDto;
            }

            export interface JobAutoCalSettingDto {
                jobId: string;
                normalOTTime: AutoCalOvertimeSettingDto;
                flexOTTime: AutoCalFlexOvertimeSettingDto;
                restTime: AutoCalRestTimeSettingDto;
            }

            export interface ComAutoCalSettingDto {
                normalOTTime: AutoCalOvertimeSettingDto;
                flexOTTime: AutoCalFlexOvertimeSettingDto;
                restTime: AutoCalRestTimeSettingDto;
            }

            export interface AutoCalFlexOvertimeSettingDto {
                flexOtTime: AutoCalSettingDto;
                flexOtNightTime: AutoCalSettingDto;
            }

            export interface AutoCalRestTimeSettingDto {
                restTime: AutoCalSettingDto;
                lateNightTime: AutoCalSettingDto;
            }
            export interface AutoCalOvertimeSettingDto {
                earlyOtTime: AutoCalSettingDto;
                earlyMidOtTime: AutoCalSettingDto;
                normalOtTime: AutoCalSettingDto;
                normalMidOtTime: AutoCalSettingDto;
                legalOtTime: AutoCalSettingDto;
                legalMidOtTime: AutoCalSettingDto;
            }

            export interface AutoCalSettingDto {
                upLimitOtSet: number;
                calAtr: number;
            }

            export interface UnitAutoCalSettingDto {
                useJobSet: boolean;
                useWkpSet: boolean;
                useJobwkpSet: boolean;
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
        }
    }



}
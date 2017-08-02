module nts.uk.at.view.ksm003.a {
    export module service {
        var paths = {
            getAllPattCalender: "ctx/at/schedule/shift/pattern/daily/getall",
            addPattCalender: "ctx/at/schedule/shift/pattern/daily/save",
            getPatternValByPatternCd: "ctx/at/schedule/shift/pattern/daily/find",
            deleteDailyPattern: "ctx/at/schedule/shift/pattern/daily/delete"
        }

        /**
        * get all Patt Calender
        */
        export function getAllPattCalender(): JQueryPromise<Array<model.DailyPatternItemDto>> {
            return nts.uk.request.ajax("at", paths.getAllPattCalender);
        }
        /**
        * add Patt Calender
        */
        export function addPattCalender(dto: model.DailyPatternDetailDto): JQueryPromise<Array<model.DailyPatternDetailDto>> {
            return nts.uk.request.ajax("at", paths.addPattCalender, dto);
        }

        /**
      * get Patt Val
      */
        export function getPatternValByPatternCd(patternCd: string): JQueryPromise<model.DailyPatternDetailDto> {
            return nts.uk.request.ajax("at", paths.getPatternValByPatternCd + '/' + patternCd);
        }
        /**
         * delete divergence reason
        */
        export function deleteDailyPattern(patternCd: string): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.deleteDailyPattern + '/' + patternCd);
        }

        export module model {

            export interface DailyPatternItemDto {
                patternCode: string;
                patternName: string;
            }

            export interface DailyPatternDetailDto {
                patternCode: string;
                patternName: string;
                itemDailyPatternVal: Array<DailyPatternValDto>;
            }

            export interface DailyPatternValDto {
                dispOrder: number;
                workTypeSetCd: string;
                workingHoursCd: string;
                days: number;
            }
        }
    }
}
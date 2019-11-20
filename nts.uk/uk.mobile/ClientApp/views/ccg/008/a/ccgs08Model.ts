export class BaseDto {
  public title?: String;
  public description?: String;
  public visible?: Boolean;
  public tableConfigs?: any;
  public errorMessage?: String;

  constructor() {
    this.visible = false;
    this.errorMessage = '';
  }
}

// server operation
export class ServerAlertDto extends BaseDto {
  public company: SystemOperationSetting;
  public system: SystemOperationSetting;
}

class SystemOperationSetting extends BaseDto {
  public companyCd?: String;
  public contractCd: String;
  public stopMessage: String;
  public stopMode: Number;
  public systemStatus: Number;
  public usageStopMessage: String;
}
// end server operation

// server notifi
export class DisplayNotifiDto extends BaseDto {
  public checkDailyErrorA2_2: boolean;
  public ktg002: boolean;
}
// end server notifi

// overtime agreement time detail
export class AgreementTimeDetail extends BaseDto {
  public employeeId?: String;
  constructor() {
    super();
    this.tableConfigs = {
      headers: [
        { label: 'CCGS08_32', key: 'month', html: true },
        { label: 'CCGS08_7', key: 'time1', filter: 'timept' },
        { label: 'CCGS08_8', key: 'time2', filter: 'timept' },
        { label: '', key: 'time3', html: true }
      ],
      search: false,
      // can custom table by add new class
      tableClass: 'overtime-working-hours',
      headerClass: 'bg-weekdays uk-bg-lighten-gray',
      rowClass: 'bg-schedule-work-by-dow',
      items: [],
      noDataMessage: '',
      loading: false
    };
  }
}

export class OvertimeLaborInforDto {
    public afterAppReflect: OverTimeInfo;
    public confirmed: OverTimeInfo;
    public empName: String;
    public employeeCD: String;
    public errorMessage?: String;
}

export class OverTimeInfo {
  public agreementTime: number;
  public exceptionLimitAlarmTime: number;
  public exceptionLimitErrorTime: number;
  public limitAlarmTime: number;
  public limitErrorTime: number;
  public status: number;
}
// end overtime agreement time detail

// ktg029
export class TimeStatus extends BaseDto {
  constructor() {
    super();

    this.tableConfigs = {
      search: false,
      // can custom table by add new class
      tableClass: 'table-striped table-bordered time-status-table',
      items: [],
      noDataMessage: '',
      loading: false
    };
  }
}

export class WidgetDisplayItemType {  
  public overTime?: Number;
  public holidayInstruction?: Number;
  public approved?: Number;
  public unApproved?: Number;
  public deniedNo?: Number;
  public remand?: Number;
  public appDeadlineMonth?: any;
  public presenceDailyPer?: Boolean;
  public overtimeHours?: TimeDto;
  public flexTime?: TimeDto;
  public restTime?: TimeDto;
  public nightWorktime?: TimeDto;
  public lateRetreat?: Number;
  public earlyRetreat?: Number;
  public yearlyHoliday?: YearlyHolidayDto;
  public reservedYearsRemainNo?: RemainingNumber;
  public remainAlternationNoDay?: Number;
  public remainsLeft?: Number;
  public publicHDNo?: Number;
  public childRemainNo?: RemainingNumber;
  public careLeaveNo?: RemainingNumber;
  public sphdramainNo?: Array<RemainingNumber>;
  public extraRest?: TimeDto;
 
}

class YearlyHolidayDto {
  public afterGrantDateInfo: DateInfoDto;
  public grantedDaysNo: Number;
  public nextGrantDateInfo: DateInfoDto;
  public nextGrantDate: String;
  public attendanceRate: Number;
  public calculationMethod: Number;
  public nextTime: String;
  public nextTimeInfo: DateInfoDto;
  public showGrantDate: Boolean;
  public useSimultaneousGrant: Number;
  public workingDays: number;
}

export class DateInfoDto {
  public day: Number;
  public hours: TimeDto;
  public remaining: Number;
  public timeYearLimit: TimeDto;
}

export class TimeDto {
  public hours: Number;
  public min: Number;
}

export class DisplayItemType {
  public name: String;
  public value: String;
}

class RemainingNumber {
  public name: String;
  public before: Number;
  public after: Number;
  public grantDate: String;
  public showAfter: Boolean;
  constructor() {}
}
// end ktg029




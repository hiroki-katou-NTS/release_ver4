module FullCalendar {
    class AllDaySplitter extends Splitter {
        getKeyInfo(): {
            allDay: {};
            timed: {};
        };
        getKeysForDateSpan(dateSpan: DateSpan): string[];
        getKeysForEventDef(eventDef: EventDef): string[];
    }

    class TimeColsSlatsCoords {
        positions: PositionCache;
        private dateProfile;
        private slatMetas;
        constructor(positions: PositionCache, dateProfile: DateProfile, slatMetas: TimeSlatMeta[]);
        safeComputeTop(date: Date): number;
        computeDateTop(when: Date, startOfDayDate?: Date): number;
        computeTimeTop(duration: Duration): number;
    }

    interface TimeSlatMeta {
        date: Date;
        time: Duration;
        key: string;
        isoTimeStr: string;
        isLabeled: boolean;
    }

    function buildSlatMetas(slotMinTime: Duration, slotMaxTime: Duration, explicitLabelInterval: Duration | null, slotDuration: Duration, dateEnv: DateEnv): TimeSlatMeta[];

    interface TimeColsViewState {
        slatCoords: TimeColsSlatsCoords | null;
    }

    abstract class TimeColsView extends DateComponent<ViewProps, TimeColsViewState> {
        protected allDaySplitter: AllDaySplitter;
        protected headerElRef: RefObject<HTMLTableCellElement>;
        private rootElRef;
        private scrollerElRef;
        state: {
            slatCoords: any;
        };
        renderSimpleLayout(headerRowContent: VNode | null, allDayContent: ((contentArg: ChunkContentCallbackArgs) => VNode) | null, timeContent: ((contentArg: ChunkContentCallbackArgs) => VNode) | null): createElement.JSX.Element;
        renderHScrollLayout(headerRowContent: VNode | null, allDayContent: ((contentArg: ChunkContentCallbackArgs) => VNode) | null, timeContent: ((contentArg: ChunkContentCallbackArgs) => VNode) | null, colCnt: number, dayMinWidth: number, slatMetas: TimeSlatMeta[], slatCoords: TimeColsSlatsCoords | null): createElement.JSX.Element;
        handleScrollTopRequest: (scrollTop: number) => void;
        getAllDayMaxEventProps(): {
            dayMaxEvents: number | boolean;
            dayMaxEventRows: number | false;
        };
        renderHeadAxis: (frameHeight?: CssDimValue) => createElement.JSX.Element;
        renderTableRowAxis: (rowHeight?: number) => createElement.JSX.Element;
        handleSlatCoords: (slatCoords: TimeColsSlatsCoords) => void;
    }

    class DayTimeColsView extends TimeColsView {
        private buildTimeColsModel;
        private buildSlatMetas;
        render(): createElement.JSX.Element;
    }

    function buildTimeColsModel(dateProfile: DateProfile, dateProfileGenerator: DateProfileGenerator): DayTableModel;

    interface TimeColsSeg extends Seg {
        col: number;
        start: Date;
        end: Date;
    }

    interface DayTimeColsProps {
        dateProfile: DateProfile;
        dayTableModel: DayTableModel;
        axis: boolean;
        slotDuration: Duration;
        slatMetas: TimeSlatMeta[];
        businessHours: EventStore;
        eventStore: EventStore;
        eventUiBases: EventUiHash;
        dateSelection: DateSpan | null;
        eventSelection: string;
        eventDrag: EventInteractionState | null;
        eventResize: EventInteractionState | null;
        tableColGroupNode: VNode;
        tableMinWidth: CssDimValue;
        clientWidth: number | null;
        clientHeight: number | null;
        expandRows: boolean;
        onScrollTopRequest?: (scrollTop: number) => void;
        forPrint: boolean;
        onSlatCoords?: (slatCoords: TimeColsSlatsCoords) => void;
    }

    class DayTimeCols extends DateComponent<DayTimeColsProps> {
        private buildDayRanges;
        private slicer;
        private timeColsRef;
        render(): createElement.JSX.Element;
        handleRootEl: (rootEl: HTMLDivElement | null) => void;
        queryHit(positionLeft: number, positionTop: number): Hit;
    }

    function buildDayRanges(dayTableModel: DayTableModel, dateProfile: DateProfile, dateEnv: DateEnv): DateRange[];

    class DayTimeColsSlicer extends Slicer<TimeColsSeg, [DateRange[]]> {
        sliceRange(range: DateRange, dayRanges: DateRange[]): TimeColsSeg[];
    }

    const OPTION_REFINERS: {
        allDaySlot: BooleanConstructor;
    };

    type ExtraOptionRefiners = typeof OPTION_REFINERS;

    interface BaseOptionRefiners extends ExtraOptionRefiners { }

    interface TimeColsProps {
        cells: TableCellModel[];
        dateProfile: DateProfile;
        slotDuration: Duration;
        nowDate: Date;
        todayRange: DateRange;
        businessHourSegs: TimeColsSeg[];
        bgEventSegs: TimeColsSeg[];
        fgEventSegs: TimeColsSeg[];
        dateSelectionSegs: TimeColsSeg[];
        eventSelection: string;
        eventDrag: EventSegUiInteractionState | null;
        eventResize: EventSegUiInteractionState | null;
        rootElRef?: Ref<HTMLDivElement>;
        tableColGroupNode: VNode;
        tableMinWidth: CssDimValue;
        clientWidth: number | null;
        clientHeight: number | null;
        expandRows: boolean;
        nowIndicatorSegs: TimeColsSeg[];
        onScrollTopRequest?: (scrollTop: number) => void;
        forPrint: boolean;
        axis: boolean;
        slatMetas: TimeSlatMeta[];
        onSlatCoords?: (slatCoords: TimeColsSlatsCoords) => void;
    }

    interface TimeColsState {
        slatCoords: TimeColsSlatsCoords | null;
    }

    class TimeCols extends BaseComponent<TimeColsProps, TimeColsState> {
        private processSlotOptions;
        private scrollResponder;
        private colCoords;
        state: {
            slatCoords: any;
        };
        render(): createElement.JSX.Element;
        componentDidMount(): void;
        componentDidUpdate(prevProps: TimeColsProps): void;
        componentWillUnmount(): void;
        handleScrollRequest: (request: ScrollRequest) => boolean;
        handleColCoords: (colCoords: PositionCache | null) => void;
        handleSlatCoords: (slatCoords: TimeColsSlatsCoords | null) => void;
        positionToHit(positionLeft: any, positionTop: any): {
            col: any;
            dateSpan: {
                range: {
                    start: Date;
                    end: Date;
                };
                allDay: boolean;
            };
            dayEl: HTMLElement;
            relativeRect: {
                left: any;
                right: any;
                top: any;
                bottom: any;
            };
        };
    }
}
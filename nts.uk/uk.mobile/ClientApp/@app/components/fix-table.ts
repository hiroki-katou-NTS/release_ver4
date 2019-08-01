import { Vue } from '@app/provider';
import { component, Prop } from '@app/core/component';

@component({
    template: `<div class="table-container">
        <div class="table-header">
            <button ref="previous" class="btn btn-secondary btn-sm" v-on:click="previous" disabled>前項</button>
            <button ref="next" class="btn btn-secondary btn-sm" v-on:click="next">次項</button>
            <table v-bind:class="tableClass">
                <tbody></tbody>
            </table>
        </div>
        <div class="table-body">
            <table v-bind:class="tableClass"
                v-on:touchstart="handleTouchStart"
                v-on:touchend="handleTouchEnd">
                <slot/>
            </table>
        </div>
        <div class="table-footer">
            <table v-bind:class="tableClass">
                <tbody></tbody>
            </table>
        </div>

    </div>`
})
export class FixTableComponent extends Vue {

    @Prop({default: 'table table-bordered table-sm m-0'})
    public tableClass: string;

    @Prop({ default: 4 })
    public displayColumns: number;

    @Prop({ default: 5 })
    public rowNumber: number;

    public startDisplayIndex: number = 1;
    public oldStartDisplayIndex: number = 1;

    private headerTable: HTMLTableElement = null;
    private bodyTable: HTMLTableElement = null;
    private footerTable: HTMLTableElement = null;

    private xDown: number = null;
    private yDown: number = null;

    public mounted() {
        this.bodyTable = this.$el.querySelector('.table-container .table-body table') as HTMLTableElement;
        this.headerTable = this.$el.querySelector('.table-container .table-header table') as HTMLTableElement;
        this.footerTable = this.$el.querySelector('.table-container .table-footer table') as HTMLTableElement;

        this.moveTheadAndTfoot();

        this.hiddenColumns();

        this.addPrevNextButtons();

        this.setStyleOfTableBody();
    }

    private moveTheadAndTfoot() {
        this.headerTable.tHead = this.bodyTable.tHead;
        this.footerTable.tFoot = this.bodyTable.tFoot;
    }

    private setStyleOfTableBody() {
        this.$nextTick(() => {
            let height: number = (this.bodyTable.tBodies[0].firstChild.firstChild as HTMLTableCellElement).offsetHeight;

            let tableBodyDiv = this.$el.querySelector('.table-container .table-body') as HTMLDivElement;
            tableBodyDiv.style.height = `${height * this.rowNumber}px`;
            tableBodyDiv.style.overflowY = 'scroll';

            this.resize();

        });
    }

    private resize() {
        this.resizeHeader();
        this.resizeFooter();
    }

    private resizeFooter() {
        // resize header
        let columns = this.bodyTable.tBodies[0].children[0].children;
        let maps = Array.from(columns).map(
            (c: HTMLTableCellElement) => c.offsetWidth
        );

        let footerColumns = this.footerTable.tFoot.children[0].children;
        for (let i = 0; i < footerColumns.length; i++) {
            (footerColumns[i] as HTMLElement).style.width = `${maps[i]}px`;
        }
    }

    private resizeHeader() {
        // resize header
        let columns = this.bodyTable.tBodies[0].children[0].children;
        let maps = Array.from(columns).map(
            (c: HTMLTableCellElement) => c.offsetWidth
        );

        let headerColumns = this.headerTable.tHead.children[0].children;
        for (let i = 0; i < headerColumns.length; i++) {
            (headerColumns[i] as HTMLElement).style.width = `${maps[i]}px`;
        }
    }

    private hiddenColumns() {
        let displayColumns = this.displayColumns;

        let headerColums = this.headerTable.tHead.firstChild.childNodes;
        for (let i = displayColumns + 1; i < headerColums.length - 1; i++) {
            (headerColums[i] as HTMLTableHeaderCellElement).classList.add('d-none');
        }

        let rows = this.bodyTable.tBodies[0].children as HTMLCollection;
        for (let row of rows) {
            for (let i = displayColumns + 1; i < headerColums.length - 1; i++) {
                (row.childNodes[i] as HTMLTableCellElement).classList.add('d-none');
            }
        }

        let footerColums = this.footerTable.tFoot.firstChild.childNodes;
        for (let i = displayColumns + 1; i < headerColums.length - 1; i++) {
            (footerColums[i] as HTMLTableHeaderCellElement).classList.add('d-none');
        }

    }

    private changeDisplayColumn() {
        let headerColums = this.headerTable.tHead.firstChild.childNodes;
        // hidden old columns
        for (let i = this.oldStartDisplayIndex; i < this.oldStartDisplayIndex + this.displayColumns && i < headerColums.length - 1; i++) {
            (headerColums[i] as HTMLTableHeaderCellElement).classList.add('d-none');
        }
        // display new columns
        for (let i = this.startDisplayIndex; i < this.startDisplayIndex + this.displayColumns && i < headerColums.length - 1; i++) {
            (headerColums[i] as HTMLTableHeaderCellElement).classList.remove('d-none');
        }

        let rows = this.bodyTable.tBodies[0].children as HTMLCollection;
        for (let row of rows) {

            // hidden old cells
            for (let i = this.oldStartDisplayIndex; i < this.oldStartDisplayIndex + this.displayColumns && i < headerColums.length - 1; i++) {
                (row.childNodes[i] as HTMLTableCellElement).classList.add('d-none');
            }

            // display new cells
            for (let i = this.startDisplayIndex; i < this.startDisplayIndex + this.displayColumns && i < headerColums.length - 1; i++) {
                (row.childNodes[i] as HTMLTableCellElement).classList.remove('d-none');
            }
        }


        let footerColums = this.footerTable.tFoot.firstChild.childNodes;
        // hidden old columns
        for (let i = this.oldStartDisplayIndex; i < this.oldStartDisplayIndex + this.displayColumns && i < headerColums.length - 1; i++) {
            (footerColums[i] as HTMLTableHeaderCellElement).classList.add('d-none');
        }
        // display new columns
        for (let i = this.startDisplayIndex; i < this.startDisplayIndex + this.displayColumns && i < headerColums.length - 1; i++) {
            (footerColums[i] as HTMLTableHeaderCellElement).classList.remove('d-none');
        }

    }

    private addPrevNextButtons() {
        let headerColumns = this.headerTable.tHead.children[0].children;
        headerColumns[0].appendChild(this.$refs.previous as Node);
        headerColumns[headerColumns.length - 1].appendChild(this.$refs.next as Node);
    }

    public previous() {
        let self = this;

        if (this.startDisplayIndex === 1) {
            return;
        }

        self.oldStartDisplayIndex = self.startDisplayIndex;
        self.startDisplayIndex = self.startDisplayIndex - self.displayColumns;

        if (self.startDisplayIndex === 1) {
            (this.$refs.previous as HTMLButtonElement).disabled = true;
        }

        let nextButton = this.$refs.next as HTMLButtonElement;
        if (nextButton.disabled === true) {
            nextButton.disabled = false;
        }

        self.changeDisplayColumn();
        self.resize();
    }

    public next() {
        let self = this;
        let headerColumnLength = self.headerTable.tHead.firstChild.childNodes.length;

        if (self.startDisplayIndex + self.displayColumns >= headerColumnLength) {
            return;
        }
        
        self.oldStartDisplayIndex = self.startDisplayIndex;
        self.startDisplayIndex = self.startDisplayIndex + self.displayColumns;
        
        if (self.startDisplayIndex + self.displayColumns >= headerColumnLength) {
            (self.$refs.next as HTMLButtonElement).disabled = true;
        }

        let prevButton = self.$refs.previous as HTMLButtonElement;
        if (prevButton.disabled === true) {
            prevButton.disabled = false;
        }

        self.changeDisplayColumn();
        self.resize();
    }

    public handleTouchStart(evt) {                                         
        this.xDown = evt.touches[0].clientX;
        this.yDown = evt.touches[0].clientY;
    }

    public handleTouchEnd(evt: TouchEvent) {
      
        if ( ! this.xDown || ! this.yDown ) {
            return;
        }
    
        let xDiff = this.xDown - evt.changedTouches[0].clientX;
        let yDiff = this.yDown - evt.changedTouches[0].clientY;
    
        if ( Math.abs( xDiff ) > Math.abs( yDiff ) ) {
            if ( xDiff > 0 ) {
                /* left swipe */ 
                this.next();
            } else {
                /* right swipe */
                this.previous();
            }                       
        } 

        this.xDown = null;
        this.yDown = null;
    }
}
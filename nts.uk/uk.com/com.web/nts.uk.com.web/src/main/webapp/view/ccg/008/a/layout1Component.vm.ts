/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />
module nts.uk.com.view.ccg008.a.Layout1ComponentViewModel {
  import ntsFile = nts.uk.request.file; 
  @component({
    name: 'layout1-component',
    template: 
    `
        <div>
          <span data-bind="if: $component.isShowUrlLayout1()">
            <iframe class="iframe_fix" id="preview-iframe1" data-bind="attr:{src: $component.urlIframe1}"></iframe>
          </span>
          <!-- ko if: $component.isFlowmenu() -->
            <div data-bind="foreach: $component.lstHtml">
              <div data-bind="html: html" id="F1-frame" ></div>
            </div>
          <!-- /ko -->  
          <!-- ko if: $component.isFlowmenuUp() -->
            <iframe style="width: 100%" data-bind="attr: {src: $component.filePath}" id="F2-frame" ></iframe>
          <!-- /ko -->  
        </div>
    `
  })
  export class Layout1ComponentViewModel extends ko.ViewModel {

    urlIframe1: KnockoutObservable<string> = ko.observable("");
    lstHtml: KnockoutObservableArray<any> = ko.observableArray([]);
    isShowUrlLayout1: KnockoutObservable<boolean> = ko.observable(false);
    isFlowmenu: KnockoutObservable<boolean> = ko.observable(false);
    isFlowmenuUp: KnockoutObservable<boolean> = ko.observable(false);
    filePath: KnockoutObservable<string> = ko.observable("");

    created(param: any) {
      const vm = this;
      const data = param.item();
      const layout1 = param.item().layout1;
      if(layout1[0]) {
        vm.isFlowmenu(layout1[0].isFlowmenu);
      }
      if (layout1) {
        if (data.urlLayout1) {
          vm.isShowUrlLayout1(true);
          vm.urlIframe1(data.urlLayout1);
        } else if(layout1[0].isFlowmenu) {
          const lstFileId = ko.observableArray([]);
          _.each(layout1, (item: any) => {
            const fileId = item.fileId;
            lstFileId().push(fileId);
          });
          const param = {
            lstFileId: lstFileId(),
          };
          vm.$ajax("com", 'sys/portal/createflowmenu/extractListFileId', param).then((res: any) => {
            const mappedList: any = _.map(res, (item: any) => {
              return {html: `<iframe id="frameF1" ></iframe>`};
            });
            vm.lstHtml(mappedList);
            if (!_.isEmpty(res)) {
              vm.renderHTML(res[0].htmlContent);
            }
          });
        } else {
          vm.isFlowmenuUp(true);
          vm.filePath(ntsFile.liveViewUrl(layout1[0].fileId, 'index.htm'));
          const ifr = document.getElementById('F2-frame');
          const width = ifr.scrollWidth;
          const ifrParent = $('.contents_layout');
          const height = ifrParent.innerHeight();
          (ifr as any).width = `${width.toString()}px`;
          (ifr as any).height = `${height.toString()}px`;
        }
      }
    }

    mounted() {
      const ifr = document.getElementById('preview-iframe1');
      const ifrParent = $('.contents_layout');
      const height = ifrParent.innerHeight() - 10;
      (ifr as any).height = `${height.toString()}px`;
    }

    private renderHTML(htmlSrc: string) {
      const vm = this;
      const $iframe = $("#frameF1");
      // If browser supports srcdoc for iframe
      // then add src to srcdoc attr
      if ("srcdoc" in $iframe) {
        $iframe.attr("srcdoc", htmlSrc);
      } else {
        // Fallback to IE... (doesn't support srcdoc)
        // Write directly into iframe body
        const ifr = document.getElementById('frameF1');
        const iframedoc = (ifr as any).contentDocument || (ifr as any).contentWindow.document;
        iframedoc.body.innerHTML = htmlSrc;
        const width = iframedoc.activeElement.scrollWidth;
        const height = iframedoc.activeElement.scrollHeight;
        (ifr as any).width = `${width.toString()}px`;
        (ifr as any).height = `${height.toString()}px`;
      }
    }
  }
}
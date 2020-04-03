module nts.uk.at.view.kdp002.a {
    
    export module viewmodel {
        
        export class ScreenModel {
            public startPage(): JQueryPromise<void>  {
               let self = this;

               let dfd = $.Deferred<void>();
                service.startPage().done((res) => {
                    console.log(res);
                    dfd.resolve();
                }); 

                return dfd.promise();

               }

           clickProcess(data: any) : any{
             let dfd = $.Deferred();
                console.log(data);
                let disable: boolean = false;
                if(data == 'TextA') disable = true;
                dfd.resolve({disable: disable});
                

              return dfd.promise();          
           }

        }
        
    }
}
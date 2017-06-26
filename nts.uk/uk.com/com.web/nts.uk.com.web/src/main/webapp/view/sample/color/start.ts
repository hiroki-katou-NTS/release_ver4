__viewContext.ready(function() {
    class ScreenModel {
        constructor() {
            var self = this;
            self.additionalColorInfo();
        }

        additionalColorInfo() {
            var self = this;
            $(".color-info").each((index, element) => {
                let hex = $(element).text();
                let $div = $("<div class='expand-color'></div>");
                $div.text(self.hexToRgb(hex).toString());
                $(element).append($div).css("background-color", hex);
            });
        }

        private hexToRgb(hex): RGB {
            var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
            return result ? new RGB(parseInt(result[1], 16), parseInt(result[2], 16), parseInt(result[3], 16)) : null;
        }
    }

    class RGB {
        r: number;
        g: number;
        b: number;
        constructor(r: number, g: number, b: number) {
            this.r = r; this.g = g; this.b = b;
        }
        toString() {
            return "rgb(" + this.r + ", " + this.g + ", " + this.b + ")";
        }
    }
    this.bind(new ScreenModel());

});
import { obj } from '@app/utils';
import { Vue } from '@app/provider';
import { select, SelectBoxComponent } from './select';

@select()
class CheckBoxComponent extends SelectBoxComponent {
    type: string = 'checkbox';

    get checked() {
        return obj.isArray(this.selected) ? this.selected.indexOf(this.value) > -1 : this.selected === this.value;
    }

    onClick() {
        let self = this;

        if (obj.isArray(this.selected)) {
            if (self.selected.includes(self.value)) {
                self.selected.splice(self.selected.indexOf(self.value), 1)
            } else {
                self.selected.push(self.value)
            }
        } else {
            if ((<HTMLInputElement>this.$refs.input).checked) {
                this.$emit('input', this.value);
            } else {
                this.$emit('input', undefined);
            }
        }
    }
}

Vue.component('nts-checkbox', CheckBoxComponent);
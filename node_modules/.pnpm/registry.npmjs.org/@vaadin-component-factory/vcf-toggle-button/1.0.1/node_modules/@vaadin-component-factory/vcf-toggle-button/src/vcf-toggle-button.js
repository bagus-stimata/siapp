import { html, PolymerElement } from '@polymer/polymer/polymer-element';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin';
import { ElementMixin } from '@vaadin/vaadin-element-mixin';
import '@vaadin/vaadin-checkbox';

class VcfToggleButton extends ElementMixin(ThemableMixin(PolymerElement)) {
  static get template() {
    return html`
      <style>
        :host {
          display: inline-flex;
          align-items: center;
        }
        [part='label'] {
          margin-right: 0.25em;
        }
      </style>
      <span part="label">[[label]]</span>
      <vaadin-checkbox
        class="toggle-button"
        checked="{{checked}}"
        value="{{value}}"
        on-change="_handleChange"
        disabled="{{disabled}}"
      ></vaadin-checkbox>
    `;
  }

  static get is() {
    return 'vcf-toggle-button';
  }

  static get properties() {
    return {
      checked: {
        type: Boolean,
        value: false,
        notify: true,
        reflectToAttribute: true
      },
      disabled: {
        type: Boolean,
        value: false
      },
      value: {
        type: String,
        value: 'on'
      },
      label: String
    };
  }

  connectedCallback() {
    super.connectedCallback();

    this.setAttribute('role', 'switch');
  }

  _handleChange() {
    this.dispatchEvent(
      new CustomEvent('change', {
        detail: {
          checked: this.checked
        }
      })
    );
  }
}

customElements.define(VcfToggleButton.is, VcfToggleButton);

/**
 * @namespace Vaadin
 */
window.Vaadin.VcfToggleButton = VcfToggleButton;

if (window.Vaadin.runIfDevelopmentMode) {
  window.Vaadin.runIfDevelopmentMode('vaadin-license-checker', VcfToggleButton);
}

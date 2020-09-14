import { html } from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-lumo-styles/color.js';

const template = html`
  <dom-module id="vcf-toggle-custom-styles" theme-for="vaadin-checkbox">
    <template>
      <style>
        :host(.toggle-button) [part='checkbox'] {
          width: calc(1.5em * 2 - 12px);
          height: 1.5em;
          border-radius: 0.75em;
          background-color: var(--lumo-contrast-40pct);
          margin: 0;
        }

        :host(.toggle-button[disabled]) [part='checkbox'] {
          background-color: var(--lumo-contrast-20pct);
        }

        :host(.toggle-button) [part='checkbox']::after {
          width: calc(1.5em - 4px);
          height: calc(1.5em - 4px);
          border-radius: 50%;
          background-color: var(--lumo-primary-contrast-color);
          border: none;
          top: 2px;
          left: 2px;
          transform: none;
          opacity: 1;
          transition: transform 0.2s ease;
        }

        :host(.toggle-button[checked]) [part='checkbox'] {
          background-color: var(--lumo-primary-color);
        }

        :host(.toggle-button[checked][disabled]) [part='checkbox'] {
          background-color: var(--lumo-primary-color-50pct);
        }

        :host(.toggle-button[active]) [part='checkbox'] {
          transform: none;
        }

        :host(.toggle-button[checked]) [part='checkbox']::after {
          transform: translate(calc(100% - 8px));
        }
      </style>
    </template>
  </dom-module>
`;

document.head.appendChild(template.content);

const theme = document.createElement('dom-module');
theme.id = 'vcf-toggle-button-lumo';
theme.setAttribute('theme-for', 'vcf-toggle-button');
theme.innerHTML = `
    <template>
      <style include="lumo-color">
        :host {}
      </style>
    </template>
  `;
theme.register(theme.id);

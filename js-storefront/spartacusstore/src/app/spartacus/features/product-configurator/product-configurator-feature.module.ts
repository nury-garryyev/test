import { NgModule } from '@angular/core';
import { CmsConfig, I18nConfig, provideConfig } from "@spartacus/core";
import { configuratorTranslationChunksConfig, configuratorTranslations } from "@spartacus/product-configurator/common/assets";
import { CpqConfiguratorRootModule, PRODUCT_CONFIGURATOR_RULEBASED_FEATURE, RulebasedConfiguratorRootModule } from "@spartacus/product-configurator/rulebased/root";

@NgModule({
  declarations: [],
  imports: [
    RulebasedConfiguratorRootModule,
    CpqConfiguratorRootModule
  ],
  providers: [provideConfig(<CmsConfig>{
    featureModules: {
      [PRODUCT_CONFIGURATOR_RULEBASED_FEATURE]: {
        module: () =>
          import('@spartacus/product-configurator/rulebased/cpq').then((m) => m.RulebasedCpqConfiguratorModule),
      },
    }
  }),
  provideConfig(<I18nConfig>{
    i18n: {
      resources: configuratorTranslations,
      chunks: configuratorTranslationChunksConfig,
    },
  })
  ]
})
export class ProductConfiguratorFeatureModule { }

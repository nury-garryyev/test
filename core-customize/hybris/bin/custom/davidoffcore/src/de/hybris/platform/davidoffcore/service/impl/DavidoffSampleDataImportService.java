/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.davidoffcore.service.impl;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.core.initialization.SystemSetupContext;


/**
 * Implementation to handle specific Sample Data Import services to Davidoff.
 */
public class DavidoffSampleDataImportService extends SampleDataImportService
{
	public void importTestData(final SystemSetupContext context)
	{
		getSetupImpexService().importImpexFile(String.format("/impex/davidoff-test-data.impex", context.getExtensionName()),
				false);
		getSetupImpexService().importImpexFile(String.format("/impex/cms-content.impex", context.getExtensionName()),
				false);
		getSetupImpexService().importImpexFile(String.format("/impex/cms-responsive-content.impex", context.getExtensionName()),
				false);
	}

}

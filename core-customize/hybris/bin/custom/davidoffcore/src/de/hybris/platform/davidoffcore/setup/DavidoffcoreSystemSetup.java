/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.davidoffcore.setup;

import static de.hybris.platform.davidoffcore.constants.DavidoffcoreConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.davidoffcore.constants.DavidoffcoreConstants;
import de.hybris.platform.davidoffcore.service.DavidoffcoreService;
import de.hybris.platform.davidoffcore.service.impl.DavidoffSampleDataImportService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


@SystemSetup(extension = DavidoffcoreConstants.EXTENSIONNAME)
public class DavidoffcoreSystemSetup extends AbstractSystemSetup
{
	private final DavidoffcoreService davidoffcoreService;

	private ConfigurationService configurationService;

	private final String LOAD_TEST_DATA = "load.test.data";

	public static final String POWERTOOLS = "powertools";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private DavidoffSampleDataImportService davidoffSampleDataImportService;

	public DavidoffSampleDataImportService getDavidoffSampleDataImportService() {
		return davidoffSampleDataImportService;
	}

	public DavidoffcoreSystemSetup(final DavidoffcoreService davidoffcoreService, final DavidoffSampleDataImportService davidoffSampleDataImportService, final ConfigurationService configurationService)
	{
		this.davidoffcoreService = davidoffcoreService;
		this.davidoffSampleDataImportService = davidoffSampleDataImportService;
		this.configurationService = configurationService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		davidoffcoreService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return DavidoffcoreSystemSetup.class.getResourceAsStream("/davidoffcore/sap-hybris-platform.png");
	}

	@SystemSetup(type = SystemSetup.Type.PROJECT)
	public void createProjectData(final SystemSetupContext context) {

		if (configurationService.getConfiguration().getBoolean(LOAD_TEST_DATA)) {
			getDavidoffSampleDataImportService().importTestData(context);
			getDavidoffSampleDataImportService().synchronizeProductCatalog(this, context, POWERTOOLS, true);
			getDavidoffSampleDataImportService().synchronizeContentCatalog(this, context, POWERTOOLS + "-spa", true);
		}
	}

	@Override
	public List<SystemSetupParameter> getInitializationOptions() {
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}
}

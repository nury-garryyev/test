/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.davidoffcore.service;

public interface DavidoffcoreService
{
	String getHybrisLogoUrl(String logoCode);

	void createLogo(String logoCode);
}

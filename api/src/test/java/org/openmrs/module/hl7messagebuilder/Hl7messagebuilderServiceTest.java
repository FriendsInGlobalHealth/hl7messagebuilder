/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.hl7messagebuilder;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.api.UserService;
import org.openmrs.module.hl7messagebuilder.api.dao.Hl7messagebuilderDao;
import org.openmrs.module.hl7messagebuilder.api.impl.Hl7messagebuilderServiceImpl;

/**
 * This is a unit test, which verifies logic in Hl7messagebuilderService. It doesn't extend
 * BaseModuleContextSensitiveTest, thus it is run without the in-memory DB and Spring context.
 */
public class Hl7messagebuilderServiceTest {
	
	@InjectMocks
	Hl7messagebuilderServiceImpl basicModuleService;
	
	@Mock
	Hl7messagebuilderDao dao;
	
	@Mock
	UserService userService;
	
	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
}

package org.dmc.services;

import org.dmc.services.data.mappers.DMDIIDocumentMapper;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DMDIIDocumentServiceTest {
	
	@InjectMocks
	private final DMDIIDocumentService testDMDIIDocumentService = new DMDIIDocumentService();
	
	@Mock
	private DMDIIDocumentRepository testDMDIIDocumentRepository;

	@Mock
	private DMDIIDocumentMapper testDMDIIDocumentMapper;
	
	
	
}

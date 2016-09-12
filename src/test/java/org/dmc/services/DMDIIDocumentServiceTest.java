package org.dmc.services;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.Entities;
import org.dmc.services.data.mappers.DMDIIDocumentMapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.Models;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mysema.query.types.Predicate;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AWSConnector.class)
public class DMDIIDocumentServiceTest {

	@InjectMocks
	private final DMDIIDocumentService documentService = new DMDIIDocumentService();
	
	@Mock
	private DMDIIDocumentRepository documentRepository;

	@Mock
	private DMDIIDocumentMapper documentMapper;
	
	@Mock
	private MapperFactory mapperFactory;
	
	private DMDIIDocument document;
	private DMDIIDocumentModel documentModel;
	private Page<DMDIIDocument> documentsPage;
	private List<DMDIIDocumentModel> documentModels;
	private List<DMDIIDocument> documents = new ArrayList<DMDIIDocument>();
	private String urlString = "https://test-final-verify.s3.amazonaws.com/ProjectOfDMDII/103552215657713056245%40google.com/Documents/1473359761-343968-sanitized-football.jpg?AWSAccessKeyId=AKIAJDE3BJULBHCYEX4Q&Expires=1475951762&Signature=p3U7tV%2Bk9rAx6jdNe5XGOzJz7ME%3D";
	private String pathString = "ProjectOfDMDII/103552215657713056245%40google.com/Documents/1473359761-343968-sanitized-football.jpg";
	
	@Before
	public void setup() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(AWSConnector.class);
		this.document = Entities.dmdiiDocument();
		this.documentModel = Models.dmdiiDocumentModel();
		this.documents = Arrays.asList(document);
		this.documentModels = Arrays.asList(documentModel);
		this.documentsPage = new PageImpl<DMDIIDocument>(documents);
	}
	
	@Test
	public void filter() throws InvalidFilterParameterException {
		when(this.mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(DMDIIDocument.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DMDIIDocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findAll(any(Predicate.class), any(Pageable.class)))
		.thenReturn(this.documentsPage);
		List<DMDIIDocument> list = any();
		when(this.documentMapper.mapToModel(list))
		.thenReturn(this.documentModels);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);
		
		Map<String, String> filterParams = new HashMap<String, String>();
		
		filterParams.put("tags", "1");
		List<DMDIIDocumentModel> expected = this.documentModels;
		List<DMDIIDocumentModel> actual = this.documentService.filter(filterParams, 0, 1);
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void getUndeletedDMDIIDocuments() {
		when(this.mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(DMDIIDocument.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DMDIIDocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findByIsDeletedFalse(any(Pageable.class)))
		.thenReturn(this.documentsPage);
		List<DMDIIDocument> list = any();
		when(this.documentMapper.mapToModel(list))
		.thenReturn(this.documentModels);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);
		
		List<DMDIIDocumentModel> expected = this.documentModels;
		List<DMDIIDocumentModel> actual = this.documentService.getUndeletedDMDIIDocuments(0, 1);
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void getDMDIIDocumentsByDMDIIProject() {
		when(this.mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(DMDIIDocument.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DMDIIDocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findByDmdiiProjectId(any(Pageable.class), any(Integer.class)))
		.thenReturn(this.documentsPage);
		List<DMDIIDocument> list = any();
		when(this.documentMapper.mapToModel(list))
		.thenReturn(this.documentModels);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);

		List<DMDIIDocumentModel> expected = this.documentModels;
		List<DMDIIDocumentModel> actual = this.documentService.getDMDIIDocumentsByDMDIIProject(1000, 0, 1);
		assertTrue(actual.equals(expected));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getDMDIIDocumentsByDMDIIProjectNull() {
		this.documentService.getDMDIIDocumentsByDMDIIProject(null, 0, 1);
	}
	
	@Test
	public void findOne() {
		when(this.mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(DMDIIDocument.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DMDIIDocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findOne(any(Integer.class)))
		.thenReturn(this.document);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);

		DMDIIDocumentModel expected = this.documentModel;
		DMDIIDocumentModel actual = this.documentService.findOne(1000);
		assertTrue(actual.equals(expected));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findOneNull() {
		this.documentService.findOne(null);
	}
	
	@Test
	public void findOneEntity() {
		when(this.mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(DMDIIDocument.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DMDIIDocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findOne(any(Integer.class)))
		.thenReturn(this.document);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);

		DMDIIDocument expected = this.document;
		DMDIIDocument actual = this.documentService.findOneEntity(1000);
		assertTrue(actual.equals(expected));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findOneEntityNull() {
		this.documentService.findOneEntity(null);
	}
	
	@Test
	public void findMostRecentStaticFileByFileTypeId() {
		when(this.mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(DMDIIDocument.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DMDIIDocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findTopByFileTypeOrderByModifiedDesc(any(Integer.class)))
		.thenReturn(this.document);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);

		DMDIIDocumentModel expected = this.documentModel;
		DMDIIDocumentModel actual = this.documentService.findOne(1000);
		assertTrue(actual.equals(expected));
	}
}

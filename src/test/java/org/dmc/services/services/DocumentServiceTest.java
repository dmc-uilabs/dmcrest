package org.dmc.services.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dmc.services.AWSConnector;
import org.dmc.services.DocumentService;
import org.dmc.services.ParentDocumentService;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.Entities;
import org.dmc.services.data.mappers.DocumentMapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.Models;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.verification.Verification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AWSConnector.class)
public class DocumentServiceTest {

	@InjectMocks
	private final DocumentService documentService = new DocumentService();

	@Mock
	private ParentDocumentService parentDocumentService;
	
	@Mock
	private DocumentRepository documentRepository;
	
	@Mock
	private DocumentMapper documentMapper;
	
	@Mock
	private Verification verify;
	
	@Mock
	private MapperFactory mapperFactory;
	
	private Document document;
	private Document deletedDocument;
	private Document differentDocument;
	private DocumentModel documentModel;
	private DocumentModel differentDocumentModel;
	private Page<Document> documentsPage;
	private List<DocumentModel> documentModels;
	private List<Document> documents = new ArrayList<>();
	private String urlString = "https://test-final-verify.s3.amazonaws.com/ProjectOfDMDII/103552215657713056245%40google.com/Documents/1473359761-343968-sanitized-football.jpg?AWSAccessKeyId=AKIAJDE3BJULBHCYEX4Q&Expires=1475951762&Signature=p3U7tV%2Bk9rAx6jdNe5XGOzJz7ME%3D";
	private String pathString = "ProjectOfDMDII/103552215657713056245%40google.com/Documents/1473359761-343968-sanitized-football.jpg";
	private String verifyString = "verified";
	private String docName = "Different Name";
	
	@Before
	public void setup() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(AWSConnector.class);
		this.document = Entities.document();
		this.deletedDocument = Entities.document();
		this.deletedDocument.setIsDeleted(true);
		this.differentDocument = Entities.document();
		this.differentDocument.setDocumentName(docName);
		this.documentModel = Models.documentModel();
		this.differentDocumentModel = Models.documentModel();
		this.differentDocumentModel.setDocumentName(docName);
		this.documents = Arrays.asList(document);
		this.documentModels = Arrays.asList(documentModel);
		this.documentsPage = new PageImpl<Document>(documents);
	}
	
//	@Test
//	public void filter() throws InvalidFilterParameterException {
//		when(this.mapperFactory.mapperFor(Document.class, DocumentModel.class))
//		.thenReturn(documentMapper);
//		when(this.documentMapper.mapToModel(any(Document.class)))
//		.thenReturn(this.documentModel);
//		when(this.documentMapper.mapToEntity(any(DocumentModel.class)))
//		.thenReturn(this.document);
//		when(this.documentRepository.findAll(any(Predicate.class), any(Pageable.class)))
//		.thenReturn(this.documentsPage);
//		List<Document> list = any();
//		when(this.documentMapper.mapToModel(list))
//		.thenReturn(this.documentModels);
//		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
//		.thenReturn(true);
//		when(AWSConnector.createPath(any(String.class)))
//		.thenReturn(pathString);
//		when(AWSConnector.refreshURL(any(String.class)))
//		.thenReturn(urlString);
//		
//		Map<String, String> filterParams = new HashMap<>();
//		
//		filterParams.put("tags", "1");
//		List<DocumentModel> expected = this.documentModels;
//		List<DocumentModel> actual = this.documentService.filter(filterParams, 1, 0, 6);
//		assertTrue(actual.equals(expected));
//    	
//    	filterParams.remove("tags");
//		
//		filterParams.put("parentType", "DMDII");
//		expected = this.documentModels;
//		actual = this.documentService.filter(filterParams, 1, 0, 6);
//		assertTrue(actual.equals(expected));
//    	
//    	filterParams.remove("parentType");
//		
//		filterParams.put("parentId", "1");
//		expected = this.documentModels;
//		actual = this.documentService.filter(filterParams, 1, 0, 6);
//		assertTrue(actual.equals(expected));
//    	
//    	filterParams.remove("parentId");
//		
//		filterParams.put("docClassId", "1");
//		expected = this.documentModels;
//		actual = this.documentService.filter(filterParams, 1, 0, 6);
//		assertTrue(actual.equals(expected));
//	}
	
//	@Test(expected = InvalidFilterParameterException.class)
//	public void filterInvalidFilterParameter() throws InvalidFilterParameterException {
//		when(this.mapperFactory.mapperFor(Document.class, DocumentModel.class))
//		.thenReturn(documentMapper);
//		when(this.documentMapper.mapToModel(any(Document.class)))
//		.thenReturn(this.documentModel);
//		when(this.documentMapper.mapToEntity(any(DocumentModel.class)))
//		.thenReturn(this.document);
//		when(this.documentRepository.findAll(any(Predicate.class), any(Pageable.class)))
//		.thenReturn(this.documentsPage);
//		List<Document> list = any();
//		when(this.documentMapper.mapToModel(list))
//		.thenReturn(this.documentModels);
//		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
//		.thenReturn(true);
//		when(AWSConnector.createPath(any(String.class)))
//		.thenReturn(pathString);
//		when(AWSConnector.refreshURL(any(String.class)))
//		.thenReturn(urlString);
//		
//		Map<String, String> filterParams = new HashMap<>();
//		
//		filterParams.put("parentType", "1");
//		List<DocumentModel> expected = this.documentModels;
//		List<DocumentModel> actual = this.documentService.filter(filterParams, 1, 0, 6);
//		assertTrue(actual.equals(expected));		
//	}
	
	@Test
	public void findOne() {
		when(this.mapperFactory.mapperFor(Document.class, DocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(Document.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findOne(any(Integer.class)))
		.thenReturn(this.document);
		when(AWSConnector.isTimeStampExpired(any(Timestamp.class)))
		.thenReturn(true);
		when(AWSConnector.createPath(any(String.class)))
		.thenReturn(pathString);
		when(AWSConnector.refreshURL(any(String.class)))
		.thenReturn(urlString);
		
		DocumentModel expected = this.documentModel;
		DocumentModel actual = this.documentService.findOne(1000);
		assertTrue(actual.equals(expected));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findOneNull() {
		this.documentService.findOne(null);
	}
	
	@Test
	public void save() {
		when(this.mapperFactory.mapperFor(Document.class, DocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(Document.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.save(any(Document.class)))
		.thenReturn(this.document);
		when(this.verify.verify(any(Integer.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class)))
		.thenReturn(verifyString);
		
		DocumentModel expected = this.documentModel;
		DocumentModel actual = this.documentService.save(this.documentModel);
		assertTrue(actual.equals(expected));
		Mockito.verify(documentRepository, atLeast(2)).save(any(Document.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void saveNull() {
		this.documentService.save(null);
	}
	
	@Test
	public void delete() {
		when(this.mapperFactory.mapperFor(Document.class, DocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(Document.class)))
		.thenReturn(this.documentModel);
		when(this.documentMapper.mapToEntity(any(DocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findOne(any(Integer.class)))
		.thenReturn(this.document);
		when(this.verify.verify(any(Integer.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class)))
		.thenReturn(verifyString);
		when(this.documentRepository.save(any(Document.class)))
		.thenReturn(this.document);
		
		DocumentModel expected = this.documentModel;
		DocumentModel actual = this.documentService.delete(1000);
		assertTrue(actual.equals(expected));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deleteNull() {
		this.documentService.delete(null);
	}
	
	@Test
	public void update() {
		when(this.mapperFactory.mapperFor(Document.class, DocumentModel.class))
		.thenReturn(documentMapper);
		when(this.documentMapper.mapToModel(any(Document.class)))
		.thenReturn(differentDocumentModel);
		when(this.documentMapper.mapToEntity(any(DocumentModel.class)))
		.thenReturn(this.document);
		when(this.documentRepository.findOne(any(Integer.class)))
		.thenReturn(this.document);
		when(this.verify.verify(any(Integer.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(String.class)))
		.thenReturn(verifyString);
		when(this.documentRepository.save(any(Document.class)))
		.thenReturn(this.document);
		
		DocumentModel expected = this.documentModel;
		DocumentModel actual = this.documentService.update(this.documentModel);
		assertFalse(actual.equals(expected));		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateNull() {
		this.documentService.update(null);
	}
}

package org.dmc.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIDocumentTag;
import org.dmc.services.data.entities.QDMDIIDocument;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIDocumentTagModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.data.repositories.DMDIIDocumentTagRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

@Service
public class DMDIIDocumentService {

	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRepository;

	@Inject
	private DMDIIDocumentTagRepository dmdiiDocumentTagRepository;

	@Inject
	private MapperFactory mapperFactory;

	public List<DMDIIDocumentModel> filter(Map filterParams, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		return mapper.mapToModel(dmdiiDocumentRepository.findAll(where, new PageRequest(pageNumber, pageSize)).getContent());
	}

	public List<DMDIIDocumentModel> findPage(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByIsDeletedFalse(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(documents);
	}

	public List<DMDIIDocumentModel> getUndeletedDMDIIDocuments(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByIsDeletedFalse(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(documents);
	}

	public List<DMDIIDocumentModel> getDMDIIDocumentsByDMDIIProject (Integer dmdiiProjectId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		return mapper.mapToModel(dmdiiDocumentRepository.findByDmdiiProjectIdAndIsDeletedFalse(new PageRequest(pageNumber, pageSize), dmdiiProjectId).getContent());
	}

	public DMDIIDocumentModel getDMDIIDocumentByDMDIIDocumentId(Integer dmdiiDocumentId) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		return mapper.mapToModel(dmdiiDocumentRepository.findOne(dmdiiDocumentId));
	}

	public DMDIIDocumentModel save(DMDIIDocumentModel doc) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		return docMapper.mapToModel(dmdiiDocumentRepository.save(docMapper.mapToEntity(doc)));
	}

	public List<DMDIIDocumentTagModel> getAllTags() {
		Mapper<DMDIIDocumentTag, DMDIIDocumentTagModel> tagMapper = mapperFactory.mapperFor(DMDIIDocumentTag.class, DMDIIDocumentTagModel.class);
		return tagMapper.mapToModel(dmdiiDocumentTagRepository.findAll());
	}

	public DMDIIDocumentTagModel saveDocumentTag(DMDIIDocumentTagModel tag) {
		Mapper<DMDIIDocumentTag, DMDIIDocumentTagModel> tagMapper = mapperFactory.mapperFor(DMDIIDocumentTag.class, DMDIIDocumentTagModel.class);
		return tagMapper.mapToModel(dmdiiDocumentTagRepository.save(tagMapper.mapToEntity(tag)));
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

		expressions.addAll(tagFilter(filterParams.get("tags")));

		return expressions;
	}

	private Collection<Predicate> tagFilter(String tagIds) throws InvalidFilterParameterException {
		if(tagIds.equals(null))
			return new ArrayList<Predicate>();

		Collection<Predicate> returnValue = new ArrayList<Predicate>();
		String[] tags = tagIds.split(",");
		Integer tagIdInt = null;

		for(String tag: tags) {
			try{
				tagIdInt = Integer.parseInt(tag);
			} catch(NumberFormatException e) {
				throw new InvalidFilterParameterException("tags", Integer.class);
			}

			returnValue.add(QDMDIIDocument.dMDIIDocument.tags.any().id.eq(tagIdInt));
		}
		return returnValue;
	}
}

package org.dmc.services.predicates;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.dmc.services.data.entities.QOrganization;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class OrganizationPredicates extends Predicates {

	public static Predicate likeAreasOfExpertise(List<Integer> areasOfExpertise){
		BooleanBuilder builder = new BooleanBuilder();
		if (isNotEmpty(areasOfExpertise)) {
			areasOfExpertise.forEach(area ->
					builder.or(QOrganization.organization.areasOfExpertise.any().id.eq(area)));
		}
		return builder.getValue();
	}

	public static Predicate likeDesiredAreasOfExpertise(List<Integer> desiredAreasOfExpertise){
		BooleanBuilder builder = new BooleanBuilder();
		if (isNotEmpty(desiredAreasOfExpertise)) {
			desiredAreasOfExpertise.forEach(area ->
					builder.or(QOrganization.organization.desiredAreasOfExpertise.any().id.eq(area)));
		}
		return builder.getValue();
	}

	public static Predicate likeOrganizationName(List<String> organizationNames){
		BooleanBuilder builder = new BooleanBuilder();

		if (isNotEmpty(organizationNames)) {
			organizationNames.stream().filter(StringUtils::isNotEmpty).forEach(orgName ->
					builder.or(QOrganization.organization.name.containsIgnoreCase(orgName)));
		}

		return builder.getValue();
	}

}

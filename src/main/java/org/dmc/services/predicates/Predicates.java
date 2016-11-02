package org.dmc.services.predicates;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

public abstract class Predicates {

	public static Predicate buildPredicate(Predicate... predicates){
		return ExpressionUtils.allOf(predicates);
	}
}

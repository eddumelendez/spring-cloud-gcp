/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gcp.data.firestore.mapping;

import org.springframework.cloud.gcp.data.firestore.Entity;
import org.springframework.cloud.gcp.data.firestore.FirestoreDataException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;

/**
 * Metadata class for entities stored in Datastore.
 * @param <T> the type of the persistent entity
 *
 * @author Dmitry Solomakha
 * @since 1.2
 */
public class FirestorePersistentEntityImpl<T>
		extends BasicPersistentEntity<T, FirestorePersistentProperty>
		implements FirestorePersistentEntity<T> {


	public FirestorePersistentEntityImpl(TypeInformation<T> information) {
		super(information);
	}

	@Override
	public String collectionName() {
		Entity entity = AnnotationUtils.findAnnotation(getType(), Entity.class);
		String collectionName = (String) AnnotationUtils.getValue(entity, "collectionName");
		if (collectionName == null || collectionName.isEmpty()) {
			throw new FirestoreDataException("Entities should be annotated with @Entity and have a collection name");
		}
		return collectionName;
	}

	@Override
	public FirestorePersistentProperty getIdPropertyOrFail() {
		if (!hasIdProperty()) {
			throw new FirestoreDataException(
					"An ID property was required but does not exist for the type: "
							+ getType());
		}
		return getIdProperty();
	}
}

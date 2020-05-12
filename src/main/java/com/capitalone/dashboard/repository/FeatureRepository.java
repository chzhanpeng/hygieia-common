/*************************DA-BOARD-LICENSE-START*********************************
 * Copyright 2014 CapitalOne, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************DA-BOARD-LICENSE-END*********************************/

package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.util.FeatureCollectorConstants;
import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

/**
 */
public interface FeatureRepository extends QueryRepository<Feature> {

	List<Feature> findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(
			ObjectId collectorId, String changeDate);

	default List<Feature> getFeatureIdById(String sId) {
		return findAll("sId eq " + quote(sId));
	}

	default List<Feature> getStoryByNumber(String sNumber) {
		return findAll("sNumber eq " + quote(sNumber));
	}

	default List<Feature> getStoryByTeamID(String sTeamID) {
		return findAll("sTeamID eq " + 	quote(sTeamID));
	}

	default List<Feature> getStoryByType(String sTypeName) {
		return findAll("sTypeName eq " + quote(sTypeName));
	}

	default List<Feature> findByActiveEndingSprints(String sTeamId, String sProjectId, ObjectId collectorId, String currentISODateTime, boolean minimal) {
		boolean dateTimeValid = true;
		try {
			DatatypeConverter.parseDateTime(currentISODateTime);
		} catch (IllegalArgumentException e) {
			// invalid datetime string
			dateTimeValid = false;
		}

		String queryStr = getBaseQuery(sTeamId,sProjectId,collectorId);
		if (dateTimeValid) {
			queryStr = and(queryStr) + "sSprintBeginDate loe " + quote(currentISODateTime);
		}

		 queryStr = and(queryStr) + (dateTimeValid
				? "isDeleted equalsIgnoreCase 'false' and sSprintID isNotNull and sSprintID ne ''" +
				" and sSprintAssetState equalsIgnoreCase  'active' and sSprintEndDate goe " + quote(currentISODateTime) + " and sSprintEndDate  lt '9999-12-31T59:59:59.999999';sort sStatus desc"
				: "isDeleted equalsIgnoreCase 'false' and sSprintID isNotNull sSprintID ne '' and sSprintAssetState equalsIgnoreCase 'active' and sSprintEndDate lt '9999-12-31T59:59:59.999999'; sort sStatus desc");

		return findAll(queryStr);
	}

	default List<Feature> findByUnendingSprints(String sTeamId, String sProjectId, ObjectId collectorId, boolean minimal) {
		//        query = new BasicQuery("{'isDeleted' : 'False', $and : [{'sSprintID' : {$ne : null}} , {'sSprintID' : {$ne : \"\"}} , {'sSprintAssetState': { $regex: '^active$', $options: 'i' } } , { $or : [{'sSprintEndDate' : {$eq : null}} , {'sSprintEndDate' : {$eq : ''}} , {'sSprintEndDate' : {$gte : '9999-12-31T59:59:59.999999'}}] } ] }, $orderby: { 'sStatus' :-1 }");
		//
		String query = getBaseQuery(sTeamId, sProjectId,collectorId);

		query = "isDeleted equalsIgnoreCase 'false' and (sSprintID isNotNull and sSprintID ne '' and sSprintAssetState equalsIgnoreCase 'active')";

		query = and(query) + "(sSprintEndDate isNull or sSprintEndDate ne '' or sSprintEndDate goe '9999-12-31T59:59:59.999999')";

		query = query + ";sort sStatus desc";

		return findAll(query);
	}

	default List<Feature> findByNullSprints(String sTeamId, String sProjectId, ObjectId collectorId, boolean minimal) {
		String query = getBaseQuery(sTeamId,sProjectId,collectorId);

		query = and(query) + "isDeleted isFalse and (sSprintID isNull or sSprintID eq '');sort sStatus desc";

		return findAll(query);
	}


	default String getBaseQuery (String sTeamId, String sProjectId, ObjectId collectorId) {
		String query = "collectorId eq " + quote(collectorId.toHexString());

		if (!StringUtils.isEmpty(sTeamId) && !FeatureCollectorConstants.TEAM_ID_ANY.equalsIgnoreCase(sTeamId)) {
			query = and(query) + "sTeamID eq " + quote(sTeamId);
		}

		if (!StringUtils.isEmpty(sProjectId) && !FeatureCollectorConstants.PROJECT_ID_ANY.equalsIgnoreCase(sProjectId)) {
			query = and(query) + "sProjectID eq " + quote(sProjectId);
		}
		return query;
	}
}
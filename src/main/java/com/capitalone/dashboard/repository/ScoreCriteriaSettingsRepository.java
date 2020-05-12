package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.score.ScoreMetric;
import com.capitalone.dashboard.model.score.ScoreValueType;
import com.capitalone.dashboard.model.score.settings.ScoreCriteriaSettings;

/**
 * Repository for {@link ScoreCriteriaSettings} data.
 */
public interface ScoreCriteriaSettingsRepository extends QueryRepository<ScoreMetric> {

    /**
     * Finds {@link ScoreCriteriaSettings}s for a given type {@link com.capitalone.dashboard.model.score.ScoreValueType}.
     *
     * @param type Score Value Type
     * @return a {@link ScoreCriteriaSettings}
     */
    ScoreCriteriaSettings findByType(ScoreValueType type);
    ScoreCriteriaSettings findAllByType(ScoreValueType type);

}

package com.capitalone.dashboard.event;

import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Dashboard;
import com.capitalone.dashboard.model.EnvironmentStage;
import com.capitalone.dashboard.model.Pipeline;
import com.capitalone.dashboard.repository.CollectorItemRepository;
import com.capitalone.dashboard.repository.CollectorRepository;
import com.capitalone.dashboard.repository.PipelineRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import java.util.List;

public abstract class HygieiaMongoEventListener<T> extends AbstractMongoEventListener<T> {

    protected final CollectorItemRepository collectorItemRepository;
    protected final PipelineRepository pipelineRepository;
    protected final CollectorRepository collectorRepository;

    public HygieiaMongoEventListener(CollectorItemRepository collectorItemRepository,
                                     PipelineRepository pipelineRepository,
                                     CollectorRepository collectorRepository) {
        this.collectorItemRepository = collectorItemRepository;
        this.pipelineRepository = pipelineRepository;
        this.collectorRepository = collectorRepository;
    }

    private Collector getProductCollector(){
        List<Collector> productCollectors = collectorRepository.findByCollectorType(CollectorType.Product);
        if(productCollectors.isEmpty()){
            return null;
        }
        return productCollectors.get(0);
    }

    /**
     * Finds the team dashboard collectoritem by dashboard id and product collectorid
     * @param teamDashboard
     * @return
     */
    protected CollectorItem getTeamDashboardCollectorItem(Dashboard teamDashboard) {
        Collector productColl = getProductCollector();
        if (productColl == null) {
            return null;
        } else {
            ObjectId productCollectorId = productColl.getId();
            ObjectId dashboardId = teamDashboard.getId();
            return collectorItemRepository.findTeamDashboardCollectorItemsByCollectorIdAndDashboardId(productCollectorId, dashboardId.toString());
        }
    }

    /**
     * Finds or creates a pipeline for a dashboard
     * @param teamDashboard
     * @return
     */
    protected Pipeline getOrCreatePipeline(Dashboard teamDashboard) {
        CollectorItem teamDashboardCollectorItem = getTeamDashboardCollectorItem(teamDashboard);
        return getOrCreatePipeline(teamDashboardCollectorItem);
    }

    /**
     * Finds or creates a pipeline for a dashboard collectoritem
     * @param collectorItem
     * @return
     */
    protected Pipeline getOrCreatePipeline(CollectorItem collectorItem) {
        Pipeline pipeline = pipelineRepository.findByCollectorItemId(collectorItem.getId());
        if(pipeline == null){
            pipeline = new Pipeline();
            pipeline.setCollectorItemId(collectorItem.getId());
        }
        return pipeline;
    }

    /**
     * Finds or creates a new environment stage for a pipeline
     * @param pipeline
     * @param pseudoEnvironmentName
     * @return
     */
    protected EnvironmentStage getOrCreateEnvironmentStage(Pipeline pipeline, String pseudoEnvironmentName){
        EnvironmentStage stage = pipeline.getEnvironmentStageMap().get(pseudoEnvironmentName);
        if(stage == null){
            stage = new EnvironmentStage();
            pipeline.getEnvironmentStageMap().put(pseudoEnvironmentName, stage);
        }
        return stage;
    }
}

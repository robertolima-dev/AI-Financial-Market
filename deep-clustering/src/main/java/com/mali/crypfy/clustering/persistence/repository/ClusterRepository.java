package com.mali.crypfy.clustering.persistence.repository;

import com.mali.crypfy.clustering.persistence.entity.Cluster;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClusterRepository extends MongoRepository<Cluster,String> {

    public List<Cluster> findByShapeSize(int shapeSize);
    public List<Cluster> findByShapeSizeAndName(int shapeSize,String name);
    public Cluster findById(int id);
    public List<Cluster> findByName(String name);
    public Cluster findFirstByName(String name);
}

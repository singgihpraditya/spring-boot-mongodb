package id.co.singgih.springboot.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import id.co.singgih.springboot.mongodb.entity.SmallFile;

public interface SmallFileRepository extends MongoRepository<SmallFile, String> {
}

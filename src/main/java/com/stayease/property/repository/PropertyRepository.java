package com.stayease.property.repository;

import com.stayease.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access for Property. Same JpaRepository superpowers as UserRepository.
 *
 * findByOwnerId is another "derived query": Spring turns the method name into
 * SELECT * FROM properties WHERE owner_id = ?. We'll use it to support
 * GET /api/properties?ownerId=5 (list all properties for one owner).
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerId(Long ownerId);
}

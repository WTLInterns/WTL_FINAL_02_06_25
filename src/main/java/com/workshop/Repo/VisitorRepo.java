package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Visitors;

@Repository
public interface VisitorRepo extends JpaRepository<Visitors, Integer>{
    

    
}

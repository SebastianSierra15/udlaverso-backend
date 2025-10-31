package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Integer> {

}

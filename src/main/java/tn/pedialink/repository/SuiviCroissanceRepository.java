package tn.pedialink.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.pedialink.model.SuiviCroissance;
import java.util.List;

@Repository
public interface SuiviCroissanceRepository extends MongoRepository<SuiviCroissance, String> {
    List<SuiviCroissance> findByPatientIdOrderByDateMesureDesc(String patientId);
}

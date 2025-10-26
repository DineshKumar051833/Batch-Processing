package org.example.java.xmlbatch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final CpeRepo repo;

    public Service(CpeRepo repo) {
        this.repo = repo;
    }

    public Page<CpeEntity> getAll(Pageable pageable){
        return repo.findAll(pageable);
    }

    public List<CpeEntity> searchCpeS(
            String title,
            String cpe_22_uri,
            String cpe_23_uri,
            String deprecation_date
    ){
        LocalDate date;
        if (deprecation_date != null && !deprecation_date.isBlank()) date = LocalDate.parse(deprecation_date);
        else date = null;
        List<CpeEntity> entities = repo.findAll();
        return entities.stream()
                .filter(cpe -> title==null || cpe.getTitle() != null && cpe.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(cpe -> cpe_22_uri==null || cpe.getCpe22URI() != null && cpe.getCpe22URI().toLowerCase().contains(cpe_22_uri.toLowerCase()))
                .filter(cpe -> cpe_23_uri==null || cpe.getCpe23URI() != null && cpe.getCpe23URI().toLowerCase().contains(cpe_23_uri.toLowerCase()))
                .filter(cpe -> date==null ||
                        cpe.getCpe22DeprecationDate()!=null && cpe.getCpe22DeprecationDate().isBefore(date) ||
                        cpe.getCpe23DeprecationDate()!=null && cpe.getCpe23DeprecationDate().isBefore(date))
                .toList();
    }
}
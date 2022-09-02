package intern.sapo.be.service.impl;

import intern.sapo.be.dto.response.ImportInvoice.DetailsImportsInvoiceResponse;
import intern.sapo.be.dto.response.ImportInvoice.ImportResponse;
import intern.sapo.be.entity.Import;
import intern.sapo.be.entity.ImportsStatus;
import intern.sapo.be.repository.IImportRepo;
import intern.sapo.be.repository.IStatusRepo;
import intern.sapo.be.repository.ISupplierRepo;
import intern.sapo.be.repository.InventoryRepository;
import intern.sapo.be.service.IImportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportService implements IImportService {

    private final IImportRepo importRepo;

    private final ImportStatusService importStatusService;

    private final IStatusRepo statusRepo;
    private final ISupplierRepo supplierRepo;

    private final InventoryRepository inventoryRepository;

    private final EntityManager entityManager;

    @Override
    public List<Import> findAll() {
        return importRepo.findAll();
    }

    @Override
    public List<ImportResponse> findAllImportDTO() {
        Query query = entityManager.createNamedQuery("getFeaturedInventoryDTO");
        return (List<ImportResponse>) query.getResultList();
    }

    @Override
    public Import save(Import importField) {
        Import anImport = importRepo.save(importField);
        ImportsStatus importsStatus = new ImportsStatus();
        updateStatus(anImport.getId(), "IMPORT01");
        return anImport;
    }

    public boolean checkStatusExitsInImport(Integer importId, Integer statusId) {
        var statusInImport = importStatusService.findByImportIdAndStatusId(importId, statusId);
        return statusInImport == null;
    }

    public void updateStatus(Integer importId, String code) {
        ImportsStatus importsStatus = new ImportsStatus();
        Integer statusId = statusRepo.findByCode(code).getId();
        if (checkStatusExitsInImport(importId, statusId)) {
            importsStatus.setImportId(importId);
            importsStatus.setStatusId(statusId);
            importsStatus.setCreateAt(Timestamp.from(Instant.now()));
            importStatusService.save(importsStatus);
        }
    }

    @Override
    public void updateStatusImport(Integer importId, String chooses) {
        Import anImport = importRepo.findById(importId).orElseThrow(() -> new IllegalArgumentException(("id not found: " + importId)));
        switch (chooses) {
            case "paidPayment": {
                updateStatus(importId, "IMPORT02");
                anImport.setIsPaid(true);
                break;
            }
            case "importWarehouse": {
                updateStatus(importId, "IMPORT03");
                anImport.setIsImport(true);
                break;
            }
            case "paidPaymentAndImportWarehouse": {
                updateStatus(importId, "IMPORT02");
                anImport.setIsImport(true);
                updateStatus(importId, "IMPORT03");
                anImport.setIsPaid(true);
                break;
            }
        }
        if (anImport.getIsImport() && anImport.getIsPaid()) {
            anImport.setIsDone(true);
        }
        importRepo.save(anImport);
    }


    @Override
    public DetailsImportsInvoiceResponse getDetailInvoiceByCode(String code) {
        DetailsImportsInvoiceResponse dImportResponse = new DetailsImportsInvoiceResponse();
        var im = importRepo.findByCode(code).orElseThrow(() -> new IllegalArgumentException(("code not found: " + code)));
        dImportResponse.setAnImport(im);
        dImportResponse.setSupplier(supplierRepo.findById(im.getSupplierId()).get());
        dImportResponse.setInventoryName(inventoryRepository.findById(im.getInventoryId()).get().getName());
        return dImportResponse;
    }
}

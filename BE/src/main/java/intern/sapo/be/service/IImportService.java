package intern.sapo.be.service;

import intern.sapo.be.dto.request.ImportDTO;
import intern.sapo.be.entity.Import;

import java.util.List;

public interface IImportService {

    List<Import> findAll();

    List<ImportDTO> findAllImportDTO();
    Import save(Import importField);

}
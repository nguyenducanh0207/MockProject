package intern.sapo.be.service.impl;

import intern.sapo.be.base.BaseService;
import intern.sapo.be.base.IBaseRepo;
import intern.sapo.be.base.IBaseService;
import intern.sapo.be.dto.request.SuppliersDTO;
import intern.sapo.be.entity.Supplier;
import intern.sapo.be.exception.AlreadyExistsException;
import intern.sapo.be.repository.ISupplierRepo;
import intern.sapo.be.service.ISupplierService;
import intern.sapo.be.util.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {

    private final ISupplierRepo supplierRepo;

    private final Utils utils;

    @Override
    public Page<Supplier> findAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        if (sortDir != null) {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            return supplierRepo.findAll(PageRequest.of(pageNumber - 1, pageSize, sort));
        }
        return supplierRepo.findAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepo.findAll();
    }

    @Override
    public Supplier create(Supplier request, BindingResult bindingResult) {
        supplierRepo.findByPhone(request.getPhone()).ifPresent(e -> {
            throw new AlreadyExistsException("phone number has already exits");
        });
        supplierRepo.findByEmail(request.getEmail()).ifPresent(e -> {
            throw new AlreadyExistsException("email has already exits");
        });
        supplierRepo.findByCode(request.getCode()).ifPresent(e -> {
            throw new AlreadyExistsException("code has already exits");
        });
        if (bindingResult.hasErrors()) {
            throw utils.invalidInputException(bindingResult);
        }
        return supplierRepo.save(request);

    }

    @Override
    public Supplier findById(int id) {
        return supplierRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("id not found: " + id));
    }

    @Override
    public Supplier update(Supplier request, BindingResult bindingResult) {
        var t = supplierRepo.findById(request.getId()).orElseThrow(() -> new IllegalArgumentException(("id not found: " + request.getId())));
        if (!t.getPhone().equals(request.getPhone())) {
            supplierRepo.findByPhone(request.getPhone()).ifPresent(e -> {
                throw new AlreadyExistsException("phone number has already exits");
            });
        } else if (!t.getEmail().equals(request.getEmail())) {
            supplierRepo.findByEmail(request.getEmail()).ifPresent(e -> {
                throw new AlreadyExistsException("email has already exits");
            });
        }
        BindingResult result = utils.getListResult(bindingResult, request);
        if (result.hasErrors()) {
            throw utils.invalidInputException(result);
        } else {
            request.setCreatedAt(t.getCreatedAt());
            return supplierRepo.save(request);
        }
    }

    @Override
    public void softDeleteAllIds(List<Integer> listId) {
        listId.forEach(id -> supplierRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(("id not found: " + id))));
        supplierRepo.softDeleteAllIds(listId);
    }

    @Override
    public void updateStatusFalseTransaction(List<Integer> listId) {
        listId.forEach(id -> supplierRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(("id not found: " + id))));
        supplierRepo.updateStatusFalseTransaction(listId);
    }

    @Override
    public void updateStatusTrueTransaction(List<Integer> listId) {
        listId.forEach(id -> supplierRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(("id not found: " + id))));
        supplierRepo.updateStatusTrueTransaction(listId);
    }
}
package com.daw.webapp07.service;

import com.daw.webapp07.DTO.InversionDTO;
import com.daw.webapp07.model.Inversion;
import com.daw.webapp07.repository.InversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InversionService {
    @Autowired
    InversionRepository inversionRepository;

    public Optional<Inversion> getInversion(Long id){
        return inversionRepository.findById(id);
    }
}

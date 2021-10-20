package com.example.catalogservice.service;

import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public Iterable<CatalogEntity> getAllCatalogs(){
        return catalogRepository.findAll();
    }

    public CatalogEntity getCatalogByProduct(String productId){
        return catalogRepository.findByProductId(productId);
    }
}

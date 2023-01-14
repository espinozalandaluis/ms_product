package com.bootcamp.java.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private String id;
    private Integer idProduct;
    private String description;
    private Double transactionFee;
    private ProductTypeDTO productTypeDTO;
    private ProductSubTypeDTO productSubTypeDTO;
}

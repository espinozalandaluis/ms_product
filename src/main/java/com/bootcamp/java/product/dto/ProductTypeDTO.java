package com.bootcamp.java.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeDTO {

    private String id;
    private Integer idProductType;
    private String description;

}

package com.adfecomm.adfecomm.payload;

import com.adfecomm.adfecomm.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private String imageUrl;
    private Integer quantity;
    private double price;
    private Category category;
}

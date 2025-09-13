package com.egin.ecommerce.category;

import com.egin.ecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Category {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
//    Bir kategoriyi sildiğimiz zaman o kategoride bulunan ürünlerin silinmesi için CascadeType.REMOVE olarak seçtik

    private List<Product> products;
}

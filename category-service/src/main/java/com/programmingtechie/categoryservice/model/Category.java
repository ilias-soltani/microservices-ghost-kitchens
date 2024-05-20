package com.programmingtechie.categoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Category {

    @Id
    private String id;
    private String name;

}

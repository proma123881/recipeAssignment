package com.abnamro.assignment.recipes.persistence.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {


    @Id
    @GenericGenerator(
            name = "sequenceGenerator",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "optimizer",
                            value = "pooled-lo"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "increment_size",
                            value = "1"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "hibernate_sequence"
                    )
            }
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequenceGenerator"
    )
    private Long id;
    @Column(name = "RECIPE_NAME", nullable = false)
    private String recipeName;
    private boolean isVegetarian;
    private Integer noOfServings;
    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ingredients")
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}

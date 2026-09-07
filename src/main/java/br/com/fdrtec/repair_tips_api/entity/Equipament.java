package br.com.fdrtec.repair_tips_api.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private String category;

    private String type;

    @ManyToMany
    @JoinTable(
        name = "equipament_part",
        joinColumns = @JoinColumn(name = "equipament_id"),
        inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    @Builder.Default
    private List<Part> parts = new ArrayList<>();
}
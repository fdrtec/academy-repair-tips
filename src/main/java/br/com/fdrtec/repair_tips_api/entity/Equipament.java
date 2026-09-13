package br.com.fdrtec.repair_tips_api.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE equipament SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Equipament extends BaseEntity {

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
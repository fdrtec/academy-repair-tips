package br.com.fdrtec.repair_tips_api.entity;

import jakarta.persistence.Entity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
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
@SQLDelete(sql = "UPDATE part SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Part extends BaseEntity {

    private String name;

    private String number;
}

package com.pradip.roommanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pradip.roommanagementsystem.dto.ERoles;
import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERoles name;

    @OneToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}

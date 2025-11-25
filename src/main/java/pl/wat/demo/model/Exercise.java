package pl.wat.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
public class Exercise {
    @Id
    @SequenceGenerator(
            name = "exercise_seq",
            sequenceName = "exercise_sequence",
            initialValue = 100,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_seq")
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "muscles_exercises",
            inverseJoinColumns = @JoinColumn(name = "muscle_id"),
            joinColumns = @JoinColumn(name = "exercise_id")
    )

    private List<Muscle> muscles;

    @Column(columnDefinition = "TEXT", nullable = true)
    @JsonIgnore
    private String description;

    @JsonIgnore
    private String image;
}

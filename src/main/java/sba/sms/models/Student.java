package sba.sms.models;


import sba.sms.models.Course;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;
import lombok.experimental.FieldDefaults;
//import org.h2.security.SHA256;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Table;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@Entity


public class Student {
    @Id
    @NonNull @Column(length = 50, unique = true) @GeneratedValue(strategy = GenerationType.IDENTITY)
    String email;
    @NonNull @Column(length = 50, name = "name")
    String name;
    @NonNull @Column(length = 50, name = "password")
    String password;


   @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))
            Set<Student> students = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }


}

package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Email(regexp = ".+@.+\\..+")
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Length(min = 5)
    private String password;

    @JsonIgnore
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Quiz> quizzes;

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.PERSIST)
    private List<Completed> completedList;

    public User() {
        this.quizzes = new ArrayList<>();
        this.role = "ROLE_USER";
        this.completedList = new ArrayList<>();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Completed> getCompletions() {
        return completedList;
    }

    public void setCompletions(List<Completed> completedList) {
        this.completedList = completedList;
    }

    public String getPassword() {
        return passwordEncoder().encode(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

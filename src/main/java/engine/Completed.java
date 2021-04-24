package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Completed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private int no;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private int id;

    @Column(name = "Time")
    private Date completedAt;

    public Completed(int quizId, Date completedAt, User user) {
        this.id = quizId;
        this.completedAt = completedAt;
        this.user = user;
    }

    public Completed(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

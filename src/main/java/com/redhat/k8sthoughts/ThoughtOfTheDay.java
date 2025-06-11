package com.redhat.k8sthoughts;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "k8s_thoughts")
public class ThoughtOfTheDay {
    @Id
    @GeneratedValue
    Long id;

    public String thought;

    public String author;

    public LocalDate day;

    public ThoughtOfTheDay() {
    }

    public ThoughtOfTheDay(String thought, String author, String day) {
        this.thought = thought;
        this.author = author;
        this.day = LocalDate.parse(day);
    }
    
    public ThoughtOfTheDay(String thought, String author, LocalDate day) {
        this.thought = thought;
        this.author = author;
        this.day = day;
    }

    public String getThought() {
        return thought;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "ThoughtOfTheDay{" +
                "id=" + id +
                ", thought='" + thought + '\'' +
                ", author='" + author + '\'' +
                ", day=" + day +
                '}';
    }
}
 

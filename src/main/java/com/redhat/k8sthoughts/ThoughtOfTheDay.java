package com.redhat.k8sthoughts;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "k8s_thoughts")
public class ThoughtOfTheDay extends PanacheEntity {
    public String thought;
    public String author;
    public LocalDate day;

    public ThoughtOfTheDay() {
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


}
 

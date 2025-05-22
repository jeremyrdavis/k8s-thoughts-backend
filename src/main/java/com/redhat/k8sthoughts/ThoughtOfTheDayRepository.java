package com.redhat.k8sthoughts;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ThoughtOfTheDayRepository implements PanacheRepository<ThoughtOfTheDay> {
}

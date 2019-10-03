package com.kekeek.api.service;

import com.kekeek.api.model.Visit;
import com.kekeek.api.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class VisitService {
    private VisitRepository visitRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Scheduled(fixedDelay = 3_600_000, initialDelay = 120_000)
    void clearStaleVisits() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
        Date date = Date.from(cutoffDate.atZone(ZoneId.systemDefault()).toInstant());
        visitRepository.deleteVisitsOlderThan(date);
    }

    public Visit addVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    public Collection<Visit> getTopVisits(int topVisitCount) {
        Collection<Visit> allVisits = visitRepository.findAll();

        Map<String, Integer> collapsedVisits = groupVisitsByIdentifier(allVisits);

        Map<Integer, Collection<String>> sortedVisits = sortVisitsBytDescendingCount(collapsedVisits);

        return extractTopVisits(sortedVisits, topVisitCount);
    }

    private Map<String, Integer> groupVisitsByIdentifier(Collection<Visit> visits) {
        Map<String, Integer> collapsedVisits = new HashMap<>();
        for (Visit visit: visits) {
            collapsedVisits.putIfAbsent(visit.getIdentifier(), 0);
            collapsedVisits.computeIfPresent(visit.getIdentifier(), (k, v) -> v + visit.getCounter());
        }

        return collapsedVisits;
    }

    private Map<Integer, Collection<String>> sortVisitsBytDescendingCount(Map<String, Integer> collapsedVisits) {
        TreeMap<Integer, Collection<String>> sortedVisits = new TreeMap<>(Collections.reverseOrder());
        for (String identifier: collapsedVisits.keySet()) {
            Integer counter = collapsedVisits.get(identifier);
            sortedVisits.putIfAbsent(counter, new HashSet<>(Collections.singletonList(identifier)));
            sortedVisits.computeIfPresent(counter, (k, v) -> addAndReturnCollection(v, identifier));
        }

        return sortedVisits;
    }

    private Collection<Visit> extractTopVisits(Map<Integer, Collection<String>> sortedVisits, int topVisitCount) {
        Collection<Visit> topVisits = new ArrayList<>();
        int index = 0;
        outerLoop:
        for (Integer counter: sortedVisits.keySet()) {
            Collection<String> identifiers = sortedVisits.get(counter);
            for (String identifier: identifiers) {
                if (index >= topVisitCount) {
                    break outerLoop;
                }
                Visit visit = new Visit();
                visit.setIdentifier(identifier);
                visit.setCounter(counter);
                topVisits.add(visit);
                index++;
            }
        }
        return topVisits;
    }

    private Collection<String> addAndReturnCollection(Collection<String> collection, String value) {
        collection.add(value);
        return collection;
    }
}

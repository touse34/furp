package com.furp.service;

import java.util.Map;
import java.util.Set;

public interface TeacherSkillService {
    public Set<Integer> findTeacherSkillsById(Integer teacherId);
    public Map<Integer, Set<Integer>> findAllTeacherSkillsAsMap();
    public Map<Integer, Set<Integer>> findAllAsMap();
}

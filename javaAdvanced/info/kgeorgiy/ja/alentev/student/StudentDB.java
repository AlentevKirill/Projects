package javaAdvanced.info.kgeorgiy.ja.alentev.student;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.GroupQuery;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StudentDB implements GroupQuery {
    @Override
    public List<Group> getGroupsByName(Collection<Student> collection) {
        return convertedStudentsToGroups(sortStudentsByName(collection));
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> collection) {
        return convertedStudentsToGroups(sortStudentsById(collection));
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> collection) {
        return collection.size() != 0 ?
                Objects.requireNonNull(collection.stream().
                                collect(Collectors.toMap(Student::getGroup, student -> 0, (count, s) -> count + 1)).
                                entrySet().
                                stream().
                                max(Map.Entry.<GroupName, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey())).
                                orElse(null)).
                        getKey() :
                null;
        /*return collection.size() != 0 ?
                getGroupsByName(collection).stream()
                        .max(Comparator.comparing((Group group) -> group.getStudents().size()).
                            thenComparing(Group::getName)).
                        get().
                        getName() :
                null;*/
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> collection) {
        return collection.size() != 0 ?
                Objects.requireNonNull(collection.stream().
                                collect(Collectors.toMap(Student::getGroup,
                                        student -> new HashSet<>(Collections.singleton(student.getFirstName())),
                                        (namesHashSet, oneName) -> {
                                            namesHashSet.add(oneName.iterator().next());
                                            return namesHashSet;
                                        })).
                                entrySet().
                                stream().
                                max(Map.Entry.<GroupName, HashSet<String>>comparingByValue(Comparator.comparingInt(HashSet::size))
                                        .thenComparing((key1, key2) -> key2.getKey().compareTo(key1.getKey()))).
                                orElse(null)).
                        getKey() :
                null;

        /*return collection.size() != 0 ?
                getGroupsByName(collection).stream().
                        max(Comparator.comparing((Group group) -> group.getStudents().stream().
                                        map(Student::getFirstName).
                                        distinct().
                                        count()).
                                thenComparing(Group::getName, Comparator.reverseOrder())).
                        get().
                        getName() :
                null;*/
    }

    @Override
    public List<String> getFirstNames(List<Student> list) {
        /*return list.stream().
                map(Student::getFirstName).
                collect(Collectors.toList());*/
        return get(list, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> list) {
        /*return list.stream().
                map(Student::getLastName).
                collect(Collectors.toList());*/
        return get(list, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> list) {
        return list.stream().
                map(Student::getGroup).
                collect(Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> list) {
        return /*list.stream().
                map(s -> s.getFirstName() + " " + s.getLastName()).
                collect(Collectors.toList());*/
        get(list, (Student student) -> student.getFirstName() + " " + student.getLastName());
    }

    //Уникальные имена
    @Override
    public Set<String> getDistinctFirstNames(List<Student> list) {
        return list.stream().
                map(Student::getFirstName).
                collect(Collectors.toSet());
    }

    @Override
    public String getMaxStudentFirstName(List<Student> list) {
        return list.size() != 0 ?
                list.stream().
                        max(Comparator.comparingInt(Student::getId)).
                        get().
                        getFirstName() :
                "";
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> collection) {
        return collection.stream().
                sorted(Comparator.comparingInt(Student::getId)).
                collect(Collectors.toList());
    }

    //Фамилия по возрастанию -> Имя по возрастанию -> id по убыванию
    @Override
    public List<Student> sortStudentsByName(Collection<Student> collection) {
        return collection.stream().
                sorted(Comparator.comparing(Student::getLastName, Comparator.reverseOrder()).
                        thenComparing(Student::getFirstName, Comparator.reverseOrder()).
                        thenComparingInt(Student::getId)).
                collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> collection, String s) {
        /*return collection.stream().
                filter(element -> element.getFirstName().equals(s)).
                sorted(Comparator.comparing(Student::getLastName, Comparator.reverseOrder())).
                collect(Collectors.toList());*/
        return findStudentBy(collection, Student::getFirstName, Student::getLastName, s);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> collection, String s) {
        /*return collection.stream().
                filter(element -> element.getLastName().equals(s)).
                sorted(Comparator.comparing(Student::getFirstName, Comparator.reverseOrder())).
                collect(Collectors.toList());*/
        return findStudentBy(collection, Student::getLastName, Student::getFirstName, s);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> collection, GroupName groupName) {
        return sortStudentsByName(collection).stream().
                filter(element -> element.getGroup().equals(groupName)).
                collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> collection, GroupName groupName) {
        return collection.stream().
                filter(element -> element.getGroup().equals(groupName)).
                collect(Collectors.toMap(Student::getLastName, Student::getFirstName,
                        (element1, element2) -> element1.compareTo(element2) < 0 ? element1 : element2));
    }

    private List<Group> convertedStudentsToGroups(List<Student> collection) {
        return collection.stream().
                collect(Collectors.toMap(Student::getGroup, student -> new ArrayList<>(Collections.singleton(student)), (studentsList, oneStudent) -> {
                    studentsList.add(oneStudent.iterator().next());
                    return studentsList;
                })).
                entrySet().
                stream().
                sorted(Map.Entry.comparingByKey()).
                map(pair -> new Group(pair.getKey(), pair.getValue())).
                collect(Collectors.toUnmodifiableList());
    }

    private List<String> get(List<Student> list,  java.util.function.Function<Student, String> function) {
        return list.stream().
                map(function).
                collect(Collectors.toList());
    }

    private List<Student> findStudentBy(Collection<Student> collection, java.util.function.Function<Student, String> function1,
                                        java.util.function.Function<Student, String> function2, String s) {
        return collection.stream().
                filter(student -> function1.apply(student).equals(s)).
                sorted(Comparator.comparing(function2, Comparator.reverseOrder())).
                collect(Collectors.toList());
    }
}
package com.romedawg.domain.todo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="todo")
public class Todo {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id ;

    @Column(name = "task_name")
    private String TaskName ;

    @Column(name = "task_description")
    private String TaskDescription ;

    @Column(name = "date")
    private Date Date;

    @Column(name = "active")
    private Boolean Active ;

    public Todo(Integer id, String taskName, java.util.Date date, Boolean active) {
        Id = id;
        TaskName = taskName;
        Date = date;
        Active = active;
    }

    public Todo() {

    }

    @Override
    public String toString() {
        return "Todo{" +
                "Id=" + Id +
                ", TaskName='" + TaskName + '\'' +
                ", TaskDescription='" + TaskDescription + '\'' +
                ", Date=" + Date +
                ", Active=" + Active +
                '}';
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        TaskDescription = taskDescription;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }
}

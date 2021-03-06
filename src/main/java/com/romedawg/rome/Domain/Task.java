package com.romedawg.rome.Domain;

import javax.persistence.*;

@Entity
@Table(name="task")
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    private String title;
    private String summary;
    private int priority;

//    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="owner_id")
//    private Owner owner;

    protected Task(){}

    public Task( String title, String description, int priority ){
//        this.id = id;
        this.title = title;
        this.summary = description;
        this.priority = priority;
//        this.owner = owner;
    }

    @Transient
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

//    public Owner getOwner() {
//        return owner;
//    }

//    public void setOwner(Owner owner) {
//        this.owner = owner;
//    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title + ", summary=" + summary
                + ", priority=" + priority + "]";
    }
}

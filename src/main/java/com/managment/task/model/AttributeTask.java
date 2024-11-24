package com.managment.task.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_attribute")
public class AttributeTask {
    
    @EmbeddedId
    private AttributeTaskKey attributeTaskKey;
    
    @Column(name = "attribute_value")
    private String attribute_value;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "taskid", referencedColumnName = "taskid")
    private Tasks tasks;

    public AttributeTask() {
    }

    public AttributeTask(AttributeTaskKey attributeTaskKey, String attribute_value, Tasks tasks) {
        this.attributeTaskKey = attributeTaskKey;
        this.attribute_value = attribute_value;
        this.tasks = tasks;
    }

    @Embeddable
    public static class AttributeTaskKey implements Serializable{

        @Column(name = "taskid")
        private Integer taskId;

        @Column(name = "attribute_name")
        private String attribute_name;

        public AttributeTaskKey() {
        }

        public AttributeTaskKey(Integer taskId, String attribute_name) {
            this.taskId = taskId;
            this.attribute_name = attribute_name;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public String getAttribute_name() {
            return attribute_name;
        }

        public void setAttribute_name(String attribute_name) {
            this.attribute_name = attribute_name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + taskId;
            result = prime * result + ((attribute_name == null) ? 0 : attribute_name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AttributeTaskKey other = (AttributeTaskKey) obj;
            if (taskId != other.taskId)
                return false;
            if (attribute_name == null) {
                if (other.attribute_name != null)
                    return false;
            } else if (!attribute_name.equals(other.attribute_name))
                return false;
            return true;
        }
    }

    public AttributeTaskKey getAttributeTaskKey() {
        return attributeTaskKey;
    }

    public void setAttributeTaskKey(AttributeTaskKey attributeTaskKey) {
        this.attributeTaskKey = attributeTaskKey;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
}

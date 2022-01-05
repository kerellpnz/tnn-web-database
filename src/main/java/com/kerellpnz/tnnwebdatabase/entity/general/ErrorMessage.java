package com.kerellpnz.tnnwebdatabase.entity.general;


import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "errormessage")
public class ErrorMessage extends BaseEntity {

    @Column
    @NotBlank(message = "Не может быть пустым!")
    private String shortDescription;

    @Column
    @NotBlank(message = "Не может быть пустым!")
    private String description;

    @Column
    private String inspectorName;

    public ErrorMessage() {}

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }
}

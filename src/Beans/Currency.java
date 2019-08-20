/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Miguel
 */
public class Currency implements Serializable {

    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty value;
    //String name;
    //double value;

    private PropertyChangeSupport propertySupport;

    //Constructors with Property
    public Currency(Integer id, String name, double value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleDoubleProperty(value);
        this.id = new SimpleIntegerProperty(id);
    }

    /*public Currency() {
        propertySupport = new PropertyChangeSupport(this);
    }*/
    public String getName() {
        return name.get();
    }

    public void setName(String Name) {
        this.name.set(Name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public Double getValue() {
        return value.get();
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public Integer getID() {
        return id.get();
    }
}

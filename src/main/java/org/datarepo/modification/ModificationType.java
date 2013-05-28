package org.datarepo.modification;

public enum  ModificationType {
    UPDATE,  //update a single property
    MODIFY,  //modify an entire object, remove and re-add it
    ADD      //Add a new object
}

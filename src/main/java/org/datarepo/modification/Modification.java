package org.datarepo.modification;


public interface Modification <KEY, ITEM> {

     void modification(ModificationEvent event);

}

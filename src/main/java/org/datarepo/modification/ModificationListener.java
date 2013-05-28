package org.datarepo.modification;


public interface ModificationListener<KEY, ITEM> {

     void modification(ModificationEvent event);

}

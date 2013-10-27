package org.boon.modification;


public interface ModificationListener<KEY, ITEM> {

    void modification(ModificationEvent event);

}

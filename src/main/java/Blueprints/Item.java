/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Blueprints;

import java.util.Objects;

/**
 *
 * @author eiker
 */
public class Item implements Comparable<Item>{
    private final String subtypeId;
    private final String typeId;

    public String getSubtypeId() {
        return subtypeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public Item(String subtypeId, String typeId) {
        this.subtypeId = subtypeId;
        this.typeId = typeId;
    } 
    
    @Override
    public int compareTo(Item anotherItem) {
        return this.getSubtypeId().compareTo(anotherItem.getSubtypeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtypeId + typeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.subtypeId, other.subtypeId)) {
            return false;
        }
        if (!Objects.equals(this.typeId, other.typeId)) {
            return false;
        }
        return true;
    }
}

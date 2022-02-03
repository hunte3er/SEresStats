/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unused;

import java.util.*;
import org.w3c.dom.*;

public final class XmlUtil {
  private XmlUtil(){}

  public static List<Node> asList(NodeList n) {
    return n.getLength()==0?
      Collections.<Node>emptyList(): new NodeListWrapper(n);
  }
  static final class NodeListWrapper extends AbstractList<Node>
  implements RandomAccess {
    private final NodeList list;
    NodeListWrapper(NodeList l) {
      list=l;
    }
    public Node get(int index) {
      return list.item(index);
    }
    public int size() {
      return list.getLength();
    }
  }
  
  public static List<Element> getChildElements(Element ele) {
//    Assert.notNull(ele, "Element must not be null");
    NodeList nl = ele.getChildNodes();
    List<Element> childEles = new ArrayList<>();
    for (int i = 0; i < nl.getLength(); i++) {
      Node node = nl.item(i);
      if (node instanceof Element) {
        childEles.add((Element) node);
      }
    }
    return childEles;
  }
  
  public static List<Element> nodeListToEleList(NodeList nl) {
    List<Element> childEles = new ArrayList<>();
    for (int i = 0; i < nl.getLength(); i++) {
      Node node = nl.item(i);
      if (node instanceof Element) {
        childEles.add((Element) node);
      }
    }
    return childEles;
  }
}
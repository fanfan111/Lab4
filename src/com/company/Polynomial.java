package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**.
 * Created by forandroid on 16-9-21.expression between +,witch means * contained
 *
 */
public class Polynomial implements Cloneable {

  /**.
   * symbol: (
   */
  private static final char SYMBOL_LEFT = '(';

  /**.
   * symbol: )
   */
  private static final char SYMBOL_RIGHT = ')';

  /**.
   * symbol: -
   */
  private static final char SYMBOL_MINUS = '-';

  /**.
   * symbol: ^
   */
  private static final char SYMBOL_POWER = '^';

  /**.
   * Member variable: prenum
   */
  private transient int prenum;


  // the Item is expression between *

  /**.
   * Member variable: item
   */
  private transient List<ItemALongName> item;

  /**.
   * @return the prenum
   */
  public final int getPrenum() {
    return prenum;
  }


  /**.
   * @param prenumParam the prenum to set
   */
  public final void setPrenum(final int prenumParam) {
    this.prenum = prenumParam;
  }


  /**.
   * @return the item
   */
  public final List<ItemALongName> getItem() {
    return item;
  }


  /**.
   * @param itemParam the item to set
   */
  public final void setItem(final List<ItemALongName> itemParam) {
    this.item = itemParam;
  }


  /**.
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException will throw CloneNotSupportedException
   */
  public final Polynomial copy() {
    final Polynomial cloned = new Polynomial();
    cloned.setPrenum(prenum);
    for (final ItemALongName i : getItem()) {
      cloned.getItem().add((ItemALongName) i.copy());
    }
    return cloned;
  }


  /**.
   * Construction method
   */
  Polynomial() {
    setPrenum(1);
    setItem(new ArrayList<ItemALongName>());
  }

  /**.
   * @param name param1
   * @param power param2
   */
  private void createVar(final String name, final int power) {
    getItem().add(new ItemALongName(name, power));
  }

  /**.
   * @param name param1
   * @return Item
   */
  private ItemALongName findVariable(final String name) {
    for (int i = 0; i < getItem().size(); i++) {
      if (getItem().get(i).getName().equals(name)) {
        return getItem().get(i);
      }
    }
    return null;
  }

  /**.
   * @param name param1
   * @param power param2
   */
  private void changeExp(final String name, final int power) {
    final ItemALongName itemLocal = findVariable(name);
    if (itemLocal == null) {
      createVar(name, power);
    } else {
      itemLocal.setPower(itemLocal.getPower() + power);
    }
  }

  /**.
   * @param string param1
   * @param num param2
   * @return char
   */
  private char nstring(final String string, final int num) {
    return string.charAt(num);
  }

  /**.
   * @param string param1
   * @return char
   */
  private char firstString(final String string) {
    return nstring(string, 0);
  }

  /**.
   * @param string param1
   * @return int
   */
  private int findNumber(final String string) {
    int anspos = 0;
    if (firstString(string) == SYMBOL_MINUS) {
      anspos++;
      if (!(nstring(string, anspos) <= '9'
          && nstring(string, anspos) >= '0')) {
        return -1;
      }
    }

    for (; anspos < string.length(); anspos++) {
      if (!(nstring(string, anspos) <= '9'
          && nstring(string, anspos) >= '0')) {
        break;
      }
    }

    return Integer.parseInt(string.substring(0, anspos));
  }

  /**.
   * @param string param1
   * @param start param2
   * @return int
   */
  private int howLongTheNumber(final String string, final int start) {
    int tmpStart = start;
    if (nstring(string, tmpStart) == SYMBOL_MINUS) {
      tmpStart++;
    }
    for (int i = tmpStart; i < string.length(); i++) {
      if (!(nstring(string, i) <= '9' && nstring(string, i) >= '0')) {
        return i;
      }
    }
    return string.length();
  }

  /**.
   * @param string param1
   * @return int
   */
  private int findRightBraket(final String string) {
    int num = 0;
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) == SYMBOL_LEFT) {
        num++;
      }
      if (string.charAt(i) == SYMBOL_RIGHT) {
        num--;
        if (num == 0) {
          return i;
        }
      }
    }

    return string.length();
  }

  /**.
   * @param string param1
   * @return String
   */
  private String bracketExpressionFromLeft(final String string) {
    return string.substring(0, (1 + findRightBraket(string)));
  }

  /**.
   * @param name param1
   * @param power param2
   * @return int
   */
  private int havePowerOrNotJudge(final String name, final String power) {
    if (firstString(power) == SYMBOL_POWER) {
      changeExp(name, findNumber(power.substring(1)));
      return name.length() + howLongTheNumber(power, 1);
    } else {
      changeExp(name, 1);
      return name.length();
    }
  }

  /**.
   * @param name param1
   * @param power param2
   * @return int
   */
  private int havePowerOrNot(final String name, final String power) {
    if ("".equals(power)) {
      return havePowerOrNotJudge(name, "1");
    } else {
      return havePowerOrNotJudge(name, power);
    }

  }

  /**.
   * @param string param1
   * @return int
   * @throws Exception will throw Exception
   */
  private int numberAddToList(final String string) throws Exception {
    final char firstChar = firstString(string);
    if ('a' <= firstChar && firstChar <= 'z') {
      return havePowerOrNot(string.substring(0, 1), string.substring(1));
    } else if (firstChar == SYMBOL_LEFT) {
      return havePowerOrNot(bracketExpressionFromLeft(string),
          string.substring(findRightBraket(string) + 1));
    } else {
      throw new IllegalArgumentException("not legal"
          + " in number-started expression");
    }

  }

  /**.
   * @param string param1
   * @throws Exception will throw Exception
   */
  private void startWithNumber(final String string) throws Exception {
    final int prenumLocal = findNumber(string);
    this.setPrenum(this.getPrenum() * prenumLocal);
    for (int i = howLongTheNumber(string, 0); i < string.length();) {
      i += numberAddToList(string.substring(i));
    }
  }

  /**.
   * @param string param1
   * @return String
   */
  private String symName(final String string) {
    final char firstChar = firstString(string);
    if (firstChar == SYMBOL_LEFT) {
      return string.substring(0, (findRightBraket(string) + 1));
    } else {
      final int pos1 = string.indexOf('^');
      final int pos2 = string.indexOf('(');
      if (pos1 == -1 && pos2 == -1) {
        return string;
      }
      if (pos1 == -1) {
        return string.substring(0, pos2);
      }
      if (pos2 == -1) {
        return string.substring(0, pos1);
      }
      return string.substring(0, Math.min(pos1, pos2));
    }
  }

  /**.
   * @param string param1
   * @return int
   */
  private int symbolAddToList(final String string) {
    final String name = symName(string);
    return havePowerOrNot(name, string.substring(name.length()));
  }

  /**.
   * @param string param1
   */
  private void startWithSymbol(final String string) {
    for (int i = 0; i < string.length();) {
      i += symbolAddToList(string.substring(i));
    }
  }

  /**.
   * @param string param1
   * @throws Exception will throw Exception
   */
  private void minmor(final String string) throws Exception {
    final char first = firstString(string);
    if (first >= '0' && first <= '9' || first == '-') {
      startWithNumber(string);
    }
    if ('a' <= first && first <= 'z' || first == '(') {
      startWithSymbol(string);
    }
  }

  /**.
   * @param origin param1
   * @param sym param2
   * @return ArrayList
   */
  private List<String> bracketSplit(final String origin, final char sym) {
    final List<String> ans = new ArrayList<String>();
    int num = 0;
    int startPoint = 0;
    for (int i = 0; i < origin.length(); i++) {
      if (nstring(origin, i) == SYMBOL_LEFT) {
        num++;
      }
      if (nstring(origin, i) == SYMBOL_RIGHT) {
        num--;
      }
      if (nstring(origin, i) == sym && num == 0) {
        ans.add(origin.substring(startPoint, i));
        startPoint = i + 1;
      }
    }
    ans.add(origin.substring(startPoint, origin.length()));
    return ans;

  }


  /**.
   * @param string param1
   * @param posOrNeg param2
   * @throws Exception will throw Exception
   */
  public final void morpheme(final String string, final int posOrNeg)
      throws Exception {
    this.setPrenum(posOrNeg);
    for (final String s : bracketSplit(string, '*')) {
      minmor(s);
    }

    this.getItem().sort(new Comparator<ItemALongName>() {
      /**.
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       * @param item1 param1
       * @param item2 param2
       * @return int
       */
      @Override
      public int compare(final ItemALongName item1, final ItemALongName item2) {
        return item1.getName().compareTo(item2.getName());
      }
    });
  }

  /**.
   * @param itemParam param1
   * @return Poly
   */
  public final Polynomial remove(final ItemALongName itemParam) {
    final Polynomial ans = new Polynomial();
    ans.setPrenum(this.getPrenum());
    ans.setItem(this.getItem());
    ans.getItem().remove(itemParam);
    return ans;

  }

  /**.
   *
   */
  public final void printOut() {
    System.out.println(getPrenum());
    for (final ItemALongName i : getItem()) {
      System.out.printf("%s %s\n", i.getName(), i.getPower());
    }

  }
}


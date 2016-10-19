package com.company;

import java.util.ArrayList;
import java.util.Comparator;

/**.
 * Created by forandroid on 16-9-21.expression between +,witch means * contained
 *
 */
public class Poly implements Cloneable {

  /**.
   * Member variable: prenum
   */
  private int prenum;


  // the Item is expression between *

  /**.
   * Member variable: it
   */
  private ArrayList<Item> it;

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
   * @return the it
   */
  public final ArrayList<Item> getIt() {
    return it;
  }


  /**.
   * @param itParam the it to set
   */
  public final void setIt(final ArrayList<Item> itParam) {
    this.it = itParam;
  }


  /**.
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException will throw CloneNotSupportedException
   */
  public final Object clone() throws CloneNotSupportedException {
    Poly cloned = new Poly();
    cloned.setPrenum(prenum);
    for (Item i : getIt()) {
      cloned.getIt().add((Item) i.clone());
    }
    return cloned;
  }


  /**.
   * Construction method
   */
  Poly() {
    setPrenum(1);
    setIt(new ArrayList<Item>());
  }

  /**.
   * @param name param1
   * @param power param2
   */
  private void createVar(final String name, final int power) {
    getIt().add(new Item(name, power));
  }

  /**.
   * @param name param1
   * @return Item
   */
  private Item findVariable(final String name) {
    for (int i = 0; i < getIt().size(); i++) {
      if (getIt().get(i).getName().equals(name)) {
        return getIt().get(i);
      }
    }
    return null;
  }

  /**.
   * @param name param1
   * @param power param2
   */
  private void changeExp(final String name, final int power) {
    Item item = findVariable(name);
    if (item != null) {
      item.setPower(item.getPower() + power);
    } else {
      createVar(name, power);
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
    if (firstString(string) == '-') {
      anspos++;
      if (!((nstring(string, anspos) <= '9'
          && nstring(string, anspos) >= '0'))) {
        return -1;
      }
    }

    for (; anspos < string.length(); anspos++) {
      if (!((nstring(string, anspos) <= '9'
          && nstring(string, anspos) >= '0'))) {
        break;
      }
    }
    int ans = Integer.parseInt(string.substring(0, anspos));
    return ans;
  }

  /**.
   * @param string param1
   * @param start param2
   * @return int
   */
  private int howLongTheNumber(final String string, final int start) {
    int tmpStart = start;
    if (nstring(string, tmpStart) == '-') {
      tmpStart++;
    }
    for (int i = tmpStart; i < string.length(); i++) {
      if (!((nstring(string, i) <= '9' && nstring(string, i) >= '0'))) {
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
      if (string.charAt(i) == '(') {
        num++;
      }
      if (string.charAt(i) == ')') {
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
    if ((firstString(power)) == '^') {
      changeExp(name, (findNumber(power.substring(1))));
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
    if (power.equals("")) {
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
    char ch = firstString(string);
    if ('a' <= ch && ch <= 'z') {
      return havePowerOrNot(string.substring(0, 1), string.substring(1));
    } else if (ch == '(') {
      return havePowerOrNot(bracketExpressionFromLeft(string),
          string.substring(findRightBraket(string) + 1));
    } else {
      throw new Exception("not legal in number-started expression");
    }

  }

  /**.
   * @param string param1
   * @throws Exception will throw Exception
   */
  private void startWithNumber(final String string) throws Exception {
    int prenumLocal = findNumber(string);
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
    char ch = firstString(string);
    if (ch == '(') {
      return string.substring(0, (findRightBraket(string) + 1));
    } else {
      int pos1 = string.indexOf('^');
      int pos2 = string.indexOf('(');
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
    String name = symName(string);
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
    char first = firstString(string);
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
  private ArrayList<String> bracketSplit(final String origin, final char sym) {
    ArrayList<String> ans = new ArrayList<String>();
    int num = 0;
    int startPoint = 0;
    for (int i = 0; i < origin.length(); i++) {
      if (nstring(origin, i) == '(') {
        num++;
      }
      if (nstring(origin, i) == ')') {
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
    for (String s : bracketSplit(string, '*')) {
      minmor(s);
    }

    this.getIt().sort(new Comparator<Item>() {
      @Override
      public int compare(final Item o1, final Item o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
  }

  /**.
   * @param itParam param1
   * @return Poly
   */
  public final Poly remove(final Item itParam) {
    Poly ans = new Poly();
    ans.setPrenum(this.getPrenum());
    ans.setIt(this.getIt());
    ans.getIt().remove(itParam);
    return ans;

  }

  /**.
   *
   */
  public final void printOut() {
    System.out.println(getPrenum());
    for (Item i : getIt()) {
      System.out.printf("%s %s\n", i.getName(), i.getPower());
    }

  }
}


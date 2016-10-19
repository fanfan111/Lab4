package com.company;

import java.util.ArrayList;
import java.util.List;

/**.
 * Created by forandroid on 16-9-23.
 */
public class Union {

  /**.
   * symbol: (
   */
  private static final char SYMBOL_LEFT = '(';

  /**.
   * symbol: )
   */
  private static final char SYMBOL_RIGHT = ')';

  /**.
   * symbol: +
   */
  private static final char SYMBOL_PLUS = '+';

  /**.
   * symbol: -
   */
  private static final char SYMBOL_MINUS = '-';

  /**.
   * symbol: *
   */
  private static final char SYMBOL_MUL = '*';

  /**.
   * Member variable: ans
   */
  private transient List<Polynomial> ans;

  /**.
   * @return the ans
   */
  public final List<Polynomial> getAns() {
    return ans;
  }

  /**.
   * @param ansParam the ans to set
   */
  public final void setAns(final List<Polynomial> ansParam) {
    this.ans = ansParam;
  }

  /**.
   * Construction method
   */
  Union() {
    setAns(new ArrayList<Polynomial>());
  }

  /**.
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException will throw CloneNotSupportedException
   */

  public final Object copy() throws CloneNotSupportedException {

    final Union cloned = new Union();
    for (final Polynomial p : getAns()) {
      cloned.getAns().add((Polynomial) p.copy());
    }
    return cloned;
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
   * Created by forandroid.
   */
  private class MorphemeObj {

    /**.
     * Member variable: morpheme
     */
    private transient String morpheme;

    /**.
     * Member variable: flag
     */
    private transient int flag;

    /**.
     * @return the morpheme
     */
    public final String getMorpheme() {
      return morpheme;
    }

    /**.
     * @param morphemeParam the morpheme to set
     */
    public final void setMorpheme(final String morphemeParam) {
      this.morpheme = morphemeParam;
    }

    /**.
     * @return the flag
     */
    public final int getFlag() {
      return flag;
    }

    /**.
     * @param flagParam the flag to set
     */
    public final void setFlag(final int flagParam) {
      this.flag = flagParam;
    }

    /**.
     * @param morphemeInput param1
     * @param flagInput param2
     */
    MorphemeObj(final String morphemeInput, final int flagInput) {
      this.setFlag(flagInput);
      this.setMorpheme(morphemeInput);
    }
  }

  /**.
   * @param string param1
   * @return ArrayList
   * @throws Exception will throw Exception
   */
  private List<MorphemeObj> splitMorpheme(final String string)
      throws Exception {
    int num = 0;
    int startPoint = 0;
    int flag;

    if (firstString(string) == SYMBOL_MINUS) {
      startPoint++;
      flag = -1;
    } else {
      flag = 1;
    }

    final ArrayList<MorphemeObj> ansLocal = new ArrayList<MorphemeObj>();
    for (int i = startPoint; i < string.length(); i++) {
      if (nstring(string, i) == SYMBOL_LEFT) {
        num++;
      } else if (nstring(string, i) == SYMBOL_RIGHT) {
        num--;
      } else if (nstring(string, i) == SYMBOL_PLUS && num == 0) {
        ansLocal.add(new MorphemeObj(string.substring(startPoint, i), flag));
        flag = 1;
        startPoint = i + 1;
      } else if (nstring(string, i) == SYMBOL_MINUS && num == 0
          && nstring(string, i - 1) != SYMBOL_MUL) {
        ansLocal.add(new MorphemeObj(string.substring(startPoint, i), flag));
        flag = -1;
        startPoint = i + 1;
      }
    }

    ansLocal.add(new MorphemeObj(string.substring(startPoint, string.length()),
        flag));
    if (num != 0) {
      throw new IllegalArgumentException("num = 0");
    }
    return ansLocal;
  }

  /**.
   * @param poly param1
   * @return Poly
   */
  private Polynomial equalsPoly(final Polynomial poly) {
    for (final Polynomial pre : getAns()) {
      if (pre.getItem().size() != poly.getItem().size()) {
        continue;
      }
      int flag = 0;
      for (int i = 0; i < poly.getItem().size(); i++) {
        if (!pre.getItem().get(i).getName()
            .equals(poly.getItem().get(i).getName())
            || pre.getItem().get(i).getPower()
            != poly.getItem().get(i).getPower()) {
          flag = 1;
          break;
        }
      }

      if (flag == 0) {
        return pre;
      }
    }

    return null;
  }

  // this method will DESTORY our ans
  /**.
   * this method will DESTORY our ans
   * @param tmp param1
   */
  private void mergeSameItem(final Polynomial tmp) {
    final Polynomial poly = equalsPoly(tmp);
    if (poly == null) {
      getAns().add(tmp);
    } else {
      poly.setPrenum(poly.getPrenum() + tmp.getPrenum());
      if (poly.getPrenum() == 0) {
        getAns().remove(poly);
      }
    }
  }

  /**.
   * @param string param1
   * @throws Exception will throw exception
   */
  public final void turnStringToList(final String string) throws Exception {
    for (final MorphemeObj i : splitMorpheme(string)) {
      final Polynomial tmp = new Polynomial();
      tmp.morpheme(i.getMorpheme(), i.getFlag());
      mergeSameItem(tmp);
    }

  }

  /**.
   * @return String
   */
  public final String listToString() {
    String string = "";
    if (getAns().isEmpty() || getAns().size() == 1
        && getAns().get(0).getPrenum() == 0) {
      string =  "0";
    } else {
      for (final Polynomial i : getAns()) {
        final int prenum = i.getPrenum();
        if (prenum != 0) {
          if (prenum < 0) {
            string = string.concat(String.valueOf(prenum));
          } else if ("".equals(string)) {
            string = String.valueOf(prenum);
          } else {
            string = string.concat("+").concat(String.valueOf(prenum));
          }

          for (final ItemALongName var : i.getItem()) {
            if (var.getPower() - 1 == 0) {
              string = string.concat("*").concat(var.getName());
            } else {
              string = string.concat("*").concat(var.getName()).concat("^")
                 .concat(String.valueOf(var.getPower()));
            }
          }
        }
      }
    }

    return string;
  }


  /**.
   * @param poly param1
   * @param vars param2
   * @return Polynomial
   * @throws Exception will throw Exception
   */
  private Polynomial symbolToNumber(final Polynomial poly,
      final List<VarALongName> vars) throws Exception {
    final Polynomial ansLocal = new Polynomial();
    ansLocal.setPrenum(poly.getPrenum());

    for (final ItemALongName i : poly.getItem()) {
      if (firstString(i.getName()) == SYMBOL_LEFT) {
        final Union bracket = new Union();
        bracket.turnStringToList(i.getName()
            .substring(1, i.getName().length() - 1));
        String bracketString = bracket.simply(vars);
        try {
          final int lpre = Integer.parseInt(bracketString);
          ansLocal.setPrenum((int) (ansLocal.getPrenum()
              * Math.pow(lpre, i.getPower())));
          if (poly.getPrenum() == 0) {
            return null;
          }
        } catch (NumberFormatException exception) {
          bracketString = "(".concat(bracketString).concat(")");
          ansLocal.getItem()
            .add(new ItemALongName(bracketString, i.getPower()));
        }

      } else {
        int flag = 0;
        for (final VarALongName v : vars) {
          if (v.getName().equals(i.getName())) {
            flag = 1;
            ansLocal.setPrenum((int) (ansLocal.getPrenum()
                * Math.pow(v.getValue(), i.getPower())));
            if (poly.getPrenum() == 0) {
              return null;
            }
          }
        }
        if (flag == 0) {
          ansLocal.getItem().add(i);
        }
      }
    }
    return ansLocal;
  }

  /**.
   * @param vars param1
   * @return string
   * @throws Exception will throw exception
   */
  public final String simply(final List<VarALongName> vars)
      throws Exception {
    final ArrayList<Polynomial> tmp = new ArrayList<Polynomial>();
    tmp.addAll(getAns());
    getAns().clear();
    for (final Polynomial i : tmp) {
      final Polynomial newPoly = symbolToNumber(i, vars);
      if (newPoly != null) {
        mergeSameItem(newPoly);
      }
    }

    return listToString();
  }

  /**.
   * @param poly param1
   * @param itALongName param2
   * @return int
   */
  private int power1OtNot(final Polynomial poly,
      final ItemALongName itALongName) {
    int power = 1;
    if (itALongName.getPower() == power) {
      poly.remove(itALongName);
    } else {
      power = itALongName.getPower();
      itALongName.setPower(itALongName.getPower() - 1);
    }
    return power;
  }


  /**.
   * @param poly param1
   * @param itAlongname param2
   * @param sym param3
   * @return Poly
   * @throws Exception will throw Exception
   */
  private Polynomial derivativeSingle(final Polynomial poly,
      final ItemALongName itAlongname, final String sym) throws Exception {
    final Polynomial ansLocal = new Polynomial();
    ansLocal.setPrenum(poly.getPrenum());
    ansLocal.getItem().addAll(poly.getItem());

    if (firstString(itAlongname.getName()) == SYMBOL_LEFT) {
      final Union bracket = new Union();
      bracket.turnStringToList(
          itAlongname.getName()
          .substring(1, (itAlongname.getName().length() - 1)));
      String bracketString = bracket.derivative(sym);
      try {
        final int pre = Integer.parseInt(bracketString);
        ansLocal.setPrenum((int) (ansLocal.getPrenum()
            * Math.pow(pre, itAlongname.getPower())));
        ansLocal.setPrenum(ansLocal.getPrenum()
            * power1OtNot(ansLocal, itAlongname));
        if (ansLocal.getPrenum() == 0) {
          return null;
        }
      } catch (NumberFormatException exception) {
        bracketString = "(".concat(bracketString).concat(")");
        final ItemALongName newItem = new ItemALongName(bracketString, 1);
        ansLocal.getItem().add(newItem);
        ansLocal.setPrenum(ansLocal.getPrenum()
            * power1OtNot(ansLocal, itAlongname));
        if (ansLocal.getPrenum() == 0) {
          return null;
        }
      }
    } else {
      if (itAlongname.getName().equals(sym)) {
        ansLocal.setPrenum(ansLocal.getPrenum()
            * power1OtNot(ansLocal, itAlongname));
        if (ansLocal.getPrenum() == 0) {
          return null;
        }
      } else {
        return null;
      }
    }
    return ansLocal;
  }


  /**.
   * @param poly param1
   * @param sym param2
   * @return ArrayList
   * @throws Exception will throw Exception
   */
  private List<Polynomial> derivativeToPoly(final Polynomial poly,
      final String sym) throws Exception {
    final ArrayList<Polynomial> ansLocal = new ArrayList<>();
    for (final ItemALongName it : poly.getItem()) {
      final Polynomial tmp = derivativeSingle(poly, it, sym);
      if (tmp != null) {
        ansLocal.add(tmp);
      }
    }
    return ansLocal;
  }

  /**.
   * @param sym param1
   * @return string
   * @throws Exception will throw exception
   */
  public final String derivative(final String sym) throws Exception {
    final ArrayList<Polynomial> tmp = new ArrayList<Polynomial>();
    tmp.addAll(getAns());
    getAns().clear();

    for (final Polynomial i : tmp) {
      final List<Polynomial> newPoly = derivativeToPoly(i, sym);
      for (final Polynomial p : newPoly) {
        mergeSameItem(p);
      }

    }

    return listToString();

  }

}

package com.company;

import java.util.ArrayList;


/**.
 * Created by forandroid on 16-9-23.
 */
public class Union implements Cloneable {

  /**.
   * Member variable: ans
   */
  private ArrayList<Poly> ans;

  /**.
   * @return the ans
   */
  public final ArrayList<Poly> getAns() {
    return ans;
  }

  /**.
   * @param ansParam the ans to set
   */
  public final void setAns(final ArrayList<Poly> ansParam) {
    this.ans = ansParam;
  }

  /**.
   * Construction method
   */
  Union() {
    setAns(new ArrayList<Poly>());
  }

  /**.
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException will throw CloneNotSupportedException
   */
  public final Object clone() throws CloneNotSupportedException {
    Union cloned = new Union();
    for (Poly p : getAns()) {
      cloned.getAns().add((Poly) p.clone());
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
    private String morpheme;

    /**.
     * Member variable: flag
     */
    private int flag;

    /**.
     * @return the morpheme
     */
    public String getMorpheme() {
      return morpheme;
    }

    /**.
     * @param morphemeParam the morpheme to set
     */
    public void setMorpheme(final String morphemeParam) {
      this.morpheme = morphemeParam;
    }

    /**.
     * @return the flag
     */
    public int getFlag() {
      return flag;
    }

    /**.
     * @param flagParam the flag to set
     */
    public void setFlag(final int flagParam) {
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
  private ArrayList<MorphemeObj> splitMorpheme(final String string)
      throws Exception {
    int num = 0;
    int startPoint = 0;
    int flag;
    if (firstString(string) == '-') {
      startPoint++;
      flag = -1;
    } else {
      flag = 1;
    }

    ArrayList<MorphemeObj> ansLocal = new ArrayList<MorphemeObj>();
    for (int i = startPoint; i < string.length(); i++) {
      if (nstring(string, i) == '(') {
        num++;
      } else if (nstring(string, i) == ')') {
        num--;
      } else if (nstring(string, i) == '+' && num == 0) {
        ansLocal.add(new MorphemeObj(string.substring(startPoint, i), flag));
        flag = 1;
        startPoint = i + 1;
      } else if (nstring(string, i) == '-' && num == 0
          && nstring(string, i - 1) != '*') {
        ansLocal.add(new MorphemeObj(string.substring(startPoint, i), flag));
        flag = -1;
        startPoint = i + 1;
      }
    }

    ansLocal.add(new MorphemeObj(string.substring(startPoint, string.length()),
        flag));
    if (num != 0) {
      throw new Exception();
    }
    return ansLocal;
  }

  /**.
   * @param poly param1
   * @return Poly
   */
  private Poly equals(final Poly poly) {
    for (Poly pre : getAns()) {
      if (pre.getIt().size() != poly.getIt().size()) {
        continue;
      }
      int flag = 0;
      for (int i = 0; i < poly.getIt().size(); i++) {
        if (!pre.getIt().get(i).getName().equals(poly.getIt().get(i).getName())
            || pre.getIt().get(i).getPower()
            != poly.getIt().get(i).getPower()) {
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
  private void mergeSameItem(final Poly tmp) {
    Poly poly = equals(tmp);
    if (poly != null) {
      poly.setPrenum(poly.getPrenum() + tmp.getPrenum());
      if (poly.getPrenum() == 0) {
        getAns().remove(poly);
      }
    } else {
      getAns().add(tmp);
    }
  }

  /**.
   * @param string param1
   * @throws Exception will throw exception
   */
  public final void turnStringToList(final String string) throws Exception {
    for (MorphemeObj i : splitMorpheme(string)) {
      Poly tmp = new Poly();
      tmp.morpheme(i.getMorpheme(), i.getFlag());
      mergeSameItem(tmp);
    }

  }

  /**.
   * @return String
   */
  public final String listToString() {
    if (getAns().isEmpty() || getAns().size() == 1
        && getAns().get(0).getPrenum() == 0) {
      return "0";
    }
    String string = "";
    for (Poly i : getAns()) {
      int prenum = i.getPrenum();
      if (prenum != 0) {
        if (prenum < 0) {
          string = string.concat(String.valueOf(prenum));
        } else if (string.equals("")) {
          string = String.valueOf(prenum);
        } else {
          string = string.concat("+").concat(String.valueOf(prenum));
        }

        for (Item var : i.getIt()) {
          if (var.getPower() == 1) {
            string = string.concat("*").concat(var.getName());
          } else {
            string = string.concat("*").concat(var.getName()).concat("^")
                .concat(String.valueOf(var.getPower()));
          }
        }
      }
    }
    return string;
  }


  /**.
   * @param poly param1
   * @param vars param2
   * @return Poly
   * @throws Exception will throw Exception
   */
  private Poly symbolToNumber(final Poly poly, final ArrayList<Var> vars)
      throws Exception {
    Poly ansLocal = new Poly();
    ansLocal.setPrenum(poly.getPrenum());

    for (Item i : poly.getIt()) {
      if (firstString(i.getName()) == '(') {
        Union bracket = new Union();
        bracket.turnStringToList(i.getName()
            .substring(1, i.getName().length() - 1));
        String bracketString = bracket.simply(vars);
        try {
          int lpre = Integer.parseInt(bracketString);
          ansLocal.setPrenum((int) (ansLocal.getPrenum()
              * Math.pow(lpre, i.getPower())));
          if (poly.getPrenum() == 0) {
            return null;
          }
        } catch (NumberFormatException exception) {
          bracketString = "(".concat(bracketString).concat(")");
          ansLocal.getIt().add(new Item(bracketString, i.getPower()));
        }

      } else {
        int flag = 0;
        for (Var v : vars) {
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
          ansLocal.getIt().add(i);
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
  public final String simply(final ArrayList<Var> vars) throws Exception {
    ArrayList<Poly> tmp = new ArrayList<Poly>();
    tmp.addAll(getAns());
    getAns().clear();

    for (Poly i : tmp) {
      Poly newPoly = symbolToNumber(i, vars);
      if (newPoly != null) {
        mergeSameItem(newPoly);
      }
    }

    return listToString();
  }

  /**.
   * @param poly param1
   * @param it param2
   * @return int
   */
  private int power1OtNot(final Poly poly, final Item it) {
    if (it.getPower() == 1) {
      poly.remove(it);
      return 1;
    }
    int power = it.getPower();
    it.setPower(it.getPower() - 1);
    return power;
  }


  /**.
   * @param poly param1
   * @param it param2
   * @param sym param3
   * @return Poly
   * @throws Exception will throw Exception
   */
  private Poly derivativeSingle(final Poly poly, final Item it,
      final String sym) throws Exception {
    Poly ansLocal = new Poly();
    ansLocal.setPrenum(poly.getPrenum());
    ansLocal.getIt().addAll(poly.getIt());

    if (firstString(it.getName()) != '(') {
      if (it.getName().equals(sym)) {
        ansLocal.setPrenum(ansLocal.getPrenum() * power1OtNot(ansLocal, it));
        if (ansLocal.getPrenum() == 0) {
          return null;
        }
      } else {
        return null;
      }
    } else {
      Union bracket = new Union();
      bracket.turnStringToList(it.getName()
          .substring(1, (it.getName().length() - 1)));
      String bracketString = bracket.derivative(sym);
      try {
        int pre = Integer.parseInt(bracketString);
        ansLocal.setPrenum((int) (ansLocal.getPrenum()
            * Math.pow(pre, it.getPower())));
        ansLocal.setPrenum(ansLocal.getPrenum() * power1OtNot(ansLocal, it));
        if (ansLocal.getPrenum() == 0) {
          return null;
        }
      } catch (NumberFormatException exception) {
        bracketString = "(".concat(bracketString).concat(")");
        Item newItem = new Item(bracketString, 1);
        ansLocal.getIt().add(newItem);
        ansLocal.setPrenum(ansLocal.getPrenum() * power1OtNot(ansLocal, it));
        if (ansLocal.getPrenum() == 0) {
          return null;
        }
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
  private ArrayList<Poly> derivativeToPoly(final Poly poly, final String sym)
      throws Exception {
    ArrayList<Poly> ansLocal = new ArrayList<>();
    for (Item it : poly.getIt()) {
      Poly tmp = derivativeSingle(poly, it, sym);
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
    ArrayList<Poly> tmp = new ArrayList<Poly>();
    tmp.addAll(getAns());
    getAns().clear();

    for (Poly i : tmp) {
      ArrayList<Poly> newPoly = derivativeToPoly(i, sym);
      for (Poly p : newPoly) {
        mergeSameItem(p);
      }

    }

    return listToString();

  }

}

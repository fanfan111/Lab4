package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**.
 * Created by forandroid.
 */

public final class Main {

  /**.
   * not called
   */
  private Main() {
    // not called
  }

  /**.
   * @param args param1
   */
  public static void main(final String[] args) {
    Scanner in = new Scanner(System.in);
    ArrayList<Var> vars = new ArrayList<>();
    Pattern simplify = Pattern.compile("^!simplify\\s(\\w+=\\d+\\s?)+");
    Pattern simplifyS = Pattern.compile("\\w+=\\d+");
    Pattern derivative = Pattern.compile("^!d/d\\s\\w+");

    final int derivativeConst = 5;

    String exp = in.nextLine();
    Union union = new Union();
    while (!exp.equals("exit")) {

      try {
        if (exp.charAt(0) != '!') {
          union.getAns().clear();
          exp = exp.replace(" ", "");
          union.turnStringToList(exp);
          System.out.println(union.listToString());
        } else {
          Matcher sm = simplify.matcher(exp);
          Matcher sms = simplifyS.matcher(exp);
          Matcher dm = derivative.matcher(exp);

          if (sm.find()) {
            Union tmpSim = (Union) union.clone();
            while (sms.find()) {
              String tmp = sms.group();
              int pos = tmp.indexOf('=');
              String name = tmp.substring(0, pos);
              int power = Integer.parseInt(tmp.substring(pos + 1));
              vars.add(new Var(name, power));
            }
            String fin = tmpSim.simply(vars);
            if (tmpSim.listToString().equals(union.listToString())) {
              System.out.println("no value has been found "
                  + "in early expression!");
            } else {
              System.out.println(fin);
            }
            vars.clear();
          } else if (dm.find()) {

            String tmp = exp.substring(derivativeConst);
            Union tmpSim;
            tmpSim = (Union) union.clone();
            String fin = tmpSim.derivative(tmp);
            if (tmpSim.listToString().equals(union.listToString())) {
              System.out.println("no value has been found "
                  + "in early expression!");
            } else {
              System.out.println(fin);
            }
          } else {
            System.out.println("wrong command!");
          }
        }
      } catch (Exception exception) {
        System.out.println("wrong format");

      }
      exp = in.nextLine();
    }

    in.close();
  }
}

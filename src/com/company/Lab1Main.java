package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**.
 * Created by forandroid.
 */

public final class Lab1Main {

  /**.
   * derivative string offset value
   */
  private static final int DERIV_OFFSET = 5;

  /**.
   * command flag: !
   */
  private static final char COMMAND_FLAG = '!';

  /**.
   * not called
   */
  private Lab1Main() {
    // not called
  }

  /**.
   * @param args param1
   */
  public static void main(final String[] args) {
    final Scanner inShortFix = new Scanner(System.in);
    final ArrayList<VarALongName> vars = new ArrayList<>();
    final Pattern simplify = Pattern.compile("^!simplify\\s(\\w+=\\d+\\s?)+");
    final Pattern simplifyS = Pattern.compile("\\w+=\\d+");
    final Pattern derivative = Pattern.compile("^!d/d\\s\\w+");

    String exp = inShortFix.nextLine();
    final Union union = new Union();
    while (!"exit".equals(exp)) {

      try {
        if (exp.charAt(0) == COMMAND_FLAG) {
          final Matcher smShortFix = simplify.matcher(exp);
          final Matcher sms = simplifyS.matcher(exp);
          final Matcher dmShortFix = derivative.matcher(exp);

          if (smShortFix.find()) {
            final Union tmpSim = (Union) union.copy();
            while (sms.find()) {
              final String tmp = sms.group();
              final int pos = tmp.indexOf('=');
              final String name = tmp.substring(0, pos);
              final int power = Integer.parseInt(tmp.substring(pos + 1));
              vars.add(new VarALongName(name, power));
            }
            final String fin = tmpSim.simply(vars);
            if (tmpSim.listToString().equals(union.listToString())) {
              System.out.println("no value has been found "
                  + "in early expression!");
            } else {
              System.out.println(fin);
            }
            vars.clear();
          } else if (dmShortFix.find()) {

            final String tmp = exp.substring(DERIV_OFFSET);
            Union tmpSim;
            tmpSim = (Union) union.copy();
            final String fin = tmpSim.derivative(tmp);
            if (tmpSim.listToString().equals(union.listToString())) {
              System.out.println("no value has been found "
                  + "in early expression!");
            } else {
              System.out.println(fin);
            }
          } else {
            System.out.println("wrong command!");
          }
        } else {
          union.getAns().clear();
          exp = exp.replace(" ", "");
          union.turnStringToList(exp);
          System.out.println(union.listToString());
        }
      } catch (Exception exception) {
        System.out.println("wrong format");
      }
      exp = inShortFix.nextLine();
    }

    inShortFix.close();
  }
}

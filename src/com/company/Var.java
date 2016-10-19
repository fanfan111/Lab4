package com.company;

/**.
 * Created by forandroid on 16-9-23.
 */
public class Var {

  /**.
   * Member variable: name
   */
  private String name;

  /**.
   * Member variable: value
   */
  private int value;

  /**.
   * @param nameInput param1
   * @param valueInput param2
   */
  Var(final String nameInput, final int valueInput) {
    this.setName(nameInput);
    this.setValue(valueInput);
  }

  /**.
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**.
   * @param nameInput the name to set
   */
  public final void setName(final String nameInput) {
    this.name = nameInput;
  }

  /**.
   * @return the value
   */
  public final int getValue() {
    return value;
  }

  /**.
   * @param valueInput the value to set
   */
  public final void setValue(final int valueInput) {
    this.value = valueInput;
  }

}

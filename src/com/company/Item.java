package com.company;

/**.
 * Created by forandroid.
 */
public class Item implements Cloneable {

  /**.
   * Member variable: name
   */
  private String name;

  /**.
   * Member variable: power
   */
  private int power;

  /**.
   * @param nameInput param1
   * @param powerInput param2
   */
  Item(final String nameInput, final int powerInput) {
    this.setName(nameInput);
    this.setPower(powerInput);
  }

  /**.
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException will throw CloneNotSupportedException
   */
  public final Object clone() throws CloneNotSupportedException {
    Item it = new Item(getName(), getPower());
    return it;
  }

  /**.
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**.
   * @param nameParam the name to set
   */
  public final void setName(final String nameParam) {
    this.name = nameParam;
  }

  /**.
   * @return the power
   */
  public final int getPower() {
    return power;
  }

  /**.
   * @param powerParam the power to set
   */
  public final void setPower(final int powerParam) {
    this.power = powerParam;
  }
}

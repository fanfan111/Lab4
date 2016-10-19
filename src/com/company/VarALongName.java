package com.company;

/**.
 * Created by forandroid on 16-9-23.
 */
public class VarALongName {

  /**.
   * Member variable: name
   */
  private transient String name;

  /**.
   * Member variable: value
   */
  private transient int value;

  /**.
   * @param nameInput param1
   * @param valueInput param2
   */
  VarALongName(final String nameInput, final int valueInput) {
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
   * @param nameParam the name to set
   */
  public final void setName(final String nameParam) {
    this.name = nameParam;
  }

  /**.
   * @return the value
   */
  public final int getValue() {
    return value;
  }

  /**.
   * @param valueParam the value to set
   */
  public final void setValue(final int valueParam) {
    this.value = valueParam;
  }

}
